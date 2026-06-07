import { ref } from 'vue';
import { defineStore } from 'pinia';
import { productService } from '@/services/productService';
import type { Product } from '@/types/product';

export const useProductStore = defineStore('products', () => {
  const items   = ref<Product[]>([]);
  const loading = ref(false);
  const error   = ref<string | null>(null);
  const loaded  = ref(false);

  /** Fetch all products. Skips the request if data is already loaded unless force = true. */
  async function fetchAll(force = false) {
    if (loaded.value && !force) return;
    loading.value = true;
    error.value   = null;
    try {
      items.value  = await productService.getAllProducts();
      loaded.value = true;
    } catch (e: any) {
      error.value = e?.message ?? 'Failed to load products';
    } finally {
      loading.value = false;
    }
  }

  /** Delete a product and remove it from the local cache. */
  async function remove(id: number) {
    await productService.deleteProduct(id);
    items.value = items.value.filter(p => p.id !== id);
  }

  /** Prepend an item to the local cache (used for client-side duplicate). */
  function prependItem(item: Product) {
    items.value.unshift(item);
  }

  /** Patch a product in the local cache by id. */
  function updateItem(id: number, patch: Partial<Product>) {
    const idx = items.value.findIndex(p => p.id === id);
    if (idx !== -1) items.value[idx] = { ...items.value[idx], ...patch };
  }

  return { items, loading, error, loaded, fetchAll, remove, prependItem, updateItem };
});
