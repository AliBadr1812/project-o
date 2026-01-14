<template>
  <div class="max-w-4xl mx-auto">
    <!-- Header -->
    <div class="mb-6">
      <h1 class="text-2xl font-bold">{{ isEditing ? 'Edit Product' : 'Add New Product' }}</h1>
      <p class="text-gray-600">{{ isEditing ? 'Update product information' : 'Add a new product to your store' }}</p>
    </div>

    <!-- Form -->
    <Card>
      <form @submit.prevent="submitForm">
        <div class="gap-6">
          <!-- Basic Information -->
          <div>
            <h2 class="text-lg font-semibold mb-4">Basic Information</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- Product Name -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Product Name *
                </label>
                <input
                  v-model="product.name"
                  type="text"
                  required
                  class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  placeholder="Enter product name"
                >
              </div>

              <!-- SKU -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  SKU (Stock Keeping Unit) *
                </label>
                <input
                  v-model="product.sku"
                  type="text"
                  required
                  class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  placeholder="e.g., PROD-001"
                >
              </div>

              <!-- Category -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Category *
                </label>
                <select
                  v-model="product.categoryId"
                  required
                  class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                >
                  <option value="">Select a category</option>
                  <option v-for="category in categories" :key="category.id" :value="category.id">
                    {{ category.name }}
                  </option>
                </select>
              </div>

              <!-- Brand -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Brand
                </label>
                <input
                  v-model="product.brand"
                  type="text"
                  class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  placeholder="Enter brand name"
                >
              </div>
            </div>
          </div>

          <!-- Pricing -->
          <div>
            <h2 class="text-lg font-semibold mb-4">Pricing</h2>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
              <!-- Price -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Price *
                </label>
                <div class="relative">
                  <span class="absolute left-3 top-2.5 text-gray-500">$</span>
                  <input
                    v-model="product.price"
                    type="number"
                    step="0.01"
                    min="0"
                    required
                    class="w-full border border-gray-300 rounded-lg pl-8 pr-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    placeholder="0.00"
                  >
                </div>
              </div>

              <!-- Compare Price -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Compare at Price
                </label>
                <div class="relative">
                  <span class="absolute left-3 top-2.5 text-gray-500">$</span>
                  <input
                    v-model="product.compareAtPrice"
                    type="number"
                    step="0.01"
                    min="0"
                    class="w-full border border-gray-300 rounded-lg pl-8 pr-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    placeholder="0.00"
                  >
                </div>
              </div>

              <!-- Cost per Item -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Cost per Item
                </label>
                <div class="relative">
                  <span class="absolute left-3 top-2.5 text-gray-500">$</span>
                  <input
                    v-model="product.costPerItem"
                    type="number"
                    step="0.01"
                    min="0"
                    class="w-full border border-gray-300 rounded-lg pl-8 pr-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    placeholder="0.00"
                  >
                </div>
              </div>
            </div>
          </div>

          <!-- Inventory -->
          <div>
            <h2 class="text-lg font-semibold mb-4">Inventory</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- Stock Quantity -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Stock Quantity *
                </label>
                <input
                  v-model="product.stock"
                  type="number"
                  min="0"
                  required
                  class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  placeholder="0"
                >
              </div>

              <!-- Low Stock Threshold -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Low Stock Threshold
                </label>
                <input
                  v-model="product.lowStockThreshold"
                  type="number"
                  min="0"
                  class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  placeholder="10"
                >
                <p class="text-sm text-gray-500 mt-1">Get notified when stock drops below this number</p>
              </div>
            </div>
          </div>

          <!-- Images -->
          <div>
            <h2 class="text-lg font-semibold mb-4">Product Images</h2>
            <div class="gap-4">
              <!-- Main Image -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Main Image *
                </label>
                <div class="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center">
                  <div v-if="mainImagePreview" class="mb-4">
                    <img :src="mainImagePreview" alt="Main product image" class="mx-auto h-32 w-32 object-cover rounded-lg">
                  </div>
                  <div v-else class="text-gray-500 mb-4">
                    <svg class="mx-auto h-12 w-12" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                    </svg>
                  </div>
                  <label class="cursor-pointer">
                    <span class="text-blue-600 hover:text-blue-800 font-medium">Click to upload</span>
                    <input
                      type="file"
                      @change="handleMainImageUpload"
                      accept="image/*"
                      class="hidden"
                    >
                  </label>
                  <p class="text-sm text-gray-500 mt-2">PNG, JPG, GIF up to 10MB</p>
                </div>
              </div>

              <!-- Additional Images -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Additional Images
                </label>
                <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                  <div
                    v-for="(image, index) in additionalImagesPreview"
                    :key="index"
                    class="relative border border-gray-300 rounded-lg p-4 text-center"
                  >
                    <img :src="image" alt="Additional image" class="h-20 w-20 object-cover rounded mx-auto mb-2">
                    <button
                      @click="removeAdditionalImage(index)"
                      class="absolute -top-2 -right-2 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center"
                    >
                      <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"/>
                      </svg>
                    </button>
                  </div>
                  <div class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center cursor-pointer hover:bg-gray-50">
                    <label class="cursor-pointer">
                      <svg class="mx-auto h-8 w-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
                      </svg>
                      <span class="text-sm text-gray-600 block mt-2">Add Image</span>
                      <input
                        type="file"
                        @change="handleAdditionalImageUpload"
                        accept="image/*"
                        multiple
                        class="hidden"
                      >
                    </label>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Description -->
          <div>
            <h2 class="text-lg font-semibold mb-4">Description</h2>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Product Description *
              </label>
              <textarea
                v-model="product.description"
                rows="6"
                required
                class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                placeholder="Describe your product in detail..."
              ></textarea>
            </div>
          </div>

          <!-- Status -->
          <div>
            <h2 class="text-lg font-semibold mb-4">Status</h2>
            <div class="flex items-center gap-4">
              <label class="flex items-center">
                <input
                  v-model="product.status"
                  type="radio"
                  value="active"
                  class="h-4 w-4 text-blue-600"
                >
                <span class="ml-2">Active (Visible in store)</span>
              </label>
              <label class="flex items-center">
                <input
                  v-model="product.status"
                  type="radio"
                  value="draft"
                  class="h-4 w-4 text-blue-600"
                >
                <span class="ml-2">Draft (Hidden from store)</span>
              </label>
              <label class="flex items-center">
                <input
                  v-model="product.status"
                  type="radio"
                  value="archived"
                  class="h-4 w-4 text-blue-600"
                >
                <span class="ml-2">Archived</span>
              </label>
            </div>
          </div>

          <!-- Form Actions -->
          <div class="flex items-center justify-between pt-6 border-t border-gray-200">
            <router-link
              to="/products"
              class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
            >
              Cancel
            </router-link>
            <div class="flex items-center gap-3">
              <button
                v-if="isEditing"
                type="button"
                @click="saveAsDraft"
                class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
              >
                Save as Draft
              </button>
              <button
                type="submit"
                class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                :disabled="isSubmitting"
              >
                {{ isSubmitting ? 'Saving...' : isEditing ? 'Update Product' : 'Create Product' }}
              </button>
            </div>
          </div>
        </div>
      </form>
    </Card>
  </div>
</template>

<script setup lang="ts">
import Card from '@/components/ui/Card.vue';
</script>
