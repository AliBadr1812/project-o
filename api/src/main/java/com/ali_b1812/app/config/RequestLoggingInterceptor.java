package com.ali_b1812.app.config;

import com.ali_b1812.app.service.AuditLoggerService;
import com.ali_b1812.app.util.UserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {
    
    private final AuditLoggerService auditLogger;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
                             Object handler) throws Exception {
        // Start timer
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                           Object handler, ModelAndView modelAndView) throws Exception {
        // Nothing needed here
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) throws Exception {
        Long startTime = (Long) request.getAttribute("startTime");
        Long duration = System.currentTimeMillis() - startTime;
        
        // Get current user if authenticated
        Long userId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal) {
            userId = ((UserPrincipal) auth.getPrincipal()).getId();
        }
        
        // Log the request
        auditLogger.logApiRequest(
            request.getMethod(),
            request.getRequestURI(),
            userId,
            request.getRemoteAddr(),
            response.getStatus(),
            duration
        );
        
        // Log errors if any
        if (ex != null) {
            auditLogger.logError(
                "API",
                request.getMethod() + " " + request.getRequestURI(),
                ex.getMessage(),
                "Exception occurred",
                userId
            );
        }
    }
}