import { ref } from 'vue';
import { defineStore } from 'pinia';
import { categoryService } from '@/services/categoryService';
import type { Category } from '@/types/category';

export const useCategoryStore = defineStore('categories', () => {
  const items   = ref<Category[]>([]);
  const loading = ref(false);
  const error   = ref<string | null>(null);
  const loaded  = ref(false);

  /** Fetch all categories. Skips the request if data is already loaded unless force = true. */
  async function fetchAll(force = false) {
    if (loaded.value && !force) return;
    loading.value = true;
    error.value   = null;
    try {
      items.value  = await categoryService.getAllCategories();
      loaded.value = true;
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to load categories';
    } finally {
      loading.value = false;
    }
  }

  /** Delete a category and remove it from the local cache. */
  async function remove(id: number) {
    await categoryService.deleteCategory(id);
    items.value = items.value.filter(c => c.id !== id);
  }

  return { items, loading, error, loaded, fetchAll, remove };
});
