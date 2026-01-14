<template>
  <div class="analytics-dashboard">
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-800">Analytics Dashboard</h1>
      <p class="text-gray-600 mt-1">Track your business performance and metrics</p>
    </div>

    <div class="mb-6">
      <Card>
        <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4 md:gap-0">
          <div class="flex items-center gap-4">
            <Button
              :variant="timeRange === 'today' ? 'primary' : 'outline'"
              size="sm"
              @click="timeRange = 'today'"
            >
              Today
            </Button>
            <Button
              :variant="timeRange === 'week' ? 'primary' : 'outline'"
              size="sm"
              @click="timeRange = 'week'"
            >
              This Week
            </Button>
            <Button
              :variant="timeRange === 'month' ? 'primary' : 'outline'"
              size="sm"
              @click="timeRange = 'month'"
            >
              This Month
            </Button>
            <Button
              :variant="timeRange === 'year' ? 'primary' : 'outline'"
              size="sm"
              @click="timeRange = 'year'"
            >
              This Year
            </Button>
          </div>
          <div class="flex items-center gap-2">
            <input
              type="date"
              v-model="dateRange.start"
              class="border border-gray-300 rounded-lg px-3 py-1 text-sm"
            />
            <span class="text-gray-500">to</span>
            <input
              type="date"
              v-model="dateRange.end"
              class="border border-gray-300 rounded-lg px-3 py-1 text-sm"
            />
            <Button variant="outline" size="sm" @click="applyCustomDate">
              Apply
            </Button>
          </div>
        </div>
      </Card>
    </div>

    <!-- Overview Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
      <Card class="hover:shadow-lg transition-shadow">
        <div class="flex items-center">
          <div class="w-12 h-12 rounded-lg bg-blue-100 flex items-center justify-center mr-4">
            <i class="fas fa-shopping-cart text-blue-600 text-xl"></i>
          </div>
          <div>
            <p class="text-sm text-gray-600">Total Sales</p>
            <p class="text-2xl font-bold text-gray-800">
              {{ formatCurrency(overview.totalSales) }}
            </p>
            <p
              :class="overview.salesGrowth >= 0 ? 'text-green-600' : 'text-red-600'"
              class="text-sm"
            >
              <i
                :class="overview.salesGrowth >= 0 ? 'fas fa-arrow-up' : 'fas fa-arrow-down'"
                class="mr-1"
              ></i>
              {{ Math.abs(overview.salesGrowth) }}% from previous period
            </p>
          </div>
        </div>
      </Card>

      <Card class="hover:shadow-lg transition-shadow">
        <div class="flex items-center">
          <div class="w-12 h-12 rounded-lg bg-green-100 flex items-center justify-center mr-4">
            <i class="fas fa-box text-green-600 text-xl"></i>
          </div>
          <div>
            <p class="text-sm text-gray-600">Orders</p>
            <p class="text-2xl font-bold text-gray-800">
              {{ overview.totalOrders }}
            </p>
            <p class="text-sm text-gray-600">
              {{ overview.avgOrderValue }} avg. value
            </p>
          </div>
        </div>
      </Card>

      <Card class="hover:shadow-lg transition-shadow">
        <div class="flex items-center">
          <div class="w-12 h-12 rounded-lg bg-purple-100 flex items-center justify-center mr-4">
            <i class="fas fa-users text-purple-600 text-xl"></i>
          </div>
          <div>
            <p class="text-sm text-gray-600">Customers</p>
            <p class="text-2xl font-bold text-gray-800">
              {{ overview.totalCustomers }}
            </p>
            <p
              :class="overview.newCustomers >= 0 ? 'text-green-600' : 'text-red-600'"
              class="text-sm"
            >
              +{{ overview.newCustomers }} new
            </p>
          </div>
        </div>
      </Card>

      <Card class="hover:shadow-lg transition-shadow">
        <div class="flex items-center">
          <div class="w-12 h-12 rounded-lg bg-yellow-100 flex items-center justify-center mr-4">
            <i class="fas fa-eye text-yellow-600 text-xl"></i>
          </div>
          <div>
            <p class="text-sm text-gray-600">Conversion Rate</p>
            <p class="text-2xl font-bold text-gray-800">
              {{ overview.conversionRate }}%
            </p>
            <p class="text-sm text-gray-600">
              {{ overview.visitors }} visitors
            </p>
          </div>
        </div>
      </Card>
    </div>

    <!-- Charts and Graphs -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
      <!-- Sales Chart -->
      <Card>
        <div class="flex justify-between items-center mb-6">
          <h3 class="text-lg font-semibold text-gray-800">Sales Trend</h3>
          <select
            v-model="salesChartType"
            class="border border-gray-300 rounded-lg px-3 py-1 text-sm"
          >
            <option value="line">Line Chart</option>
            <option value="bar">Bar Chart</option>
          </select>
        </div>
        <div class="h-64 flex items-center justify-center">
          <!-- Chart would be implemented with Chart.js or similar -->
          <div class="text-center text-gray-500">
            <i class="fas fa-chart-line text-4xl mb-3"></i>
            <p>Sales chart visualization</p>
          </div>
        </div>
      </Card>

      <!-- Revenue by Category -->
      <Card>
        <div class="flex justify-between items-center mb-6">
          <h3 class="text-lg font-semibold text-gray-800">Revenue by Category</h3>
          <Button variant="ghost" size="sm">
            <i class="fas fa-download"></i>
          </Button>
        </div>
        <div class="h-64">
          <div v-if="categoryRevenue.length === 0" class="h-full flex items-center justify-center">
            <div class="text-center text-gray-500">
              <i class="fas fa-chart-pie text-4xl mb-3"></i>
              <p>No category revenue data available</p>
            </div>
          </div>
          <div v-else class="gap-3">
            <div v-for="item in categoryRevenue" :key="item.category" class="gap-1">
              <div class="flex justify-between">
                <span class="text-sm text-gray-700">{{ item.category }}</span>
                <span class="text-sm font-medium">{{ formatCurrency(item.revenue) }}</span>
              </div>
              <div class="h-2 bg-gray-200 rounded-full overflow-hidden">
                <div
                  :style="{ width: item.percentage + '%' }"
                  class="h-full bg-blue-600"
                ></div>
              </div>
            </div>
          </div>
        </div>
      </Card>
    </div>

    <!-- Top Products and Recent Orders -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Top Products -->
      <Card>
        <h3 class="text-lg font-semibold text-gray-800 mb-4">Top Products</h3>
        <Table>
          <thead>
            <tr>
              <th>Product</th>
              <th>Sales</th>
              <th>Revenue</th>
              <th>Stock</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in topProducts" :key="product.id">
              <td>
                <div class="flex items-center">
                  <img
                    v-if="product.image"
                    :src="product.image"
                    :alt="product.name"
                    class="w-8 h-8 rounded mr-3 object-cover"
                  />
                  <span class="font-medium">{{ product.name }}</span>
                </div>
              </td>
              <td>{{ product.sales }}</td>
              <td>{{ formatCurrency(product.revenue) }}</td>
              <td>
                <Badge
                  :variant="product.stock > 10 ? 'success' : product.stock > 0 ? 'warning' : 'danger'"
                >
                  {{ product.stock }} left
                </Badge>
              </td>
            </tr>
          </tbody>
        </Table>
        <div class="mt-4 text-right">
          <Button variant="ghost" size="sm" @click="viewAllProducts">
            View All Products
          </Button>
        </div>
      </Card>

      <!-- Recent Orders -->
      <Card>
        <h3 class="text-lg font-semibold text-gray-800 mb-4">Recent Orders</h3>
        <div class="gap-4">
          <div
            v-for="order in recentOrders"
            :key="order.id"
            class="flex items-center justify-between p-3 hover:bg-gray-50 rounded-lg"
          >
            <div>
              <p class="font-medium">Order #{{ order.id }}</p>
              <p class="text-sm text-gray-600">{{ order.customerName }}</p>
            </div>
            <div class="text-right">
              <p class="font-medium">{{ formatCurrency(order.amount) }}</p>
              <Badge :variant="getOrderStatusVariant(order.status)">
                {{ order.status }}
              </Badge>
            </div>
          </div>
        </div>
        <div class="mt-4 text-right">
          <Button variant="ghost" size="sm" @click="viewAllOrders">
            View All Orders
          </Button>
        </div>
      </Card>
    </div>

    <!-- Geographic Data -->
    <Card class="mt-6">
      <h3 class="text-lg font-semibold text-gray-800 mb-4">Geographic Distribution</h3>
      <div class="h-48 flex items-center justify-center">
        <div class="text-center text-gray-500">
          <i class="fas fa-globe-americas text-4xl mb-3"></i>
          <p>Map visualization of customer locations</p>
        </div>
      </div>
    </Card>
  </div>
</template>
<script setup lang="ts">
import Button from '@/components/ui/Button.vue';
import Card from '@/components/ui/Card.vue';
import Table from '@/components/ui/Table.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatCurrency } from '@/utils/formatters';

</script>
