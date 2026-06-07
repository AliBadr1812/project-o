import { ref } from 'vue';
import { defineStore } from 'pinia';
import { customerService } from '@/services/customerService';
import type { Customer } from '@/types/customer';

export const useCustomerStore = defineStore('customers', () => {
  const items   = ref<Customer[]>([]);
  const loading = ref(false);
  const error   = ref<string | null>(null);
  const loaded  = ref(false);

  /** Fetch all customers. Skips the request if data is already loaded unless force = true. */
  async function fetchAll(force = false) {
    if (loaded.value && !force) return;
    loading.value = true;
    error.value   = null;
    try {
      items.value  = await customerService.getAllCustomers();
      loaded.value = true;
    } catch (e: any) {
      error.value = e?.message ?? 'Failed to load customers';
    } finally {
      loading.value = false;
    }
  }

  /** Delete a customer and remove them from the local cache. */
  async function remove(id: number) {
    await customerService.deleteCustomer(id);
    items.value = items.value.filter(c => c.id !== id);
  }

  return { items, loading, error, loaded, fetchAll, remove };
});
