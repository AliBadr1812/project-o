<template>
  <nav class="flex mb-6" aria-label="Breadcrumb">
    <ol class="inline-flex items-center gap-1 md:gap-3">
      <li class="inline-flex items-center">
        <router-link to="/dashboard" class="inline-flex items-center text-gray-700 hover:text-blue-600">
          <svg class="w-4 h-4 mr-2" fill="currentColor" viewBox="0 0 20 20">
            <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z"/>
          </svg>
          Dashboard
        </router-link>
      </li>

      <li v-for="(crumb, index) in breadcrumbs" :key="index">
        <div class="flex items-center">
          <svg class="w-6 h-6 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd"/>
          </svg>
          <router-link
            :to="crumb.path"
            class="ml-1 text-sm font-medium text-gray-700 hover:text-blue-600 md:ml-2"
            :class="{ 'text-blue-600': index === breadcrumbs.length - 1 }"
          >
            {{ crumb.name }}
          </router-link>
        </div>
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

