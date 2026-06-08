/**
 * useKeyboardShortcuts — app-wide keyboard shortcut registry
 *
 * Provides a singleton shortcut listener. Call `registerShortcut` to add a
 * shortcut; it returns an `unregister` function for cleanup.
 *
 * Built-in shortcuts (registered from App.vue):
 *   ⌘K / Ctrl+K  → open command palette
 *   N             → new item (context-aware)
 *   /             → focus sidebar search
 *   Escape        → close modals / go back
 *   ?             → open shortcut help overlay
 */

import { ref } from 'vue';

export type ShortcutHandler = (event: KeyboardEvent) => void;

export interface Shortcut {
  id:          string;
  key:         string;         // e.g. 'k', 'n', '/', 'Escape'
  meta?:       boolean;        // ⌘ or Ctrl
  shift?:      boolean;
  alt?:        boolean;
  description: string;
  group:       string;
  handler:     ShortcutHandler;
}

// ── Singleton state ────────────────────────────────────────────────────────
const shortcuts    = ref<Shortcut[]>([]);
const helpVisible  = ref(false);
let   listenerInstalled = false;

function matchesEvent(s: Shortcut, e: KeyboardEvent): boolean {
  const meta  = e.metaKey || e.ctrlKey;
  const key   = e.key.toLowerCase();
  return (
    key === s.key.toLowerCase() &&
    !!s.meta  === meta &&
    !!s.shift === e.shiftKey &&
    !!s.alt   === e.altKey
  );
}

function onKeyDown(e: KeyboardEvent) {
  // Skip when user is typing in an input / textarea / contenteditable
  const tag = (e.target as HTMLElement).tagName;
  const isInput = tag === 'INPUT' || tag === 'TEXTAREA' || tag === 'SELECT'
    || (e.target as HTMLElement).isContentEditable;

  for (const s of shortcuts.value) {
    if (matchesEvent(s, e)) {
      // Allow Escape to fire even inside inputs
      if (isInput && s.key !== 'Escape') continue;
      e.preventDefault();
      s.handler(e);
      return;
    }
  }
}

function ensureListener() {
  if (!listenerInstalled) {
    window.addEventListener('keydown', onKeyDown);
    listenerInstalled = true;
  }
}

// ── Public API ─────────────────────────────────────────────────────────────
export function useKeyboardShortcuts() {
  ensureListener();

  function registerShortcut(shortcut: Omit<Shortcut, 'id'> & { id?: string }): () => void {
    const id = shortcut.id ?? `${shortcut.key}-${Date.now()}`;
    // Avoid duplicate registration
    if (!shortcuts.value.find(s => s.id === id)) {
      shortcuts.value.push({ ...shortcut, id } as Shortcut);
    }
    return () => unregisterShortcut(id);
  }

  function unregisterShortcut(id: string) {
    const idx = shortcuts.value.findIndex(s => s.id === id);
    if (idx !== -1) shortcuts.value.splice(idx, 1);
  }

  function toggleHelp() {
    helpVisible.value = !helpVisible.value;
  }

  function hideHelp() {
    helpVisible.value = false;
  }

  return {
    shortcuts,
    helpVisible,
    registerShortcut,
    unregisterShortcut,
    toggleHelp,
    hideHelp,
  };
}
