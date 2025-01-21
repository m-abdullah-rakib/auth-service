package com.dp.authservice.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private LocalDateTime createdAt;
}
