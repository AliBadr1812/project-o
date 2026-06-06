<template>
  <nav class="flex pb-6" aria-label="Breadcrumb" v-if="items.length > 0">
    <ol class="inline-flex items-center gap-1 md:gap-3">
      <li v-for="(crumb, index) in items" :key="index" class="inline-flex items-center">
        <!-- Separator for items after the first one -->
        <svg
          v-if="index > 0"
          class="w-4 h-4 text-gray-400 mx-2"
          fill="currentColor"
          viewBox="0 0 20 20"
        >
          <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd"/>
        </svg>

        <!-- Home icon for first item (Dashboard) -->
        <router-link
          v-if="index === 0"
          :to="crumb.path"
          class="inline-flex items-center text-[var(--color-icon)] hover:text-blue-600"
        >
          <svg class="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
            <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z"/>
          </svg>
          {{ crumb.name }}
        </router-link>

        <!-- Regular breadcrumb items -->
        <router-link
          v-else
          :to="crumb.path"
          class="text-sm font-medium text-[var(--color-icon)] hover:text-blue-600"
          :class="{ 'text-[var(--primary-color)]': index === items.length - 1 }"
        >
          {{ crumb.name }}
        </router-link>
      </li>
    </ol>
  </nav>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { BreadcrumbItem } from '@/utils/constants';

const props = defineProps<{
  items: BreadcrumbItem[];
}>();

const breadcrumbs = computed(() => props.items || []);
</script>
