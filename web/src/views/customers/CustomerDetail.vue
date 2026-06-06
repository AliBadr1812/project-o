<template>
  <div class="max-w-6xl mx-auto gap-6">
    <!-- Header -->
    <div class="flex items-center justify-between pb-5">
      <div class="flex items-center gap-4">
        <img
          :src="customer.avatar || 'https://freesvg.org/img/abstract-user-flat-4.png'"
          :alt="customer.name"
          class="w-16 h-16 rounded-full border-2 border-[var(--color-border)]"
        >
        <div>
          <h1 class="text-2xl font-bold text-white">{{ customer.name }}</h1>
          <p class="text-[var(--color-icon)]">{{ customer.email }} • {{ customer.phone }}</p>
        </div>
      </div>

      <div class="flex items-center gap-3">
        <button
          @click="sendMessage"
          class="flex items-center gap-2 border border-[var(--color-border)] rounded-lg px-4 py-2 text-[var(--color-icon)] hover:bg-[var(--color-hover)] transition-colors duration-150"
        >
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"/>
            <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"/>
          </svg>
          <span>Send Message</span>
        </button>

        <button
          @click="editCustomer"
          class="flex items-center gap-2 bg-blue-600 text-white rounded-lg px-4 py-2 hover:bg-blue-700 transition-colors duration-150"
        >
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
          </svg>
          <span>Edit Profile</span>
        </button>
      </div>
    </div>

    <!-- Customer Stats -->
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
      <Card class="p-4 hover:bg-[var(--color-hover)] transition-colors duration-200">
        <p class="text-sm text-[var(--color-icon)] font-medium">Total Orders</p>
        <p class="text-2xl font-bold text-white mt-2">{{ customer.stats.totalOrders }}</p>
      </Card>

      <Card class="p-4 hover:bg-[var(--color-hover)] transition-colors duration-200">
        <p class="text-sm text-[var(--color-icon)] font-medium">Total Spent</p>
        <p class="text-2xl font-bold text-white mt-2">{{ formatCurrency(customer.stats.totalSpent) }}</p>
      </Card>

      <Card class="p-4 hover:bg-[var(--color-hover)] transition-colors duration-200">
        <p class="text-sm text-[var(--color-icon)] font-medium">Average Order</p>
        <p class="text-2xl font-bold text-white mt-2">{{ formatCurrency(customer.stats.averageOrderValue) }}</p>
      </Card>

      <Card class="p-4 hover:bg-[var(--color-hover)] transition-colors duration-200">
        <p class="text-sm text-[var(--color-icon)] font-medium">Last Order</p>
        <p class="text-lg font-bold text-white mt-2">{{ formatDate(customer.stats.lastOrderDate) }}</p>
      </Card>
    </div>

    <!-- Main Content -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Customer Details -->
      <div class="lg:col-span-2 gap-6">
        <!-- Recent Orders -->
        <Card>
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold text-white">Recent Orders</h2>
            <router-link
              :to="`/customers/${customer.id}/orders`"
              class="text-sm text-blue-500 hover:text-blue-400 transition-colors duration-150"
            >
              View all orders →
            </router-link>
          </div>

          <div class="gap-3">
            <div
              v-for="order in customer.recentOrders"
              :key="order.id"
              class="flex items-center justify-between p-3 border border-[var(--color-border)] rounded-lg hover:bg-[var(--color-hover)] transition-colors duration-150"
            >
              <div>
                <p class="font-medium text-white">Order #{{ order.orderNumber }}</p>
                <p class="text-sm text-[var(--color-icon)]">{{ formatDate(order.createdAt) }}</p>
              </div>
              <div class="text-right">
                <p class="font-medium text-white">{{ formatCurrency(order.total) }}</p>
                <Badge :variant="getStatusVariant(order.status)" class="mt-1">
                  {{ order.status }}
                </Badge>
              </div>
            </div>
          </div>
        </Card>

        <!-- Customer Notes -->
        <Card>
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold text-white">Customer Notes</h2>
            <button
              @click="addNote"
              class="text-blue-500 hover:text-blue-400 text-sm font-medium transition-colors duration-150"
            >
              + Add Note
            </button>
          </div>

          <div v-if="customer.notes.length > 0" class="gap-4">
            <div
              v-for="note in customer.notes"
              :key="note.id"
              class="p-4 bg-[var(--color-hover)] rounded-lg"
            >
              <div class="flex items-center justify-between mb-2">
                <div class="flex items-center gap-2">
                  <div
                    class="w-6 h-6 rounded-full flex items-center justify-center text-white text-xs font-medium"
                    :style="{
                      background: `linear-gradient(135deg, ${stringToColor(note.author)}, ${stringToColor(note.author + '2')})`
                    }"
                  >
                    {{ getInitials(note.author) }}
                  </div>
                  <span class="text-sm font-medium text-white">{{ note.author }}</span>
                </div>
                <span class="text-xs text-[var(--color-icon)]">{{ formatDate(note.createdAt) }}</span>
              </div>
              <p class="text-sm text-[var(--color-icon)]">{{ note.content }}</p>
            </div>
          </div>

          <div v-else class="text-center py-8 text-[var(--color-icon)]">
            <p>No notes added for this customer yet.</p>
          </div>
        </Card>
      </div>

      <!-- Sidebar -->
      <div class="gap-6">
        <!-- Customer Information -->
        <Card>
          <h2 class="text-lg font-semibold mb-4 text-white">Customer Information</h2>

          <div class="gap-4">
            <div>
              <p class="text-sm text-[var(--color-icon)]">Customer Since</p>
              <p class="font-medium text-white">{{ formatDate(customer.createdAt) }}</p>
            </div>

            <div>
              <p class="text-sm text-[var(--color-icon)]">Account Status</p>
              <Badge :variant="customer.status === 'active' ? 'success' : 'secondary'" class="mt-1">
                {{ customer.status }}
              </Badge>
            </div>

            <div>
              <p class="text-sm text-[var(--color-icon)]">Customer Type</p>
              <p class="font-medium text-white">{{ customer.type }}</p>
            </div>

            <div>
              <p class="text-sm text-[var(--color-icon)]">Email Verified</p>
              <div class="flex items-center mt-1">
                <div
                  class="w-3 h-3 rounded-full mr-2 animate-pulse"
                  :class="customer.emailVerified ? 'bg-green-500' : 'bg-red-500'"
                ></div>
                <span class="text-white">{{ customer.emailVerified ? 'Verified' : 'Not Verified' }}</span>
              </div>
            </div>

            <div>
              <p class="text-sm text-[var(--color-icon)]">Preferred Language</p>
              <p class="font-medium text-white">{{ customer.preferredLanguage }}</p>
            </div>
          </div>
        </Card>

        <!-- Shipping Addresses -->
        <Card>
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold text-white">Shipping Addresses</h2>
            <button
              @click="addAddress"
              class="text-blue-500 hover:text-blue-400 text-sm font-medium transition-colors duration-150"
            >
              + Add
            </button>
          </div>

          <div class="gap-3">
            <div
              v-for="address in customer.addresses"
              :key="address.id"
              class="p-3 border border-[var(--color-border)] rounded-lg hover:bg-[var(--color-hover)] transition-colors duration-150"
            >
              <div class="flex items-start justify-between">
                <div>
                  <p class="font-medium text-white">{{ address.name }}</p>
                  <p class="text-sm text-[var(--color-icon)]">{{ address.street }}</p>
                  <p class="text-sm text-[var(--color-icon)]">{{ address.city }}, {{ address.state }} {{ address.zipCode }}</p>
                  <p class="text-sm text-[var(--color-icon)]">{{ address.country }}</p>
                </div>
                <Badge v-if="address.isDefault" variant="primary">Default</Badge>
              </div>
              <div class="mt-3 flex gap-2">
                <button
                  @click="editAddress(address.id)"
                  class="text-sm text-blue-500 hover:text-blue-400 transition-colors duration-150"
                >
                  Edit
                </button>
                <button
                  @click="deleteAddress(address.id)"
                  class="text-sm text-red-500 hover:text-red-400 transition-colors duration-150"
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
            <h2 class="text-lg font-semibold text-white">Tags</h2>
            <button
              @click="addTag"
              class="text-blue-500 hover:text-blue-400 text-sm font-medium transition-colors duration-150"
            >
              + Add Tag
            </button>
          </div>

          <div class="flex flex-wrap gap-2">
            <Badge
              v-for="tag in customer.tags"
              :key="tag"
              variant="secondary"
              class="flex items-center hover:opacity-80 transition-opacity duration-150"
            >
              {{ tag }}
              <button
                @click="removeTag(tag)"
                class="ml-1 text-[var(--color-icon)] hover:text-white transition-colors duration-150"
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
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatCurrency, formatDate, getInitials } from '@/utils/formatters';

const route = useRoute();
const router = useRouter();
const customerId = route.params.id;

// Mock customer data
const customer = ref({
  id: customerId,
  name: 'John Smith',
  email: 'john.smith@example.com',
  phone: '+1 (555) 123-4567',
  avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=John',
  status: 'active',
  type: 'Premium',
  emailVerified: true,
  preferredLanguage: 'English',
  createdAt: '2023-06-15T10:30:00Z',
  stats: {
    totalOrders: 24,
    totalSpent: 2899.99,
    averageOrderValue: 120.83,
    lastOrderDate: '2024-01-15T14:30:00Z'
  },
  recentOrders: [
    { id: 1, orderNumber: '001245', createdAt: '2024-01-15T14:30:00Z', total: 149.99, status: 'pending' },
    { id: 2, orderNumber: '001244', createdAt: '2024-01-10T11:20:00Z', total: 89.99, status: 'delivered' },
    { id: 3, orderNumber: '001239', createdAt: '2024-01-05T09:15:00Z', total: 199.99, status: 'shipped' }
  ],
  notes: [
    { id: 1, author: 'Support Team', createdAt: '2024-01-10T14:30:00Z', content: 'Customer reported an issue with shipping. Followed up and resolved.' },
    { id: 2, author: 'Sales Team', createdAt: '2023-12-15T11:20:00Z', content: 'Customer showed interest in bulk purchase. Sent quote.' }
  ],
  addresses: [
    { id: 1, name: 'Home', street: '123 Main St', city: 'New York', state: 'NY', zipCode: '10001', country: 'USA', isDefault: true },
    { id: 2, name: 'Office', street: '456 Business Ave', city: 'New York', state: 'NY', zipCode: '10002', country: 'USA', isDefault: false }
  ],
  tags: ['VIP', 'Premium', 'Tech', 'Early Adopter']
});

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
    case 'pending': return 'warning';
    case 'processing': return 'info';
    case 'shipped': return 'primary';
    case 'delivered': return 'success';
    case 'cancelled':
    case 'refunded': return 'danger';
    default: return 'secondary';
  }
};

// Methods
const sendMessage = () => {
  alert(`Sending message to ${customer.value.name}`);
};

const editCustomer = () => {
  router.push(`/customers/edit/${customer.value.id}`);
};

const addNote = () => {
  const content = prompt('Enter note content:');
  if (content) {
    customer.value.notes.unshift({
      id: Date.now(),
      author: 'You',
      createdAt: new Date().toISOString(),
      content
    });
  }
};

const addAddress = () => {
  alert('Adding new address...');
};

const editAddress = (addressId: number) => {
  alert(`Editing address ${addressId}`);
};

const deleteAddress = (addressId: number) => {
  if (confirm('Are you sure you want to delete this address?')) {
    customer.value.addresses = customer.value.addresses.filter(addr => addr.id !== addressId);
  }
};

const addTag = () => {
  const tag = prompt('Enter tag name:');
  if (tag && !customer.value.tags.includes(tag)) {
    customer.value.tags.push(tag);
  }
};

const removeTag = (tag: string) => {
  customer.value.tags = customer.value.tags.filter(t => t !== tag);
};
</script>

<style scoped>
/* Smooth transitions for interactive elements */
button, a {
  transition: all 0.15s ease-out;
}

button:focus {
  outline: 2px solid var(--primary-color);
  outline-offset: 2px;
}

/* Avatar hover effect */
img.w-16.h-16 {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

img.w-16.h-16:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

/* Status indicator animation */
.animate-pulse {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

/* Card hover effects */
.hover\:bg-\[var\(--color-hover\)\] {
  cursor: pointer;
}

/* Address card hover */
.hover\:bg-\[var\(--color-hover\)\]:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

/* Badge transitions */
.badge-transition {
  transition: all 0.2s ease;
}
</style>
