<template>
  <Teleport to="body">
    <div
      class="toast-stack"
      role="region"
      aria-label="Notifications"
      aria-live="polite"
    >
      <TransitionGroup name="toast" tag="div" class="toast-list">
        <div
          v-for="toast in toasts"
          :key="toast.id"
          class="toast-item"
          :class="`toast-${toast.variant}`"
          @mouseenter="pauseToast(toast.id)"
          @mouseleave="resumeToast(toast.id)"
        >
          <!-- Left accent bar -->
          <div class="toast-accent-bar"></div>

          <!-- Icon -->
          <div class="toast-icon">
            <i :class="iconClass(toast.variant)"></i>
          </div>

          <!-- Body -->
          <div class="toast-body">
            <p v-if="toast.title" class="toast-title">{{ toast.title }}</p>
            <p class="toast-message">{{ toast.message }}</p>
          </div>

          <!-- Close -->
          <button class="toast-close" @click="dismiss(toast.id)" aria-label="Close">
            <i class="fas fa-xmark text-xs"></i>
          </button>

          <!-- Progress bar -->
          <div class="toast-progress-track">
            <div
              class="toast-progress-fill"
              :style="{ width: toast.progress + '%' }"
            ></div>
          </div>
        </div>
      </TransitionGroup>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import { useToast } from '@/composables/useToast';

const { toasts, dismiss } = useToast();

const paused = new Set<number>();

function iconClass(variant: string): string {
  return {
    success: 'fas fa-circle-check',
    error:   'fas fa-circle-xmark',
    warning: 'fas fa-triangle-exclamation',
    info:    'fas fa-circle-info',
  }[variant] ?? 'fas fa-circle-info';
}

function pauseToast(id: number)  { paused.add(id); }
function resumeToast(id: number) { paused.delete(id); }
</script>

<style scoped>
.toast-stack {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
  display: flex;
  flex-direction: column;
  gap: 10px;
  pointer-events: none;
  max-width: 380px;
  width: 100%;
}

.toast-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.toast-item {
  position: relative;
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 14px 16px 18px 12px;
  border-radius: 14px;
  overflow: hidden;
  pointer-events: all;
  cursor: default;
  backdrop-filter: blur(20px) saturate(1.4);
  -webkit-backdrop-filter: blur(20px) saturate(1.4);
  border: 1px solid rgba(255, 255, 255, 0.22);
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.18),
    inset 0 1px 0 rgba(255, 255, 255, 0.4);
}

/* Variant backgrounds */
.toast-success {
  background: rgba(34, 197, 94, 0.12);
  border-color: rgba(34, 197, 94, 0.25);
}
.toast-error {
  background: rgba(239, 68, 68, 0.12);
  border-color: rgba(239, 68, 68, 0.25);
}
.toast-warning {
  background: rgba(245, 158, 11, 0.12);
  border-color: rgba(245, 158, 11, 0.25);
}
.toast-info {
  background: rgba(124, 58, 237, 0.12);
  border-color: rgba(124, 58, 237, 0.25);
}

/* Left accent bar */
.toast-accent-bar {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  border-radius: 14px 0 0 14px;
}
.toast-success .toast-accent-bar { background: #22c55e; }
.toast-error   .toast-accent-bar { background: #ef4444; }
.toast-warning .toast-accent-bar { background: #f59e0b; }
.toast-info    .toast-accent-bar { background: var(--accent, #7c3aed); }

/* Icon */
.toast-icon {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 1px;
  font-size: 15px;
}
.toast-success .toast-icon { color: #22c55e; }
.toast-error   .toast-icon { color: #ef4444; }
.toast-warning .toast-icon { color: #f59e0b; }
.toast-info    .toast-icon { color: var(--accent, #7c3aed); }

/* Body */
.toast-body {
  flex: 1;
  min-width: 0;
}
.toast-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-primary, #1a0a2e);
  margin: 0 0 2px;
  line-height: 1.3;
}
.toast-message {
  font-size: 12.5px;
  color: var(--text-secondary, #4a3070);
  margin: 0;
  line-height: 1.45;
}

/* Close button */
.toast-close {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  border-radius: 7px;
  border: none;
  background: rgba(255, 255, 255, 0.2);
  color: var(--text-muted, #8070a0);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.15s, color 0.15s;
  margin-top: 1px;
}
.toast-close:hover {
  background: rgba(255, 255, 255, 0.35);
  color: var(--text-primary, #1a0a2e);
}

/* Progress bar */
.toast-progress-track {
  position: absolute;
  left: 0;
  bottom: 0;
  right: 0;
  height: 3px;
  background: rgba(255, 255, 255, 0.15);
}
.toast-progress-fill {
  height: 100%;
  transition: width 50ms linear;
  border-radius: 0 0 0 0;
}
.toast-success .toast-progress-fill { background: rgba(34, 197, 94, 0.6); }
.toast-error   .toast-progress-fill { background: rgba(239, 68, 68, 0.6); }
.toast-warning .toast-progress-fill { background: rgba(245, 158, 11, 0.6); }
.toast-info    .toast-progress-fill { background: rgba(124, 58, 237, 0.6); }

/* TransitionGroup animations */
.toast-enter-active {
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.toast-leave-active {
  transition: all 0.25s ease-in;
  position: absolute;
}
.toast-enter-from {
  opacity: 0;
  transform: translateX(60px) scale(0.95);
}
.toast-leave-to {
  opacity: 0;
  transform: translateX(60px) scale(0.9);
}
.toast-move {
  transition: transform 0.3s ease;
}

/* Dark mode support */
:global(.dark) .toast-success { background: rgba(34, 197, 94, 0.15); }
:global(.dark) .toast-error   { background: rgba(239, 68, 68, 0.15); }
:global(.dark) .toast-warning { background: rgba(245, 158, 11, 0.15); }
:global(.dark) .toast-info    { background: rgba(124, 58, 237, 0.18); }
:global(.dark) .toast-item {
  border-color: rgba(255, 255, 255, 0.10);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.45), inset 0 1px 0 rgba(255,255,255,0.12);
}
:global(.dark) .toast-title   { color: #f0e8ff; }
:global(.dark) .toast-message { color: rgba(200, 180, 240, 0.85); }
:global(.dark) .toast-close   { background: rgba(255,255,255,0.08); color: rgba(200,180,240,0.6); }
:global(.dark) .toast-close:hover { background: rgba(255,255,255,0.15); color: #f0e8ff; }
</style>
