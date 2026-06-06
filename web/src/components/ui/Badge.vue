<template>
  <span class="badge" :class="badgeClass">
    <slot></slot>
  </span>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  variant: {
    type: String,
    default: 'neutral',
    validator: (v: string) =>
      ['primary', 'secondary', 'neutral', 'success', 'warning', 'danger', 'info'].includes(v),
  },
});

const badgeClass = computed(() => {
  const map: Record<string, string> = {
    primary:   'badge-primary',
    secondary: 'badge-neutral',
    neutral:   'badge-neutral',
    success:   'badge-success',
    warning:   'badge-warning',
    danger:    'badge-danger',
    info:      'badge-info',
  };
  return map[props.variant] ?? 'badge-neutral';
});
</script>
