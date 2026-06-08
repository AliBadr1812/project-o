<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Products</h1>
        <p class="page-subtitle">Manage your product inventory</p>
      </div>
      <div class="flex items-center gap-3">
        <button @click="exportProducts" class="btn-glass text-sm">
          <i class="fas fa-download text-xs mr-1"></i>Export CSV
        </button>
        <router-link to="/products/create" class="btn-accent text-sm">
          <i class="fas fa-plus text-xs mr-1"></i>
          New Product
        </router-link>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-b"><i class="fas fa-box"></i></div>
            <span class="badge badge-info">All</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Total Products</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.total }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">In your catalog</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-g"><i class="fas fa-circle-check"></i></div>
            <span class="badge badge-success">{{ Math.round((stats.inStock / stats.total) * 100) || 0 }}%</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">In Stock</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.inStock }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Of total catalog</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-o"><i class="fas fa-triangle-exclamation"></i></div>
            <span class="badge badge-warning">Needs attention</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Low Stock</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.lowStock }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Below threshold</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-r"><i class="fas fa-xmark"></i></div>
            <span class="badge badge-danger">Restock needed</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Out of Stock</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.outOfStock }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Requires restocking</p>
        </div>
      </Card>
    </div>

    <!-- Products Table -->
    <Card class="overflow-hidden">
      <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
        <div class="flex items-center justify-between mb-3">
          <div>
            <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Product Inventory</h2>
            <p class="text-xs mt-0.5" style="color: var(--text-muted);">
              <span v-if="loading">Loading…</span>
              <span v-else>Showing {{ paginatedProducts.length }} of {{ filteredProducts.length }} products</span>
            </p>
          </div>
        </div>
        <div class="flex items-center gap-3 flex-wrap">
          <div class="relative flex-1 min-w-[200px]">
            <i class="fas fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted)"></i>
            <input v-model="searchQuery" type="text" placeholder="Search products…" class="glass-input w-full pl-9 text-sm">
          </div>
          <select v-model="sortBy" class="glass-select text-sm">
            <option value="createdAt:desc">Newest first</option>
            <option value="price:asc">Price: Low to High</option>
            <option value="price:desc">Price: High to Low</option>
            <option value="name:asc">Name: A to Z</option>
          </select>
        </div>
      </div>

      <!-- Error state -->
      <div v-if="error" class="px-6 py-8 text-center">
        <i class="fas fa-triangle-exclamation text-2xl mb-2" style="color: var(--ni-orange);"></i>
        <p class="text-sm font-medium mb-1" style="color: var(--text-primary);">Could not load products</p>
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
                <th>Product</th>
                <th>Category</th>
                <th>Price</th>
                <th>Stock</th>
                <th>Status</th>
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
              <tr v-for="product in paginatedProducts" :key="product.id">
                <td>
                  <input type="checkbox" :value="product.id" v-model="selectedIds"
                    style="accent-color:var(--accent);width:14px;height:14px;" />
                </td>
                <td>
                  <div class="flex items-center gap-3">
                    <div class="relative">
                      <img
                        :src="product.images?.[0] || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                        :alt="product.name"
                        class="w-11 h-11 rounded-lg object-cover"
                        style="border: 1px solid var(--glass-border);"
                      >
                      <div v-if="product.stock === 0"
                        class="absolute -top-1 -right-1 w-4 h-4 bg-red-500 rounded-full flex items-center justify-center">
                        <i class="fas fa-xmark text-[8px] text-white"></i>
                      </div>
                      <div v-else-if="product.stock <= 10"
                        class="absolute -top-1 -right-1 w-4 h-4 bg-yellow-500 rounded-full flex items-center justify-center">
                        <span class="text-[8px] font-bold text-white">!</span>
                      </div>
                    </div>
                    <div>
                      <p class="td-primary cursor-pointer" @click="viewProduct(product.id)">{{ product.name }}</p>
                      <div class="flex items-center gap-2 mt-0.5">
                        <span class="text-xs px-2 py-0.5 rounded" style="color: var(--text-muted); background: var(--glass-bg);">{{ formatSku(product.sku) }}</span>
                        <span class="text-xs" style="color: var(--text-muted);">{{ formatDate(product.createdAt) }}</span>
                      </div>
                    </div>
                  </div>
                </td>
                <td><Badge variant="info">{{ product.categories || '—' }}</Badge></td>
                <td>
                  <span class="td-accent">{{ formatCurrency(product.price) }}</span>
                  <p v-if="product.price > 100" class="text-xs mt-0.5" style="color: var(--ni-orange);">
                    <i class="fas fa-arrow-trend-up mr-1"></i>Premium
                  </p>
                </td>
                <td>
                  <div>
                    <div class="flex justify-between text-xs mb-1">
                      <span style="color: var(--text-secondary);">{{ product.stock }} units</span>
                      <span :style="product.stock > 20 ? 'color:var(--ni-green)' : product.stock > 0 ? 'color:var(--ni-orange)' : 'color:var(--ni-red)'">
                        {{ getStockLevel(product.stock) }}
                      </span>
                    </div>
                    <div class="progress-track">
                      <div class="progress-fill" :style="{
                        width: Math.min((product.stock / 100) * 100, 100) + '%',
                        background: product.stock > 20 ? 'var(--progress-primary)' : product.stock > 0 ? 'var(--ni-orange)' : 'var(--ni-red)'
                      }"></div>
                    </div>
                  </div>
                </td>
                <td>
                  <Badge :variant="product.status === 'active' ? 'success' : 'secondary'">{{ product.status }}</Badge>
                </td>
                <td>
                  <div class="flex items-center gap-1">
                    <button @click="viewProduct(product.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="View">
                      <i class="fas fa-eye"></i>
                    </button>
                    <button @click="editProduct(product.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Edit">
                      <i class="fas fa-pen"></i>
                    </button>
                    <button @click="duplicateProduct(product.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Duplicate">
                      <i class="fas fa-copy"></i>
                    </button>
                    <button @click="deleteProduct(product.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Delete">
                      <i class="fas fa-trash"></i>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <EmptyState v-if="!loading && products.length === 0"
          title="No products found"
          description="Get started by adding your first product."
          action-text="Add Product" action-to="/products/create" class="py-12" />

        <Pagination
          :current-page="currentPage"
          :total-pages="totalPages"
          :total-items="filteredProducts.length"
          :items-per-page="itemsPerPage"
          @page-change="onPageChange"
        />
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
        <button @click="bulkExport" class="btn-glass text-sm flex items-center gap-1.5">
          <i class="fas fa-download text-xs"></i>Export
        </button>
        <button @click="bulkDelete" class="btn-glass text-sm flex items-center gap-1.5"
          style="color: var(--ni-red);">
          <i class="fas fa-trash text-xs"></i>Delete
        </button>
        <button @click="selectedIds = []" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Clear selection">
          <i class="fas fa-xmark"></i>
        </button>
      </div>
    </Transition>
  </div>
</template>

<style scoped>
.bulk-bar-enter-active { transition: all 0.3s cubic-bezier(0.34,1.56,0.64,1); }
.bulk-bar-leave-active { transition: all 0.2s ease; }
.bulk-bar-enter-from,
.bulk-bar-leave-to   { opacity: 0; transform: translateX(-50%) translateY(16px) scale(0.95); }
</style>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import EmptyState from '@/components/shared/EmptyState.vue';
import Pagination from '@/components/ui/Pagination.vue';
import { formatCurrency, formatDate, formatSku } from '@/utils/formatters';
import { exportToCsv, datestampedFilename } from '@/utils/csvExport';
import { useProductStore } from '@/stores/productStore';
import { useToast } from '@/composables/useToast';
import { useConfirm } from '@/composables/useConfirm';
import type { Product } from '@/types/product';

const router  = useRouter();
const store   = useProductStore();
const toast   = useToast();
const { confirm } = useConfirm();
const { items: products, loading, error } = storeToRefs(store);

// ── UI state ──────────────────────────────────────────────────────────────
const sortBy      = ref('createdAt:desc');
const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 5;
const selectedIds = ref<number[]>([]);

const selectAll = computed({
  get: () => paginatedProducts.value.length > 0 &&
             paginatedProducts.value.every(p => selectedIds.value.includes(p.id)),
  set: (v: boolean) => {
    const ids = paginatedProducts.value.map(p => p.id);
    if (v) selectedIds.value = [...new Set([...selectedIds.value, ...ids])];
    else   selectedIds.value = selectedIds.value.filter(id => !ids.includes(id));
  },
});

onMounted(() => store.fetchAll());

// ── Stats ─────────────────────────────────────────────────────────────────
const stats = computed(() => ({
  total:      products.value.length,
  inStock:    products.value.filter(p => p.stock > 10).length,
  lowStock:   products.value.filter(p => p.stock > 0 && p.stock <= 10).length,
  outOfStock: products.value.filter(p => p.stock === 0).length,
}));

// ── Filtering + sorting ───────────────────────────────────────────────────
const filteredProducts = computed(() => {
  let list = [...products.value];

  if (searchQuery.value) {
    const q = searchQuery.value.toLowerCase();
    list = list.filter(p =>
      p.name.toLowerCase().includes(q) ||
      (p.sku ?? '').toLowerCase().includes(q) ||
      (p.categories ?? '').toLowerCase().includes(q)
    );
  }

  const [field, order] = sortBy.value.split(':');
  list.sort((a, b) => {
    let av = a[field as keyof Product] as string | number;
    let bv = b[field as keyof Product] as string | number;
    if (field === 'createdAt') { av = new Date(av ?? 0).getTime(); bv = new Date(bv ?? 0).getTime(); }
    return order === 'asc' ? (av > bv ? 1 : -1) : (av < bv ? 1 : -1);
  });

  return list;
});

// ── Pagination ────────────────────────────────────────────────────────────
const totalPages = computed(() => Math.ceil(filteredProducts.value.length / itemsPerPage));
const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredProducts.value.slice(start, start + itemsPerPage);
});

watch([searchQuery, sortBy], () => { currentPage.value = 1; });

// ── Helpers ───────────────────────────────────────────────────────────────
const getStockLevel = (stock: number) => {
  if (stock > 20) return 'High';
  if (stock > 10) return 'Good';
  if (stock > 0)  return 'Low';
  return 'Out';
};

// ── Actions ───────────────────────────────────────────────────────────────
const onPageChange = (page: number) => {
  currentPage.value = page;
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const viewProduct = (id: number) => router.push(`/products/${id}`);
const editProduct = (id: number) => router.push(`/products/${id}/edit`);

const deleteProduct = async (id: number) => {
  const product = products.value.find(p => p.id === id);
  const ok = await confirm({
    title:       'Delete product',
    message:     `"${product?.name ?? 'This product'}" will be permanently removed.`,
    detail:      'This action cannot be undone.',
    confirmText: 'Delete',
    variant:     'danger',
  });
  if (!ok) return;
  try {
    await store.remove(id);
    toast.success('Product deleted successfully');
  } catch (e: unknown) {
    toast.error(e instanceof Error ? e.message : 'Delete failed', 'Error');
  }
};

const exportProducts = () => {
  exportToCsv(
    datestampedFilename('products'),
    filteredProducts.value,
    ['id', 'name', 'sku', 'categories', 'price', 'stock', 'status', 'createdAt'],
  );
  toast.success(`Exported ${filteredProducts.value.length} products to CSV`);
};

const duplicateProduct = (id: number) => {
  const src = products.value.find(p => p.id === id);
  if (!src) return;
  const copy: Product = {
    ...src,
    id: Math.max(...products.value.map(p => p.id)) + 1,
    name: `${src.name} (Copy)`,
    sku:  `${src.sku}-COPY`,
    createdAt: new Date().toISOString(),
  };
  store.prependItem(copy);
  toast.info(`"${src.name}" duplicated`);
};

// ── Bulk actions ──────────────────────────────────────────────────────────
const bulkDelete = async () => {
  if (!selectedIds.value.length) return;
  const ok = await confirm({
    title:       'Delete products',
    message:     `Permanently delete ${selectedIds.value.length} selected products?`,
    confirmText: `Delete ${selectedIds.value.length}`,
    variant:     'danger',
  });
  if (!ok) return;
  for (const id of [...selectedIds.value]) {
    try { await store.remove(id); } catch { /* skip */ }
  }
  toast.success(`${selectedIds.value.length} products deleted`);
  selectedIds.value = [];
};

const bulkExport = () => {
  const selected = filteredProducts.value.filter(p => selectedIds.value.includes(p.id));
  exportToCsv(datestampedFilename('products-selected'), selected, ['id', 'name', 'sku', 'categories', 'price', 'stock', 'status']);
  toast.success(`Exported ${selected.length} products to CSV`);
};
</script>
