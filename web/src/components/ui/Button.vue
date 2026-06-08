<template>
  <button
    :type="type"
    :disabled="disabled || loading"
    :class="['btn-accent', variant === 'ghost' && 'btn-glass', variant === 'danger' && 'btn-danger']"
    @click="$emit('click')"
  >
    <span v-if="loading" class="flex items-center gap-2">
      <i class="fas fa-circle-notch fa-spin text-sm"></i>
      {{ loadingText || 'Loading…' }}
    </span>
    <span v-else class="flex items-center gap-2">
      <slot name="icon"></slot>
      <slot></slot>
    </span>
  </button>
</template>

<script setup lang="ts">
defineProps({
  type:        { type: String as () => 'button' | 'submit' | 'reset', default: 'button' },
  disabled:    { type: Boolean, default: false },
  loading:     { type: Boolean, default: false },
  loadingText: { type: String,  default: '' },
  variant:     {
    type: String,
    default: 'accent',
    validator: (v: string) => ['accent', 'ghost', 'danger'].includes(v),
  },
});
defineEmits(['click']);
</script>
