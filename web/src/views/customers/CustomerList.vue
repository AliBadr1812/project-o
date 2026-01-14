<template>
  <div class="gap-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-2xl font-bold">Customers</h1>
        <p class="text-gray-600">Manage your customer database</p>
      </div>

      <div class="flex items-center gap-3">
        <!-- Search -->
        <div class="relative">
          <input
            type="search"
            v-model="searchQuery"
            placeholder="Search customers..."
            class="border border-gray-300 rounded-lg pl-10 pr-4 py-2 w-64"
          >
          <svg class="absolute left-3 top-2.5 w-5 h-5 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd"/>
          </svg>
        </div>

        <!-- Filter -->
        <select v-model="customerType" class="border border-gray-300 rounded-lg px-4 py-2">
          <option value="">All Customers</option>
          <option value="new">New Customers</option>
          <option value="returning">Returning Customers</option>
          <option value="vip">VIP Customers</option>
        </select>

        <!-- Import/Export -->
        <button
          @click="showImportModal = true"
          class="flex items-center gap-2 border border-gray-300 rounded-lg px-4 py-2 hover:bg-gray-50"
        >
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M3 17a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm3.293-7.707a1 1 0 011.414 0L9 10.586V3a1 1 0 112 0v7.586l1.293-1.293a1 1 0 111.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd"/>
          </svg>
          <span>Import</span>
        </button>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-4 gap-4">
      <div class="bg-blue-50 p-4 rounded-lg">
        <p class="text-sm text-blue-600 font-medium">Total Customers</p>
        <p class="text-2xl font-bold">{{ stats.total }}</p>
      </div>
      <div class="bg-green-50 p-4 rounded-lg">
        <p class="text-sm text-green-600 font-medium">Active</p>
        <p class="text-2xl font-bold">{{ stats.active }}</p>
      </div>
      <div class="bg-yellow-50 p-4 rounded-lg">
        <p class="text-sm text-yellow-600 font-medium">New This Month</p>
        <p class="text-2xl font-bold">{{ stats.newThisMonth }}</p>
      </div>
      <div class="bg-purple-50 p-4 rounded-lg">
        <p class="text-sm text-purple-600 font-medium">Avg. Order Value</p>
        <p class="text-2xl font-bold">{{ formatCurrency(stats.avgOrderValue) }}</p>
      </div>
    </div>

    <!-- Customers Table -->
    <Card>
      <Table>
        <template #header>
          <tr>
            <th class="py-3 px-4 text-left">Customer</th>
            <th class="py-3 px-4 text-left">Email</th>
            <th class="py-3 px-4 text-left">Orders</th>
            <th class="py-3 px-4 text-left">Total Spent</th>
            <th class="py-3 px-4 text-left">Last Order</th>
            <th class="py-3 px-4 text-left">Status</th>
            <th class="py-3 px-4 text-left">Actions</th>
          </tr>
        </template>

        <template #body>
          <tr v-for="customer in customers" :key="customer.id" class="hover:bg-gray-50">
            <td class="py-3 px-4">
              <div class="flex items-center gap-3">
                <img
                  :src="customer.avatar || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                  :alt="customer.name"
                  class="w-10 h-10 rounded-full"
                >
                <div>
                  <p class="font-medium">{{ customer.name }}</p>
                  <p class="text-sm text-gray-500">{{ customer.phone }}</p>
                </div>
              </div>
            </td>
            <td class="py-3 px-4">
              <p>{{ customer.email }}</p>
            </td>
            <td class="py-3 px-4">
              <div class="flex items-center">
                <span class="font-medium mr-2">{{ customer.orderCount }}</span>
                <div class="w-16 h-2 bg-gray-200 rounded-full overflow-hidden">
                  <div
                    class="h-full bg-blue-500"
                    :style="{ width: Math.min((customer.orderCount / maxOrders) * 100, 100) + '%' }"
                  ></div>
                </div>
              </div>
            </td>
            <td class="py-3 px-4 font-medium">
              {{ formatCurrency(customer.totalSpent) }}
            </td>
            <td class="py-3 px-4">
              <p class="text-sm">{{ formatDate(customer.lastOrderDate) }}</p>
            </td>
            <td class="py-3 px-4">
              <Badge :variant="customer.status === 'active' ? 'success' : 'secondary'">
                {{ customer.status }}
              </Badge>
            </td>
            <td class="py-3 px-4">
              <div class="flex items-center gap-2">
                <button
                  @click="viewCustomer(customer.id)"
                  class="p-1 text-blue-600 hover:text-blue-800"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                    <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                  </svg>
                </button>
                <button
                  @click="editCustomer(customer.id)"
                  class="p-1 text-gray-600 hover:text-gray-800"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
                  </svg>
                </button>
                <button
                  @click="sendEmailToCustomer(customer.email)"
                  class="p-1 text-gray-600 hover:text-gray-800"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"/>
                    <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"/>
                  </svg>
                </button>
              </div>
            </td>
          </tr>
        </template>
      </Table>

      <EmptyState
        v-if="customers.length === 0"
        title="No customers found"
        description="When customers place orders, they will appear here."
        action-text="Import Customers"
        @action-click="showImportModal = true"
      />

      <Pagination
        v-if="customers.length > 0"
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-items="totalItems"
        @page-change="onPageChange"
      />
    </Card>
  </div>
</template>
<script setup lang="ts">
import Card from '@/components/ui/Card.vue';
import Table from '@/components/ui/Table.vue';
import Badge from '@/components/ui/Badge.vue';
import EmptyState from '@/components/shared/EmptyState.vue';
import Pagination from '@/components/ui/Pagination.vue';
import { formatCurrency, formatDate } from '@/utils/formatters';
</script>
