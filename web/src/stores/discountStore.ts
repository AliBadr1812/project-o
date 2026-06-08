import { ref } from 'vue';
import { defineStore } from 'pinia';
import { discountService } from '@/services/discountService';
import type { Discount } from '@/types/discount';

export const useDiscountStore = defineStore('discounts', () => {
  const items   = ref<Discount[]>([]);
  const loading = ref(false);
  const error   = ref<string | null>(null);
  const loaded  = ref(false);

  async function fetchAll(force = false) {
    if (loaded.value && !force) return;
    loading.value = true;
    error.value   = null;
    try {
      items.value  = await discountService.getAllDiscounts();
      loaded.value = true;
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to load discounts';
    } finally {
      loading.value = false;
    }
  }

  async function remove(id: number) {
    await discountService.deleteDiscount(id);
    items.value = items.value.filter(d => d.id !== id);
  }

  function prependItem(item: Discount) {
    items.value.unshift(item);
  }

  function updateItem(id: number, patch: Partial<Discount>) {
    const idx = items.value.findIndex(d => d.id === id);
    if (idx !== -1) items.value[idx] = { ...items.value[idx]!, ...patch };
  }

  return { items, loading, error, loaded, fetchAll, remove, prependItem, updateItem };
});
