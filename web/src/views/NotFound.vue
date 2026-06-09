<template>
  <div class="flex flex-col items-center justify-center min-h-[60vh] gap-6 text-center">
    <div class="not-found-icon">
      <i class="fas fa-compass-drafting"></i>
    </div>
    <div>
      <p class="text-[80px] font-black leading-none tracking-tighter" style="color: var(--text-primary); opacity: 0.12;">404</p>
      <h1 class="text-2xl font-bold -mt-2" style="color: var(--text-primary);">Page not found</h1>
      <p class="text-sm mt-2 max-w-xs mx-auto" style="color: var(--text-muted);">
        The page you're looking for doesn't exist or has been moved.
      </p>
    </div>
    <div class="flex items-center gap-3">
      <button @click="router.back()" class="btn-glass text-sm">
        <i class="fas fa-arrow-left text-xs mr-1.5"></i>Go back
      </button>
      <router-link to="/dashboard" class="btn-accent text-sm">
        <i class="fas fa-gauge-high text-xs mr-1.5"></i>Dashboard
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
const router = useRouter();
</script>

<style scoped>
.not-found-icon {
  width: 72px;
  height: 72px;
  border-radius: 22px;
  background: rgba(var(--accent-rgb), 0.08);
  border: 1px solid rgba(var(--accent-rgb), 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: var(--accent);
}
</style>
