<template>
  <header class="bg-[var(--color-primary)] border-b border-[var(--color-border)] px-6 py-4">
    <div class="flex items-center justify-between">
      <!-- Left: Search & Navigation -->
      <div class="flex items-center gap-4 flex-1">
        <!-- Mobile Menu Button -->
        <button class="lg:hidden p-2 rounded-lg hover:bg-gray-100 transition-colors">
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clip-rule="evenodd"/>
          </svg>
        </button>

        <!-- Search Bar -->
        <div class="relative flex-1 max-w-md">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <i class="fas fa-search fa-1x text-[var(--color-icon)]"></i>
          </div>
          <input
            type="search"
            v-model="searchQuery"
            placeholder="Search products, orders, customers..."
            class="w-full pl-10 pr-4 py-2 text-[var(--color-icon)] border border-[var(--color-border)] rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          >
        </div>
      </div>

      <!-- Right: Notifications & User Menu -->
      <div class="flex items-center gap-3">
        <!-- Notifications -->
        <button @click="printFetchedData()" class="cursor-pointer relative px-3 py-2 rounded-lg border border-[var(--color-border)] hover:bg-[var(--color-hover)] transition-colors">
          <i class="fas fa-bell fa-1x text-[var(--color-icon)]"></i>
          <span v-if="unreadNotifications" class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
            {{ unreadNotifications }}
          </span>
        </button>

        <!-- New Item Dropdown -->
        <div class="relative" ref="newMenuRef">
          <button
            @click="toggleDropdown('new')"
            class="cursor-pointer flex items-center gap-3 px-3 py-3 rounded-lg border border-[var(--color-border)] hover:bg-[var(--color-hover)] transition-colors"
          >
            <i class="fas fa-plus fa-1x text-[var(--color-icon)]"></i>
            <i class="fas fa-angle-down fa-xs text-[var(--color-icon)]"></i>
          </button>

          <!-- Dropdown Menu with animation -->
          <div v-if="activeDropdown === 'new'" class="absolute right-0 mt-2 w-48 bg-[var(--color-primary)] rounded-lg shadow-lg border border-[var(--color-border)] z-50 dropdown-menu">
            <div class="p-2">
              <router-link to="/products/create" class="flex items-center gap-2 px-2 py-1.5 rounded-lg hover:bg-[var(--color-hover)] transition-colors">
                <i class="fas fa-cart-plus fa-1x text-[var(--color-icon)] w-5"></i>
                <span class="text-white">New Product</span>
              </router-link>
              <router-link to="/categories/create" class="flex items-center gap-2 px-2 py-1.5 rounded-lg hover:bg-[var(--color-hover)] transition-colors">
                <i class="fas fa-book-medical fa-1x text-[var(--color-icon)] w-5"></i>
                <span class="text-white">New Category</span>
              </router-link>
            </div>
            <div class="border-t border-gray-600"></div>
            <div class="p-2">
              <a href="#" class="flex items-center gap-2 px-2 py-1.5 rounded-lg hover:bg-[var(--color-hover)] transition-colors">
                <i class="fas fa-upload fa-1x text-[var(--color-icon)] w-5"></i>
                <span class="text-white">Import Products</span>
              </a>
            </div>
          </div>
        </div>

        <!-- User Menu -->
        <div class="relative" ref="userMenuRef">
          <button
            @click="toggleDropdown('user')"
            class="cursor-pointer flex items-center gap-3 px-3 py-2 rounded-lg border border-[var(--color-border)] hover:bg-[var(--color-hover)] transition-colors"
          >
            <div v-if="user?.profileImageUrl">
              <img
                :src="user.profileImageUrl || 'https://freesvg.org/img/abstract-user-flat-4.png'"
                alt="Profile"
                class="w-6 h-6 rounded-full"
              >
            </div>
            <div v-else>
              <i class="fas fa-user fa-1x text-[var(--color-icon)]"></i>
            </div>
            <i class="fas fa-angle-down fa-xs text-[var(--color-icon)]"></i>
          </button>

          <!-- User Dropdown with animation -->
          <div v-if="activeDropdown === 'user'" class="absolute right-0 mt-2 w-48 bg-[var(--color-primary)] border border-[var(--color-border)] rounded-lg shadow-lg z-50 dropdown-menu">
            <div class="px-4 py-3 border-b border-gray-600">
              <p class="text-sm font-medium text-gray-400">Signed in as</p>
              <p class="text-sm text-white truncate">{{ user?.email || 'user@example.com' }}</p>
            </div>

            <div class="p-2">
              <router-link to="/settings/profile" class="flex items-center gap-2 px-2 py-1.5 rounded-lg hover:bg-[var(--color-hover)] transition-colors">
                <i class="fas fa-user fa-1x text-[var(--color-icon)] w-5"></i>
                <span class="text-white">Your profile</span>
              </router-link>

              <router-link to="/settings" class="flex items-center gap-2 px-2 py-1.5 rounded-lg hover:bg-[var(--color-hover)] transition-colors">
                <i class="fas fa-cog fa-1x text-[var(--color-icon)] w-5"></i>
                <span class="text-white">Settings</span>
              </router-link>
            </div>

            <div class="border-t border-gray-600"></div>

            <div class="p-2">
              <button @click="logout" class="cursor-pointer w-full text-left flex items-center gap-2 px-2 py-1.5 rounded-lg hover:bg-[var(--color-hover)] transition-colors text-red-500 hover:text-red-400">
                <i class="fas fa-arrow-right-from-bracket fa-1x w-5"></i>
                <span>Sign out</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { computed, ref, onMounted, onUnmounted } from 'vue';

type DropdownType = 'new' | 'user' | null;

const activeDropdown = ref<DropdownType>(null);
const searchQuery = ref('');
const unreadNotifications = 2;

// Refs for dropdown containers
const newMenuRef = ref<HTMLElement | null>(null);
const userMenuRef = ref<HTMLElement | null>(null);

interface User {
  email: string;
  profileImageUrl: string;
}

const props = defineProps<{
  user: User;
}>();

// Toggle specific dropdown, closing others
const toggleDropdown = (dropdown: DropdownType) => {
  if (activeDropdown.value === dropdown) {
    // Clicking the same button closes it
    activeDropdown.value = null;
  } else {
    // Clicking a different button opens it and closes others
    activeDropdown.value = dropdown;
  }
};

// Close all dropdowns
const closeAllDropdowns = () => {
  activeDropdown.value = null;
};

// Handle click outside
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as Node;

  // Check if click is outside all dropdown containers
  const isOutsideNewMenu = newMenuRef.value && !newMenuRef.value.contains(target);
  const isOutsideUserMenu = userMenuRef.value && !userMenuRef.value.contains(target);

  // If click is outside both dropdowns, close all
  if (isOutsideNewMenu && isOutsideUserMenu) {
    closeAllDropdowns();
  }
};

// Handle escape key
const handleEscapeKey = (event: KeyboardEvent) => {
  if (event.key === 'Escape') {
    closeAllDropdowns();
  }
};

// Add event listeners when component mounts
onMounted(() => {
  document.addEventListener('click', handleClickOutside);
  document.addEventListener('keydown', handleEscapeKey);
});

// Remove event listeners when component unmounts
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
  document.removeEventListener('keydown', handleEscapeKey);
});

const logout = () => {
  console.log("logout button pressed");
  closeAllDropdowns();
};

const printFetchedData =  async () => {
  const response = await fetch('https://jsonplaceholder.typicode.com/posts');
  const users = await response.json();
  console.log(users);
};
</script>

<style scoped>
/* Dropdown animation - exactly like your date picker */
.dropdown-menu {
  animation: slideDown 0.2s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Ensure proper spacing between button and dropdown */
.relative > .dropdown-menu {
  margin-top: 0.5rem; /* mt-2 = 0.5rem */
}

/* Hover effects */
.hover\:bg-\[var\(--color-hover\)\]:hover {
  background-color: var(--color-hover);
}

/* Consistent icon widths for alignment */
.fa-1x.w-5 {
  width: 1.25rem;
  text-align: center;
}

/* Smooth transitions */
button, a {
  transition: all 0.15s ease-out;
}

/* Focus states */
button:focus, a:focus {
  outline: 2px solid var(--primary-color);
  outline-offset: 2px;
}

/* Custom scrollbar for dropdown if needed */
.dropdown-menu {
  scrollbar-width: thin;
  scrollbar-color: var(--color-border) transparent;
  max-height: calc(100vh - 100px);
  overflow-y: auto;
}

.dropdown-menu::-webkit-scrollbar {
  width: 4px;
}

.dropdown-menu::-webkit-scrollbar-track {
  background: transparent;
}

.dropdown-menu::-webkit-scrollbar-thumb {
  background-color: var(--color-border);
  border-radius: 2px;
}
</style>
