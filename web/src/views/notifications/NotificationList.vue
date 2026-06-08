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
      <div class="flex items-center gap-2 flex-wrap">
        <!-- Bulk mode toggle -->
        <button @click="toggleBulkMode" class="btn-glass flex items-center gap-2 text-sm"
          :class="{ 'btn-accent': bulkMode }">
          <i class="fas fa-list-check text-xs"></i>
          {{ bulkMode ? 'Cancel' : 'Select' }}
        </button>
        <!-- Bulk actions -->
        <template v-if="bulkMode && selectedIds.size > 0">
          <button @click="bulkMarkRead" class="btn-glass flex items-center gap-2 text-sm">
            <i class="fas fa-check-double text-xs"></i>
            Mark read ({{ selectedIds.size }})
          </button>
          <button @click="bulkDismiss" class="btn-glass flex items-center gap-2 text-sm"
            style="color: var(--ni-red);">
            <i class="fas fa-trash-can text-xs"></i>
            Delete ({{ selectedIds.size }})
          </button>
        </template>
        <template v-else-if="!bulkMode">
          <button v-if="unreadCount > 0" @click="markAllRead"
            class="btn-glass flex items-center gap-2 text-sm">
            <i class="fas fa-check-double text-xs"></i>
            Mark all read
          </button>
          <button v-if="notifications.length > 0" @click="clearAll"
            class="btn-glass flex items-center gap-2 text-sm">
            <i class="fas fa-trash-can text-xs"></i>
            Clear all
          </button>
          <button @click="showPrefs = !showPrefs"
            class="btn-glass flex items-center gap-2 text-sm"
            :class="{ 'btn-accent': showPrefs }" title="Notification preferences">
            <i class="fas fa-sliders text-xs"></i>
          </button>
        </template>
      </div>
    </div>

    <!-- Preferences panel -->
    <Transition name="prefs-slide">
      <div v-if="showPrefs" class="glass-card p-5">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-[14px] font-semibold" style="color: var(--text-primary);">
            <i class="fas fa-sliders text-xs mr-2" style="color: var(--accent);"></i>
            Notification Preferences
          </h2>
          <button @click="showPrefs = false" class="btn-glass-icon w-7 h-7 rounded-lg text-xs">
            <i class="fas fa-xmark"></i>
          </button>
        </div>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
          <div v-for="pref in prefs" :key="pref.key"
            class="flex items-center justify-between p-3 rounded-xl"
            style="background: var(--glass-bg); border: 1px solid var(--glass-border);">
            <div class="flex items-center gap-2.5">
              <div class="w-7 h-7 rounded-lg flex items-center justify-center text-xs"
                :style="{ background: pref.iconBg, color: pref.iconColor }">
                <i :class="pref.icon"></i>
              </div>
              <span class="text-sm font-medium" style="color: var(--text-primary);">{{ pref.label }}</span>
            </div>
            <button @click="pref.enabled = !pref.enabled"
              class="notif-toggle"
              :class="{ 'notif-toggle--on': pref.enabled }">
              <span class="notif-toggle-knob"></span>
            </button>
          </div>
        </div>
        <p class="text-xs mt-3" style="color: var(--text-muted);">
          <i class="fas fa-circle-info mr-1"></i>
          Changes are saved automatically and apply to in-app notifications.
        </p>
      </div>
    </Transition>

    <!-- Filter row + search -->
    <div class="flex items-center gap-3 flex-wrap">
      <div class="notif-filter-bar flex items-center gap-1 p-1 rounded-xl flex-wrap">
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
      <!-- Search -->
      <div class="relative ml-auto">
        <i class="fas fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-xs"
          style="color: var(--text-muted);"></i>
        <input v-model="searchQuery" type="text" placeholder="Search notifications…"
          class="glass-input pl-9 text-sm" style="width: 220px;">
        <button v-if="searchQuery" @click="searchQuery = ''"
          class="absolute right-3 top-1/2 -translate-y-1/2 text-xs"
          style="color: var(--text-muted);">
          <i class="fas fa-xmark"></i>
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
            @click="bulkMode ? toggleSelect(n.id) : markRead(n)"
            class="notif-row flex items-start gap-4 px-5 py-4 cursor-pointer transition-all duration-150"
            :class="{ 'is-unread': !n.read, 'border-t': idx > 0, 'is-selected': selectedIds.has(n.id) }"
            :style="idx > 0 ? 'border-color: var(--glass-border)' : ''"
          >
            <!-- Bulk checkbox / Unread pip -->
            <div class="flex-shrink-0 w-5 flex justify-center pt-[14px]">
              <template v-if="bulkMode">
                <div class="notif-checkbox" :class="{ 'notif-checkbox--checked': selectedIds.has(n.id) }">
                  <i v-if="selectedIds.has(n.id)" class="fas fa-check text-[9px]"></i>
                </div>
              </template>
              <div v-else-if="!n.read" class="w-2 h-2 rounded-full mt-1"
                :style="{ background: typeConfig[n.type].dotColor }"></div>
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
import { ref, computed, reactive } from 'vue';
import { useToast } from '@/composables/useToast';
import { useConfirm } from '@/composables/useConfirm';

const toast = useToast();
const { confirm } = useConfirm();

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

/* ── UI state ── */
const searchQuery = ref('');
const showPrefs   = ref(false);
const bulkMode    = ref(false);
const selectedIds = ref(new Set<number>());

/* ── Notification preferences ── */
const prefs = reactive([
  { key: 'orders',    label: 'Orders',    icon: 'fas fa-bag-shopping',         iconBg: 'var(--ni-blue-bg)',   iconColor: 'var(--ni-blue)',   enabled: true  },
  { key: 'payments',  label: 'Payments',  icon: 'fas fa-credit-card',          iconBg: 'var(--ni-green-bg)',  iconColor: 'var(--ni-green)',  enabled: true  },
  { key: 'customers', label: 'Customers', icon: 'fas fa-user-circle',          iconBg: 'var(--ni-purple-bg)', iconColor: 'var(--ni-purple)', enabled: true  },
  { key: 'products',  label: 'Products',  icon: 'fas fa-box',                  iconBg: 'var(--ni-orange-bg)', iconColor: 'var(--ni-orange)', enabled: true  },
  { key: 'alerts',    label: 'Alerts',    icon: 'fas fa-triangle-exclamation', iconBg: 'var(--ni-red-bg)',    iconColor: 'var(--ni-red)',    enabled: true  },
  { key: 'info',      label: 'Info',      icon: 'fas fa-circle-info',          iconBg: 'var(--ni-teal-bg)',   iconColor: 'var(--ni-teal)',   enabled: false },
]);

/* ── Derived counts ── */
const unreadCount = computed(() => notifications.value.filter(n => !n.read).length);
const alertCount  = computed(() => notifications.value.filter(n => n.type === 'alert').length);

/* ── Filter tabs ── */
const activeFilter = ref<string>('all');

const filterTabs = computed(() => [
  { id: 'all',       label: 'All',       icon: 'fas fa-inbox',                count: notifications.value.length },
  { id: 'unread',    label: 'Unread',    icon: 'fas fa-circle',               count: unreadCount.value },
  { id: 'orders',    label: 'Orders',    icon: 'fas fa-bag-shopping',         count: notifications.value.filter(n => n.type === 'order').length },
  { id: 'alerts',    label: 'Alerts',    icon: 'fas fa-triangle-exclamation', count: alertCount.value },
  { id: 'payments',  label: 'Payments',  icon: 'fas fa-credit-card',          count: notifications.value.filter(n => n.type === 'payment').length },
  { id: 'customers', label: 'Customers', icon: 'fas fa-user-circle',          count: notifications.value.filter(n => n.type === 'customer').length },
]);

const filtered = computed(() => {
  let list = notifications.value;
  if (activeFilter.value === 'unread')    list = list.filter(n => !n.read);
  else if (activeFilter.value === 'orders')    list = list.filter(n => n.type === 'order');
  else if (activeFilter.value === 'alerts')    list = list.filter(n => n.type === 'alert');
  else if (activeFilter.value === 'payments')  list = list.filter(n => n.type === 'payment');
  else if (activeFilter.value === 'customers') list = list.filter(n => n.type === 'customer');
  if (searchQuery.value.trim()) {
    const q = searchQuery.value.toLowerCase();
    list = list.filter(n =>
      n.title.toLowerCase().includes(q) || n.message.toLowerCase().includes(q)
    );
  }
  return list;
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
function markRead(n: Notification) { n.read = true; }

function markAllRead() {
  const count = notifications.value.filter(n => !n.read).length;
  notifications.value.forEach(n => { n.read = true; });
  toast.success(`${count} notification${count !== 1 ? 's' : ''} marked as read`);
}

function dismiss(n: Notification) {
  notifications.value.splice(notifications.value.findIndex(x => x.id === n.id), 1);
}

async function clearAll() {
  const ok = await confirm({
    title:       'Clear all notifications',
    message:     `Remove all ${notifications.value.length} notifications?`,
    detail:      'This cannot be undone.',
    confirmText: 'Clear all',
    variant:     'danger',
  });
  if (ok) {
    const count = notifications.value.length;
    notifications.value = [];
    toast.success(`${count} notifications cleared`);
  }
}

/* ── Bulk select ── */
function toggleBulkMode() {
  bulkMode.value = !bulkMode.value;
  if (!bulkMode.value) selectedIds.value.clear();
}

function toggleSelect(id: number) {
  const s = new Set(selectedIds.value);
  if (s.has(id)) s.delete(id); else s.add(id);
  selectedIds.value = s;
}

function bulkMarkRead() {
  const count = selectedIds.value.size;
  notifications.value.forEach(n => { if (selectedIds.value.has(n.id)) n.read = true; });
  selectedIds.value = new Set();
  toast.success(`${count} notification${count !== 1 ? 's' : ''} marked as read`);
}

function bulkDismiss() {
  const count = selectedIds.value.size;
  notifications.value = notifications.value.filter(n => !selectedIds.value.has(n.id));
  selectedIds.value = new Set();
  bulkMode.value = false;
  toast.success(`${count} notification${count !== 1 ? 's' : ''} deleted`);
}
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

/* Selected row highlight */
.notif-row.is-selected {
  background: rgba(var(--accent-rgb), 0.08) !important;
  border-left: 3px solid var(--accent);
}

/* Bulk-select checkbox */
.notif-checkbox {
  width: 16px;
  height: 16px;
  border-radius: 4px;
  border: 1.5px solid var(--glass-border);
  background: var(--glass-bg);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.15s;
  color: #fff;
}
.notif-checkbox--checked {
  background: var(--accent);
  border-color: var(--accent);
}

/* Notification preference toggle */
.notif-toggle {
  position: relative;
  width: 36px;
  height: 20px;
  border-radius: 999px;
  background: var(--glass-border);
  border: none;
  cursor: pointer;
  transition: background 0.2s;
  flex-shrink: 0;
}
.notif-toggle--on { background: var(--accent); }
.notif-toggle-knob {
  position: absolute;
  top: 3px;
  left: 3px;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: #fff;
  box-shadow: 0 1px 3px rgba(0,0,0,0.2);
  transition: transform 0.2s cubic-bezier(0.34,1.56,0.64,1);
}
.notif-toggle--on .notif-toggle-knob { transform: translateX(16px); }

/* Preferences panel slide */
.prefs-slide-enter-active { transition: all 0.25s ease; }
.prefs-slide-leave-active { transition: all 0.2s ease; }
.prefs-slide-enter-from,
.prefs-slide-leave-to {
  opacity: 0;
  transform: translateY(-8px) scaleY(0.96);
  transform-origin: top;
}
</style>
