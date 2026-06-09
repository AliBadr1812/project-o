<template>
  <aside
    class="glass-panel w-[270px] h-screen flex-shrink-0 relative z-10 flex flex-col overflow-x-hidden"
    style="border-right: 1px solid var(--glass-border);
           box-shadow: inset -1px 0 0 rgba(255,255,255,0.35), 1px 0 20px rgba(140,100,200,0.06);"
  >
    <!-- ── Fixed top: brand + search ─────────────────────────────────── -->
    <div class="flex-shrink-0 px-3.5 pt-7">
      <!-- Brand -->
      <div class="flex items-center gap-3 px-2.5 pb-6">
        <div
          class="w-[42px] h-[42px] rounded-[14px] flex items-center justify-center flex-shrink-0 text-white/90 text-[17px]"
          style="background: linear-gradient(145deg, #b97fff, #7c5ef0, #5e5ce6);
                 box-shadow: inset 0 1px 0 rgba(255,255,255,0.4), 0 4px 16px rgba(124,94,240,0.5);"
        >
          <i class="fas fa-store"></i>
        </div>
        <div>
          <div class="text-[17px] font-bold tracking-tight" style="color: var(--text-primary);">ShopAdmin</div>
          <div class="text-[11px] mt-px" style="color: var(--text-muted);">Commerce Dashboard</div>
        </div>
      </div>

      <!-- Search — opens command palette -->
      <button
        class="sidebar-search w-full flex items-center gap-2.5 px-3 py-2.5 rounded-xl mb-3 text-left transition-colors"
        style="cursor: pointer;"
        @click="openPalette"
      >
        <i class="fas fa-magnifying-glass text-xs" style="color: var(--text-muted);"></i>
        <span class="flex-1 text-[13px]" style="color: var(--text-muted);">Search…</span>
        <span class="flex items-center gap-0.5">
          <kbd class="sidebar-kbd">⌘</kbd>
          <kbd class="sidebar-kbd">K</kbd>
        </span>
      </button>
    </div>

    <!-- ── Scrollable nav ─────────────────────────────────────────────── -->
    <div class="flex-1 min-h-0 overflow-y-auto px-3.5 py-2 sidebar-scroll">

      <!-- Nav: Overview -->
      <div class="text-[11px] font-semibold px-2.5 pb-1 tracking-wide uppercase" style="color: var(--text-muted);">Overview</div>

      <nav class="flex flex-col gap-0.5 mb-2">
        <router-link
          v-for="item in overviewItems"
          :key="item.path"
          :to="item.path"
          class="glass-nav-item"
        >
          <div class="nav-icon" :class="item.iconColor">
            <i :class="item.icon"></i>
          </div>
          <span class="flex-1">{{ item.name }}</span>
          <span v-if="item.badge"
            class="badge"
            :class="`badge-${item.badge.variant}`"
            style="font-size:10px; padding: 2px 7px;"
          >{{ item.badge.text }}</span>
        </router-link>
      </nav>

      <!-- Nav: Commerce -->
      <div class="text-[11px] font-semibold px-2.5 pb-1 pt-2 tracking-wide uppercase" style="color: var(--text-muted);">Commerce</div>

      <nav class="flex flex-col gap-0.5 mb-2">
        <router-link
          v-for="item in commerceItems"
          :key="item.path"
          :to="item.path"
          class="glass-nav-item"
        >
          <div class="nav-icon" :class="item.iconColor">
            <i :class="item.icon"></i>
          </div>
          <span class="flex-1">{{ item.name }}</span>
          <span v-if="item.badge"
            class="badge"
            :class="`badge-${item.badge.variant}`"
            style="font-size:10px; padding: 2px 7px;"
          >{{ item.badge.text }}</span>
        </router-link>
      </nav>

      <div class="glass-divider"></div>

      <!-- Settings -->
      <nav class="flex flex-col gap-0.5">
        <router-link
          v-for="item in settingsItems"
          :key="item.path"
          :to="item.path"
          class="glass-nav-item"
        >
          <div class="nav-icon" :class="item.iconColor">
            <i :class="item.icon"></i>
          </div>
          <span class="flex-1">{{ item.name }}</span>
        </router-link>
      </nav>

    </div>

    <!-- ── Fixed bottom: controls + user card ────────────────────────── -->
    <div class="flex-shrink-0 px-3.5 py-7 flex flex-col gap-2.5">

      <!-- Notification + Dark mode + Quick-add row -->
      <div class="flex items-center gap-2 px-1">
        <!-- Notifications — plain button, no nested anchor -->
        <button
          class="btn-glass-icon relative flex-1 rounded-xl"
          style="height: 38px;"
          title="Notifications"
          @click="router.push('/notifications')"
        >
          <i class="fas fa-bell text-sm"></i>
          <!-- Live unread badge — hidden when 0 -->
          <span
            v-if="unreadCount > 0"
            class="notif-badge"
          >{{ unreadCount > 9 ? '9+' : unreadCount }}</span>
        </button>

        <!-- Dark mode toggle -->
        <button
          @click="toggleDark()"
          class="btn-glass-icon flex-1 rounded-xl"
          style="height: 38px;"
          :title="isDark ? 'Switch to light mode' : 'Switch to dark mode'"
        >
          <i :class="isDark ? 'fas fa-sun' : 'fas fa-moon'" class="text-sm"></i>
        </button>

        <!-- Context-aware quick-add -->
        <button
          class="btn-glass-icon flex-1 rounded-xl"
          style="height: 38px;"
          :title="quickAddLabel"
          @click="router.push(quickAddPath)"
        >
          <i class="fas fa-plus text-sm"></i>
        </button>
      </div>

      <!-- User card -->
      <div class="sidebar-user-card flex items-center gap-2.5 p-3 rounded-2xl transition-all duration-200"
           style="cursor: pointer;"
           @click="router.push('/settings/profile')">
        <div
          class="w-9 h-9 rounded-full flex items-center justify-center text-sm font-bold flex-shrink-0"
          style="background: linear-gradient(145deg, #c084fc, #7c3aed);
                 color: #fff;
                 box-shadow: 0 2px 8px rgba(124,58,237,0.4), inset 0 1px 0 rgba(255,255,255,0.3);"
        >
          {{ userInitial }}
        </div>
        <div class="flex-1 min-w-0">
          <div class="text-[13px] font-semibold truncate" style="color: var(--text-primary);">{{ userName }}</div>
          <div class="text-[11px] truncate" style="color: var(--text-muted);">{{ userRoleLabel }}</div>
        </div>
        <!-- Logout button -->
        <button
          class="btn-glass-icon w-7 h-7 rounded-lg text-xs flex-shrink-0"
          title="Sign out"
          @click.stop="handleLogout"
        >
          <i class="fas fa-right-from-bracket"></i>
        </button>
      </div>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { NAVIGATION_ITEMS } from '@/utils/constants';
import { useTheme } from '@/composables/useTheme';
import { useCommandPalette } from '@/composables/useCommandPalette';
import { useNotifications } from '@/composables/useNotifications';
import { useAuth } from '@/composables/useAuth';

defineProps<{ dark: boolean }>();
defineEmits(['toggle-dark']);

const route  = useRoute();
const router = useRouter();

const { isDark, toggleDark } = useTheme();
const { openPalette } = useCommandPalette();
const { unreadCount } = useNotifications();
const { user, logout } = useAuth();

const userName      = computed(() => user.value?.name ?? 'Admin');
const userInitial   = computed(() => userName.value.charAt(0).toUpperCase());
const userRoleLabel = computed(() => {
  const r = user.value?.role?.toLowerCase() ?? 'admin';
  return r.charAt(0).toUpperCase() + r.slice(1);
});

function handleLogout() {
  logout();
  router.push('/login');
}

const overviewItems  = computed(() => NAVIGATION_ITEMS.filter(i => ['Dashboard', 'Analytics'].includes(i.name)));
const commerceItems  = computed(() => NAVIGATION_ITEMS.filter(i => ['Products', 'Orders', 'Customers', 'Categories', 'Inventory Alerts', 'Discounts', 'Returns', 'Segments'].includes(i.name)));
const settingsItems  = computed(() => NAVIGATION_ITEMS.filter(i => ['Settings', 'Audit Log'].includes(i.name)));

// ── Context-aware quick-add ──────────────────────────────────────────────
const quickAddConfig: Record<string, { path: string; label: string }> = {
  '/products':   { path: '/products/create',    label: 'New product'  },
  '/orders':     { path: '/orders/create',       label: 'New order'    },
  '/customers':  { path: '/customers/create',    label: 'New customer' },
  '/discounts':  { path: '/discounts/create',    label: 'New discount' },
  '/categories': { path: '/categories/create',   label: 'New category' },
};

const quickAddPath = computed(() => {
  for (const prefix of Object.keys(quickAddConfig)) {
    if (route.path.startsWith(prefix)) return quickAddConfig[prefix]!.path;
  }
  return '/products/create';
});

const quickAddLabel = computed(() => {
  for (const prefix of Object.keys(quickAddConfig)) {
    if (route.path.startsWith(prefix)) return quickAddConfig[prefix]!.label;
  }
  return 'New product';
});
</script>

<style scoped>
/* Search button hover */
.sidebar-search:hover { background: rgba(var(--accent-rgb), 0.07); }

/* ⌘K key chips */
.sidebar-kbd {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 18px;
  padding: 0 4px;
  border-radius: 4px;
  font-family: ui-monospace, monospace;
  font-size: 10px;
  font-weight: 600;
  background: var(--glass-bg);
  border: 1px solid var(--glass-border);
  color: var(--text-muted);
  box-shadow: inset 0 1px 0 rgba(255,255,255,0.2);
}

/* Notification unread badge */
.notif-badge {
  position: absolute;
  top: 5px;
  right: 5px;
  min-width: 16px;
  height: 16px;
  padding: 0 3px;
  border-radius: 999px;
  background: #dc2626;
  color: #fff;
  font-size: 9px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 0 0 2px var(--glass-panel, #fff);
  pointer-events: none;
}

/* Thin, subtle scrollbar for the nav region */
.sidebar-scroll {
  scrollbar-width: thin;
  scrollbar-color: var(--scrollbar-thumb, rgba(124,94,240,0.28)) transparent;
}
.sidebar-scroll::-webkit-scrollbar       { width: 4px; }
.sidebar-scroll::-webkit-scrollbar-track { background: transparent; }
.sidebar-scroll::-webkit-scrollbar-thumb {
  background: var(--scrollbar-thumb, rgba(124,94,240,0.28));
  border-radius: 9999px;
}
.sidebar-scroll::-webkit-scrollbar-thumb:hover { background: var(--accent, #7c3aed); }
</style>
