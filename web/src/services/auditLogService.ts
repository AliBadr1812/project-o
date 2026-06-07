import api from './api';
import type { AuditLog } from '@/types/auditLog';

export interface AuditLogFilters {
  action?:      string;
  entityType?:  string;
  performedBy?: string;
}

export class AuditLogService {
  private readonly base = '/audit-logs';

  async getLogs(filters: AuditLogFilters = {}): Promise<AuditLog[]> {
    const params: Record<string, string> = {};
    if (filters.action)      params.action      = filters.action;
    if (filters.entityType)  params.entityType  = filters.entityType;
    if (filters.performedBy) params.performedBy = filters.performedBy;
    const { data } = await api.get<AuditLog[]>(this.base, { params });
    return data;
  }
}

export const auditLogService = new AuditLogService();
