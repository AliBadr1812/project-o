<template>
  <div class="overflow-x-auto">
    <table class="glass-table min-w-full">
      <thead style="border-bottom: 1px solid var(--table-border);">
        <slot name="header"></slot>
      </thead>
      <tbody>
        <slot name="body"></slot>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
</script>
