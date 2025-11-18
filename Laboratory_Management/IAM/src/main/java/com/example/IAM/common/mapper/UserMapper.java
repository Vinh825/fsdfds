package com.example.IAM.common.mapper;

import com.example.IAM.dto.request.CreateUserRequest;
import com.example.IAM.dto.respone.UserResponse;
import com.example.IAM.entity.Role;
import com.example.IAM.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toUser(CreateUserRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "identityNumber", source = "identityNumber")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "roleCodes", expression = "java(mapRoleCodes(user))")
    @Mapping(target = "locked", source = "isLocked")
    @Mapping(target = "active", source = "isActive")
    UserResponse toUserResponse(User user);

    default List<String> mapRoleCodes(User user) {
        if (user == null || user.getRoles() == null || user.getRoles().isEmpty()) {
            return Collections.emptyList();
        }
        return user.getRoles().stream()
                .filter(Objects::nonNull)
                .map(Role::getRoleCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}