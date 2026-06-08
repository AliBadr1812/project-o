<template>
  <Teleport to="body">
    <Transition name="ks-backdrop">
      <div v-if="helpVisible"
        class="fixed inset-0 z-[10001] flex items-center justify-center"
        style="background: rgba(0,0,0,0.55); backdrop-filter: blur(4px);"
        @click.self="hideHelp">

        <Transition name="ks-panel" appear>
          <div v-if="helpVisible"
            class="glass-card w-full max-w-lg mx-4 overflow-hidden"
            style="max-height: 80vh; display: flex; flex-direction: column;">

            <!-- Header -->
            <div class="flex items-center justify-between px-6 py-4"
              style="border-bottom: 1px solid var(--glass-border);">
              <div class="flex items-center gap-3">
                <div class="w-8 h-8 rounded-lg flex items-center justify-center text-sm"
                  style="background: rgba(var(--accent-rgb),0.12); color: var(--accent);">
                  <i class="fas fa-keyboard"></i>
                </div>
                <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Keyboard Shortcuts</h2>
              </div>
              <button @click="hideHelp"
                class="btn-glass-icon w-8 h-8 rounded-xl text-xs"
                title="Close (Esc)">
                <i class="fas fa-xmark"></i>
              </button>
            </div>

            <!-- Shortcut list -->
            <div class="overflow-y-auto flex-1 px-6 py-4 flex flex-col gap-5">
              <div v-for="group in groupedShortcuts" :key="group.name">
                <p class="text-[11px] font-semibold uppercase tracking-widest mb-2"
                  style="color: var(--text-muted);">
                  {{ group.name }}
                </p>
                <div class="flex flex-col gap-1.5">
                  <div v-for="s in group.items" :key="s.id"
                    class="flex items-center justify-between py-2 px-3 rounded-xl"
                    style="background: var(--glass-bg); border: 1px solid var(--glass-border);">
                    <span class="text-sm" style="color: var(--text-secondary);">{{ s.description }}</span>
                    <div class="flex items-center gap-1">
                      <kbd v-if="s.meta" class="ks-key">{{ isMac ? '⌘' : 'Ctrl' }}</kbd>
                      <kbd v-if="s.shift" class="ks-key">⇧</kbd>
                      <kbd v-if="s.alt" class="ks-key">{{ isMac ? '⌥' : 'Alt' }}</kbd>
                      <kbd class="ks-key ks-key--main">{{ displayKey(s.key) }}</kbd>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Footer hint -->
            <div class="px-6 py-3 text-xs text-center"
              style="border-top: 1px solid var(--glass-border); color: var(--text-muted);">
              Press <kbd class="ks-key mx-1">?</kbd> to toggle this overlay · <kbd class="ks-key mx-1">Esc</kbd> to close
            </div>
          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useKeyboardShortcuts } from '@/composables/useKeyboardShortcuts';

const { shortcuts, helpVisible, hideHelp } = useKeyboardShortcuts();

const isMac = navigator.platform.toUpperCase().includes('MAC');

// Group shortcuts by their `group` field
const groupedShortcuts = computed(() => {
  const map = new Map<string, typeof shortcuts.value>();
  for (const s of shortcuts.value) {
    if (!map.has(s.group)) map.set(s.group, []);
    map.get(s.group)!.push(s);
  }
  return [...map.entries()].map(([name, items]) => ({ name, items }));
});

function displayKey(key: string): string {
  const labels: Record<string, string> = {
    escape:    'Esc',
    arrowup:   '↑',
    arrowdown: '↓',
    arrowleft: '←',
    arrowright:'→',
    enter:     '↵',
    '/':       '/',
    '?':       '?',
  };
  return labels[key.toLowerCase()] ?? key.toUpperCase();
}
</script>

<style scoped>
.ks-key {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 24px;
  height: 24px;
  padding: 0 6px;
  border-radius: 6px;
  font-family: ui-monospace, monospace;
  font-size: 11px;
  font-weight: 600;
  background: var(--glass-bg);
  border: 1px solid var(--glass-border);
  box-shadow: inset 0 1px 0 rgba(255,255,255,0.3), 0 1px 2px rgba(0,0,0,0.15);
  color: var(--text-secondary);
}
.ks-key--main {
  color: var(--accent);
  background: rgba(var(--accent-rgb), 0.08);
  border-color: rgba(var(--accent-rgb), 0.2);
}

/* Backdrop transition */
.ks-backdrop-enter-active,
.ks-backdrop-leave-active { transition: opacity 0.2s ease; }
.ks-backdrop-enter-from,
.ks-backdrop-leave-to    { opacity: 0; }

/* Panel transition */
.ks-panel-enter-active  { transition: opacity 0.25s ease, transform 0.25s cubic-bezier(0.34,1.56,0.64,1); }
.ks-panel-leave-active  { transition: opacity 0.15s ease, transform 0.15s ease; }
.ks-panel-enter-from    { opacity: 0; transform: scale(0.93) translateY(-12px); }
.ks-panel-leave-to      { opacity: 0; transform: scale(0.96) translateY(8px); }

/* Dark mode */
:global(.dark) .ks-key {
  background: rgba(255,255,255,0.06);
  border-color: rgba(255,255,255,0.12);
}
</style>
