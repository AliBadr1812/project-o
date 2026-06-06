<template>
  <div class="category-form min-h-screen">

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Main Form Column -->
      <div class="lg:col-span-2">
        <Card class="p-6">
          <div class="mb-6">
            <h2 class="text-2xl font-bold text-white">
              {{ isEditing ? 'Edit Category' : 'Create New Category' }}
            </h2>
            <p class="text-[var(--color-icon)] mt-1">
              {{ isEditing ? 'Update your category details' : 'Add a new category to organize your products' }}
            </p>
          </div>

          <form @submit.prevent="handleSubmit" class="space-y-6">
            <!-- Category Name -->
            <div>
              <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                Category Name <span class="text-red-500">*</span>
              </label>
              <input
                type="text"
                v-model="form.name"
                required
                placeholder="Enter category name"
                class="w-full px-4 py-2.5 bg-[var(--color-card)] border border-[var(--color-border)] rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                :class="{ 'border-red-500': errors.name }"
              />
              <p v-if="errors.name" class="mt-1 text-sm text-red-500">
                {{ errors.name }}
              </p>
            </div>

            <!-- Description -->
            <div>
              <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                Description
              </label>
              <textarea
                v-model="form.description"
                rows="4"
                placeholder="Enter category description"
                class="w-full px-4 py-2.5 bg-[var(--color-card)] border border-[var(--color-border)] rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              ></textarea>
            </div>

            <!-- Parent Category and Icon -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                  Parent Category
                </label>
                <select
                  v-model="form.parentId"
                  class="w-full px-4 py-2.5 bg-[var(--color-card)] border border-[var(--color-border)] rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                >
                  <option value="" class="bg-[var(--color-primary)]">No Parent Category</option>
                  <option
                    v-for="category in parentCategories"
                    :key="category.id"
                    :value="category.id"
                    class="bg-[var(--color-primary)]"
                  >
                    {{ category.name }}
                  </option>
                </select>
              </div>

              <div>
                <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                  Icon
                </label>
                <div class="relative">
                  <i
                    :class="form.icon || 'fas fa-folder'"
                    class="absolute left-3 top-3 text-[var(--color-icon)]"
                  ></i>
                  <input
                    type="text"
                    v-model="form.icon"
                    placeholder="fas fa-folder"
                    class="w-full pl-10 pr-4 py-2.5 bg-[var(--color-card)] border border-[var(--color-border)] rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
                <p class="mt-1 text-sm text-[var(--color-icon)]">
                  Use Font Awesome class names (e.g., "fas fa-folder")
                </p>
              </div>
            </div>

            <!-- Status and Display Order -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                  Status <span class="text-red-500">*</span>
                </label>
                <div class="flex gap-6">
                  <label class="flex items-center cursor-pointer">
                    <input
                      type="radio"
                      v-model="form.status"
                      value="active"
                      class="mr-2 accent-blue-500"
                    />
                    <span class="text-white">Active</span>
                  </label>
                  <label class="flex items-center cursor-pointer">
                    <input
                      type="radio"
                      v-model="form.status"
                      value="inactive"
                      class="mr-2 accent-blue-500"
                    />
                    <span class="text-white">Inactive</span>
                  </label>
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                  Display Order
                </label>
                <input
                  type="number"
                  v-model="form.displayOrder"
                  min="0"
                  class="w-full px-4 py-2.5 bg-[var(--color-card)] border border-[var(--color-border)] rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
              </div>
            </div>

            <!-- SEO Fields -->
            <div>
              <h3 class="text-lg font-semibold text-white mb-4">SEO Settings</h3>
              <div class="space-y-4">
                <div>
                  <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                    Meta Keywords
                  </label>
                  <input
                    type="text"
                    v-model="form.metaKeywords"
                    placeholder="e.g., electronics, gadgets, tech"
                    class="w-full px-4 py-2.5 bg-[var(--color-card)] border border-[var(--color-border)] rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-[var(--color-icon)] mb-2">
                    Meta Description
                  </label>
                  <textarea
                    v-model="form.metaDescription"
                    rows="2"
                    placeholder="Brief description for search engines"
                    class="w-full px-4 py-2.5 bg-[var(--color-card)] border border-[var(--color-border)] rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  ></textarea>
                </div>
              </div>
            </div>

            <!-- Form Actions -->
            <div class="flex justify-end gap-4 pt-6 mt-6 border-t border-[var(--color-border)]">
              <Button
                type="button"
                variant="outline"
                @click="handleCancel"
              >
                Cancel
              </Button>
              <Button
                type="submit"
                variant="primary"
                :loading="submitting"
              >
                {{ isEditing ? 'Update Category' : 'Create Category' }}
              </Button>
            </div>
          </form>
        </Card>
      </div>

      <!-- Sidebar Column -->
      <div class="space-y-6">
        <!-- Category Image Card -->
        <Card class="p-6">
          <h3 class="text-lg font-semibold text-white mb-4">
            Category Image
          </h3>
          <div class="space-y-4">
            <div
              v-if="form.imageUrl"
              class="relative rounded-lg overflow-hidden border border-[var(--color-border)]"
            >
              <img
                :src="form.imageUrl"
                :alt="form.name"
                class="w-full h-48 object-cover"
              />
              <button
                @click="form.imageUrl = ''"
                class="absolute top-2 right-2 bg-red-500 text-white rounded-full p-2 hover:bg-red-600 transition-colors"
              >
                <i class="fas fa-times"></i>
              </button>
            </div>
            <div
              v-else
              class="border-2 border-dashed border-[var(--color-border)] rounded-lg p-8 text-center hover:border-blue-500 transition-colors"
            >
              <i class="fas fa-image text-4xl text-[var(--color-icon)] mb-3"></i>
              <p class="text-[var(--color-icon)] mb-4">No image selected</p>
              <input
                type="file"
                ref="fileInput"
                accept="image/*"
                class="hidden"
                @change="handleFileSelect"
              />
              <Button
                variant="outline"
                @click="triggerFileUpload"
              >
                <i class="fas fa-upload mr-2"></i> Upload Image
              </Button>
            </div>
          </div>
        </Card>

        <!-- Quick Stats Card (only in edit mode) -->
        <Card v-if="isEditing" class="p-6">
          <h3 class="text-lg font-semibold text-white mb-4">
            Quick Stats
          </h3>
          <div class="space-y-3">
            <div class="flex justify-between items-center py-2 border-b border-[var(--color-border)]">
              <span class="text-[var(--color-icon)]">Total Products</span>
              <span class="font-semibold text-white">{{ categoryStats?.productCount || 0 }}</span>
            </div>
            <div class="flex justify-between items-center py-2 border-b border-[var(--color-border)]">
              <span class="text-[var(--color-icon)]">Active Products</span>
              <span class="font-semibold text-white">{{ categoryStats?.activeProducts || 0 }}</span>
            </div>
            <div class="flex justify-between items-center py-2 border-b border-[var(--color-border)]">
              <span class="text-[var(--color-icon)]">Created</span>
              <span class="text-white">{{ formatDate(form.createdAt) }}</span>
            </div>
            <div class="flex justify-between items-center py-2">
              <span class="text-[var(--color-icon)]">Last Updated</span>
              <span class="text-white">{{ formatDate(form.updatedAt) }}</span>
            </div>
          </div>
        </Card>

        <!-- Preview Card -->
        <Card class="p-6">
          <h3 class="text-lg font-semibold text-white mb-4">
            Preview
          </h3>
          <div class="bg-[var(--color-card)] rounded-lg p-4 border border-[var(--color-border)]">
            <div class="flex items-center gap-3">
              <div
                class="w-12 h-12 rounded-lg bg-blue-500/10 flex items-center justify-center"
              >
                <i :class="form.icon || 'fas fa-folder'" class="text-blue-500 text-xl"></i>
              </div>
              <div>
                <p class="font-medium text-white">{{ form.name || 'Category Name' }}</p>
                <Badge :variant="form.status === 'active' ? 'success' : 'secondary'">
                  {{ form.status || 'active' }}
                </Badge>
              </div>
            </div>
            <p class="text-sm text-[var(--color-icon)] mt-3 line-clamp-2">
              {{ form.description || 'No description provided' }}
            </p>
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

<style scoped>
/* Override input styles for better contrast */
input[type="text"],
input[type="number"],
textarea,
select {
  background-color: var(--color-card);
  color: white;
}

input[type="text"]::placeholder,
textarea::placeholder {
  color: #6b7280;
}

select option {
  background-color: var(--color-primary);
  color: white;
}

/* Custom scrollbar for textareas */
textarea {
  scrollbar-width: thin;
  scrollbar-color: var(--color-border) var(--color-card);
}

textarea::-webkit-scrollbar {
  width: 8px;
}

textarea::-webkit-scrollbar-track {
  background: var(--color-card);
}

textarea::-webkit-scrollbar-thumb {
  background-color: var(--color-border);
  border-radius: 4px;
}

/* Line clamp utility */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
