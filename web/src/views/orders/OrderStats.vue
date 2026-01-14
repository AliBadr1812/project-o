<template>
  <Card>
    <h2 class="text-lg font-semibold mb-6">Order Analytics</h2>

    <!-- Time Period Selector -->
    <div class="flex items-center justify-between mb-6">
      <div class="flex gap-2">
        <button
          v-for="period in timePeriods"
          :key="period.value"
          @click="selectedPeriod = period.value"
          :class="[
            'px-3 py-1 rounded-lg text-sm',
            selectedPeriod === period.value
              ? 'bg-blue-600 text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          ]"
        >
          {{ period.label }}
        </button>
      </div>

      <button
        @click="exportData"
        class="flex items-center gap-2 text-sm text-blue-600 hover:text-blue-800"
      >
        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
          <path fill-rule="evenodd" d="M3 17a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm3.293-7.707a1 1 0 011.414 0L9 10.586V3a1 1 0 112 0v7.586l1.293-1.293a1 1 0 111.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd"/>
        </svg>
        <span>Export Data</span>
      </button>
    </div>

    <!-- Key Metrics -->
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
      <div class="p-4 bg-blue-50 rounded-lg">
        <p class="text-sm text-blue-600 font-medium">Total Revenue</p>
        <p class="text-2xl font-bold">{{ formatCurrency(stats.totalRevenue) }}</p>
        <p class="text-sm text-green-600 flex items-center mt-1">
          <svg class="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M5.293 9.707a1 1 0 010-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 01-1.414 1.414L11 7.414V15a1 1 0 11-2 0V7.414L6.707 9.707a1 1 0 01-1.414 0z" clip-rule="evenodd"/>
          </svg>
          {{ stats.revenueChange }}% from previous period
        </p>
      </div>

      <div class="p-4 bg-green-50 rounded-lg">
        <p class="text-sm text-green-600 font-medium">Average Order Value</p>
        <p class="text-2xl font-bold">{{ formatCurrency(stats.averageOrderValue) }}</p>
        <p class="text-sm text-gray-600 mt-1">Per order</p>
      </div>

      <div class="p-4 bg-purple-50 rounded-lg">
        <p class="text-sm text-purple-600 font-medium">Conversion Rate</p>
        <p class="text-2xl font-bold">{{ stats.conversionRate }}%</p>
        <p class="text-sm text-green-600 flex items-center mt-1">
          <svg class="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M5.293 9.707a1 1 0 010-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 01-1.414 1.414L11 7.414V15a1 1 0 11-2 0V7.414L6.707 9.707a1 1 0 01-1.414 0z" clip-rule="evenodd"/>
          </svg>
          {{ stats.conversionChange }}% change
        </p>
      </div>

      <div class="p-4 bg-yellow-50 rounded-lg">
        <p class="text-sm text-yellow-600 font-medium">Returning Customers</p>
        <p class="text-2xl font-bold">{{ stats.returningCustomers }}</p>
        <p class="text-sm text-gray-600 mt-1">{{ stats.returningRate }}% of total</p>
      </div>
    </div>

    <!-- Charts Grid -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Revenue Chart -->
      <div class="bg-white border border-gray-200 rounded-lg p-6">
        <h3 class="font-medium mb-4">Revenue Over Time</h3>
        <div class="h-64">
          <!-- Chart will go here -->
          <div class="flex items-center justify-center h-full border-2 border-dashed border-gray-200 rounded">
            <p class="text-gray-500">Revenue chart</p>
          </div>
        </div>
      </div>

      <!-- Orders by Status -->
      <div class="bg-white border border-gray-200 rounded-lg p-6">
        <h3 class="font-medium mb-4">Orders by Status</h3>
        <div class="h-64">
          <!-- Chart will go here -->
          <div class="flex items-center justify-center h-full border-2 border-dashed border-gray-200 rounded">
            <p class="text-gray-500">Status chart</p>
          </div>
        </div>
      </div>

      <!-- Top Products -->
      <div class="bg-white border border-gray-200 rounded-lg p-6 lg:col-span-2">
        <h3 class="font-medium mb-4">Top Selling Products</h3>
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead>
              <tr>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Product</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Units Sold</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Revenue</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Conversion</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-200">
              <tr v-for="product in topProducts" :key="product.id">
                <td class="px-4 py-3">
                  <div class="flex items-center gap-3">
                    <img
                      :src="product.imageUrl || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                      :alt="product.name"
                      class="w-10 h-10 rounded-lg object-cover"
                    >
                    <div>
                      <p class="font-medium text-sm">{{ product.name }}</p>
                      <p class="text-xs text-gray-500">{{ product.category }}</p>
                    </div>
                  </div>
                </td>
                <td class="px-4 py-3">
                  <div class="flex items-center">
                    <span class="font-medium">{{ product.unitsSold }}</span>
                    <div class="ml-3 w-32 h-2 bg-gray-200 rounded-full overflow-hidden">
                      <div
                        class="h-full bg-blue-500"
                        :style="{ width: Math.min((product.unitsSold / maxUnits) * 100, 100) + '%' }"
                      ></div>
                    </div>
                  </div>
                </td>
                <td class="px-4 py-3 font-medium">
                  {{ formatCurrency(product.revenue) }}
                </td>
                <td class="px-4 py-3">
                  <span class="text-green-600 font-medium">{{ product.conversionRate }}%</span>
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
import Card from '@/components/ui/Card.vue';
import { formatCurrency } from '@/utils/formatters';
</script>
