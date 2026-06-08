<template>
  <div class="max-w-6xl mx-auto">

    <!-- Page header -->
    <div class="flex items-center justify-between mb-7">
      <div>
        <h1 class="page-title">{{ isEditing ? 'Edit Product' : 'New Product' }}</h1>
        <p class="page-subtitle">{{ isEditing ? 'Update product information' : 'Add a new product to your catalogue' }}</p>
      </div>
      <div class="flex items-center gap-2.5">
        <router-link to="/products">
          <button type="button" class="btn-glass">Cancel</button>
        </router-link>
        <button v-if="isEditing" type="button" @click="saveAsDraft" class="btn-glass">Save Draft</button>
        <button @click="submitForm" class="btn-accent" :disabled="isSubmitting">
          <i class="fas fa-check mr-1.5 text-xs"></i>
          {{ isSubmitting ? 'Saving…' : isEditing ? 'Update' : 'Create Product' }}
        </button>
      </div>
    </div>

    <!-- Two-column layout -->
    <div class="grid grid-cols-1 xl:grid-cols-[1fr_320px] gap-5">

      <!-- ── Left column ─────────────────────────────────────────────── -->
      <div class="flex flex-col gap-5">

        <!-- Basic Information -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#b97fff,#7c5ef0); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-tag"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Basic Information</h2>
            </div>
          </div>
          <div class="px-5 pb-5 flex flex-col gap-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="pf-label">Product Name <span style="color:#dc2626">*</span></label>
                <input v-model="product.name" type="text" required class="glass-input w-full" placeholder="e.g. Premium Wireless Headphones">
              </div>
              <div>
                <label class="pf-label">SKU <span style="color:#dc2626">*</span></label>
                <input v-model="product.sku" type="text" required class="glass-input w-full" placeholder="e.g. PROD-001">
              </div>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="pf-label">Brand</label>
                <input v-model="product.brand" type="text" class="glass-input w-full" placeholder="e.g. Apple">
              </div>
              <div>
                <label class="pf-label">Category <span style="color:#dc2626">*</span></label>
                <select v-model="product.categoryId" required class="glass-select w-full">
                  <option value="">Select a category</option>
                  <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
                </select>
              </div>
            </div>
          </div>
        </div>

        <!-- Description -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#5ac8fa,#007aff); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-align-left"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Description</h2>
            </div>
          </div>
          <div class="px-5 pb-5">
            <textarea
              v-model="product.description"
              rows="6"
              required
              class="glass-input w-full"
              placeholder="Describe your product in detail — materials, features, dimensions…"
            ></textarea>
          </div>
        </div>

        <!-- Variants -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center justify-between">
              <div class="flex items-center gap-2">
                <div class="stat-icon" style="background: linear-gradient(135deg,#30d158,#28b14c); width:30px; height:30px; font-size:13px;">
                  <i class="fas fa-layer-group"></i>
                </div>
                <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Variants</h2>
              </div>
              <button type="button" @click="addVariant" class="btn-glass text-xs">
                <i class="fas fa-plus text-[10px] mr-1"></i>Add variant
              </button>
            </div>
          </div>
          <div class="px-5 pb-5 flex flex-col gap-3">
            <p v-if="variants.length === 0" class="text-sm text-center py-4" style="color: var(--text-muted);">
              No variants — click "Add variant" to create size/colour options.
            </p>
            <div v-for="(v, i) in variants" :key="i"
              class="grid grid-cols-[1fr_1fr_80px_80px_28px] gap-2 items-center">
              <input v-model="v.options" type="text" class="glass-input text-sm" placeholder="e.g. Red / L">
              <input v-model="v.sku" type="text" class="glass-input text-sm font-mono" placeholder="SKU-R-L">
              <input v-model.number="v.price" type="number" step="0.01" min="0" class="glass-input text-sm" placeholder="0.00">
              <input v-model.number="v.stock" type="number" min="0" class="glass-input text-sm" placeholder="0">
              <button type="button" @click="removeVariant(i)"
                class="w-7 h-7 rounded-lg flex items-center justify-center text-xs"
                style="color: var(--ni-red); background: rgba(239,68,68,0.1);">
                <i class="fas fa-xmark"></i>
              </button>
            </div>
            <div v-if="variants.length > 0"
              class="grid grid-cols-[1fr_1fr_80px_80px_28px] gap-2 text-[10px] font-semibold px-0.5"
              style="color: var(--text-muted);">
              <span>Options</span><span>SKU</span><span>Price</span><span>Stock</span><span></span>
            </div>
          </div>
        </div>

        <!-- Product Images -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#ff9f0a,#e08800); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-images"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Product Images</h2>
            </div>
          </div>
          <div class="px-5 pb-5 flex flex-col gap-4">
            <!-- Main image -->
            <div>
              <label class="pf-label">Main Image <span style="color:#dc2626">*</span></label>
              <label
                class="flex flex-col items-center justify-center rounded-2xl cursor-pointer transition-all duration-200"
                style="border: 2px dashed var(--glass-border); background: var(--glass-bg); min-height: 140px;"
                onmouseover="this.style.background='rgba(255,255,255,0.35)'"
                onmouseout="this.style.background='var(--glass-bg)'"
              >
                <div v-if="mainImagePreview">
                  <img :src="mainImagePreview" alt="Main" class="h-28 w-28 object-cover rounded-xl">
                </div>
                <div v-else class="flex flex-col items-center gap-2" style="color:var(--text-muted)">
                  <i class="fas fa-cloud-arrow-up text-3xl"></i>
                  <span class="text-[13px] font-medium" style="color:var(--accent)">Click to upload</span>
                  <span class="text-[12px]">PNG, JPG up to 10MB</span>
                </div>
                <input type="file" @change="handleMainImageUpload" accept="image/*" class="hidden">
              </label>
            </div>
            <!-- Additional images -->
            <div>
              <label class="pf-label">Additional Images</label>
              <div class="grid grid-cols-4 gap-3">
                <div
                  v-for="(img, i) in additionalImagesPreview"
                  :key="i"
                  class="relative rounded-xl overflow-hidden"
                  style="aspect-ratio: 1; background: var(--glass-bg); border: 1px solid var(--glass-border);"
                >
                  <img :src="img" alt="Additional" class="w-full h-full object-cover">
                  <button
                    @click="removeAdditionalImage(i)"
                    class="absolute top-1 right-1 w-6 h-6 rounded-full flex items-center justify-center text-[11px]"
                    style="background: rgba(220,38,38,0.9); color:#fff;"
                  ><i class="fas fa-xmark"></i></button>
                </div>
                <label
                  class="flex flex-col items-center justify-center rounded-xl cursor-pointer transition-all"
                  style="aspect-ratio:1; border: 2px dashed var(--glass-border); background: var(--glass-bg);"
                  onmouseover="this.style.background='rgba(255,255,255,0.35)'"
                  onmouseout="this.style.background='var(--glass-bg)'"
                >
                  <i class="fas fa-plus text-lg" style="color:var(--text-muted)"></i>
                  <span class="text-[11px] mt-1" style="color:var(--text-muted)">Add</span>
                  <input type="file" @change="handleAdditionalImageUpload" accept="image/*" multiple class="hidden">
                </label>
              </div>
            </div>
          </div>
        </div>

      </div>

      <!-- ── Right column ────────────────────────────────────────────── -->
      <div class="flex flex-col gap-5">

        <!-- Status -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#34c759,#30a84b); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-circle-check"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Status</h2>
            </div>
          </div>
          <div class="px-5 pb-5 flex flex-col gap-2">
            <label
              v-for="s in statusOptions"
              :key="s.value"
              class="flex items-center gap-3 p-3 rounded-xl cursor-pointer transition-all duration-150"
              :style="product.status === s.value
                ? 'background: rgba(124,94,240,0.10); border: 1px solid rgba(124,94,240,0.25);'
                : 'background: transparent; border: 1px solid transparent;'"
            >
              <input v-model="product.status" type="radio" :value="s.value" class="accent-[var(--accent)]">
              <div>
                <p class="text-[13px] font-medium" style="color:var(--text-primary)">{{ s.label }}</p>
                <p class="text-[11px]" style="color:var(--text-muted)">{{ s.description }}</p>
              </div>
            </label>
          </div>
        </div>

        <!-- Pricing -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#ff6b6b,#ee5a24); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-dollar-sign"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Pricing</h2>
            </div>
          </div>
          <div class="px-5 pb-5 flex flex-col gap-3">
            <div>
              <label class="pf-label">Price <span style="color:#dc2626">*</span></label>
              <div class="relative">
                <span class="absolute left-3.5 top-1/2 -translate-y-1/2 text-sm" style="color:var(--text-muted)">$</span>
                <input v-model="product.price" type="number" step="0.01" min="0" required class="glass-input w-full pl-8" placeholder="0.00">
              </div>
            </div>
            <div>
              <label class="pf-label">Compare at Price</label>
              <div class="relative">
                <span class="absolute left-3.5 top-1/2 -translate-y-1/2 text-sm" style="color:var(--text-muted)">$</span>
                <input v-model="product.compareAtPrice" type="number" step="0.01" min="0" class="glass-input w-full pl-8" placeholder="0.00">
              </div>
            </div>
            <div>
              <label class="pf-label">Cost per Item</label>
              <div class="relative">
                <span class="absolute left-3.5 top-1/2 -translate-y-1/2 text-sm" style="color:var(--text-muted)">$</span>
                <input v-model="product.costPerItem" type="number" step="0.01" min="0" class="glass-input w-full pl-8" placeholder="0.00">
              </div>
            </div>
            <div
              v-if="product.compareAtPrice && product.price && product.compareAtPrice > product.price"
              class="flex items-center gap-2 p-2.5 rounded-xl text-[12px] font-medium"
              style="background: rgba(52,199,89,0.12); color: #22c55e; border: 1px solid rgba(52,199,89,0.2);"
            >
              <i class="fas fa-tag text-[11px]"></i>
              {{ Math.round((1 - product.price / product.compareAtPrice) * 100) }}% off — sale badge will show
            </div>
          </div>
        </div>

        <!-- Inventory -->
        <div class="glass-card p-0 overflow-hidden">
          <div class="px-5 pt-5 pb-3">
            <div class="flex items-center gap-2">
              <div class="stat-icon" style="background: linear-gradient(135deg,#c084fc,#9333ea); width:30px; height:30px; font-size:13px;">
                <i class="fas fa-boxes-stacked"></i>
              </div>
              <h2 class="text-[14px] font-semibold" style="color:var(--text-primary)">Inventory</h2>
            </div>
          </div>
          <div class="px-5 pb-5 flex flex-col gap-3">
            <div>
              <label class="pf-label">Stock Quantity <span style="color:#dc2626">*</span></label>
              <input v-model="product.stock" type="number" min="0" required class="glass-input w-full" placeholder="0">
            </div>
            <div>
              <label class="pf-label">Low Stock Threshold</label>
              <input v-model="product.lowStockThreshold" type="number" min="0" class="glass-input w-full" placeholder="10">
              <p class="text-[11px] mt-1.5" style="color:var(--text-muted)">Alert when stock drops below this number</p>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { productService } from '@/services/productService';
import { useProductStore } from '@/stores/productStore';
import { useToast } from '@/composables/useToast';

const route  = useRoute();
const router = useRouter();
const store  = useProductStore();
const toast  = useToast();

const isEditing = computed(() => !!route.params.id);
const isSubmitting = ref(false);

const product = ref({
  name: '', sku: '', categoryId: '', brand: '',
  price: null as number | null,
  compareAtPrice: null as number | null,
  costPerItem: null as number | null,
  stock: null as number | null,
  lowStockThreshold: 10,
  description: '',
  status: 'active',
});

const statusOptions = [
  { value: 'active',   label: 'Active',    description: 'Visible and purchasable in your store' },
  { value: 'draft',    label: 'Draft',     description: 'Hidden from customers until published' },
  { value: 'archived', label: 'Archived',  description: 'Removed from store, kept for records' },
];

const categories = ref([
  { id: 1, name: 'Electronics' },
  { id: 2, name: 'Clothing' },
  { id: 3, name: 'Home & Garden' },
]);

// Variants
const variants = ref<Array<{ options: string; sku: string; price: number | null; stock: number | null }>>([]);
const addVariant    = () => variants.value.push({ options: '', sku: '', price: null, stock: 0 });
const removeVariant = (i: number) => variants.value.splice(i, 1);

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

onMounted(async () => {
  if (!isEditing.value) return;
  await store.fetchAll();
  const p = store.items.find(p => p.id === Number(route.params.id));
  if (p) {
    product.value.name             = p.name;
    product.value.sku              = p.sku ?? '';
    product.value.categoryId       = p.categories ?? '';
    product.value.price            = p.price;
    product.value.stock            = p.stock;
    product.value.status           = p.status as typeof product.value.status;
    if (p.images?.[0]) mainImagePreview.value = p.images[0];
  }
});

const saveAsDraft = () => { product.value.status = 'draft'; submitForm(); };

const submitForm = async () => {
  isSubmitting.value = true;
  try {
    const payload = {
      name:       product.value.name,
      sku:        product.value.sku,
      categories: product.value.categoryId,
      price:      product.value.price ?? 0,
      stock:      product.value.stock ?? 0,
      status:     product.value.status,
      images:     mainImagePreview.value ? [mainImagePreview.value] : [],
      variants:   variants.value,
    };
    if (isEditing.value) {
      const updated = await productService.updateProduct(Number(route.params.id), payload);
      store.updateItem(Number(route.params.id), updated);
    } else {
      const created = await productService.createProduct(payload);
      store.prependItem(created);
    }
    toast.success(isEditing.value ? 'Product updated' : 'Product created', 'Saved');
    router.push('/products');
  } catch (e: unknown) {
    toast.error(e instanceof Error ? e.message : 'Save failed', 'Error');
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
.pf-label {
  display: block;
  font-size: 12px;
  font-weight: 500;
  color: var(--text-secondary);
  margin-bottom: 6px;
}
</style>
