<template>
  <div class="overflow-x-auto">
    <table class="min-w-full divide-y divide-gray-200">
      <slot name="header"></slot>
      <tbody class="divide-y divide-gray-200">
        <slot name="body"></slot>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
</script>
