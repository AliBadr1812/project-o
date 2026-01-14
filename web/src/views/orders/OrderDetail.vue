<template>
  <div class="max-w-6xl mx-auto gap-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-2xl font-bold">Order #{{ order.orderNumber }}</h1>
        <p class="text-gray-600">Placed on {{ formatDate(order.createdAt) }}</p>
      </div>

      <div class="flex items-center gap-3">
        <!-- Status Update -->
        <select
          v-model="order.status"
          @change="updateStatus"
          class="border border-gray-300 rounded-lg px-4 py-2"
        >
          <option value="pending">Pending</option>
          <option value="processing">Processing</option>
          <option value="shipped">Shipped</option>
          <option value="delivered">Delivered</option>
          <option value="cancelled">Cancelled</option>
        </select>

        <!-- Actions -->
        <button
          @click="printInvoice"
          class="flex items-center gap-2 border border-gray-300 rounded-lg px-4 py-2 hover:bg-gray-50"
        >
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M5 4v3H4a2 2 0 00-2 2v3a2 2 0 002 2h1v2a2 2 0 002 2h6a2 2 0 002-2v-2h1a2 2 0 002-2V9a2 2 0 00-2-2h-1V4a2 2 0 00-2-2H7a2 2 0 00-2 2zm8 0H7v3h6V4zm0 8H7v4h6v-4z" clip-rule="evenodd"/>
          </svg>
          <span>Print Invoice</span>
        </button>

        <button
          @click="sendEmail"
          class="flex items-center gap-2 border border-gray-300 rounded-lg px-4 py-2 hover:bg-gray-50"
        >
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"/>
            <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"/>
          </svg>
          <span>Send Email</span>
        </button>
      </div>
    </div>

    <!-- Order Status Timeline -->
    <Card>
      <h2 class="text-lg font-semibold mb-4">Order Status</h2>
      <div class="flex items-center justify-between">
        <div class="flex flex-col items-center">
          <div class="w-8 h-8 rounded-full bg-green-500 flex items-center justify-center mb-2">
            <svg class="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"/>
            </svg>
          </div>
          <span class="text-sm font-medium">Order Placed</span>
          <span class="text-xs text-gray-500">{{ formatDate(order.createdAt) }}</span>
        </div>

        <div class="h-1 flex-1 bg-gray-200 mx-4"></div>

        <div class="flex flex-col items-center">
          <div :class="[
            'w-8 h-8 rounded-full flex items-center justify-center mb-2',
            order.status !== 'pending' ? 'bg-green-500' : 'bg-gray-200'
          ]">
            <svg v-if="order.status !== 'pending'" class="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"/>
            </svg>
            <span v-else class="text-sm text-gray-500">2</span>
          </div>
          <span class="text-sm font-medium">Processing</span>
          <span v-if="order.processedAt" class="text-xs text-gray-500">{{ formatDate(order.processedAt) }}</span>
        </div>

        <div class="h-1 flex-1 bg-gray-200 mx-4"></div>

        <div class="flex flex-col items-center">
          <div :class="[
            'w-8 h-8 rounded-full flex items-center justify-center mb-2',
            order.status === 'shipped' || order.status === 'delivered' ? 'bg-green-500' : 'bg-gray-200'
          ]">
            <svg v-if="order.status === 'shipped' || order.status === 'delivered'" class="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"/>
            </svg>
            <span v-else class="text-sm text-gray-500">3</span>
          </div>
          <span class="text-sm font-medium">Shipped</span>
          <span v-if="order.shippedAt" class="text-xs text-gray-500">{{ formatDate(order.shippedAt) }}</span>
        </div>

        <div class="h-1 flex-1 bg-gray-200 mx-4"></div>

        <div class="flex flex-col items-center">
          <div :class="[
            'w-8 h-8 rounded-full flex items-center justify-center mb-2',
            order.status === 'delivered' ? 'bg-green-500' : 'bg-gray-200'
          ]">
            <svg v-if="order.status === 'delivered'" class="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"/>
            </svg>
            <span v-else class="text-sm text-gray-500">4</span>
          </div>
          <span class="text-sm font-medium">Delivered</span>
          <span v-if="order.deliveredAt" class="text-xs text-gray-500">{{ formatDate(order.deliveredAt) }}</span>
        </div>
      </div>
    </Card>

    <!-- Order Details Grid -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Order Items -->
      <Card class="lg:col-span-2">
        <h2 class="text-lg font-semibold mb-4">Order Items</h2>
        <div class="gap-4">
          <div v-for="item in order.items" :key="item.id" class="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
            <div class="flex items-center gap-4">
              <img
                :src="item.imageUrl || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                :alt="item.name"
                class="w-16 h-16 rounded-lg object-cover"
              >
              <div>
                <p class="font-medium">{{ item.name }}</p>
                <p class="text-sm text-gray-500">SKU: {{ item.sku }}</p>
                <p class="text-sm text-gray-500">Quantity: {{ item.quantity }}</p>
              </div>
            </div>
            <div class="text-right">
              <p class="font-medium">{{ formatCurrency(item.price) }}</p>
              <p class="text-sm text-gray-500">{{ formatCurrency(item.price * item.quantity) }} total</p>
            </div>
          </div>
        </div>
      </Card>

      <!-- Order Summary -->
      <div class="gap-6">
        <!-- Customer Info -->
        <Card>
          <h2 class="text-lg font-semibold mb-4">Customer</h2>
          <div class="gap-3">
            <div class="flex items-center gap-3">
              <div class="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
                <span class="text-sm font-medium">{{ getInitials(order.customerName) }}</span>
              </div>
              <div>
                <p class="font-medium">{{ order.customerName }}</p>
                <p class="text-sm text-gray-500">{{ order.customerEmail }}</p>
              </div>
            </div>

            <div class="pt-3 border-t border-gray-200">
              <p class="text-sm text-gray-600 mb-2">Contact Information</p>
              <p class="text-sm">{{ order.customerPhone }}</p>
            </div>

            <button
              @click="viewCustomer"
              class="w-full text-center text-blue-600 hover:text-blue-800 text-sm font-medium py-2"
            >
              View Customer Profile →
            </button>
          </div>
        </Card>

        <!-- Shipping Info -->
        <Card>
          <h2 class="text-lg font-semibold mb-4">Shipping Address</h2>
          <div class="gap-2 text-sm">
            <p>{{ order.shippingAddress.name }}</p>
            <p>{{ order.shippingAddress.street }}</p>
            <p>{{ order.shippingAddress.city }}, {{ order.shippingAddress.state }} {{ order.shippingAddress.zipCode }}</p>
            <p>{{ order.shippingAddress.country }}</p>
            <p class="pt-2">
              <span class="font-medium">Phone:</span> {{ order.shippingAddress.phone }}
            </p>
          </div>
        </Card>

        <!-- Payment Info -->
        <Card>
          <h2 class="text-lg font-semibold mb-4">Payment Information</h2>
          <div class="gap-3">
            <div class="flex justify-between">
              <span class="text-gray-600">Subtotal</span>
              <span>{{ formatCurrency(order.subtotal) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Shipping</span>
              <span>{{ formatCurrency(order.shippingCost) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Tax</span>
              <span>{{ formatCurrency(order.tax) }}</span>
            </div>
            <div class="pt-3 border-t border-gray-200 flex justify-between font-medium">
              <span>Total</span>
              <span>{{ formatCurrency(order.total) }}</span>
            </div>

            <div class="pt-3 border-t border-gray-200">
              <p class="text-sm text-gray-600 mb-1">Payment Method</p>
              <div class="flex items-center gap-2">
                <div class="w-8 h-8 bg-gray-100 rounded flex items-center justify-center">
                  <svg class="w-5 h-5 text-gray-600" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M4 4a2 2 0 00-2 2v1h16V6a2 2 0 00-2-2H4z"/>
                    <path fill-rule="evenodd" d="M18 9H2v5a2 2 0 002 2h12a2 2 0 002-2V9zM4 13a1 1 0 011-1h1a1 1 0 110 2H5a1 1 0 01-1-1zm5-1a1 1 0 100 2h1a1 1 0 100-2H9z" clip-rule="evenodd"/>
                  </svg>
                </div>
                <span>{{ order.paymentMethod }}</span>
                <Badge variant="success">Paid</Badge>
              </div>
            </div>
          </div>
        </Card>
      </div>
    </div>

    <!-- Order Notes -->
    <Card>
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-lg font-semibold">Order Notes</h2>
        <button
          @click="addNote"
          class="text-blue-600 hover:text-blue-800 text-sm font-medium"
        >
          + Add Note
        </button>
      </div>

      <div v-if="order.notes.length > 0" class="gap-4">
        <div
          v-for="note in order.notes"
          :key="note.id"
          class="p-4 bg-gray-50 rounded-lg"
        >
          <div class="flex items-center justify-between mb-2">
            <div class="flex items-center gap-2">
              <div class="w-6 h-6 bg-gray-200 rounded-full"></div>
              <span class="text-sm font-medium">{{ note.author }}</span>
            </div>
            <span class="text-xs text-gray-500">{{ formatDate(note.createdAt) }}</span>
          </div>
          <p class="text-sm">{{ note.content }}</p>
        </div>
      </div>

      <div v-else class="text-center py-8 text-gray-500">
        <p>No notes added to this order yet.</p>
      </div>
    </Card>
  </div>
</template>
<script setup lang="ts">
import Card from '@/components/ui/Card.vue';
import { formatCurrency, formatDate, getInitials } from '@/utils/formatters';
</script>
