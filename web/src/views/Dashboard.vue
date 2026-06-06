<template>
  <div class="grid grid-cols-1 gap-6">
    <!-- Header -->
    <div class="flex items-center justify-between mb-1">
      <div>
        <h1 class="text-2xl font-bold text-gray-300">Welcome back, {{ recentOrders[0]?.customerName }}</h1>
        <span class="text-l text-gray-300"> Here's what's happening with your store today.</span>
      </div>
      <div class="flex items-center gap-3">
        <select class="appearance-none text-[var(--color-icon)] border border-[var(--color-border)] rounded-lg px-4 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 cursor-pointer bg-[var(--color-card)]">
          <option>Today</option>
          <option>Last 7 days</option>
          <option>Last 30 days</option>
          <option>Last Quarter</option>
        </select>
        <button @click="refreshData" class="flex items-center gap-2 border border-[var(--color-border)] rounded-lg px-4 py-2 text-[var(--color-icon)] hover:bg-[var(--color-hover)] transition-colors duration-150">
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z" clip-rule="evenodd"/>
          </svg>
          Refresh
        </button>
      </div>
    </div>

    <!-- Stats Overview -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <!-- Total Revenue -->
      <Card class="hover:bg-[var(--color-hover)] transition-all duration-200 hover:translate-y-[-2px]">
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="p-2.5 bg-blue-500/10 rounded-lg">
              <svg class="w-6 h-6 text-blue-500" fill="currentColor" viewBox="0 0 20 20">
                <path d="M8.433 7.418c.155-.103.346-.196.567-.267v1.698a2.305 2.305 0 01-.567-.267C8.07 8.34 8 8.114 8 8c0-.114.07-.34.433-.582zM11 12.849v-1.698c.22.071.412.164.567.267.364.243.433.468.433.582 0 .114-.07.34-.433.582a2.305 2.305 0 01-.567.267z"/>
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-13a1 1 0 10-2 0v.092a4.535 4.535 0 00-1.676.662C6.602 6.234 6 7.009 6 8c0 .99.602 1.765 1.324 2.246.48.32 1.054.545 1.676.662v1.941c-.391-.127-.68-.317-.843-.504a1 1 0 10-1.51 1.31c.562.649 1.413 1.076 2.353 1.253V15a1 1 0 102 0v-.092a4.535 4.535 0 001.676-.662C13.398 13.766 14 12.991 14 12c0-.99-.602-1.765-1.324-2.246A4.535 4.535 0 0011 9.092V7.151c.391.127.68.317.843.504a1 1 0 101.511-1.31c-.563-.649-1.413-1.076-2.354-1.253V5z" clip-rule="evenodd"/>
              </svg>
            </div>
            <div class="text-sm text-green-500 flex items-center bg-green-500/10 px-2 py-1 rounded-full">
              <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M5.293 9.707a1 1 0 010-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 01-1.414 1.414L11 7.414V15a1 1 0 11-2 0V7.414L6.707 9.707a1 1 0 01-1.414 0z" clip-rule="evenodd"/>
              </svg>
              +12.5%
            </div>
          </div>
          <p class="text-sm font-medium text-[var(--color-icon)] mb-1">Total Revenue</p>
          <p class="text-2xl font-bold text-white mb-2">{{ formatCurrency(stats.totalRevenue) }}</p>
          <p class="text-xs text-[var(--color-icon)]">vs {{ formatCurrency(stats.lastMonthRevenue) }} last month</p>
        </div>
      </Card>

      <!-- Total Orders -->
      <Card class="hover:bg-[var(--color-hover)] transition-all duration-200 hover:translate-y-[-2px]">
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="p-2.5 bg-green-500/10 rounded-lg">
              <svg class="w-6 h-6 text-green-500" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M10 2a4 4 0 00-4 4v1H5a1 1 0 00-.994.89l-1 9A1 1 0 004 18h12a1 1 0 00.994-1.11l-1-9A1 1 0 0015 7h-1V6a4 4 0 00-4-4zm2 5V6a2 2 0 10-4 0v1h4zm-6 3a1 1 0 112 0 1 1 0 01-2 0zm7-1a1 1 0 100 2 1 1 0 000-2z" clip-rule="evenodd"/>
              </svg>
            </div>
            <div class="text-sm text-green-500 flex items-center bg-green-500/10 px-2 py-1 rounded-full">
              <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M5.293 9.707a1 1 0 010-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 01-1.414 1.414L11 7.414V15a1 1 0 11-2 0V7.414L6.707 9.707a1 1 0 01-1.414 0z" clip-rule="evenodd"/>
              </svg>
              +8.2%
            </div>
          </div>
          <p class="text-sm font-medium text-[var(--color-icon)] mb-1">Total Orders</p>
          <p class="text-2xl font-bold text-white mb-2">{{ stats.totalOrders }}</p>
          <p class="text-xs text-[var(--color-icon)]">{{ stats.avgOrderValue }} avg order value</p>
        </div>
      </Card>

      <!-- Active Customers -->
      <Card class="hover:bg-[var(--color-hover)] transition-all duration-200 hover:translate-y-[-2px]">
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="p-2.5 bg-purple-500/10 rounded-lg">
              <svg class="w-6 h-6 text-purple-500" fill="currentColor" viewBox="0 0 20 20">
                <path d="M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3zM6 8a2 2 0 11-4 0 2 2 0 014 0zM16 18v-3a5.972 5.972 0 00-.75-2.906A3.005 3.005 0 0119 15v3h-3zM4.75 12.094A5.973 5.973 0 004 15v3H1v-3a3 3 0 013.75-2.906z"/>
              </svg>
            </div>
            <div class="text-sm text-green-500 flex items-center bg-green-500/10 px-2 py-1 rounded-full">
              <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M5.293 9.707a1 1 0 010-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 01-1.414 1.414L11 7.414V15a1 1 0 11-2 0V7.414L6.707 9.707a1 1 0 01-1.414 0z" clip-rule="evenodd"/>
              </svg>
              +15.3%
            </div>
          </div>
          <p class="text-sm font-medium text-[var(--color-icon)] mb-1">Active Customers</p>
          <p class="text-2xl font-bold text-white mb-2">{{ stats.totalCustomers }}</p>
          <p class="text-xs text-[var(--color-icon)]">{{ stats.newCustomers }} new this month</p>
        </div>
      </Card>

      <!-- Conversion Rate -->
      <Card class="hover:bg-[var(--color-hover)] transition-all duration-200 hover:translate-y-[-2px]">
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="p-2.5 bg-yellow-500/10 rounded-lg">
              <svg class="w-6 h-6 text-yellow-500" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M12 7a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0V8.414l-4.293 4.293a1 1 0 01-1.414 0L8 10.414l-4.293 4.293a1 1 0 01-1.414-1.414l5-5a1 1 0 011.414 0L11 10.586 14.586 7H12z" clip-rule="evenodd"/>
              </svg>
            </div>
            <div class="text-sm text-red-500 flex items-center bg-red-500/10 px-2 py-1 rounded-full">
              <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M14.707 10.293a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 111.414-1.414L9 12.586V5a1 1 0 012 0v7.586l2.293-2.293a1 1 0 011.414 0z" clip-rule="evenodd"/>
              </svg>
              -2.1%
            </div>
          </div>
          <p class="text-sm font-medium text-[var(--color-icon)] mb-1">Conversion Rate</p>
          <p class="text-2xl font-bold text-white mb-2">{{ stats.conversionRate }}%</p>
          <p class="text-xs text-[var(--color-icon)]">From {{ stats.totalVisitors }} visitors</p>
        </div>
      </Card>
    </div>

    <!-- Charts Section -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Revenue Chart -->
      <Card class="lg:col-span-2">
        <div class="border-b border-[var(--color-border)] px-6 py-4">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-lg font-semibold text-white">Revenue Overview</h2>
              <p class="text-sm text-[var(--color-icon)]">Total revenue and growth over time</p>
            </div>
            <div class="flex items-center gap-2">
              <button @click="setChartRange('week')" :class="['px-3 py-1.5 text-sm rounded-lg transition-colors', chartRange === 'week' ? 'bg-blue-500 text-white' : 'text-[var(--color-icon)] hover:bg-[var(--color-hover)]']">
                Week
              </button>
              <button @click="setChartRange('month')" :class="['px-3 py-1.5 text-sm rounded-lg transition-colors', chartRange === 'month' ? 'bg-blue-500 text-white' : 'text-[var(--color-icon)] hover:bg-[var(--color-hover)]']">
                Month
              </button>
              <button @click="setChartRange('quarter')" :class="['px-3 py-1.5 text-sm rounded-lg transition-colors', chartRange === 'quarter' ? 'bg-blue-500 text-white' : 'text-[var(--color-icon)] hover:bg-[var(--color-hover)]']">
                Quarter
              </button>
            </div>
          </div>
        </div>
        <div class="p-6">
          <div class="h-72">
            <canvas id="revenueChart"></canvas>
          </div>
        </div>
      </Card>

      <!-- Top Products -->
      <Card>
        <div class="border-b border-[var(--color-border)] px-6 py-4">
          <h2 class="text-lg font-semibold text-white">Top Products</h2>
          <p class="text-sm text-[var(--color-icon)]">Best selling products this month</p>
        </div>
        <div class="p-4">
          <div class="space-y-4">
            <div v-for="product in topProducts" :key="product.id" class="flex items-center justify-between p-3 hover:bg-[var(--color-hover)] rounded-lg transition-colors duration-150">
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 bg-gray-700 rounded-lg flex items-center justify-center">
                  <svg class="w-5 h-5 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M4 3a2 2 0 100 4h12a2 2 0 100-4H4z"/>
                    <path fill-rule="evenodd" d="M3 8h14v7a2 2 0 01-2 2H5a2 2 0 01-2-2V8zm5 3a1 1 0 011-1h2a1 1 0 110 2H9a1 1 0 01-1-1z" clip-rule="evenodd"/>
                  </svg>
                </div>
                <div>
                  <p class="font-medium text-white text-sm">{{ product.name }}</p>
                  <p class="text-xs text-[var(--color-icon)]">{{ product.category }}</p>
                </div>
              </div>
              <div class="text-right">
                <p class="font-medium text-white text-sm">{{ formatCurrency(product.revenue) }}</p>
                <p class="text-xs text-[var(--color-icon)]">{{ product.sales }} sold</p>
              </div>
            </div>
          </div>
          <router-link to="/products" class="block mt-4 text-center text-[var(--color-icon)] hover:text-blue-500 text-sm font-medium transition-colors">
            View all products →
          </router-link>
        </div>
      </Card>
    </div>

    <!-- Recent Activity Section -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Recent Orders -->
      <Card class="lg:col-span-2">
        <div class="border-b border-[var(--color-border)] px-6 py-4">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-lg font-semibold text-white">Recent Orders</h2>
              <p class="text-sm text-[var(--color-icon)]">Latest customer orders</p>
            </div>
            <router-link to="/orders" class="text-sm text-blue-500 hover:text-blue-400 transition-colors duration-150">
              View all
            </router-link>
          </div>
        </div>
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead>
              <tr class="text-left text-sm text-[var(--color-icon)] border-b border-[var(--color-border)]">
                <th class="py-3 px-6 font-medium">Order ID</th>
                <th class="py-3 px-6 font-medium">Customer</th>
                <th class="py-3 px-6 font-medium">Date</th>
                <th class="py-3 px-6 font-medium">Amount</th>
                <th class="py-3 px-6 font-medium">Status</th>
                <th class="py-3 px-6 font-medium">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in recentOrders" :key="order.id" class="border-b border-[var(--color-border)] last:border-b-0 hover:bg-[var(--color-hover)] transition-colors duration-150">
                <td class="py-3 px-6">
                  <a href="#" @click.prevent="goToOrder(order.id)" class="font-medium text-blue-500 hover:text-blue-400 transition-colors">
                    #{{ formatOrderNumber(order.id) }}
                  </a>
                </td>
                <td class="py-3 px-6">
                  <div class="flex items-center gap-3">
                    <div class="w-8 h-8 rounded-full flex items-center justify-center text-white text-xs font-medium" :style="{ background: `linear-gradient(135deg, ${stringToColor(order.customerName)}, ${stringToColor(order.customerName + '2')})` }">
                      {{ getInitials(order.customerName) }}
                    </div>
                    <span class="text-white">{{ order.customerName }}</span>
                  </div>
                </td>
                <td class="py-3 px-6 text-[var(--color-icon)] text-sm">{{ formatDate(order.date) }}</td>
                <td class="py-3 px-6 font-medium text-white">{{ formatCurrency(order.amount) }}</td>
                <td class="py-3 px-6">
                  <Badge :variant="getStatusVariant(order.status)" class="px-3 py-1">
                    {{ order.status }}
                  </Badge>
                </td>
                <td class="py-3 px-6">
                  <div class="flex items-center gap-2">
                    <button @click="viewOrder(order.id)" class="p-1.5 text-[var(--color-icon)] hover:text-blue-500 hover:bg-blue-500/10 rounded-lg transition-colors" title="View">
                      <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                        <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                      </svg>
                    </button>
                    <button @click="processOrder(order.id)" v-if="order.status === 'pending'" class="p-1.5 text-[var(--color-icon)] hover:text-green-500 hover:bg-green-500/10 rounded-lg transition-colors" title="Process">
                      <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"/>
                      </svg>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </Card>

      <!-- Quick Actions & Activity -->
      <div class="grid grid-cols-1 gap-6">
        <!-- Quick Actions -->
        <Card>
          <div class="border-b border-[var(--color-border)] px-6 py-4">
            <h2 class="text-lg font-semibold text-white">Quick Actions</h2>
          </div>
          <div class="p-4 grid grid-cols-2 gap-3">
            <button @click="goToPage('/products/create')" class="flex flex-col items-center justify-center p-4 rounded-lg hover:bg-[var(--color-hover)] transition-colors duration-150 group">
              <div class="p-2.5 bg-blue-500/10 rounded-lg mb-2 group-hover:bg-blue-500/20">
                <svg class="w-5 h-5 text-blue-500" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M10 3a1 1 0 00-1 1v5H4a1 1 0 100 2h5v5a1 1 0 102 0v-5h5a1 1 0 100-2h-5V4a1 1 0 00-1-1z" clip-rule="evenodd"/>
                </svg>
              </div>
              <span class="text-sm text-white">Add Product</span>
            </button>

            <button @click="goToPage('/orders')" class="flex flex-col items-center justify-center p-4 rounded-lg hover:bg-[var(--color-hover)] transition-colors duration-150 group">
              <div class="p-2.5 bg-green-500/10 rounded-lg mb-2 group-hover:bg-green-500/20">
                <svg class="w-5 h-5 text-green-500" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M10 2a4 4 0 00-4 4v1H5a1 1 0 00-.994.89l-1 9A1 1 0 004 18h12a1 1 0 00.994-1.11l-1-9A1 1 0 0015 7h-1V6a4 4 0 00-4-4zm2 5V6a2 2 0 10-4 0v1h4zm-6 3a1 1 0 112 0 1 1 0 01-2 0zm7-1a1 1 0 100 2 1 1 0 000-2z" clip-rule="evenodd"/>
                </svg>
              </div>
              <span class="text-sm text-white">View Orders</span>
            </button>

            <button @click="goToPage('/customers')" class="flex flex-col items-center justify-center p-4 rounded-lg hover:bg-[var(--color-hover)] transition-colors duration-150 group">
              <div class="p-2.5 bg-purple-500/10 rounded-lg mb-2 group-hover:bg-purple-500/20">
                <svg class="w-5 h-5 text-purple-500" fill="currentColor" viewBox="0 0 20 20">
                  <path d="M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3zM6 8a2 2 0 11-4 0 2 2 0 014 0zM16 18v-3a5.972 5.972 0 00-.75-2.906A3.005 3.005 0 0119 15v3h-3zM4.75 12.094A5.973 5.973 0 004 15v3H1v-3a3 3 0 013.75-2.906z"/>
                </svg>
              </div>
              <span class="text-sm text-white">Customers</span>
            </button>

            <button @click="goToPage('/analytics')" class="flex flex-col items-center justify-center p-4 rounded-lg hover:bg-[var(--color-hover)] transition-colors duration-150 group">
              <div class="p-2.5 bg-yellow-500/10 rounded-lg mb-2 group-hover:bg-yellow-500/20">
                <svg class="w-5 h-5 text-yellow-500" fill="currentColor" viewBox="0 0 20 20">
                  <path d="M2 11a1 1 0 011-1h2a1 1 0 011 1v5a1 1 0 01-1 1H3a1 1 0 01-1-1v-5zM8 7a1 1 0 011-1h2a1 1 0 011 1v9a1 1 0 01-1 1H9a1 1 0 01-1-1V7zM14 4a1 1 0 011-1h2a1 1 0 011 1v12a1 1 0 01-1 1h-2a1 1 0 01-1-1V4z"/>
                </svg>
              </div>
              <span class="text-sm text-white">Analytics</span>
            </button>
          </div>
        </Card>

        <!-- Store Performance -->
        <Card>
          <div class="border-b border-[var(--color-border)] px-6 py-4">
            <h2 class="text-lg font-semibold text-white">Store Performance</h2>
          </div>
          <div class="grid grid-cols-1 gap-4 p-4">
            <div>
              <div class="flex justify-between text-sm mb-1">
                <span class="text-[var(--color-icon)]">Order Completion</span>
                <span class="text-white font-medium">{{ stats.completionRate }}%</span>
              </div>
              <div class="h-2 bg-gray-800 rounded-full overflow-hidden">
                <div class="h-full bg-green-500 rounded-full transition-all duration-500" :style="{ width: stats.completionRate + '%' }"></div>
              </div>
            </div>
            <div>
              <div class="flex justify-between text-sm mb-1">
                <span class="text-[var(--color-icon)]">Customer Satisfaction</span>
                <span class="text-white font-medium">{{ stats.satisfactionRate }}%</span>
              </div>
              <div class="h-2 bg-gray-800 rounded-full overflow-hidden">
                <div class="h-full bg-blue-500 rounded-full transition-all duration-500" :style="{ width: stats.satisfactionRate + '%' }"></div>
              </div>
            </div>
            <div>
              <div class="flex justify-between text-sm mb-1">
                <span class="text-[var(--color-icon)]">Inventory Health</span>
                <span class="text-white font-medium">{{ stats.inventoryHealth }}%</span>
              </div>
              <div class="h-2 bg-gray-800 rounded-full overflow-hidden">
                <div class="h-full bg-purple-500 rounded-full transition-all duration-500" :style="{ width: stats.inventoryHealth + '%' }"></div>
              </div>
            </div>
          </div>
        </Card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatCurrency, formatOrderNumber, getInitials } from '@/utils/formatters';
import { Chart, registerables } from 'chart.js';
import { useRouter } from 'vue-router';

const router = useRouter();
Chart.register(...registerables);

// State
const chartRange = ref<'week' | 'month' | 'quarter'>('month');

// Enhanced stats data
const stats = ref({
  totalRevenue: 25489.67,
  lastMonthRevenue: 22652.34,
  totalOrders: 342,
  avgOrderValue: 74.53,
  totalCustomers: 2432,
  newCustomers: 128,
  conversionRate: 3.2,
  totalVisitors: 76025,
  completionRate: 89,
  satisfactionRate: 94,
  inventoryHealth: 76
});

// Top products
const topProducts = ref([
  { id: 1, name: 'Wireless Earbuds Pro', category: 'Electronics', revenue: 2450.75, sales: 89 },
  { id: 2, name: 'Ergonomic Office Chair', category: 'Furniture', revenue: 1899.99, sales: 23 },
  { id: 3, name: 'Organic Coffee Beans', category: 'Food & Drink', revenue: 1245.50, sales: 178 },
  { id: 4, name: 'Fitness Tracker Watch', category: 'Wearables', revenue: 1120.25, sales: 56 }
]);

// Recent orders with more data
const recentOrders = ref([
  { id: '7842', customerName: 'Alex Johnson', date: '2024-01-15', amount: 124.99, status: 'completed' },
  { id: '7841', customerName: 'Maria Garcia', date: '2024-01-15', amount: 89.50, status: 'completed' },
  { id: '7840', customerName: 'David Chen', date: '2024-01-14', amount: 245.75, status: 'processing' },
  { id: '7839', customerName: 'Sarah Williams', date: '2024-01-14', amount: 67.25, status: 'completed' },
  { id: '7838', customerName: 'James Wilson', date: '2024-01-13', amount: 189.99, status: 'pending' },
  { id: '7837', customerName: 'Lisa Anderson', date: '2024-01-13', amount: 320.50, status: 'shipped' }
]);

// Chart data based on range
const chartData = computed(() => {
  const ranges = {
    week: {
      labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      data: [3200, 4200, 3800, 4500, 5200, 6100, 5800]
    },
    month: {
      labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
      data: [12500, 14200, 13800, 15400]
    },
    quarter: {
      labels: ['Jan', 'Feb', 'Mar'],
      data: [28500, 32400, 34800]
    }
  };

  const range = ranges[chartRange.value];

  return {
    labels: range.labels,
    datasets: [{
      label: 'Revenue',
      data: range.data,
      borderColor: '#3B82F6',
      backgroundColor: 'rgba(59, 130, 246, 0.1)',
      fill: true,
      tension: 0.4,
      borderWidth: 2,
      pointBackgroundColor: '#3B82F6',
      pointBorderColor: '#ffffff',
      pointBorderWidth: 2,
      pointRadius: 3,
      pointHoverRadius: 5
    }]
  };
});

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: false
    },
    tooltip: {
      backgroundColor: 'rgba(17, 24, 39, 0.95)',
      titleColor: '#ffffff',
      bodyColor: '#D1D5DB',
      borderColor: '#374151',
      borderWidth: 1,
      cornerRadius: 6,
      displayColors: false,
      callbacks: {
        label: function(context: any) {
          return `$${context.parsed.y.toLocaleString()}`;
        }
      }
    }
  },
  scales: {
    y: {
      beginAtZero: true,
      grid: {
        color: 'rgba(255, 255, 255, 0.05)'
      },
      ticks: {
        color: 'rgba(255, 255, 255, 0.6)',
        callback: function(value: any) {
          return '$' + value.toLocaleString();
        }
      }
    },
    x: {
      grid: {
        color: 'rgba(255, 255, 255, 0.05)'
      },
      ticks: {
        color: 'rgba(255, 255, 255, 0.6)'
      }
    }
  }
};

let chart: Chart | null = null;

// Helper functions
const stringToColor = (str: string) => {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  const hue = hash % 360;
  return `hsl(${hue}, 70%, 50%)`;
};

const getStatusVariant = (status: string) => {
  switch (status) {
    case 'completed': return 'success';
    case 'processing': return 'info';
    case 'shipped': return 'primary';
    case 'pending': return 'warning';
    default: return 'secondary';
  }
};

const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
};

// Methods
const setChartRange = (range: 'week' | 'month' | 'quarter') => {
  chartRange.value = range;
  updateChart();
};

const updateChart = () => {
  if (chart) {
    chart.data = chartData.value;
    chart.update();
  }
};

const refreshData = () => {
  window.location.reload();
  console.log('Refreshing dashboard data...');
};

const goToOrder = (orderId: string) => {
  router.push(`/orders/${orderId}`);
};

const goToPage = (path: string) => {
  router.push(path);
};

const viewOrder = (orderId: string) => {
  goToOrder(orderId);
};

const processOrder = (orderId: string) => {
  const order = recentOrders.value.find(o => o.id === orderId);
  if (order && order.status === 'pending') {
    order.status = 'processing';
  }
};

// Initialize chart
onMounted(() => {
  const ctx = document.getElementById('revenueChart') as HTMLCanvasElement;
  if (ctx) {
    chart = new Chart(ctx, {
      type: 'line',
      data: chartData.value,
      options: chartOptions
    });
  }
});

// Watch for chart range changes
watch(chartRange, updateChart);
</script>

<style scoped>
/* Custom scrollbar for tables */
.overflow-x-auto {
  scrollbar-width: thin;
  scrollbar-color: var(--color-border) transparent;
}

.overflow-x-auto::-webkit-scrollbar {
  height: 6px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  background: transparent;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background-color: var(--color-border);
  border-radius: 3px;
}

/* Smooth transitions */
button, a {
  transition: all 0.15s ease-out;
}

button:focus {
  outline: 2px solid var(--primary-color);
  outline-offset: 2px;
}

/* Card hover effects */
.hover\:translate-y-\[-2px\] {
  transition: transform 0.2s ease, background-color 0.2s ease;
}

/* Progress bar animation */
.h-2 > div {
  transition: width 0.5s ease-in-out;
}

/* Badge styling */
.badge {
  font-size: 0.75rem;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  display: inline-flex;
  align-items: center;
}

/* Avatar gradient effect */
.w-8.h-8 {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.w-8.h-8:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

/* Table row hover */
tbody tr:hover {
  background-color: var(--color-hover);
}

/* Quick action buttons */
.flex-col.items-center.justify-center {
  cursor: pointer;
}

.flex-col.items-center.justify-center:hover {
  transform: translateY(-2px);
}

/* Chart canvas smoothing */
canvas {
  image-rendering: -webkit-optimize-contrast;
  image-rendering: crisp-edges;
}
</style>
