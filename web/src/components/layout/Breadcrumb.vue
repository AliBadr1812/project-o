<template>
  <nav class="flex pb-5" aria-label="Breadcrumb" v-if="items.length > 0">
    <ol class="inline-flex items-center gap-1">
      <li v-for="(crumb, index) in items" :key="index" class="inline-flex items-center">

        <i v-if="index > 0" class="fas fa-chevron-right text-[10px] mx-2" style="color: var(--text-muted);"></i>

        <router-link
          :to="crumb.path"
          class="inline-flex items-center gap-1.5 text-sm font-medium transition-colors hover:text-[var(--accent)]"
          :class="index === items.length - 1
            ? 'text-[var(--accent)]'
            : 'text-[var(--text-muted)]'"
        >
          <i v-if="index === 0" class="fas fa-house text-xs"></i>
          {{ crumb.name }}
        </router-link>

      </li>
    </ol>
  </nav>
</template>

<script setup lang="ts">
import type { BreadcrumbItem } from '@/utils/constants';
defineProps<{ items: BreadcrumbItem[] }>();
</script>
