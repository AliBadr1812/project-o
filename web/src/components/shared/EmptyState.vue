<template>
  <div class="text-center py-12">
    <!-- Icon -->
    <div class="mx-auto h-12 w-12 text-gray-400 mb-4">
      <slot name="icon">
        <svg class="h-full w-full" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"/>
        </svg>
      </slot>
    </div>

    <!-- Title -->
    <h3 class="text-lg font-medium text-gray-900 mb-2">
      {{ title }}
    </h3>

    <!-- Description -->
    <p class="text-gray-500 max-w-md mx-auto mb-6">
      {{ description }}
    </p>

    <!-- Action -->
    <div v-if="actionText">
      <router-link
        v-if="actionTo"
        :to="actionTo"
        class="inline-flex items-center px-4 py-2 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
      >
        <svg class="-ml-1 mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
        </svg>
        {{ actionText }}
      </router-link>

      <button
        v-else-if="actionClick"
        @click="actionClick"
        class="inline-flex items-center px-4 py-2 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
      >
        <svg class="-ml-1 mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
        </svg>
        {{ actionText }}
      </button>
    </div>

    <!-- Secondary Action -->
    <p v-if="secondaryText" class="mt-3">
      <button
        @click="secondaryClick"
        class="text-sm font-medium text-blue-600 hover:text-blue-500"
      >
        {{ secondaryText }}
        <span aria-hidden="true"> &rarr;</span>
      </button>
    </p>
  </div>
</template>
<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const props = defineProps({
  // These will receive values whether passed with or without colon
  title: {
    type: String,
    required: true
  },
  description: {
    type: String,
    default: ''
  },
  actionText: {
    type: String,
    default: ''
  },
  actionTo: {
    type: String,
    default: ''
  },
  secondaryText: {
    type: String,
    required: false,
  }
})

// Handle button click
const actionClick = () => {
  if (props.actionTo) {
    // Navigate to the route
    router.push(props.actionTo)
  }
  // Emit event for parent to handle
  emit('action')
}

const secondaryClick = () => {
  
}

// Or using emits
const emit = defineEmits(['action'])
</script>
