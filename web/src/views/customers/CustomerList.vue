<template>
  <div class="gap-6">
    <!-- Header -->
    <div class="flex items-center justify-between pb-5">
      <div>
        <h1 class="text-2xl font-bold text-white">Customers</h1>
        <p class="text-[var(--color-icon)]">Manage your customer database</p>
      </div>

      <div class="flex items-center gap-3">
        <!-- Search -->
        <div class="relative">
          <input
            type="search"
            v-model="searchQuery"
            placeholder="Search customers..."
            class="border border-[var(--color-border)] rounded-lg pl-10 pr-4 py-2 w-64 bg-[var(--color-card)] text-[var(--color-icon)] focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 placeholder:text-gray-500"
          >
          <i class="fas fa-search fa-1x absolute left-4 top-3.5 text-[var(--color-icon)]"></i>
        </div>

        <!-- Filter -->
        <div class="relative">
          <select
            v-model="customerType"
            class="appearance-none text-[var(--color-icon)] border border-[var(--color-border)] rounded-lg px-4 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 cursor-pointer bg-[var(--color-card)]"
          >
            <option value="">All Customers</option>
            <option value="new">New Customers</option>
            <option value="returning">Returning Customers</option>
            <option value="vip">VIP Customers</option>
          </select>
          <i class="fas fa-angle-down absolute right-4 top-3.5 text-[var(--color-icon)]"></i>
        </div>

        <!-- Import Button -->
        <button
          @click="showImportModal = true"
          class="flex items-center gap-2 rounded-lg px-4 py-2 text-white bg-[var(--primary-color)] hover:bg-blue-700 transition-colors duration-150"
        >
          <i class="fas fa-download fa-1x"></i>
          <span>Import</span>
        </button>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-4 gap-4 pb-6">
      <div class="bg-blue-900/20 p-4 rounded-lg border border-blue-800/30 hover:bg-blue-900/30 transition-colors duration-200">
        <p class="text-sm text-blue-400 font-medium">Total Customers</p>
        <p class="text-2xl font-bold text-white">{{ stats.total }}</p>
      </div>
      <div class="bg-green-900/20 p-4 rounded-lg border border-green-800/30 hover:bg-green-900/30 transition-colors duration-200">
        <p class="text-sm text-green-400 font-medium">Active</p>
        <p class="text-2xl font-bold text-white">{{ stats.active }}</p>
      </div>
      <div class="bg-yellow-900/20 p-4 rounded-lg border border-yellow-800/30 hover:bg-yellow-900/30 transition-colors duration-200">
        <p class="text-sm text-yellow-400 font-medium">New This Month</p>
        <p class="text-2xl font-bold text-white">{{ stats.newThisMonth }}</p>
      </div>
      <div class="bg-purple-900/20 p-4 rounded-lg border border-purple-800/30 hover:bg-purple-900/30 transition-colors duration-200">
        <p class="text-sm text-purple-400 font-medium">Avg. Order Value</p>
        <p class="text-2xl font-bold text-white">{{ formatCurrency(stats.avgOrderValue) }}</p>
      </div>
    </div>

    <!-- Customers Table -->
    <Card class="overflow-hidden">
      <div class="border-b border-[var(--color-border)] px-4 py-3 bg-[var(--color-card)]">
        <h2 class="text-lg font-semibold text-white">Customer List</h2>
        <p class="text-sm text-[var(--color-icon)]">Showing {{ paginatedCustomers.length }} of {{ filteredCustomers.length }} customers</p>
      </div>

      <div class="overflow-x-auto">
        <Table>
          <template #header>
            <tr class="bg-[var(--color-card)] text-[var(--color-icon)]">
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
              class="hover:bg-[var(--color-hover)] transition-colors duration-150 border-b border-[var(--color-border)] last:border-b-0"
            >
              <td class="py-4 px-4">
                <div class="flex items-center gap-3">
                  <div
                    class="w-10 h-10 rounded-full flex items-center justify-center text-white font-medium text-sm border-2 border-[var(--color-border)]"
                    :style="{
                      background: `linear-gradient(135deg, ${stringToColor(customer.name)}, ${stringToColor(customer.name + '2')})`
                    }"
                  >
                    {{ getInitials(customer.name) }}
                  </div>
                  <div>
                    <p class="font-medium text-white">{{ customer.name }}</p>
                    <p class="text-sm text-gray-400">{{ customer.phone }}</p>
                  </div>
                </div>
              </td>
              <td class="py-4 px-4">
                <p class="text-[var(--color-icon)]">{{ customer.email }}</p>
              </td>
              <td class="py-4 px-4">
                <div class="flex items-center">
                  <span class="font-medium text-white mr-2">{{ customer.orderCount }}</span>
                  <div class="w-16 h-2 bg-gray-800 rounded-full overflow-hidden">
                    <div
                      class="h-full bg-blue-500 transition-all duration-500"
                      :style="{ width: Math.min((customer.orderCount / maxOrders) * 100, 100) + '%' }"
                    ></div>
                  </div>
                </div>
              </td>
              <td class="py-4 px-4 font-medium text-white">
                {{ formatCurrency(customer.totalSpent) }}
              </td>
              <td class="py-4 px-4">
                <p class="text-sm text-[var(--color-icon)]">{{ formatDate(customer.lastOrderDate) }}</p>
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
                    class="p-2 text-[var(--color-icon)] hover:text-blue-500 hover:bg-blue-500/10 rounded-lg transition-all duration-150"
                    title="View customer"
                  >
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                      <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                    </svg>
                  </button>
                  <button
                    @click="editCustomer(customer.id)"
                    class="p-2 text-[var(--color-icon)] hover:text-green-500 hover:bg-green-500/10 rounded-lg transition-all duration-150"
                    title="Edit customer"
                  >
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
                    </svg>
                  </button>
                  <button
                    @click="sendEmailToCustomer(customer.email)"
                    class="p-2 text-[var(--color-icon)] hover:text-yellow-500 hover:bg-yellow-500/10 rounded-lg transition-all duration-150"
                    title="Send email"
                  >
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"/>
                      <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"/>
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

/* Customer avatar hover effect */
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

/* Progress bar animation */
.bg-blue-500 {
  transition: width 0.5s ease-in-out;
}

/* Clickable elements */
button:focus {
  outline: 2px solid var(--primary-color);
  outline-offset: 2px;
}

/* Stats cards hover effect */
.bg-blue-900\/20, .bg-green-900\/20, .bg-yellow-900\/20, .bg-purple-900\/20 {
  transition: all 0.2s ease;
}

.bg-blue-900\/20:hover, .bg-green-900\/20:hover, .bg-yellow-900\/20:hover, .bg-purple-900\/20:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}
</style>
