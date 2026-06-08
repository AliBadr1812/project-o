<template>
  <div class="flex flex-col gap-6">
    <!-- Loading / not found -->
    <div v-if="!customer" class="py-12 text-center">
      <i class="fas fa-spinner fa-spin text-2xl mb-3" style="color: var(--text-muted);"></i>
      <p style="color: var(--text-muted);">Loading customer…</p>
    </div>

    <template v-else>
    <!-- Page Header -->
    <div class="page-header">
      <div class="flex items-center gap-4">
        <img :src="customer.avatar || 'https://freesvg.org/img/abstract-user-flat-4.png'"
          :alt="customer.name" class="w-14 h-14 rounded-full" style="border: 2px solid var(--glass-border);">
        <div>
          <h1 class="page-title">{{ customer.name }}</h1>
          <p class="page-subtitle">{{ customer.email }} &bull; {{ customer.phone }}</p>
        </div>
      </div>
      <div class="flex items-center gap-3">
        <button @click="sendMessage" class="btn-glass text-sm">
          <i class="fas fa-envelope text-xs mr-1"></i>Send Message
        </button>
        <button @click="editCustomer" class="btn-accent text-sm">
          <i class="fas fa-pen text-xs mr-1"></i>Edit Profile
        </button>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-b"><i class="fas fa-bag-shopping"></i></div>
            <span class="badge badge-info">Total</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Total Orders</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ customer.stats.totalOrders }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">All time orders</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-p"><i class="fas fa-dollar-sign"></i></div>
            <span class="badge badge-success">Lifetime</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Total Spent</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ formatCurrency(customer.stats.totalSpent) }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Lifetime value</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-g"><i class="fas fa-chart-bar"></i></div>
            <span class="badge badge-info">Average</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Average Order</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ formatCurrency(customer.stats.averageOrderValue) }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Per order average</p>
        </div>
      </Card>
      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-o"><i class="fas fa-clock"></i></div>
            <span class="badge badge-warning">Recent</span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Last Order</p>
          <p class="text-xl font-bold tracking-tight" style="color: var(--text-primary);">{{ formatDate(customer.stats.lastOrderDate) }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">Most recent activity</p>
        </div>
      </Card>
    </div>

    <!-- Main Content -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Left Column -->
      <div class="lg:col-span-2 flex flex-col gap-4">
        <!-- Recent Orders -->
        <Card>
          <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center justify-between">
              <div>
                <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Recent Orders</h2>
              </div>
              <router-link :to="`/customers/${customer.id}/orders`" class="text-sm font-medium" style="color: var(--text-accent);">
                View all orders →
              </router-link>
            </div>
          </div>
          <div class="p-5 flex flex-col gap-3">
            <div v-for="order in customer.recentOrders" :key="order.id"
              class="flex items-center justify-between p-3 rounded-xl transition-colors"
              style="border: 1px solid var(--glass-border);">
              <div>
                <p class="font-medium text-sm" style="color: var(--text-primary);">Order #{{ order.orderNumber }}</p>
                <p class="text-xs mt-0.5" style="color: var(--text-secondary);">{{ formatDate(order.createdAt) }}</p>
              </div>
              <div class="text-right">
                <p class="font-medium text-sm td-accent">{{ formatCurrency(order.total) }}</p>
                <Badge :variant="getStatusVariant(order.status)" class="mt-1">{{ order.status }}</Badge>
              </div>
            </div>
          </div>
        </Card>

        <!-- Notes -->
        <Card>
          <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center justify-between">
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Customer Notes</h2>
              <button @click="addNote" class="text-sm font-medium" style="color: var(--text-accent);">+ Add Note</button>
            </div>
          </div>
          <div class="p-5">
            <div v-if="customer.notes.length > 0" class="flex flex-col gap-3">
              <div v-for="note in customer.notes" :key="note.id" class="p-4 rounded-xl"
                style="background: var(--glass-bg); border: 1px solid var(--glass-border);">
                <div class="flex items-center justify-between mb-2">
                  <div class="flex items-center gap-2">
                    <div class="w-6 h-6 rounded-full flex items-center justify-center text-xs font-medium"
                      style="color: var(--text-primary);"
                      :style="{ background: `linear-gradient(135deg, ${stringToColor(note.author)}, ${stringToColor(note.author + '2')})` }">
                      {{ getInitials(note.author) }}
                    </div>
                    <span class="text-sm font-medium" style="color: var(--text-primary);">{{ note.author }}</span>
                  </div>
                  <span class="text-xs" style="color: var(--text-muted);">{{ formatDate(note.createdAt) }}</span>
                </div>
                <p class="text-sm" style="color: var(--text-secondary);">{{ note.content }}</p>
              </div>
            </div>
            <div v-else class="text-center py-8">
              <p class="text-sm" style="color: var(--text-muted);">No notes added for this customer yet.</p>
            </div>
          </div>
        </Card>
      </div>

      <!-- Sidebar -->
      <div class="flex flex-col gap-4">
        <!-- Customer Information -->
        <Card>
          <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Customer Information</h2>
          </div>
          <div class="p-5 flex flex-col gap-4 text-sm">
            <div>
              <p class="text-xs mb-0.5" style="color: var(--text-secondary);">Customer Since</p>
              <p style="color: var(--text-primary);">{{ formatDate(customer.createdAt) }}</p>
            </div>
            <div>
              <p class="text-xs mb-1" style="color: var(--text-secondary);">Account Status</p>
              <Badge :variant="customer.status === 'active' ? 'success' : 'secondary'">{{ customer.status }}</Badge>
            </div>
            <div>
              <p class="text-xs mb-0.5" style="color: var(--text-secondary);">Customer Type</p>
              <p style="color: var(--text-primary);">{{ customer.type }}</p>
            </div>
            <div>
              <p class="text-xs mb-1" style="color: var(--text-secondary);">Email Verified</p>
              <div class="flex items-center gap-2">
                <div class="w-2 h-2 rounded-full" :class="customer.emailVerified ? 'bg-green-500' : 'bg-red-500'"></div>
                <span style="color: var(--text-primary);">{{ customer.emailVerified ? 'Verified' : 'Not Verified' }}</span>
              </div>
            </div>
            <div>
              <p class="text-xs mb-0.5" style="color: var(--text-secondary);">Preferred Language</p>
              <p style="color: var(--text-primary);">{{ customer.preferredLanguage }}</p>
            </div>
          </div>
        </Card>

        <!-- Addresses -->
        <Card>
          <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center justify-between">
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Shipping Addresses</h2>
              <button @click="addAddress" class="text-sm font-medium" style="color: var(--text-accent);">+ Add</button>
            </div>
          </div>
          <div class="p-5 flex flex-col gap-3">
            <div v-for="address in customer.addresses" :key="address.id" class="p-3 rounded-xl"
              style="border: 1px solid var(--glass-border);">
              <div class="flex items-start justify-between mb-2">
                <div class="text-sm flex flex-col gap-0.5">
                  <p class="font-medium" style="color: var(--text-primary);">{{ address.name }}</p>
                  <p style="color: var(--text-secondary);">{{ address.street }}</p>
                  <p style="color: var(--text-secondary);">{{ address.city }}, {{ address.state }} {{ address.zipCode }}</p>
                  <p style="color: var(--text-secondary);">{{ address.country }}</p>
                </div>
                <Badge v-if="address.isDefault" variant="primary">Default</Badge>
              </div>
              <div class="flex gap-3 mt-2 pt-2" style="border-top: 1px solid var(--glass-border);">
                <button @click="editAddress(address.id)" class="text-xs font-medium" style="color: var(--text-accent);">Edit</button>
                <button @click="deleteAddress(address.id)" class="text-xs font-medium" style="color: var(--ni-red);">Delete</button>
              </div>
            </div>
          </div>
        </Card>

        <!-- Tags -->
        <Card>
          <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center justify-between">
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Tags</h2>
              <button @click="addTag" class="text-sm font-medium" style="color: var(--text-accent);">+ Add Tag</button>
            </div>
          </div>
          <div class="p-5">
            <div class="flex flex-wrap gap-2">
              <Badge v-for="tag in customer.tags" :key="tag" variant="secondary" class="flex items-center gap-1">
                {{ tag }}
                <button @click="removeTag(tag)" class="ml-0.5 hover:opacity-70">
                  <i class="fas fa-xmark text-[9px]"></i>
                </button>
              </Badge>
            </div>
          </div>
        </Card>
      </div>
    </div>
    </template><!-- end v-else -->
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatCurrency, formatDate, getInitials } from '@/utils/formatters';
import { useCustomerStore } from '@/stores/customerStore';
import { useToast } from '@/composables/useToast';
import { useConfirm } from '@/composables/useConfirm';

const route  = useRoute();
const router = useRouter();
const store  = useCustomerStore();
const toast  = useToast();
const { confirm } = useConfirm();

// Customer data — enriched from store + sensible defaults
const customer = ref<any>(null);

onMounted(async () => {
  await store.fetchAll();
  const c = store.items.find(c => c.id === Number(route.params.id));
  if (c) {
    customer.value = {
      id:    c.id,
      name:  c.fullName,
      email: c.email,
      phone: c.phone ?? '—',
      avatar: null,
      status: c.status,
      type:   c.type ?? 'new',
      emailVerified: true,
      preferredLanguage: 'English',
      createdAt: c.lastOrderDate ?? new Date().toISOString(),
      stats: {
        totalOrders:        c.orderCount,
        totalSpent:         c.totalSpent,
        averageOrderValue:  c.orderCount > 0 ? Math.round(c.totalSpent / c.orderCount * 100) / 100 : 0,
        lastOrderDate:      c.lastOrderDate ?? '—',
      },
      recentOrders: [],
      notes: [],
      addresses: [],
      tags: c.type === 'vip' ? ['VIP'] : c.type === 'returning' ? ['Returning'] : ['New'],
    };
  }
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
  toast.info(`Message composer coming soon for ${customer.value?.name}`, 'Send Message');
};

const editCustomer = () => {
  router.push(`/customers/edit/${customer.value.id}`);
};

const addNote = () => {
  const content = window.prompt('Enter note content:');
  if (content?.trim()) {
    customer.value.notes.unshift({
      id: Date.now(),
      author: 'You',
      createdAt: new Date().toISOString(),
      content: content.trim(),
    });
    toast.success('Note added');
  }
};

const addAddress = () => {
  toast.info('Address management coming soon', 'Add Address');
};

const editAddress = (addressId: number) => {
  toast.info(`Address editor coming soon (ID: ${addressId})`, 'Edit Address');
};

const deleteAddress = async (addressId: number) => {
  const ok = await confirm({
    title:       'Delete address',
    message:     'Remove this address from the customer profile?',
    confirmText: 'Delete',
    variant:     'danger',
  });
  if (ok) {
    customer.value.addresses = customer.value.addresses.filter((addr: any) => addr.id !== addressId);
    toast.success('Address removed');
  }
};

const addTag = () => {
  const tag = window.prompt('Enter tag name:');
  if (tag?.trim() && !customer.value.tags.includes(tag.trim())) {
    customer.value.tags.push(tag.trim());
    toast.success(`Tag "${tag.trim()}" added`);
  }
};

const removeTag = (tag: string) => {
  customer.value.tags = customer.value.tags.filter((t: string) => t !== tag);
  toast.info(`Tag "${tag}" removed`);
};
</script>

