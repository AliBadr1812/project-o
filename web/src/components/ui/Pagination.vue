<template>
  <div
    class="flex items-center justify-between px-5 py-3"
    style="border-top: 1px solid var(--glass-border);"
  >
    <!-- Count -->
    <p class="text-sm" style="color: var(--text-muted);">
      Showing
      <span class="font-semibold" style="color: var(--text-primary);">{{ startItem }}</span>
      –
      <span class="font-semibold" style="color: var(--text-primary);">{{ endItem }}</span>
      of
      <span class="font-semibold" style="color: var(--text-primary);">{{ totalItems }}</span>
    </p>

    <!-- Controls -->
    <div class="flex items-center gap-1">
      <!-- Prev -->
      <button
        @click="previousPage"
        :disabled="currentPage === 1"
        class="btn-glass-icon w-8 h-8 rounded-[10px] text-sm"
        :class="{ 'opacity-40 cursor-not-allowed': currentPage === 1 }"
      >
        <i class="fas fa-chevron-left text-xs"></i>
      </button>

      <!-- Pages -->
      <template v-for="page in visiblePages" :key="typeof page === 'string' ? `e-${Math.random()}` : `p-${page}`">
        <button
          v-if="typeof page === 'string'"
          disabled
          class="w-8 h-8 flex items-center justify-center text-sm rounded-[10px]"
          style="color: var(--text-muted);"
        >…</button>
        <button
          v-else
          @click="goToPage(page)"
          class="w-8 h-8 flex items-center justify-center text-sm font-semibold rounded-[10px] transition-all"
          :class="currentPage === page ? 'btn-accent' : 'btn-glass-icon'"
          style="width: 32px; height: 32px;"
        >{{ page }}</button>
      </template>

      <!-- Next -->
      <button
        @click="nextPage"
        :disabled="currentPage === totalPages"
        class="btn-glass-icon w-8 h-8 rounded-[10px] text-sm"
        :class="{ 'opacity-40 cursor-not-allowed': currentPage === totalPages }"
      >
        <i class="fas fa-chevron-right text-xs"></i>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  currentPage:      number;
  totalPages:       number;
  totalItems:       number;
  itemsPerPage?:    number;
  maxVisiblePages?: number;
}>();

const emit = defineEmits<{ 'page-change': [page: number] }>();

const itemsPerPage    = computed(() => props.itemsPerPage    || 10);
const maxVisiblePages = computed(() => props.maxVisiblePages || 5);
const startItem       = computed(() => (props.currentPage - 1) * itemsPerPage.value + 1);
const endItem         = computed(() => Math.min(props.currentPage * itemsPerPage.value, props.totalItems));

const visiblePages = computed(() => {
  const pages: (number | string)[] = [];
  const half  = Math.floor(maxVisiblePages.value / 2);
  let start   = Math.max(1, props.currentPage - half);
  let end     = Math.min(props.totalPages, start + maxVisiblePages.value - 1);
  if (end - start < maxVisiblePages.value - 1) start = Math.max(1, end - maxVisiblePages.value + 1);

  for (let i = start; i <= end; i++) pages.push(i);

  if (start > 2) pages.unshift(1, '...');
  else if (start === 2) pages.unshift(1);

  if (end < props.totalPages - 1) pages.push('...', props.totalPages);
  else if (end === props.totalPages - 1) pages.push(props.totalPages);

  return pages;
});

const goToPage = (page: number | string) => {
  if (typeof page !== 'number' || page < 1 || page > props.totalPages || page === props.currentPage) return;
  emit('page-change', page);
};
const previousPage = () => { if (props.currentPage > 1) emit('page-change', props.currentPage - 1); };
const nextPage     = () => { if (props.currentPage < props.totalPages) emit('page-change', props.currentPage + 1); };
</script>
