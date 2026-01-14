<template>
  <aside class="w-64 bg-[var(--color-primary)] text-white min-h-screen border-r border-[var(--color-border)]">
    <!-- Logo/Brand -->
    <div class="p-4 ">
      <div class="flex items-center gap-20">
        <div class="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center">
          <svg class="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 24 24">
            <path d="M3 3h8v8H3V3zm10 0h8v8h-8V3zM3 13h8v8H3v-8zm10 0h8v8h-8v-8z"/>
          </svg>
        </div>
        <h1 class="text-xl font-semibold">ShopAdmin</h1>
      </div>
    </div>

    <!-- User Profile -->
    <router-link to="/profile">
      <div class="p-4">
        <div class="flex items-center gap-3">
          <img
            :src="user?.profileImageUrl || 'https://freesvg.org/img/abstract-user-flat-4.png'"
            alt="Profile"
            class="w-10 h-10 rounded-full"
          >
          <div class="flex-1">
            <p class="font-medium">{{ user?.username || 'Admin' }}</p>
            <p class="text-sm text-gray-400">{{ user?.role || 'Administrator' }}</p>
          </div>
        </div>
      </div>
    </router-link>

    <!-- Navigation -->
    <nav class="p-4">
      <ul class="gap-3">
        <!-- Dashboard -->
        <li>
          <router-link
            to="/dashboard"
            class="flex items-center gap-3 px-3 py-2 rounded-lg text-gray-400 transition-colors hover:bg-gray-800 hover:text-white"
            :class="{ 'bg-gray-800 text-blue-400': $route.path === '/dashboard' }"
          >
            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
              <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z"/>
            </svg>
            <span>Dashboard</span>
          </router-link>
        </li>

        <!-- Products -->
        <li>
          <router-link
            to="/products"
            class="flex items-center gap-3 px-3 py-2 rounded-lg text-gray-400 transition-colors hover:bg-gray-800 hover:text-white"
            :class="{ 'bg-gray-800 text-blue-400': $route.path.startsWith('/products') }"
          >
            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
              <path d="M3 1a1 1 0 000 2h1.22l.305 1.222a.997.997 0 00.01.042l1.358 5.43-.893.892C3.74 11.846 4.632 14 6.414 14H15a1 1 0 000-2H6.414l1-1H14a1 1 0 00.894-.553l3-6A1 1 0 0017 3H6.28l-.31-1.243A1 1 0 005 1H3zM16 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM6.5 18a1.5 1.5 0 100-3 1.5 1.5 0 000 3z"/>
            </svg>
            <span>Products</span>
            <Badge class="ml-auto" variant="secondary">{{ productCount || 0 }}</Badge>
          </router-link>
        </li>

        <!-- Orders -->
        <li>
          <router-link
            to="/orders"
            class="flex items-center gap-3 px-3 py-2 rounded-lg text-gray-400 transition-colors hover:bg-gray-800 hover:text-white"
            :class="{ 'bg-gray-800 text-blue-400': $route.path.startsWith('/orders') }"
          >
            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M10 2a4 4 0 00-4 4v1H5a1 1 0 00-.994.89l-1 9A1 1 0 004 18h12a1 1 0 00.994-1.11l-1-9A1 1 0 0015 7h-1V6a4 4 0 00-4-4zm2 5V6a2 2 0 10-4 0v1h4zm-6 3a1 1 0 112 0 1 1 0 01-2 0zm7-1a1 1 0 100 2 1 1 0 000-2z" clip-rule="evenodd"/>
            </svg>
            <span>Orders</span>
            <Badge class="ml-auto" variant="warning">{{ pendingOrders || 0 }}</Badge>
          </router-link>
        </li>

        <!-- Customers -->
        <li>
          <router-link
            to="/customers"
            class="flex items-center gap-3 px-3 py-2 rounded-lg text-gray-400 transition-colors hover:bg-gray-800 hover:text-white"
            :class="{ 'bg-gray-800 text-blue-400': $route.path.startsWith('/customers') }"
          >
            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
              <path d="M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3zM6 8a2 2 0 11-4 0 2 2 0 014 0zM16 18v-3a5.972 5.972 0 00-.75-2.906A3.005 3.005 0 0119 15v3h-3zM4.75 12.094A5.973 5.973 0 004 15v3H1v-3a3 3 0 013.75-2.906z"/>
            </svg>
            <span>Customers</span>
            <Badge class="ml-auto">{{ customerCount || 0 }}</Badge>
          </router-link>
        </li>

        <!-- Categories -->
        <li>
          <router-link
            to="/categories"
            class="flex items-center gap-3 px-3 py-2 rounded-lg text-gray-400 transition-colors hover:bg-gray-800 hover:text-white"
            :class="{ 'bg-gray-800 text-blue-400': $route.path.startsWith('/categories') }"
          >
            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M17 10a1 1 0 01-1 1H4a1 1 0 01-1-1V4a1 1 0 011-1h12a1 1 0 011 1v6zM3 16a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clip-rule="evenodd"/>
            </svg>
            <span>Categories</span>
          </router-link>
        </li>

        <!-- Analytics -->
        <li>
          <router-link
            to="/analytics"
            class="flex items-center gap-3 px-3 py-2 rounded-lg text-gray-400 transition-colors hover:bg-gray-800 hover:text-white"
            :class="{ 'bg-gray-800 text-blue-400': $route.path.startsWith('/analytics') }"
          >
            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
              <path d="M2 11a1 1 0 011-1h2a1 1 0 011 1v5a1 1 0 01-1 1H3a1 1 0 01-1-1v-5zM8 7a1 1 0 011-1h2a1 1 0 011 1v9a1 1 0 01-1 1H9a1 1 0 01-1-1V7zM14 4a1 1 0 011-1h2a1 1 0 011 1v12a1 1 0 01-1 1h-2a1 1 0 01-1-1V4z"/>
            </svg>
            <span>Analytics</span>
          </router-link>
        </li>

        <!-- Divider -->
        <li class="pt-4 mt-4 border-t border-gray-800">
          <p class="px-3 text-xs font-semibold text-gray-500 uppercase">Settings</p>
        </li>

        <!-- Settings -->
        <li>
          <router-link
            to="/settings"
            class="flex items-center gap-3 px-3 py-2 rounded-lg text-gray-400 transition-colors hover:bg-gray-800 hover:text-white"
            :class="{ 'bg-gray-800 text-blue-400': $route.path.startsWith('/settings') }"
          >
            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd"/>
            </svg>
            <span>Settings</span>
          </router-link>
        </li>
      </ul>
    </nav>
  </aside>
</template>

<script setup lang="ts">
import Badge from '../ui/Badge.vue';
const user = ({
  username: "Ali",
  role: "admin",
  profileImageUrl: "https://freesvg.org/img/abstract-user-flat-4.png"
})
const productCount = 0;
const pendingOrders = 0;
const customerCount = 0;
</script>
