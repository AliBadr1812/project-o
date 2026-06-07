<template>
  <aside
    class="glass-panel w-[270px] h-screen flex-shrink-0 relative z-10 flex flex-col py-7 px-3.5 overflow-hidden"
    style="border-right: 1px solid var(--glass-border);
           box-shadow: inset -1px 0 0 rgba(255,255,255,0.35), 1px 0 20px rgba(140,100,200,0.06);"
  >
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

    <!-- Search -->
    <div class="sidebar-search flex items-center gap-2.5 px-3 py-2.5 rounded-xl mb-5">
      <i class="fas fa-magnifying-glass text-xs" style="color: var(--text-muted);"></i>
      <span class="text-[13px]" style="color: var(--text-muted);">Search…</span>
    </div>

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

    <!-- Bottom controls -->
    <div class="mt-auto flex flex-col gap-2.5">

      <!-- Notification + Dark mode row -->
      <div class="flex items-center gap-2 px-1">
        <!-- Notifications -->
        <router-link to="/notifications" class="flex-1">
          <button
            class="btn-glass-icon relative w-full rounded-xl"
            style="height: 38px;"
            title="Notifications"
          >
            <i class="fas fa-bell text-sm"></i>
            <span
              class="absolute top-1.5 right-1.5 w-2 h-2 rounded-full"
              style="background: #dc2626; box-shadow: 0 0 4px rgba(220,38,38,0.7);"
            ></span>
          </button>
        </router-link>

        <!-- Dark mode toggle -->
        <button
          @click="toggleDark()"
          class="btn-glass-icon flex-1 rounded-xl"
          style="height: 38px;"
          :title="isDark ? 'Switch to light mode' : 'Switch to dark mode'"
        >
          <i :class="isDark ? 'fas fa-sun' : 'fas fa-moon'" class="text-sm"></i>
        </button>

        <!-- New item quick add -->
        <router-link to="/products/create" class="flex-1">
          <button
            class="btn-glass-icon w-full rounded-xl"
            style="height: 38px;"
            title="Add new product"
          >
            <i class="fas fa-plus text-sm"></i>
          </button>
        </router-link>
      </div>

      <!-- User card -->
      <router-link to="/settings/profile">
        <div class="sidebar-user-card flex items-center gap-2.5 p-3 rounded-2xl transition-all duration-200">
          <div
            class="w-9 h-9 rounded-full flex items-center justify-center text-sm font-bold flex-shrink-0"
            style="background: linear-gradient(145deg, #c084fc, #7c3aed);
                   color: #fff;
                   box-shadow: 0 2px 8px rgba(124,58,237,0.4), inset 0 1px 0 rgba(255,255,255,0.3);"
          >
            A
          </div>
          <div class="flex-1 min-w-0">
            <div class="text-[13px] font-semibold truncate" style="color: var(--text-primary);">Ali</div>
            <div class="text-[11px] truncate" style="color: var(--text-muted);">Administrator</div>
          </div>
          <div
            class="w-2 h-2 rounded-full flex-shrink-0"
            style="background: var(--online-dot); box-shadow: 0 0 6px rgba(48,209,88,0.7);"
          ></div>
        </div>
      </router-link>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { NAVIGATION_ITEMS } from '@/utils/constants';
import { useTheme } from '@/composables/useTheme';

// Keep the prop/emit for backwards-compat with App.vue but drive the icon
// from the composable's isDark so Settings can also change it.
defineProps<{ dark: boolean }>();
defineEmits(['toggle-dark']);

const { isDark, toggleDark } = useTheme();

const overviewItems  = computed(() => NAVIGATION_ITEMS.filter(i => ['Dashboard', 'Analytics'].includes(i.name)));
const commerceItems  = computed(() => NAVIGATION_ITEMS.filter(i => ['Products', 'Orders', 'Customers', 'Categories', 'Discounts'].includes(i.name)));
const settingsItems  = computed(() => NAVIGATION_ITEMS.filter(i => ['Settings'].includes(i.name)));
</script>
