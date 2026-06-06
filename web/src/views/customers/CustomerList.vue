<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Customers</h1>
        <p class="page-subtitle">Manage your customer database</p>
      </div>
      <div class="flex items-center gap-3">
        <button @click="showImportModal = true" class="btn-accent text-sm">
          <i class="fas fa-download text-xs mr-1"></i>
          Import
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
            <span class="badge badge-warning">This month</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">New This Month</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.newThisMonth }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Recently joined</p>
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
            <p class="text-xs mt-0.5" style="color: var(--text-muted);">Showing {{ paginatedCustomers.length }} of {{ filteredCustomers.length }} customers</p>
          </div>
        </div>
        <div class="flex items-center gap-3 flex-wrap">
          <div class="relative flex-1 min-w-[200px]">
            <i class="fas fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted)"></i>
            <input v-model="searchQuery" type="text" placeholder="Search customers..." class="glass-input w-full pl-9 text-sm">
          </div>
          <select v-model="customerType" class="glass-select text-sm">
            <option value="">All Customers</option>
            <option value="new">New Customers</option>
            <option value="returning">Returning Customers</option>
            <option value="vip">VIP Customers</option>
          </select>
        </div>
      </div>

      <div class="overflow-x-auto">
        <table class="glass-table w-full">
          <thead>
            <tr>
              <th>Customer</th>
              <th>Email</th>
              <th>Orders</th>
              <th>Total Spent</th>
              <th>Last Order</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="customer in paginatedCustomers" :key="customer.id">
              <td>
                <div class="flex items-center gap-3">
                  <div class="w-9 h-9 rounded-full flex items-center justify-center text-xs font-medium"
                    style="color: var(--text-primary);"
                    :style="{ background: `linear-gradient(135deg, ${stringToColor(customer.name)}, ${stringToColor(customer.name + '2')})` }">
                    {{ getInitials(customer.name) }}
                  </div>
                  <div>
                    <p class="td-primary">{{ customer.name }}</p>
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
                  <button @click="sendEmailToCustomer(customer.email)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Email">
                    <i class="fas fa-envelope"></i>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <EmptyState v-if="filteredCustomers.length === 0" title="No customers found"
        description="When customers place orders, they will appear here."
        action-text="Import Customers" @action-click="showImportModal = true" class="py-12" />

      <Pagination v-if="filteredCustomers.length > 0" :current-page="currentPage"
        :total-pages="totalPages" :total-items="filteredCustomers.length"
        :items-per-page="itemsPerPage" @page-change="onPageChange" />
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
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

