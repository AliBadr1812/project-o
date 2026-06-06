<template>
  <div class="text-center py-16 px-6">
    <!-- Icon -->
    <div
      class="mx-auto w-16 h-16 rounded-2xl flex items-center justify-center mb-5 text-2xl"
      style="background: var(--ni-purple-bg); color: var(--ni-purple);
             border: 1px solid rgba(255,255,255,0.55);
             box-shadow: inset 0 1px 0 rgba(255,255,255,0.7);"
    >
      <slot name="icon">
        <i class="fas fa-inbox"></i>
      </slot>
    </div>

    <!-- Title -->
    <h3 class="text-lg font-semibold mb-2" style="color: var(--text-primary);">{{ title }}</h3>

    <!-- Description -->
    <p class="text-sm max-w-sm mx-auto mb-6" style="color: var(--text-secondary);">{{ description }}</p>

    <!-- Primary action -->
    <div v-if="actionText" class="flex flex-col items-center gap-3">
      <router-link v-if="actionTo" :to="actionTo">
        <button class="btn-accent">
          <i class="fas fa-plus text-xs"></i>
          {{ actionText }}
        </button>
      </router-link>
      <button v-else-if="actionClick" @click="actionClick" class="btn-accent">
        <i class="fas fa-plus text-xs"></i>
        {{ actionText }}
      </button>

      <button v-if="secondaryText" @click="secondaryClick" class="text-sm font-medium hover:opacity-80 transition-opacity" style="color: var(--text-accent);">
        {{ secondaryText }} →
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
const router = useRouter();

const props = defineProps({
  title:         { type: String,   required: true },
  description:   { type: String,   default: '' },
  actionText:    { type: String,   default: '' },
  actionTo:      { type: String,   default: '' },
  secondaryText: { type: String,   required: false },
});

const emit = defineEmits(['action']);

const actionClick = () => {
  if (props.actionTo) router.push(props.actionTo);
  emit('action');
};
const secondaryClick = () => {};
</script>
