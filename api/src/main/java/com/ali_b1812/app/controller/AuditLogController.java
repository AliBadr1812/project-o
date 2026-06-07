package com.ali_b1812.app.controller;

import com.ali_b1812.app.dto.response.ApiResponse;
import com.ali_b1812.app.dto.response.AuditLogResponse;
import com.ali_b1812.app.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Audit Logs", description = "System audit log viewer")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @Operation(summary = "Get audit logs with optional filters")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getLogs(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String performedBy) {
        return ResponseEntity.ok(ApiResponse.success("OK",
                auditLogService.getLogs(action, entityType, performedBy)));
    }
}
