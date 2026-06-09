<template>
  <!-- Login page has its own full-screen layout — no sidebar/nav -->
  <div v-if="isPublicRoute" class="h-screen relative overflow-hidden">
    <div class="wallpaper fixed inset-0 -z-10"></div>
    <router-view />
    <ToastContainer />
    <ConfirmDialog />
  </div>

  <!-- Authenticated shell -->
  <div v-else id="app" class="h-screen flex relative overflow-hidden">
    <div class="wallpaper fixed inset-0 -z-10"></div>

    <Sidebar :dark="isDark" @toggle-dark="toggleDark" />

    <div class="flex-1 flex flex-col h-screen overflow-hidden">
      <main class="flex-1 p-7 overflow-y-auto">
        <Breadcrumb v-if="!isDashboardRoute" :items="breadcrumbs" class="mb-5" />

        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>

      <Footer />
    </div>

    <!-- Global overlays -->
    <LoadingSpinner v-if="isNavigating" />
    <ToastContainer />
    <ConfirmDialog />
    <KeyboardShortcutsOverlay />
    <CommandPalette />
  </div>
</template>

<script setup lang="ts">
import LoadingSpinner           from './components/shared/LoadingSpinner.vue';
import ToastContainer           from './components/ui/ToastContainer.vue';
import ConfirmDialog            from './components/ui/ConfirmDialog.vue';
import KeyboardShortcutsOverlay from './components/ui/KeyboardShortcutsOverlay.vue';
import CommandPalette           from './components/ui/CommandPalette.vue';
import Sidebar                  from './components/layout/Sidebar.vue';
import Breadcrumb               from './components/layout/Breadcrumb.vue';
import Footer                   from './components/layout/Footer.vue';
import { computed, ref, watch, onMounted } from 'vue';
import { useRoute, useRouter }  from 'vue-router';
import { useTheme }             from '@/composables/useTheme';
import { useKeyboardShortcuts } from '@/composables/useKeyboardShortcuts';

const route  = useRoute();
const router = useRouter();

/* ── Route helpers ──────────────────────────────────────────────────────── */
const isPublicRoute    = computed(() => !!route.meta.public || route.name === 'NotFound');
const isDashboardRoute = computed(() => route.path === '/dashboard' || route.path === '/');

/* ── Global loading state — shown during route transitions ─────────────── */
const isNavigating = ref(false);
router.beforeEach(() => { isNavigating.value = true;  });
router.afterEach(()  => { isNavigating.value = false; });

/* ── Theme / dark mode ──────────────────────────────────────────────────── */
const { isDark, toggleDark, init: initTheme } = useTheme();

/* ── Keyboard shortcuts ─────────────────────────────────────────────────── */
const { registerShortcut, toggleHelp, hideHelp } = useKeyboardShortcuts();

onMounted(() => {
  initTheme();

  // Navigation shortcuts
  registerShortcut({ id: 'goto-dashboard', key: 'd', meta: true,  description: 'Go to Dashboard', group: 'Navigation', handler: () => router.push('/dashboard') });
  registerShortcut({ id: 'goto-products',  key: 'p', meta: true,  description: 'Go to Products',  group: 'Navigation', handler: () => router.push('/products')  });
  registerShortcut({ id: 'goto-orders',    key: 'o', meta: true,  description: 'Go to Orders',    group: 'Navigation', handler: () => router.push('/orders')    });
  registerShortcut({ id: 'goto-customers', key: 'u', meta: true,  description: 'Go to Customers', group: 'Navigation', handler: () => router.push('/customers') });

  // Context-aware create
  registerShortcut({
    id: 'new-item', key: 'n', meta: false,
    description: 'New item (context-aware)', group: 'Create',
    handler: () => {
      if      (route.path.startsWith('/products'))   router.push('/products/create');
      else if (route.path.startsWith('/orders'))     router.push('/orders/create');
      else if (route.path.startsWith('/customers'))  router.push('/customers/create');
      else if (route.path.startsWith('/discounts'))  router.push('/discounts/create');
      else if (route.path.startsWith('/categories')) router.push('/categories/create');
      else router.push('/products/create');
    },
  });

  registerShortcut({ id: 'toggle-dark', key: 't',      meta: false, description: 'Toggle dark mode',       group: 'General', handler: () => toggleDark()  });
  registerShortcut({ id: 'show-help',   key: '?',      meta: false, description: 'Show keyboard shortcuts', group: 'General', handler: () => toggleHelp()  });
  registerShortcut({ id: 'escape',      key: 'Escape', meta: false, description: 'Close overlay / go back', group: 'General', handler: () => hideHelp()    });
});

/* ── Breadcrumbs with real record names ─────────────────────────────────── */
// Storage for dynamic segment names resolved from route params
const recordLabel = ref('');

watch(
  () => route.fullPath,
  () => {
    const name = route.name as string;
    const id   = route.params.id ?? route.params.orderId;

    if      (name === 'ProductDetail') recordLabel.value = `Product #${id}`;
    else if (name === 'EditProduct')   recordLabel.value = `Product #${id}`;
    else if (name === 'OrderDetails')  recordLabel.value = `Order #${id}`;
    else if (name === 'EditOrder')     recordLabel.value = `Order #${id}`;
    else if (name === 'CustomerDetail')recordLabel.value = `Customer #${id}`;
    else if (name === 'EditCustomer')  recordLabel.value = `Customer #${id}`;
    else                               recordLabel.value = '';
  },
  { immediate: true },
);

const breadcrumbs = computed(() => {
  const items: { path: string; name: string }[] = [];
  const cr = router.currentRoute.value;

  // Always start with Dashboard
  if (!isDashboardRoute.value) {
    items.push({ path: '/dashboard', name: 'Dashboard' });
  }

  // Optional parent breadcrumb
  if (cr.meta.parent) {
    const parent = cr.meta.parent as { path: string; name: string };
    let parentPath = parent.path;
    if (parentPath.includes(':id') && route.params.id) {
      parentPath = parentPath.replace(':id', route.params.id as string);
    }
    if (parentPath.includes(':orderId') && route.params.orderId) {
      parentPath = parentPath.replace(':orderId', route.params.orderId as string);
    }
    items.push({ path: parentPath, name: parent.name });
  }

  // Current page — use resolved record label when available
  const label = recordLabel.value || (cr.meta.breadcrumb as string);
  if (label && label !== 'Dashboard' && !items.some(i => i.name === label)) {
    items.push({ path: cr.path, name: label });
  }

  return items;
});
</script>
