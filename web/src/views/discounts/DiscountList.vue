<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Discounts</h1>
        <p class="page-subtitle">Manage coupon codes and promotional discounts</p>
      </div>
      <div class="flex items-center gap-3">
        <button @click="store.fetchAll(true)" class="btn-glass text-sm">
          <i class="fas fa-sync-alt text-xs mr-1"></i>Refresh
        </button>
        <router-link to="/discounts/create" class="btn-accent text-sm">
          <i class="fas fa-plus text-xs mr-1"></i>New Discount
        </router-link>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-4">
          <div class="stat-icon ni-b"><i class="fas fa-ticket"></i></div>
          <span class="badge badge-info">All</span>
        </div>
        <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Total Codes</p>
        <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ items.length }}</p>
        <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Created codes</p>
      </div>
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-4">
          <div class="stat-icon ni-g"><i class="fas fa-circle-check"></i></div>
          <span class="badge badge-success">Active</span>
        </div>
        <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Active</p>
        <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ activeCount }}</p>
        <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Currently usable</p>
      </div>
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-4">
          <div class="stat-icon ni-o"><i class="fas fa-chart-bar"></i></div>
          <span class="badge badge-warning">Usage</span>
        </div>
        <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Total Uses</p>
        <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ totalUses }}</p>
        <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Across all codes</p>
      </div>
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-4">
          <div class="stat-icon ni-r"><i class="fas fa-ban"></i></div>
          <span class="badge badge-danger">Inactive</span>
        </div>
        <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Inactive / Expired</p>
        <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ items.length - activeCount }}</p>
        <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Not currently usable</p>
      </div>
    </div>

    <!-- Table -->
    <div class="glass-card overflow-hidden">
      <div class="px-6 py-4 flex items-center gap-3" style="border-bottom: 1px solid var(--glass-border);">
        <h2 class="text-[15px] font-semibold flex-1" style="color: var(--text-primary);">Coupon Codes</h2>
        <div class="relative">
          <i class="fas fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-xs" style="color: var(--text-muted)"></i>
          <input v-model="search" type="text" placeholder="Search codes…" class="glass-input pl-9 text-sm" style="width:200px;">
        </div>
      </div>

      <!-- Error -->
      <div v-if="error" class="px-6 py-8 text-center">
        <i class="fas fa-triangle-exclamation text-2xl mb-2" style="color: var(--ni-orange);"></i>
        <p class="text-sm mb-1" style="color: var(--text-primary);">Could not load discounts</p>
        <p class="text-xs mb-3" style="color: var(--text-muted);">{{ error }}</p>
        <button @click="store.fetchAll()" class="btn-glass text-sm">Try again</button>
      </div>

      <div v-else class="overflow-x-auto">
        <table class="glass-table w-full">
          <thead>
            <tr>
              <th>Code</th>
              <th>Description</th>
              <th>Type</th>
              <th>Value</th>
              <th>Usage</th>
              <th>Expires</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="loading">
              <td colspan="8" class="py-10 text-center">
                <i class="fas fa-spinner fa-spin mr-2" style="color: var(--text-muted);"></i>
                <span style="color: var(--text-muted);">Loading…</span>
              </td>
            </tr>
            <tr v-for="d in paginatedItems" :key="d.id">
              <td>
                <span class="font-mono text-sm font-semibold px-2 py-0.5 rounded"
                  style="background: rgba(var(--accent-rgb),0.10); color: var(--accent);">
                  {{ d.code }}
                </span>
              </td>
              <td><span class="text-sm" style="color: var(--text-secondary);">{{ d.description }}</span></td>
              <td>
                <span class="badge" :class="d.type === 'PERCENTAGE' ? 'badge-info' : 'badge-warning'">
                  {{ d.type === 'PERCENTAGE' ? '%' : '$' }} {{ d.type }}
                </span>
              </td>
              <td>
                <span class="td-accent">
                  {{ d.type === 'PERCENTAGE' ? d.value + '%' : formatCurrency(d.value) }}
                </span>
              </td>
              <td>
                <div class="flex items-center gap-2 text-sm">
                  <span style="color: var(--text-primary);">{{ d.usedCount }}</span>
                  <span style="color: var(--text-muted);">/ {{ d.usageLimit ?? '∞' }}</span>
                  <div v-if="d.usageLimit" class="progress-track w-12">
                    <div class="progress-fill" :style="{ width: Math.min((d.usedCount / d.usageLimit) * 100, 100) + '%' }"></div>
                  </div>
                </div>
              </td>
              <td>
                <span class="text-sm" :style="isExpired(d.endDate) ? 'color: var(--ni-red)' : 'color: var(--text-secondary)'">
                  {{ formatDate(d.endDate) }}
                </span>
              </td>
              <td>
                <span class="badge" :class="d.isActive && !isExpired(d.endDate) ? 'badge-success' : 'badge-danger'">
                  {{ d.isActive && !isExpired(d.endDate) ? 'Active' : 'Inactive' }}
                </span>
              </td>
              <td>
                <div class="flex items-center gap-1">
                  <button @click="editDiscount(d.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Edit">
                    <i class="fas fa-pen"></i>
                  </button>
                  <button @click="toggleActive(d)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs"
                    :title="d.isActive ? 'Deactivate' : 'Activate'">
                    <i :class="d.isActive ? 'fas fa-pause' : 'fas fa-play'"></i>
                  </button>
                  <button @click="deleteDiscount(d.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Delete"
                    style="color: var(--ni-red);">
                    <i class="fas fa-trash"></i>
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="!loading && items.length > 0 && filtered.length === 0">
              <td colspan="8" class="py-12 text-center">
                <i class="fas fa-magnifying-glass text-3xl mb-3" style="color: var(--text-muted);"></i>
                <p class="text-sm font-medium mb-1" style="color: var(--text-primary);">No results</p>
                <p class="text-xs" style="color: var(--text-muted);">No discount codes match your search.</p>
              </td>
            </tr>
          </tbody>
        </table>
        <EmptyState v-if="!loading && items.length === 0"
          title="No discount codes yet"
          description="Create your first coupon code to offer promotions to customers."
          action-text="New Discount"
          action-to="/discounts/create">
          <template #icon><i class="fas fa-ticket"></i></template>
        </EmptyState>
        <Pagination v-if="filtered.length > itemsPerPage"
          :current-page="currentPage" :total-pages="totalPages"
          :total-items="filtered.length" :items-per-page="itemsPerPage"
          @page-change="(p: number) => { currentPage = p; }" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { useDiscountStore } from '@/stores/discountStore';
import { useToast } from '@/composables/useToast';
import { useConfirm } from '@/composables/useConfirm';
import EmptyState from '@/components/shared/EmptyState.vue';
import Pagination from '@/components/ui/Pagination.vue';
import { formatCurrency, formatDate } from '@/utils/formatters';
import type { Discount } from '@/types/discount';

const router  = useRouter();
const store   = useDiscountStore();
const toast   = useToast();
const { confirm } = useConfirm();
const { items, loading, error } = storeToRefs(store);

const search      = ref('');
const currentPage  = ref(1);
const itemsPerPage = 8;

onMounted(() => store.fetchAll());

const activeCount = computed(() =>
  items.value.filter(d => d.isActive && !isExpired(d.endDate)).length,
);

const totalUses = computed(() =>
  items.value.reduce((s, d) => s + d.usedCount, 0),
);

const filtered = computed(() => {
  if (!search.value) return items.value;
  const q = search.value.toLowerCase();
  return items.value.filter(d =>
    d.code.toLowerCase().includes(q) ||
    d.description.toLowerCase().includes(q),
  );
});

const totalPages     = computed(() => Math.ceil(filtered.value.length / itemsPerPage));
const paginatedItems = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filtered.value.slice(start, start + itemsPerPage);
});

watch(search, () => { currentPage.value = 1; });

function isExpired(endDate: string): boolean {
  return endDate ? new Date(endDate) < new Date() : false;
}

const editDiscount   = (id: number) => router.push(`/discounts/${id}/edit`);

const toggleActive = async (d: Discount) => {
  const { discountService } = await import('@/services/discountService');
  const updated = await discountService.updateDiscount(d.id, { isActive: !d.isActive });
  store.updateItem(d.id, updated);
};

const deleteDiscount = async (id: number) => {
  const disc = items.value.find(d => d.id === id);
  const ok = await confirm({
    title: 'Delete discount code',
    message: `Delete "${disc?.code ?? 'this code'}"?`,
    detail: 'Customers will no longer be able to use this code.',
    confirmText: 'Delete',
    variant: 'danger',
  });
  if (!ok) return;
  try {
    await store.remove(id);
    toast.success('Discount code deleted');
  } catch (e: unknown) {
    toast.error(e instanceof Error ? e.message : 'Delete failed', 'Error');
  }
};
</script>
