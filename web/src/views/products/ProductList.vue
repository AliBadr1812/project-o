<template>
  <div class="gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Products</h1>
        <p class="page-subtitle">Manage your product inventory</p>
      </div>
      <div class="flex items-center gap-3">
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
            <p class="text-xs mt-0.5" style="color: var(--text-muted);">Showing {{ paginatedProducts.length }} of {{ filteredProducts.length }} products</p>
          </div>
        </div>
        <div class="flex items-center gap-3 flex-wrap">
          <div class="relative flex-1 min-w-[200px]">
            <i class="fas fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted)"></i>
            <input v-model="searchQuery" type="text" placeholder="Search products..." class="glass-input w-full pl-9 text-sm">
          </div>
          <select v-model="sortBy" class="glass-select text-sm">
            <option value="createdAt:desc">Newest first</option>
            <option value="price:asc">Price: Low to High</option>
            <option value="price:desc">Price: High to Low</option>
            <option value="name:asc">Name: A to Z</option>
          </select>
        </div>
      </div>

      <div class="overflow-x-auto">
        <table class="glass-table w-full">
          <thead>
            <tr>
              <th>Product</th>
              <th>Category</th>
              <th>Price</th>
              <th>Stock</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in paginatedProducts" :key="product.id">
              <td>
                <div class="flex items-center gap-3">
                  <div class="relative">
                    <img :src="product.imageUrl || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                      :alt="product.name" class="w-11 h-11 rounded-lg object-cover"
                      style="border: 1px solid var(--glass-border);">
                    <div v-if="product.stock === 0"
                      class="absolute -top-1 -right-1 w-4 h-4 bg-red-500 rounded-full flex items-center justify-center">
                      <i class="fas fa-xmark text-[8px]" style="color: var(--text-primary);"></i>
                    </div>
                    <div v-else-if="product.stock <= 10"
                      class="absolute -top-1 -right-1 w-4 h-4 bg-yellow-500 rounded-full flex items-center justify-center">
                      <span class="text-[8px] font-bold" style="color: var(--text-primary);">!</span>
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
              <td><Badge variant="info">{{ product.category }}</Badge></td>
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
                    <span :style="product.stock > 20 ? 'color: var(--ni-green)' : product.stock > 0 ? 'color: var(--ni-orange)' : 'color: var(--ni-red)'">
                      {{ getStockLevel(product.stock) }}
                    </span>
                  </div>
                  <div class="progress-track">
                    <div class="progress-fill" :style="{ width: Math.min((product.stock / 100) * 100, 100) + '%', background: product.stock > 20 ? 'var(--progress-primary)' : product.stock > 0 ? 'var(--ni-orange)' : 'var(--ni-red)' }"></div>
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

      <EmptyState v-if="products.length === 0" title="No products found"
        description="Get started by adding your first product."
        action-text="Add Product" action-to="/products/create" class="py-12" />

      <Pagination :current-page="currentPage" :total-pages="totalPages"
        :total-items="filteredProducts.length" :items-per-page="itemsPerPage"
        @page-change="onPageChange" />
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import EmptyState from '@/components/shared/EmptyState.vue';
import Pagination from '@/components/ui/Pagination.vue';
import { formatCurrency, formatDate, formatSku } from '@/utils/formatters';

const router = useRouter();

// State
const sortBy = ref('createdAt:desc');
const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 5;
const totalItems = ref(10); // Mock total

// Mock product data
const products = ref([
  {
    id: 1,
    name: 'Premium Wireless Headphones',
    sku: 'PHONES-001',
    category: 'Electronics',
    price: 149.99,
    stock: 25,
    status: 'active',
    imageUrl: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=400&fit=crop',
    createdAt: '2024-01-15T10:30:00Z'
  },
  {
    id: 2,
    name: 'Organic Cotton T-Shirt',
    sku: 'CLOTH-045',
    category: 'Clothing',
    price: 24.99,
    stock: 15,
    status: 'active',
    imageUrl: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop',
    createdAt: '2024-01-14T14:20:00Z'
  },
  {
    id: 3,
    name: 'Stainless Steel Water Bottle',
    sku: 'HOME-023',
    category: 'Home & Kitchen',
    price: 29.99,
    stock: 8,
    status: 'active',
    imageUrl: 'https://images.unsplash.com/photo-1523362628745-0c100150b504?w=400&h=400&fit=crop',
    createdAt: '2024-01-13T09:15:00Z'
  },
  {
    id: 4,
    name: 'Yoga Mat Premium',
    sku: 'FIT-078',
    category: 'Fitness',
    price: 39.99,
    stock: 0,
    status: 'inactive',
    imageUrl: 'https://images.unsplash.com/photo-1599901860904-17e6ed7083a0?w=400&h=400&fit=crop',
    createdAt: '2024-01-12T16:45:00Z'
  },
  {
    id: 5,
    name: 'Bluetooth Speaker',
    sku: 'ELECT-012',
    category: 'Electronics',
    price: 89.99,
    stock: 32,
    status: 'active',
    imageUrl: 'https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=400&h=400&fit=crop',
    createdAt: '2024-01-11T11:30:00Z'
  },
  {
    id: 6,
    name: 'Coffee Maker',
    sku: 'HOME-067',
    category: 'Home & Kitchen',
    price: 129.99,
    stock: 5,
    status: 'active',
    imageUrl: 'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400&h=400&fit=crop',
    createdAt: '2024-01-10T13:20:00Z'
  },
  {
    id: 7,
    name: 'Running Shoes',
    sku: 'SHOE-034',
    category: 'Footwear',
    price: 79.99,
    stock: 12,
    status: 'active',
    imageUrl: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&h=400&fit=crop',
    createdAt: '2024-01-09T15:10:00Z'
  },
  {
    id: 8,
    name: 'Backpack',
    sku: 'BAG-021',
    category: 'Accessories',
    price: 49.99,
    stock: 18,
    status: 'active',
    imageUrl: 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=400&fit=crop',
    createdAt: '2024-01-08T10:45:00Z'
  },
  {
    id: 9,
    name: 'Smart Watch',
    sku: 'ELECT-089',
    category: 'Electronics',
    price: 199.99,
    stock: 3,
    status: 'active',
    imageUrl: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=400&fit=crop',
    createdAt: '2024-01-07T14:30:00Z'
  },
  {
    id: 10,
    name: 'Desk Lamp',
    sku: 'HOME-045',
    category: 'Home & Kitchen',
    price: 34.99,
    stock: 22,
    status: 'active',
    imageUrl: 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=400&h=400&fit=crop',
    createdAt: '2024-01-06T09:20:00Z'
  }
]);

// Stats
const stats = computed(() => {
  const total = products.value.length;
  const inStock = products.value.filter(p => p.stock > 10).length;
  const lowStock = products.value.filter(p => p.stock > 0 && p.stock <= 10).length;
  const outOfStock = products.value.filter(p => p.stock === 0).length;

  return { total, inStock, lowStock, outOfStock };
});

// Filtered and sorted products
const filteredProducts = computed(() => {
  let filtered = [...products.value];

  // Apply search
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter(product =>
      product.name.toLowerCase().includes(query) ||
      product.sku.toLowerCase().includes(query) ||
      product.category.toLowerCase().includes(query)
    );
  }

  // Apply sorting
  const [field, order] = sortBy.value.split(':');
  filtered.sort((a, b) => {
    let aVal = a[field as keyof typeof a];
    let bVal = b[field as keyof typeof b];

    if (field === 'createdAt') {
      aVal = new Date(aVal as string).getTime();
      bVal = new Date(bVal as string).getTime();
    }

    if (order === 'asc') {
      return aVal > bVal ? 1 : -1;
    } else {
      return aVal < bVal ? 1 : -1;
    }
  });

  return filtered;
});

// Pagination
const totalPages = computed(() => Math.ceil(totalItems.value / itemsPerPage));
const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredProducts.value.slice(start, start + itemsPerPage);
});

const getStockLevel = (stock: number) => {
  if (stock > 20) return 'High';
  if (stock > 10) return 'Good';
  if (stock > 0) return 'Low';
  return 'Out';
};

// Methods
const onPageChange = (page: number) => {
  currentPage.value = page;
  // Scroll to top when page changes
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const editProduct = (id: number) => {
  router.push(`/products/edit/${id}`);
};

const viewProduct = (id: number) => {
  router.push(`/products/${id}`);
};

const deleteProduct = (id: number) => {
  if (confirm('Are you sure you want to delete this product?')) {
    const index = products.value.findIndex(p => p.id === id);
    if (index !== -1) {
      products.value.splice(index, 1);
      alert('Product deleted successfully!');
    }
  }
};

const duplicateProduct = (id: number) => {
  const product = products.value.find(p => p.id === id);
  if (product) {
    const newProduct = {
      ...product,
      id: Math.max(...products.value.map(p => p.id)) + 1,
      name: `${product.name} (Copy)`,
      sku: `${product.sku}-COPY`,
      createdAt: new Date().toISOString()
    };
    products.value.unshift(newProduct);
    alert('Product duplicated successfully!');
  }
};

// Initialize
onMounted(() => {
  console.log('ProductList component mounted');
});
</script>

