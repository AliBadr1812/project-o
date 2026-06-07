<template>
  <div class="flex flex-col gap-6">

    <!-- Page Header -->
    <div class="flex items-center justify-between gap-4 flex-wrap">
      <div>
        <h1 class="page-title">Notifications</h1>
        <div class="flex items-center gap-2 mt-1.5 flex-wrap">
          <span class="badge badge-info">{{ notifications.length }} total</span>
          <span v-if="unreadCount > 0" class="badge badge-danger">{{ unreadCount }} unread</span>
          <span v-if="alertCount > 0" class="badge badge-warning">{{ alertCount }} alerts</span>
        </div>
      </div>
      <div class="flex items-center gap-2">
        <button
          v-if="unreadCount > 0"
          @click="markAllRead"
          class="btn-glass flex items-center gap-2 text-sm"
        >
          <i class="fas fa-check-double text-xs"></i>
          Mark all read
        </button>
        <button
          v-if="notifications.length > 0"
          @click="clearAll"
          class="btn-glass flex items-center gap-2 text-sm"
        >
          <i class="fas fa-trash-can text-xs"></i>
          Clear all
        </button>
      </div>
    </div>

    <!-- Filter row -->
    <div class="flex items-center gap-3 flex-wrap">
      <div class="notif-filter-bar flex items-center gap-1 p-1 rounded-xl">
        <button
          v-for="tab in filterTabs"
          :key="tab.id"
          @click="activeFilter = tab.id"
          class="pill-tab px-4 py-1.5 rounded-lg text-[13px] font-medium transition-all duration-200 flex items-center gap-1.5"
          :class="{ active: activeFilter === tab.id }"
        >
          <i :class="tab.icon" class="text-[11px]"></i>
          {{ tab.label }}
          <span
            v-if="tab.count > 0"
            class="notif-count-badge"
            :class="{ 'active': activeFilter === tab.id }"
          >{{ tab.count }}</span>
        </button>
      </div>
    </div>

    <!-- Empty state -->
    <div v-if="filtered.length === 0" class="glass-card flex flex-col items-center justify-center py-20 gap-4">
      <div class="w-16 h-16 rounded-2xl flex items-center justify-center" style="background: var(--ni-neutral-bg);">
        <i class="fas fa-bell-slash text-2xl" style="color: var(--text-muted);"></i>
      </div>
      <div class="text-center">
        <p class="text-[15px] font-semibold" style="color: var(--text-primary);">All caught up</p>
        <p class="text-[13px] mt-1" style="color: var(--text-muted);">No notifications to show.</p>
      </div>
    </div>

    <!-- Notification feed -->
    <template v-else>
      <div v-for="group in groupedNotifications" :key="group.label">

        <!-- Group label -->
        <div class="flex items-center gap-3 mb-3">
          <span class="text-[11px] font-semibold uppercase tracking-widest px-0.5" style="color: var(--text-muted);">
            {{ group.label }}
          </span>
          <div class="flex-1 h-px" style="background: var(--divider);"></div>
          <span class="text-[11px]" style="color: var(--text-muted);">{{ group.items.length }}</span>
        </div>

        <!-- Feed card -->
        <div class="glass-card overflow-hidden mb-5">
          <div
            v-for="(n, idx) in group.items"
            :key="n.id"
            @click="markRead(n)"
            class="notif-row flex items-start gap-4 px-5 py-4 cursor-pointer transition-all duration-150"
            :class="{ 'is-unread': !n.read, 'border-t': idx > 0 }"
            :style="idx > 0 ? 'border-color: var(--glass-border)' : ''"
          >
            <!-- Unread pip -->
            <div class="flex-shrink-0 w-2 flex justify-center pt-[18px]">
              <div
                v-if="!n.read"
                class="w-2 h-2 rounded-full"
                :style="{ background: typeConfig[n.type].dotColor }"
              ></div>
            </div>

            <!-- Icon -->
            <div
              class="notif-icon w-9 h-9 rounded-[11px] flex items-center justify-center flex-shrink-0 mt-0.5"
              :style="{ background: typeConfig[n.type].iconBg }"
            >
              <i :class="typeConfig[n.type].icon" class="text-[13px]" :style="{ color: typeConfig[n.type].iconColor }"></i>
            </div>

            <!-- Body -->
            <div class="flex-1 min-w-0">
              <div class="flex items-baseline justify-between gap-3 mb-0.5">
                <p
                  class="text-[13.5px] leading-snug"
                  :class="n.read ? 'font-medium' : 'font-semibold'"
                  style="color: var(--text-primary);"
                >{{ n.title }}</p>
                <span class="text-[11.5px] flex-shrink-0 tabular-nums" style="color: var(--text-muted);">{{ n.time }}</span>
              </div>
              <p class="text-[13px] leading-relaxed" style="color: var(--text-secondary);">{{ n.message }}</p>
              <div v-if="n.action" class="mt-2">
                <router-link
                  :to="n.action.path"
                  @click.stop
                  class="inline-flex items-center gap-1 text-[12px] font-semibold transition-opacity hover:opacity-70"
                  style="color: var(--text-accent);"
                >
                  {{ n.action.label }}
                  <i class="fas fa-arrow-right text-[10px]"></i>
                </router-link>
              </div>
            </div>

            <!-- Dismiss -->
            <button
              @click.stop="dismiss(n)"
              class="notif-dismiss flex-shrink-0 w-7 h-7 rounded-lg flex items-center justify-center transition-all duration-150"
              title="Dismiss"
            >
              <i class="fas fa-xmark text-xs"></i>
            </button>
          </div>
        </div>

      </div>
    </template>

  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';

/* ── Types ── */
type NotificationType = 'order' | 'customer' | 'product' | 'alert' | 'info' | 'payment';

interface Notification {
  id: number;
  type: NotificationType;
  title: string;
  message: string;
  time: string;
  timestamp: number;
  read: boolean;
  action?: { label: string; path: string };
}

/* ── Type visual config ── */
const typeConfig: Record<NotificationType, {
  icon: string;
  iconBg: string;
  iconColor: string;
  dotColor: string;
}> = {
  order:    { icon: 'fas fa-bag-shopping',         iconBg: 'var(--ni-blue-bg)',   iconColor: 'var(--ni-blue)',   dotColor: 'var(--ni-blue)'   },
  customer: { icon: 'fas fa-user-circle',          iconBg: 'var(--ni-purple-bg)', iconColor: 'var(--ni-purple)', dotColor: 'var(--ni-purple)' },
  product:  { icon: 'fas fa-box',                  iconBg: 'var(--ni-orange-bg)', iconColor: 'var(--ni-orange)', dotColor: 'var(--ni-orange)' },
  alert:    { icon: 'fas fa-triangle-exclamation', iconBg: 'var(--ni-red-bg)',    iconColor: 'var(--ni-red)',    dotColor: 'var(--ni-red)'    },
  info:     { icon: 'fas fa-circle-info',          iconBg: 'var(--ni-teal-bg)',   iconColor: 'var(--ni-teal)',   dotColor: 'var(--ni-teal)'   },
  payment:  { icon: 'fas fa-credit-card',          iconBg: 'var(--ni-green-bg)',  iconColor: 'var(--ni-green)',  dotColor: 'var(--ni-green)'  },
};

/* ── Mock data ── */
const notifications = ref<Notification[]>([
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
    message: "Ethan Clarke signed up and completed their first purchase — eligible for the returning-customer tier.",
    time: '1 hr ago', timestamp: Date.now() - 60 * 60_000, read: false,
    action: { label: 'View customer', path: '/customers' },
  },
  {
    id: 5, type: 'order',
    title: 'Order shipped',
    message: 'Order #1035 for Aisha Patel has been dispatched. Tracking: TRK-882910-US via FedEx.',
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
    message: '$45.00 refund issued to Isabella Torres for order #1019. Expected within 3–5 business days.',
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
    message: '"Audio & Wearables" was updated — 14 products re-tagged with the new subcategories.',
    time: '3 days ago', timestamp: Date.now() - 72 * 3600_000, read: true,
    action: { label: 'View categories', path: '/categories' },
  },
]);

/* ── Derived counts ── */
const unreadCount = computed(() => notifications.value.filter(n => !n.read).length);
const alertCount  = computed(() => notifications.value.filter(n => n.type === 'alert').length);

/* ── Filter tabs ── */
const activeFilter = ref<'all' | 'unread' | 'orders' | 'alerts'>('all');

const filterTabs = computed(() => [
  { id: 'all',    label: 'All',    icon: 'fas fa-inbox',                count: notifications.value.length },
  { id: 'unread', label: 'Unread', icon: 'fas fa-circle',               count: unreadCount.value },
  { id: 'orders', label: 'Orders', icon: 'fas fa-bag-shopping',         count: notifications.value.filter(n => n.type === 'order').length },
  { id: 'alerts', label: 'Alerts', icon: 'fas fa-triangle-exclamation', count: alertCount.value },
]);

const filtered = computed(() => {
  if (activeFilter.value === 'unread') return notifications.value.filter(n => !n.read);
  if (activeFilter.value === 'orders') return notifications.value.filter(n => n.type === 'order');
  if (activeFilter.value === 'alerts') return notifications.value.filter(n => n.type === 'alert');
  return notifications.value;
});

/* ── Grouping ── */
const groupedNotifications = computed(() => {
  const now = Date.now();
  const groups: { label: string; items: Notification[] }[] = [];

  const today     = filtered.value.filter(n => now - n.timestamp < 24 * 3600_000);
  const yesterday = filtered.value.filter(n => { const d = now - n.timestamp; return d >= 24 * 3600_000 && d < 48 * 3600_000; });
  const older     = filtered.value.filter(n => now - n.timestamp >= 48 * 3600_000);

  if (today.length)     groups.push({ label: 'Today',     items: today });
  if (yesterday.length) groups.push({ label: 'Yesterday', items: yesterday });
  if (older.length)     groups.push({ label: 'Earlier',   items: older });

  return groups;
});

/* ── Actions ── */
function markRead(n: Notification)  { n.read = true; }
function markAllRead()              { notifications.value.forEach(n => { n.read = true; }); }
function dismiss(n: Notification)   { notifications.value.splice(notifications.value.findIndex(x => x.id === n.id), 1); }
function clearAll()                 { notifications.value = []; }
</script>

<style scoped>
/* Filter bar container — uses CSS vars so dark mode picks up automatically */
.notif-filter-bar {
  background: var(--glass-bg);
  border: 1px solid var(--glass-border);
}

/* Unread row tint */
.notif-row.is-unread {
  background: rgba(var(--accent-rgb), 0.04);
}
.notif-row:hover {
  background: var(--table-row-hover);
}

/* Dismiss button — hidden until row is hovered */
.notif-dismiss {
  opacity: 0;
  color: var(--text-muted);
  background: transparent;
}
.notif-dismiss:hover {
  background: var(--ni-red-bg);
  color: var(--ni-red);
  opacity: 1 !important;
}
.notif-row:hover .notif-dismiss {
  opacity: 1;
}

/* Unread badge on filter tabs */
.notif-count-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 16px;
  padding: 0 4px;
  border-radius: 999px;
  font-size: 10px;
  font-weight: 700;
  background: var(--glass-bg-strong);
  color: var(--text-secondary);
  border: 1px solid var(--glass-border);
}
.notif-count-badge.active {
  background: rgba(255,255,255,0.25);
  color: #fff;
  border-color: rgba(255,255,255,0.20);
}
</style>
