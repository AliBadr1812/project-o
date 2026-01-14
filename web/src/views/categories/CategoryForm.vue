<template>
  <div class="category-form">
    <Breadcrumb
      :items="breadcrumbs"
      class="mb-6"
    />

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <div class="lg:col-span-2">
        <Card>
          <div class="mb-6">
            <h2 class="text-xl font-semibold text-gray-800">
              {{ isEditing ? 'Edit Category' : 'Create New Category' }}
            </h2>
            <p class="text-gray-600 mt-1">
              {{ isEditing ? 'Update your category details' : 'Add a new category to organize your products' }}
            </p>
          </div>

          <form @submit.prevent="handleSubmit">
            <div class="gap-6">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Category Name *
                </label>
                <input
                  type="text"
                  v-model="form.name"
                  required
                  placeholder="Enter category name"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  :class="{ 'border-red-500': errors.name }"
                />
                <p v-if="errors.name" class="mt-1 text-sm text-red-600">
                  {{ errors.name }}
                </p>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Description
                </label>
                <textarea
                  v-model="form.description"
                  rows="3"
                  placeholder="Enter category description"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                ></textarea>
              </div>

              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Parent Category
                  </label>
                  <select
                    v-model="form.parentId"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  >
                    <option value="">No Parent Category</option>
                    <option
                      v-for="category in parentCategories"
                      :key="category.id"
                      :value="category.id"
                    >
                      {{ category.name }}
                    </option>
                  </select>
                </div>

                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Icon
                  </label>
                  <div class="relative">
                    <i
                      :class="form.icon || 'fas fa-folder'"
                      class="absolute left-3 top-3 text-gray-400"
                    ></i>
                    <input
                      type="text"
                      v-model="form.icon"
                      placeholder="fas fa-folder"
                      class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    />
                  </div>
                  <p class="mt-1 text-sm text-gray-500">
                    Use Font Awesome class names (e.g., "fas fa-folder")
                  </p>
                </div>
              </div>

              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Status *
                  </label>
                  <div class="flex gap-4">
                    <label class="flex items-center">
                      <input
                        type="radio"
                        v-model="form.status"
                        value="active"
                        class="mr-2"
                      />
                      <span>Active</span>
                    </label>
                    <label class="flex items-center">
                      <input
                        type="radio"
                        v-model="form.status"
                        value="inactive"
                        class="mr-2"
                      />
                      <span>Inactive</span>
                    </label>
                  </div>
                </div>

                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Display Order
                  </label>
                  <input
                    type="number"
                    v-model="form.displayOrder"
                    min="0"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Meta Keywords (for SEO)
                </label>
                <input
                  type="text"
                  v-model="form.metaKeywords"
                  placeholder="e.g., electronics, gadgets, tech"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Meta Description (for SEO)
                </label>
                <textarea
                  v-model="form.metaDescription"
                  rows="2"
                  placeholder="Brief description for search engines"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                ></textarea>
              </div>
            </div>

            <div class="flex justify-end gap-4 pt-6 mt-6 border-t border-gray-200">
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

      <div>
        <Card class="mb-6">
          <h3 class="text-lg font-semibold text-gray-800 mb-4">
            Category Image
          </h3>
          <div class="gap-4">
            <div
              v-if="form.imageUrl"
              class="relative rounded-lg overflow-hidden border border-gray-200"
            >
              <img
                :src="form.imageUrl"
                :alt="form.name"
                class="w-full h-48 object-cover"
              />
              <button
                @click="form.imageUrl = ''"
                class="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1 hover:bg-red-600"
              >
                <i class="fas fa-times"></i>
              </button>
            </div>
            <div
              v-else
              class="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center"
            >
              <i class="fas fa-image text-4xl text-gray-400 mb-3"></i>
              <p class="text-gray-600 mb-4">No image selected</p>
              <Button
                variant="outline"
                @click="handleImageUpload"
              >
                <i class="fas fa-upload mr-2"></i> Upload Image
              </Button>
            </div>
          </div>
        </Card>

        <Card>
          <h3 class="text-lg font-semibold text-gray-800 mb-4">
            Quick Stats
          </h3>
          <div v-if="isEditing" class="gap-4">
            <div class="flex justify-between items-center">
              <span class="text-gray-600">Total Products</span>
              <span class="font-semibold">{{ categoryStats?.productCount || 0 }}</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-600">Active Products</span>
              <span class="font-semibold">{{ categoryStats?.activeProducts || 0 }}</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-600">Created</span>
              <span class="text-gray-800">{{ formatDate(form.createdAt) }}</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-600">Last Updated</span>
              <span class="text-gray-800">{{ formatDate(form.updatedAt) }}</span>
            </div>
          </div>
          <div v-else class="text-center py-4 text-gray-500">
            Stats will appear after creation
          </div>
        </Card>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import Breadcrumb from '@/components/layout/Breadcrumb.vue';
import Card from '@/components/ui/Card.vue';
import Button from '@/components/ui/Button.vue';
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { formatDate } from '@/utils/formatters';

const route = useRoute();

const breadcrumbs = computed(() => {
  const items = [
    { path: '/dashboard', name: 'Dashboard' }
  ];

  if (route.meta.breadcrumb) {
    items.push({
      path: route.path,
      name: route.meta.breadcrumb as string
    });
  }

  return items;
});
</script>
