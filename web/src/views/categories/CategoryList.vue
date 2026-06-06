<template>
  <div class="gap-6">
    <!-- Header with actions -->
    <div class="flex items-center justify-between pb-5">
      <div>
        <h1 class="page-title">Categories</h1>
        <p class="text-[var(--text-secondary)]">Manage your product categories</p>
      </div>
      <div class="flex items-center gap-3">
        <!-- Filter by Status -->
        <div class="relative text-[var(--text-secondary)]">
            <select
                v-model="statusFilter"
                class="appearance-none text-[var(--text-secondary)] border border-[var(--glass-border)] rounded-lg px-4 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-accent focus:border-accent cursor-pointer"
            >
                <option value="">All Status</option>
                <option value="active">Active</option>
                <option value="inactive">Inactive</option>
            </select>
            <i class="fas fa-angle-down fa-1x absolute right-4 top-4"></i>
        </div>

        <!-- Search -->
        <div class="relative">
          <input
            type="search"
            v-model="searchQuery"
            placeholder="Search categories..."
            class="border border-[var(--glass-border)] rounded-lg pl-10 pr-4 py-2 w-64 bg-[var(--glass-bg)] text-[var(--text-secondary)] focus:outline-none focus:ring-2 focus:ring-accent focus:border-accent"
          >
          <i class="fas fa-search absolute left-4 top-3.5 text-[var(--text-secondary)]"></i>
        </div>

        <!-- New Category Button -->
        <button
          @click="handleCreate"
          class="flex items-center gap-2 px-4 py-2 bg-[var(--accent)] text-[var(--text-primary)] rounded-lg hover:bg-blue-700"
        >
          <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M10 3a1 1 0 00-1 1v5H4a1 1 0 100 2h5v5a1 1 0 102 0v-5h5a1 1 0 100-2h-5V4a1 1 0 00-1-1z" clip-rule="evenodd"/>
          </svg>
          <span>New Category</span>
        </button>
      </div>
    </div>

    <!-- Stats Bar -->
    <div class="grid grid-cols-4 gap-4 pb-4">
      <Card class="p-4 hover:bg-[rgba(255,255,255,0.35)] transition-all duration-200 hover:translate-y-[-2px]">
        <p class="text-sm text-[var(--text-secondary)] font-medium">Total Categories</p>
        <p class="page-title">{{ stats.total }}</p>
      </Card>
      <Card class="p-4 hover:bg-[rgba(255,255,255,0.35)] transition-all duration-200 hover:translate-y-[-2px]">
        <p class="text-sm text-[var(--text-secondary)] font-medium">Active</p>
        <p class="page-title">{{ stats.active }}</p>
      </Card>
      <Card class="p-4 hover:bg-[rgba(255,255,255,0.35)] transition-all duration-200 hover:translate-y-[-2px]">
        <p class="text-sm text-[var(--text-secondary)] font-medium">Inactive</p>
        <p class="page-title">{{ stats.inactive }}</p>
      </Card>
      <Card class="p-4 hover:bg-[rgba(255,255,255,0.35)] transition-all duration-200 hover:translate-y-[-2px]">
        <p class="text-sm text-[var(--text-secondary)] font-medium">Avg Products</p>
        <p class="page-title">{{ stats.avgProducts }}</p>
      </Card>
    </div>

    <!-- Categories Table -->
    <Card>
      <Table class="rounded-xl">
        <template #header>
          <tr class="bg-[var(--glass-bg)] text-[var(--text-secondary)]">
            <th class="py-3 px-4 text-left w-12">
              <input type="checkbox" v-model="selectAll" />
            </th>
            <th class="py-3 px-4 text-left">Name</th>
            <th class="py-3 px-4 text-left">Description</th>
            <th class="py-3 px-4 text-left">Products</th>
            <th class="py-3 px-4 text-left">Status</th>
            <th class="py-3 px-4 text-left">Created</th>
            <th class="py-3 px-4 text-left">Actions</th>
          </tr>
        </template>
        <template #body>
          <tr v-for="category in paginatedCategories" :key="category.id" class="hover:bg-[var(--glass-bg)]">
            <td class="py-3 px-4">
              <input
                type="checkbox"
                :value="category.id"
                v-model="selectedCategories"
              />
            </td>
            <td class="py-3 px-4">
              <div class="flex items-center gap-3">
                <div
                  v-if="category.icon"
                  class="w-10 h-10 rounded-lg bg-blue-500/10 flex items-center justify-center"
                >
                  <i :class="category.icon" class="text-[var(--accent)] text-lg"></i>
                </div>
                <div v-else class="w-10 h-10 rounded-lg bg-gray-500/10 flex items-center justify-center">
                  <svg class="w-5 h-5 text-[var(--text-secondary)]" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M4 4a2 2 0 00-2 2v8a2 2 0 002 2h12a2 2 0 002-2V8a2 2 0 00-2-2h-5L9 4H4z" clip-rule="evenodd"/>
                  </svg>
                </div>
                <div>
                  <p class="font-medium text-[var(--text-secondary)]">{{ category.name }}</p>
                  <p class="text-sm text-[var(--text-secondary)]">ID: {{ category.id }}</p>
                </div>
              </div>
            </td>
            <td class="py-3 px-4 text-[var(--text-secondary)]">
              {{ truncateText(category.description, 60) }}
            </td>
            <td class="py-3 px-4">
              <div class="flex items-center gap-2">
                <span class="text-[var(--text-secondary)]">{{ category.productCount }}</span>
                <div class="w-24 h-2 bg-[var(--glass-bg)] rounded-full overflow-hidden border border-[var(--glass-border)]">
                  <div
                    class="h-full bg-blue-500"
                    :style="{ width: Math.min((category.productCount / maxProducts) * 100, 100) + '%' }"
                  ></div>
                </div>
              </div>
            </td>
            <td class="py-3 px-4">
              <Badge
                :variant="category.status === 'active' ? 'success' : 'secondary'"
              >
                {{ category.status }}
              </Badge>
            </td>
            <td class="py-3 px-4 text-[var(--text-secondary)]">
              {{ formatDate(category.createdAt) }}
            </td>
            <td class="py-3 px-4">
              <div class="flex items-center gap-2">
                <button
                  @click="handleEdit(category.id)"
                  class="p-1 text-[var(--text-secondary)] hover:text-[var(--accent)]"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
                  </svg>
                </button>
                <button
                  @click="handleView(category.id)"
                  class="p-1 text-[var(--text-secondary)] hover:text-[var(--text-secondary)]"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                    <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                  </svg>
                </button>
                <button
                  @click="handleDelete(category.id)"
                  class="p-1 text-[var(--text-secondary)] hover:text-red-800"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd"/>
                  </svg>
                </button>
              </div>
            </td>
          </tr>
        </template>
      </Table>

      <!-- Empty State -->
      <EmptyState
        v-if="categories.length === 0"
        title="No categories found"
        description="Get started by creating your first category."
        action-text="Create Category"
        @action="handleCreate"
      />

      <!-- Pagination -->
      <Pagination
        v-if="filteredCategories.length > 0"
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-items="filteredCategories.length"
        :items-per-page="itemsPerPage"
        @page-change="handlePageChange"
      />
    </Card>

    <!-- Bulk Actions Bar -->
    <div v-if="selectedCategories.length > 0" class="fixed bottom-6 right-6">
      <Card class="shadow-xl">
        <div class="flex items-center gap-4 p-3">
          <span class="text-[var(--text-secondary)] font-medium">
            {{ selectedCategories.length }} selected
          </span>
          <button
            @click="handleBulkStatusChange"
            class="px-3 py-1.5 border border-[var(--glass-border)] rounded-lg text-[var(--text-secondary)] hover:bg-[var(--glass-bg)]"
          >
            Change Status
          </button>
          <button
            @click="handleBulkDelete"
            class="px-3 py-1.5 bg-red-600 text-[var(--text-primary)] rounded-lg hover:bg-red-700"
          >
            Delete Selected
          </button>
        </div>
      </Card>
    </div>
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

<style scoped>
.category-list {
  min-height: 100vh;
}

input[type="checkbox"] {
  accent-color: var(--accent);
  width: 16px;
  height: 16px;
  cursor: pointer;
}

.fixed {
  z-index: 50;
}

/* Icon styling */
.fas {
  font-size: 0.875rem; /* 14px */
}
</style>
