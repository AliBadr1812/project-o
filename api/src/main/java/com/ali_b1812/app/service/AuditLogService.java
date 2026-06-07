package com.ali_b1812.app.service;

import com.ali_b1812.app.dto.response.AuditLogResponse;
import com.ali_b1812.app.mockdata.AuditLogMockData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {

    private final AuditLogMockData mockData;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public List<AuditLogResponse> getLogs(String action, String entityType, String performedBy) {
        return mockData.getAll().stream()
                .filter(l -> action      == null || l.getAction().equalsIgnoreCase(action))
                .filter(l -> entityType  == null || l.getEntityType().equalsIgnoreCase(entityType))
                .filter(l -> performedBy == null || l.getPerformedBy().equalsIgnoreCase(performedBy))
                .collect(Collectors.toList());
    }

    public void record(String action, String entityType, Long entityId, String entityName,
                       String performedBy, String details, String ipAddress) {
        AuditLogResponse entry = AuditLogResponse.builder()
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .entityName(entityName)
                .performedBy(performedBy)
                .details(details)
                .ipAddress(ipAddress)
                .timestamp(LocalDateTime.now().format(FMT))
                .build();
        mockData.append(entry);
    }
}
