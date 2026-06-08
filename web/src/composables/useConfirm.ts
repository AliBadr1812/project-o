/**
 * useConfirm — singleton promise-based confirmation dialog.
 * Replaces window.confirm() with a polished glass modal.
 *
 * Usage:
 *   const { confirm } = useConfirm()
 *   const ok = await confirm({
 *     title: 'Delete product',
 *     message: 'This action cannot be undone.',
 *     variant: 'danger',
 *     confirmText: 'Delete',
 *   })
 *   if (ok) { ... }
 */
import { ref } from 'vue';

export type ConfirmVariant = 'danger' | 'warning' | 'info';

export interface ConfirmOptions {
  title:        string;
  message:      string;
  confirmText?: string;
  cancelText?:  string;
  variant?:     ConfirmVariant;
  /** Optional detail line beneath the message */
  detail?:      string;
}

interface ConfirmState extends ConfirmOptions {
  show:    boolean;
  resolve: ((value: boolean) => void) | null;
}

const state = ref<ConfirmState>({
  show:        false,
  title:       '',
  message:     '',
  confirmText: 'Confirm',
  cancelText:  'Cancel',
  variant:     'danger',
  resolve:     null,
});

export function useConfirm() {
  function confirm(options: ConfirmOptions): Promise<boolean> {
    return new Promise<boolean>(resolve => {
      state.value = {
        show:        true,
        confirmText: 'Confirm',
        cancelText:  'Cancel',
        variant:     'danger',
        ...options,
        resolve,
      };
    });
  }

  function _respond(value: boolean) {
    state.value.resolve?.(value);
    state.value.show = false;
    state.value.resolve = null;
  }

  return { state, confirm, _respond };
}
