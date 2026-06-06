<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">{{ isEditing ? 'Edit Category' : 'Create Category' }}</h1>
        <p class="page-subtitle">{{ isEditing ? 'Update your category details' : 'Add a new category to organize your products' }}</p>
      </div>
      <div class="flex items-center gap-3">
        <button type="button" @click="handleCancel" class="btn-glass text-sm">Cancel</button>
        <button type="button" @click="handleSubmit" :disabled="submitting" class="btn-accent text-sm">
          <i v-if="submitting" class="fas fa-spinner fa-spin text-xs mr-1"></i>
          {{ isEditing ? 'Update Category' : 'Create Category' }}
        </button>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Main Form -->
      <div class="lg:col-span-2">
        <Card>
          <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Category Details</h2>
          </div>
          <div class="p-5">
            <form @submit.prevent="handleSubmit" class="flex flex-col gap-5">
              <!-- Name -->
              <div>
                <label class="block text-sm font-medium mb-2" style="color: var(--text-secondary);">
                  Category Name <span style="color: var(--ni-red);">*</span>
                </label>
                <input type="text" v-model="form.name" required placeholder="Enter category name"
                  class="glass-input w-full"
                  :style="errors.name ? 'border-color: var(--ni-red);' : ''" />
                <p v-if="errors.name" class="mt-1 text-xs" style="color: var(--ni-red);">{{ errors.name }}</p>
              </div>

              <!-- Description -->
              <div>
                <label class="block text-sm font-medium mb-2" style="color: var(--text-secondary);">Description</label>
                <textarea v-model="form.description" rows="4" placeholder="Enter category description"
                  class="glass-input w-full resize-none"></textarea>
              </div>

              <!-- Parent + Icon -->
              <div class="grid grid-cols-1 md:grid-cols-2 gap-5">
                <div>
                  <label class="block text-sm font-medium mb-2" style="color: var(--text-secondary);">Parent Category</label>
                  <select v-model="form.parentId" class="glass-select w-full">
                    <option value="">No Parent Category</option>
                    <option v-for="category in parentCategories" :key="category.id" :value="category.id">
                      {{ category.name }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium mb-2" style="color: var(--text-secondary);">Icon</label>
                  <div class="relative">
                    <i :class="form.icon || 'fas fa-folder'" class="absolute left-3 top-1/2 -translate-y-1/2" style="color: var(--text-secondary);"></i>
                    <input type="text" v-model="form.icon" placeholder="fas fa-folder" class="glass-input w-full pl-9" />
                  </div>
                  <p class="mt-1 text-xs" style="color: var(--text-muted);">Font Awesome class (e.g., "fas fa-folder")</p>
                </div>
              </div>

              <!-- Status + Display Order -->
              <div class="grid grid-cols-1 md:grid-cols-2 gap-5">
                <div>
                  <label class="block text-sm font-medium mb-2" style="color: var(--text-secondary);">
                    Status <span style="color: var(--ni-red);">*</span>
                  </label>
                  <div class="flex gap-5">
                    <label class="flex items-center gap-2 cursor-pointer text-sm" style="color: var(--text-primary);">
                      <input type="radio" v-model="form.status" value="active" style="accent-color: var(--accent);" />
                      Active
                    </label>
                    <label class="flex items-center gap-2 cursor-pointer text-sm" style="color: var(--text-primary);">
                      <input type="radio" v-model="form.status" value="inactive" style="accent-color: var(--accent);" />
                      Inactive
                    </label>
                  </div>
                </div>
                <div>
                  <label class="block text-sm font-medium mb-2" style="color: var(--text-secondary);">Display Order</label>
                  <input type="number" v-model="form.displayOrder" min="0" class="glass-input w-full" />
                </div>
              </div>

              <!-- SEO -->
              <div class="pt-4" style="border-top: 1px solid var(--glass-border);">
                <h3 class="text-sm font-semibold mb-4" style="color: var(--text-primary);">SEO Settings</h3>
                <div class="flex flex-col gap-4">
                  <div>
                    <label class="block text-sm font-medium mb-2" style="color: var(--text-secondary);">Meta Keywords</label>
                    <input type="text" v-model="form.metaKeywords" placeholder="e.g., electronics, gadgets, tech" class="glass-input w-full" />
                  </div>
                  <div>
                    <label class="block text-sm font-medium mb-2" style="color: var(--text-secondary);">Meta Description</label>
                    <textarea v-model="form.metaDescription" rows="2" placeholder="Brief description for search engines" class="glass-input w-full resize-none"></textarea>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </Card>
      </div>

      <!-- Sidebar -->
      <div class="flex flex-col gap-4">
        <!-- Image -->
        <Card>
          <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h3 class="text-[15px] font-semibold" style="color: var(--text-primary);">Category Image</h3>
          </div>
          <div class="p-5">
            <div v-if="form.imageUrl" class="relative rounded-xl overflow-hidden" style="border: 1px solid var(--glass-border);">
              <img :src="form.imageUrl" :alt="form.name" class="w-full h-44 object-cover" />
              <button @click="form.imageUrl = ''" class="absolute top-2 right-2 w-7 h-7 rounded-full flex items-center justify-center" style="background: var(--ni-red); color: var(--text-primary);">
                <i class="fas fa-times text-xs"></i>
              </button>
            </div>
            <div v-else class="rounded-xl p-8 text-center cursor-pointer transition-colors" style="border: 2px dashed var(--glass-border);"
              @click="triggerFileUpload">
              <i class="fas fa-image text-3xl mb-3" style="color: var(--text-secondary);"></i>
              <p class="text-sm mb-3" style="color: var(--text-secondary);">No image selected</p>
              <input type="file" ref="fileInput" accept="image/*" class="hidden" @change="handleFileSelect" />
              <button type="button" class="btn-glass text-sm" @click.stop="triggerFileUpload">
                <i class="fas fa-upload text-xs mr-1"></i> Upload Image
              </button>
            </div>
          </div>
        </Card>

        <!-- Quick Stats (edit mode) -->
        <Card v-if="isEditing">
          <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h3 class="text-[15px] font-semibold" style="color: var(--text-primary);">Quick Stats</h3>
          </div>
          <div class="p-5 flex flex-col gap-3 text-sm">
            <div class="flex justify-between py-2" style="border-bottom: 1px solid var(--glass-border);">
              <span style="color: var(--text-secondary);">Total Products</span>
              <span class="font-semibold" style="color: var(--text-primary);">{{ categoryStats?.productCount || 0 }}</span>
            </div>
            <div class="flex justify-between py-2" style="border-bottom: 1px solid var(--glass-border);">
              <span style="color: var(--text-secondary);">Active Products</span>
              <span class="font-semibold" style="color: var(--text-primary);">{{ categoryStats?.activeProducts || 0 }}</span>
            </div>
            <div class="flex justify-between py-2" style="border-bottom: 1px solid var(--glass-border);">
              <span style="color: var(--text-secondary);">Created</span>
              <span style="color: var(--text-primary);">{{ formatDate(form.createdAt) }}</span>
            </div>
            <div class="flex justify-between py-2">
              <span style="color: var(--text-secondary);">Last Updated</span>
              <span style="color: var(--text-primary);">{{ formatDate(form.updatedAt) }}</span>
            </div>
          </div>
        </Card>

        <!-- Preview -->
        <Card>
          <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h3 class="text-[15px] font-semibold" style="color: var(--text-primary);">Preview</h3>
          </div>
          <div class="p-5">
            <div class="p-4 rounded-xl" style="background: var(--glass-bg); border: 1px solid var(--glass-border);">
              <div class="flex items-center gap-3 mb-3">
                <div class="w-12 h-12 rounded-lg flex items-center justify-center" style="background: rgba(var(--accent-rgb, 59, 130, 246), 0.1);">
                  <i :class="form.icon || 'fas fa-folder'" class="text-xl" style="color: var(--accent);"></i>
                </div>
                <div>
                  <p class="font-medium text-sm" style="color: var(--text-primary);">{{ form.name || 'Category Name' }}</p>
                  <Badge :variant="form.status === 'active' ? 'success' : 'secondary'">{{ form.status || 'active' }}</Badge>
                </div>
              </div>
              <p class="text-xs" style="color: var(--text-secondary);">{{ form.description || 'No description provided' }}</p>
            </div>
          </div>
        </Card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import Breadcrumb from '@/components/layout/Breadcrumb.vue';
import Card from '@/components/ui/Card.vue';
import Button from '@/components/ui/Button.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatDate } from '@/utils/formatters';

const router = useRouter();
const route = useRoute();

// State
const submitting = ref(false);
const fileInput = ref<HTMLInputElement | null>(null);
const errors = reactive<Record<string, string>>({});

// Mock data for parent categories
const parentCategories = ref([
  { id: 1, name: 'Electronics' },
  { id: 2, name: 'Clothing' },
  { id: 3, name: 'Home & Kitchen' },
  { id: 4, name: 'Books' }
]);

// Mock data for category stats (edit mode)
const categoryStats = ref({
  productCount: 42,
  activeProducts: 38,
  totalRevenue: 15420.50
});

// Determine if we're editing or creating
const isEditing = computed(() => route.params.id !== undefined);

// Mock category data for edit mode
const mockCategoryData = {
  id: 1,
  name: 'Electronics',
  description: 'Electronic devices and gadgets including phones, laptops, and accessories',
  icon: 'fas fa-laptop',
  parentId: null,
  status: 'active',
  displayOrder: 1,
  imageUrl: 'https://images.unsplash.com/photo-1550009158-9ebf69173e03?w=800&h=800&fit=crop',
  metaKeywords: 'electronics, gadgets, tech, devices',
  metaDescription: 'Browse our wide selection of electronic devices and gadgets',
  createdAt: '2024-01-15T10:30:00Z',
  updatedAt: '2024-02-20T14:45:00Z'
};

// Form state
const form = reactive({
  name: '',
  description: '',
  icon: 'fas fa-folder',
  parentId: '',
  status: 'active',
  displayOrder: 0,
  imageUrl: '',
  metaKeywords: '',
  metaDescription: '',
  createdAt: '',
  updatedAt: ''
});

// Load mock data if editing
onMounted(() => {
  if (isEditing.value) {
    // Simulate API call to fetch category data
    setTimeout(() => {
      Object.assign(form, mockCategoryData);
    }, 300);
  }
});

// Breadcrumbs
const breadcrumbs = computed(() => {
  const items = [
    { path: '/dashboard', name: 'Dashboard' },
    { path: '/categories', name: 'Categories' }
  ];

  if (isEditing.value) {
    items.push({
      path: route.path,
      name: `Edit: ${form.name || 'Category'}`
    });
  } else {
    items.push({
      path: '/categories/new',
      name: 'New Category'
    });
  }

  return items;
});

// Methods
const validateForm = (): boolean => {
  errors.name = '';

  if (!form.name.trim()) {
    errors.name = 'Category name is required';
    return false;
  }

  if (form.name.length < 2) {
    errors.name = 'Category name must be at least 2 characters';
    return false;
  }

  return true;
};

const handleSubmit = async () => {
  if (!validateForm()) return;

  submitting.value = true;

  try {
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1500));

    if (isEditing.value) {
      console.log('Updating category:', { id: route.params.id, ...form });
      alert('Category updated successfully!');
    } else {
      console.log('Creating category:', form);
      alert('Category created successfully!');
    }

    router.push('/categories');
  } catch (error) {
    console.error('Error saving category:', error);
    alert('Failed to save category. Please try again.');
  } finally {
    submitting.value = false;
  }
};

const handleCancel = () => {
  if (confirm('Are you sure you want to cancel? Any unsaved changes will be lost.')) {
    router.push('/categories');
  }
};

const triggerFileUpload = () => {
  fileInput.value?.click();
};

const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];

  if (file) {
    // In a real app, you would upload this to a server
    // For mock, we'll create a local URL
    const reader = new FileReader();
    reader.onload = (e) => {
      form.imageUrl = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  }
};

// Watch for changes (for debugging)
watch(form, (newVal) => {
  console.log('Form updated:', newVal);
}, { deep: true });
</script>

