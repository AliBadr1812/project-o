<template>
  <div
    class="fixed inset-0 z-50 flex items-center justify-center"
    style="background: rgba(30,10,60,0.25); backdrop-filter: blur(6px); -webkit-backdrop-filter: blur(6px);"
  >
    <div class="glass-card p-8 text-center" style="min-width: 220px;">
      <!-- Spinner -->
      <div
        class="w-12 h-12 rounded-full mx-auto mb-4"
        style="border: 3px solid var(--glass-border);
               border-top-color: var(--accent);
               animation: spin-smooth 0.9s linear infinite;"
      ></div>

      <h3 class="text-base font-semibold mb-1" style="color: var(--text-primary);">
        {{ title || 'Loading…' }}
      </h3>
      <p class="text-sm" style="color: var(--text-muted);">{{ message }}</p>

      <!-- Progress -->
      <div v-if="showProgress" class="mt-4">
        <div class="progress-track">
          <div class="progress-fill" :style="{ width: progress + '%', background: 'var(--progress-primary)' }"></div>
        </div>
        <p class="text-xs mt-2" style="color: var(--text-muted);">{{ progress }}% complete</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  show:         { type: Boolean, default: true },
  title:        { type: String,  default: '' },
  message:      { type: String,  default: '' },
  showProgress: { type: Boolean, default: false },
  progress:     { type: Number,  default: 0 },
});
</script>
