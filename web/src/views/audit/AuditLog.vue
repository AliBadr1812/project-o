<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Audit Log</h1>
        <p class="page-subtitle">Track all administrative actions and system events</p>
      </div>
      <div class="flex items-center gap-3">
        <button @click="loadLogs" class="btn-glass text-sm">
          <i class="fas fa-sync-alt text-xs mr-1"></i>Refresh
        </button>
        <button @click="exportCsv" class="btn-glass text-sm">
          <i class="fas fa-download text-xs mr-1"></i>Export CSV
        </button>
      </div>
    </div>

    <!-- Filters -->
    <div class="glass-card px-6 py-4 flex flex-wrap items-center gap-3">
      <div class="relative flex-1 min-w-[180px]">
        <i class="fas fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted)"></i>
        <input v-model="search" type="text" placeholder="Search logs…" class="glass-input pl-9 text-sm w-full">
      </div>
      <select v-model="filterAction" class="glass-input text-sm" style="width:130px;">
        <option value="">All Actions</option>
        <option v-for="a in actions" :key="a" :value="a">{{ a }}</option>
      </select>
      <select v-model="filterEntity" class="glass-input text-sm" style="width:140px;">
        <option value="">All Entities</option>
        <option v-for="e in entityTypes" :key="e" :value="e">{{ e }}</option>
      </select>
      <button @click="clearFilters" class="btn-glass text-sm">
        <i class="fas fa-xmark text-xs mr-1"></i>Clear
      </button>
    </div>

    <!-- Error -->
    <div v-if="error" class="glass-card px-6 py-8 text-center">
      <i class="fas fa-triangle-exclamation text-2xl mb-2" style="color: var(--ni-orange);"></i>
      <p class="text-sm mb-3" style="color: var(--text-primary);">{{ error }}</p>
      <button @click="loadLogs" class="btn-glass text-sm">Try again</button>
    </div>

    <!-- Log Table -->
    <div v-else class="glass-card overflow-hidden">
      <div class="px-6 py-3 flex items-center justify-between" style="border-bottom: 1px solid var(--glass-border);">
        <span class="text-sm" style="color: var(--text-muted);">
          Showing {{ filtered.length }} of {{ logs.length }} events
        </span>
      </div>
      <div class="overflow-x-auto">
        <table class="glass-table w-full">
          <thead>
            <tr>
              <th>Timestamp</th>
              <th>Action</th>
              <th>Entity</th>
              <th>Target</th>
              <th>Details</th>
              <th>User</th>
              <th>IP</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="loading">
              <td colspan="7" class="py-10 text-center">
                <i class="fas fa-spinner fa-spin mr-2" style="color: var(--text-muted);"></i>
                <span style="color: var(--text-muted);">Loading…</span>
              </td>
            </tr>
            <tr v-for="log in paginated" :key="log.id">
              <td>
                <span class="text-xs font-mono" style="color: var(--text-muted);">
                  {{ formatDateTime(log.timestamp) }}
                </span>
              </td>
              <td>
                <span class="badge text-[10px]" :class="actionClass(log.action)">
                  <i :class="actionIcon(log.action)" class="mr-1 text-[9px]"></i>
                  {{ log.action }}
                </span>
              </td>
              <td>
                <span class="badge badge-info text-[10px]">{{ log.entityType }}</span>
              </td>
              <td>
                <div>
                  <p class="text-sm" style="color: var(--text-primary);">{{ log.entityName }}</p>
                  <p v-if="log.entityId" class="text-xs font-mono" style="color: var(--text-muted);">#{{ log.entityId }}</p>
                </div>
              </td>
              <td>
                <span class="text-sm" style="color: var(--text-secondary);">{{ log.details }}</span>
              </td>
              <td>
                <span class="text-sm" style="color: var(--text-primary);">{{ log.performedBy }}</span>
              </td>
              <td>
                <span class="text-xs font-mono" style="color: var(--text-muted);">{{ log.ipAddress }}</span>
              </td>
            </tr>
            <tr v-if="!loading && filtered.length === 0">
              <td colspan="7" class="py-12 text-center">
                <i class="fas fa-scroll text-3xl mb-3" style="color: var(--text-muted);"></i>
                <p class="text-sm" style="color: var(--text-muted);">No log entries found.</p>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <!-- Pagination -->
      <div v-if="totalPages > 1" class="px-6 py-3 flex items-center justify-between"
        style="border-top: 1px solid var(--glass-border);">
        <button @click="page--" :disabled="page <= 1" class="btn-glass text-sm">
          <i class="fas fa-chevron-left text-xs mr-1"></i>Prev
        </button>
        <span class="text-sm" style="color: var(--text-muted);">Page {{ page }} / {{ totalPages }}</span>
        <button @click="page++" :disabled="page >= totalPages" class="btn-glass text-sm">
          Next<i class="fas fa-chevron-right text-xs ml-1"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { auditLogService } from '@/services/auditLogService';
import { exportToCsv, datestampedFilename } from '@/utils/csvExport';
import type { AuditLog } from '@/types/auditLog';

const logs    = ref<AuditLog[]>([]);
const loading = ref(false);
const error   = ref<string | null>(null);

const search       = ref('');
const filterAction = ref('');
const filterEntity = ref('');
const page         = ref(1);
const perPage      = 15;

const actions     = ['CREATE', 'UPDATE', 'DELETE', 'LOGIN', 'LOGOUT', 'EXPORT', 'APPROVE', 'REJECT', 'VIEW'];
const entityTypes = ['PRODUCT', 'ORDER', 'CUSTOMER', 'CATEGORY', 'DISCOUNT', 'RETURN', 'SEGMENT', 'USER', 'SYSTEM'];

onMounted(loadLogs);

// Reset page when filters change
watch([search, filterAction, filterEntity], () => { page.value = 1; });

async function loadLogs() {
  loading.value = true;
  error.value   = null;
  try {
    logs.value = await auditLogService.getLogs({
      action:     filterAction.value || undefined,
      entityType: filterEntity.value || undefined,
    });
  } catch (e: unknown) {
    error.value = e instanceof Error ? e.message : 'Failed to load audit logs';
  } finally {
    loading.value = false;
  }
}

const filtered = computed(() => {
  let list = logs.value;
  if (filterAction.value) list = list.filter(l => l.action === filterAction.value);
  if (filterEntity.value) list = list.filter(l => l.entityType === filterEntity.value);
  if (search.value) {
    const q = search.value.toLowerCase();
    list = list.filter(l =>
      l.entityName.toLowerCase().includes(q) ||
      l.performedBy.toLowerCase().includes(q) ||
      l.details.toLowerCase().includes(q),
    );
  }
  return list;
});

const totalPages = computed(() => Math.max(1, Math.ceil(filtered.value.length / perPage)));
const paginated  = computed(() => filtered.value.slice((page.value - 1) * perPage, page.value * perPage));

function exportCsv() {
  exportToCsv(
    datestampedFilename('audit-log'),
    filtered.value,
    ['id', 'action', 'entityType', 'entityId', 'entityName', 'performedBy', 'details', 'ipAddress', 'timestamp'],
  );
}

function clearFilters() {
  search.value       = '';
  filterAction.value = '';
  filterEntity.value = '';
  page.value         = 1;
}

function formatDateTime(ts: string): string {
  if (!ts) return '';
  const d = new Date(ts);
  return d.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }) + ' ' +
         d.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
}

function actionClass(action: string): string {
  const map: Record<string, string> = {
    CREATE:  'badge-success',
    UPDATE:  'badge-info',
    DELETE:  'badge-danger',
    LOGIN:   'badge-warning',
    LOGOUT:  'badge-warning',
    EXPORT:  'badge-info',
    APPROVE: 'badge-success',
    REJECT:  'badge-danger',
    VIEW:    'badge-info',
  };
  return map[action] ?? 'badge-info';
}

function actionIcon(action: string): string {
  const map: Record<string, string> = {
    CREATE:  'fas fa-plus',
    UPDATE:  'fas fa-pen',
    DELETE:  'fas fa-trash',
    LOGIN:   'fas fa-right-to-bracket',
    LOGOUT:  'fas fa-right-from-bracket',
    EXPORT:  'fas fa-download',
    APPROVE: 'fas fa-check',
    REJECT:  'fas fa-xmark',
    VIEW:    'fas fa-eye',
  };
  return map[action] ?? 'fas fa-circle';
}
</script>
