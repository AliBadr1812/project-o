<template>
  <Teleport to="body">
    <Transition name="cp-backdrop">
      <div v-if="open"
        class="fixed inset-0 z-[10002] flex items-start justify-center pt-[12vh]"
        style="background: rgba(0,0,0,0.6); backdrop-filter: blur(4px);"
        @click.self="close">

        <Transition name="cp-panel" appear>
          <div v-if="open"
            class="w-full max-w-2xl mx-4 rounded-2xl overflow-hidden shadow-2xl"
            style="background: var(--glass-bg-strong); border: 1px solid var(--glass-border);
                   box-shadow: 0 24px 80px rgba(0,0,0,0.4);">

            <!-- Search input -->
            <div class="flex items-center gap-3 px-4 py-3.5"
              style="border-bottom: 1px solid var(--glass-border);">
              <i class="fas fa-magnifying-glass text-sm" style="color: var(--text-muted);"></i>
              <input
                ref="inputRef"
                v-model="query"
                type="text"
                placeholder="Search products, orders, customers… or type a command"
                class="flex-1 bg-transparent outline-none text-[15px]"
                style="color: var(--text-primary);"
                @keydown.escape="close"
                @keydown.arrow-down.prevent="moveSelection(1)"
                @keydown.arrow-up.prevent="moveSelection(-1)"
                @keydown.enter.prevent="activateSelected"
              />
              <div class="flex items-center gap-1">
                <kbd class="ks-key text-[10px]">Esc</kbd>
              </div>
            </div>

            <!-- Results / default commands -->
            <div class="overflow-y-auto" style="max-height: 420px;">

              <!-- Quick commands (shown when query is empty) -->
              <template v-if="!query.trim()">
                <div class="px-4 pt-3 pb-1">
                  <p class="text-[11px] font-semibold uppercase tracking-widest mb-1"
                    style="color: var(--text-muted);">Quick Navigation</p>
                </div>
                <div v-for="(cmd, idx) in quickCommands" :key="cmd.id"
                  @click="activateItem(cmd)"
                  @mouseenter="selectedIndex = idx"
                  class="cp-row flex items-center gap-3 px-4 py-2.5 cursor-pointer"
                  :class="{ 'is-selected': selectedIndex === idx }">
                  <div class="w-8 h-8 rounded-xl flex items-center justify-center flex-shrink-0 text-xs"
                    :style="{ background: cmd.iconBg, color: cmd.iconColor }">
                    <i :class="cmd.icon"></i>
                  </div>
                  <div class="flex-1 min-w-0">
                    <p class="text-sm font-medium" style="color: var(--text-primary);">{{ cmd.label }}</p>
                    <p v-if="cmd.description" class="text-xs" style="color: var(--text-muted);">{{ cmd.description }}</p>
                  </div>
                  <kbd v-if="cmd.shortcut" class="ks-key text-[10px]">{{ cmd.shortcut }}</kbd>
                </div>
                <div class="px-4 pb-3"></div>
              </template>

              <!-- Search results -->
              <template v-else>
                <div v-if="isSearching" class="py-10 text-center">
                  <i class="fas fa-spinner fa-spin text-lg mb-2" style="color: var(--text-muted);"></i>
                  <p class="text-sm" style="color: var(--text-muted);">Searching…</p>
                </div>

                <template v-else-if="allResults.length > 0">
                  <div v-for="group in groupedResults" :key="group.type">
                    <div class="px-4 pt-3 pb-1">
                      <p class="text-[11px] font-semibold uppercase tracking-widest"
                        style="color: var(--text-muted);">{{ group.label }}</p>
                    </div>
                    <div v-for="item in group.items" :key="item.id"
                      @click="activateItem(item)"
                      @mouseenter="selectedIndex = getGlobalIndex(item)"
                      class="cp-row flex items-center gap-3 px-4 py-2.5 cursor-pointer"
                      :class="{ 'is-selected': selectedIndex === getGlobalIndex(item) }">
                      <div class="w-8 h-8 rounded-xl flex items-center justify-center flex-shrink-0 text-xs"
                        :style="{ background: item.iconBg, color: item.iconColor }">
                        <i :class="item.icon"></i>
                      </div>
                      <div class="flex-1 min-w-0">
                        <p class="text-sm font-medium truncate" style="color: var(--text-primary);"
                          v-html="highlight(item.label)"></p>
                        <p v-if="item.meta" class="text-xs truncate" style="color: var(--text-muted);">{{ item.meta }}</p>
                      </div>
                      <span v-if="item.badge" class="badge badge-info text-[10px]">{{ item.badge }}</span>
                    </div>
                  </div>
                </template>

                <div v-else class="py-12 text-center">
                  <i class="fas fa-face-frown text-2xl mb-3" style="color: var(--text-muted);"></i>
                  <p class="text-sm font-medium" style="color: var(--text-primary);">No results for "{{ query }}"</p>
                  <p class="text-xs mt-1" style="color: var(--text-muted);">Try a product name, order number, or customer email.</p>
                </div>
              </template>

            </div>

            <!-- Footer hints -->
            <div class="flex items-center gap-4 px-4 py-2.5 text-[11px]"
              style="border-top: 1px solid var(--glass-border); color: var(--text-muted);">
              <span><kbd class="ks-key mr-1">↑↓</kbd> Navigate</span>
              <span><kbd class="ks-key mr-1">↵</kbd> Open</span>
              <span><kbd class="ks-key mr-1">Esc</kbd> Close</span>
              <span class="ml-auto">{{ allResults.length > 0 ? allResults.length + ' results' : '' }}</span>
            </div>
          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { useProductStore  } from '@/stores/productStore';
import { useOrderStore    } from '@/stores/orderStore';
import { useCustomerStore } from '@/stores/customerStore';
import { useKeyboardShortcuts } from '@/composables/useKeyboardShortcuts';
import { useCommandPalette }    from '@/composables/useCommandPalette';
import { formatCurrency } from '@/utils/formatters';

const router = useRouter();
const { registerShortcut } = useKeyboardShortcuts();

const productStore  = useProductStore();
const orderStore    = useOrderStore();
const customerStore = useCustomerStore();

const { items: products  } = storeToRefs(productStore);
const { items: orders    } = storeToRefs(orderStore);
const { items: customers } = storeToRefs(customerStore);

// ── State ──────────────────────────────────────────────────────────────────
// `open` lives in the shared composable so the sidebar (and any other component)
// can also trigger the palette without prop-drilling or events.
const { open, openPalette: _open, closePalette } = useCommandPalette();
const query         = ref('');
const selectedIndex = ref(0);
const isSearching   = ref(false);
const inputRef      = ref<HTMLInputElement | null>(null);

// ── Register ⌘K shortcut ──────────────────────────────────────────────────
const unregister = registerShortcut({
  id: 'command-palette', key: 'k', meta: true,
  description: 'Open command palette',
  group: 'General',
  handler: () => toggle(),
});
onUnmounted(() => unregister());

// ── Open / close ──────────────────────────────────────────────────────────
function toggle() {
  open.value ? close() : openPalette();
}
function openPalette() {
  _open();
  query.value = '';
  selectedIndex.value = 0;
  // Ensure stores are loaded
  if (!products.value.length)  productStore.fetchAll();
  if (!orders.value.length)    orderStore.fetchAll();
  if (!customers.value.length) customerStore.fetchAll();
  nextTick(() => inputRef.value?.focus());
}
function close() {
  closePalette();
  query.value = '';
}

// ── Quick commands (shown when query is empty) ────────────────────────────
const quickCommands = [
  { id: 'nav-dashboard', label: 'Dashboard',       description: 'Overview & analytics',  icon: 'fas fa-chart-pie',    iconBg: 'var(--ni-purple-bg)', iconColor: 'var(--ni-purple)', shortcut: null, action: () => router.push('/dashboard')  },
  { id: 'nav-products',  label: 'Products',        description: 'Manage your catalogue',  icon: 'fas fa-box',          iconBg: 'var(--ni-orange-bg)', iconColor: 'var(--ni-orange)', shortcut: null, action: () => router.push('/products')   },
  { id: 'nav-orders',    label: 'Orders',          description: 'View & process orders',  icon: 'fas fa-bag-shopping', iconBg: 'var(--ni-blue-bg)',   iconColor: 'var(--ni-blue)',   shortcut: null, action: () => router.push('/orders')     },
  { id: 'nav-customers', label: 'Customers',       description: 'Manage customer accounts',icon: 'fas fa-users',       iconBg: 'var(--ni-teal-bg)',   iconColor: 'var(--ni-teal)',   shortcut: null, action: () => router.push('/customers')  },
  { id: 'new-product',   label: 'New Product',     description: 'Add a product to your store', icon: 'fas fa-plus',   iconBg: 'var(--ni-green-bg)',  iconColor: 'var(--ni-green)',  shortcut: 'N',  action: () => router.push('/products/create') },
  { id: 'new-discount',  label: 'New Discount',    description: 'Create a coupon code',   icon: 'fas fa-ticket',       iconBg: 'var(--ni-red-bg)',    iconColor: 'var(--ni-red)',    shortcut: null, action: () => router.push('/discounts/create') },
];

// ── Search results ────────────────────────────────────────────────────────
interface ResultItem {
  id:        string;
  type:      string;
  label:     string;
  meta?:     string;
  badge?:    string;
  icon:      string;
  iconBg:    string;
  iconColor: string;
  action:    () => void;
}

const allResults = computed<ResultItem[]>(() => {
  const q = query.value.toLowerCase().trim();
  if (!q) return [];

  const results: ResultItem[] = [];

  // Products
  for (const p of products.value) {
    if (p.name.toLowerCase().includes(q) || (p.sku ?? '').toLowerCase().includes(q)) {
      results.push({
        id:        `product-${p.id}`,
        type:      'product',
        label:     p.name,
        meta:      `${formatCurrency(p.price)} · ${p.stock} units · ${p.categories || 'No category'}`,
        badge:     p.status === 'active' ? undefined : p.status,
        icon:      'fas fa-box',
        iconBg:    'var(--ni-orange-bg)',
        iconColor: 'var(--ni-orange)',
        action:    () => router.push(`/products/${p.id}`),
      });
    }
  }

  // Orders
  for (const o of orders.value) {
    const oNum = (o.orderNumber ?? '').toLowerCase();
    const cName = (o.customerName ?? '').toLowerCase();
    const cEmail = (o.customerEmail ?? '').toLowerCase();
    if (oNum.includes(q) || cName.includes(q) || cEmail.includes(q)) {
      results.push({
        id:        `order-${o.id}`,
        type:      'order',
        label:     `Order #${o.orderNumber}`,
        meta:      `${o.customerName} · ${formatCurrency(o.total)} · ${o.status}`,
        badge:     undefined,
        icon:      'fas fa-bag-shopping',
        iconBg:    'var(--ni-blue-bg)',
        iconColor: 'var(--ni-blue)',
        action:    () => router.push(`/orders/${o.id}`),
      });
    }
  }

  // Customers
  for (const c of customers.value) {
    const name  = (c.fullName  ?? '').toLowerCase();
    const email = (c.email     ?? '').toLowerCase();
    const phone = (c.phone     ?? '').toLowerCase();
    if (name.includes(q) || email.includes(q) || phone.includes(q)) {
      results.push({
        id:        `customer-${c.id}`,
        type:      'customer',
        label:     c.fullName,
        meta:      `${c.email} · ${c.orderCount} orders`,
        badge:     c.type === 'vip' ? 'VIP' : undefined,
        icon:      'fas fa-user-circle',
        iconBg:    'var(--ni-teal-bg)',
        iconColor: 'var(--ni-teal)',
        action:    () => router.push(`/customers/${c.id}`),
      });
    }
  }

  return results.slice(0, 30);
});

const groupedResults = computed(() => {
  const groups: { type: string; label: string; items: ResultItem[] }[] = [];
  const typeLabel: Record<string, string> = {
    product: 'Products', order: 'Orders', customer: 'Customers',
  };
  const seen = new Set<string>();
  for (const r of allResults.value) {
    if (!seen.has(r.type)) {
      seen.add(r.type);
      groups.push({ type: r.type, label: typeLabel[r.type] ?? r.type, items: [] });
    }
    groups.find(g => g.type === r.type)!.items.push(r);
  }
  return groups;
});

function getGlobalIndex(item: ResultItem): number {
  return allResults.value.findIndex(r => r.id === item.id);
}

// ── Keyboard navigation ───────────────────────────────────────────────────
const flatItems = computed<Array<ResultItem | typeof quickCommands[0]>>(() =>
  query.value.trim() ? allResults.value : quickCommands,
);

function moveSelection(dir: 1 | -1) {
  const len = flatItems.value.length;
  if (!len) return;
  selectedIndex.value = (selectedIndex.value + dir + len) % len;
}

function activateSelected() {
  const item = flatItems.value[selectedIndex.value];
  if (item) activateItem(item as any);
}

function activateItem(item: { action: () => void }) {
  close();
  nextTick(() => item.action());
}

// Reset selection when query changes
watch(query, () => { selectedIndex.value = 0; });

// ── Search highlight ──────────────────────────────────────────────────────
function highlight(text: string): string {
  const q = query.value.trim();
  if (!q) return text;
  const idx = text.toLowerCase().indexOf(q.toLowerCase());
  if (idx === -1) return text;
  return (
    text.slice(0, idx) +
    `<mark style="background:rgba(var(--accent-rgb),0.25);color:inherit;border-radius:2px;">${text.slice(idx, idx + q.length)}</mark>` +
    text.slice(idx + q.length)
  );
}
</script>

<style scoped>
/* Row highlight */
.cp-row { transition: background 0.1s; }
.cp-row.is-selected,
.cp-row:hover { background: rgba(var(--accent-rgb), 0.07); }

/* Keyboard key chips */
.ks-key {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 20px;
  height: 20px;
  padding: 0 5px;
  border-radius: 5px;
  font-family: ui-monospace, monospace;
  font-size: 10px;
  font-weight: 600;
  background: var(--glass-bg);
  border: 1px solid var(--glass-border);
  box-shadow: inset 0 1px 0 rgba(255,255,255,0.25);
  color: var(--text-secondary);
}

/* Backdrop transition */
.cp-backdrop-enter-active,
.cp-backdrop-leave-active { transition: opacity 0.2s ease; }
.cp-backdrop-enter-from,
.cp-backdrop-leave-to    { opacity: 0; }

/* Panel transition */
.cp-panel-enter-active  { transition: opacity 0.2s ease, transform 0.25s cubic-bezier(0.34,1.56,0.64,1); }
.cp-panel-leave-active  { transition: opacity 0.15s ease, transform 0.15s ease; }
.cp-panel-enter-from    { opacity: 0; transform: scale(0.94) translateY(-20px); }
.cp-panel-leave-to      { opacity: 0; transform: scale(0.97) translateY(-8px); }
</style>
