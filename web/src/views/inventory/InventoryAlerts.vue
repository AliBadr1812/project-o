<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Inventory Alerts</h1>
        <p class="page-subtitle">Products that need restocking attention</p>
      </div>
      <div class="flex items-center gap-3">
        <button @click="store.fetchAll(true)" class="btn-glass text-sm">
          <i class="fas fa-sync-alt text-xs mr-1"></i>Refresh
        </button>
        <router-link to="/products/create" class="btn-accent text-sm">
          <i class="fas fa-plus text-xs mr-1"></i>Add Product
        </router-link>
      </div>
    </div>

    <!-- Summary Stats -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-4">
          <div class="stat-icon ni-r"><i class="fas fa-xmark"></i></div>
          <span class="badge badge-danger">Critical</span>
        </div>
        <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Out of Stock</p>
        <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ outOfStock.length }}</p>
        <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Requires immediate restock</p>
      </div>
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-4">
          <div class="stat-icon ni-o"><i class="fas fa-triangle-exclamation"></i></div>
          <span class="badge badge-warning">Low</span>
        </div>
        <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Low Stock</p>
        <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ lowStock.length }}</p>
        <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">≤ 10 units remaining</p>
      </div>
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-4">
          <div class="stat-icon ni-g"><i class="fas fa-circle-check"></i></div>
          <span class="badge badge-success">OK</span>
        </div>
        <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Well Stocked</p>
        <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ wellStocked.length }}</p>
        <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Above threshold</p>
      </div>
    </div>

    <!-- Out of Stock -->
    <div v-if="outOfStock.length" class="glass-card overflow-hidden">
      <div class="px-6 py-4 flex items-center gap-3" style="border-bottom: 1px solid var(--glass-border);">
        <div class="w-2 h-2 rounded-full bg-red-500"></div>
        <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Out of Stock</h2>
        <span class="badge badge-danger ml-auto">{{ outOfStock.length }}</span>
      </div>
      <div class="overflow-x-auto">
        <table class="glass-table w-full">
          <thead>
            <tr>
              <th>Product</th>
              <th>SKU</th>
              <th>Category</th>
              <th>Price</th>
              <th>Stock</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in outOfStock" :key="p.id">
              <td>
                <div class="flex items-center gap-3">
                  <img :src="p.images?.[0] || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                    class="w-9 h-9 rounded-lg object-cover" style="border:1px solid var(--glass-border);" />
                  <span class="td-primary">{{ p.name }}</span>
                </div>
              </td>
              <td><span class="text-xs font-mono px-2 py-0.5 rounded" style="color:var(--text-muted);background:var(--glass-bg);">{{ p.sku }}</span></td>
              <td><span class="text-sm" style="color:var(--text-secondary);">{{ p.categories || '—' }}</span></td>
              <td><span class="td-accent">{{ formatCurrency(p.price) }}</span></td>
              <td>
                <span class="badge badge-danger">0 units</span>
              </td>
              <td>
                <router-link :to="`/products/${p.id}/edit`" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Restock">
                  <i class="fas fa-pen"></i>
                </router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Low Stock -->
    <div v-if="lowStock.length" class="glass-card overflow-hidden">
      <div class="px-6 py-4 flex items-center gap-3" style="border-bottom: 1px solid var(--glass-border);">
        <div class="w-2 h-2 rounded-full bg-yellow-500"></div>
        <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Low Stock</h2>
        <span class="badge badge-warning ml-auto">{{ lowStock.length }}</span>
      </div>
      <div class="overflow-x-auto">
        <table class="glass-table w-full">
          <thead>
            <tr>
              <th>Product</th>
              <th>SKU</th>
              <th>Category</th>
              <th>Price</th>
              <th>Stock</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in lowStock" :key="p.id">
              <td>
                <div class="flex items-center gap-3">
                  <img :src="p.images?.[0] || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                    class="w-9 h-9 rounded-lg object-cover" style="border:1px solid var(--glass-border);" />
                  <span class="td-primary">{{ p.name }}</span>
                </div>
              </td>
              <td><span class="text-xs font-mono px-2 py-0.5 rounded" style="color:var(--text-muted);background:var(--glass-bg);">{{ p.sku }}</span></td>
              <td><span class="text-sm" style="color:var(--text-secondary);">{{ p.categories || '—' }}</span></td>
              <td><span class="td-accent">{{ formatCurrency(p.price) }}</span></td>
              <td>
                <div class="flex items-center gap-2">
                  <span class="font-semibold text-sm" style="color:var(--ni-orange);">{{ p.stock }} units</span>
                  <div class="progress-track w-16">
                    <div class="progress-fill" :style="{ width: Math.min((p.stock / 50)*100, 100) + '%', background: 'var(--ni-orange)' }"></div>
                  </div>
                </div>
              </td>
              <td>
                <router-link :to="`/products/${p.id}/edit`" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Restock">
                  <i class="fas fa-pen"></i>
                </router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Loading / Empty -->
    <div v-if="loading" class="glass-card p-12 text-center">
      <i class="fas fa-spinner fa-spin text-2xl mb-3" style="color:var(--text-muted);"></i>
      <p style="color:var(--text-muted);">Loading inventory…</p>
    </div>

    <div v-else-if="!outOfStock.length && !lowStock.length" class="glass-card p-12 text-center">
      <i class="fas fa-circle-check text-4xl mb-4" style="color:var(--ni-green);"></i>
      <p class="text-lg font-semibold mb-1" style="color:var(--text-primary);">All good!</p>
      <p style="color:var(--text-muted);">No inventory alerts at this time. All products are well stocked.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useProductStore } from '@/stores/productStore';
import { formatCurrency } from '@/utils/formatters';

const store = useProductStore();
const { items: products, loading } = storeToRefs(store);

onMounted(() => store.fetchAll());

const outOfStock  = computed(() => products.value.filter(p => p.stock === 0));
const lowStock    = computed(() => products.value.filter(p => p.stock > 0 && p.stock <= 10));
const wellStocked = computed(() => products.value.filter(p => p.stock > 10));
</script>
