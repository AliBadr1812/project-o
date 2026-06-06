<template>
  <div class="overflow-x-auto">
    <table class="min-w-full divide-y divide-[var(--color-border)]">
      <slot name="header" class="bg-[var(--color-card)]"></slot>
      <tbody class="divide-y divide-[var(--color-border)]">
        <slot name="body"></slot>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
</script>
