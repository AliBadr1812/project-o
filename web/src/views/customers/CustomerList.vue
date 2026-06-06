<template>
  <div class="gap-6">
    <!-- Header -->
    <div class="flex items-center justify-between pb-5">
      <div>
        <h1 class="page-title">Customers</h1>
        <p class="text-[var(--text-secondary)]">Manage your customer database</p>
      </div>

      <div class="flex items-center gap-3">
        <!-- Search -->
        <div class="relative">
          <input
            type="search"
            v-model="searchQuery"
            placeholder="Search customers..."
            class="border border-[var(--glass-border)] rounded-lg pl-10 pr-4 py-2 w-64 bg-[var(--glass-bg)] text-[var(--text-secondary)] focus:outline-none focus:ring-2 focus:ring-accent focus:border-accent placeholder:text-[var(--text-muted)]"
          >
          <i class="fas fa-search fa-1x absolute left-4 top-3.5 text-[var(--text-secondary)]"></i>
        </div>

        <!-- Filter -->
        <div class="relative">
          <select
            v-model="customerType"
            class="glass-select"
          >
            <option value="">All Customers</option>
            <option value="new">New Customers</option>
            <option value="returning">Returning Customers</option>
            <option value="vip">VIP Customers</option>
          </select>
          <i class="fas fa-angle-down absolute right-4 top-3.5 text-[var(--text-secondary)]"></i>
        </div>

        <!-- Import Button -->
        <button @click="showImportModal = true" class="btn-accent">
          <i class="fas fa-download text-xs"></i>
          <span>Import</span>
        </button>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-4 gap-4 pb-6">
      <div class="glass-card p-4">
        <div class="flex items-center gap-3 mb-2">
          <div class="stat-icon ni-b"><i class="fas fa-users text-sm"></i></div>
          <p class="text-sm font-medium" style="color:var(--ni-blue)">Total Customers</p>
        </div>
        <p class="text-2xl font-bold" style="color:var(--text-primary)">{{ stats.total }}</p>
      </div>
      <div class="glass-card p-4">
        <div class="flex items-center gap-3 mb-2">
          <div class="stat-icon ni-g"><i class="fas fa-circle-check text-sm"></i></div>
          <p class="text-sm font-medium" style="color:var(--ni-green)">Active</p>
        </div>
        <p class="text-2xl font-bold" style="color:var(--text-primary)">{{ stats.active }}</p>
      </div>
      <div class="glass-card p-4">
        <div class="flex items-center gap-3 mb-2">
          <div class="stat-icon ni-o"><i class="fas fa-star text-sm"></i></div>
          <p class="text-sm font-medium" style="color:var(--ni-orange)">New This Month</p>
        </div>
        <p class="text-2xl font-bold" style="color:var(--text-primary)">{{ stats.newThisMonth }}</p>
      </div>
      <div class="glass-card p-4">
        <div class="flex items-center gap-3 mb-2">
          <div class="stat-icon ni-p"><i class="fas fa-dollar-sign text-sm"></i></div>
          <p class="text-sm font-medium" style="color:var(--ni-purple)">Avg. Order Value</p>
        </div>
        <p class="text-2xl font-bold" style="color:var(--text-primary)">{{ formatCurrency(stats.avgOrderValue) }}</p>
      </div>
    </div>

    <!-- Customers Table -->
    <Card class="overflow-hidden">
      <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
        <h2 class="text-lg font-semibold text-[var(--text-primary)]">Customer List</h2>
        <p class="text-sm text-[var(--text-secondary)]">Showing {{ paginatedCustomers.length }} of {{ filteredCustomers.length }} customers</p>
      </div>

      <div class="overflow-x-auto">
        <Table>
          <template #header>
            <tr class="bg-[var(--glass-bg)] text-[var(--text-secondary)]">
              <th class="py-3 px-4 text-left font-semibold text-sm">Customer</th>
              <th class="py-3 px-4 text-left font-semibold text-sm">Email</th>
              <th class="py-3 px-4 text-left font-semibold text-sm">Orders</th>
              <th class="py-3 px-4 text-left font-semibold text-sm">Total Spent</th>
              <th class="py-3 px-4 text-left font-semibold text-sm">Last Order</th>
              <th class="py-3 px-4 text-left font-semibold text-sm">Status</th>
              <th class="py-3 px-4 text-left font-semibold text-sm">Actions</th>
            </tr>
          </template>

          <template #body>
            <tr
              v-for="customer in paginatedCustomers"
              :key="customer.id"
              class="hover:bg-[rgba(255,255,255,0.35)] transition-colors duration-150 border-b border-[var(--glass-border)] last:border-b-0"
            >
              <td class="py-4 px-4">
                <div class="flex items-center gap-3">
                  <div
                    class="w-10 h-10 rounded-full flex items-center justify-center text-[var(--text-primary)] font-medium text-sm border-2 border-[var(--glass-border)]"
                    :style="{
                      background: `linear-gradient(135deg, ${stringToColor(customer.name)}, ${stringToColor(customer.name + '2')})`
                    }"
                  >
                    {{ getInitials(customer.name) }}
                  </div>
                  <div>
                    <p class="font-medium text-[var(--text-primary)]">{{ customer.name }}</p>
                    <p class="text-sm text-[var(--text-muted)]">{{ customer.phone }}</p>
                  </div>
                </div>
              </td>
              <td class="py-4 px-4">
                <p class="text-[var(--text-secondary)]">{{ customer.email }}</p>
              </td>
              <td class="py-4 px-4">
                <div class="flex items-center">
                  <span class="font-medium text-[var(--text-primary)] mr-2">{{ customer.orderCount }}</span>
                  <div class="w-16 h-2 bg-[var(--glass-bg)] rounded-full overflow-hidden">
                    <div
                      class="h-full transition-all duration-500 rounded-full"
                      style="background: var(--accent);"
                      :style="{ width: Math.min((customer.orderCount / maxOrders) * 100, 100) + '%' }"
                    ></div>
                  </div>
                </div>
              </td>
              <td class="py-4 px-4 font-medium text-[var(--text-primary)]">
                {{ formatCurrency(customer.totalSpent) }}
              </td>
              <td class="py-4 px-4">
                <p class="text-sm text-[var(--text-secondary)]">{{ formatDate(customer.lastOrderDate) }}</p>
              </td>
              <td class="py-4 px-4">
                <Badge :variant="customer.status === 'active' ? 'success' : 'secondary'">
                  <span class="flex items-center gap-1.5">
                    <span
                      class="w-2 h-2 rounded-full animate-pulse"
                      :class="{
                        'bg-green-500': customer.status === 'active',
                        'bg-gray-500': customer.status !== 'active',
                        'animate-none': customer.status !== 'active'
                      }"
                    ></span>
                    {{ customer.status }}
                  </span>
                </Badge>
              </td>
              <td class="py-4 px-4">
                <div class="flex items-center gap-1">
                  <button
                    @click="viewCustomer(customer.id)"
                    class="btn-glass-icon"
                    title="View customer"
                  >
                    <i class="fas fa-eye w-4 h-4 text-xs"></i>
                  </button>
                  <button
                    @click="editCustomer(customer.id)"
                    class="btn-glass-icon"
                    title="Edit customer"
                  >
                    <i class="fas fa-pen w-4 h-4 text-xs"></i>
                  </button>
                  <button
                    @click="sendEmailToCustomer(customer.email)"
                    class="btn-glass-icon"
                    title="Send email"
                  >
                    <i class="fas fa-envelope w-4 h-4 text-xs"></i>
                  </button>
                </div>
              </td>
            </tr>
          </template>
        </Table>
      </div>

      <!-- Empty State -->
      <EmptyState
        v-if="filteredCustomers.length === 0"
        title="No customers found"
        description="When customers place orders, they will appear here."
        action-text="Import Customers"
        @action-click="showImportModal = true"
        class="py-12"
      />

      <!-- Pagination -->
      <Pagination
        v-if="filteredCustomers.length > 0"
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-items="filteredCustomers.length"
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
import { formatCurrency, formatDate, getInitials } from '@/utils/formatters';

const router = useRouter();

// State
const searchQuery = ref('');
const customerType = ref('');
const showImportModal = ref(false);
const currentPage = ref(1);
const itemsPerPage = 5;

// Mock customers data
const customers = ref([
  {
    id: 1,
    name: 'John Smith',
    email: 'john.smith@example.com',
    phone: '+1 (555) 123-4567',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=John',
    orderCount: 24,
    totalSpent: 2899.99,
    lastOrderDate: '2024-01-15T14:30:00Z',
    status: 'active',
    type: 'vip'
  },
  {
    id: 2,
    name: 'Emma Johnson',
    email: 'emma.j@example.com',
    phone: '+1 (555) 234-5678',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Emma',
    orderCount: 12,
    totalSpent: 1499.50,
    lastOrderDate: '2024-01-14T11:20:00Z',
    status: 'active',
    type: 'returning'
  },
  {
    id: 3,
    name: 'Robert Brown',
    email: 'robert.b@example.com',
    phone: '+1 (555) 345-6789',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Robert',
    orderCount: 8,
    totalSpent: 899.99,
    lastOrderDate: '2024-01-12T09:15:00Z',
    status: 'active',
    type: 'new'
  },
  {
    id: 4,
    name: 'Sarah Davis',
    email: 'sarah.d@example.com',
    phone: '+1 (555) 456-7890',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Sarah',
    orderCount: 32,
    totalSpent: 3899.99,
    lastOrderDate: '2024-01-10T16:45:00Z',
    status: 'inactive',
    type: 'vip'
  },
  {
    id: 5,
    name: 'Michael Wilson',
    email: 'michael.w@example.com',
    phone: '+1 (555) 567-8901',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Michael',
    orderCount: 5,
    totalSpent: 599.99,
    lastOrderDate: '2024-01-08T11:30:00Z',
    status: 'active',
    type: 'new'
  },
  {
    id: 6,
    name: 'Jessica Miller',
    email: 'jessica.m@example.com',
    phone: '+1 (555) 678-9012',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Jessica',
    orderCount: 18,
    totalSpent: 2299.50,
    lastOrderDate: '2024-01-07T13:20:00Z',
    status: 'active',
    type: 'returning'
  },
  {
    id: 7,
    name: 'David Taylor',
    email: 'david.t@example.com',
    phone: '+1 (555) 789-0123',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=David',
    orderCount: 3,
    totalSpent: 399.99,
    lastOrderDate: '2024-01-05T15:10:00Z',
    status: 'active',
    type: 'new'
  },
  {
    id: 8,
    name: 'Lisa Anderson',
    email: 'lisa.a@example.com',
    phone: '+1 (555) 890-1234',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Lisa',
    orderCount: 15,
    totalSpent: 1799.99,
    lastOrderDate: '2024-01-04T10:45:00Z',
    status: 'inactive',
    type: 'returning'
  },
  {
    id: 9,
    name: 'James Thomas',
    email: 'james.t@example.com',
    phone: '+1 (555) 901-2345',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=James',
    orderCount: 27,
    totalSpent: 3299.99,
    lastOrderDate: '2024-01-03T14:30:00Z',
    status: 'active',
    type: 'vip'
  },
  {
    id: 10,
    name: 'Jennifer White',
    email: 'jennifer.w@example.com',
    phone: '+1 (555) 012-3456',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Jennifer',
    orderCount: 9,
    totalSpent: 1099.99,
    lastOrderDate: '2024-01-02T09:20:00Z',
    status: 'active',
    type: 'new'
  }
]);

// Computed properties
const stats = computed(() => {
  const total = customers.value.length;
  const active = customers.value.filter(c => c.status === 'active').length;
  const newThisMonth = customers.value.filter(c => {
    const customerDate = new Date(c.lastOrderDate);
    const now = new Date();
    return customerDate.getMonth() === now.getMonth() && customerDate.getFullYear() === now.getFullYear();
  }).length;
  const avgOrderValue = customers.value.length > 0
    ? customers.value.reduce((sum, c) => sum + c.totalSpent, 0) / customers.value.length
    : 0;

  return {
    total,
    active,
    newThisMonth,
    avgOrderValue
  };
});

const maxOrders = computed(() => {
  return Math.max(...customers.value.map(c => c.orderCount), 1);
});

const filteredCustomers = computed(() => {
  let filtered = [...customers.value];

  // Apply search filter
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter(customer =>
      customer.name.toLowerCase().includes(query) ||
      customer.email.toLowerCase().includes(query) ||
      customer.phone.toLowerCase().includes(query)
    );
  }

  // Apply customer type filter
  if (customerType.value) {
    filtered = filtered.filter(customer => customer.type === customerType.value);
  }

  // Sort by total spent (highest first)
  filtered.sort((a, b) => b.totalSpent - a.totalSpent);

  return filtered;
});

const totalPages = computed(() => Math.ceil(filteredCustomers.value.length / itemsPerPage));
const paginatedCustomers = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredCustomers.value.slice(start, start + itemsPerPage);
});

// Helper functions
const stringToColor = (str: string) => {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  const hue = hash % 360;
  return `hsl(${hue}, 70%, 50%)`;
};

// Methods
const onPageChange = (page: number) => {
  currentPage.value = page;
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const viewCustomer = (id: number) => {
  router.push(`/customers/${id}`);
};

const editCustomer = (id: number) => {
  router.push(`/customers/edit/${id}`);
};

const sendEmailToCustomer = (email: string) => {
  window.location.href = `mailto:${email}`;
};

// Initialize
onMounted(() => {
  console.log('CustomerList component mounted');
});

// Watch for filter changes and reset to page 1
watch([searchQuery, customerType], () => {
  currentPage.value = 1;
});
</script>

<style scoped>
.overflow-x-auto {
  scrollbar-width: thin;
  scrollbar-color: var(--glass-border) transparent;
}
.overflow-x-auto::-webkit-scrollbar { height: 6px; }
.overflow-x-auto::-webkit-scrollbar-track { background: transparent; }
.overflow-x-auto::-webkit-scrollbar-thumb { background-color: var(--glass-border); border-radius: 3px; }
</style>
