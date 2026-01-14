<template>
  <header class="bg-[var(--color-primary)] border-b border-gray-500 px-6 py-4">
    <div class="flex items-center justify-between">
      <!-- Left: Search & Navigation -->
      <div class="flex items-center gap-4 flex-1">
        <!-- Mobile Menu Button -->
        <button class="lg:hidden p-2 rounded-lg hover:bg-gray-100 transition-colors">
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clip-rule="evenodd"/>
          </svg>
        </button>

        <!-- Search Bar (GitHub style) -->
        <div class="relative flex-1 max-w-md">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <svg class="w-5 h-5 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd"/>
            </svg>
          </div>
          <input
            type="search"
            v-model="searchQuery"
            placeholder="Search products, orders, customers..."
            class="w-full pl-10 pr-4 py-2 text-gray-300 border border-gray-500 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          >
        </div>
      </div>

      <!-- Right: Notifications & User Menu -->
      <div class="flex items-center gap-3">
        <!-- Notifications -->
        <button class="relative p-3 rounded-lg border border-gray-500 hover:bg-gray-800 transition-colors">
          <svg class="w-4 h-4 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
            <path d="M10 2a6 6 0 00-6 6v3.586l-.707.707A1 1 0 004 14h12a1 1 0 00.707-1.707L16 11.586V8a6 6 0 00-6-6zM10 18a3 3 0 01-3-3h6a3 3 0 01-3 3z"/>
          </svg>
          <span v-if="unreadNotifications" class="absolute -top-1 -right-1 bg-red-500 text-gray-400 text-xs rounded-full w-5 h-5 flex items-center justify-center">
            {{ unreadNotifications }}
          </span>
        </button>

        <!-- New Item Dropdown -->
        <div class="relative">
          <button
            @click="showNewMenu = !showNewMenu"
            class="flex items-center gap-2 px-3 py-3 text-gray-400 rounded-lg border border-gray-500 hover:bg-gray-800 transition-colors"
          >
            <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M10 3a1 1 0 00-1 1v5H4a1 1 0 100 2h5v5a1 1 0 102 0v-5h5a1 1 0 100-2h-5V4a1 1 0 00-1-1z" clip-rule="evenodd"/>
            </svg>
            <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"/>
            </svg>
          </button>

          <!-- Dropdown Menu -->
            <div v-if="showNewMenu" class="absolute right-0 mt-2 w-45 bg-[var(--color-primary)] rounded-lg shadow-lg border border-[var(--color-border)] py-1 z-50">
              <router-link to="/products/new" class="flex items-center gap-3 px-4 py-2 hover:bg-gray-800">
                <svg class="w-4 h-4 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                  <path d="M3 1a1 1 0 000 2h1.22l.305 1.222a.997.997 0 00.01.042l1.358 5.43-.893.892C3.74 11.846 4.632 14 6.414 14H15a1 1 0 000-2H6.414l1-1H14a1 1 0 00.894-.553l3-6A1 1 0 0017 3H6.28l-.31-1.243A1 1 0 005 1H3z"/>
                </svg>
                <span class="text-gray-300">New Product</span>
              </router-link>
              <router-link to="/categories/new" class="flex items-center gap-3 px-4 py-2 hover:bg-gray-800">
                <svg class="w-4 h-4 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M17 10a1 1 0 01-1 1H4a1 1 0 01-1-1V4a1 1 0 011-1h12a1 1 0 011 1v6z" clip-rule="evenodd"/>
                </svg>
                <span class="text-gray-300">New Category</span>
              </router-link>
            <div class="border-t border-gray-500 my-1"></div>
            <a href="#" class="flex items-center gap-3 px-4 py-2 hover:bg-gray-800">
              <svg class="w-4 h-4 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-11a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V7z" clip-rule="evenodd"/>
              </svg>
              <span class="text-gray-300">Import Products</span>
            </a>
          </div>
        </div>

        <!-- User Menu -->
        <div class="relative">
          <button
            @click="showUserMenu = !showUserMenu"
            class="flex items-center gap-3 p-2 rounded-lg border border-gray-500 hover:bg-gray-800 transition-colors"
          >
            <img
              :src="user?.profileImageUrl || 'https://freesvg.org/img/abstract-user-flat-4.png'"
              alt="Profile"
              class="w-6 h-6 rounded-full"
            >
            <svg class="w-4 h-4 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"/>
            </svg>
          </button>

          <!-- User Dropdown (GitHub style) -->
            <div v-if="showUserMenu" class="absolute right-0 mt-2 w-45 bg-[var(--color-primary)] rounded-lg shadow-lg py-1 z-50">
              <div class="px-4 py-3 border-b border-gray-500">
              <p class="text-sm font-medium text-gray-300">Signed in as</p>
              <p class="text-sm text-white truncate">{{ user?.email }}</p>
            </div>

            <router-link to="/settings/profile" class="flex items-center gap-3 px-4 py-2 rounded-lg hover:bg-gray-800">
              <svg class="w-4 h-4 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"/>
              </svg>
              <span class="text-gray-300">Your profile</span>
            </router-link>

            <router-link to="/settings" class="flex items-center gap-3 px-4 py-2 hover:bg-gray-800">
              <svg class="w-4 h-4 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd"/>
              </svg>
              <span class="text-gray-300">Settings</span>
            </router-link>

            <div class="border-t border-gray-500 my-1"></div>

            <button @click="logout" class="w-full text-left flex items-center gap-3 px-4 py-2 hover:bg-gray-800 text-red-600">
              <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 102 0V4a1 1 0 00-1-1zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z" clip-rule="evenodd"/>
              </svg>
              <span>Sign out</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';

const showNewMenu = ref<boolean>(false);
const showUserMenu = ref<boolean>(false);
const user = computed(() => props.user || null);
const unreadNotifications = 0;

interface User {
  email: string;
  profileImageUrl: string;
}

const props = defineProps<{
  user: User;
}>();

const logout = () => {
  console.log("logout button pressed");
}
</script>
