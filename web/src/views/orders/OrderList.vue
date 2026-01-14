<template>
  <div class="gap-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-2xl font-bold">Orders</h1>
        <p class="text-gray-600">Manage customer orders</p>
      </div>

      <div class="flex items-center gap-3">
        <!-- Filter -->
        <select v-model="statusFilter" class="border border-gray-300 rounded-lg px-4 py-2">
          <option value="">All Status</option>
          <option value="pending">Pending</option>
          <option value="processing">Processing</option>
          <option value="shipped">Shipped</option>
          <option value="delivered">Delivered</option>
          <option value="cancelled">Cancelled</option>
        </select>

        <!-- Date Range -->
        <div class="relative">
          <button
            @click="showDatePicker = !showDatePicker"
            class="flex items-center gap-2 border border-gray-300 rounded-lg px-4 py-2"
          >
            <svg class="w-5 h-5 text-gray-500" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd"/>
            </svg>
            <span>{{ dateRangeLabel }}</span>
          </button>
        </div>

        <!-- Export -->
        <button class="flex items-center gap-2 border border-gray-300 rounded-lg px-4 py-2 hover:bg-gray-50">
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M3 17a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm3.293-7.707a1 1 0 011.414 0L9 10.586V3a1 1 0 112 0v7.586l1.293-1.293a1 1 0 111.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd"/>
          </svg>
          <span>Export</span>
        </button>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
      <div class="bg-blue-50 p-4 rounded-lg">
        <p class="text-sm text-blue-600 font-medium">Total Orders</p>
        <p class="text-2xl font-bold">{{ stats.total }}</p>
      </div>
      <div class="bg-yellow-50 p-4 rounded-lg">
        <p class="text-sm text-yellow-600 font-medium">Pending</p>
        <p class="text-2xl font-bold">{{ stats.pending }}</p>
      </div>
      <div class="bg-green-50 p-4 rounded-lg">
        <p class="text-sm text-green-600 font-medium">Completed</p>
        <p class="text-2xl font-bold">{{ stats.completed }}</p>
      </div>
      <div class="bg-red-50 p-4 rounded-lg">
        <p class="text-sm text-red-600 font-medium">Cancelled</p>
        <p class="text-2xl font-bold">{{ stats.cancelled }}</p>
      </div>
    </div>

    <!-- Orders Table -->
    <Card>
      <Table>
        <template #header>
          <tr>
            <th class="py-3 px-4 text-left">Order</th>
            <th class="py-3 px-4 text-left">Customer</th>
            <th class="py-3 px-4 text-left">Date</th>
            <th class="py-3 px-4 text-left">Total</th>
            <th class="py-3 px-4 text-left">Status</th>
            <th class="py-3 px-4 text-left">Actions</th>
          </tr>
        </template>

        <template #body>
          <tr v-for="order in orders" :key="order.id" class="hover:bg-gray-50">
            <td class="py-3 px-4">
              <div>
                <p class="font-medium">#{{ order.orderNumber }}</p>
                <p class="text-sm text-gray-500">{{ order.itemsCount }} items</p>
              </div>
            </td>
            <td class="py-3 px-4">
              <div class="flex items-center gap-3">
                <div class="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center">
                  <span class="text-sm font-medium">{{ getInitials(order.customerName) }}</span>
                </div>
                <div>
                  <p class="font-medium">{{ order.customerName }}</p>
                  <p class="text-sm text-gray-500">{{ order.customerEmail }}</p>
                </div>
              </div>
            </td>
            <td class="py-3 px-4">
              <p>{{ formatDate(order.createdAt) }}</p>
            </td>
            <td class="py-3 px-4 font-medium">
              {{ formatCurrency(order.total) }}
            </td>
            <td class="py-3 px-4">
              <Badge :variant="getStatusVariant(order.status)">
                {{ order.status }}
              </Badge>
            </td>
            <td class="py-3 px-4">
              <div class="flex items-center gap-2">
                <button
                  @click="viewOrder(order.id)"
                  class="p-1 text-blue-600 hover:text-blue-800"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                    <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                  </svg>
                </button>
                <button
                  @click="editOrder(order.id)"
                  class="p-1 text-gray-600 hover:text-gray-800"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
                  </svg>
                </button>
                <button
                  @click="printInvoice(order.id)"
                  class="p-1 text-gray-600 hover:text-gray-800"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M5 4v3H4a2 2 0 00-2 2v3a2 2 0 002 2h1v2a2 2 0 002 2h6a2 2 0 002-2v-2h1a2 2 0 002-2V9a2 2 0 00-2-2h-1V4a2 2 0 00-2-2H7a2 2 0 00-2 2zm8 0H7v3h6V4zm0 8H7v4h6v-4z" clip-rule="evenodd"/>
                  </svg>
                </button>
              </div>
            </td>
          </tr>
        </template>
      </Table>

      <EmptyState
        v-if="orders.length === 0"
        title="No orders found"
        description="When customers place orders, they will appear here."
        action-text="View Documentation"
      />

      <Pagination
        v-if="orders.length > 0"
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
import { formatCurrency, formatDate, getInitials } from '@/utils/formatters';
</script>
