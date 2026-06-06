<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Categories</h1>
        <p class="page-subtitle">Manage your product categories</p>
      </div>
      <div class="flex items-center gap-3">
        <button @click="handleCreate" class="btn-accent text-sm">
          <i class="fas fa-plus text-xs mr-1"></i>New Category
        </button>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-b"><i class="fas fa-folder"></i></div>
            <span class="badge badge-info">All</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Total Categories</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.total }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Across all products</p>
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
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Currently published</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-s"><i class="fas fa-eye-slash"></i></div>
            <span class="badge badge-warning">Inactive</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Inactive</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.inactive }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Hidden from store</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-p"><i class="fas fa-chart-bar"></i></div>
            <span class="badge badge-info">Average</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Avg Products</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.avgProducts }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Per category</p>
        </div>
      </Card>
    </div>

    <!-- Categories Table -->
    <Card class="overflow-hidden">
      <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
        <div class="flex items-center justify-between mb-3">
          <div>
            <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Category List</h2>
            <p class="text-xs mt-0.5" style="color: var(--text-muted);">{{ filteredCategories.length }} categories</p>
          </div>
        </div>
        <div class="flex items-center gap-3 flex-wrap">
          <div class="relative flex-1 min-w-[200px]">
            <i class="fas fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted)"></i>
            <input v-model="searchQuery" type="text" placeholder="Search categories..." class="glass-input w-full pl-9 text-sm">
          </div>
          <select v-model="statusFilter" class="glass-select text-sm">
            <option value="">All Status</option>
            <option value="active">Active</option>
            <option value="inactive">Inactive</option>
          </select>
        </div>
      </div>

      <div class="overflow-x-auto">
        <table class="glass-table w-full">
          <thead>
            <tr>
              <th class="w-10"><input type="checkbox" v-model="selectAll" style="accent-color: var(--accent); width: 14px; height: 14px;" /></th>
              <th>Name</th>
              <th>Description</th>
              <th>Products</th>
              <th>Status</th>
              <th>Created</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="category in paginatedCategories" :key="category.id">
              <td>
                <input type="checkbox" :value="category.id" v-model="selectedCategories" style="accent-color: var(--accent); width: 14px; height: 14px;" />
              </td>
              <td>
                <div class="flex items-center gap-3">
                  <div class="w-9 h-9 rounded-lg flex items-center justify-center" style="background: var(--accent)1a;">
                    <i :class="category.icon || 'fas fa-folder'" style="color: var(--accent);"></i>
                  </div>
                  <div>
                    <p class="td-primary">{{ category.name }}</p>
                    <p class="text-xs" style="color: var(--text-muted);">ID: {{ category.id }}</p>
                  </div>
                </div>
              </td>
              <td>
                <span class="text-sm" style="color: var(--text-secondary);">{{ truncateText(category.description, 60) }}</span>
              </td>
              <td>
                <div class="flex items-center gap-2">
                  <span class="font-medium text-sm" style="color: var(--text-primary);">{{ category.productCount }}</span>
                  <div class="progress-track w-20">
                    <div class="progress-fill" :style="{ width: Math.min((category.productCount / maxProducts) * 100, 100) + '%', background: 'var(--progress-primary)' }"></div>
                  </div>
                </div>
              </td>
              <td><Badge :variant="category.status === 'active' ? 'success' : 'secondary'">{{ category.status }}</Badge></td>
              <td><span class="text-sm" style="color: var(--text-secondary);">{{ formatDate(category.createdAt) }}</span></td>
              <td>
                <div class="flex items-center gap-1">
                  <button @click="handleView(category.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="View">
                    <i class="fas fa-eye"></i>
                  </button>
                  <button @click="handleEdit(category.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Edit">
                    <i class="fas fa-pen"></i>
                  </button>
                  <button @click="handleDelete(category.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Delete">
                    <i class="fas fa-trash"></i>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <EmptyState v-if="categories.length === 0" title="No categories found"
        description="Get started by creating your first category."
        action-text="Create Category" @action="handleCreate" />

      <Pagination v-if="filteredCategories.length > 0" :current-page="currentPage"
        :total-pages="totalPages" :total-items="filteredCategories.length"
        :items-per-page="itemsPerPage" @page-change="handlePageChange" />
    </Card>

    <!-- Bulk Actions -->
    <div v-if="selectedCategories.length > 0" class="fixed bottom-6 right-6 z-50">
      <Card>
        <div class="flex items-center gap-4 p-3">
          <span class="text-sm font-medium" style="color: var(--text-secondary);">{{ selectedCategories.length }} selected</span>
          <button @click="handleBulkStatusChange" class="btn-glass text-sm">Change Status</button>
          <button @click="handleBulkDelete" class="btn-accent text-sm" style="background: var(--ni-red);">Delete Selected</button>
        </div>
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import EmptyState from '@/components/shared/EmptyState.vue';
import Pagination from '@/components/ui/Pagination.vue';
import { formatDate, truncateText } from '@/utils/formatters';

const router = useRouter();

// State
const searchQuery = ref('');
const statusFilter = ref('');
const currentPage = ref(1);
const itemsPerPage = 5;
const selectedCategories = ref<number[]>([]);
const loading = ref(false);

// Mock category data
const categories = ref([
  {
    id: 1,
    name: 'Electronics',
    description: 'Electronic devices and gadgets including phones, laptops, and accessories',
    icon: 'fas fa-laptop',
    productCount: 42,
    status: 'active',
    createdAt: '2024-01-15T10:30:00Z'
  },
  {
    id: 2,
    name: 'Clothing',
    description: 'Apparel and fashion items for all ages and genders',
    icon: 'fas fa-tshirt',
    productCount: 128,
    status: 'active',
    createdAt: '2024-01-14T14:20:00Z'
  },
  {
    id: 3,
    name: 'Home & Kitchen',
    description: 'Home appliances, furniture, and kitchen essentials',
    icon: 'fas fa-home',
    productCount: 76,
    status: 'active',
    createdAt: '2024-01-13T09:15:00Z'
  },
  {
    id: 4,
    name: 'Fitness',
    description: 'Exercise equipment, sportswear, and health supplements',
    icon: 'fas fa-dumbbell',
    productCount: 31,
    status: 'active',
    createdAt: '2024-01-12T16:45:00Z'
  },
  {
    id: 5,
    name: 'Books',
    description: 'Fiction, non-fiction, educational books and magazines',
    icon: 'fas fa-book',
    productCount: 89,
    status: 'active',
    createdAt: '2024-01-11T11:30:00Z'
  },
  {
    id: 6,
    name: 'Beauty',
    description: 'Cosmetics, skincare, and personal care products',
    icon: 'fas fa-spa',
    productCount: 54,
    status: 'active',
    createdAt: '2024-01-10T13:20:00Z'
  },
  {
    id: 7,
    name: 'Toys & Games',
    description: 'Children toys, board games, and entertainment products',
    icon: 'fas fa-gamepad',
    productCount: 23,
    status: 'inactive',
    createdAt: '2024-01-09T15:10:00Z'
  },
  {
    id: 8,
    name: 'Automotive',
    description: 'Car accessories, tools, and maintenance products',
    icon: 'fas fa-car',
    productCount: 18,
    status: 'active',
    createdAt: '2024-01-08T10:45:00Z'
  },
  {
    id: 9,
    name: 'Garden',
    description: 'Gardening tools, plants, and outdoor equipment',
    icon: 'fas fa-seedling',
    productCount: 12,
    status: 'active',
    createdAt: '2024-01-07T14:30:00Z'
  },
  {
    id: 10,
    name: 'Office Supplies',
    description: 'Stationery, office furniture, and business essentials',
    icon: 'fas fa-briefcase',
    productCount: 67,
    status: 'active',
    createdAt: '2024-01-06T09:20:00Z'
  }
]);

// Stats
const stats = computed(() => {
  const total = categories.value.length;
  const active = categories.value.filter(c => c.status === 'active').length;
  const inactive = categories.value.filter(c => c.status === 'inactive').length;
  const avgProducts = categories.value.length > 0
    ? Math.round(categories.value.reduce((sum, c) => sum + c.productCount, 0) / categories.value.length)
    : 0;

  return { total, active, inactive, avgProducts };
});

// Calculate max products for progress bars
const maxProducts = computed(() => {
  return Math.max(...categories.value.map(c => c.productCount), 1);
});

// Filtered categories
const filteredCategories = computed(() => {
  let filtered = [...categories.value];

  // Apply search
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter(category =>
      category.name.toLowerCase().includes(query) ||
      category.description.toLowerCase().includes(query)
    );
  }

  // Apply status filter
  if (statusFilter.value) {
    filtered = filtered.filter(category => category.status === statusFilter.value);
  }

  return filtered;
});

// Pagination
const totalPages = computed(() => Math.ceil(filteredCategories.value.length / itemsPerPage));
const paginatedCategories = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredCategories.value.slice(start, start + itemsPerPage);
});

// Select all functionality
const selectAll = computed({
  get: () => {
    if (paginatedCategories.value.length === 0) return false;
    return paginatedCategories.value.every(category =>
      selectedCategories.value.includes(category.id)
    );
  },
  set: (value: boolean) => {
    if (value) {
      selectedCategories.value = [
        ...new Set([
          ...selectedCategories.value,
          ...paginatedCategories.value.map(category => category.id)
        ])
      ];
    } else {
      selectedCategories.value = selectedCategories.value.filter(
        id => !paginatedCategories.value.map(c => c.id).includes(id)
      );
    }
  }
});

// Methods
const handlePageChange = (page: number) => {
  currentPage.value = page;
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const handleCreate = () => {
  router.push('/categories/create');
};

const handleEdit = (id: number) => {
  router.push(`/categories/edit/${id}`);
};

const handleView = (id: number) => {
  router.push(`/categories/${id}`);
};

const handleDelete = (id: number) => {
  if (confirm('Are you sure you want to delete this category?')) {
    const index = categories.value.findIndex(c => c.id === id);
    if (index !== -1) {
      categories.value.splice(index, 1);
      // Remove from selected if present
      const selectedIndex = selectedCategories.value.indexOf(id);
      if (selectedIndex !== -1) {
        selectedCategories.value.splice(selectedIndex, 1);
      }
      alert('Category deleted successfully!');
    }
  }
};

const handleBulkDelete = () => {
  if (selectedCategories.value.length === 0) return;

  if (confirm(`Are you sure you want to delete ${selectedCategories.value.length} categories?`)) {
    categories.value = categories.value.filter(
      category => !selectedCategories.value.includes(category.id)
    );
    selectedCategories.value = [];
    alert('Categories deleted successfully!');
  }
};

const handleBulkStatusChange = () => {
  if (selectedCategories.value.length === 0) return;

  const newStatus = prompt('Enter new status (active/inactive):');
  if (newStatus && ['active', 'inactive'].includes(newStatus)) {
    categories.value = categories.value.map(category => {
      if (selectedCategories.value.includes(category.id)) {
        return { ...category, status: newStatus as 'active' | 'inactive' };
      }
      return category;
    });
    alert('Status updated successfully!');
  }
};

const handleExport = () => {
  // Mock export functionality
  const dataStr = JSON.stringify(filteredCategories.value, null, 2);
  const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr);

  const exportFileDefaultName = 'categories_export.json';

  const linkElement = document.createElement('a');
  linkElement.setAttribute('href', dataUri);
  linkElement.setAttribute('download', exportFileDefaultName);
  linkElement.click();

  alert('Export started!');
};

const handleRefresh = () => {
  loading.value = true;
  // Simulate API call
  setTimeout(() => {
    loading.value = false;
    console.log('Categories refreshed');
  }, 500);
};

// Initialize
onMounted(() => {
  console.log('CategoryList component mounted');
});

// Watch for filter changes and reset to page 1
watch([searchQuery, statusFilter], () => {
  currentPage.value = 1;
});
</script>

