package com.ali_b1812.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String path;
    private Integer status;
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
            true, 
            message, 
            data, 
            LocalDateTime.now(), 
            null, 
            200
        );
    }
    
    public static <T> ApiResponse<T> error(String message, Integer status) {
        return new ApiResponse<>(
            false, 
            message, 
            null, 
            LocalDateTime.now(), 
            null, 
            status
        );
    }
    
    public static <T> ApiResponse<T> created(String message, T data, String path) {
        return new ApiResponse<>(
            true,
            message,
            data,
            LocalDateTime.now(),
            path,
            201
        );
    }
}