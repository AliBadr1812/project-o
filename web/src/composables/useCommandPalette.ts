import { ref } from 'vue';

// Module-level singleton so any component can open/close the palette
const open = ref(false);

export function useCommandPalette() {
  function openPalette()   { open.value = true;  }
  function closePalette()  { open.value = false; }
  function togglePalette() { open.value = !open.value; }

  return { open, openPalette, closePalette, togglePalette };
}
