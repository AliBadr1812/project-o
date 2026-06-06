<template>
  <div class="flex items-center justify-between border-t border-[var(--color-border)] px-4 py-3 sm:px-6">
    <!-- Mobile -->
    <div class="flex flex-1 justify-between sm:hidden">
      <button
        @click="previousPage"
        :disabled="currentPage === 1"
        class="relative inline-flex items-center rounded-md border border-[var(--color-border)] px-4 py-2 text-sm font-medium text-[var(--color-icon)] hover:bg-gray-50"
        :class="{ 'opacity-50 cursor-not-allowed': currentPage === 1 }"
      >
        Previous
      </button>
      <button
        @click="nextPage"
        :disabled="currentPage === totalPages"
        class="relative ml-3 inline-flex items-center rounded-md border border-[var(--color-border)] px-4 py-2 text-sm font-medium text-[var(--color-icon)] hover:bg-gray-50"
        :class="{ 'opacity-50 cursor-not-allowed': currentPage === totalPages }"
      >
        Next
      </button>
    </div>

    <!-- Desktop -->
    <div class="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
      <div>
        <p class="text-sm text-var[--color-icon]">
          Showing
          <span class="font-medium">{{ startItem }}</span>
          to
          <span class="font-medium">{{ endItem }}</span>
          of
          <span class="font-medium">{{ totalItems }}</span>
          results
        </p>
      </div>

      <div>
        <nav class="isolate inline-flex -space-x-px rounded-md shadow-sm">
          <!-- Previous -->
          <button
            @click="previousPage"
            :disabled="currentPage === 1"
            class="relative inline-flex items-center rounded-l-md px-2 py-2 text-[var(--color-icon)] ring-1 ring-inset ring-gray-300 hover:bg-[var(--color-hover)] focus:z-20 focus:outline-offset-0"
            :class="{ 'cursor-not-allowed opacity-50': currentPage === 1 }"
          >
            <span class="sr-only">Previous</span>
            <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M12.79 5.23a.75.75 0 01-.02 1.06L8.832 10l3.938 3.71a.75.75 0 11-1.04 1.08l-4.5-4.25a.75.75 0 010-1.08l4.5-4.25a.75.75 0 011.06.02z" clip-rule="evenodd"/>
            </svg>
          </button>

          <!-- Page Numbers -->
          <template v-for="page in visiblePages" :key="typeof page === 'string' ? `ellipsis-${Math.random()}` : `page-${page}`">
            <button
              v-if="typeof page === 'string'"
              disabled
              class="relative inline-flex items-center px-4 py-2 text-sm font-semibold text-[var(--color-icon)] ring-1 ring-inset ring-gray-300 focus:z-20 focus:outline-offset-0"
            >
              {{ page }}
            </button>
            <button
              v-else
              @click="goToPage(page)"
              :class="[
                'relative inline-flex items-center px-4 py-2 text-sm font-semibold ring-1 ring-inset ring-gray-300 focus:z-20 focus:outline-offset-0',
                currentPage === page
                  ? 'z-10 bg-[var(--color-hover)] text-[var(--color-icon)] focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600'
                  : 'text-[var(--color-icon)] hover:bg-[var(--color-hover)]'
              ]"
            >
              {{ page }}
            </button>
          </template>

          <!-- Next -->
          <button
            @click="nextPage"
            :disabled="currentPage === totalPages"
            class="relative inline-flex items-center rounded-r-md px-2 py-2 text-[var(--color-icon)] ring-1 ring-inset ring-gray-300 hover:bg-[var(--color-hover)] focus:z-20 focus:outline-offset-0"
            :class="{ 'cursor-not-allowed opacity-50': currentPage === totalPages }"
          >
            <span class="sr-only">Next</span>
            <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M7.21 14.77a.75.75 0 01.02-1.06L11.168 10 7.23 6.29a.75.75 0 111.04-1.08l4.5 4.25a.75.75 0 010 1.08l-4.5 4.25a.75.75 0 01-1.06-.02z" clip-rule="evenodd"/>
            </svg>
          </button>
        </nav>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

// Props
const props = defineProps<{
  currentPage: number;
  totalPages: number;
  totalItems: number;
  itemsPerPage?: number;
  maxVisiblePages?: number;
}>();

// Emits
const emit = defineEmits<{
  'page-change': [page: number];
}>();

// Default values
const itemsPerPage = computed(() => props.itemsPerPage || 10);
const maxVisiblePages = computed(() => props.maxVisiblePages || 5);

// Computed values
const startItem = computed(() => {
  return ((props.currentPage - 1) * itemsPerPage.value) + 1;
});

const endItem = computed(() => {
  const end = props.currentPage * itemsPerPage.value;
  return end > props.totalItems ? props.totalItems : end;
});

// Generate visible page numbers
const visiblePages = computed(() => {
  const pages: (number | string)[] = [];
  const half = Math.floor(maxVisiblePages.value / 2);
  let start = props.currentPage - half;
  let end = props.currentPage + half;

  // Adjust start and end if out of bounds
  if (start < 1) {
    start = 1;
    end = Math.min(maxVisiblePages.value, props.totalPages);
  }

  if (end > props.totalPages) {
    end = props.totalPages;
    start = Math.max(1, end - maxVisiblePages.value + 1);
  }

  // Generate page numbers
  for (let i = start; i <= end; i++) {
    pages.push(i);
  }

  // Add ellipsis if needed
  if (start > 1) {
    if (start > 2) {
      // Add first page and ellipsis
      return [1, '...', ...pages];
    } else {
      // Just add first page
      return [1, ...pages];
    }
  }

  if (end < props.totalPages) {
    if (end < props.totalPages - 1) {
      // Add ellipsis and last page
      return [...pages, '...', props.totalPages];
    } else {
      // Just add last page
      return [...pages, props.totalPages];
    }
  }

  return pages;
});

// Methods
const goToPage = (page: number | string) => {
  if (typeof page === 'string' || page < 1 || page > props.totalPages || page === props.currentPage) {
    return;
  }
  emit('page-change', page);
};

const previousPage = () => {
  if (props.currentPage > 1) {
    emit('page-change', props.currentPage - 1);
  }
};

const nextPage = () => {
  if (props.currentPage < props.totalPages) {
    emit('page-change', props.currentPage + 1);
  }
};
</script>
