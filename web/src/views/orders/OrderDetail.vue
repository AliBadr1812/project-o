<template>
    <div v-if="!order"></div>
    <div v-else class="flex flex-col gap-6">
        <!-- Page Header -->
        <div class="page-header">
            <div>
                <h1 class="page-title">Order #{{ order.orderNumber }}</h1>
                <p class="page-subtitle">Placed on {{ formatDate(order.createdAt) }}</p>
            </div>
            <div class="flex items-center gap-2 flex-wrap">
                <!-- Context-aware quick actions -->
                <button v-if="order.status === 'pending'"
                    @click="quickStatus('processing')" class="btn-accent text-sm">
                    <i class="fas fa-play text-xs mr-1"></i>Process Order
                </button>
                <button v-if="order.status === 'processing'"
                    @click="openShipDialog" class="btn-accent text-sm">
                    <i class="fas fa-truck text-xs mr-1"></i>Mark Shipped
                </button>
                <button v-if="order.status === 'shipped'"
                    @click="quickStatus('delivered')" class="btn-accent text-sm">
                    <i class="fas fa-circle-check text-xs mr-1"></i>Mark Delivered
                </button>
                <button v-if="order.status === 'delivered'"
                    @click="initiateRefund" class="btn-glass text-sm">
                    <i class="fas fa-rotate-left text-xs mr-1"></i>Refund
                </button>
                <select v-model="order.status" @change="updateStatus" class="glass-select text-sm">
                    <option value="pending">Pending</option>
                    <option value="processing">Processing</option>
                    <option value="shipped">Shipped</option>
                    <option value="delivered">Delivered</option>
                    <option value="cancelled">Cancelled</option>
                </select>
                <button @click="printInvoice" class="btn-glass text-sm">
                    <i class="fas fa-print text-xs mr-1"></i>Print
                </button>
                <button @click="sendEmail" class="btn-glass text-sm">
                    <i class="fas fa-envelope text-xs mr-1"></i>Email
                </button>
            </div>
        </div>

        <!-- Order Status Timeline -->
        <Card>
            <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
                <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Order Status</h2>
            </div>
            <div class="p-5">
                <div class="flex items-center justify-between">
                    <div class="flex flex-col items-center">
                        <div class="w-8 h-8 rounded-full bg-green-500 flex items-center justify-center mb-2">
                            <i class="fas fa-check text-xs" style="color: var(--text-primary);"></i>
                        </div>
                        <span class="text-sm font-medium" style="color: var(--text-primary);">Order Placed</span>
                        <span class="text-xs" style="color: var(--text-muted);">{{ formatDate(order.createdAt) }}</span>
                    </div>
                    <div class="h-px flex-1 mx-4" style="background: var(--glass-border);"></div>
                    <div class="flex flex-col items-center">
                        <div class="w-8 h-8 rounded-full flex items-center justify-center mb-2"
                            :style="order.status !== 'pending' ? 'background: var(--ni-green);' : 'background: var(--glass-border);'">
                            <i v-if="order.status !== 'pending'" class="fas fa-check text-xs" style="color: var(--text-primary);"></i>
                            <span v-else class="text-xs" style="color: var(--text-muted);">2</span>
                        </div>
                        <span class="text-sm font-medium" style="color: var(--text-primary);">Processing</span>
                        <span v-if="order.processedAt" class="text-xs" style="color: var(--text-muted);">{{ formatDate(order.processedAt) }}</span>
                    </div>
                    <div class="h-px flex-1 mx-4" style="background: var(--glass-border);"></div>
                    <div class="flex flex-col items-center">
                        <div class="w-8 h-8 rounded-full flex items-center justify-center mb-2"
                            :style="(order.status === 'shipped' || order.status === 'delivered') ? 'background: var(--ni-green);' : 'background: var(--glass-border);'">
                            <i v-if="order.status === 'shipped' || order.status === 'delivered'" class="fas fa-check text-xs" style="color: var(--text-primary);"></i>
                            <span v-else class="text-xs" style="color: var(--text-muted);">3</span>
                        </div>
                        <span class="text-sm font-medium" style="color: var(--text-primary);">Shipped</span>
                        <span v-if="order.shippedAt" class="text-xs" style="color: var(--text-muted);">{{ formatDate(order.shippedAt) }}</span>
                    </div>
                    <div class="h-px flex-1 mx-4" style="background: var(--glass-border);"></div>
                    <div class="flex flex-col items-center">
                        <div class="w-8 h-8 rounded-full flex items-center justify-center mb-2"
                            :style="order.status === 'delivered' ? 'background: var(--ni-green);' : 'background: var(--glass-border);'">
                            <i v-if="order.status === 'delivered'" class="fas fa-check text-xs" style="color: var(--text-primary);"></i>
                            <span v-else class="text-xs" style="color: var(--text-muted);">4</span>
                        </div>
                        <span class="text-sm font-medium" style="color: var(--text-primary);">Delivered</span>
                        <span v-if="order.deliveredAt" class="text-xs" style="color: var(--text-muted);">{{ formatDate(order.deliveredAt) }}</span>
                    </div>
                </div>
            </div>
        </Card>

        <!-- Order Details Grid -->
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <!-- Order Items -->
            <Card class="lg:col-span-2">
                <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
                    <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Order Items</h2>
                </div>
                <div class="p-5 flex flex-col gap-3">
                    <div v-for="item in order.items" :key="item.id"
                        class="flex items-center justify-between p-4 rounded-xl"
                        style="border: 1px solid var(--glass-border);">
                        <div class="flex items-center gap-4">
                            <img :src="item.imageUrl || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                                :alt="item.name" class="w-14 h-14 rounded-lg object-cover">
                            <div>
                                <p class="font-medium text-sm" style="color: var(--text-primary);">{{ item.name }}</p>
                                <p class="text-xs mt-0.5" style="color: var(--text-muted);">SKU: {{ item.sku }}</p>
                                <p class="text-xs" style="color: var(--text-muted);">Qty: {{ item.quantity }}</p>
                            </div>
                        </div>
                        <div class="text-right">
                            <p class="font-medium text-sm" style="color: var(--text-primary);">{{ formatCurrency(item.price) }}</p>
                            <p class="text-xs mt-0.5" style="color: var(--text-muted);">{{ formatCurrency(item.price * item.quantity) }} total</p>
                        </div>
                    </div>
                </div>
            </Card>

            <!-- Sidebar -->
            <div class="flex flex-col gap-4">
                <!-- Customer Info -->
                <Card>
                    <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
                        <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Customer</h2>
                    </div>
                    <div class="p-5 flex flex-col gap-3">
                        <div class="flex items-center gap-3">
                            <div class="stat-icon ni-s">{{ getInitials(order.customerName) }}</div>
                            <div>
                                <p class="font-medium text-sm" style="color: var(--text-primary);">{{ order.customerName }}</p>
                                <p class="text-xs" style="color: var(--text-muted);">{{ order.customerEmail }}</p>
                            </div>
                        </div>
                        <div class="pt-3" style="border-top: 1px solid var(--glass-border);">
                            <p class="text-xs mb-1" style="color: var(--text-secondary);">Phone</p>
                            <p class="text-sm" style="color: var(--text-primary);">{{ order.customerPhone }}</p>
                        </div>
                        <button @click="viewCustomer" class="text-sm font-medium mt-1" style="color: var(--text-accent);">
                            View Customer Profile →
                        </button>
                    </div>
                </Card>

                <!-- Shipping Info -->
                <Card>
                    <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
                        <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Shipping Address</h2>
                    </div>
                    <div class="p-5 flex flex-col gap-1">
                        <p class="text-sm" style="color: var(--text-primary);">{{ order.shippingAddress.name }}</p>
                        <p class="text-sm" style="color: var(--text-secondary);">{{ order.shippingAddress.street }}</p>
                        <p class="text-sm" style="color: var(--text-secondary);">{{ order.shippingAddress.city }}, {{ order.shippingAddress.state }} {{ order.shippingAddress.zipCode }}</p>
                        <p class="text-sm" style="color: var(--text-secondary);">{{ order.shippingAddress.country }}</p>
                        <p class="text-sm mt-2" style="color: var(--text-secondary);"><span class="font-medium" style="color: var(--text-primary);">Phone:</span> {{ order.shippingAddress.phone }}</p>
                    </div>
                </Card>

                <!-- Payment Info -->
                <Card>
                    <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
                        <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Payment</h2>
                    </div>
                    <div class="p-5 flex flex-col gap-2">
                        <div class="flex justify-between text-sm">
                            <span style="color: var(--text-secondary);">Subtotal</span>
                            <span style="color: var(--text-primary);">{{ formatCurrency(order.subtotal) }}</span>
                        </div>
                        <div class="flex justify-between text-sm">
                            <span style="color: var(--text-secondary);">Shipping</span>
                            <span style="color: var(--text-primary);">{{ formatCurrency(order.shippingCost) }}</span>
                        </div>
                        <div class="flex justify-between text-sm">
                            <span style="color: var(--text-secondary);">Tax</span>
                            <span style="color: var(--text-primary);">{{ formatCurrency(order.tax) }}</span>
                        </div>
                        <div v-if="refundAmount > 0" class="flex justify-between text-sm" style="color: var(--ni-red);">
                            <span>Refunded</span>
                            <span>-{{ formatCurrency(refundAmount) }}</span>
                        </div>
                        <div class="flex justify-between font-semibold pt-2" style="border-top: 1px solid var(--glass-border);">
                            <span style="color: var(--text-primary);">Total</span>
                            <span class="td-accent">{{ formatCurrency(order.total) }}</span>
                        </div>
                        <div class="pt-3 flex items-center gap-2" style="border-top: 1px solid var(--glass-border);">
                            <div class="stat-icon ni-b w-8 h-8 text-xs"><i class="fas fa-credit-card"></i></div>
                            <span class="text-sm" style="color: var(--text-primary);">{{ order.paymentMethod }}</span>
                            <Badge :variant="refundAmount > 0 ? 'warning' : 'success'">
                                {{ refundAmount > 0 ? 'Refunded' : 'Paid' }}
                            </Badge>
                        </div>
                    </div>
                </Card>

                <!-- Tracking Info (shown when shipped) -->
                <Card v-if="trackingInfo.number">
                    <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
                        <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">
                            <i class="fas fa-truck text-xs mr-2" style="color: var(--accent);"></i>Tracking
                        </h2>
                    </div>
                    <div class="p-5 flex flex-col gap-2 text-sm">
                        <div class="flex justify-between">
                            <span style="color: var(--text-secondary);">Carrier</span>
                            <span style="color: var(--text-primary);">{{ trackingInfo.carrier }}</span>
                        </div>
                        <div class="flex justify-between">
                            <span style="color: var(--text-secondary);">Tracking #</span>
                            <span class="font-mono text-xs font-semibold" style="color: var(--accent);">{{ trackingInfo.number }}</span>
                        </div>
                        <div class="flex justify-between">
                            <span style="color: var(--text-secondary);">Est. Delivery</span>
                            <span style="color: var(--text-primary);">{{ trackingInfo.eta }}</span>
                        </div>
                        <a :href="trackingInfo.url" target="_blank"
                            class="mt-1 text-xs font-semibold flex items-center gap-1"
                            style="color: var(--text-accent);">
                            Track shipment <i class="fas fa-arrow-up-right-from-square text-[10px]"></i>
                        </a>
                    </div>
                </Card>
            </div>
        </div>

        <!-- Activity Log -->
        <Card>
            <div class="px-6 py-4 flex items-center justify-between" style="border-bottom: 1px solid var(--glass-border);">
                <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Activity Log</h2>
                <span class="text-xs" style="color: var(--text-muted);">{{ activityLog.length }} events</span>
            </div>
            <div class="p-5">
                <div class="relative flex flex-col gap-0">
                    <!-- Timeline line -->
                    <div class="absolute left-[15px] top-4 bottom-4 w-px"
                        style="background: var(--glass-border);"></div>
                    <div v-for="(event, idx) in activityLog" :key="idx"
                        class="flex items-start gap-4 pb-4 last:pb-0 relative">
                        <div class="w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0 text-xs z-10"
                            :style="{ background: event.iconBg, color: event.iconColor }">
                            <i :class="event.icon"></i>
                        </div>
                        <div class="flex-1 pt-1">
                            <p class="text-[13.5px] font-medium" style="color: var(--text-primary);">{{ event.title }}</p>
                            <p v-if="event.detail" class="text-[12.5px] mt-0.5" style="color: var(--text-secondary);">{{ event.detail }}</p>
                            <p class="text-[11.5px] mt-1" style="color: var(--text-muted);">{{ event.time }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </Card>

        <!-- Order Notes -->
        <Card>
            <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
                <div class="flex items-center justify-between">
                    <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Order Notes</h2>
                    <button @click="addNote" class="text-sm font-medium" style="color: var(--text-accent);">+ Add Note</button>
                </div>
            </div>
            <div class="p-5">
                <div v-if="order.notes && order.notes.length > 0" class="flex flex-col gap-3">
                    <div v-for="note in order.notes" :key="note.id" class="p-4 rounded-xl" style="background: var(--glass-bg); border: 1px solid var(--glass-border);">
                        <div class="flex items-center justify-between mb-2">
                            <div class="flex items-center gap-2">
                                <div class="w-6 h-6 rounded-full" style="background: var(--glass-border);"></div>
                                <span class="text-sm font-medium" style="color: var(--text-primary);">{{ note.author }}</span>
                            </div>
                            <span class="text-xs" style="color: var(--text-muted);">{{ formatDate(note.createdAt) }}</span>
                        </div>
                        <p class="text-sm" style="color: var(--text-secondary);">{{ note.content }}</p>
                    </div>
                </div>
                <div v-else class="text-center py-8">
                    <p class="text-sm" style="color: var(--text-muted);">No notes added to this order yet.</p>
                </div>
            </div>
        </Card>
    </div>

    <!-- Ship Dialog -->
    <Teleport to="body">
      <div v-if="showShipDialog" class="fixed inset-0 z-[9998] flex items-center justify-center"
          style="background: rgba(0,0,0,0.5);" @click.self="showShipDialog = false">
        <div class="glass-card w-full max-w-sm mx-4 p-6 flex flex-col gap-4">
          <div class="flex items-center justify-between">
            <h3 class="text-[15px] font-semibold" style="color: var(--text-primary);">Mark as Shipped</h3>
            <button @click="showShipDialog = false" class="btn-glass-icon w-8 h-8 rounded-xl text-xs">
              <i class="fas fa-xmark"></i>
            </button>
          </div>
          <div class="flex flex-col gap-3">
            <div class="flex flex-col gap-1.5">
              <label class="form-label">Carrier</label>
              <select v-model="shipForm.carrier" class="glass-input text-sm">
                <option>FedEx</option><option>UPS</option><option>USPS</option>
                <option>DHL</option><option>Other</option>
              </select>
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="form-label">Tracking Number</label>
              <input v-model="shipForm.trackingNumber" type="text" class="glass-input text-sm"
                placeholder="TRK-000000-US" />
            </div>
            <div class="flex flex-col gap-1.5">
              <label class="form-label">Estimated Delivery</label>
              <input v-model="shipForm.eta" type="date" class="glass-input text-sm" />
            </div>
          </div>
          <div class="flex gap-2 pt-1">
            <button @click="confirmShip" class="btn-accent flex-1 text-sm">
              <i class="fas fa-truck text-xs mr-1"></i>Mark Shipped
            </button>
            <button @click="showShipDialog = false" class="btn-glass flex-1 text-sm">Cancel</button>
          </div>
        </div>
      </div>
    </Teleport>

    <!-- ═══════════════════════════════════════════════════════════════════
         PRINT-ONLY INVOICE — hidden on screen, visible when window.print()
         ═══════════════════════════════════════════════════════════════════ -->
    <div v-if="order" class="print-invoice" id="print-invoice">
      <div class="pi-header">
        <div>
          <h1 class="pi-logo">ShopAdmin</h1>
          <p class="pi-tagline">Tax Invoice</p>
        </div>
        <div class="pi-header-right">
          <p class="pi-order-num">Order #{{ order.orderNumber }}</p>
          <p>Date: {{ formatDate(order.createdAt) }}</p>
          <p>Status: <strong>{{ order.status.toUpperCase() }}</strong></p>
        </div>
      </div>

      <div class="pi-addresses">
        <div>
          <h3>Bill To</h3>
          <p>{{ order.customerName }}</p>
          <p>{{ order.customerEmail }}</p>
          <p>{{ order.customerPhone }}</p>
        </div>
        <div>
          <h3>Ship To</h3>
          <p>{{ order.shippingAddress.name }}</p>
          <p>{{ order.shippingAddress.street }}</p>
          <p>{{ order.shippingAddress.city }}, {{ order.shippingAddress.state }} {{ order.shippingAddress.zipCode }}</p>
          <p>{{ order.shippingAddress.country }}</p>
        </div>
      </div>

      <table class="pi-table">
        <thead>
          <tr>
            <th>Item</th>
            <th>SKU</th>
            <th class="pi-right">Qty</th>
            <th class="pi-right">Unit Price</th>
            <th class="pi-right">Total</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in order.items" :key="item.id">
            <td>{{ item.name }}</td>
            <td>{{ item.sku }}</td>
            <td class="pi-right">{{ item.quantity }}</td>
            <td class="pi-right">{{ formatCurrency(item.price) }}</td>
            <td class="pi-right">{{ formatCurrency(item.price * item.quantity) }}</td>
          </tr>
        </tbody>
      </table>

      <div class="pi-totals">
        <div class="pi-total-row"><span>Subtotal</span><span>{{ formatCurrency(order.subtotal) }}</span></div>
        <div class="pi-total-row"><span>Shipping</span><span>{{ formatCurrency(order.shippingCost) }}</span></div>
        <div class="pi-total-row"><span>Tax</span><span>{{ formatCurrency(order.tax) }}</span></div>
        <div class="pi-total-row pi-grand-total"><span>Total</span><span>{{ formatCurrency(order.total) }}</span></div>
        <div class="pi-total-row" style="margin-top:8px; font-size:11px; color:#666;">
          <span>Payment</span><span>{{ order.paymentMethod }}</span>
        </div>
      </div>

      <div class="pi-footer">
        <p>Thank you for your business!</p>
        <p style="font-size:11px; color:#aaa; margin-top:4px;">Generated by ShopAdmin</p>
      </div>
    </div>
</template>
<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatCurrency, formatDate, getInitials } from '@/utils/formatters';
import { orderService } from '@/services/orderService';
import { useToast } from '@/composables/useToast';
import { useConfirm } from '@/composables/useConfirm';
import type { Order } from '@/types/order';

const route = useRoute();
const router = useRouter();
const toast  = useToast();
const { confirm } = useConfirm();
const order = ref<Order | null>(null);
const orderId = ref();
const loading = ref(true);
const error = ref<string | null>(null);

// ── Feature 7: activity log, tracking, ship dialog, refund ──────────────
const refundAmount = ref(0);
const showShipDialog = ref(false);
const shipForm = reactive({ carrier: 'FedEx', trackingNumber: '', eta: '' });
const trackingInfo = reactive({ carrier: '', number: '', eta: '', url: '#' });

interface ActivityEvent {
  title:      string;
  detail?:    string;
  time:       string;
  icon:       string;
  iconBg:     string;
  iconColor:  string;
}
const activityLog = ref<ActivityEvent[]>([
  {
    title:    'Order created',
    detail:   'Customer placed order online',
    time:     'Just now',
    icon:     'fas fa-bag-shopping',
    iconBg:   'var(--ni-blue-bg)',
    iconColor:'var(--ni-blue)',
  },
]);

const fetchOrderDetails = async () => {
    loading.value = true;
    error.value = null;

    try {
        const id = Number(route.params.id);
        order.value = await orderService.getOrderById(id);
    } catch (err) {
        error.value = err instanceof Error ? err.message : 'Failed to load order';
    } finally {
        loading.value = false;
    }
};

const updateStatus = async () => {
    if (!order.value?.id) return;

    try {
        order.value = await orderService.updateOrderStatus(order.value.id, order.value.status);
        toast.success(`Order status updated to ${order.value.status}`, 'Status Updated');
    } catch (err: unknown) {
        toast.error(err instanceof Error ? err.message : 'Failed to update order status', 'Error');
    }
};

// ── Quick status helper ──────────────────────────────────────────────────
const statusMeta: Record<string, { icon: string; iconBg: string; iconColor: string }> = {
    processing: { icon: 'fas fa-gear',         iconBg: 'var(--ni-blue-bg)',   iconColor: 'var(--ni-blue)'   },
    shipped:    { icon: 'fas fa-truck',         iconBg: 'var(--ni-purple-bg)', iconColor: 'var(--ni-purple)' },
    delivered:  { icon: 'fas fa-circle-check',  iconBg: 'var(--ni-green-bg)',  iconColor: 'var(--ni-green)'  },
    cancelled:  { icon: 'fas fa-ban',           iconBg: 'var(--ni-red-bg)',    iconColor: 'var(--ni-red)'    },
};

async function quickStatus(newStatus: string) {
    if (!order.value?.id) return;
    try {
        order.value = await orderService.updateOrderStatus(order.value.id, newStatus as Order['status']);
        const meta = statusMeta[newStatus] ?? { icon: 'fas fa-circle-check', iconBg: 'var(--ni-green-bg)', iconColor: 'var(--ni-green)' };
        activityLog.value.unshift({
            title:    `Status changed to ${newStatus}`,
            detail:   'Updated by store admin',
            time:     'Just now',
            ...meta,
        });
        toast.success(`Order marked as ${newStatus}`, 'Status Updated');
    } catch (e: unknown) {
        toast.error(e instanceof Error ? e.message : 'Status update failed', 'Error');
    }
}

function openShipDialog() { showShipDialog.value = true; }

async function confirmShip() {
    if (!shipForm.trackingNumber.trim()) {
        toast.warning('Please enter a tracking number');
        return;
    }
    trackingInfo.carrier = shipForm.carrier;
    trackingInfo.number  = shipForm.trackingNumber;
    trackingInfo.eta     = shipForm.eta ? new Date(shipForm.eta).toLocaleDateString() : 'TBD';
    trackingInfo.url     = `https://www.google.com/search?q=${encodeURIComponent(shipForm.carrier + ' ' + shipForm.trackingNumber)}`;
    await quickStatus('shipped');
    activityLog.value.unshift({
        title:    `Tracking added — ${shipForm.carrier}`,
        detail:   shipForm.trackingNumber,
        time:     'Just now',
        icon:     'fas fa-truck',
        iconBg:   'var(--ni-purple-bg)',
        iconColor:'var(--ni-purple)',
    });
    showShipDialog.value = false;
    toast.success(`Tracking ${shipForm.trackingNumber} saved`);
}

async function initiateRefund() {
    if (!order.value) return;
    const ok = await confirm({
        title:       'Process refund',
        message:     `Issue a full refund of ${formatCurrency(order.value.total)} for order #${order.value.orderNumber}?`,
        detail:      'The refund will be returned to the original payment method within 3–5 business days.',
        confirmText: 'Issue Refund',
        variant:     'warning',
    });
    if (!ok) return;
    refundAmount.value = order.value.total;
    activityLog.value.unshift({
        title:    `Refund issued — ${formatCurrency(order.value.total)}`,
        detail:   'Returned to original payment method',
        time:     'Just now',
        icon:     'fas fa-rotate-left',
        iconBg:   'var(--ni-orange-bg)',
        iconColor:'var(--ni-orange)',
    });
    toast.success(`Refund of ${formatCurrency(order.value.total)} initiated`, 'Refund Processed');
}

    // Print invoice — uses the #print-invoice element + @media print styles
const printInvoice = () => {
    if (!order.value) return;
    toast.info('Opening print dialog…');
    setTimeout(() => window.print(), 150);
};

    // Send email functionality
const sendEmail = () => {
    if (!order.value) {
        return;
    }
    const emailSubject = encodeURIComponent(`Update on your order ${order.value.orderNumber}`);
    const emailBody = encodeURIComponent(
        `Dear ${order.value.customerName},\n\n` +
        `This is an update regarding your order #${order.value.orderNumber}.\n` +
        `Current Status: ${order.value.status}\n\n` +
        `Thank you for your business!\n\n` +
        `Best regards,\n` +
        `Store Support Team`
    );

    window.location.href = `mailto:${order.value.customerEmail}?subject=${emailSubject}&body=${emailBody}`;
};

    // View customer profile
const viewCustomer = () => {
    router.push(`/customers/${order.value!.id}`);
};

    // Add note functionality
const addNote = async () => {
    if (!order.value?.id) return;

    const content = window.prompt('Enter note:');
    if (content?.trim()) {
        try {
        order.value = await orderService.addOrderNote(order.value.id, {
            author: 'Current User',
            content: content.trim()
        });
        toast.success('Note added to order');
        } catch (err: unknown) {
        toast.error(err instanceof Error ? err.message : 'Failed to add note', 'Error');
        }
    }
};

onMounted(async () => {
    orderId.value = route.params.id;
    await fetchOrderDetails();
    if (order.value) {
        // Build initial activity log from order data
        activityLog.value = [];
        const o = order.value;
        const fallbackMeta = { icon: 'fas fa-circle', iconBg: 'var(--ni-blue-bg)', iconColor: 'var(--ni-blue)' };
        if (o.deliveredAt) activityLog.value.push({ title: 'Order delivered', detail: 'Marked as delivered', time: formatDate(o.deliveredAt), ...(statusMeta.delivered ?? fallbackMeta) });
        if (o.shippedAt)   activityLog.value.push({ title: 'Order shipped',   detail: trackingInfo.number ? `Tracking: ${trackingInfo.number}` : 'Dispatched to carrier', time: formatDate(o.shippedAt), ...(statusMeta.shipped ?? fallbackMeta) });
        if (o.processedAt) activityLog.value.push({ title: 'Processing started', detail: 'Order accepted for fulfillment', time: formatDate(o.processedAt), ...(statusMeta.processing ?? fallbackMeta) });
        activityLog.value.push({ title: 'Order placed', detail: 'Customer placed order online', time: formatDate(o.createdAt), icon: 'fas fa-bag-shopping', iconBg: 'var(--ni-blue-bg)', iconColor: 'var(--ni-blue)' });
    }
});
</script>

<style>
/* ── Print invoice layout ──────────────────────────────────────────────── */

/* On screen: hide the invoice div */
.print-invoice { display: none; }

/* When printing: hide the whole app, show only the invoice */
@media print {
  #app,
  .page-header,
  .glass-card,
  footer,
  nav { display: none !important; }

  .print-invoice {
    display: block !important;
    font-family: 'Helvetica Neue', Arial, sans-serif;
    font-size: 13px;
    color: #111;
    padding: 40px;
    max-width: 900px;
    margin: 0 auto;
  }

  .pi-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    padding-bottom: 20px;
    border-bottom: 2px solid #222;
    margin-bottom: 28px;
  }
  .pi-logo {
    font-size: 26px;
    font-weight: 800;
    letter-spacing: -0.5px;
    margin: 0;
    color: #111;
  }
  .pi-tagline {
    font-size: 12px;
    color: #888;
    margin: 2px 0 0;
    text-transform: uppercase;
    letter-spacing: 1px;
  }
  .pi-header-right { text-align: right; line-height: 1.6; }
  .pi-order-num { font-size: 18px; font-weight: 700; margin: 0 0 4px; }

  .pi-addresses {
    display: flex;
    gap: 40px;
    margin-bottom: 28px;
  }
  .pi-addresses > div { flex: 1; }
  .pi-addresses h3 {
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 0.8px;
    color: #888;
    margin: 0 0 6px;
    border-bottom: 1px solid #ddd;
    padding-bottom: 4px;
  }
  .pi-addresses p { margin: 2px 0; line-height: 1.5; }

  .pi-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 24px;
  }
  .pi-table th {
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 0.7px;
    color: #555;
    background: #f5f5f5;
    padding: 8px 10px;
    border-bottom: 2px solid #ddd;
    text-align: left;
  }
  .pi-table td {
    padding: 9px 10px;
    border-bottom: 1px solid #eee;
    color: #222;
  }
  .pi-right { text-align: right !important; }

  .pi-totals {
    width: 260px;
    margin-left: auto;
    margin-bottom: 32px;
  }
  .pi-total-row {
    display: flex;
    justify-content: space-between;
    padding: 4px 0;
    font-size: 13px;
    color: #444;
    border-bottom: 1px solid #eee;
  }
  .pi-grand-total {
    font-weight: 700;
    font-size: 15px;
    color: #111;
    border-top: 2px solid #222;
    border-bottom: none;
    padding-top: 8px;
    margin-top: 4px;
  }

  .pi-footer {
    text-align: center;
    padding-top: 24px;
    border-top: 1px solid #ddd;
    color: #888;
    font-size: 12px;
  }
}
</style>
