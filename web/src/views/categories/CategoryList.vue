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
            <p class="text-xs mt-0.5" style="color: var(--text-muted);">
              <span v-if="loading">Loading…</span>
              <span v-else>{{ filteredCategories.length }} categories</span>
            </p>
          </div>
        </div>
        <div class="flex items-center gap-3 flex-wrap">
          <div class="relative flex-1 min-w-[200px]">
            <i class="fas fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted)"></i>
            <input v-model="searchQuery" type="text" placeholder="Search categories…" class="glass-input w-full pl-9 text-sm">
          </div>
          <select v-model="statusFilter" class="glass-select text-sm">
            <option value="">All Status</option>
            <option value="active">Active</option>
            <option value="inactive">Inactive</option>
          </select>
        </div>
      </div>

      <!-- Error state -->
      <div v-if="error" class="px-6 py-8 text-center">
        <i class="fas fa-triangle-exclamation text-2xl mb-2" style="color: var(--ni-orange);"></i>
        <p class="text-sm font-medium mb-1" style="color: var(--text-primary);">Could not load categories</p>
        <p class="text-xs mb-3" style="color: var(--text-muted);">{{ error }}</p>
        <button @click="store.fetchAll()" class="btn-glass text-sm">Try again</button>
      </div>

      <template v-else>
        <div class="overflow-x-auto">
          <table class="glass-table w-full">
            <thead>
              <tr>
                <th class="w-10">
                  <input type="checkbox" v-model="selectAll" style="accent-color: var(--accent); width:14px; height:14px;" />
                </th>
                <th>Name</th>
                <th>Description</th>
                <th>Products</th>
                <th>Status</th>
                <th>Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="loading">
                <td colspan="7" class="py-10 text-center">
                  <i class="fas fa-spinner fa-spin mr-2" style="color: var(--text-muted);"></i>
                  <span style="color: var(--text-muted);">Loading…</span>
                </td>
              </tr>
              <tr v-for="category in paginatedCategories" :key="category.id">
                <td>
                  <input type="checkbox" :value="category.id" v-model="selectedIds" style="accent-color:var(--accent);width:14px;height:14px;" />
                </td>
                <td>
                  <div class="flex items-center gap-3">
                    <div class="w-9 h-9 rounded-lg flex items-center justify-center" style="background: rgba(var(--accent-rgb),0.10);">
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
                    <span class="font-medium text-sm" style="color: var(--text-primary);">{{ category.productCount ?? 0 }}</span>
                    <div class="progress-track w-20">
                      <div class="progress-fill" :style="{ width: Math.min(((category.productCount ?? 0) / maxProducts) * 100, 100) + '%', background: 'var(--progress-primary)' }"></div>
                    </div>
                  </div>
                </td>
                <td>
                  <Badge :variant="category.isActive ? 'success' : 'secondary'">{{ category.isActive ? 'active' : 'inactive' }}</Badge>
                </td>
                <td><span class="text-sm" style="color: var(--text-secondary);">{{ formatDate(category.createdAt) }}</span></td>
                <td>
                  <div class="flex items-center gap-1">
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

        <EmptyState v-if="!loading && categories.length === 0"
          title="No categories found"
          description="Get started by creating your first category."
          action-text="Create Category" @action="handleCreate" />

        <Pagination v-if="filteredCategories.length > 0"
          :current-page="currentPage" :total-pages="totalPages"
          :total-items="filteredCategories.length" :items-per-page="itemsPerPage"
          @page-change="handlePageChange" />
      </template>
    </Card>

    <!-- Bulk Actions -->
    <div v-if="selectedIds.length > 0" class="fixed bottom-6 right-6 z-50">
      <Card>
        <div class="flex items-center gap-4 p-3">
          <span class="text-sm font-medium" style="color: var(--text-secondary);">{{ selectedIds.length }} selected</span>
          <button @click="handleBulkDelete" class="btn-accent text-sm" style="background: var(--ni-red);">Delete Selected</button>
        </div>
      </Card>
    </div>
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
import { formatDate, truncateText } from '@/utils/formatters';
import { useCategoryStore } from '@/stores/categoryStore';

const router = useRouter();
const store = useCategoryStore();
const { items: categories, loading, error } = storeToRefs(store);

// ── UI state ──────────────────────────────────────────────────────────────
const searchQuery  = ref('');
const statusFilter = ref('');
const currentPage  = ref(1);
const itemsPerPage = 5;
const selectedIds  = ref<number[]>([]);

onMounted(() => store.fetchAll());

// ── Stats ─────────────────────────────────────────────────────────────────
const stats = computed(() => {
  const total    = categories.value.length;
  const active   = categories.value.filter(c => c.isActive).length;
  const inactive = categories.value.filter(c => !c.isActive).length;
  const avgProducts = total > 0
    ? Math.round(categories.value.reduce((s, c) => s + (c.productCount ?? 0), 0) / total)
    : 0;
  return { total, active, inactive, avgProducts };
});

const maxProducts = computed(() => Math.max(...categories.value.map(c => c.productCount ?? 0), 1));

// ── Filtering ─────────────────────────────────────────────────────────────
const filteredCategories = computed(() => {
  let list = [...categories.value];

  if (searchQuery.value) {
    const q = searchQuery.value.toLowerCase();
    list = list.filter(c =>
      c.name.toLowerCase().includes(q) ||
      (c.description ?? '').toLowerCase().includes(q)
    );
  }

  if (statusFilter.value === 'active')   list = list.filter(c => c.isActive);
  if (statusFilter.value === 'inactive') list = list.filter(c => !c.isActive);

  return list;
});

// ── Pagination ────────────────────────────────────────────────────────────
const totalPages = computed(() => Math.ceil(filteredCategories.value.length / itemsPerPage));
const paginatedCategories = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredCategories.value.slice(start, start + itemsPerPage);
});

watch([searchQuery, statusFilter], () => { currentPage.value = 1; });

// ── Select all ────────────────────────────────────────────────────────────
const selectAll = computed({
  get: () => paginatedCategories.value.length > 0 &&
             paginatedCategories.value.every(c => selectedIds.value.includes(c.id)),
  set: (v: boolean) => {
    const ids = paginatedCategories.value.map(c => c.id);
    if (v) selectedIds.value = [...new Set([...selectedIds.value, ...ids])];
    else   selectedIds.value = selectedIds.value.filter(id => !ids.includes(id));
  },
});

// ── Actions ───────────────────────────────────────────────────────────────
const handlePageChange = (page: number) => { currentPage.value = page; window.scrollTo({ top: 0, behavior: 'smooth' }); };
const handleCreate = () => router.push('/categories/create');
const handleEdit   = (id: number) => router.push(`/categories/edit/${id}`);

const handleDelete = async (id: number) => {
  if (!confirm('Are you sure you want to delete this category?')) return;
  try {
    await store.remove(id);
    selectedIds.value = selectedIds.value.filter(x => x !== id);
  } catch (e: any) {
    alert(e?.message ?? 'Delete failed');
  }
};

const handleBulkDelete = async () => {
  if (!selectedIds.value.length) return;
  if (!confirm(`Delete ${selectedIds.value.length} categories?`)) return;
  for (const id of [...selectedIds.value]) {
    try { await store.remove(id); } catch { /* skip */ }
  }
  selectedIds.value = [];
};
</script>
