<template>
  <div id="app" class="h-screen flex relative overflow-hidden">
    <!-- Fixed wallpaper behind everything -->
    <div class="wallpaper fixed inset-0 -z-10"></div>

    <!-- Sidebar -->
    <Sidebar :dark="isDark" @toggle-dark="toggleDark" />

    <!-- Main content -->
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

    <LoadingSpinner v-if="isLoading" />
  </div>
</template>

<script setup lang="ts">
import LoadingSpinner from './components/shared/LoadingSpinner.vue';
import Sidebar from './components/layout/Sidebar.vue';
import Breadcrumb from './components/layout/Breadcrumb.vue';
import Footer from './components/layout/Footer.vue';
import { computed, ref, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { STORAGE_KEYS } from '@/utils/constants';

const isLoading = false;
const route = useRoute();
const router = useRouter();

/* ── Dark mode ── */
const isDark = ref(false);

const applyTheme = (dark: boolean) => {
  document.documentElement.classList.toggle('dark', dark);
  localStorage.setItem(STORAGE_KEYS.THEME, dark ? 'dark' : 'light');
};

const toggleDark = () => {
  isDark.value = !isDark.value;
  applyTheme(isDark.value);
};

onMounted(() => {
  const saved = localStorage.getItem(STORAGE_KEYS.THEME);
  const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
  isDark.value = saved ? saved === 'dark' : prefersDark;
  applyTheme(isDark.value);
});

/* ── Breadcrumbs ── */
const productName = ref('');

watch(
  () => route.path,
  () => {
    if (route.name === 'ProductDetail' && route.params.id) {
      productName.value = 'Product #' + route.params.id;
    } else {
      productName.value = '';
    }
  },
  { immediate: true }
);

const isDashboardRoute = computed(
  () => route.path === '/dashboard' || route.path === '/'
);

const breadcrumbs = computed(() => {
  const items = [];
  const currentRoute = router.currentRoute.value;

  if (!isDashboardRoute.value) {
    items.push({ path: '/dashboard', name: 'Dashboard' });
  }

  if (currentRoute.meta.parent) {
    const parent = currentRoute.meta.parent as { path: string; name: string };
    let parentPath = parent.path;
    if (parentPath.includes(':id') && route.params.id) {
      parentPath = parentPath.replace(':id', route.params.id as string);
    }
    items.push({ path: parentPath, name: parent.name });
  }

  let breadcrumbName = currentRoute.meta.breadcrumb as string;
  if (breadcrumbName === ':id' && route.params.id) {
    breadcrumbName = productName.value || `Product #${route.params.id}`;
  }

  if (
    breadcrumbName &&
    breadcrumbName !== 'Dashboard' &&
    !items.some(item => item.name === breadcrumbName)
  ) {
    items.push({ path: currentRoute.path, name: breadcrumbName });
  }

  return items;
});
</script>
