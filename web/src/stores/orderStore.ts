import { ref } from 'vue';
import { defineStore } from 'pinia';
import { orderService } from '@/services/orderService';
import type { Order } from '@/types/order';

export const useOrderStore = defineStore('orders', () => {
  const items   = ref<Order[]>([]);
  const loading = ref(false);
  const error   = ref<string | null>(null);
  const loaded  = ref(false);

  /** Fetch all orders. Skips the request if data is already loaded unless force = true. */
  async function fetchAll(force = false) {
    if (loaded.value && !force) return;
    loading.value = true;
    error.value   = null;
    try {
      items.value  = await orderService.getAllOrders();
      loaded.value = true;
    } catch (e: any) {
      error.value = e?.message ?? 'Failed to load orders';
    } finally {
      loading.value = false;
    }
  }

  /** Partially update an order in the local cache (e.g. status change). */
  function updateItem(id: number, patch: Partial<Order>) {
    const idx = items.value.findIndex(o => o.id === id);
    if (idx !== -1) items.value[idx] = { ...items.value[idx], ...patch };
  }

  /** Delete an order and remove it from the local cache. */
  async function remove(id: number) {
    await orderService.deleteOrder(id);
    items.value = items.value.filter(o => o.id !== id);
  }

  return { items, loading, error, loaded, fetchAll, updateItem, remove };
});
