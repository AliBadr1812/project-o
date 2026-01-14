<template>
  <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto">
    <!-- Backdrop -->
    <div class="fixed inset-0 bg-black bg-opacity-50" @click="close"></div>

    <!-- Modal -->
    <div class="flex items-center justify-center min-h-screen p-4">
      <div class="relative bg-white rounded-lg shadow-xl max-w-2xl w-full">
        <!-- Header -->
        <div class="flex items-center justify-between p-6 border-b border-gray-200">
          <h3 class="text-lg font-semibold">
            <slot name="title"></slot>
          </h3>
          <button @click="close" class="p-1 text-gray-400 hover:text-gray-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <!-- Content -->
        <div class="p-6">
          <slot name="content"></slot>
        </div>

        <!-- Footer -->
        <div class="flex items-center justify-end p-6 border-t border-gray-200 gap-3">
          <slot name="footer"></slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  isOpen: Boolean
});

const emit = defineEmits(['close']);

const close = () => {
  emit('close');
};
</script>
