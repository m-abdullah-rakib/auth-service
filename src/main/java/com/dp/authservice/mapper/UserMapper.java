package com.dp.authservice.mapper;

import com.dp.authservice.entity.User;
import com.dp.authservice.request.UserRequest;
import com.dp.authservice.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Mapping(target = "password", expression = "java(encodePassword(request.password()))")
    public abstract User toEntity(UserRequest request);

    public abstract UserResponse toResponse(User user);

    protected String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
}
