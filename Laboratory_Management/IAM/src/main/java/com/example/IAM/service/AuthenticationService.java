package com.example.IAM.service;

import com.example.IAM.common.exception.AppException;
import com.example.IAM.common.exception.ErrorCode;
import com.example.IAM.dto.request.AuthenticationRequest;
import com.example.IAM.dto.request.IntrospectRequest;
import com.example.IAM.dto.request.LogoutRequest;
import com.example.IAM.dto.respone.AuthenticationRespone;
import com.example.IAM.dto.respone.IntrospectResponse;
import com.example.IAM.dto.respone.JwtInfo;
import com.example.IAM.entity.RedisToken;
import com.example.IAM.entity.RolePrivilege;
import com.example.IAM.entity.User;
import com.example.IAM.repository.RedisRepository;
import com.example.IAM.repository.RolePrivilegeRepository;
import com.example.IAM.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    final RedisRepository redisRepository;
    final UserRepository userRepository;
    final RolePrivilegeRepository rolePrivilegeRepository;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerKey;

    private int MAX_FAILED_ATTEMPTS = 5;

    private boolean isLocked(User user) {
        return Boolean.TRUE.equals(user.getIsLocked());
    }

    private void FailedLogin(User user) {
        int attempts = user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts();
        attempts++;
        user.setFailedLoginAttempts(attempts);
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setIsLocked(true);
        }
        userRepository.save(user);
    }

    private void SuccessLogin(User user) {
        user.setFailedLoginAttempts(0);
        user.setLastLoginDate(OffsetDateTime.now());
        userRepository.save(user);
    }

    public IntrospectResponse introspectRespone(IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (AppException | JOSEException | ParseException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    public AuthenticationRespone authenticate(AuthenticationRequest request) {

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new AppException(ErrorCode.USER_INACTIVE);
        }

        if (isLocked(user)) {
            throw new AppException(ErrorCode.ACCOUNT_LOCKED);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            FailedLogin(user);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        SuccessLogin(user);
        Set<String> roleCodes = resolveRoleCodes(user);
        Set<String> privilegeCodes = resolvePrivilegeCodes(user);

        var token = generateToken(user, roleCodes, privilegeCodes);
        return AuthenticationRespone.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .authenticated(true)
                .mustChangePassword(Boolean.TRUE.equals(user.getMustChangePassword()))
                .roleCodes(roleCodes)
                .privilegeCodes(privilegeCodes)
                .build();
    }

    private String generateToken(User user, Set<String> roleCodes, Set<String> privilegeCodes) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("Laboratory Management")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", user.getId())
                .claim("userName", user.getFullName())
                .claim("role", buildScope(user))
                .claim("roleCodes", roleCodes)
                .claim("privilegeCodes", privilegeCodes)
                .claim("mustChangePassword", user.mustChangePassword())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> stringJoiner.add("ROLE_" + role.getName()));
        return stringJoiner.toString();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());
        String jwtId = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        long ttlInSeconds = (expiryTime.getTime() - new Date().getTime()) / 1000;
        Objects.requireNonNull(jwtId, "JWT id must not be null");
        RedisToken redisToken = RedisToken.builder()
                .jwtId(jwtId)
                .expiredTime(ttlInSeconds)
                .build();
        RedisToken persisted = redisRepository.save(redisToken);
        Objects.requireNonNull(persisted, "Failed to persist logout token");
    }

    public JwtInfo parseToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Objects.requireNonNull(jwtId, "JWT id must not be null");
        Date issueTime = signedJWT.getJWTClaimsSet().getIssueTime();
        Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return JwtInfo.builder()
                .jwtId(jwtId)
                .issueTime(issueTime)
                .expiredTime(expiredTime)
                .build();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verifed = signedJWT.verify(verifier);

        if (!(verifed && expityTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Objects.requireNonNull(jwtId, "JWT id must not be null");
        if (redisRepository.existsById(jwtId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    private Set<String> resolveRoleCodes(User user) {
        Set<String> roleCodes = new HashSet<>();
        if (CollectionUtils.isEmpty(user.getRoles())) {
            return roleCodes;
        }
        user.getRoles().stream()
                .filter(Objects::nonNull)
                .map(role -> role.getRoleCode())
                .filter(Objects::nonNull)
                .forEach(roleCodes::add);
        return roleCodes;
    }

    private Set<String> resolvePrivilegeCodes(User user) {
        Set<String> privilegeCodes = new HashSet<>();
        if (CollectionUtils.isEmpty(user.getRoles())) {
            return privilegeCodes;
        }

        user.getRoles().stream()
                .filter(Objects::nonNull)
                .forEach(role -> {
                    List<RolePrivilege> rolePrivileges = rolePrivilegeRepository.findByRole(role);
                    if (CollectionUtils.isEmpty(rolePrivileges)) {
                        return;
                    }
                    rolePrivileges.stream()
                            .filter(rp -> Boolean.TRUE.equals(rp.getPermitted()))
                            .map(RolePrivilege::getPrivilege)
                            .filter(Objects::nonNull)
                            .map(Enum::name)
                            .forEach(privilegeCodes::add);
                });

        return privilegeCodes;
    }
}
