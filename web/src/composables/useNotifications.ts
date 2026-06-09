/**
 * Shared notification state.
 * Module-level refs so every component reads the same unread count,
 * just like useCommandPalette / useTheme.
 */
import { ref, computed } from 'vue';

export interface AppNotification {
  id:        number;
  type:      'order' | 'customer' | 'product' | 'alert' | 'info' | 'payment';
  title:     string;
  message:   string;
  time:      string;
  timestamp: number;
  read:      boolean;
  action?:   { label: string; path: string };
}

const notifications = ref<AppNotification[]>([
  {
    id: 1, type: 'order',
    title: 'New order received',
    message: 'Order #1042 for $284.00 was placed by Marcus Johnson — 2× Wireless Headphones, 1× USB-C Hub.',
    time: '2 min ago', timestamp: Date.now() - 2 * 60_000, read: false,
    action: { label: 'View order', path: '/orders' },
  },
  {
    id: 2, type: 'alert',
    title: 'Low stock warning',
    message: 'Mechanical Keyboard Pro is down to 3 units. Restock soon to avoid a stockout.',
    time: '15 min ago', timestamp: Date.now() - 15 * 60_000, read: false,
    action: { label: 'Manage products', path: '/products' },
  },
  {
    id: 3, type: 'payment',
    title: 'Payment confirmed',
    message: '$1,240.00 for order #1038 (Sophia Kim) was successfully processed via Stripe.',
    time: '38 min ago', timestamp: Date.now() - 38 * 60_000, read: false,
    action: { label: 'View order', path: '/orders' },
  },
  {
    id: 4, type: 'customer',
    title: 'New customer registered',
    message: "Ethan Clarke signed up and completed their first purchase.",
    time: '1 hr ago', timestamp: Date.now() - 60 * 60_000, read: false,
    action: { label: 'View customer', path: '/customers' },
  },
  {
    id: 5, type: 'order',
    title: 'Order shipped',
    message: 'Order #1035 for Aisha Patel has been dispatched via FedEx.',
    time: '2 hr ago', timestamp: Date.now() - 2 * 3600_000, read: true,
    action: { label: 'View order', path: '/orders' },
  },
  {
    id: 6, type: 'info',
    title: 'Monthly report ready',
    message: 'Your May 2026 sales report is ready. Total revenue: $48,320 — up 12% vs. April.',
    time: '4 hr ago', timestamp: Date.now() - 4 * 3600_000, read: true,
    action: { label: 'View analytics', path: '/analytics' },
  },
  {
    id: 7, type: 'alert',
    title: 'Order cancellation request',
    message: 'Noah Bennett has requested to cancel order #1031 ($98.00). Respond within 24 hours.',
    time: '6 hr ago', timestamp: Date.now() - 6 * 3600_000, read: true,
    action: { label: 'Review order', path: '/orders' },
  },
  {
    id: 8, type: 'product',
    title: 'New product review',
    message: '5-star review submitted for "Noise-Cancelling Earbuds Pro" by verified buyer Lena Fischer.',
    time: 'Yesterday', timestamp: Date.now() - 24 * 3600_000, read: true,
    action: { label: 'View product', path: '/products' },
  },
  {
    id: 9, type: 'customer',
    title: 'VIP milestone reached',
    message: "Sophia Kim completed her 10th purchase and has been upgraded to VIP status.",
    time: 'Yesterday', timestamp: Date.now() - 26 * 3600_000, read: true,
    action: { label: 'View customer', path: '/customers' },
  },
  {
    id: 10, type: 'payment',
    title: 'Refund issued',
    message: '$45.00 refund issued to Isabella Torres for order #1019.',
    time: '2 days ago', timestamp: Date.now() - 48 * 3600_000, read: true,
  },
  {
    id: 11, type: 'info',
    title: 'Maintenance window scheduled',
    message: 'Planned maintenance June 10, 02:00–04:00 UTC. Dashboard will be in read-only mode.',
    time: '2 days ago', timestamp: Date.now() - 50 * 3600_000, read: true,
  },
  {
    id: 12, type: 'product',
    title: 'Category reorganised',
    message: '"Audio & Wearables" was updated — 14 products re-tagged with new subcategories.',
    time: '3 days ago', timestamp: Date.now() - 72 * 3600_000, read: true,
    action: { label: 'View categories', path: '/categories' },
  },
]);

const unreadCount = computed(() => notifications.value.filter(n => !n.read).length);

function markRead(id: number) {
  const n = notifications.value.find(n => n.id === id);
  if (n) n.read = true;
}

function markAllRead() {
  notifications.value.forEach(n => { n.read = true; });
}

function dismiss(id: number) {
  notifications.value = notifications.value.filter(n => n.id !== id);
}

function clearAll() {
  notifications.value = [];
}

export function useNotifications() {
  return { notifications, unreadCount, markRead, markAllRead, dismiss, clearAll };
}
