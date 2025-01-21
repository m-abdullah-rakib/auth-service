package com.dp.authservice.service;

import com.dp.authservice.entity.User;
import com.dp.authservice.exception.UserNotFoundException;
import com.dp.authservice.mapper.UserMapper;
import com.dp.authservice.repository.UserRepository;
import com.dp.authservice.request.UserRequest;
import com.dp.authservice.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        user.setCreatedAt(LocalDateTime.now());

        userRequest = new UserRequest("test@test.com", "password");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("test@test.com");
        userResponse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createUser_ValidRequest_ReturnsUserResponse() {
        when(userMapper.toEntity(userRequest)).thenReturn(user);
//        when(passwordEncoder.encode(userRequest.password())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.createUser(userRequest);

        assertNotNull(result);
        assertEquals(userResponse, result);

        verify(userRepository, times(1)).save(user);
//        verify(passwordEncoder, times(1)).encode("password");
        verify(userMapper, times(1)).toEntity(userRequest);
        verify(userMapper, times(1)).toResponse(user);
    }

//    @Test
//    void createUser_NullRequest_ThrowsException() {
//        assertThrows(NullPointerException.class, () -> userService.createUser(null));
//        verify(userRepository, times(0)).save(any());
//        verify(passwordEncoder, times(0)).encode(any());
//        verify(userMapper, times(0)).toEntity(any());
//        verify(userMapper, times(0)).toResponse(any());
//    }

//    @Test
//    void createUser_InvalidEmail_ThrowsException() {
//        UserRequest request = new UserRequest("invalid", "password");
//        when(userMapper.toEntity(request)).thenReturn(user);
//        assertThrows(NullPointerException.class, () -> userService.createUser(request));
//        verify(userRepository, times(0)).save(any());
//        verify(passwordEncoder, times(0)).encode(any());
//        verify(userMapper, times(0)).toEntity(any());
//        verify(userMapper, times(0)).toResponse(any());
//    }

//    @Test
//    void createUser_InvalidPassword_ThrowsException() {
//        UserRequest request = new UserRequest("test@test.com", null);
//        when(userMapper.toEntity(request)).thenReturn(user);
//        assertThrows(NullPointerException.class, () -> userService.createUser(request));
//        verify(userRepository, times(0)).save(any());
//        verify(passwordEncoder, times(0)).encode(any());
//        verify(userMapper, times(0)).toEntity(any());
//        verify(userMapper, times(0)).toResponse(any());
//    }

    @Test
    void createUser_RepositoryThrowsException_ThrowsException() {
        when(userMapper.toEntity(userRequest)).thenReturn(user);
//        when(passwordEncoder.encode(userRequest.password())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> userService.createUser(userRequest));
        verify(userRepository, times(1)).save(any());
//        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userMapper, times(1)).toEntity(any(UserRequest.class));
        verify(userMapper, times(0)).toResponse(any(User.class));
    }

    @Test
    void getUserById_ExistingId_ReturnsUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(userResponse, result);

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toResponse(user);

    }

    @Test
    void getUserById_RepositoryThrowsException_ThrowsException() {
        when(userRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(any());
        verify(userMapper, times(0)).toResponse(any());
    }

    @Test
    void getUserById_NonExistingId_ThrowsUserNotFoundException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(2L));
        verify(userRepository, times(1)).findById(2L);
        verify(userMapper, times(0)).toResponse(any());
    }
}
