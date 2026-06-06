<template>
  <div class="gap-6">
    <!-- Header with actions -->
    <div class="flex items-center justify-between pb-5">
      <div>
        <h1 class="text-2xl font-bold text-white">Products</h1>
        <p class="text-[var(--color-icon)]">Manage your product inventory</p>
      </div>
      <div class="flex items-center gap-3">
        <!-- Filter/Sort -->
        <div class="relative">
          <select
            v-model="sortBy"
            class="appearance-none text-[var(--color-icon)] border border-[var(--color-border)] rounded-lg px-4 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 cursor-pointer bg-[var(--color-card)]"
          >
            <option value="createdAt:desc">Newest first</option>
            <option value="price:asc">Price: Low to High</option>
            <option value="price:desc">Price: High to Low</option>
            <option value="name:asc">Name: A to Z</option>
          </select>
          <svg class="absolute right-3 top-3 w-5 h-5 text-[var(--color-icon)] pointer-events-none" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"/>
          </svg>
        </div>

        <!-- Search -->
        <div class="relative">
          <input
            type="search"
            v-model="searchQuery"
            placeholder="Search products..."
            class="border border-[var(--color-border)] rounded-lg pl-10 pr-4 py-2 w-64 bg-[var(--color-card)] text-[var(--color-icon)] focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 placeholder:text-gray-500"
          >
          <svg class="absolute left-3 top-2.5 w-5 h-5 text-[var(--color-icon)]" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd"/>
          </svg>
        </div>

        <!-- New Product Button -->
        <router-link
          to="/products/create"
          class="flex items-center gap-2 px-4 py-2 bg-[var(--primary-color)] text-white rounded-lg hover:bg-blue-700 transition-colors duration-200"
        >
          <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M10 3a1 1 0 00-1 1v5H4a1 1 0 100 2h5v5a1 1 0 102 0v-5h5a1 1 0 100-2h-5V4a1 1 0 00-1-1z" clip-rule="evenodd"/>
          </svg>
          <span>New Product</span>
        </router-link>
      </div>
    </div>

    <!-- Stats Bar -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 pb-4">
      <Card class="p-4 hover:bg-[var(--color-hover)] transition-all duration-200 hover:translate-y-[-2px]">
        <p class="text-sm text-[var(--color-icon)] font-medium">Total Products</p>
        <p class="text-2xl font-bold text-white">{{ stats.total }}</p>
      </Card>
      <Card class="p-4 hover:bg-[var(--color-hover)] transition-colors duration-200 hover:translate-y-[-2px]">
        <p class="text-sm text-[var(--color-icon)] font-medium">In Stock</p>
        <p class="text-2xl font-bold text-white">{{ stats.inStock }}</p>
        <p class="text-xs text-green-500 mt-1">{{ Math.round((stats.inStock / stats.total) * 100) || 0 }}% of total</p>
      </Card>
      <Card class="p-4 hover:bg-[var(--color-hover)] transition-colors duration-200 hover:translate-y-[-2px]">
        <p class="text-sm text-[var(--color-icon)] font-medium">Low Stock</p>
        <p class="text-2xl font-bold text-white">{{ stats.lowStock }}</p>
        <p class="text-xs text-yellow-500 mt-1">Needs attention</p>
      </Card>
      <Card class="p-4 hover:bg-[var(--color-hover)] transition-colors duration-200 hover:translate-y-[-2px]">
        <p class="text-sm text-[var(--color-icon)] font-medium">Out of Stock</p>
        <p class="text-2xl font-bold text-white">{{ stats.outOfStock }}</p>
        <p class="text-xs text-red-500 mt-1">Requires restocking</p>
      </Card>
    </div>

    <!-- Products Table -->
    <Card class="overflow-hidden">
      <div class="border-b border-[var(--color-border)] px-4 py-3 bg-[var(--color-card)]">
        <h2 class="text-lg font-semibold text-white">Product Inventory</h2>
        <p class="text-sm text-[var(--color-icon)]">Showing {{ paginatedProducts.length }} of {{ filteredProducts.length }} products</p>
      </div>

      <div class="overflow-x-auto">
        <Table>
          <template #header>
            <tr class="bg-[var(--color-card)] text-[var(--color-icon)]">
              <th class="py-3 px-4 text-left font-semibold text-sm">Product</th>
              <th class="py-3 px-4 text-left font-semibold text-sm">Category</th>
              <th class="py-3 px-4 text-left font-semibold text-sm">Price</th>
              <th class="py-3 px-4 text-left font-semibold text-sm">Stock</th>
              <th class="py-3 px-4 text-left font-semibold text-sm">Status</th>
              <th class="py-3 px-4 text-left font-semibold text-sm">Actions</th>
            </tr>
          </template>
          <template #body>
            <tr
              v-for="product in paginatedProducts"
              :key="product.id"
              class="hover:bg-[var(--color-card)] transition-colors duration-150 border-b border-[var(--color-border)] last:border-b-0"
            >
              <td class="py-4 px-4">
                <div class="flex items-center gap-3">
                  <div class="relative">
                    <img
                      :src="product.imageUrl || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                      :alt="product.name"
                      class="w-12 h-12 rounded-lg object-cover border border-[var(--color-border)]"
                    >
                    <div
                      v-if="product.stock === 0"
                      class="absolute -top-1 -right-1 w-5 h-5 bg-red-500 rounded-full flex items-center justify-center"
                    >
                      <svg class="w-3 h-3 text-white" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd"/>
                      </svg>
                    </div>
                    <div
                      v-else-if="product.stock <= 10"
                      class="absolute -top-1 -right-1 w-5 h-5 bg-yellow-500 rounded-full flex items-center justify-center"
                    >
                      <span class="text-xs font-bold text-white">!</span>
                    </div>
                  </div>
                  <div>
                    <p class="font-medium text-white hover:text-[var(--primary-color)] transition-colors duration-150 cursor-pointer" @click="viewProduct(product.id)">
                      {{ product.name }}
                    </p>
                    <div class="flex items-center gap-2 mt-1">
                      <span class="text-xs text-gray-400 bg-gray-800/50 px-2 py-0.5 rounded">{{ formatSku(product.sku) }}</span>
                      <span class="text-xs text-gray-400">{{ formatDate(product.createdAt) }}</span>
                    </div>
                  </div>
                </div>
              </td>
              <td class="py-4 px-2">
                <Badge
                  class="hover:bg-opacity-80 transition-colors duration-150 cursor-default px-3 py-1"
                  :class="{
                    'bg-blue-500/20 text-blue-400 border-blue-500/30': product.category === 'Electronics',
                    'bg-green-500/20 text-green-400 border-green-500/30': product.category === 'Clothing',
                    'bg-purple-500/20 text-purple-400 border-purple-500/30': product.category === 'Home & Kitchen',
                    'bg-amber-500/20 text-amber-400 border-amber-500/30': product.category === 'Fitness',
                    'bg-indigo-500/20 text-indigo-400 border-indigo-500/30': product.category === 'Footwear',
                    'bg-pink-500/20 text-pink-400 border-pink-500/30': product.category === 'Accessories'
                  }"
                >
                  {{ product.category }}
                </Badge>
              </td>
              <td class="py-4 px-4">
                <div class="flex flex-col">
                  <span class="font-medium text-white">{{ formatCurrency(product.price) }}</span>
                  <span
                    v-if="product.price > 100"
                    class="text-xs text-amber-500 mt-1"
                  >
                    <svg class="w-3 h-3 inline mr-1" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M12 7a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0V8.414l-4.293 4.293a1 1 0 01-1.414 0L8 10.414l-4.293 4.293a1 1 0 01-1.414-1.414l5-5a1 1 0 011.414 0L11 10.586 14.586 7H12z" clip-rule="evenodd"/>
                    </svg>
                    Premium
                  </span>
                </div>
              </td>
              <td class="py-4 px-4">
                <div class="flex items-center gap-3">
                  <div class="flex-1">
                    <div class="flex justify-between text-sm mb-1">
                      <span class="text-[var(--color-icon)]">{{ product.stock }} units</span>
                      <span
                        class="font-medium"
                        :class="{
                          'text-green-500': product.stock > 20,
                          'text-yellow-500': product.stock <= 20 && product.stock > 0,
                          'text-red-500': product.stock === 0
                        }"
                      >
                        {{ getStockLevel(product.stock) }}
                      </span>
                    </div>
                    <div class="w-full h-2 bg-gray-800 rounded-full overflow-hidden">
                      <div
                        class="h-full transition-all duration-500 ease-out"
                        :class="{
                          'bg-gradient-to-r from-green-500 to-emerald-400': product.stock > 20,
                          'bg-gradient-to-r from-yellow-500 to-amber-400': product.stock <= 20 && product.stock > 0,
                          'bg-gradient-to-r from-red-500 to-rose-400': product.stock === 0
                        }"
                        :style="{ width: Math.min((product.stock / 100) * 100, 100) + '%' }"
                      ></div>
                    </div>
                  </div>
                </div>
              </td>
              <td class="py-4 px-4">
                <div class="flex items-center gap-2">
                  <Badge
                    :variant="product.status === 'active' ? 'success' : 'secondary'"
                    class="px-3 py-1"
                  >
                    <span class="flex items-center gap-1.5">
                      <span
                        class="w-2 h-2 rounded-full"
                        :class="{
                          'bg-green-500': product.status === 'active',
                          'bg-gray-500': product.status !== 'active'
                        }"
                      ></span>
                      {{ product.status }}
                    </span>
                  </Badge>
                </div>
              </td>
              <td class="py-4 px-4">
                <div class="flex items-center gap-1">
                  <button
                    @click="editProduct(product.id)"
                    class="p-2 text-[var(--color-icon)] hover:text-blue-500 hover:bg-blue-500/10 rounded-lg transition-all duration-150"
                    title="Edit product"
                  >
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
                    </svg>
                  </button>
                  <button
                    @click="viewProduct(product.id)"
                    class="p-2 text-[var(--color-icon)] hover:text-gray-300 hover:bg-gray-500/10 rounded-lg transition-all duration-150"
                    title="View details"
                  >
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                      <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                    </svg>
                  </button>
                  <button
                    @click="duplicateProduct(product.id)"
                    class="p-2 text-[var(--color-icon)] hover:text-purple-500 hover:bg-purple-500/10 rounded-lg transition-all duration-150"
                    title="Duplicate product"
                  >
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path d="M7 9a2 2 0 012-2h6a2 2 0 012 2v6a2 2 0 01-2 2H9a2 2 0 01-2-2V9z"/>
                      <path d="M5 3a2 2 0 00-2 2v6a2 2 0 002 2V5h8a2 2 0 00-2-2H5z"/>
                    </svg>
                  </button>
                  <button
                    @click="deleteProduct(product.id)"
                    class="p-2 text-[var(--color-icon)] hover:text-red-500 hover:bg-red-500/10 rounded-lg transition-all duration-150"
                    title="Delete product"
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
        v-if="products.length === 0"
        title="No products found"
        description="Get started by adding your first product."
        action-text="Add Product"
        action-to="/products/create"
        class="py-12"
      />

      <!-- Pagination -->
      <Pagination
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-items="filteredProducts.length"
        :items-per-page="itemsPerPage"
        @page-change="onPageChange"
      />
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import Table from '@/components/ui/Table.vue';
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

<style scoped>
/* Custom scrollbar for table */
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

/* Smooth transitions */
tr {
  transition: all 0.15s ease-out;
}

/* Image hover effect */
img {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

img:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* Button focus states */
button:focus {
  outline: 2px solid var(--primary-color);
  outline-offset: 2px;
}

/* Gradient border for premium items */
.premium-border {
  position: relative;
}

.premium-border::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: 8px;
  padding: 1px;
  background: linear-gradient(45deg, #f59e0b, #d97706);
  -webkit-mask:
    linear-gradient(#fff 0 0) content-box,
    linear-gradient(#fff 0 0);
  mask:
    linear-gradient(#fff 0 0) content-box,
    linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask-composite: exclude;
}
</style>
