/**
 * useToast — singleton toast notification system.
 * Module-level state is shared across all components.
 *
 * Usage:
 *   const toast = useToast()
 *   toast.success('Saved!')
 *   toast.error('Something went wrong', 'Error')
 *   toast.warning('Low stock detected')
 *   toast.info('Settings updated')
 */
import { ref } from 'vue';

export type ToastVariant = 'success' | 'error' | 'warning' | 'info';

export interface Toast {
  id: number;
  message: string;
  variant: ToastVariant;
  title?: string;
  duration: number;
  /** 0–100, drives the shrinking progress bar */
  progress: number;
}

const toasts = ref<Toast[]>([]);
const timers  = new Map<number, ReturnType<typeof setInterval>>();
let   counter = 0;

export function useToast() {
  function push(
    message: string,
    variant:  ToastVariant = 'info',
    title?:   string,
    duration  = 4000,
  ): number {
    const id = ++counter;
    toasts.value.push({ id, message, variant, title, duration, progress: 100 });

    if (duration > 0) {
      const step     = 50;                         // tick every 50 ms
      const decrement = (step / duration) * 100;   // % to subtract per tick

      const interval = setInterval(() => {
        const t = toasts.value.find(x => x.id === id);
        if (!t) { clearInterval(interval); return; }
        t.progress -= decrement;
        if (t.progress <= 0) {
          clearInterval(interval);
          timers.delete(id);
          dismiss(id);
        }
      }, step);

      timers.set(id, interval);
    }

    return id;
  }

  function dismiss(id: number) {
    const interval = timers.get(id);
    if (interval) { clearInterval(interval); timers.delete(id); }
    const i = toasts.value.findIndex(t => t.id === id);
    if (i !== -1) toasts.value.splice(i, 1);
  }

  function dismissAll() {
    timers.forEach((iv) => clearInterval(iv));
    timers.clear();
    toasts.value = [];
  }

  return {
    toasts,
    dismiss,
    dismissAll,
    success: (msg: string, title?: string, duration?: number) => push(msg, 'success', title, duration),
    error:   (msg: string, title?: string, duration?: number) => push(msg, 'error',   title, duration ?? 6000),
    warning: (msg: string, title?: string, duration?: number) => push(msg, 'warning', title, duration),
    info:    (msg: string, title?: string, duration?: number) => push(msg, 'info',    title, duration),
  };
}
