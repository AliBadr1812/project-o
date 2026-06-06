<template>
    <div class="gap-6">
        <!-- Header -->
        <div class="flex items-center justify-between pb-5">
            <div>
                <h1 class="page-title">Orders</h1>
                <p class="text-[var(--text-secondary)]">Manage customer orders</p>
            </div>

            <div class="flex items-center gap-3">
                <!-- Filter -->
                <div class="relative text-[var(--text-secondary)]">
                    <select
                        v-model="statusFilter"
                        class="appearance-none border border-[var(--glass-border)] rounded-lg px-4 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-accent focus:border-accent cursor-pointer bg-[var(--glass-bg)]"
                    >
                        <option value="">All Status</option>
                        <option value="pending">Pending</option>
                        <option value="processing">Processing</option>
                        <option value="shipped">Shipped</option>
                        <option value="delivered">Delivered</option>
                        <option value="cancelled">Cancelled</option>
                        <option value="refunded">Refunded</option>
                    </select>
                    <i class="fas fa-angle-down fa-1x absolute right-4 top-4"></i>
                </div>

                <!-- Date Range -->
                <div class="relative">
                    <button
                        @click="toggleDatePicker"
                        class="flex items-center gap-2 border border-[var(--glass-border)] rounded-lg px-4 py-2 pr-10 bg-[var(--glass-bg)] text-[var(--text-secondary)] hover:bg-[rgba(255,255,255,0.35)] transition-colors duration-150"
                    >
                        <i class="fas fa-calendar fa-1x"></i>
                        <span>{{ dateRangeLabel }}</span>
                        <i class="fas fa-angle-down fa-1x absolute right-4 top-4"></i>
                    </button>

                    <!-- Date Picker Dropdown -->
                    <div v-if="showDatePicker" class="absolute top-full mt-1 right-0 z-10 bg-[var(--glass-bg)] border border-[var(--glass-border)] rounded-lg shadow-xl p-4 w-64">
                        <div class="mb-3">
                            <label class="block text-sm font-medium text-[var(--text-secondary)] mb-1">From Date</label>
                            <input
                                type="date"
                                v-model="dateRange.start"
                                class="w-full border border-[var(--glass-border)] rounded px-3 py-2 bg-transparent text-[var(--text-secondary)]"
                            />
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-[var(--text-secondary)] mb-1">To Date</label>
                            <input
                                type="date"
                                v-model="dateRange.end"
                                class="w-full border border-[var(--glass-border)] rounded px-3 py-2 bg-transparent text-[var(--text-secondary)]"
                            />
                        </div>
                        <div class="flex justify-end gap-2">
                            <button
                                @click="resetDateRange"
                                class="px-3 py-1.5 text-sm border border-[var(--glass-border)] rounded text-[var(--text-secondary)] hover:bg-[rgba(255,255,255,0.35)]"
                            >
                                Reset
                            </button>
                            <button
                                @click="applyDateRange"
                                class="px-3 py-1.5 text-sm bg-[var(--glass-bg)] text-[var(--text-primary)] rounded hover:bg-blue-700"
                            >
                                Apply
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Search -->
                <div class="relative">
                    <input
                        type="search"
                        v-model="searchQuery"
                        placeholder="Search orders..."
                        class="border border-[var(--glass-border)] rounded-lg pl-10 pr-4 py-2 w-64 bg-[var(--glass-bg)] text-[var(--text-secondary)] focus:outline-none focus:ring-2 focus:ring-accent focus:border-accent placeholder:text-[var(--text-muted)]"
                    >
                    <i class="fas fa-search fa-1x absolute left-4 top-3.5 text-[var(--text-secondary)]"></i>
                </div>

                <!-- Export Button -->
                <button
                @click="exportOrders"
                class="flex items-center gap-2 px-4 py-2 rounded-lg text-[var(--text-primary)] bg-[var(--accent)] hover:bg-blue-700 transition-colors duration-150"
                >
                    <i class="fas fa-download fa-1x"></i>
                    <span>Export</span>
                </button>
            </div>
        </div>

        <!-- Stats -->
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4 pb-4">
            <Card class="p-4 hover:bg-[rgba(255,255,255,0.35)] transition-all duration-200 hover:translate-y-[-2px]">
                <p class="text-sm text-[var(--text-secondary)] font-medium">Total Orders</p>
                <p class="page-title">{{ stats ? stats.total : 0 }}</p>
                <p class="text-xs text-[var(--text-accent)] mt-1">{{ stats ? formatCurrency(stats.totalRevenue) : 0 }} revenue</p>
            </Card>
            <Card class="p-4 hover:bg-[rgba(255,255,255,0.35)] transition-all duration-200 hover:translate-y-[-2px]">
                <p class="text-sm text-[var(--text-secondary)] font-medium">Pending</p>
                <p class="page-title">{{ stats ? stats.pending : 0 }}</p>
                <p class="text-xs text-yellow-500 mt-1">Requires action</p>
            </Card>
            <Card class="p-4 hover:bg-[rgba(255,255,255,0.35)] transition-all duration-200 hover:translate-y-[-2px]">
                <p class="text-sm text-[var(--text-secondary)] font-medium">Completed</p>
                <p class="page-title">{{ stats ? stats.completed : 0 }}</p>
                <p class="text-xs text-green-500 mt-1">{{ stats ? stats.completionRate : 0 }}% rate</p>
            </Card>
            <Card class="p-4 hover:bg-[rgba(255,255,255,0.35)] transition-all duration-200 hover:translate-y-[-2px]">
                <p class="text-sm text-[var(--text-secondary)] font-medium">Cancelled</p>
                <p class="page-title">{{ stats ? stats.cancelled : 0 }}</p>
                <p class="text-xs text-red-500 mt-1">{{ stats ? stats.cancellationRate : 0 }}% rate</p>
            </Card>
        </div>

        <!-- Orders Table -->
        <Card class="overflow-hidden">
            <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
                <h2 class="text-lg font-semibold text-[var(--text-primary)]">Order History</h2>
                <p class="text-sm text-[var(--text-secondary)]">Showing {{ paginatedOrders!.length }} of {{ filteredOrders!.length }} orders</p>
            </div>

            <div class="overflow-x-auto">
                <Table>
                    <template #header>
                        <tr class="bg-[var(--glass-bg)] text-[var(--text-secondary)]">
                            <th class="py-3 px-4 text-left font-semibold text-sm cursor-pointer hover:text-[var(--text-primary)] transition-colors duration-150" @click="sortBy('order')">
                                Order
                                <i
                                    v-if="sortField === 'order'"
                                    :class="sortDirection === 'asc' ? 'fas fa-arrow-up ml-2' : 'fas fa-arrow-down ml-2'">
                                </i>
                            </th>
                            <th class="py-3 px-4 text-left font-semibold text-sm cursor-pointer hover:text-[var(--text-primary)] transition-colors duration-150" @click="sortBy('customer')">
                                Customer
                                <i
                                    v-if="sortField === 'customer'"
                                    :class="sortDirection === 'asc' ? 'fas fa-arrow-up ml-2' : 'fas fa-arrow-down ml-2'">
                                </i>
                            </th>
                            <th class="py-3 px-4 text-left font-semibold text-sm cursor-pointer hover:text-[var(--text-primary)] transition-colors duration-150" @click="sortBy('date')">
                                Date
                                <i
                                    v-if="sortField === 'date'"
                                    :class="sortDirection === 'asc' ? 'fas fa-arrow-up ml-2' : 'fas fa-arrow-down ml-2'">
                                </i>
                            </th>
                            <th class="py-3 px-4 text-left font-semibold text-sm cursor-pointer hover:text-[var(--text-primary)] transition-colors duration-150" @click="sortBy('total')">
                                Total
                                <i
                                    v-if="sortField === 'total'"
                                    :class="sortDirection === 'asc' ? 'fas fa-arrow-up ml-2' : 'fas fa-arrow-down ml-2'">
                                </i>
                            </th>
                            <th class="py-3 px-4 text-left font-semibold text-sm cursor-pointer hover:text-[var(--text-primary)] transition-colors duration-150" @click="sortBy('status')">
                                Status
                                <i
                                    v-if="sortField === 'status'"
                                    :class="sortDirection === 'asc' ? 'fas fa-arrow-up ml-2' : 'fas fa-arrow-down ml-2'">
                                </i>
                            </th>
                            <th class="py-3 px-4 text-left font-semibold text-sm">
                                Actions
                            </th>
                        </tr>
                    </template>

                    <template #body>
                        <tr
                        v-for="order in paginatedOrders"
                        :key="order.id"
                        class="hover:bg-[rgba(255,255,255,0.35)] transition-colors duration-150 border-b border-[var(--glass-border)] last:border-b-0"
                        >
                            <td v-if="!order">

                            </td>
                            <td v-else class="py-4 px-4">
                                <div class="flex flex-col">
                                    <a
                                        href="#"
                                        @click.prevent="viewOrder(order.id!)"
                                        class="font-medium text-[var(--text-primary)] hover:text-[var(--text-accent)] transition-colors duration-150"
                                    >
                                        #{{ formatOrderNumber(order.orderNumber) }}
                                    </a>
                                    <div class="flex items-center gap-2 mt-1">
                                        <span class="text-xs text-[var(--text-muted)] bg-[rgba(255,255,255,0.25)] px-2 py-0.5 rounded">{{ order.items.length }} items</span>
                                        <span
                                        v-if="order.paymentMethod"
                                        class="text-xs text-[var(--text-muted)] capitalize"
                                        >
                                        {{ order.paymentMethod }}
                                        </span>
                                    </div>
                                </div>
                            </td>
                            <td class="py-4 px-4">
                                <div class="flex items-center gap-3">
                                    <div
                                        class="w-10 h-10 rounded-full flex items-center justify-center text-[var(--text-primary)] font-medium text-sm"
                                        :style="{
                                        background: `linear-gradient(135deg, ${stringToColor(order.customerName)}, ${stringToColor(order.customerName + '2')})`,
                                        boxShadow: '0 2px 4px rgba(0,0,0,0.2)'
                                        }"
                                    >
                                        {{ getInitials(order.customerName) }}
                                    </div>
                                    <div>
                                        <p class="font-medium text-[var(--text-primary)]">{{ order.customerName }}</p>
                                        <p class="text-sm text-[var(--text-muted)]">{{ order.customerEmail }}</p>
                                    </div>
                                </div>
                            </td>
                            <td class="py-4 px-4">
                                <div class="flex flex-col">
                                    <span class="text-[var(--text-secondary)]">{{ formatDate(order.createdAt) }}</span>
                                    <span class="text-xs text-[var(--text-muted)]">{{ formatTime(order.createdAt!) }}</span>
                                </div>
                            </td>
                            <td class="py-4 px-4">
                                <div class="flex flex-col">
                                    <span class="font-medium text-[var(--text-primary)]">{{ formatCurrency(order.total) }}</span>
                                    <span
                                        v-if="order.discount && order.discount > 0"
                                        class="text-xs text-green-500 mt-1"
                                    >
                                        Saved {{ formatCurrency(order.discount) }}
                                    </span>
                                </div>
                            </td>
                            <td class="py-4 px-4">
                                <div class="flex items-center gap-2">
                                    <Badge
                                        :variant="getStatusVariant(order.status)"
                                        class="px-3 py-1.5"
                                    >
                                        <span class="flex items-center gap-1.5">
                                        <span
                                            class="w-2 h-2 rounded-full animate-pulse"
                                            :class="{
                                            'bg-yellow-500': order.status === 'pending',
                                            'bg-blue-500': order.status === 'processing',
                                            'bg-purple-500': order.status === 'shipped',
                                            'bg-green-500': order.status === 'delivered',
                                            'bg-red-500': order.status === 'cancelled' || order.status === 'refunded',
                                            'animate-none': order.status === 'delivered' || order.status === 'cancelled' || order.status === 'refunded'
                                            }"
                                        ></span>
                                        {{ order.status }}
                                        </span>
                                    </Badge>
                                    <button
                                        v-if="order.status === 'pending'"
                                        @click="processOrder(order.id!)"
                                        class="text-xs text-[var(--accent)] hover:text-[var(--text-accent)] transition-colors duration-150"
                                    >
                                        Process
                                    </button>
                                </div>
                            </td>
                            <td class="py-4 px-4">
                                <div class="flex items-center gap-1">
                                    <button
                                        @click="viewOrder(order.id!)"
                                        class="btn-glass-icon"
                                        title="View order"
                                    >
                                        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                                            <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                                            <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                                        </svg>
                                    </button>
                                    <button
                                        @click="editOrder(order.id!)"
                                        class="p-2 text-[var(--text-secondary)] hover:text-green-500 hover:bg-green-500/10 rounded-lg transition-all duration-150"
                                        title="Edit order"
                                    >
                                        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                                            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
                                        </svg>
                                    </button>
                                    <button
                                        @click="printInvoice(order.id!)"
                                        class="p-2 text-[var(--text-secondary)] hover:text-purple-500 hover:bg-purple-500/10 rounded-lg transition-all duration-150"
                                        title="Print invoice"
                                    >
                                        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M5 4v3H4a2 2 0 00-2 2v3a2 2 0 002 2h1v2a2 2 0 002 2h6a2 2 0 002-2v-2h1a2 2 0 002-2V9a2 2 0 00-2-2h-1V4a2 2 0 00-2-2H7a2 2 0 00-2 2zm8 0H7v3h6V4zm0 8H7v4h6v-4z" clip-rule="evenodd"/>
                                        </svg>
                                    </button>
                                    <button
                                        v-if="order.status === 'pending' || order.status === 'processing'"
                                        @click="cancelOrder(order.id!)"
                                        class="p-2 text-[var(--text-secondary)] hover:text-red-500 hover:bg-red-500/10 rounded-lg transition-all duration-150"
                                        title="Cancel order"
                                    >
                                        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd"/>
                                        </svg>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </template>
                </Table>
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
                :total-items="filteredOrders!.length"
                :items-per-page="itemsPerPage"
                @page-change="onPageChange"
            />
        </Card>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import Table from '@/components/ui/Table.vue';
import Badge from '@/components/ui/Badge.vue';
import EmptyState from '@/components/shared/EmptyState.vue';
import Pagination from '@/components/ui/Pagination.vue';
import { formatCurrency, formatDate, formatOrderNumber, getInitials } from '@/utils/formatters';
import { orderService } from '@/services/orderService';
import type { Order } from '@/types/order';

const router = useRouter();

// State
const orders = ref<Order[] | null>(null);
const statusFilter = ref('');
const searchQuery = ref('');
const showDatePicker = ref(false);
const currentPage = ref(1);
const itemsPerPage = 8;

const loading = ref(true);
const error = ref<string | null>(null);

const sortField = ref('date');
const sortDirection = ref('desc');

const fetchOrders = async () => {
    loading.value = true;
    error.value = null;

    try {
        orders.value = await orderService.getAllOrders();
    }
    catch (err) {
        error.value = err instanceof Error ? err.message : 'Failed to load orders';
    }
    finally {
        loading.value = false;
    }
}

// Date Range
const dateRange = ref({
  start: '',
  end: ''
});

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
    if (!orders.value) {
        return;
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
const filteredOrders = computed(() => {
    if (!orders.value) {
        return;
    }
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
const totalPages = computed(() => {
    if (!filteredOrders.value) return;
    return Math.ceil(filteredOrders.value.length / itemsPerPage)
});

const paginatedOrders = computed(() => {
    if (!filteredOrders.value) return;
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
    if (!orders.value) return;
    const order = orders.value.find(o => o.id === id);
    if (order) {
        alert(`Printing invoice for order #${order.orderNumber}`);
        // In a real app, you would open a print dialog or generate a PDF
    }
};

const processOrder = (id: number) => {
    if (!orders.value) return;
    const order = orders.value.find(o => o.id === id);
    if (order && order.status === 'pending') {
        order.status = 'processing';
        alert(`Order #${order.orderNumber} is now being processed`);
    }
};

const cancelOrder = (id: number) => {
    if (!orders.value) return;
    if (confirm('Are you sure you want to cancel this order?')) {
        const order = orders.value.find(o => o.id === id);
        if (order && (order.status === 'pending' || order.status === 'processing')) {
        order.status = 'cancelled';
        alert(`Order #${order.orderNumber} has been cancelled`);
        }
    }
};

const exportOrders = () => {
    const dataStr = JSON.stringify(filteredOrders.value, null, 2);
    const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr);
    const exportFileDefaultName = `orders_export_${new Date().toISOString().split('T')[0]}.json`;

    const linkElement = document.createElement('a');
    linkElement.setAttribute('href', dataUri);
    linkElement.setAttribute('download', exportFileDefaultName);
    linkElement.click();

    alert('Orders exported successfully!');
};

const goToAnalytics = () => {
    router.push('/analytics');
};

// Initialize
onMounted(() => {
    console.log('OrderList component mounted');
    fetchOrders();
});

// Watch for filter changes and reset to page 1
watch([statusFilter, searchQuery, dateRange], () => {
    currentPage.value = 1;
}, { deep: true });
</script>

<style scoped>
/* Custom scrollbar */
.overflow-x-auto {
  scrollbar-width: thin;
  scrollbar-color: var(--color-border) transparent;
}

.overflow-x-auto::-webkit-scrollbar {
  height: 6px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  background: transparent;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background-color: var(--color-border);
  border-radius: 3px;
}

/* Date picker positioning */
.relative > .absolute {
  animation: slideDown 0.2s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Status indicator animation */
.animate-pulse {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

/* Clickable elements */
a, button {
  cursor: pointer;
}

button:focus {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}

/* Customer avatar gradient */
.w-10.h-10 {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.w-10.h-10:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

/* Table row hover effect */
tr {
  transition: all 0.15s ease-out;
}

/* Date picker inputs */
input[type="date"] {
  color-scheme: dark;
}

input[type="date"]::-webkit-calendar-picker-indicator {
  filter: invert(1);
  cursor: pointer;
}
</style>
