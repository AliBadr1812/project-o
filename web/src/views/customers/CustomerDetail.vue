<template>
  <div class="max-w-6xl mx-auto gap-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-4">
        <img
          :src="customer.avatar || 'https://freesvg.org/img/abstract-user-flat-4.png'"
          :alt="customer.name"
          class="w-16 h-16 rounded-full"
        >
        <div>
          <h1 class="text-2xl font-bold">{{ customer.name }}</h1>
          <p class="text-gray-600">{{ customer.email }} • {{ customer.phone }}</p>
        </div>
      </div>

      <div class="flex items-center gap-3">
        <button
          @click="sendMessage"
          class="flex items-center gap-2 border border-gray-300 rounded-lg px-4 py-2 hover:bg-gray-50"
        >
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"/>
            <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"/>
          </svg>
          <span>Send Message</span>
        </button>

        <button
          @click="editCustomer"
          class="flex items-center gap-2 bg-blue-600 text-white rounded-lg px-4 py-2 hover:bg-blue-700"
        >
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
          </svg>
          <span>Edit Profile</span>
        </button>
      </div>
    </div>

    <!-- Customer Stats -->
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
      <Card class="p-4">
        <p class="text-sm text-gray-600 font-medium">Total Orders</p>
        <p class="text-2xl font-bold mt-2">{{ customer.stats.totalOrders }}</p>
      </Card>

      <Card class="p-4">
        <p class="text-sm text-gray-600 font-medium">Total Spent</p>
        <p class="text-2xl font-bold mt-2">{{ formatCurrency(customer.stats.totalSpent) }}</p>
      </Card>

      <Card class="p-4">
        <p class="text-sm text-gray-600 font-medium">Average Order</p>
        <p class="text-2xl font-bold mt-2">{{ formatCurrency(customer.stats.averageOrderValue) }}</p>
      </Card>

      <Card class="p-4">
        <p class="text-sm text-gray-600 font-medium">Last Order</p>
        <p class="text-lg font-bold mt-2">{{ formatDate(customer.stats.lastOrderDate) }}</p>
      </Card>
    </div>

    <!-- Main Content -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Customer Details -->
      <div class="lg:col-span-2 gap-6">
        <!-- Recent Orders -->
        <Card>
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold">Recent Orders</h2>
            <router-link
              :to="`/customers/${customer.id}/orders`"
              class="text-sm text-blue-600 hover:text-blue-800"
            >
              View all orders →
            </router-link>
          </div>

          <div class="gap-3">
            <div
              v-for="order in customer.recentOrders"
              :key="order.id"
              class="flex items-center justify-between p-3 border border-gray-200 rounded-lg hover:bg-gray-50"
            >
              <div>
                <p class="font-medium">Order #{{ order.orderNumber }}</p>
                <p class="text-sm text-gray-600">{{ formatDate(order.createdAt) }}</p>
              </div>
              <div class="text-right">
                <p class="font-medium">{{ formatCurrency(order.total) }}</p>
                <Badge :variant="getStatusVariant(order.status)">
                  {{ order.status }}
                </Badge>
              </div>
            </div>
          </div>
        </Card>

        <!-- Customer Notes -->
        <Card>
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold">Customer Notes</h2>
            <button
              @click="addNote"
              class="text-blue-600 hover:text-blue-800 text-sm font-medium"
            >
              + Add Note
            </button>
          </div>

          <div v-if="customer.notes.length > 0" class="gap-4">
            <div
              v-for="note in customer.notes"
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
            <p>No notes added for this customer yet.</p>
          </div>
        </Card>
      </div>

      <!-- Sidebar -->
      <div class="gap-6">
        <!-- Customer Information -->
        <Card>
          <h2 class="text-lg font-semibold mb-4">Customer Information</h2>

          <div class="gap-4">
            <div>
              <p class="text-sm text-gray-600">Customer Since</p>
              <p class="font-medium">{{ formatDate(customer.createdAt) }}</p>
            </div>

            <div>
              <p class="text-sm text-gray-600">Account Status</p>
              <Badge :variant="customer.status === 'active' ? 'success' : 'secondary'">
                {{ customer.status }}
              </Badge>
            </div>

            <div>
              <p class="text-sm text-gray-600">Customer Type</p>
              <p class="font-medium">{{ customer.type }}</p>
            </div>

            <div>
              <p class="text-sm text-gray-600">Email Verified</p>
              <div class="flex items-center">
                <div
                  class="w-3 h-3 rounded-full mr-2"
                  :class="customer.emailVerified ? 'bg-green-500' : 'bg-red-500'"
                ></div>
                <span>{{ customer.emailVerified ? 'Verified' : 'Not Verified' }}</span>
              </div>
            </div>

            <div>
              <p class="text-sm text-gray-600">Preferred Language</p>
              <p class="font-medium">{{ customer.preferredLanguage }}</p>
            </div>
          </div>
        </Card>

        <!-- Shipping Addresses -->
        <Card>
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold">Shipping Addresses</h2>
            <button
              @click="addAddress"
              class="text-blue-600 hover:text-blue-800 text-sm font-medium"
            >
              + Add
            </button>
          </div>

          <div class="gap-3">
            <div
              v-for="address in customer.addresses"
              :key="address.id"
              class="p-3 border border-gray-200 rounded-lg"
            >
              <div class="flex items-start justify-between">
                <div>
                  <p class="font-medium">{{ address.name }}</p>
                  <p class="text-sm text-gray-600">{{ address.street }}</p>
                  <p class="text-sm text-gray-600">{{ address.city }}, {{ address.state }} {{ address.zipCode }}</p>
                  <p class="text-sm text-gray-600">{{ address.country }}</p>
                </div>
                <Badge v-if="address.isDefault" variant="primary">Default</Badge>
              </div>
              <div class="mt-3 flex gap-2">
                <button
                  @click="editAddress(address.id)"
                  class="text-sm text-blue-600 hover:text-blue-800"
                >
                  Edit
                </button>
                <button
                  @click="deleteAddress(address.id)"
                  class="text-sm text-red-600 hover:text-red-800"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        </Card>

        <!-- Customer Tags -->
        <Card>
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold">Tags</h2>
            <button
              @click="addTag"
              class="text-blue-600 hover:text-blue-800 text-sm font-medium"
            >
              + Add Tag
            </button>
          </div>

          <div class="flex flex-wrap gap-2">
            <Badge
              v-for="tag in customer.tags"
              :key="tag"
              variant="secondary"
              class="flex items-center"
            >
              {{ tag }}
              <button
                @click="removeTag(tag)"
                class="ml-1 text-gray-500 hover:text-gray-700"
              >
                <svg class="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"/>
                </svg>
              </button>
            </Badge>
          </div>
        </Card>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatCurrency, formatDate } from '@/utils/formatters';
</script>
