import { ref } from 'vue';
import { defineStore } from 'pinia';
import { segmentService } from '@/services/segmentService';
import type { Segment } from '@/types/segment';

export const useSegmentStore = defineStore('segments', () => {
  const items   = ref<Segment[]>([]);
  const loading = ref(false);
  const error   = ref<string | null>(null);
  const loaded  = ref(false);

  async function fetchAll(force = false) {
    if (loaded.value && !force) return;
    loading.value = true;
    error.value   = null;
    try {
      items.value  = await segmentService.getAllSegments();
      loaded.value = true;
    } catch (e: any) {
      error.value = e?.message ?? 'Failed to load segments';
    } finally {
      loading.value = false;
    }
  }

  async function remove(id: number) {
    await segmentService.deleteSegment(id);
    items.value = items.value.filter(s => s.id !== id);
  }

  function prependItem(item: Segment) {
    items.value.unshift(item);
  }

  function updateItem(id: number, patch: Partial<Segment>) {
    const idx = items.value.findIndex(s => s.id === id);
    if (idx !== -1) items.value[idx] = { ...items.value[idx], ...patch };
  }

  return { items, loading, error, loaded, fetchAll, remove, prependItem, updateItem };
});
