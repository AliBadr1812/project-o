<template>
  <div id="app" class="min-h-screen bg-gray-50 flex">
    <!-- Sidebar on left -->
    <Sidebar />

    <!-- Main content area (right side) -->
    <div class="flex-1 flex flex-col min-h-screen">
      <!-- Header at top of right side -->
      <Header :user="user" />

      <!-- Main content below header -->
      <main class="flex-1 p-6">
        <!-- Breadcrumb -->
        <Breadcrumb :items="breadcrumbs"/>

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
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const isLoading = false;
const route = useRoute();
const user = {
  email: "blabla",
  profileImageUrl: "",
  name: "John Doe"
};

const breadcrumbs = computed(() => {
  const items = [
    { path: '/dashboard', name: 'Dashboard' }
  ];

  if (route.meta.breadcrumb) {
    items.push({
      path: route.path,
      name: route.meta.breadcrumb as string
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
