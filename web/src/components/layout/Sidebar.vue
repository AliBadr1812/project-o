<template>
  <aside class="w-64 bg-[var(--color-primary)] text-white min-h-screen border-r border-[var(--color-border)]">
    <!-- Logo/Brand -->
    <div class="p-4 ">
      <div class="flex items-center gap-10 p-3">
        <img src="@/assets/logo.svg" alt="ShopAdmin Logo" class="w-8 h-8"/>
        <h1 class="text-xl font-semibold text-[var(--color-icon)]">ShopAdmin</h1>
      </div>
    </div>

    <!-- User Profile -->
    <router-link to="/settings/profile">
      <div class="p-4">
        <div class="flex items-center gap-3 p-3 hover:bg-[var(--color-hover)] rounded-lg transition-colors">
          <i class="fas fa-user fa-2x text-[var(--color-icon)]"></i>
          <div class="flex-1">
            <p class="font-medium">{{ user?.username || 'Admin' }}</p>
            <p class="text-sm text-gray-400">{{ user?.role || 'Administrator' }}</p>
          </div>
        </div>
      </div>
    </router-link>

    <!-- Navigation -->
    <nav class="p-4">
      <ul class="grid grid-cols-1 gap-3">
        <li v-for="item in NAVIGATION_ITEMS" :key="item.name">
          <router-link
            :to=item.path
            class="flex items-center gap-3 px-3 py-2 rounded-lg text-gray-400 transition-colors hover:bg-[var(--color-hover)] hover:text-white"
            :class="{ 'bg-gray-800 text-blue-400': $route.path === item.path }"
          >
            <i :class="item.icon" class="fa-1x text-[var(--color-icon)]"></i>
            <span>{{ item.name }}</span>
            <Badge v-if="item.badge" class="ml-auto" :variant="item.badge.variant">{{ item.badge.text }}</Badge>
          </router-link>
        </li>
      </ul>
    </nav>
  </aside>
</template>

<script setup lang="ts">
import { NAVIGATION_ITEMS } from '@/utils/constants';
import Badge from '../ui/Badge.vue';
const user = ({
  username: "Ali",
  role: "admin",
  profileimageUrl: "",
})
</script>
