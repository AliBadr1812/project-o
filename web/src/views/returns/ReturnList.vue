<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Returns & Refunds</h1>
        <p class="page-subtitle">Manage customer return requests and refund processing</p>
      </div>
      <div class="flex items-center gap-3">
        <button @click="store.fetchAll(true)" class="btn-glass text-sm">
          <i class="fas fa-sync-alt text-xs mr-1"></i>Refresh
        </button>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-2 lg:grid-cols-5 gap-4">
      <div v-for="s in statusStats" :key="s.status" class="glass-card p-5">
        <div class="flex items-center justify-between mb-3">
          <div class="stat-icon" :class="s.iconColor"><i :class="s.icon"></i></div>
          <span class="badge" :class="s.badgeClass">{{ s.status }}</span>
        </div>
        <p class="text-2xl font-bold" style="color: var(--text-primary);">{{ s.count }}</p>
        <p class="text-xs mt-1" style="color: var(--text-muted);">{{ s.label }}</p>
      </div>
    </div>

    <!-- Filters + Table -->
    <div class="glass-card overflow-hidden">
      <div class="px-6 py-4 flex flex-wrap items-center gap-3" style="border-bottom: 1px solid var(--glass-border);">
        <h2 class="text-[15px] font-semibold flex-1" style="color: var(--text-primary);">Return Requests</h2>
        <div class="relative">
          <i class="fas fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted)"></i>
          <input v-model="search" type="text" placeholder="Search…" class="glass-input pl-9 text-sm" style="width:180px;">
        </div>
        <select v-model="filterStatus" class="glass-input text-sm" style="width:140px;">
          <option value="">All Statuses</option>
          <option value="PENDING">Pending</option>
          <option value="APPROVED">Approved</option>
          <option value="IN_TRANSIT">In Transit</option>
          <option value="REFUNDED">Refunded</option>
          <option value="REJECTED">Rejected</option>
        </select>
      </div>

      <!-- Error -->
      <div v-if="error" class="px-6 py-8 text-center">
        <i class="fas fa-triangle-exclamation text-2xl mb-2" style="color: var(--ni-orange);"></i>
        <p class="text-sm mb-3" style="color: var(--text-primary);">{{ error }}</p>
        <button @click="store.fetchAll()" class="btn-glass text-sm">Try again</button>
      </div>

      <div v-else class="overflow-x-auto">
        <table class="glass-table w-full">
          <thead>
            <tr>
              <th>Return #</th>
              <th>Order</th>
              <th>Customer</th>
              <th>Reason</th>
              <th>Refund</th>
              <th>Status</th>
              <th>Date</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="loading">
              <td colspan="8" class="py-10 text-center">
                <i class="fas fa-spinner fa-spin mr-2" style="color: var(--text-muted);"></i>
                <span style="color: var(--text-muted);">Loading…</span>
              </td>
            </tr>
            <tr v-for="r in paginatedReturns" :key="r.id">
              <td>
                <span class="font-mono text-sm font-semibold" style="color: var(--accent);">{{ r.returnNumber }}</span>
              </td>
              <td>
                <span class="text-sm" style="color: var(--text-secondary);">{{ r.orderNumber }}</span>
              </td>
              <td>
                <div>
                  <p class="text-sm font-medium" style="color: var(--text-primary);">{{ r.customerName }}</p>
                  <p class="text-xs" style="color: var(--text-muted);">{{ r.customerEmail }}</p>
                </div>
              </td>
              <td>
                <span class="badge badge-info text-xs">{{ reasonLabel(r.reason) }}</span>
              </td>
              <td>
                <span class="td-accent">{{ formatCurrency(r.refundAmount) }}</span>
              </td>
              <td>
                <span class="badge" :class="statusClass(r.status)">{{ r.status }}</span>
              </td>
              <td>
                <span class="text-xs" style="color: var(--text-muted);">{{ formatDate(r.createdAt) }}</span>
              </td>
              <td>
                <div class="flex items-center gap-1">
                  <!-- Status update buttons -->
                  <button v-if="r.status === 'PENDING'" @click="updateStatus(r, 'APPROVED')"
                    class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Approve"
                    style="color: var(--ni-green);">
                    <i class="fas fa-check"></i>
                  </button>
                  <button v-if="r.status === 'APPROVED'" @click="updateStatus(r, 'REFUNDED')"
                    class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Mark Refunded"
                    style="color: var(--accent);">
                    <i class="fas fa-money-bill-wave"></i>
                  </button>
                  <button v-if="r.status === 'PENDING'" @click="updateStatus(r, 'REJECTED')"
                    class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Reject"
                    style="color: var(--ni-red);">
                    <i class="fas fa-xmark"></i>
                  </button>
                  <button @click="showDetail(r)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="View details">
                    <i class="fas fa-eye"></i>
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="!loading && items.length > 0 && paginatedReturns.length === 0 && filtered.length === 0">
              <td colspan="8" class="py-12 text-center">
                <i class="fas fa-magnifying-glass text-3xl mb-3" style="color: var(--text-muted);"></i>
                <p class="text-sm font-medium mb-1" style="color: var(--text-primary);">No results</p>
                <p class="text-xs" style="color: var(--text-muted);">No returns match your current filters.</p>
              </td>
            </tr>
          </tbody>
        </table>
        <EmptyState v-if="!loading && items.length === 0"
          title="No return requests"
          description="When customers submit return requests they will appear here.">
          <template #icon><i class="fas fa-box-open"></i></template>
        </EmptyState>
        <Pagination v-if="filtered.length > itemsPerPage"
          :current-page="currentPage" :total-pages="totalPages"
          :total-items="filtered.length" :items-per-page="itemsPerPage"
          @page-change="(p) => { currentPage.value = p; window.scrollTo({ top: 0, behavior: 'smooth' }); }" />
      </div>
    </div>

    <!-- Detail Drawer -->
    <Teleport to="body">
      <div v-if="selected" class="fixed inset-0 z-50 flex items-end sm:items-center justify-center"
        style="background: rgba(0,0,0,0.5);" @click.self="selected = null">
        <div class="glass-card w-full sm:max-w-lg mx-4 p-6 rounded-2xl flex flex-col gap-4">
          <div class="flex items-center justify-between">
            <h3 class="text-base font-semibold" style="color: var(--text-primary);">
              {{ selected.returnNumber }}
            </h3>
            <button @click="selected = null" class="btn-glass-icon w-8 h-8 rounded-xl text-xs">
              <i class="fas fa-xmark"></i>
            </button>
          </div>
          <div class="grid grid-cols-2 gap-3 text-sm">
            <div><p class="text-xs" style="color: var(--text-muted);">Customer</p><p style="color: var(--text-primary);">{{ selected.customerName }}</p></div>
            <div><p class="text-xs" style="color: var(--text-muted);">Order</p><p style="color: var(--text-primary);">{{ selected.orderNumber }}</p></div>
            <div><p class="text-xs" style="color: var(--text-muted);">Reason</p><p style="color: var(--text-primary);">{{ reasonLabel(selected.reason) }}</p></div>
            <div><p class="text-xs" style="color: var(--text-muted);">Refund</p><p style="color: var(--accent);" class="font-semibold">{{ formatCurrency(selected.refundAmount) }}</p></div>
            <div><p class="text-xs" style="color: var(--text-muted);">Status</p><span class="badge" :class="statusClass(selected.status)">{{ selected.status }}</span></div>
            <div><p class="text-xs" style="color: var(--text-muted);">Method</p><p style="color: var(--text-primary);">{{ methodLabel(selected.refundMethod) }}</p></div>
          </div>
          <div v-if="selected.reasonDetail" class="text-sm p-3 rounded-xl" style="background: var(--glass-bg); color: var(--text-secondary);">
            <p class="text-xs font-medium mb-1" style="color: var(--text-muted);">Customer note</p>
            {{ selected.reasonDetail }}
          </div>
          <!-- Items -->
          <div v-if="selected.items?.length">
            <p class="text-xs font-semibold mb-2" style="color: var(--text-muted);">Returned Items</p>
            <div v-for="item in selected.items" :key="item.productId" class="flex items-center justify-between py-1.5 text-sm"
              style="border-bottom: 1px solid var(--glass-border);">
              <div>
                <p style="color: var(--text-primary);">{{ item.productName }}</p>
                <p class="text-xs" style="color: var(--text-muted);">{{ item.sku }} × {{ item.quantity }}</p>
              </div>
              <span class="td-accent">{{ formatCurrency(item.unitPrice * item.quantity) }}</span>
            </div>
          </div>
          <!-- Staff notes -->
          <div class="flex flex-col gap-2">
            <label class="text-xs font-semibold" style="color: var(--text-muted);">Staff Notes</label>
            <textarea v-model="staffNotesDraft" class="glass-input text-sm resize-none" rows="2"
              placeholder="Add a note…"></textarea>
          </div>
          <!-- Status actions -->
          <div class="flex gap-2 flex-wrap">
            <button v-if="selected.status === 'PENDING'" @click="updateStatusFromDrawer('APPROVED')"
              class="btn-accent text-sm flex-1">
              <i class="fas fa-check text-xs mr-1"></i>Approve
            </button>
            <button v-if="selected.status === 'APPROVED'" @click="updateStatusFromDrawer('REFUNDED')"
              class="btn-accent text-sm flex-1">
              <i class="fas fa-money-bill-wave text-xs mr-1"></i>Mark Refunded
            </button>
            <button v-if="selected.status === 'PENDING'" @click="updateStatusFromDrawer('REJECTED')"
              class="btn-glass text-sm flex-1" style="color: var(--ni-red);">
              <i class="fas fa-xmark text-xs mr-1"></i>Reject
            </button>
            <button v-if="staffNotesDraft !== selected.staffNotes" @click="saveNotes()"
              class="btn-glass text-sm flex-1">
              <i class="fas fa-floppy-disk text-xs mr-1"></i>Save Notes
            </button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { storeToRefs } from 'pinia';
import { useReturnStore } from '@/stores/returnStore';
import { returnService } from '@/services/returnService';
import { useToast } from '@/composables/useToast';
import EmptyState from '@/components/shared/EmptyState.vue';
import Pagination from '@/components/ui/Pagination.vue';
import { formatCurrency, formatDate } from '@/utils/formatters';
import type { Return } from '@/types/return';

const store = useReturnStore();
const toast = useToast();
const { items, loading, error } = storeToRefs(store);

const search       = ref('');
const filterStatus = ref('');
const selected     = ref<Return | null>(null);
const staffNotesDraft = ref('');
const currentPage  = ref(1);
const itemsPerPage = 8;

onMounted(() => store.fetchAll());

watch(selected, (r) => {
  staffNotesDraft.value = r?.staffNotes ?? '';
});

// Status summary cards
const statusStats = computed(() => [
  { status: 'PENDING',    label: 'Awaiting review', count: count('PENDING'),    icon: 'fas fa-clock',           iconColor: 'ni-o', badgeClass: 'badge-warning' },
  { status: 'APPROVED',   label: 'Approved',         count: count('APPROVED'),   icon: 'fas fa-circle-check',    iconColor: 'ni-g', badgeClass: 'badge-success' },
  { status: 'IN_TRANSIT', label: 'In transit',       count: count('IN_TRANSIT'), icon: 'fas fa-truck',           iconColor: 'ni-b', badgeClass: 'badge-info'    },
  { status: 'REFUNDED',   label: 'Refunds issued',   count: count('REFUNDED'),   icon: 'fas fa-money-bill-wave', iconColor: 'ni-p', badgeClass: 'badge-success' },
  { status: 'REJECTED',   label: 'Rejected',         count: count('REJECTED'),   icon: 'fas fa-ban',             iconColor: 'ni-r', badgeClass: 'badge-danger'  },
]);

function count(status: string) {
  return items.value.filter(r => r.status === status).length;
}

const filtered = computed(() => {
  let list = items.value;
  if (filterStatus.value) list = list.filter(r => r.status === filterStatus.value);
  if (search.value) {
    const q = search.value.toLowerCase();
    list = list.filter(r =>
      r.returnNumber.toLowerCase().includes(q) ||
      r.customerName.toLowerCase().includes(q) ||
      r.orderNumber.toLowerCase().includes(q),
    );
  }
  return list;
});

const totalPages    = computed(() => Math.ceil(filtered.value.length / itemsPerPage));
const paginatedReturns = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filtered.value.slice(start, start + itemsPerPage);
});

watch([search, filterStatus], () => { currentPage.value = 1; });

function statusClass(status: string): string {
  const map: Record<string, string> = {
    PENDING:    'badge-warning',
    APPROVED:   'badge-success',
    IN_TRANSIT: 'badge-info',
    REFUNDED:   'badge-success',
    REJECTED:   'badge-danger',
  };
  return map[status] ?? 'badge-info';
}

function reasonLabel(r: string): string {
  const map: Record<string, string> = {
    DEFECTIVE:        'Defective',
    WRONG_ITEM:       'Wrong Item',
    NOT_AS_DESCRIBED: 'Not as Described',
    CHANGED_MIND:     'Changed Mind',
    OTHER:            'Other',
  };
  return map[r] ?? r;
}

function methodLabel(m: string): string {
  const map: Record<string, string> = {
    ORIGINAL_PAYMENT: 'Original Payment',
    STORE_CREDIT:     'Store Credit',
    BANK_TRANSFER:    'Bank Transfer',
  };
  return map[m] ?? m;
}

async function updateStatus(r: Return, status: string) {
  try {
    const updated = await returnService.updateReturn(r.id, { status });
    store.updateItem(r.id, updated);
    toast.success(`Return ${r.returnNumber} marked as ${status.toLowerCase().replace('_', ' ')}`);
  } catch (e: any) {
    toast.error(e?.message ?? 'Update failed', 'Error');
  }
}

function showDetail(r: Return) {
  selected.value = r;
}

async function updateStatusFromDrawer(status: string) {
  if (!selected.value) return;
  const notes = staffNotesDraft.value || undefined;
  try {
    const updated = await returnService.updateReturn(selected.value.id, { status, staffNotes: notes });
    store.updateItem(selected.value.id, updated);
    selected.value = { ...selected.value, ...updated };
    toast.success('Status updated');
  } catch (e: any) {
    toast.error(e?.message ?? 'Update failed', 'Error');
  }
}

async function saveNotes() {
  if (!selected.value) return;
  try {
    const updated = await returnService.updateReturn(selected.value.id, { staffNotes: staffNotesDraft.value });
    store.updateItem(selected.value.id, updated);
    selected.value = { ...selected.value, staffNotes: staffNotesDraft.value };
    toast.success('Staff notes saved');
  } catch (e: any) {
    toast.error(e?.message ?? 'Save failed', 'Error');
  }
}
</script>
