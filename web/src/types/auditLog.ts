export interface AuditLog {
  id:          number;
  /** CREATE | UPDATE | DELETE | LOGIN | LOGOUT | EXPORT | APPROVE | REJECT | VIEW */
  action:      string;
  /** PRODUCT | ORDER | CUSTOMER | CATEGORY | DISCOUNT | RETURN | SEGMENT | USER | SYSTEM */
  entityType:  string;
  entityId:    number | null;
  entityName:  string;
  performedBy: string;
  details:     string;
  ipAddress:   string;
  timestamp:   string;
}
