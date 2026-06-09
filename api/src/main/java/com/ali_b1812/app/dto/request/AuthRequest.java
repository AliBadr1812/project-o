package com.ali_b1812.app.dto.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
