<template>
    <div class="flex flex-col gap-6">
        <!-- Page Header -->
        <div class="page-header">
            <div>
                <h1 class="page-title">Orders</h1>
                <p class="page-subtitle">Manage customer orders</p>
            </div>
            <div class="flex items-center gap-3">
                <button @click="exportOrders" class="btn-accent text-sm">
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
                        <div class="stat-icon ni-b"><i class="fas fa-bag-shopping"></i></div>
                        <span class="badge badge-info"><i class="fas fa-arrow-up text-[9px] mr-1"></i>All</span>
                    </div>
                    <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Total Orders</p>
                    <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats ? stats.total : 0 }}</p>
                    <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">{{ stats ? formatCurrency(stats.totalRevenue) : '$0' }} revenue</p>
                </div>
            </Card>
            <Card>
                <div class="p-5">
                    <div class="flex items-center justify-between mb-4">
                        <div class="stat-icon ni-o"><i class="fas fa-clock"></i></div>
                        <span class="badge badge-warning">Action needed</span>
                    </div>
                    <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Pending</p>
                    <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats ? stats.pending : 0 }}</p>
                    <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Requires attention</p>
                </div>
            </Card>
            <Card>
                <div class="p-5">
                    <div class="flex items-center justify-between mb-4">
                        <div class="stat-icon ni-g"><i class="fas fa-circle-check"></i></div>
                        <span class="badge badge-success"><i class="fas fa-arrow-up text-[9px] mr-1"></i>{{ stats ? stats.completionRate : 0 }}%</span>
                    </div>
                    <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Completed</p>
                    <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats ? stats.completed : 0 }}</p>
                    <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Completion rate</p>
                </div>
            </Card>
            <Card>
                <div class="p-5">
                    <div class="flex items-center justify-between mb-4">
                        <div class="stat-icon ni-r"><i class="fas fa-xmark"></i></div>
                        <span class="badge badge-danger"><i class="fas fa-arrow-down text-[9px] mr-1"></i>{{ stats ? stats.cancellationRate : 0 }}%</span>
                    </div>
                    <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Cancelled</p>
                    <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats ? stats.cancelled : 0 }}</p>
                    <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Cancellation rate</p>
                </div>
            </Card>
        </div>

        <!-- Orders Table -->
        <Card class="overflow-hidden">
            <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
                <div class="flex items-center justify-between mb-3">
                    <div>
                        <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Order History</h2>
                        <p class="text-xs mt-0.5" style="color: var(--text-muted);">Showing {{ paginatedOrders.length }} of {{ filteredOrders.length }} orders</p>
                    </div>
                </div>
                <!-- Filter/Search Row -->
                <div class="flex items-center gap-3 flex-wrap">
                    <div class="relative flex-1 min-w-[200px]">
                        <i class="fas fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted)"></i>
                        <input v-model="searchQuery" type="text" placeholder="Search orders..." class="glass-input w-full pl-9 text-sm">
                    </div>
                    <select v-model="statusFilter" class="glass-select text-sm">
                        <option value="">All Status</option>
                        <option value="pending">Pending</option>
                        <option value="processing">Processing</option>
                        <option value="shipped">Shipped</option>
                        <option value="delivered">Delivered</option>
                        <option value="cancelled">Cancelled</option>
                        <option value="refunded">Refunded</option>
                    </select>
                    <div class="relative">
                        <button @click="toggleDatePicker" class="btn-glass text-sm flex items-center gap-2">
                            <i class="fas fa-calendar text-xs"></i>
                            <span>{{ dateRangeLabel }}</span>
                        </button>
                        <div v-if="showDatePicker" class="absolute top-full mt-1 right-0 z-10 rounded-lg shadow-xl p-4 w-64" style="background: var(--glass-bg); border: 1px solid var(--glass-border);">
                            <div class="mb-3">
                                <label class="block text-xs font-medium mb-1" style="color: var(--text-secondary);">From Date</label>
                                <input type="date" v-model="dateRange.start" class="glass-input w-full text-sm" />
                            </div>
                            <div class="mb-4">
                                <label class="block text-xs font-medium mb-1" style="color: var(--text-secondary);">To Date</label>
                                <input type="date" v-model="dateRange.end" class="glass-input w-full text-sm" />
                            </div>
                            <div class="flex justify-end gap-2">
                                <button @click="resetDateRange" class="btn-glass text-sm">Reset</button>
                                <button @click="applyDateRange" class="btn-accent text-sm">Apply</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="overflow-x-auto">
                <table class="glass-table w-full">
                    <thead>
                        <tr>
                            <th class="w-10">
                              <input type="checkbox" v-model="selectAll"
                                style="accent-color:var(--accent);width:14px;height:14px;" />
                            </th>
                            <th class="cursor-pointer" @click="sortBy('order')">
                                Order
                                <i v-if="sortField === 'order'" :class="sortDirection === 'asc' ? 'fas fa-arrow-up ml-1' : 'fas fa-arrow-down ml-1'" class="text-[10px]"></i>
                            </th>
                            <th class="cursor-pointer" @click="sortBy('customer')">
                                Customer
                                <i v-if="sortField === 'customer'" :class="sortDirection === 'asc' ? 'fas fa-arrow-up ml-1' : 'fas fa-arrow-down ml-1'" class="text-[10px]"></i>
                            </th>
                            <th class="cursor-pointer" @click="sortBy('date')">
                                Date
                                <i v-if="sortField === 'date'" :class="sortDirection === 'asc' ? 'fas fa-arrow-up ml-1' : 'fas fa-arrow-down ml-1'" class="text-[10px]"></i>
                            </th>
                            <th class="cursor-pointer" @click="sortBy('total')">
                                Total
                                <i v-if="sortField === 'total'" :class="sortDirection === 'asc' ? 'fas fa-arrow-up ml-1' : 'fas fa-arrow-down ml-1'" class="text-[10px]"></i>
                            </th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="order in paginatedOrders" :key="order.id">
                            <td>
                              <input v-if="order.id" type="checkbox" :value="order.id" v-model="selectedIds"
                                style="accent-color:var(--accent);width:14px;height:14px;" />
                            </td>
                            <td v-if="!order"></td>
                            <td v-else>
                                <div class="flex flex-col">
                                    <a href="#" @click.prevent="viewOrder(order.id!)" class="td-primary hover:opacity-80 transition-opacity">
                                        #{{ formatOrderNumber(order.orderNumber) }}
                                    </a>
                                    <div class="flex items-center gap-2 mt-1">
                                        <span class="text-xs px-2 py-0.5 rounded" style="color: var(--text-muted); background: var(--glass-bg);">{{ order.items.length }} items</span>
                                        <span v-if="order.paymentMethod" class="text-xs capitalize" style="color: var(--text-muted);">{{ order.paymentMethod }}</span>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex items-center gap-3">
                                    <div class="w-8 h-8 rounded-full flex items-center justify-center text-xs font-medium" style="color: var(--text-primary);"
                                        :style="{ background: `linear-gradient(135deg, ${stringToColor(order.customerName)}, ${stringToColor(order.customerName + '2')})` }">
                                        {{ getInitials(order.customerName) }}
                                    </div>
                                    <div>
                                        <p class="text-sm font-medium" style="color: var(--text-primary);">{{ order.customerName }}</p>
                                        <p class="text-xs" style="color: var(--text-muted);">{{ order.customerEmail }}</p>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <span class="text-sm" style="color: var(--text-secondary);">{{ formatDate(order.createdAt) }}</span>
                                <p class="text-xs" style="color: var(--text-muted);">{{ formatTime(order.createdAt!) }}</p>
                            </td>
                            <td>
                                <span class="td-accent">{{ formatCurrency(order.total) }}</span>
                                <p v-if="order.discount && order.discount > 0" class="text-xs mt-0.5" style="color: var(--ni-green);">Saved {{ formatCurrency(order.discount) }}</p>
                            </td>
                            <td>
                                <div class="flex items-center gap-2">
                                    <Badge :variant="getStatusVariant(order.status)">{{ order.status }}</Badge>
                                    <button v-if="order.status === 'pending'" @click="processOrder(order.id!)" class="text-xs" style="color: var(--text-accent);">Process</button>
                                </div>
                            </td>
                            <td>
                                <div class="flex items-center gap-1">
                                    <button @click="viewOrder(order.id!)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="View order">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                    <button @click="editOrder(order.id!)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Edit order">
                                        <i class="fas fa-pen"></i>
                                    </button>
                                    <button @click="printInvoice(order.id!)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Print invoice">
                                        <i class="fas fa-print"></i>
                                    </button>
                                    <button v-if="order.status === 'pending' || order.status === 'processing'" @click="cancelOrder(order.id!)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Cancel order">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- Empty State -->
            <EmptyState
                v-if="!filteredOrders || filteredOrders.length === 0"
                title="No orders found"
                description="When customers place orders, they will appear here."
                action-text="View Analytics"
                @action="goToAnalytics"
                class="py-12"
            />
            <!-- Pagination -->
            <Pagination
                :current-page="currentPage"
                :total-pages="totalPages!"
                :total-items="filteredOrders.length"
                :items-per-page="itemsPerPage"
                @page-change="onPageChange"
            />
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
        <button @click="bulkExportOrders" class="btn-glass text-sm flex items-center gap-1.5">
          <i class="fas fa-download text-xs"></i>Export
        </button>
        <button @click="bulkCancel" class="btn-glass text-sm flex items-center gap-1.5"
          style="color: var(--ni-orange);">
          <i class="fas fa-ban text-xs"></i>Cancel
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
import { formatCurrency, formatDate, formatOrderNumber, getInitials } from '@/utils/formatters';
import { exportToCsv, datestampedFilename } from '@/utils/csvExport';
import { useOrderStore } from '@/stores/orderStore';
import { useToast } from '@/composables/useToast';
import { useConfirm } from '@/composables/useConfirm';
import type { Order } from '@/types/order';

const router  = useRouter();
const store   = useOrderStore();
const toast   = useToast();
const { confirm } = useConfirm();
const { items: orders } = storeToRefs(store);

// ── UI state ──────────────────────────────────────────────────────────────
const statusFilter = ref('');
const searchQuery = ref('');
const showDatePicker = ref(false);
const currentPage = ref(1);
const itemsPerPage = 8;
const sortField = ref('date');
const sortDirection = ref('desc');
const selectedIds = ref<number[]>([]);

const selectAll = computed({
  get: () => paginatedOrders.value.length > 0 &&
             paginatedOrders.value.every(o => o.id && selectedIds.value.includes(o.id)),
  set: (v: boolean) => {
    const ids = paginatedOrders.value.map(o => o.id!).filter(Boolean);
    if (v) selectedIds.value = [...new Set([...selectedIds.value, ...ids])];
    else   selectedIds.value = selectedIds.value.filter(id => !ids.includes(id));
  },
});

// Date Range
const dateRange = ref({
  start: '',
  end: ''
});

onMounted(() => store.fetchAll());

// Computed properties
const dateRangeLabel = computed(() => {
  if (!dateRange.value.start && !dateRange.value.end) {
    return 'Date Range';
  }
  if (dateRange.value.start && dateRange.value.end) {
    return `${formatDateShort(dateRange.value.start)} - ${formatDateShort(dateRange.value.end)}`;
  }
  return 'Select Range';
});

// Stats
const stats = computed(() => {
    if (!orders.value?.length) {
        return { total: 0, pending: 0, completed: 0, cancelled: 0, totalRevenue: 0, completionRate: 0, cancellationRate: 0 };
    }
    const total = orders.value.length;
    const pending = orders.value.filter(o => o.status === 'pending').length;
    const completed = orders.value.filter(o => o.status === 'delivered').length;
    const cancelled = orders.value.filter(o => o.status === 'cancelled' || o.status === 'refunded').length;
    const totalRevenue = orders.value
        .filter(o => o.status !== 'cancelled' && o.status !== 'refunded')
        .reduce((sum, order) => sum + order.total, 0);
    const completionRate = total > 0 ? Math.round((completed / total) * 100) : 0;
    const cancellationRate = total > 0 ? Math.round((cancelled / total) * 100) : 0;

    return {
        total,
        pending,
        completed,
        cancelled,
        totalRevenue,
        completionRate,
        cancellationRate
    };
});

// Filtered orders
const filteredOrders = computed((): Order[] => {
    if (!orders.value?.length) return [];
    let filtered = [...orders.value];

    // Apply status filter
    if (statusFilter.value) {
        filtered = filtered.filter(order => order.status === statusFilter.value);
    }

    // Apply search
    if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase();
        filtered = filtered.filter(order =>
            order.orderNumber.toLowerCase().includes(query) ||
            order.customerName.toLowerCase().includes(query) ||
            order.customerEmail.toLowerCase().includes(query)
        );
    }

    // Apply date range filter
    if (dateRange.value.start && dateRange.value.end) {
        const startDate = new Date(dateRange.value.start);
        const endDate = new Date(dateRange.value.end);
        endDate.setHours(23, 59, 59, 999);

        filtered = filtered.filter(order => {
            const orderDate = new Date(order.createdAt!);
            return orderDate >= startDate && orderDate <= endDate;
        });
    }

    filtered.sort((a, b) => {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        let aValue: any = (a as any)[sortField.value];
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        let bValue: any = (b as any)[sortField.value];

        // Handle special cases
        if (sortField.value === 'date') {
            aValue = new Date(aValue).getTime();
            bValue = new Date(bValue).getTime();
        }

        if (sortField.value === 'customer') {
            aValue = a.customerName.toLowerCase();
            bValue = b.customerName.toLowerCase();
        }

        if (typeof aValue === 'string') {
            aValue = aValue.toLowerCase();
            bValue = bValue.toLowerCase();
        }

        // Compare based on direction
        if (sortDirection.value === 'asc') {
            return aValue > bValue ? 1 : -1;
        } else {
            return aValue < bValue ? 1 : -1;
        }
    });

    return filtered;
});

// Pagination
const totalPages = computed(() => Math.ceil(filteredOrders.value.length / itemsPerPage));

const paginatedOrders = computed((): Order[] => {
    if (!filteredOrders.value.length) return [];
    const start = (currentPage.value - 1) * itemsPerPage;
    return filteredOrders.value.slice(start, start + itemsPerPage);
});

// Helper functions
const formatDateShort = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
};

const formatTime = (dateString: string) => {
    return new Date(dateString).toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
    });
};

const stringToColor = (str: string) => {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
    const hue = hash % 360;
    return `hsl(${hue}, 70%, 50%)`;
};

const getStatusVariant = (status: string) => {
    switch (status) {
        case 'pending': return 'warning';
        case 'processing': return 'info';
        case 'shipped': return 'primary';
        case 'delivered': return 'success';
        case 'cancelled':
        case 'refunded': return 'danger';
        default: return 'secondary';
    }
};

// Methods
const sortBy = (field: string) => {
    if (sortField.value === field) {
        // Toggle direction if same field
        sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc';
    } else {
        // New field, set to ascending by default
        sortField.value = field;
        sortDirection.value = 'asc';
    }
    // Reset to first page
    currentPage.value = 1;
};

const onPageChange = (page: number) => {
    currentPage.value = page;
    window.scrollTo({ top: 0, behavior: 'smooth' });
};

const toggleDatePicker = () => {
    showDatePicker.value = !showDatePicker.value;
};

const applyDateRange = () => {
    showDatePicker.value = false;
    currentPage.value = 1; // Reset to first page
};

const resetDateRange = () => {
    dateRange.value = { start: '', end: '' };
    showDatePicker.value = false;
    currentPage.value = 1;
};

const viewOrder = (id: number) => {
    router.push(`/orders/${id}`);
};

const editOrder = (id: number) => {
    router.push(`/orders/edit/${id}`);
};

const printInvoice = (id: number) => {
    router.push(`/orders/${id}`);
    toast.info('Open the order detail to print the invoice');
};

const processOrder = (id: number) => {
    const order = store.items.find(o => o.id === id);
    if (order && order.status === 'pending') {
        store.updateItem(id, { status: 'processing' });
        toast.success(`Order #${order.orderNumber} is now processing`);
    }
};

const cancelOrder = async (id: number) => {
    const order = store.items.find(o => o.id === id);
    if (!order) return;
    const ok = await confirm({
        title:       'Cancel order',
        message:     `Cancel order #${order.orderNumber} for ${order.customerName}?`,
        detail:      'The customer will need to place a new order.',
        confirmText: 'Cancel Order',
        variant:     'warning',
    });
    if (ok && (order.status === 'pending' || order.status === 'processing')) {
        store.updateItem(id, { status: 'cancelled' });
        toast.warning(`Order #${order.orderNumber} has been cancelled`);
    }
};

const exportOrders = () => {
  exportToCsv(
    datestampedFilename('orders'),
    filteredOrders.value as unknown as Record<string, unknown>[],
    ['id', 'orderNumber', 'customerName', 'customerEmail', 'status', 'total', 'itemCount', 'createdAt'],
  );
  toast.success(`Exported ${filteredOrders.value.length} orders to CSV`);
};

const goToAnalytics = () => {
    router.push('/analytics');
};

// ── Bulk actions ──────────────────────────────────────────────────────────
const bulkCancel = async () => {
  if (!selectedIds.value.length) return;
  const ok = await confirm({
    title:       'Cancel orders',
    message:     `Cancel ${selectedIds.value.length} selected orders?`,
    confirmText: `Cancel ${selectedIds.value.length} Orders`,
    variant:     'warning',
  });
  if (!ok) return;
  for (const id of selectedIds.value) {
    const o = orders.value.find(x => x.id === id);
    if (o && (o.status === 'pending' || o.status === 'processing')) {
      store.updateItem(id, { status: 'cancelled' });
    }
  }
  toast.warning(`${selectedIds.value.length} orders cancelled`);
  selectedIds.value = [];
};

const bulkExportOrders = () => {
  const selected = filteredOrders.value.filter(o => o.id && selectedIds.value.includes(o.id));
  exportToCsv(datestampedFilename('orders-selected'), selected as unknown as Record<string, unknown>[],
    ['id', 'orderNumber', 'customerName', 'customerEmail', 'status', 'total', 'createdAt']);
  toast.success(`Exported ${selected.length} orders to CSV`);
};

// Watch for filter changes and reset to page 1
watch([statusFilter, searchQuery, dateRange], () => {
    currentPage.value = 1;
}, { deep: true });
</script>

<style scoped>
.bulk-bar-enter-active { transition: all 0.3s cubic-bezier(0.34,1.56,0.64,1); }
.bulk-bar-leave-active { transition: all 0.2s ease; }
.bulk-bar-enter-from,
.bulk-bar-leave-to   { opacity: 0; transform: translateX(-50%) translateY(16px) scale(0.95); }
</style>

