<template>
  <div class="product-detail">
    <Breadcrumb
      :items="breadcrumbs"
      class="mb-6"
    />

    <div v-if="loading" class="py-12">
      <LoadingSpinner />
    </div>

    <div v-else-if="!product" class="py-12">
      <EmptyState
        title="Product not found"
        description="The product you're looking for doesn't exist or has been removed."
        icon="box-open"
      >
        <Button variant="primary" @click="goBack">
          <i class="fas fa-arrow-left mr-2"></i> Back to Products
        </Button>
      </EmptyState>
    </div>

    <div v-else>
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- Left Column - Product Info -->
        <div class="lg:col-span-2">
          <Card>
            <div class="flex justify-between items-start mb-6">
              <div>
                <h1 class="text-2xl font-bold text-gray-800">{{ product.name }}</h1>
                <div class="flex items-center gap-4 mt-2">
                  <Badge :variant="product.status === 'active' ? 'success' : 'warning'">
                    {{ product.status }}
                  </Badge>
                  <span class="text-gray-600">SKU: {{ product.sku }}</span>
                  <span class="text-gray-600">ID: {{ product.id }}</span>
                </div>
              </div>
              <div class="flex gap-2">
                <Button variant="outline" size="sm" @click="handleEdit">
                  <i class="fas fa-edit mr-2"></i> Edit
                </Button>
                <Button variant="outline" size="sm" @click="handleDuplicate">
                  <i class="fas fa-copy mr-2"></i> Duplicate
                </Button>
                <Button variant="danger" size="sm" @click="handleDelete">
                  <i class="fas fa-trash mr-2"></i> Delete
                </Button>
              </div>
            </div>

            <!-- Product Images -->
            <div class="mb-8">
              <div class="flex gap-4 mb-4">
                <div
                  class="w-24 h-24 rounded-lg border-2 border-blue-500 overflow-hidden"
                >
                  <img
                    :src="mainImage"
                    :alt="product.name"
                    class="w-full h-full object-cover"
                  />
                </div>
                <div
                  v-for="image in product.images"
                  :key="image"
                  class="w-24 h-24 rounded-lg border border-gray-200 overflow-hidden cursor-pointer hover:border-blue-400"
                  @click="mainImage = image"
                >
                  <img
                    :src="image"
                    :alt="product.name"
                    class="w-full h-full object-cover"
                  />
                </div>
                <div
                  class="w-24 h-24 rounded-lg border-2 border-dashed border-gray-300 flex items-center justify-center cursor-pointer hover:border-blue-400"
                >
                  <i class="fas fa-plus text-2xl text-gray-400"></i>
                </div>
              </div>
            </div>

            <!-- Product Details Tabs -->
            <div class="border-b border-gray-200 mb-6">
              <div class="flex gap-6">
                <button
                  v-for="tab in tabs"
                  :key="tab.id"
                  @click="activeTab = tab.id"
                  class="pb-3 px-1 border-b-2 transition-colors"
                  :class="activeTab === tab.id
                    ? 'border-blue-500 text-blue-600 font-medium'
                    : 'border-transparent text-gray-600 hover:text-gray-800'"
                >
                  {{ tab.label }}
                </button>
              </div>
            </div>

            <!-- Tab Content -->
            <div>
              <!-- Overview Tab -->
              <div v-if="activeTab === 'overview'" class="gap-6">
                <div>
                  <h3 class="text-lg font-semibold text-gray-800 mb-3">Description</h3>
                  <div class="prose max-w-none">
                    {{ product.description }}
                  </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <h3 class="text-lg font-semibold text-gray-800 mb-3">Pricing</h3>
                    <div class="gap-2">
                      <div class="flex justify-between">
                        <span class="text-gray-600">Price</span>
                        <span class="font-medium">{{ formatCurrency(product.price) }}</span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-600">Cost</span>
                        <span class="font-medium">{{ formatCurrency(product.cost) }}</span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-600">Profit Margin</span>
                        <span class="font-medium text-green-600">
                          {{ calculateProfitMargin(product) }}%
                        </span>
                      </div>
                      <div v-if="product.compareAtPrice" class="flex justify-between">
                        <span class="text-gray-600">Compare at Price</span>
                        <span class="font-medium line-through text-red-600">
                          {{ formatCurrency(product.compareAtPrice) }}
                        </span>
                      </div>
                    </div>
                  </div>

                  <div>
                    <h3 class="text-lg font-semibold text-gray-800 mb-3">Inventory</h3>
                    <div class="gap-2">
                      <div class="flex justify-between">
                        <span class="text-gray-600">Stock Quantity</span>
                        <Badge
                          :variant="product.stock > 10 ? 'success' : product.stock > 0 ? 'warning' : 'danger'"
                        >
                          {{ product.stock }}
                        </Badge>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-600">Low Stock Threshold</span>
                        <span>{{ product.lowStockThreshold }}</span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-600">Track Inventory</span>
                        <span>{{ product.trackInventory ? 'Yes' : 'No' }}</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div>
                  <h3 class="text-lg font-semibold text-gray-800 mb-3">Categories</h3>
                  <div class="flex flex-wrap gap-2">
                    <Badge
                      v-for="category in product.categories"
                      :key="category.id"
                      variant="info"
                    >
                      {{ category.name }}
                    </Badge>
                  </div>
                </div>
              </div>

              <!-- Specifications Tab -->
              <div v-if="activeTab === 'specifications'" class="gap-4">
                <div v-for="spec in product.specifications" :key="spec.name" class="flex">
                  <div class="w-1/3 font-medium text-gray-700">{{ spec.name }}</div>
                  <div class="w-2/3 text-gray-600">{{ spec.value }}</div>
                </div>
              </div>

              <!-- Reviews Tab -->
              <div v-if="activeTab === 'reviews'">
                <div class="flex items-center justify-between mb-6">
                  <div>
                    <div class="flex items-center">
                      <div class="text-2xl font-bold mr-3">{{ product.averageRating }}</div>
                      <div class="mr-4">
                        <div class="flex">
                          <i
                            v-for="star in 5"
                            :key="star"
                            :class="star <= Math.floor(product.averageRating)
                              ? 'fas fa-star text-yellow-400'
                              : star <= product.averageRating
                                ? 'fas fa-star-half-alt text-yellow-400'
                                : 'far fa-star text-gray-300'"
                          ></i>
                        </div>
                      </div>
                      <span class="text-gray-600">Based on {{ product.reviewCount }} reviews</span>
                    </div>
                  </div>
                  <Button variant="outline" size="sm" @click="showReviewModal = true">
                    <i class="fas fa-plus mr-2"></i> Add Review
                  </Button>
                </div>

                <div v-if="reviews.length === 0" class="text-center py-8 text-gray-500">
                  <i class="fas fa-comments text-4xl mb-3"></i>
                  <p>No reviews yet</p>
                </div>

                <div v-else class="gap-6">
                  <div
                    v-for="review in reviews"
                    :key="review.id"
                    class="border border-gray-200 rounded-lg p-4"
                  >
                    <div class="flex justify-between mb-2">
                      <div class="font-medium">{{ review.customerName }}</div>
                      <div class="text-sm text-gray-600">{{ formatDate(review.date) }}</div>
                    </div>
                    <div class="flex items-center mb-3">
                      <div class="flex mr-3">
                        <i
                          v-for="star in 5"
                          :key="star"
                          :class="star <= review.rating
                            ? 'fas fa-star text-yellow-400'
                            : 'far fa-star text-gray-300'"
                          class="text-sm"
                        ></i>
                      </div>
                      <span class="font-medium">{{ review.rating }}/5</span>
                    </div>
                    <p class="text-gray-700">{{ review.comment }}</p>
                  </div>
                </div>
              </div>

              <!-- Sales Tab -->
              <div v-if="activeTab === 'sales'" class="gap-6">
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                  <Card>
                    <div class="text-center">
                      <div class="text-3xl font-bold text-gray-800 mb-2">
                        {{ product.salesData.totalSales }}
                      </div>
                      <div class="text-gray-600">Total Sold</div>
                    </div>
                  </Card>
                  <Card>
                    <div class="text-center">
                      <div class="text-3xl font-bold text-gray-800 mb-2">
                        {{ formatCurrency(product.salesData.totalRevenue) }}
                      </div>
                      <div class="text-gray-600">Total Revenue</div>
                    </div>
                  </Card>
                  <Card>
                    <div class="text-center">
                      <div class="text-3xl font-bold text-gray-800 mb-2">
                        {{ product.salesData.monthlySales }}
                      </div>
                      <div class="text-gray-600">This Month</div>
                    </div>
                  </Card>
                </div>

                <div>
                  <h3 class="text-lg font-semibold text-gray-800 mb-4">Sales Trend</h3>
                  <div class="h-48 border border-gray-200 rounded-lg flex items-center justify-center">
                    <div class="text-center text-gray-500">
                      <i class="fas fa-chart-line text-3xl mb-2"></i>
                      <p>Sales trend chart</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </Card>
        </div>

        <!-- Right Column - Sidebar -->
        <div>
          <!-- Status Card -->
          <Card class="mb-6">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">Product Status</h3>
            <div class="gap-4">
              <div class="flex items-center justify-between">
                <span class="text-gray-700">Visibility</span>
                <Badge :variant="product.isVisible ? 'success' : 'warning'">
                  {{ product.isVisible ? 'Visible' : 'Hidden' }}
                </Badge>
              </div>
              <div class="flex items-center justify-between">
                <span class="text-gray-700">Featured</span>
                <Badge :variant="product.isFeatured ? 'success' : 'default'">
                  {{ product.isFeatured ? 'Yes' : 'No' }}
                </Badge>
              </div>
              <div class="flex items-center justify-between">
                <span class="text-gray-700">In Stock</span>
                <Badge :variant="product.inStock ? 'success' : 'danger'">
                  {{ product.inStock ? 'Yes' : 'No' }}
                </Badge>
              </div>
              <div class="pt-4 border-t border-gray-200">
                <Button
                  variant="primary"
                  class="w-full"
                  @click="toggleVisibility"
                >
                  {{ product.isVisible ? 'Hide Product' : 'Make Visible' }}
                </Button>
              </div>
            </div>
          </Card>

          <!-- Categories Card -->
          <Card class="mb-6">
            <div class="flex justify-between items-center mb-4">
              <h3 class="text-lg font-semibold text-gray-800">Categories</h3>
              <Button variant="ghost" size="sm" @click="showCategoryModal = true">
                <i class="fas fa-edit"></i>
              </Button>
            </div>
            <div class="gap-2">
              <div
                v-for="category in product.categories"
                :key="category.id"
                class="flex items-center justify-between p-2 hover:bg-gray-50 rounded"
              >
                <div class="flex items-center">
                  <i :class="category.icon || 'fas fa-folder'" class="text-gray-400 mr-2"></i>
                  <span>{{ category.name }}</span>
                </div>
                <Badge variant="info">{{ category.productCount }}</Badge>
              </div>
            </div>
          </Card>

          <!-- Product Meta -->
          <Card class="mb-6">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">Product Info</h3>
            <div class="gap-3">
              <div>
                <div class="text-sm text-gray-600 mb-1">Created</div>
                <div>{{ formatDate(product.createdAt) }}</div>
              </div>
              <div>
                <div class="text-sm text-gray-600 mb-1">Last Updated</div>
                <div>{{ formatDate(product.updatedAt) }}</div>
              </div>
              <div>
                <div class="text-sm text-gray-600 mb-1">Weight</div>
                <div>{{ product.weight }} {{ product.weightUnit }}</div>
              </div>
              <div>
                <div class="text-sm text-gray-600 mb-1">Dimensions</div>
                <div>
                  {{ product.dimensions?.length }} × {{ product.dimensions?.width }} ×
                  {{ product.dimensions?.height }} {{ product.dimensions?.unit }}
                </div>
              </div>
            </div>
          </Card>

          <!-- Quick Actions -->
          <Card>
            <h3 class="text-lg font-semibold text-gray-800 mb-4">Quick Actions</h3>
            <div class="gap-2">
              <Button variant="outline" class="w-full justify-start" @click="handleViewOrders">
                <i class="fas fa-shopping-cart mr-2"></i> View Orders
              </Button>
              <Button variant="outline" class="w-full justify-start" @click="handleUpdateStock">
                <i class="fas fa-boxes mr-2"></i> Update Stock
              </Button>
              <Button variant="outline" class="w-full justify-start" @click="handleCopyLink">
                <i class="fas fa-link mr-2"></i> Copy Product Link
              </Button>
              <Button variant="outline" class="w-full justify-start" @click="handleViewAnalytics">
                <i class="fas fa-chart-bar mr-2"></i> View Analytics
              </Button>
            </div>
          </Card>
        </div>
      </div>
    </div>

    <!-- Modals -->
    <Modal
      v-if="showReviewModal"
      :title="'Add Review'"
      @close="showReviewModal = false"
    >
      <!-- Review form content -->
    </Modal>

    <Modal
      v-if="showCategoryModal"
      :title="'Edit Categories'"
      size="lg"
      @close="showCategoryModal = false"
    >
      <!-- Category edit form -->
    </Modal>
  </div>
</template>
<script setup lang="ts">
import LoadingSpinner from '@/components/shared/LoadingSpinner.vue';
import Modal from '@/components/ui/Modal.vue';
import Button from '@/components/ui/Button.vue';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import EmptyState from '@/components/shared/EmptyState.vue';
import Breadcrumb from '@/components/layout/Breadcrumb.vue';
import { formatCurrency, formatDate } from '@/utils/formatters';
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const breadcrumbs = computed(() => {
  const items = [
    { path: '/dashboard', name: 'Dashboard' }
  ];

  // Add route segments
  if (route.meta.breadcrumb) {
    items.push({
      path: route.path,
      name: route.meta.breadcrumb as string
    });
  }

  return items;
});
</script>
