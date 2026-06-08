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
            <i class="fas fa-xmark"></i>
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
/* ── Container ─────────────────────────────────────────────── */
.toast-stack {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
  pointer-events: none;
  width: 340px;
}

.toast-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

/* ── Toast card ────────────────────────────────────────────── */
.toast-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 14px 15px 14px;   /* extra 3px bottom for progress bar */
  border-radius: 12px;
  overflow: hidden;
  pointer-events: all;
  cursor: default;

  background: #ffffff;
  border: 1px solid #e8e3f2;
  box-shadow:
    0 1px 3px rgba(0, 0, 0, 0.06),
    0 4px 12px rgba(0, 0, 0, 0.07);
}

/* ── Icon ──────────────────────────────────────────────────── */
.toast-icon {
  flex-shrink: 0;
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
  align-self: flex-start;
  margin-top: 1px;
}

.toast-success .toast-icon { color: #16a34a; }
.toast-error   .toast-icon { color: #dc2626; }
.toast-warning .toast-icon { color: #d97706; }
.toast-info    .toast-icon { color: #7c3aed; }

/* ── Body ──────────────────────────────────────────────────── */
.toast-body {
  flex: 1;
  min-width: 0;
}

.toast-title {
  font-size: 13px;
  font-weight: 600;
  color: #18102e;
  margin: 0 0 2px;
  line-height: 1.3;
}

.toast-message {
  font-size: 12px;
  color: #6b5b8d;
  margin: 0;
  line-height: 1.45;
}

/* ── Close button ──────────────────────────────────────────── */
.toast-close {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  border-radius: 6px;
  border: none;
  background: transparent;
  color: #b8a8d0;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  transition: background 0.15s, color 0.15s;
  align-self: flex-start;
  margin-top: 1px;
  padding: 0;
}
.toast-close:hover {
  background: #f0ebfa;
  color: #4a3070;
}

/* ── Progress bar ──────────────────────────────────────────── */
.toast-progress-track {
  position: absolute;
  left: 0;
  bottom: 0;
  right: 0;
  height: 3px;
  background: transparent;
}

.toast-progress-fill {
  height: 100%;
  transition: width 50ms linear;
  border-radius: 0 0 12px 12px;
  opacity: 0.45;
}

.toast-success .toast-progress-fill { background: #16a34a; }
.toast-error   .toast-progress-fill { background: #dc2626; }
.toast-warning .toast-progress-fill { background: #d97706; }
.toast-info    .toast-progress-fill { background: #7c3aed; }

/* ── Animations ────────────────────────────────────────────── */
.toast-enter-active {
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.toast-leave-active {
  transition: all 0.2s ease-in;
  position: absolute;
}
.toast-enter-from {
  opacity: 0;
  transform: translateX(60px) scale(0.96);
}
.toast-leave-to {
  opacity: 0;
  transform: translateX(60px) scale(0.92);
}
.toast-move {
  transition: transform 0.28s ease;
}

/* ── Dark mode ─────────────────────────────────────────────── */
:global(.dark) .toast-item {
  background: #1e1530;
  border-color: rgba(255, 255, 255, 0.09);
  box-shadow:
    0 1px 3px rgba(0, 0, 0, 0.3),
    0 4px 16px rgba(0, 0, 0, 0.4);
}

/* Lighter icon colors for dark backgrounds */
:global(.dark) .toast-success .toast-icon { color: #4ade80; }
:global(.dark) .toast-error   .toast-icon { color: #f87171; }
:global(.dark) .toast-warning .toast-icon { color: #fbbf24; }
:global(.dark) .toast-info    .toast-icon { color: #a78bfa; }

:global(.dark) .toast-title   { color: #f0e8ff; }
:global(.dark) .toast-message { color: rgba(200, 180, 240, 0.7); }

:global(.dark) .toast-close         { color: rgba(200, 180, 240, 0.35); }
:global(.dark) .toast-close:hover   { background: rgba(255,255,255,0.07); color: #f0e8ff; }

/* Progress bar — keep same hue, slightly brighter in dark */
:global(.dark) .toast-success .toast-progress-fill { background: #4ade80; }
:global(.dark) .toast-error   .toast-progress-fill { background: #f87171; }
:global(.dark) .toast-warning .toast-progress-fill { background: #fbbf24; }
:global(.dark) .toast-info    .toast-progress-fill { background: #a78bfa; }
</style>
