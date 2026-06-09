<template>
  <div class="flex flex-col gap-6">

    <!-- ── Page header ──────────────────────────────────────────────── -->
    <div class="page-header">
      <div>
        <div class="flex items-center gap-2 mb-1">
          <router-link to="/orders" class="text-sm hover:opacity-70 transition-opacity" style="color: var(--text-muted);">
            Orders
          </router-link>
          <i class="fas fa-chevron-right text-[10px]" style="color: var(--text-muted);"></i>
          <span class="text-sm" style="color: var(--text-secondary);">#{{ order?.orderNumber }}</span>
        </div>
        <h1 class="page-title">Edit Order</h1>
        <p class="page-subtitle">Update status, shipping details, or add notes</p>
      </div>
      <div class="flex items-center gap-2">
        <router-link to="/orders" class="btn-glass">Cancel</router-link>
        <button @click="handleSave" :disabled="saving" class="btn-accent">
          <i v-if="saving" class="fas fa-spinner fa-spin text-xs mr-1.5"></i>
          <i v-else class="fas fa-check text-xs mr-1.5"></i>
          {{ saving ? 'Saving…' : 'Save Changes' }}
        </button>
      </div>
    </div>

    <!-- Loading skeleton -->
    <div v-if="loading" class="grid grid-cols-1 lg:grid-cols-[1fr_300px] gap-6">
      <div class="flex flex-col gap-6">
        <div v-for="i in 3" :key="i" class="glass-card h-40 animate-pulse"></div>
      </div>
      <div class="flex flex-col gap-6">
        <div v-for="i in 3" :key="i" class="glass-card h-32 animate-pulse"></div>
      </div>
    </div>

    <!-- Form -->
    <div v-else-if="order" class="grid grid-cols-1 lg:grid-cols-[1fr_300px] gap-6">

      <!-- ── Left column ──────────────────────────────────────────── -->
      <div class="flex flex-col gap-6">

        <!-- Order Items (read-only) -->
        <Card class="overflow-hidden">
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center gap-2.5">
              <div class="stat-icon ni-b" style="width:30px;height:30px;font-size:13px;"><i class="fas fa-box"></i></div>
              <div>
                <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Order Items</h2>
                <p class="text-xs" style="color: var(--text-muted);">{{ order.items.length }} item{{ order.items.length !== 1 ? 's' : '' }} · read-only</p>
              </div>
            </div>
          </div>
          <div class="overflow-x-auto">
            <table class="glass-table w-full">
              <thead>
                <tr>
                  <th>Product</th>
                  <th>SKU</th>
                  <th>Qty</th>
                  <th>Unit Price</th>
                  <th class="text-right">Total</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in order.items" :key="item.id">
                  <td>
                    <div class="flex items-center gap-3">
                      <div class="w-9 h-9 rounded-lg overflow-hidden flex-shrink-0" style="border: 1px solid var(--glass-border);">
                        <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.name" class="w-full h-full object-cover" />
                        <div v-else class="w-full h-full flex items-center justify-center" style="background: var(--glass-bg);">
                          <i class="fas fa-image text-xs" style="color: var(--text-muted);"></i>
                        </div>
                      </div>
                      <span class="td-primary">{{ item.name }}</span>
                    </div>
                  </td>
                  <td><span class="text-xs font-mono" style="color: var(--text-muted);">{{ item.sku }}</span></td>
                  <td><span class="font-medium" style="color: var(--text-primary);">{{ item.quantity }}</span></td>
                  <td><span style="color: var(--text-secondary);">{{ formatCurrency(item.price) }}</span></td>
                  <td class="text-right td-accent">{{ formatCurrency(item.price * item.quantity) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </Card>

        <!-- Shipping Address -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center gap-2.5">
              <div class="stat-icon ni-g" style="width:30px;height:30px;font-size:13px;"><i class="fas fa-location-dot"></i></div>
              <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Shipping Address</h2>
            </div>
          </div>
          <div class="p-5 flex flex-col gap-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="ef-label">Full Name</label>
                <input v-model="form.shippingAddress.name" type="text" class="glass-input w-full" placeholder="Recipient name" />
              </div>
              <div>
                <label class="ef-label">Phone</label>
                <input v-model="form.shippingAddress.phone" type="tel" class="glass-input w-full" placeholder="+1 (555) 000-0000" />
              </div>
            </div>
            <div>
              <label class="ef-label">Street Address</label>
              <input v-model="form.shippingAddress.street" type="text" class="glass-input w-full" placeholder="123 Main St, Apt 4B" />
            </div>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label class="ef-label">City</label>
                <input v-model="form.shippingAddress.city" type="text" class="glass-input w-full" placeholder="New York" />
              </div>
              <div>
                <label class="ef-label">State / Province</label>
                <input v-model="form.shippingAddress.state" type="text" class="glass-input w-full" placeholder="NY" />
              </div>
              <div>
                <label class="ef-label">ZIP / Postal</label>
                <input v-model="form.shippingAddress.zipCode" type="text" class="glass-input w-full" placeholder="10001" />
              </div>
            </div>
            <div>
              <label class="ef-label">Country</label>
              <select v-model="form.shippingAddress.country" class="glass-select w-full">
                <option value="US">United States</option>
                <option value="CA">Canada</option>
                <option value="GB">United Kingdom</option>
                <option value="AU">Australia</option>
                <option value="DE">Germany</option>
                <option value="FR">France</option>
                <option value="NL">Netherlands</option>
                <option value="other">Other</option>
              </select>
            </div>
          </div>
        </Card>

        <!-- Internal Notes -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center gap-2.5">
              <div class="stat-icon ni-p" style="width:30px;height:30px;font-size:13px;"><i class="fas fa-note-sticky"></i></div>
              <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Internal Notes</h2>
            </div>
          </div>
          <div class="p-5 flex flex-col gap-4">
            <!-- Existing notes -->
            <div v-if="order.notes && order.notes.length > 0" class="flex flex-col gap-3">
              <div
                v-for="note in order.notes"
                :key="note.id"
                class="p-3 rounded-xl"
                style="background: rgba(255,255,255,0.35); border: 1px solid var(--glass-border);"
              >
                <div class="flex items-center gap-2 mb-1.5">
                  <div class="w-6 h-6 rounded-full flex items-center justify-center text-[10px] font-bold flex-shrink-0"
                    style="background: linear-gradient(135deg,#b97fff,#7c5ef0); color:#fff;">
                    {{ getInitials(note.author) }}
                  </div>
                  <span class="text-[12px] font-medium" style="color: var(--text-primary);">{{ note.author }}</span>
                  <span class="text-[11px]" style="color: var(--text-muted);">{{ formatDate(note.createdAt) }}</span>
                </div>
                <p class="text-[13px]" style="color: var(--text-secondary);">{{ note.content }}</p>
              </div>
            </div>
            <!-- New note -->
            <div>
              <label class="ef-label">Add a Note</label>
              <textarea
                v-model="newNote"
                rows="3"
                class="glass-input w-full resize-none"
                placeholder="Write an internal note about this order…"
              ></textarea>
              <button
                @click="addNote"
                :disabled="!newNote.trim()"
                class="mt-2 btn-glass text-sm"
                :style="!newNote.trim() ? 'opacity: 0.5; cursor: not-allowed;' : ''"
              >
                <i class="fas fa-plus text-xs mr-1.5"></i>Add Note
              </button>
            </div>
          </div>
        </Card>

      </div>

      <!-- ── Right column ─────────────────────────────────────────── -->
      <div class="flex flex-col gap-6">

        <!-- Status -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <div class="flex items-center gap-2.5">
              <div class="stat-icon ni-o" style="width:30px;height:30px;font-size:13px;"><i class="fas fa-rotate"></i></div>
              <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Order Status</h2>
            </div>
          </div>
          <div class="p-4 flex flex-col gap-2">
            <label
              v-for="s in statusOptions"
              :key="s.value"
              class="flex items-center gap-3 p-3 rounded-xl cursor-pointer transition-all duration-150"
              :style="form.status === s.value
                ? 'background: rgba(124,94,240,0.10); border: 1px solid rgba(124,94,240,0.25);'
                : 'background: transparent; border: 1px solid transparent;'"
            >
              <input type="radio" v-model="form.status" :value="s.value" class="flex-shrink-0" style="accent-color: var(--accent);">
              <div class="flex items-center gap-2.5 flex-1">
                <span
                  class="w-7 h-7 rounded-lg flex items-center justify-center text-xs flex-shrink-0"
                  :style="`background: ${s.color}18; color: ${s.color};`"
                >
                  <i :class="s.icon"></i>
                </span>
                <div>
                  <p class="text-[13px] font-medium" style="color: var(--text-primary);">{{ s.label }}</p>
                  <p class="text-[11px]" style="color: var(--text-muted);">{{ s.description }}</p>
                </div>
              </div>
            </label>
          </div>
        </Card>

        <!-- Customer Info (read-only) -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Customer</h2>
          </div>
          <div class="p-4 flex flex-col gap-3 text-[13px]">
            <div class="flex items-center gap-3">
              <div class="w-10 h-10 rounded-full flex items-center justify-center text-sm font-bold flex-shrink-0"
                :style="`background: linear-gradient(135deg, ${stringToColor(order.customerName)}, ${stringToColor(order.customerName + '2')}); color:#fff;`">
                {{ getInitials(order.customerName) }}
              </div>
              <div>
                <p class="font-semibold" style="color: var(--text-primary);">{{ order.customerName }}</p>
                <p class="text-[12px]" style="color: var(--text-muted);">Customer</p>
              </div>
            </div>
            <div class="pt-2 flex flex-col gap-2" style="border-top: 1px solid var(--glass-border);">
              <div class="flex items-center gap-2" style="color: var(--text-secondary);">
                <i class="fas fa-envelope text-xs w-4 text-center" style="color: var(--text-muted);"></i>
                {{ order.customerEmail }}
              </div>
              <div v-if="order.customerPhone" class="flex items-center gap-2" style="color: var(--text-secondary);">
                <i class="fas fa-phone text-xs w-4 text-center" style="color: var(--text-muted);"></i>
                {{ order.customerPhone }}
              </div>
            </div>
          </div>
        </Card>

        <!-- Financial Summary (read-only) -->
        <Card>
          <div class="px-5 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">Summary</h2>
          </div>
          <div class="p-4 flex flex-col gap-2 text-[13px]">
            <div class="flex justify-between py-1.5" style="border-bottom: 1px solid var(--glass-border);">
              <span style="color: var(--text-secondary);">Subtotal</span>
              <span style="color: var(--text-primary);">{{ formatCurrency(order.subtotal) }}</span>
            </div>
            <div class="flex justify-between py-1.5" style="border-bottom: 1px solid var(--glass-border);">
              <span style="color: var(--text-secondary);">Shipping</span>
              <span style="color: var(--text-primary);">{{ formatCurrency(order.shippingCost) }}</span>
            </div>
            <div class="flex justify-between py-1.5" style="border-bottom: 1px solid var(--glass-border);">
              <span style="color: var(--text-secondary);">Tax</span>
              <span style="color: var(--text-primary);">{{ formatCurrency(order.tax) }}</span>
            </div>
            <div v-if="order.discount && order.discount > 0" class="flex justify-between py-1.5" style="border-bottom: 1px solid var(--glass-border);">
              <span style="color: var(--ni-green);">Discount</span>
              <span style="color: var(--ni-green);">-{{ formatCurrency(order.discount) }}</span>
            </div>
            <div class="flex justify-between pt-2 mt-1">
              <span class="font-semibold" style="color: var(--text-primary);">Total</span>
              <span class="text-[15px] font-bold" style="color: var(--text-accent);">{{ formatCurrency(order.total) }}</span>
            </div>
            <div class="mt-2 pt-2 flex items-center gap-2" style="border-top: 1px solid var(--glass-border);">
              <i class="fas fa-credit-card text-xs" style="color: var(--text-muted);"></i>
              <span class="text-[12px] capitalize" style="color: var(--text-muted);">{{ order.paymentMethod }}</span>
            </div>
          </div>
        </Card>

      </div>
    </div>

    <!-- Not found -->
    <div v-else class="glass-card p-12 text-center">
      <i class="fas fa-box-open text-4xl mb-4" style="color: var(--text-muted);"></i>
      <p class="text-lg font-semibold mb-2" style="color: var(--text-primary);">Order not found</p>
      <p class="text-sm mb-6" style="color: var(--text-muted);">The order you're looking for doesn't exist.</p>
      <router-link to="/orders" class="btn-accent">Back to Orders</router-link>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue';
import { useRoute, useRouter, onBeforeRouteLeave } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import { formatCurrency, formatDate, getInitials } from '@/utils/formatters';
import type { Order, ShippingAddress } from '@/types/order';
import { useConfirm } from '@/composables/useConfirm';

const route  = useRoute();
const router = useRouter();
const { confirm } = useConfirm();

const loading = ref(true);
const saving  = ref(false);
const order   = ref<Order | null>(null);
const newNote = ref('');

const isDirty   = ref(false);
const formSaved = ref(false);
let watchPaused = true;
setTimeout(() => { watchPaused = false; }, 500);

onBeforeRouteLeave(async () => {
  if (isDirty.value && !formSaved.value) {
    const ok = await confirm({
      title:       'Unsaved changes',
      message:     'You have unsaved changes.',
      detail:      'Leave without saving?',
      confirmText: 'Leave',
      variant:     'danger',
    });
    if (!ok) return false;
  }
});

// Editable form state (separate from the read-only order ref)
const form = reactive({
  status: 'pending' as Order['status'],
  shippingAddress: {
    name: '', phone: '', street: '', city: '', state: '', zipCode: '', country: 'US',
  } as ShippingAddress,
});

watch(form, () => { if (!watchPaused) isDirty.value = true; }, { deep: true });

// ── Static helpers ────────────────────────────────────────────────────
const statusOptions = [
  { value: 'pending',    label: 'Pending',    description: 'Waiting to be processed',  icon: 'fas fa-clock',         color: '#f59e0b' },
  { value: 'processing', label: 'Processing', description: 'Being prepared',           icon: 'fas fa-gear',          color: '#3b82f6' },
  { value: 'shipped',    label: 'Shipped',    description: 'On the way to customer',   icon: 'fas fa-truck',         color: '#8b5cf6' },
  { value: 'delivered',  label: 'Delivered',  description: 'Successfully delivered',   icon: 'fas fa-circle-check',  color: '#22c55e' },
  { value: 'cancelled',  label: 'Cancelled',  description: 'Order was cancelled',      icon: 'fas fa-ban',           color: '#ef4444' },
  { value: 'refunded',   label: 'Refunded',   description: 'Payment returned',         icon: 'fas fa-rotate-left',   color: '#f97316' },
];

function stringToColor(str: string) {
  let hash = 0;
  for (let i = 0; i < str.length; i++) hash = str.charCodeAt(i) + ((hash << 5) - hash);
  return `hsl(${hash % 360}, 70%, 50%)`;
}

// ── Mock data ─────────────────────────────────────────────────────────
const mockOrders: Order[] = [
  {
    id: 1, orderNumber: 'ORD-000001',
    status: 'pending',
    createdAt: '2024-01-15T10:30:00Z',
    customerName: 'Alex Johnson', customerEmail: 'alex.j@example.com', customerPhone: '+1 (555) 123-4567',
    items: [
      { id: 1, name: 'Premium Wireless Headphones', sku: 'PHONES-001', price: 149.99, quantity: 1, imageUrl: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=100&h=100&fit=crop' },
      { id: 2, name: 'Bluetooth Speaker',           sku: 'ELECT-012', price: 89.99,  quantity: 2, imageUrl: 'https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=100&h=100&fit=crop' },
    ],
    shippingAddress: { name: 'Alex Johnson', phone: '+1 (555) 123-4567', street: '123 Oak Street', city: 'New York', state: 'NY', zipCode: '10001', country: 'US' },
    subtotal: 329.97, shippingCost: 9.99, tax: 26.40, total: 366.36, discount: 0, paymentMethod: 'credit card',
    notes: [{ id: 1, author: 'Admin', content: 'Customer requested gift wrapping.', createdAt: '2024-01-15T11:00:00Z' }],
  },
  {
    id: 2, orderNumber: 'ORD-000002',
    status: 'processing',
    createdAt: '2024-01-14T14:20:00Z',
    customerName: 'Maria Garcia', customerEmail: 'maria.g@example.com', customerPhone: '+1 (555) 234-5678',
    items: [
      { id: 3, name: 'Organic Cotton T-Shirt', sku: 'CLOTH-045', price: 24.99, quantity: 3 },
    ],
    shippingAddress: { name: 'Maria Garcia', phone: '+1 (555) 234-5678', street: '456 Maple Ave', city: 'Los Angeles', state: 'CA', zipCode: '90001', country: 'US' },
    subtotal: 74.97, shippingCost: 5.99, tax: 6.40, total: 87.36, paymentMethod: 'paypal',
    notes: [],
  },
  {
    id: 3, orderNumber: 'ORD-000003',
    status: 'shipped',
    createdAt: '2024-01-13T09:15:00Z',
    customerName: 'David Chen', customerEmail: 'david.c@example.com',
    items: [
      { id: 4, name: 'Smart Watch', sku: 'ELECT-089', price: 199.99, quantity: 1, imageUrl: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=100&h=100&fit=crop' },
    ],
    shippingAddress: { name: 'David Chen', phone: '+1 (555) 345-6789', street: '789 Pine Road', city: 'Chicago', state: 'IL', zipCode: '60601', country: 'US' },
    subtotal: 199.99, shippingCost: 0, tax: 16.00, total: 215.99, paymentMethod: 'credit card',
    notes: [],
  },
];

// ── Lifecycle ─────────────────────────────────────────────────────────
onMounted(async () => {
  const id = Number(route.params.id);
  await new Promise(r => setTimeout(r, 400)); // simulate fetch

  const found = mockOrders.find(o => o.id === id);
  if (found) {
    order.value = found;
    form.status = found.status;
    Object.assign(form.shippingAddress, { ...found.shippingAddress });
  }
  loading.value = false;
});

// ── Methods ───────────────────────────────────────────────────────────
function addNote() {
  if (!newNote.value.trim() || !order.value) return;
  order.value.notes = order.value.notes ?? [];
  order.value.notes.push({
    id: Date.now(),
    author: 'Admin',
    content: newNote.value.trim(),
    createdAt: new Date().toISOString(),
  });
  newNote.value = '';
}

async function handleSave() {
  if (!order.value) return;
  saving.value = true;
  await new Promise(r => setTimeout(r, 800));

  // Apply edits back to the order object
  order.value.status = form.status;
  Object.assign(order.value.shippingAddress, form.shippingAddress);

  console.log('Order saved:', { id: order.value.id, status: form.status, shippingAddress: form.shippingAddress });
  saving.value = false;
  formSaved.value = true;
  router.push('/orders');
}
</script>

<style scoped>
.ef-label {
  display: block;
  font-size: 12px;
  font-weight: 500;
  color: var(--text-secondary);
  margin-bottom: 6px;
}
</style>
