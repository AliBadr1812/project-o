<template>
  <div class="max-w-4xl mx-auto">
    <!-- Header -->
    <div class="mb-6">
      <h1 class="page-title">{{ isEditing ? 'Edit Product' : 'Add New Product' }}</h1>
      <p class="page-subtitle">{{ isEditing ? 'Update product information' : 'Add a new product to your store' }}</p>
    </div>

    <!-- Form -->
    <Card>
      <form @submit.prevent="submitForm" class="flex flex-col gap-8 p-2">

        <!-- Basic Information -->
        <div>
          <h2 class="text-base font-semibold mb-4" style="color:var(--text-primary)">Basic Information</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">Product Name *</label>
              <input v-model="product.name" type="text" required class="glass-input w-full" placeholder="Enter product name">
            </div>
            <div>
              <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">SKU *</label>
              <input v-model="product.sku" type="text" required class="glass-input w-full" placeholder="e.g., PROD-001">
            </div>
            <div>
              <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">Category *</label>
              <select v-model="product.categoryId" required class="glass-select w-full">
                <option value="">Select a category</option>
                <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.name }}</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">Brand</label>
              <input v-model="product.brand" type="text" class="glass-input w-full" placeholder="Enter brand name">
            </div>
          </div>
        </div>

        <!-- Pricing -->
        <div>
          <h2 class="text-base font-semibold mb-4" style="color:var(--text-primary)">Pricing</h2>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div>
              <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">Price *</label>
              <div class="relative">
                <span class="absolute left-3.5 top-1/2 -translate-y-1/2 text-sm" style="color:var(--text-muted)">$</span>
                <input v-model="product.price" type="number" step="0.01" min="0" required class="glass-input w-full pl-8" placeholder="0.00">
              </div>
            </div>
            <div>
              <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">Compare at Price</label>
              <div class="relative">
                <span class="absolute left-3.5 top-1/2 -translate-y-1/2 text-sm" style="color:var(--text-muted)">$</span>
                <input v-model="product.compareAtPrice" type="number" step="0.01" min="0" class="glass-input w-full pl-8" placeholder="0.00">
              </div>
            </div>
            <div>
              <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">Cost per Item</label>
              <div class="relative">
                <span class="absolute left-3.5 top-1/2 -translate-y-1/2 text-sm" style="color:var(--text-muted)">$</span>
                <input v-model="product.costPerItem" type="number" step="0.01" min="0" class="glass-input w-full pl-8" placeholder="0.00">
              </div>
            </div>
          </div>
        </div>

        <!-- Inventory -->
        <div>
          <h2 class="text-base font-semibold mb-4" style="color:var(--text-primary)">Inventory</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">Stock Quantity *</label>
              <input v-model="product.stock" type="number" min="0" required class="glass-input w-full" placeholder="0">
            </div>
            <div>
              <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">Low Stock Threshold</label>
              <input v-model="product.lowStockThreshold" type="number" min="0" class="glass-input w-full" placeholder="10">
              <p class="text-xs mt-1.5" style="color:var(--text-muted)">Get notified when stock drops below this number</p>
            </div>
          </div>
        </div>

        <!-- Images -->
        <div>
          <h2 class="text-base font-semibold mb-4" style="color:var(--text-primary)">Product Images</h2>
          <div class="flex flex-col gap-4">
            <!-- Main Image -->
            <div>
              <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">Main Image *</label>
              <div
                class="rounded-2xl p-6 text-center"
                style="border: 2px dashed var(--glass-border); background: var(--glass-bg);"
              >
                <div v-if="mainImagePreview" class="mb-4">
                  <img :src="mainImagePreview" alt="Main product image" class="mx-auto h-32 w-32 object-cover rounded-xl">
                </div>
                <div v-else class="mb-4" style="color:var(--text-muted)">
                  <i class="fas fa-image text-4xl mb-2"></i>
                </div>
                <label class="cursor-pointer">
                  <span class="text-sm font-medium" style="color:var(--accent)">Click to upload</span>
                  <input type="file" @change="handleMainImageUpload" accept="image/*" class="hidden">
                </label>
                <p class="text-xs mt-2" style="color:var(--text-muted)">PNG, JPG, GIF up to 10MB</p>
              </div>
            </div>

            <!-- Additional Images -->
            <div>
              <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">Additional Images</label>
              <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div
                  v-for="(image, index) in additionalImagesPreview"
                  :key="index"
                  class="relative rounded-xl p-3 text-center"
                  style="border: 1px solid var(--glass-border); background: var(--glass-bg);"
                >
                  <img :src="image" alt="Additional image" class="h-20 w-20 object-cover rounded-lg mx-auto mb-2">
                  <button
                    @click="removeAdditionalImage(index)"
                    class="absolute -top-2 -right-2 rounded-full w-6 h-6 flex items-center justify-center text-xs"
                    style="background: #dc2626; color: #fff;"
                  >
                    <i class="fas fa-xmark"></i>
                  </button>
                </div>
                <label
                  class="rounded-xl p-4 text-center cursor-pointer flex flex-col items-center justify-center gap-1 transition-colors"
                  style="border: 2px dashed var(--glass-border); background: var(--glass-bg); min-height: 100px;"
                  onmouseover="this.style.background='rgba(255,255,255,0.35)'"
                  onmouseout="this.style.background='var(--glass-bg)'"
                >
                  <i class="fas fa-plus text-lg" style="color:var(--text-muted)"></i>
                  <span class="text-xs" style="color:var(--text-muted)">Add Image</span>
                  <input type="file" @change="handleAdditionalImageUpload" accept="image/*" multiple class="hidden">
                </label>
              </div>
            </div>
          </div>
        </div>

        <!-- Description -->
        <div>
          <h2 class="text-base font-semibold mb-4" style="color:var(--text-primary)">Description</h2>
          <label class="block text-sm font-medium mb-2" style="color:var(--text-secondary)">Product Description *</label>
          <textarea
            v-model="product.description"
            rows="6"
            required
            class="glass-input w-full"
            placeholder="Describe your product in detail..."
          ></textarea>
        </div>

        <!-- Status -->
        <div>
          <h2 class="text-base font-semibold mb-4" style="color:var(--text-primary)">Status</h2>
          <div class="flex items-center gap-6">
            <label class="flex items-center gap-2 cursor-pointer">
              <input v-model="product.status" type="radio" value="active" class="accent-[var(--accent)]">
              <span class="text-sm" style="color:var(--text-secondary)">Active (Visible in store)</span>
            </label>
            <label class="flex items-center gap-2 cursor-pointer">
              <input v-model="product.status" type="radio" value="draft" class="accent-[var(--accent)]">
              <span class="text-sm" style="color:var(--text-secondary)">Draft (Hidden from store)</span>
            </label>
            <label class="flex items-center gap-2 cursor-pointer">
              <input v-model="product.status" type="radio" value="archived" class="accent-[var(--accent)]">
              <span class="text-sm" style="color:var(--text-secondary)">Archived</span>
            </label>
          </div>
        </div>

        <!-- Form Actions -->
        <div class="flex items-center justify-between pt-4" style="border-top: 1px solid var(--glass-border);">
          <router-link to="/products">
            <button type="button" class="btn-glass">Cancel</button>
          </router-link>
          <div class="flex items-center gap-3">
            <button v-if="isEditing" type="button" @click="saveAsDraft" class="btn-glass">
              Save as Draft
            </button>
            <button type="submit" class="btn-accent" :disabled="isSubmitting">
              {{ isSubmitting ? 'Saving…' : isEditing ? 'Update Product' : 'Create Product' }}
            </button>
          </div>
        </div>

      </form>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import Card from '@/components/ui/Card.vue';

const route = useRoute();
const isEditing = computed(() => !!route.params.id);
const isSubmitting = ref(false);

const product = ref({
  name: '', sku: '', categoryId: '', brand: '',
  price: null, compareAtPrice: null, costPerItem: null,
  stock: null, lowStockThreshold: 10,
  description: '', status: 'active',
});

const categories = ref([
  { id: 1, name: 'Electronics' },
  { id: 2, name: 'Clothing' },
  { id: 3, name: 'Home & Garden' },
]);

const mainImagePreview = ref<string | null>(null);
const additionalImagesPreview = ref<string[]>([]);

const handleMainImageUpload = (e: Event) => {
  const file = (e.target as HTMLInputElement).files?.[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = ev => { mainImagePreview.value = ev.target?.result as string; };
    reader.readAsDataURL(file);
  }
};

const handleAdditionalImageUpload = (e: Event) => {
  const files = (e.target as HTMLInputElement).files;
  if (!files) return;
  Array.from(files).forEach(file => {
    const reader = new FileReader();
    reader.onload = ev => { additionalImagesPreview.value.push(ev.target?.result as string); };
    reader.readAsDataURL(file);
  });
};

const removeAdditionalImage = (index: number) => {
  additionalImagesPreview.value.splice(index, 1);
};

const saveAsDraft = () => { product.value.status = 'draft'; submitForm(); };

const submitForm = async () => {
  isSubmitting.value = true;
  await new Promise(r => setTimeout(r, 800));
  console.log('Product saved:', product.value);
  isSubmitting.value = false;
};
</script>
