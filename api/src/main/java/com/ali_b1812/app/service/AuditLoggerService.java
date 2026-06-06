package com.ali_b1812.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuditLoggerService {
    
    private final ObjectMapper objectMapper;
    
    public AuditLoggerService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    /**
     * Log user activity with structured data
     */
    public void logUserActivity(String action, Long userId, String username, 
                                String details, Object oldValue, Object newValue) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("timestamp", LocalDateTime.now());
        logData.put("action", action);
        logData.put("userId", userId);
        logData.put("username", username);
        logData.put("details", details);
        logData.put("oldValue", oldValue);
        logData.put("newValue", newValue);
        logData.put("type", "USER_ACTIVITY");
        
        try {
            log.info("USER_ACTIVITY: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            log.error("Failed to serialize log data", e);
        }
    }
    
    /**
     * Log product activity
     */
    public void logProductActivity(String action, Long productId, String productName,
                                   Long userId, String details, Object oldValue, Object newValue) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("timestamp", LocalDateTime.now());
        logData.put("action", action);
        logData.put("productId", productId);
        logData.put("productName", productName);
        logData.put("userId", userId);
        logData.put("details", details);
        logData.put("oldValue", oldValue);
        logData.put("newValue", newValue);
        logData.put("type", "PRODUCT_ACTIVITY");
        
        try {
            log.info("PRODUCT_ACTIVITY: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            log.error("Failed to serialize log data", e);
        }
    }

    /**
     * Log order activity
     */
    public void logOrderActivity(String action, Long orderId, String customerName,
                                 Long userId, String details, Object oldValue, Object newValue) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("timestamp", LocalDateTime.now());
        logData.put("action", action);
        logData.put("orderId", orderId);
        logData.put("customerName", customerName);
        logData.put("userId", userId);
        logData.put("details", details);
        logData.put("oldValue", oldValue);
        logData.put("newValue", newValue);
        logData.put("type", "ORDER_ACTIVITY");
        
        try {
            log.info("ORDER_ACTIVITY: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            log.error("Failed to serialize log data", e);
        }
    }

    /**
     * Log category activity
     */
    public void logCategoryActivity(String action, Long categoryId, String categoryName,
                                 Long userId, String details, Object oldValue, Object newValue) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("timestamp", LocalDateTime.now());
        logData.put("action", action);
        logData.put("categoryId", categoryId);
        logData.put("categoryName", categoryName);
        logData.put("userId", userId);
        logData.put("details", details);
        logData.put("oldValue", oldValue);
        logData.put("newValue", newValue);
        logData.put("type", "CATEGORY_ACTIVITY");
        
        try {
            log.info("CATEGORY_ACTIVITY: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            log.error("Failed to serialize log data", e);
        }
    }

    /**
     * Log customer activity
     */
    public void logCustomerActivity(String action, Long customerId, String customerName,
                                 Long userId, String details, Object oldValue, Object newValue) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("timestamp", LocalDateTime.now());
        logData.put("action", action);
        logData.put("customerId", customerId);
        logData.put("customerName", customerName);
        logData.put("userId", userId);
        logData.put("details", details);
        logData.put("oldValue", oldValue);
        logData.put("newValue", newValue);
        logData.put("type", "CUSTOMER_ACTIVITY");
        
        try {
            log.info("CUSTOMER_ACTIVITY: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            log.error("Failed to serialize log data", e);
        }
    }
    
    /**
     * Log authentication events (login, logout, failed attempts)
     */
    public void logAuthEvent(String event, Long userId, String username, 
                             String ipAddress, Boolean success, String reason) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("timestamp", LocalDateTime.now());
        logData.put("event", event);
        logData.put("userId", userId);
        logData.put("username", username);
        logData.put("ipAddress", ipAddress);
        logData.put("success", success);
        logData.put("reason", reason);
        logData.put("type", "AUTH_EVENT");
        
        try {
            log.info("AUTH_EVENT: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            log.error("Failed to serialize log data", e);
        }
    }
    
    /**
     * Log API requests
     */
    public void logApiRequest(String method, String path, Long userId, 
                              String ipAddress, Integer statusCode, Long duration) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("timestamp", LocalDateTime.now());
        logData.put("method", method);
        logData.put("path", path);
        logData.put("userId", userId);
        logData.put("ipAddress", ipAddress);
        logData.put("statusCode", statusCode);
        logData.put("duration", duration);
        logData.put("type", "API_REQUEST");
        
        try {
            log.info("API_REQUEST: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            log.error("Failed to serialize log data", e);
        }
    }
    
    /**
     * Log errors with context
     */
    public void logError(String component, String operation, String error, 
                         String details, Long userId) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("timestamp", LocalDateTime.now());
        logData.put("component", component);
        logData.put("operation", operation);
        logData.put("error", error);
        logData.put("details", details);
        logData.put("userId", userId);
        logData.put("type", "ERROR");
        
        try {
            log.error("ERROR: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            log.error("Failed to serialize log data", e);
        }
    }
}