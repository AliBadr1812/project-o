import { ref } from 'vue';
import { defineStore } from 'pinia';
import { returnService } from '@/services/returnService';
import type { Return } from '@/types/return';

export const useReturnStore = defineStore('returns', () => {
  const items   = ref<Return[]>([]);
  const loading = ref(false);
  const error   = ref<string | null>(null);
  const loaded  = ref(false);

  async function fetchAll(force = false) {
    if (loaded.value && !force) return;
    loading.value = true;
    error.value   = null;
    try {
      items.value  = await returnService.getAllReturns();
      loaded.value = true;
    } catch (e: any) {
      error.value = e?.message ?? 'Failed to load returns';
    } finally {
      loading.value = false;
    }
  }

  async function remove(id: number) {
    await returnService.deleteReturn(id);
    items.value = items.value.filter(r => r.id !== id);
  }

  function prependItem(item: Return) {
    items.value.unshift(item);
  }

  function updateItem(id: number, patch: Partial<Return>) {
    const idx = items.value.findIndex(r => r.id === id);
    if (idx !== -1) items.value[idx] = { ...items.value[idx], ...patch };
  }

  return { items, loading, error, loaded, fetchAll, remove, prependItem, updateItem };
});
