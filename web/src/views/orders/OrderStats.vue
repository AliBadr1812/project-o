<template>
  <div class="gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Order Analytics</h1>
        <p class="page-subtitle">Track order performance and trends</p>
      </div>
      <div class="flex items-center gap-3">
        <div class="flex gap-1">
          <button v-for="period in timePeriods" :key="period.value" @click="selectedPeriod = period.value"
            :class="selectedPeriod === period.value ? 'btn-accent' : 'btn-glass'" class="text-sm">
            {{ period.label }}
          </button>
        </div>
        <button @click="exportData" class="btn-glass text-sm">
          <i class="fas fa-download text-xs mr-1"></i>Export
        </button>
      </div>
    </div>

    <!-- Key Metrics -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-p"><i class="fas fa-dollar-sign"></i></div>
            <span class="badge badge-success"><i class="fas fa-arrow-up text-[9px] mr-1"></i>{{ stats.revenueChange }}%</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Total Revenue</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ formatCurrency(stats.totalRevenue) }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">From previous period</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-g"><i class="fas fa-bag-shopping"></i></div>
            <span class="badge badge-info">Per order</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Avg. Order Value</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ formatCurrency(stats.averageOrderValue) }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Average per transaction</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-b"><i class="fas fa-chart-line"></i></div>
            <span class="badge badge-success"><i class="fas fa-arrow-up text-[9px] mr-1"></i>{{ stats.conversionChange }}%</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Conversion Rate</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.conversionRate }}%</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">vs. previous period</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-o"><i class="fas fa-users"></i></div>
            <span class="badge badge-warning">{{ stats.returningRate }}% of total</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Returning Customers</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.returningCustomers }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Repeat buyers this period</p>
        </div>
      </Card>
    </div>

    <!-- Charts Grid -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <Card>
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Revenue Over Time</h2>
        </div>
        <div class="p-5">
          <div class="h-64 flex items-center justify-center rounded-xl" style="border: 2px dashed var(--glass-border);">
            <p class="text-sm" style="color: var(--text-muted);">Revenue chart placeholder</p>
          </div>
        </div>
      </Card>
      <Card>
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Orders by Status</h2>
        </div>
        <div class="p-5">
          <div class="h-64 flex items-center justify-center rounded-xl" style="border: 2px dashed var(--glass-border);">
            <p class="text-sm" style="color: var(--text-muted);">Status chart placeholder</p>
          </div>
        </div>
      </Card>
    </div>

    <!-- Top Products Table -->
    <Card class="overflow-hidden">
      <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
        <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Top Selling Products</h2>
        <p class="text-xs mt-0.5" style="color: var(--text-muted);">Best performers this period</p>
      </div>
      <div class="overflow-x-auto">
        <table class="glass-table w-full">
          <thead>
            <tr>
              <th>Product</th>
              <th>Units Sold</th>
              <th>Revenue</th>
              <th>Conversion</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in topProducts" :key="product.id">
              <td>
                <div class="flex items-center gap-3">
                  <img :src="product.imageUrl || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                    :alt="product.name" class="w-10 h-10 rounded-lg object-cover">
                  <div>
                    <p class="td-primary">{{ product.name }}</p>
                    <p class="text-xs" style="color: var(--text-muted);">{{ product.category }}</p>
                  </div>
                </div>
              </td>
              <td>
                <div class="flex items-center gap-3">
                  <span class="font-medium text-sm" style="color: var(--text-primary);">{{ product.unitsSold }}</span>
                  <div class="progress-track w-24">
                    <div class="progress-fill" :style="{ width: Math.min((product.unitsSold / maxUnits) * 100, 100) + '%', background: 'var(--progress-primary)' }"></div>
                  </div>
                </div>
              </td>
              <td class="td-accent">{{ formatCurrency(product.revenue) }}</td>
              <td><span class="badge badge-success">{{ product.conversionRate }}%</span></td>
            </tr>
          </tbody>
        </table>
      </div>
    </Card>
  </div>
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
