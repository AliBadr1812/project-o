<template>
  <Teleport to="body">
    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4" style="animation: fade-up 0.2s ease-out;">
      <!-- Backdrop -->
      <div
        class="fixed inset-0"
        style="background: rgba(30,10,60,0.35); backdrop-filter: blur(4px); -webkit-backdrop-filter: blur(4px);"
        @click="close"
      ></div>

      <!-- Modal panel -->
      <div
        class="glass-card relative w-full max-w-2xl max-h-[90vh] flex flex-col z-10"
        style="overflow: hidden;"
      >
        <!-- Header -->
        <div
          class="flex items-center justify-between px-6 py-4 flex-shrink-0"
          style="border-bottom: 1px solid var(--glass-border);"
        >
          <h3 class="text-[17px] font-semibold" style="color: var(--text-primary);">
            <slot name="title"></slot>
          </h3>
          <button @click="close" class="btn-glass-icon w-8 h-8 text-sm rounded-[10px]">
            <i class="fas fa-xmark"></i>
          </button>
        </div>

        <!-- Content -->
        <div class="px-6 py-5 overflow-y-auto flex-1" style="color: var(--text-secondary);">
          <slot name="content"></slot>
        </div>

        <!-- Footer -->
        <div
          class="flex items-center justify-end gap-3 px-6 py-4 flex-shrink-0"
          style="border-top: 1px solid var(--glass-border);"
        >
          <slot name="footer"></slot>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
defineProps({ isOpen: Boolean });
const emit = defineEmits(['close']);
const close = () => emit('close');
</script>
