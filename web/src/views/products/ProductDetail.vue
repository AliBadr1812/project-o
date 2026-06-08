<template>
  <div class="flex flex-col gap-6">
    <div v-if="loading" class="py-12 flex items-center justify-center">
      <LoadingSpinner />
    </div>

    <div v-else-if="!product" class="py-12">
      <EmptyState title="Product not found"
        description="The product you're looking for doesn't exist or has been removed."
        icon="box-open">
        <button class="btn-accent text-sm" @click="goBack">
          <i class="fas fa-arrow-left text-xs mr-1"></i> Back to Products
        </button>
      </EmptyState>
    </div>

    <div v-else>
      <!-- Page Header -->
      <div class="page-header">
        <div>
          <h1 class="page-title">{{ product.name }}</h1>
          <p class="page-subtitle">SKU: {{ product.sku }} &bull; ID: {{ product.id }}</p>
        </div>
        <div class="flex items-center gap-3">
          <Badge :variant="product.status === 'active' ? 'success' : 'warning'">{{ product.status }}</Badge>
          <button @click="handleEdit" class="btn-glass text-sm"><i class="fas fa-pen text-xs mr-1"></i>Edit</button>
          <button @click="handleDuplicate" class="btn-glass text-sm"><i class="fas fa-copy text-xs mr-1"></i>Duplicate</button>
          <button @click="handleDelete" class="btn-glass text-sm" style="color: var(--ni-red);"><i class="fas fa-trash text-xs mr-1"></i>Delete</button>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- Left Column -->
        <div class="lg:col-span-2 flex flex-col gap-6">
          <!-- Images + Tabs Card -->
          <Card>
            <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Product Details</h2>
            </div>
            <div class="p-5">
              <!-- Images -->
              <div class="flex gap-3 mb-6">
                <div class="w-24 h-24 rounded-xl overflow-hidden" style="border: 2px solid var(--accent);">
                  <img :src="mainImage" :alt="product.name" class="w-full h-full object-cover" />
                </div>
                <div v-for="image in product.images" :key="image"
                  class="w-24 h-24 rounded-xl overflow-hidden cursor-pointer transition-all"
                  style="border: 1px solid var(--glass-border);"
                  @click="mainImage = image">
                  <img :src="image" :alt="product.name" class="w-full h-full object-cover" />
                </div>
                <div class="w-24 h-24 rounded-xl flex items-center justify-center cursor-pointer"
                  style="border: 2px dashed var(--glass-border);">
                  <i class="fas fa-plus text-xl" style="color: var(--text-muted);"></i>
                </div>
              </div>

              <!-- Tabs -->
              <div class="flex gap-4 mb-5" style="border-bottom: 1px solid var(--glass-border);">
                <button v-for="tab in tabs" :key="tab.id" @click="activeTab = tab.id"
                  class="pb-3 px-1 text-sm border-b-2 transition-colors"
                  :style="activeTab === tab.id ? 'border-color: var(--accent); color: var(--accent); font-weight: 500;' : 'border-color: transparent; color: var(--text-secondary);'">
                  {{ tab.label }}
                </button>
              </div>

              <!-- Overview -->
              <div v-if="activeTab === 'overview'" class="flex flex-col gap-5">
                <div>
                  <h3 class="text-sm font-semibold mb-2" style="color: var(--text-primary);">Description</h3>
                  <p class="text-sm" style="color: var(--text-secondary);">{{ product.description }}</p>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-5">
                  <div>
                    <h3 class="text-sm font-semibold mb-3" style="color: var(--text-primary);">Pricing</h3>
                    <div class="flex flex-col gap-2">
                      <div class="flex justify-between text-sm">
                        <span style="color: var(--text-secondary);">Price</span>
                        <span class="td-accent">{{ formatCurrency(product.price) }}</span>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span style="color: var(--text-secondary);">Cost</span>
                        <span style="color: var(--text-primary);">{{ formatCurrency(product.cost) }}</span>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span style="color: var(--text-secondary);">Profit Margin</span>
                        <span style="color: var(--ni-green);">{{ calculateProfitMargin(product) }}%</span>
                      </div>
                      <div v-if="product.compareAtPrice" class="flex justify-between text-sm">
                        <span style="color: var(--text-secondary);">Compare at</span>
                        <span class="line-through" style="color: var(--ni-red);">{{ formatCurrency(product.compareAtPrice) }}</span>
                      </div>
                    </div>
                  </div>
                  <div>
                    <h3 class="text-sm font-semibold mb-3" style="color: var(--text-primary);">Inventory</h3>
                    <div class="flex flex-col gap-2">
                      <div class="flex justify-between items-center text-sm">
                        <span style="color: var(--text-secondary);">Stock</span>
                        <Badge :variant="product.stock > 10 ? 'success' : product.stock > 0 ? 'warning' : 'danger'">{{ product.stock }}</Badge>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span style="color: var(--text-secondary);">Low Threshold</span>
                        <span style="color: var(--text-primary);">{{ product.lowStockThreshold }}</span>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span style="color: var(--text-secondary);">Track Inventory</span>
                        <span style="color: var(--text-primary);">{{ product.trackInventory ? 'Yes' : 'No' }}</span>
                      </div>
                    </div>
                  </div>
                </div>
                <div>
                  <h3 class="text-sm font-semibold mb-2" style="color: var(--text-primary);">Categories</h3>
                  <div class="flex flex-wrap gap-2">
                    <Badge v-for="category in product.categories" :key="category.id" variant="info">{{ category.name }}</Badge>
                  </div>
                </div>
              </div>

              <!-- Variants -->
              <div v-if="activeTab === 'variants'" class="flex flex-col gap-4">
                <div v-if="!product.variants || product.variants.length === 0"
                  class="py-10 text-center">
                  <i class="fas fa-layer-group text-3xl mb-3" style="color: var(--text-muted);"></i>
                  <p class="text-sm" style="color: var(--text-muted);">No variants for this product.</p>
                  <p class="text-xs mt-1" style="color: var(--text-muted);">Add variants via the edit form.</p>
                </div>
                <div v-else class="overflow-x-auto">
                  <table class="glass-table w-full text-sm">
                    <thead>
                      <tr>
                        <th class="text-left py-2 px-3 text-xs font-semibold" style="color: var(--text-muted);">Options</th>
                        <th class="text-left py-2 px-3 text-xs font-semibold" style="color: var(--text-muted);">SKU</th>
                        <th class="text-right py-2 px-3 text-xs font-semibold" style="color: var(--text-muted);">Price</th>
                        <th class="text-right py-2 px-3 text-xs font-semibold" style="color: var(--text-muted);">Stock</th>
                        <th class="text-center py-2 px-3 text-xs font-semibold" style="color: var(--text-muted);">Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="v in product.variants" :key="v.id">
                        <td class="py-2 px-3" style="color: var(--text-primary);">{{ v.options }}</td>
                        <td class="py-2 px-3 font-mono text-xs" style="color: var(--text-muted);">{{ v.sku }}</td>
                        <td class="py-2 px-3 text-right td-accent">{{ formatCurrency(v.price) }}</td>
                        <td class="py-2 px-3 text-right" style="color: var(--text-primary);">{{ v.stock }}</td>
                        <td class="py-2 px-3 text-center">
                          <span class="badge" :class="v.isAvailable ? 'badge-success' : 'badge-danger'">
                            {{ v.isAvailable ? 'In Stock' : 'Out of Stock' }}
                          </span>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>

              <!-- Specifications -->
              <div v-if="activeTab === 'specifications'" class="flex flex-col gap-3">
                <div v-for="spec in product.specifications" :key="spec.name" class="flex text-sm">
                  <div class="w-1/3 font-medium" style="color: var(--text-secondary);">{{ spec.name }}</div>
                  <div class="w-2/3" style="color: var(--text-primary);">{{ spec.value }}</div>
                </div>
              </div>

              <!-- Reviews -->
              <div v-if="activeTab === 'reviews'">
                <div class="flex items-center justify-between mb-5">
                  <div class="flex items-center gap-3">
                    <span class="text-2xl font-bold" style="color: var(--text-primary);">{{ product.averageRating }}</span>
                    <div class="flex">
                      <i v-for="star in 5" :key="star" class="text-sm"
                        :class="star <= Math.floor(product.averageRating) ? 'fas fa-star text-yellow-400' : star <= product.averageRating ? 'fas fa-star-half-alt text-yellow-400' : 'far fa-star'" style="color: var(--text-secondary);"></i>
                    </div>
                    <span class="text-sm" style="color: var(--text-secondary);">{{ product.reviewCount }} reviews</span>
                  </div>
                  <button class="btn-glass text-sm" @click="showReviewModal = true">
                    <i class="fas fa-plus text-xs mr-1"></i> Add Review
                  </button>
                </div>
                <div v-if="reviews.length === 0" class="text-center py-8">
                  <i class="fas fa-comments text-3xl mb-3" style="color: var(--text-muted);"></i>
                  <p class="text-sm" style="color: var(--text-muted);">No reviews yet</p>
                </div>
                <div v-else class="flex flex-col gap-4">
                  <div v-for="review in reviews" :key="review.id" class="p-4 rounded-xl" style="border: 1px solid var(--glass-border);">
                    <div class="flex justify-between mb-2">
                      <span class="font-medium text-sm" style="color: var(--text-primary);">{{ review.customerName }}</span>
                      <span class="text-xs" style="color: var(--text-muted);">{{ formatDate(review.date) }}</span>
                    </div>
                    <div class="flex items-center gap-2 mb-2">
                      <i v-for="star in 5" :key="star" class="text-xs"
                        :class="star <= review.rating ? 'fas fa-star text-yellow-400' : 'far fa-star'" style="color: var(--text-secondary);"></i>
                      <span class="text-xs font-medium" style="color: var(--text-primary);">{{ review.rating }}/5</span>
                    </div>
                    <p class="text-sm" style="color: var(--text-secondary);">{{ review.comment }}</p>
                  </div>
                </div>
              </div>

              <!-- Sales -->
              <div v-if="activeTab === 'sales'" class="flex flex-col gap-5">
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <Card>
                    <div class="p-4 text-center">
                      <p class="text-2xl font-bold" style="color: var(--text-primary);">{{ product.salesData.totalSales }}</p>
                      <p class="text-xs mt-1" style="color: var(--text-secondary);">Total Sold</p>
                    </div>
                  </Card>
                  <Card>
                    <div class="p-4 text-center">
                      <p class="text-2xl font-bold td-accent">{{ formatCurrency(product.salesData.totalRevenue) }}</p>
                      <p class="text-xs mt-1" style="color: var(--text-secondary);">Total Revenue</p>
                    </div>
                  </Card>
                  <Card>
                    <div class="p-4 text-center">
                      <p class="text-2xl font-bold" style="color: var(--text-primary);">{{ product.salesData.monthlySales }}</p>
                      <p class="text-xs mt-1" style="color: var(--text-secondary);">This Month</p>
                    </div>
                  </Card>
                </div>
                <div class="h-48 rounded-xl flex items-center justify-center" style="border: 1px solid var(--glass-border);">
                  <div class="text-center">
                    <i class="fas fa-chart-line text-3xl mb-2" style="color: var(--text-muted);"></i>
                    <p class="text-sm" style="color: var(--text-muted);">Sales trend chart</p>
                  </div>
                </div>
              </div>
            </div>
          </Card>
        </div>

        <!-- Right Column Sidebar -->
        <div class="flex flex-col gap-4">
          <!-- Status -->
          <Card>
            <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
              <h3 class="text-[15px] font-semibold" style="color: var(--text-primary);">Product Status</h3>
            </div>
            <div class="p-5 flex flex-col gap-3">
              <div class="flex items-center justify-between text-sm">
                <span style="color: var(--text-secondary);">Visibility</span>
                <Badge :variant="product.isVisible ? 'success' : 'warning'">{{ product.isVisible ? 'Visible' : 'Hidden' }}</Badge>
              </div>
              <div class="flex items-center justify-between text-sm">
                <span style="color: var(--text-secondary);">Featured</span>
                <Badge :variant="product.isFeatured ? 'success' : 'secondary'">{{ product.isFeatured ? 'Yes' : 'No' }}</Badge>
              </div>
              <div class="flex items-center justify-between text-sm">
                <span style="color: var(--text-secondary);">In Stock</span>
                <Badge :variant="product.inStock ? 'success' : 'danger'">{{ product.inStock ? 'Yes' : 'No' }}</Badge>
              </div>
              <div class="pt-3" style="border-top: 1px solid var(--glass-border);">
                <button class="btn-accent w-full text-sm" @click="toggleVisibility">
                  {{ product.isVisible ? 'Hide Product' : 'Make Visible' }}
                </button>
              </div>
            </div>
          </Card>

          <!-- Categories -->
          <Card>
            <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
              <div class="flex items-center justify-between">
                <h3 class="text-[15px] font-semibold" style="color: var(--text-primary);">Categories</h3>
                <button class="btn-glass-icon w-7 h-7 rounded-lg text-xs" @click="showCategoryModal = true">
                  <i class="fas fa-pen"></i>
                </button>
              </div>
            </div>
            <div class="p-5 flex flex-col gap-2">
              <div v-for="category in product.categories" :key="category.id"
                class="flex items-center justify-between p-2 rounded-lg transition-colors"
                style="border: 1px solid var(--glass-border);">
                <div class="flex items-center gap-2 text-sm">
                  <i :class="category.icon || 'fas fa-folder'" style="color: var(--text-muted);"></i>
                  <span style="color: var(--text-primary);">{{ category.name }}</span>
                </div>
                <Badge variant="info">{{ category.productCount }}</Badge>
              </div>
            </div>
          </Card>

          <!-- Product Info -->
          <Card>
            <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
              <h3 class="text-[15px] font-semibold" style="color: var(--text-primary);">Product Info</h3>
            </div>
            <div class="p-5 flex flex-col gap-3 text-sm">
              <div>
                <p class="text-xs mb-0.5" style="color: var(--text-secondary);">Created</p>
                <p style="color: var(--text-primary);">{{ formatDate(product.createdAt) }}</p>
              </div>
              <div>
                <p class="text-xs mb-0.5" style="color: var(--text-secondary);">Last Updated</p>
                <p style="color: var(--text-primary);">{{ formatDate(product.updatedAt) }}</p>
              </div>
              <div>
                <p class="text-xs mb-0.5" style="color: var(--text-secondary);">Weight</p>
                <p style="color: var(--text-primary);">{{ product.weight }} {{ product.weightUnit }}</p>
              </div>
              <div>
                <p class="text-xs mb-0.5" style="color: var(--text-secondary);">Dimensions</p>
                <p style="color: var(--text-primary);">{{ product.dimensions?.length }} × {{ product.dimensions?.width }} × {{ product.dimensions?.height }} {{ product.dimensions?.unit }}</p>
              </div>
            </div>
          </Card>

          <!-- Quick Actions -->
          <Card>
            <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
              <h3 class="text-[15px] font-semibold" style="color: var(--text-primary);">Quick Actions</h3>
            </div>
            <div class="p-5 flex flex-col gap-2">
              <button class="btn-glass text-sm justify-start" @click="handleViewOrders">
                <i class="fas fa-shopping-cart text-xs mr-2"></i> View Orders
              </button>
              <button class="btn-glass text-sm justify-start" @click="handleUpdateStock">
                <i class="fas fa-boxes text-xs mr-2"></i> Update Stock
              </button>
              <button class="btn-glass text-sm justify-start" @click="handleCopyLink">
                <i class="fas fa-link text-xs mr-2"></i> Copy Product Link
              </button>
              <button class="btn-glass text-sm justify-start" @click="handleViewAnalytics">
                <i class="fas fa-chart-bar text-xs mr-2"></i> View Analytics
              </button>
            </div>
          </Card>
        </div>
      </div>
    </div>

    <!-- Modals -->
    <Modal v-if="showReviewModal" title="Add Review" @close="showReviewModal = false" />
    <Modal v-if="showCategoryModal" title="Edit Categories" size="lg" @close="showCategoryModal = false" />
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import LoadingSpinner from '@/components/shared/LoadingSpinner.vue';
import Modal from '@/components/ui/Modal.vue';
import Button from '@/components/ui/Button.vue';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import EmptyState from '@/components/shared/EmptyState.vue';
import { formatCurrency, formatDate } from '@/utils/formatters';
import { useProductStore } from '@/stores/productStore';
import { useToast } from '@/composables/useToast';
import { useConfirm } from '@/composables/useConfirm';

const router = useRouter();
const route  = useRoute();
const store  = useProductStore();
const toast  = useToast();
const { confirm } = useConfirm();

// State
const loading = ref(true);
const product = ref<any>(null);
const mainImage = ref('');
const activeTab = ref('overview');
const showReviewModal = ref(false);
const showCategoryModal = ref(false);
const reviews = ref<any[]>([]);

// Tabs
const tabs = [
  { id: 'overview',       label: 'Overview'       },
  { id: 'specifications', label: 'Specifications' },
  { id: 'variants',       label: 'Variants'       },
  { id: 'reviews',        label: 'Reviews'        },
  { id: 'sales',          label: 'Sales'          },
];

// Mock product data
const mockProduct = {
  id: 1,
  name: 'Premium Wireless Headphones',
  sku: 'PHONES-001',
  status: 'active',
  description: 'Experience premium sound quality with our noise-cancelling wireless headphones. Perfect for music lovers, gamers, and professionals who demand the best audio experience.',

  images: [
    'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&h=800&fit=crop',
    'https://images.unsplash.com/photo-1484704849700-f032a568e944?w=800&h=800&fit=crop',
    'https://images.unsplash.com/photo-1583394838336-acd977736f90?w=800&h=800&fit=crop'
  ],

  price: 149.99,
  cost: 89.99,
  compareAtPrice: 199.99,

  stock: 25,
  lowStockThreshold: 10,
  trackInventory: true,
  inStock: true,

  categories: [
    { id: 1, name: 'Electronics', icon: 'fas fa-tv', productCount: 42 },
    { id: 2, name: 'Audio', icon: 'fas fa-headphones', productCount: 18 }
  ],

  specifications: [
    { name: 'Brand', value: 'AudioTech' },
    { name: 'Model', value: 'ATH-M50xBT2' },
    { name: 'Connectivity', value: 'Bluetooth 5.0' },
    { name: 'Battery Life', value: '40 hours' },
    { name: 'Charging Time', value: '2 hours' },
    { name: 'Driver Size', value: '45mm' },
    { name: 'Impedance', value: '38 ohms' },
    { name: 'Frequency Response', value: '15Hz - 28kHz' }
  ],

  averageRating: 4.5,
  reviewCount: 128,

  salesData: {
    totalSales: 342,
    totalRevenue: 51285.58,
    monthlySales: 28
  },

  isVisible: true,
  isFeatured: true,

  createdAt: '2024-01-15T10:30:00Z',
  updatedAt: '2024-02-20T14:45:00Z',

  weight: 285,
  weightUnit: 'g',

  dimensions: {
    length: 20.5,
    width: 18.7,
    height: 9.3,
    unit: 'cm'
  },

  variants: [
    { id: 1, options: 'Black', sku: 'PHONES-001-BLK', price: 149.99, stock: 12, isAvailable: true },
    { id: 2, options: 'White', sku: 'PHONES-001-WHT', price: 149.99, stock: 8,  isAvailable: true },
    { id: 3, options: 'Midnight Blue', sku: 'PHONES-001-BLU', price: 159.99, stock: 5, isAvailable: true },
  ]
};

// Mock reviews
const mockReviews = [
  {
    id: 1,
    customerName: 'Michael Johnson',
    rating: 5,
    date: '2024-02-15T10:30:00Z',
    comment: 'Excellent sound quality and very comfortable for long listening sessions. Battery life is amazing!'
  },
  {
    id: 2,
    customerName: 'Sarah Williams',
    rating: 4,
    date: '2024-02-10T14:20:00Z',
    comment: 'Great headphones, but the ear cushions could be more breathable. Sound is fantastic though!'
  },
  {
    id: 3,
    customerName: 'David Chen',
    rating: 5,
    date: '2024-02-05T09:15:00Z',
    comment: 'Best purchase I\'ve made this year. The noise cancellation works perfectly on my commute.'
  }
];

// Methods
const calculateProfitMargin = (prod: any) => {
  if (!prod || prod.price === 0) return 0;
  return (((prod.price - prod.cost) / prod.price) * 100).toFixed(1);
};

const handleEdit = () => {
  router.push(`/products/${product.value.id}/edit`);
};

const handleDuplicate = async () => {
  const ok = await confirm({
    title:       'Duplicate product',
    message:     `Duplicate "${product.value.name}"?`,
    detail:      'A new draft copy will be created for you to edit.',
    confirmText: 'Duplicate',
    variant:     'info',
  });
  if (ok) {
    toast.success('Product duplicated! Redirecting to create new product…');
    router.push('/products/create');
  }
};

const handleDelete = async () => {
  const ok = await confirm({
    title:       'Delete product',
    message:     `Delete "${product.value.name}"?`,
    detail:      'This action cannot be undone. All associated data will be removed.',
    confirmText: 'Delete',
    variant:     'danger',
  });
  if (!ok) return;
  try {
    await store.remove(Number(product.value.id));
    toast.success('Product deleted');
    router.push('/products');
  } catch (e: any) {
    toast.error(e?.message ?? 'Delete failed', 'Error');
  }
};

const toggleVisibility = () => {
  product.value.isVisible = !product.value.isVisible;
  toast.info(`Product is now ${product.value.isVisible ? 'visible' : 'hidden'}`);
};

const handleViewOrders = () => {
  router.push(`/orders?product=${product.value.id}`);
};

const handleUpdateStock = async () => {
  // Show a simple inline toast-guided flow (prompt() is replaced by a confirm round-trip)
  const newStock = window.prompt(`Enter new stock quantity for "${product.value.name}":`, product.value.stock.toString());
  if (newStock !== null && !isNaN(Number(newStock)) && Number(newStock) >= 0) {
    product.value.stock = parseInt(newStock);
    product.value.inStock = product.value.stock > 0;
    toast.success(`Stock updated to ${product.value.stock} units`, 'Stock Updated');
  } else if (newStock !== null) {
    toast.error('Invalid quantity entered', 'Error');
  }
};

const handleCopyLink = () => {
  const link = `${window.location.origin}/product/${product.value.sku}`;
  navigator.clipboard.writeText(link).then(() => {
    toast.success('Product link copied to clipboard!', 'Copied');
  }).catch(() => {
    toast.error('Failed to copy link', 'Error');
  });
};

const handleViewAnalytics = () => {
  router.push(`/analytics/products/${product.value.id}`);
};

const goBack = () => {
  router.push('/products');
};

// Lifecycle
onMounted(async () => {
  await store.fetchAll();
  const realProduct = store.items.find(p => p.id === Number(route.params.id));
  if (realProduct) {
    product.value = {
      ...mockProduct,
      id:          realProduct.id,
      name:        realProduct.name,
      sku:         realProduct.sku,
      status:      realProduct.status,
      price:       realProduct.price,
      stock:       realProduct.stock,
      images:      (realProduct.images && realProduct.images.length) ? realProduct.images : mockProduct.images,
      categories:  realProduct.categories
        ? [{ id: 1, name: realProduct.categories, icon: 'fas fa-folder', productCount: 0 }]
        : mockProduct.categories,
      createdAt:   realProduct.createdAt ?? mockProduct.createdAt,
    };
    mainImage.value = product.value.images[0]!;
  }
  reviews.value = mockReviews;
  loading.value = false;
});
</script>
