<template>
  <Card>
    <h2 class="text-base font-semibold mb-6" style="color:var(--text-primary)">Order Analytics</h2>

    <!-- Time Period Selector -->
    <div class="flex items-center justify-between mb-6">
      <div class="flex gap-1.5">
        <button
          v-for="period in timePeriods"
          :key="period.value"
          @click="selectedPeriod = period.value"
          :class="selectedPeriod === period.value ? 'btn-accent' : 'btn-glass'"
          class="px-3 py-1 text-sm"
        >
          {{ period.label }}
        </button>
      </div>

      <button @click="exportData" class="btn-glass flex items-center gap-2">
        <i class="fas fa-download text-xs"></i>
        <span class="text-sm">Export</span>
      </button>
    </div>

    <!-- Key Metrics -->
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
      <div class="glass-card p-4">
        <div class="flex items-center gap-2 mb-2">
          <div class="stat-icon ni-p"><i class="fas fa-dollar-sign text-xs"></i></div>
          <p class="text-xs font-medium" style="color:var(--ni-purple)">Total Revenue</p>
        </div>
        <p class="text-xl font-bold" style="color:var(--text-primary)">{{ formatCurrency(stats.totalRevenue) }}</p>
        <p class="text-xs mt-1 flex items-center gap-1" style="color:var(--ni-green)">
          <i class="fas fa-arrow-up"></i>
          {{ stats.revenueChange }}% from previous period
        </p>
      </div>

      <div class="glass-card p-4">
        <div class="flex items-center gap-2 mb-2">
          <div class="stat-icon ni-g"><i class="fas fa-bag-shopping text-xs"></i></div>
          <p class="text-xs font-medium" style="color:var(--ni-green)">Avg. Order Value</p>
        </div>
        <p class="text-xl font-bold" style="color:var(--text-primary)">{{ formatCurrency(stats.averageOrderValue) }}</p>
        <p class="text-xs mt-1" style="color:var(--text-muted)">Per order</p>
      </div>

      <div class="glass-card p-4">
        <div class="flex items-center gap-2 mb-2">
          <div class="stat-icon ni-b"><i class="fas fa-chart-line text-xs"></i></div>
          <p class="text-xs font-medium" style="color:var(--ni-blue)">Conversion Rate</p>
        </div>
        <p class="text-xl font-bold" style="color:var(--text-primary)">{{ stats.conversionRate }}%</p>
        <p class="text-xs mt-1 flex items-center gap-1" style="color:var(--ni-green)">
          <i class="fas fa-arrow-up"></i>
          {{ stats.conversionChange }}% change
        </p>
      </div>

      <div class="glass-card p-4">
        <div class="flex items-center gap-2 mb-2">
          <div class="stat-icon ni-o"><i class="fas fa-users text-xs"></i></div>
          <p class="text-xs font-medium" style="color:var(--ni-orange)">Returning Customers</p>
        </div>
        <p class="text-xl font-bold" style="color:var(--text-primary)">{{ stats.returningCustomers }}</p>
        <p class="text-xs mt-1" style="color:var(--text-muted)">{{ stats.returningRate }}% of total</p>
      </div>
    </div>

    <!-- Charts Grid -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Revenue Chart -->
      <div class="glass-card p-6">
        <h3 class="text-sm font-medium mb-4" style="color:var(--text-primary)">Revenue Over Time</h3>
        <div class="h-64 flex items-center justify-center rounded-xl" style="border: 2px dashed var(--glass-border);">
          <p class="text-sm" style="color:var(--text-muted)">Revenue chart</p>
        </div>
      </div>

      <!-- Orders by Status -->
      <div class="glass-card p-6">
        <h3 class="text-sm font-medium mb-4" style="color:var(--text-primary)">Orders by Status</h3>
        <div class="h-64 flex items-center justify-center rounded-xl" style="border: 2px dashed var(--glass-border);">
          <p class="text-sm" style="color:var(--text-muted)">Status chart</p>
        </div>
      </div>

      <!-- Top Products -->
      <div class="glass-card p-6 lg:col-span-2">
        <h3 class="text-sm font-medium mb-4" style="color:var(--text-primary)">Top Selling Products</h3>
        <div class="overflow-x-auto">
          <table class="min-w-full">
            <thead>
              <tr>
                <th class="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide" style="color:var(--table-header-text)">Product</th>
                <th class="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide" style="color:var(--table-header-text)">Units Sold</th>
                <th class="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide" style="color:var(--table-header-text)">Revenue</th>
                <th class="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide" style="color:var(--table-header-text)">Conversion</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="product in topProducts"
                :key="product.id"
                class="transition-colors"
                style="border-top: 1px solid var(--table-border);"
              >
                <td class="px-4 py-3">
                  <div class="flex items-center gap-3">
                    <img
                      :src="product.imageUrl || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                      :alt="product.name"
                      class="w-10 h-10 rounded-lg object-cover"
                    >
                    <div>
                      <p class="font-medium text-sm" style="color:var(--text-primary)">{{ product.name }}</p>
                      <p class="text-xs" style="color:var(--text-muted)">{{ product.category }}</p>
                    </div>
                  </div>
                </td>
                <td class="px-4 py-3">
                  <div class="flex items-center gap-3">
                    <span class="font-medium text-sm" style="color:var(--text-primary)">{{ product.unitsSold }}</span>
                    <div class="progress-track w-24">
                      <div
                        class="progress-fill"
                        :style="{ width: Math.min((product.unitsSold / maxUnits) * 100, 100) + '%', background: 'var(--progress-primary)' }"
                      ></div>
                    </div>
                  </div>
                </td>
                <td class="px-4 py-3 font-medium text-sm" style="color:var(--text-primary)">
                  {{ formatCurrency(product.revenue) }}
                </td>
                <td class="px-4 py-3">
                  <span class="badge badge-success">{{ product.conversionRate }}%</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </Card>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import Card from '@/components/ui/Card.vue';
import { formatCurrency } from '@/utils/formatters';

const selectedPeriod = ref('week');
const timePeriods = [
  { value: 'day', label: 'Day' },
  { value: 'week', label: 'Week' },
  { value: 'month', label: 'Month' },
  { value: 'year', label: 'Year' },
];

const stats = {
  totalRevenue: 48290,
  revenueChange: 12.5,
  averageOrderValue: 124.50,
  conversionRate: 3.2,
  conversionChange: 0.8,
  returningCustomers: 284,
  returningRate: 62,
};

const topProducts = [
  { id: 1, name: 'Wireless Headphones', category: 'Electronics', imageUrl: '', unitsSold: 142, revenue: 21156, conversionRate: 4.2 },
  { id: 2, name: 'Running Shoes Pro', category: 'Footwear', imageUrl: '', unitsSold: 98, revenue: 12250, conversionRate: 3.8 },
  { id: 3, name: 'Smart Watch Series 5', category: 'Electronics', imageUrl: '', unitsSold: 76, revenue: 18924, conversionRate: 5.1 },
  { id: 4, name: 'Coffee Maker Deluxe', category: 'Home', imageUrl: '', unitsSold: 54, revenue: 8100, conversionRate: 2.9 },
];

const maxUnits = computed(() => Math.max(...topProducts.map(p => p.unitsSold), 1));

const exportData = () => { console.log('Exporting data for period:', selectedPeriod.value); };
</script>
