package com.dp.authservice.service;

import com.dp.authservice.entity.User;
import com.dp.authservice.exception.UserNotFoundException;
import com.dp.authservice.mapper.UserMapper;
import com.dp.authservice.repository.UserRepository;
import com.dp.authservice.request.UserRequest;
import com.dp.authservice.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse createUser(UserRequest request) {
        User user = userMapper.toEntity(request);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }
}
