package com.example.IAM.publicApi.service;

import com.example.IAM.dto.respone.BaseSingleResultResponse;
import com.example.IAM.entity.Privilege;
import com.example.IAM.entity.Role;
import com.example.IAM.entity.RolePrivilege;
import com.example.IAM.entity.User;
import com.example.IAM.publicApi.model.request.AuthenticationRequest;
import com.example.IAM.publicApi.model.response.AuthenticationResponse;
import com.example.IAM.publicApi.model.response.AuthenticationResponseData;
import com.example.IAM.repository.RolePrivilegeRepository;
import com.example.IAM.repository.UserRepository;
import com.example.IAM.utils.constant.ErrorInfo;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class ExAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolePrivilegeRepository rolePrivilegeRepository;

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Transactional(readOnly = true)
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String token = request.getToken();
        if (token == null) {
            return buildErrorResponse(ErrorInfo.UNAUTHORIZED);
        }

        JWTClaimsSet claims = verifyToken(token);
        if (claims == null) {
            return buildErrorResponse(ErrorInfo.UNAUTHORIZED);
        }

        String userIdClaim = Objects.toString(claims.getClaim("userId"), null);
        if (userIdClaim == null) {
            return buildErrorResponse(ErrorInfo.UNAUTHORIZED);
        }

        UUID userUuid;
        try {
            userUuid = UUID.fromString(userIdClaim);
        } catch (IllegalArgumentException ex) {
            return buildErrorResponse(ErrorInfo.UNAUTHORIZED);
        }

        User user = userRepository.findById(Objects.requireNonNull(userUuid)).orElse(null);
        if (Objects.isNull(user)) {
            return buildErrorResponse(ErrorInfo.UNAUTHORIZED);
        }

        Set<Role> roles = user.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return buildErrorResponse(ErrorInfo.UNAUTHORIZED);
        }

        Set<String> privilegeCodes = new HashSet<>();
        Set<String> roleCodes = new HashSet<>();

        for (Role role : roles) {
            if (role == null) {
                continue;
            }
            roleCodes.add(role.getRoleCode());

            List<RolePrivilege> rolePrivileges = rolePrivilegeRepository.findByRole(role);
            if (CollectionUtils.isEmpty(rolePrivileges)) {
                continue;
            }

            rolePrivileges.stream()
                    .filter(rp -> Boolean.TRUE.equals(rp.getPermitted()))
                    .map(RolePrivilege::getPrivilege)
                    .filter(Objects::nonNull)
                    .map(Privilege::name)
                    .forEach(privilegeCodes::add);
        }

        if (privilegeCodes.isEmpty()) {
            return buildErrorResponse(ErrorInfo.UNAUTHORIZED);
        }

        AuthenticationResponseData data = new AuthenticationResponseData();
        data.setUserId(user.getId().toString());
        data.setUsername(user.getUsername());
        data.setEmail(user.getEmail());
        data.setRoleCode(String.join(",", roleCodes));
        data.setPrivilegeCodes(privilegeCodes);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setData(data);
        return response;
    }

    private JWTClaimsSet verifyToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            MACVerifier verifier = new MACVerifier(signerKey.getBytes());
            boolean verified = signedJWT.verify(verifier);
            Instant expiration = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            if (!verified || expiration.isBefore(Instant.now())) {
                return null;
            }
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException | JOSEException ex) {
            return null;
        }
    }

    private AuthenticationResponse buildErrorResponse(ErrorInfo errorInfo) {
        AuthenticationResponse response = new AuthenticationResponse();
        BaseSingleResultResponse.ErrorInfo error = new BaseSingleResultResponse.ErrorInfo();
        error.setCode(errorInfo.getCode());
        error.setMessage(errorInfo.getText());
        response.setError(error);
        return response;
    }
}
