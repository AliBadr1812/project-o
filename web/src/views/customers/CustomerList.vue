<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Customers</h1>
        <p class="page-subtitle">Manage your customer database</p>
      </div>
      <div class="flex items-center gap-3">
        <button @click="exportCustomers" class="btn-accent text-sm">
          <i class="fas fa-download text-xs mr-1"></i>
          Export
        </button>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-b"><i class="fas fa-users"></i></div>
            <span class="badge badge-info">All</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Total Customers</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.total }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">In your database</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-g"><i class="fas fa-circle-check"></i></div>
            <span class="badge badge-success">Active</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Active</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.active }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Currently active accounts</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-o"><i class="fas fa-star"></i></div>
            <span class="badge badge-warning">VIP</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">VIP Customers</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.vip }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">High-value accounts</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-p"><i class="fas fa-dollar-sign"></i></div>
            <span class="badge badge-info">Average</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Avg. Order Value</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ formatCurrency(stats.avgOrderValue) }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Per customer</p>
        </div>
      </Card>
    </div>

    <!-- Customers Table -->
    <Card class="overflow-hidden">
      <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
        <div class="flex items-center justify-between mb-3">
          <div>
            <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Customer List</h2>
            <p class="text-xs mt-0.5" style="color: var(--text-muted);">
              <span v-if="loading">Loading…</span>
              <span v-else>Showing {{ paginatedCustomers.length }} of {{ filteredCustomers.length }} customers</span>
            </p>
          </div>
        </div>
        <div class="flex items-center gap-3 flex-wrap">
          <div class="relative flex-1 min-w-[200px]">
            <i class="fas fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted)"></i>
            <input v-model="searchQuery" type="text" placeholder="Search customers…" class="glass-input w-full pl-9 text-sm">
          </div>
          <select v-model="customerType" class="glass-select text-sm">
            <option value="">All Customers</option>
            <option value="new">New</option>
            <option value="returning">Returning</option>
            <option value="vip">VIP</option>
          </select>
        </div>
      </div>

      <!-- Error state -->
      <div v-if="error" class="px-6 py-8 text-center">
        <i class="fas fa-triangle-exclamation text-2xl mb-2" style="color: var(--ni-orange);"></i>
        <p class="text-sm font-medium mb-1" style="color: var(--text-primary);">Could not load customers</p>
        <p class="text-xs mb-3" style="color: var(--text-muted);">{{ error }}</p>
        <button @click="store.fetchAll()" class="btn-glass text-sm">Try again</button>
      </div>

      <template v-else>
        <div class="overflow-x-auto">
          <table class="glass-table w-full">
            <thead>
              <tr>
                <th class="w-10">
                  <input type="checkbox" v-model="selectAll" style="accent-color:var(--accent);width:14px;height:14px;" />
                </th>
                <th>Customer</th>
                <th>Email</th>
                <th>Orders</th>
                <th>Total Spent</th>
                <th>Last Order</th>
                <th>Type</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="loading">
                <td colspan="9" class="py-10 text-center">
                  <i class="fas fa-spinner fa-spin mr-2" style="color: var(--text-muted);"></i>
                  <span style="color: var(--text-muted);">Loading…</span>
                </td>
              </tr>
              <tr v-for="customer in paginatedCustomers" :key="customer.id">
                <td>
                  <input type="checkbox" :value="customer.id" v-model="selectedIds"
                    style="accent-color:var(--accent);width:14px;height:14px;" />
                </td>
                <td>
                  <div class="flex items-center gap-3">
                    <div class="w-9 h-9 rounded-full flex items-center justify-center text-xs font-bold text-white"
                      :style="{ background: `linear-gradient(135deg, ${stringToColor(customer.fullName)}, ${stringToColor(customer.fullName + '2')})` }">
                      {{ getInitials(customer.fullName) }}
                    </div>
                    <div>
                      <p class="td-primary">{{ customer.fullName }}</p>
                      <p class="text-xs" style="color: var(--text-muted);">{{ customer.phone }}</p>
                    </div>
                  </div>
                </td>
                <td><span class="text-sm" style="color: var(--text-secondary);">{{ customer.email }}</span></td>
                <td>
                  <div class="flex items-center gap-2">
                    <span class="font-medium text-sm" style="color: var(--text-primary);">{{ customer.orderCount }}</span>
                    <div class="progress-track w-16">
                      <div class="progress-fill" :style="{ width: Math.min((customer.orderCount / maxOrders) * 100, 100) + '%', background: 'var(--progress-primary)' }"></div>
                    </div>
                  </div>
                </td>
                <td class="td-accent">{{ formatCurrency(customer.totalSpent) }}</td>
                <td><span class="text-sm" style="color: var(--text-secondary);">{{ formatDate(customer.lastOrderDate) }}</span></td>
                <td>
                  <Badge :variant="customer.type === 'vip' ? 'warning' : customer.type === 'returning' ? 'info' : 'neutral'">
                    {{ customer.type }}
                  </Badge>
                </td>
                <td>
                  <Badge :variant="customer.status === 'active' ? 'success' : 'secondary'">{{ customer.status }}</Badge>
                </td>
                <td>
                  <div class="flex items-center gap-1">
                    <button @click="viewCustomer(customer.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="View">
                      <i class="fas fa-eye"></i>
                    </button>
                    <button @click="editCustomer(customer.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Edit">
                      <i class="fas fa-pen"></i>
                    </button>
                    <button @click="sendEmail(customer.email)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Email">
                      <i class="fas fa-envelope"></i>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <EmptyState v-if="!loading && customers.length === 0"
          title="No customers found"
          description="When customers place orders, they will appear here." class="py-12" />

        <Pagination v-if="filteredCustomers.length > 0"
          :current-page="currentPage" :total-pages="totalPages"
          :total-items="filteredCustomers.length" :items-per-page="itemsPerPage"
          @page-change="onPageChange" />
      </template>
    </Card>

    <!-- Floating Bulk Action Bar -->
    <Transition name="bulk-bar">
      <div v-if="selectedIds.length > 0"
        class="fixed bottom-6 left-1/2 -translate-x-1/2 z-50 flex items-center gap-3 px-5 py-3 rounded-2xl shadow-2xl"
        style="background: var(--glass-bg-strong); backdrop-filter: blur(20px); border: 1px solid var(--glass-border); box-shadow: 0 8px 32px rgba(0,0,0,0.25);">
        <span class="text-sm font-semibold" style="color: var(--text-primary);">
          {{ selectedIds.length }} selected
        </span>
        <div class="w-px h-5" style="background: var(--glass-border);"></div>
        <button @click="bulkExportCustomers" class="btn-glass text-sm flex items-center gap-1.5">
          <i class="fas fa-download text-xs"></i>Export
        </button>
        <button @click="bulkDelete" class="btn-glass text-sm flex items-center gap-1.5"
          style="color: var(--ni-red);">
          <i class="fas fa-trash text-xs"></i>Delete
        </button>
        <button @click="selectedIds = []" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Clear">
          <i class="fas fa-xmark"></i>
        </button>
      </div>
    </Transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import EmptyState from '@/components/shared/EmptyState.vue';
import Pagination from '@/components/ui/Pagination.vue';
import { formatCurrency, formatDate, getInitials } from '@/utils/formatters';
import { exportToCsv, datestampedFilename } from '@/utils/csvExport';
import { useCustomerStore } from '@/stores/customerStore';
import { useToast } from '@/composables/useToast';
import { useConfirm } from '@/composables/useConfirm';

const router = useRouter();
const store = useCustomerStore();
const toast = useToast();
const { confirm } = useConfirm();
const { items: customers, loading, error } = storeToRefs(store);

// ── UI state ──────────────────────────────────────────────────────────────
const searchQuery  = ref('');
const customerType = ref('');
const currentPage  = ref(1);
const itemsPerPage = 5;
const selectedIds  = ref<number[]>([]);

const selectAll = computed({
  get: () => paginatedCustomers.value.length > 0 &&
             paginatedCustomers.value.every(c => selectedIds.value.includes(c.id)),
  set: (v: boolean) => {
    const ids = paginatedCustomers.value.map(c => c.id);
    if (v) selectedIds.value = [...new Set([...selectedIds.value, ...ids])];
    else   selectedIds.value = selectedIds.value.filter(id => !ids.includes(id));
  },
});

onMounted(() => store.fetchAll());

// ── Stats ─────────────────────────────────────────────────────────────────
const stats = computed(() => {
  const total = customers.value.length;
  const active = customers.value.filter(c => c.status === 'active').length;
  const vip    = customers.value.filter(c => c.type === 'vip').length;
  const avgOrderValue = total > 0
    ? customers.value.reduce((s, c) => s + c.totalSpent, 0) / total
    : 0;
  return { total, active, vip, avgOrderValue };
});

const maxOrders = computed(() => Math.max(...customers.value.map(c => c.orderCount), 1));

// ── Filtering ─────────────────────────────────────────────────────────────
const filteredCustomers = computed(() => {
  let list = [...customers.value];

  if (searchQuery.value) {
    const q = searchQuery.value.toLowerCase();
    list = list.filter(c =>
      c.fullName.toLowerCase().includes(q) ||
      c.email.toLowerCase().includes(q) ||
      (c.phone ?? '').toLowerCase().includes(q)
    );
  }

  if (customerType.value) {
    list = list.filter(c => c.type === customerType.value);
  }

  list.sort((a, b) => b.totalSpent - a.totalSpent);
  return list;
});

// ── Pagination ────────────────────────────────────────────────────────────
const totalPages = computed(() => Math.ceil(filteredCustomers.value.length / itemsPerPage));
const paginatedCustomers = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredCustomers.value.slice(start, start + itemsPerPage);
});

watch([searchQuery, customerType], () => { currentPage.value = 1; });

// ── Helpers ───────────────────────────────────────────────────────────────
const stringToColor = (str: string) => {
  let hash = 0;
  for (let i = 0; i < str.length; i++) hash = str.charCodeAt(i) + ((hash << 5) - hash);
  return `hsl(${hash % 360}, 70%, 50%)`;
};

// ── Actions ───────────────────────────────────────────────────────────────
const onPageChange = (page: number) => { currentPage.value = page; window.scrollTo({ top: 0, behavior: 'smooth' }); };
const viewCustomer = (id: number) => router.push(`/customers/${id}`);
const editCustomer = (id: number) => router.push(`/customers/edit/${id}`);
const sendEmail    = (email: string) => { window.location.href = `mailto:${email}`; };
const exportCustomers = () => {
  exportToCsv(
    datestampedFilename('customers'),
    filteredCustomers.value,
    ['id', 'fullName', 'email', 'phone', 'type', 'status', 'orderCount', 'totalSpent', 'lastOrderDate'],
  );
  toast.success(`Exported ${filteredCustomers.value.length} customers`);
};

// ── Bulk actions ──────────────────────────────────────────────────────────
const bulkExportCustomers = () => {
  const selected = filteredCustomers.value.filter(c => selectedIds.value.includes(c.id));
  exportToCsv(datestampedFilename('customers-selected'), selected,
    ['id', 'fullName', 'email', 'phone', 'type', 'status', 'orderCount', 'totalSpent']);
  toast.success(`Exported ${selected.length} customers to CSV`);
};

const bulkDelete = async () => {
  if (!selectedIds.value.length) return;
  const ok = await confirm({
    title:       'Delete customers',
    message:     `Permanently delete ${selectedIds.value.length} selected customers?`,
    detail:      'All their orders and data will be removed.',
    confirmText: `Delete ${selectedIds.value.length}`,
    variant:     'danger',
  });
  if (!ok) return;
  for (const id of [...selectedIds.value]) {
    try { await store.remove(id); } catch { /* skip */ }
  }
  toast.success(`${selectedIds.value.length} customers deleted`);
  selectedIds.value = [];
};
</script>

<style scoped>
.bulk-bar-enter-active { transition: all 0.3s cubic-bezier(0.34,1.56,0.64,1); }
.bulk-bar-leave-active { transition: all 0.2s ease; }
.bulk-bar-enter-from,
.bulk-bar-leave-to   { opacity: 0; transform: translateX(-50%) translateY(16px) scale(0.95); }
</style>
