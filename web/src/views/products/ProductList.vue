<template>
  <div class="gap-6">
    <!-- Header with actions -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-2xl font-bold">Products</h1>
        <p class="text-gray-600">Manage your product inventory</p>
      </div>
      <div class="flex items-center gap-3">
        <!-- Filter/Sort -->
        <select v-model="sortBy" class="border border-gray-300 rounded-lg px-4 py-2">
          <option value="createdAt:desc">Newest first</option>
          <option value="price:asc">Price: Low to High</option>
          <option value="price:desc">Price: High to Low</option>
          <option value="name:asc">Name: A to Z</option>
        </select>

        <!-- Search -->
        <div class="relative">
          <input
            type="search"
            v-model="searchQuery"
            placeholder="Search products..."
            class="border border-gray-300 rounded-lg pl-10 pr-4 py-2 w-64"
          >
          <svg class="absolute left-3 top-2.5 w-5 h-5 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd"/>
          </svg>
        </div>

        <!-- New Product Button -->
        <router-link to="/products/new" class="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M10 3a1 1 0 00-1 1v5H4a1 1 0 100 2h5v5a1 1 0 102 0v-5h5a1 1 0 100-2h-5V4a1 1 0 00-1-1z" clip-rule="evenodd"/>
          </svg>
          <span>New Product</span>
        </router-link>
      </div>
    </div>

    <!-- Stats Bar -->
    <div class="grid grid-cols-4 gap-4">
      <div class="bg-blue-50 p-4 rounded-lg">
        <p class="text-sm text-blue-600 font-medium">Total Products</p>
        <p class="text-2xl font-bold">{{ stats.total }}</p>
      </div>
      <div class="bg-green-50 p-4 rounded-lg">
        <p class="text-sm text-green-600 font-medium">In Stock</p>
        <p class="text-2xl font-bold">{{ stats.inStock }}</p>
      </div>
      <div class="bg-yellow-50 p-4 rounded-lg">
        <p class="text-sm text-yellow-600 font-medium">Low Stock</p>
        <p class="text-2xl font-bold">{{ stats.lowStock }}</p>
      </div>
      <div class="bg-red-50 p-4 rounded-lg">
        <p class="text-sm text-red-600 font-medium">Out of Stock</p>
        <p class="text-2xl font-bold">{{ stats.outOfStock }}</p>
      </div>
    </div>

    <!-- Products Table -->
    <Card>
      <Table>
        <template #header>
          <tr>
            <th class="py-3 px-4 text-left">Product</th>
            <th class="py-3 px-4 text-left">Category</th>
            <th class="py-3 px-4 text-left">Price</th>
            <th class="py-3 px-4 text-left">Stock</th>
            <th class="py-3 px-4 text-left">Status</th>
            <th class="py-3 px-4 text-left">Actions</th>
          </tr>
        </template>
        <template #body>
          <tr v-for="product in products" :key="product.id" class="hover:bg-gray-50">
            <td class="py-3 px-4">
              <div class="flex items-center gap-3">
                <img
                  :src="product.imageUrl || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                  :alt="product.name"
                  class="w-10 h-10 rounded-lg object-cover"
                >
                <div>
                  <p class="font-medium">{{ product.name }}</p>
                  <p class="text-sm text-gray-500">SKU: {{ product.sku }}</p>
                </div>
              </div>
            </td>
            <td class="py-3 px-4">
              <Badge>{{ product.category }}</Badge>
            </td>
            <td class="py-3 px-4 font-medium">
              {{ formatCurrency(product.price) }}
            </td>
            <td class="py-3 px-4">
              <div class="flex items-center gap-2">
                <span>{{ product.stock }}</span>
                <div class="w-24 h-2 bg-gray-200 rounded-full overflow-hidden">
                  <div
                    class="h-full"
                    :class="{
                      'bg-green-500': product.stock > 20,
                      'bg-yellow-500': product.stock <= 20 && product.stock > 0,
                      'bg-red-500': product.stock === 0
                    }"
                    :style="{ width: Math.min((product.stock / 100) * 100, 100) + '%' }"
                  ></div>
                </div>
              </div>
            </td>
            <td class="py-3 px-4">
              <Badge
                :variant="product.status === 'active' ? 'success' : 'secondary'"
              >
                {{ product.status }}
              </Badge>
            </td>
            <td class="py-3 px-4">
              <div class="flex items-center gap-2">
                <button
                  @click="editProduct(product.id)"
                  class="p-1 text-blue-600 hover:text-blue-800"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
                  </svg>
                </button>
                <button
                  @click="viewProduct(product.id)"
                  class="p-1 text-gray-600 hover:text-gray-800"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                    <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                  </svg>
                </button>
                <button
                  @click="deleteProduct(product.id)"
                  class="p-1 text-red-600 hover:text-red-800"
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
        v-if="products.length === 0"
        title="No products found"
        description="Get started by adding your first product."
        action-text="Add Product"
        action-to="/products/new"
      />

      <!-- Pagination -->
      <Pagination
        v-if="products.length > 0"
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-items="totalItems"
        @page-change="onPageChange"
      />
    </Card>
  </div>
</template>

<script setup lang="ts">
import Card from '@/components/ui/Card.vue';
import Table from '@/components/ui/Table.vue';
import Badge from '@/components/ui/Badge.vue';
import EmptyState from '@/components/shared/EmptyState.vue';
import Pagination from '@/components/ui/Pagination.vue';
import { formatCurrency } from '@/utils/formatters';
</script>
