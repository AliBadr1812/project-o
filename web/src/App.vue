<template>
  <div id="app" class="min-h-screen bg-[var(--color-secondary)] flex">
    <!-- Sidebar on left -->
    <Sidebar />

    <!-- Main content area (right side) -->
    <div class="flex-1 flex flex-col min-h-screen">
      <!-- Header at top of right side -->
      <Header :user="user" />

      <!-- Main content below header -->
      <main class="flex-1 p-6">
        <!-- Breadcrumb - Only show if not on dashboard -->
        <Breadcrumb v-if="!isDashboardRoute" :items="breadcrumbs"/>

        <!-- Page Content -->
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>

      <!-- Footer at bottom of right side -->
      <Footer />
    </div>

    <!-- Loading Overlay -->
    <LoadingSpinner v-if="isLoading" />
  </div>
</template>

<script setup lang="ts">
import LoadingSpinner from './components/shared/LoadingSpinner.vue';
import Header from './components/layout/Header.vue';
import Sidebar from './components/layout/Sidebar.vue';
import Breadcrumb from './components/layout/Breadcrumb.vue';
import Footer from './components/layout/Footer.vue';
import { computed, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const isLoading = false;
const route = useRoute();
const router = useRouter();

const user = {
  email: "blabla",
  profileImageUrl: "",
  name: "John Doe"
};

// Store product name for dynamic breadcrumbs
const productName = ref('');

// Function to update product name when on product detail page
const updateProductName = () => {
  if (route.name === 'ProductDetail' && route.params.id) {
    // In a real app, you would fetch the product name from API or store
    // For now, we'll use a mock or extract from route
    productName.value = 'Product #' + route.params.id;
  } else {
    productName.value = '';
  }
};

// Watch route changes
watch(
  () => route.path,
  () => {
    updateProductName();
  },
  { immediate: true }
);

// Check if we're on the dashboard
const isDashboardRoute = computed(() => {
  return route.path === '/dashboard' || route.path === '/';
});

const breadcrumbs = computed(() => {
  const items = [];

  const currentRoute = router.currentRoute.value;

  // Always add Dashboard as first item (except on dashboard itself)
  if (!isDashboardRoute.value) {
    items.push({
      path: '/dashboard',
      name: 'Dashboard'
    });
  }

  // Get parent route if exists
  if (currentRoute.meta.parent) {
    const parent = currentRoute.meta.parent as { path: string, name: string };

    // Check if parent path has dynamic segments
    let parentPath = parent.path;
    if (parentPath.includes(':id') && route.params.id) {
      parentPath = parentPath.replace(':id', route.params.id as string);
    }

    items.push({
      path: parentPath,
      name: parent.name
    });
  }

  // Handle dynamic breadcrumb names (like :id)
  let breadcrumbName = currentRoute.meta.breadcrumb as string;

  // Replace dynamic segments
  if (breadcrumbName === ':id' && route.params.id) {
    // Use product name if available, otherwise use ID
    breadcrumbName = productName.value || `Product #${route.params.id}`;
  }

  // Add current page if it has a breadcrumb name and is not already in the list
  if (breadcrumbName &&
      breadcrumbName !== 'Dashboard' &&
      !items.some(item => item.name === breadcrumbName)) {
    items.push({
      path: currentRoute.path,
      name: breadcrumbName
    });
  }

  return items;
});
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
