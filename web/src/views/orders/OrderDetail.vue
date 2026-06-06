<template>
    <div v-if="!order"></div>
    <div v-else class="gap-6">
        <!-- Page Header -->
        <div class="page-header">
            <div>
                <h1 class="page-title">Order #{{ order.orderNumber }}</h1>
                <p class="page-subtitle">Placed on {{ formatDate(order.createdAt) }}</p>
            </div>
            <div class="flex items-center gap-3">
                <select v-model="order.status" @change="updateStatus" class="glass-select text-sm">
                    <option value="pending">Pending</option>
                    <option value="processing">Processing</option>
                    <option value="shipped">Shipped</option>
                    <option value="delivered">Delivered</option>
                    <option value="cancelled">Cancelled</option>
                </select>
                <button @click="printInvoice" class="btn-glass text-sm">
                    <i class="fas fa-print text-xs mr-1"></i>
                    Print Invoice
                </button>
                <button @click="sendEmail" class="btn-glass text-sm">
                    <i class="fas fa-envelope text-xs mr-1"></i>
                    Send Email
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
                        <div class="flex justify-between font-semibold pt-2" style="border-top: 1px solid var(--glass-border);">
                            <span style="color: var(--text-primary);">Total</span>
                            <span class="td-accent">{{ formatCurrency(order.total) }}</span>
                        </div>
                        <div class="pt-3 flex items-center gap-2" style="border-top: 1px solid var(--glass-border);">
                            <div class="stat-icon ni-b w-8 h-8 text-xs"><i class="fas fa-credit-card"></i></div>
                            <span class="text-sm" style="color: var(--text-primary);">{{ order.paymentMethod }}</span>
                            <Badge variant="success">Paid</Badge>
                        </div>
                    </div>
                </Card>
            </div>
        </div>

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
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatCurrency, formatDate, getInitials } from '@/utils/formatters';
import { orderService } from '@/services/orderService';
import type { Order } from '@/types/order';

const route = useRoute();
const router = useRouter();
const order = ref<Order | null>(null);
const orderId = ref();
const loading = ref(true);
const error = ref<string | null>(null);

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
    } catch (err) {
        console.error('Error updating status:', err);
        alert('Failed to update order status');
    }
};

    // Print invoice functionality
const printInvoice = () => {
    if (!order.value) {
        return;
    }
    // Create a print-friendly version of the invoice
    const printContent = `
        <html>
        <head>
            <title>Invoice ${order.value.orderNumber}</title>
            <style>
            body { font-family: Arial, sans-serif; padding: 20px; }
            .invoice-header { border-bottom: 2px solid #333; padding-bottom: 20px; margin-bottom: 20px; }
            .invoice-details { margin-bottom: 30px; }
            table { width: 100%; border-collapse: collapse; margin: 20px 0; }
            th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
            th { background-color: #f5f5f5; }
            .total-row { font-weight: bold; }
            @media print {
                body { margin: 0; }
                .no-print { display: none; }
            }
            </style>
        </head>
        <body>
            <div class="invoice-header">
            <h1>INVOICE</h1>
            <h2>Order #${order.value.orderNumber}</h2>
            <p>Date: ${formatDate(order.value.createdAt)}</p>
            <p>Status: ${order.value.status.toUpperCase()}</p>
            </div>

            <div class="invoice-details">
            <div style="float: left; width: 50%;">
                <h3>Bill To:</h3>
                <p>${order.value.customerName}<br>
                ${order.value.customerEmail}<br>
                ${order.value.customerPhone}</p>
            </div>
            <div style="float: right; width: 50%;">
                <h3>Ship To:</h3>
                <p>${order.value.shippingAddress.name}<br>
                ${order.value.shippingAddress.street}<br>
                ${order.value.shippingAddress.city}, ${order.value.shippingAddress.state} ${order.value.shippingAddress.zipCode}<br>
                ${order.value.shippingAddress.country}</p>
            </div>
            <div style="clear: both;"></div>
            </div>

            <table>
            <thead>
                <tr>
                <th>Item</th>
                <th>SKU</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Total</th>
                </tr>
            </thead>
            <tbody>
                ${order.value.items.map(item => `
                <tr>
                    <td>${item.name}</td>
                    <td>${item.sku}</td>
                    <td>${item.quantity}</td>
                    <td>${formatCurrency(item.price)}</td>
                    <td>${formatCurrency(item.price * item.quantity)}</td>
                </tr>
                `).join('')}
            </tbody>
            </table>

            <div style="float: right; width: 300px;">
            <table>
                <tr>
                <td>Subtotal:</td>
                <td>${formatCurrency(order.value.subtotal)}</td>
                </tr>
                <tr>
                <td>Shipping:</td>
                <td>${formatCurrency(order.value.shippingCost)}</td>
                </tr>
                <tr>
                <td>Tax:</td>
                <td>${formatCurrency(order.value.tax)}</td>
                </tr>
                <tr class="total-row">
                <td>Total:</td>
                <td>${formatCurrency(order.value.total)}</td>
                </tr>
            </table>
            </div>
            <div style="clear: both;"></div>

            <div class="no-print" style="margin-top: 30px;">
            <button onclick="window.print()">Print Invoice</button>
            <button onclick="window.close()">Close</button>
            </div>
        </body>
    </html>
`;

    // Open print window
const printWindow = window.open('', '_blank');
    if (printWindow) {
        printWindow.document.write(printContent);
        printWindow.document.close();
        printWindow.focus();
        // Auto-print after a short delay
        setTimeout(() => {
        printWindow.print();
        }, 250);
    }
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

    const content = prompt('Enter note:');
    if (content?.trim()) {
        try {
        order.value = await orderService.addOrderNote(order.value.id, {
            author: 'Current User',
            content: content.trim()
        });
        } catch (err) {
        console.error('Error adding note:', err);
        alert('Failed to add note');
        }
    }
};

// In a real app, you would fetch the order data based on route params
onMounted(() => {
    orderId.value = route.params.id;
    console.log('Loading order:', orderId);
    fetchOrderDetails();
});
</script>
