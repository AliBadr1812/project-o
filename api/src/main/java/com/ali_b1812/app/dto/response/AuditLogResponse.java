package com.ali_b1812.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogResponse {

    private Long   id;
    /** CREATE | UPDATE | DELETE | LOGIN | LOGOUT | EXPORT | APPROVE | REJECT | VIEW */
    private String action;
    /** PRODUCT | ORDER | CUSTOMER | CATEGORY | DISCOUNT | RETURN | SEGMENT | USER | SYSTEM */
    private String entityType;
    private Long   entityId;
    private String entityName;
    private String performedBy;
    private String details;
    private String ipAddress;
    private String timestamp;
}
