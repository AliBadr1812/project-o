<template>
  <div v-if="show" class="fixed inset-0 z-50 overflow-y-auto bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-8 max-w-sm w-full mx-4 text-center">
      <!-- Spinner -->
      <div class="flex justify-center mb-4">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>

      <!-- Message -->
      <h3 class="text-lg font-medium text-gray-900 mb-2">
        {{ title }}
      </h3>
      <p class="text-sm text-gray-500">
        {{ message }}
      </p>

      <!-- Progress (optional) -->
      <div v-if="showProgress" class="mt-4">
        <div class="w-full bg-gray-200 rounded-full h-2">
          <div
            class="bg-blue-600 h-2 rounded-full transition-all duration-300"
            :style="{ width: progress + '%' }"
          ></div>
        </div>
        <p class="text-xs text-gray-500 mt-2">{{ progress }}% complete</p>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">


</script>
