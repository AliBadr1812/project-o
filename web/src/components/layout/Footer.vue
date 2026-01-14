<template>
  <footer class="mt-auto border-t border-gray-200 bg-white px-6 py-4">
    <div class="flex flex-col md:flex-row justify-between items-center">
      <div class="mb-4 md:mb-0">
        <p class="text-sm text-gray-600">
          © {{ currentYear }} ShopAdmin. All rights reserved.
        </p>
        <p class="text-xs text-gray-500 mt-1">
          Version {{ appVersion }}
        </p>
      </div>

      <div class="flex items-center gap-6">
        <a href="#" class="text-sm text-gray-600 hover:text-gray-900">
          Documentation
        </a>
        <a href="#" class="text-sm text-gray-600 hover:text-gray-900">
          Support
        </a>
        <a href="#" class="text-sm text-gray-600 hover:text-gray-900">
          API
        </a>
        <a href="#" class="text-sm text-gray-600 hover:text-gray-900">
          Status
        </a>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
const currentYear: string = "1999"
const appVersion: string = "v1.0"
</script>
