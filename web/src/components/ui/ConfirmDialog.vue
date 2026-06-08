<template>
  <Teleport to="body">
    <Transition name="confirm-backdrop">
      <div
        v-if="state.show"
        class="confirm-backdrop"
        @click.self="onCancel"
        role="dialog"
        aria-modal="true"
        :aria-labelledby="`confirm-title-${uid}`"
        :aria-describedby="`confirm-msg-${uid}`"
      >
        <Transition name="confirm-panel" appear>
          <div v-if="state.show" class="confirm-panel">

            <!-- Icon -->
            <div class="confirm-icon-wrap" :class="`confirm-icon-${state.variant}`">
              <i :class="iconClass"></i>
            </div>

            <!-- Title -->
            <h3 :id="`confirm-title-${uid}`" class="confirm-title">{{ state.title }}</h3>

            <!-- Message -->
            <p :id="`confirm-msg-${uid}`" class="confirm-message">{{ state.message }}</p>

            <!-- Optional detail -->
            <p v-if="state.detail" class="confirm-detail">{{ state.detail }}</p>

            <!-- Actions -->
            <div class="confirm-actions">
              <button
                class="btn-glass confirm-cancel"
                @click="onCancel"
                ref="cancelBtn"
              >
                {{ state.cancelText || 'Cancel' }}
              </button>
              <button
                class="confirm-confirm"
                :class="`confirm-confirm-${state.variant}`"
                @click="onConfirm"
              >
                {{ state.confirmText || 'Confirm' }}
              </button>
            </div>

          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import { computed, watch, nextTick, ref } from 'vue';
import { useConfirm } from '@/composables/useConfirm';

const { state, _respond } = useConfirm();
const uid = Math.random().toString(36).slice(2, 7);
const cancelBtn = ref<HTMLButtonElement | null>(null);

const iconClass = computed(() => ({
  danger:  'fas fa-triangle-exclamation',
  warning: 'fas fa-circle-exclamation',
  info:    'fas fa-circle-info',
}[state.value.variant ?? 'danger']));

watch(() => state.value.show, async (v) => {
  if (v) {
    await nextTick();
    cancelBtn.value?.focus();
  }
});

function onConfirm() { _respond(true); }
function onCancel()  { _respond(false); }

// Allow Escape to cancel
function onKeydown(e: KeyboardEvent) {
  if (state.value.show && e.key === 'Escape') onCancel();
  if (state.value.show && e.key === 'Enter')  onConfirm();
}

document.addEventListener('keydown', onKeydown);
</script>

<style scoped>
/* Backdrop */
.confirm-backdrop {
  position: fixed;
  inset: 0;
  z-index: 10000;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  background: rgba(10, 5, 20, 0.45);
  backdrop-filter: blur(6px);
  -webkit-backdrop-filter: blur(6px);
}

/* Panel */
.confirm-panel {
  width: 100%;
  max-width: 420px;
  border-radius: 20px;
  padding: 32px 28px 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 0;
  backdrop-filter: blur(24px) saturate(1.5);
  -webkit-backdrop-filter: blur(24px) saturate(1.5);
  background: var(--glass-bg-strong, rgba(255,255,255,0.62));
  border: 1px solid rgba(255,255,255,0.35);
  box-shadow:
    0 24px 60px rgba(20,10,40,0.22),
    0 4px 16px rgba(20,10,40,0.12),
    inset 0 1px 0 rgba(255,255,255,0.55);
}

/* Icon */
.confirm-icon-wrap {
  width: 56px;
  height: 56px;
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  margin-bottom: 18px;
  border: 1px solid rgba(255,255,255,0.4);
}
.confirm-icon-danger  { background: rgba(239, 68, 68, 0.12);  color: #ef4444; }
.confirm-icon-warning { background: rgba(245,158, 11, 0.12);  color: #f59e0b; }
.confirm-icon-info    { background: rgba(124, 58,237, 0.12);  color: var(--accent, #7c3aed); }

/* Text */
.confirm-title {
  font-size: 17px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 8px;
}
.confirm-message {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 0;
  line-height: 1.55;
}
.confirm-detail {
  font-size: 12px;
  color: var(--text-muted);
  margin: 6px 0 0;
  padding: 8px 12px;
  border-radius: 10px;
  background: rgba(0,0,0,0.04);
  width: 100%;
}

/* Buttons */
.confirm-actions {
  display: flex;
  gap: 10px;
  width: 100%;
  margin-top: 24px;
}
.confirm-cancel,
.confirm-confirm {
  flex: 1;
  height: 42px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.15s ease;
  border: none;
  outline: none;
}
.confirm-confirm {
  color: #fff;
}
.confirm-confirm-danger  { background: linear-gradient(135deg, #ef4444, #dc2626); box-shadow: 0 4px 12px rgba(239,68,68,0.35); }
.confirm-confirm-warning { background: linear-gradient(135deg, #f59e0b, #d97706); box-shadow: 0 4px 12px rgba(245,158,11,0.35); }
.confirm-confirm-info    { background: linear-gradient(135deg, var(--accent-light, #b97fff), var(--accent, #7c3aed)); box-shadow: 0 4px 12px rgba(124,58,237,0.35); }
.confirm-confirm:hover   { filter: brightness(1.1); transform: translateY(-1px); }
.confirm-confirm:active  { filter: brightness(0.95); transform: translateY(0); }

/* Backdrop transition */
.confirm-backdrop-enter-active,
.confirm-backdrop-leave-active { transition: opacity 0.2s ease; }
.confirm-backdrop-enter-from,
.confirm-backdrop-leave-to     { opacity: 0; }

/* Panel transition */
.confirm-panel-enter-active { transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1); }
.confirm-panel-leave-active { transition: all 0.18s ease-in; }
.confirm-panel-enter-from   { opacity: 0; transform: scale(0.88) translateY(12px); }
.confirm-panel-leave-to     { opacity: 0; transform: scale(0.92) translateY(6px); }

/* Dark mode */
:global(.dark) .confirm-panel {
  background: var(--glass-bg-strong, rgba(20,10,45,0.72));
  border-color: rgba(255,255,255,0.10);
}
:global(.dark) .confirm-detail { background: rgba(255,255,255,0.05); }
</style>
