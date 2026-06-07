/**
 * useTheme — singleton composable for accent theme + dark/light mode.
 *
 * Module-level refs are shared across all component instances, so any
 * component that calls useTheme() reads and mutates the same state.
 *
 * Preferences are persisted to:
 *  1. localStorage (immediate, offline-capable)
 *  2. backend API  /user-preferences/{userId} (fire-and-forget, cross-device)
 */
import { ref } from 'vue';
import api from '@/services/api';

// ── Types ─────────────────────────────────────────────────────────────
export type ThemeId = 'purple' | 'blue' | 'green' | 'orange';

interface ModeTokens {
  blobs:       [string, string, string, string, string];
  wallpaper:   string;
  glassBg:     string;
  glassBgStrong:  string;
  glassBgSidebar: string;
  glassBgHeader:  string;
}

interface ThemePalette {
  accent:      string;
  accentLight: string;
  accentDark:  string;
  /** Comma-separated r,g,b — used for rgba(var(--accent-rgb), α) */
  accentRGB:   string;
  light:       ModeTokens;
  dark:        ModeTokens;
}

// ── Palette definitions ───────────────────────────────────────────────
const THEMES: Record<ThemeId, ThemePalette> = {

  purple: {
    accent: '#7c3aed', accentLight: '#b97fff', accentDark: '#5e5ce6',
    accentRGB: '124,58,237',
    light: {
      blobs: [
        'rgba(200,160,255,0.55)', 'rgba(120,180,255,0.45)',
        'rgba(255,180,220,0.40)', 'rgba(100,220,200,0.40)',
        'rgba(255,240,200,0.35)',
      ],
      wallpaper: 'linear-gradient(160deg,#f0e8ff 0%,#e0f0ff 40%,#e8fff8 80%,#fff8f0 100%)',
      glassBg:        'rgba(255,255,255,0.48)',
      glassBgStrong:  'rgba(255,255,255,0.60)',
      glassBgSidebar: 'rgba(255,255,255,0.30)',
      glassBgHeader:  'rgba(255,255,255,0.38)',
    },
    dark: {
      blobs: [
        'rgba(120,60,200,0.42)', 'rgba(20,40,160,0.38)',
        'rgba(150,20,100,0.30)', 'rgba(10,100,100,0.28)',
        'rgba(80,30,140,0.22)',
      ],
      wallpaper: 'linear-gradient(160deg,#0d0320 0%,#090e28 40%,#061818 80%,#110420 100%)',
      glassBg:        'rgba(20,10,45,0.55)',
      glassBgStrong:  'rgba(28,14,58,0.68)',
      glassBgSidebar: 'rgba(12,6,28,0.68)',
      glassBgHeader:  'rgba(16,8,36,0.62)',
    },
  },

  blue: {
    accent: '#007aff', accentLight: '#5ac8fa', accentDark: '#0055d4',
    accentRGB: '0,122,255',
    light: {
      blobs: [
        'rgba(90,200,250,0.55)', 'rgba(0,122,255,0.35)',
        'rgba(180,220,255,0.45)', 'rgba(100,215,210,0.35)',
        'rgba(210,235,255,0.35)',
      ],
      wallpaper: 'linear-gradient(160deg,#e0f0ff 0%,#f0f8ff 40%,#e0f7ff 80%,#f5faff 100%)',
      glassBg:        'rgba(240,248,255,0.50)',
      glassBgStrong:  'rgba(248,252,255,0.62)',
      glassBgSidebar: 'rgba(230,244,255,0.32)',
      glassBgHeader:  'rgba(235,246,255,0.40)',
    },
    dark: {
      blobs: [
        'rgba(0,60,180,0.50)', 'rgba(0,20,120,0.38)',
        'rgba(10,80,150,0.30)', 'rgba(5,70,100,0.28)',
        'rgba(0,30,100,0.22)',
      ],
      wallpaper: 'linear-gradient(160deg,#020a1f 0%,#040d2a 40%,#021520 80%,#020718 100%)',
      glassBg:        'rgba(5,15,40,0.60)',
      glassBgStrong:  'rgba(8,20,55,0.72)',
      glassBgSidebar: 'rgba(3,10,28,0.72)',
      glassBgHeader:  'rgba(4,12,36,0.65)',
    },
  },

  green: {
    accent: '#16a34a', accentLight: '#4ade80', accentDark: '#15803d',
    accentRGB: '22,163,74',
    light: {
      blobs: [
        'rgba(74,222,128,0.50)', 'rgba(16,163,74,0.35)',
        'rgba(180,255,205,0.42)', 'rgba(100,220,180,0.35)',
        'rgba(200,255,225,0.30)',
      ],
      wallpaper: 'linear-gradient(160deg,#e8fff0 0%,#f0fff8 40%,#e5fff0 80%,#f5fff8 100%)',
      glassBg:        'rgba(240,255,245,0.50)',
      glassBgStrong:  'rgba(248,255,250,0.62)',
      glassBgSidebar: 'rgba(228,255,240,0.32)',
      glassBgHeader:  'rgba(234,255,243,0.40)',
    },
    dark: {
      blobs: [
        'rgba(10,100,40,0.50)', 'rgba(5,80,30,0.38)',
        'rgba(20,60,40,0.30)', 'rgba(5,80,60,0.28)',
        'rgba(10,50,25,0.22)',
      ],
      wallpaper: 'linear-gradient(160deg,#011208 0%,#01180a 40%,#030f06 80%,#011004 100%)',
      glassBg:        'rgba(5,20,10,0.60)',
      glassBgStrong:  'rgba(8,28,14,0.72)',
      glassBgSidebar: 'rgba(3,14,7,0.72)',
      glassBgHeader:  'rgba(4,18,9,0.65)',
    },
  },

  orange: {
    accent: '#ea580c', accentLight: '#fb923c', accentDark: '#c2410c',
    accentRGB: '234,88,12',
    light: {
      blobs: [
        'rgba(251,146,60,0.50)', 'rgba(234,88,12,0.35)',
        'rgba(255,210,160,0.42)', 'rgba(255,160,100,0.32)',
        'rgba(255,235,205,0.35)',
      ],
      wallpaper: 'linear-gradient(160deg,#fff5e8 0%,#fff0e0 40%,#ffe8d8 80%,#fff8f0 100%)',
      glassBg:        'rgba(255,248,240,0.50)',
      glassBgStrong:  'rgba(255,252,248,0.62)',
      glassBgSidebar: 'rgba(255,244,232,0.32)',
      glassBgHeader:  'rgba(255,246,236,0.40)',
    },
    dark: {
      blobs: [
        'rgba(120,40,5,0.50)', 'rgba(100,30,5,0.38)',
        'rgba(80,25,5,0.30)', 'rgba(60,30,10,0.28)',
        'rgba(90,25,5,0.22)',
      ],
      wallpaper: 'linear-gradient(160deg,#1a0800 0%,#1f0d04 40%,#150500 80%,#200d02 100%)',
      glassBg:        'rgba(30,10,5,0.62)',
      glassBgStrong:  'rgba(40,14,6,0.74)',
      glassBgSidebar: 'rgba(20,7,3,0.72)',
      glassBgHeader:  'rgba(26,9,4,0.66)',
    },
  },
};

// ── Singleton state ───────────────────────────────────────────────────
const isDark   = ref(false);
const themeId  = ref<ThemeId>('purple');

const LS_DARK    = 'sa-dark';
const LS_THEME   = 'sa-theme';
const PREF_USER  = 'default'; // single-admin-user setup

// ── Backend persistence (fire-and-forget) ─────────────────────────────
function saveToBackend(dark: boolean, id: ThemeId) {
  api.put(`/user-preferences/${PREF_USER}`, { themeId: id, darkMode: dark })
     .catch(() => { /* backend unavailable — localStorage is the fallback */ });
}

// ── Core apply ────────────────────────────────────────────────────────
function applyAll(dark: boolean, id: ThemeId) {
  const root = document.documentElement;
  const t    = THEMES[id];
  const mode = dark ? t.dark : t.light;

  // dark class
  root.classList.toggle('dark', dark);

  // accent
  root.style.setProperty('--accent',       t.accent);
  root.style.setProperty('--accent-light', t.accentLight);
  root.style.setProperty('--accent-dark',  t.accentDark);
  root.style.setProperty('--accent-rgb',   t.accentRGB);
  root.style.setProperty('--accent-hover', `rgba(${t.accentRGB},0.12)`);

  // text-accent is lighter in dark mode
  root.style.setProperty('--text-accent',  dark ? t.accentLight : t.accent);

  // input ring + scrollbar
  root.style.setProperty('--input-focus-ring', `rgba(${t.accentRGB},${dark ? '0.35' : '0.28'})`);
  root.style.setProperty('--scrollbar-thumb',  `rgba(${t.accentRGB},${dark ? '0.40' : '0.28'})`);

  // badge-info (accent-derived)
  root.style.setProperty('--badge-info-bg',     `rgba(${t.accentRGB},${dark ? '0.18' : '0.12'})`);
  root.style.setProperty('--badge-info-color',  dark ? t.accentLight : t.accent);
  root.style.setProperty('--badge-info-border', `rgba(${t.accentRGB},${dark ? '0.26' : '0.20'})`);

  // ni-p (purple slot becomes the accent colour)
  root.style.setProperty('--ni-purple',    t.accent);
  root.style.setProperty('--ni-purple-bg', `rgba(${t.accentRGB},${dark ? '0.22' : '0.09'})`);

  // glass surfaces
  root.style.setProperty('--glass-bg',           mode.glassBg);
  root.style.setProperty('--glass-bg-strong',    mode.glassBgStrong);
  root.style.setProperty('--glass-bg-sidebar',   mode.glassBgSidebar);
  root.style.setProperty('--glass-bg-header',    mode.glassBgHeader);

  // wallpaper
  mode.blobs.forEach((b, i) => root.style.setProperty(`--blob-${i + 1}`, b));
  root.style.setProperty('--wallpaper-base', mode.wallpaper);
}

// ── Public API ────────────────────────────────────────────────────────
export function useTheme() {

  function setTheme(id: ThemeId) {
    themeId.value = id;
    applyAll(isDark.value, id);
    localStorage.setItem(LS_THEME, id);
    saveToBackend(isDark.value, id);
  }

  function toggleDark() {
    isDark.value = !isDark.value;
    applyAll(isDark.value, themeId.value);
    localStorage.setItem(LS_DARK, isDark.value ? '1' : '0');
    saveToBackend(isDark.value, themeId.value);
  }

  function setDark(value: boolean) {
    isDark.value = value;
    applyAll(value, themeId.value);
    localStorage.setItem(LS_DARK, value ? '1' : '0');
    saveToBackend(value, themeId.value);
  }

  /**
   * Call once in App.vue onMounted to restore persisted preferences.
   * Tries the backend first; falls back to localStorage if unavailable.
   */
  async function init() {
    // 1. Apply localStorage immediately so the UI doesn't flash
    const savedDark  = localStorage.getItem(LS_DARK);
    const savedTheme = localStorage.getItem(LS_THEME) as ThemeId | null;
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

    isDark.value  = savedDark != null ? savedDark === '1' : prefersDark;
    themeId.value = (savedTheme && savedTheme in THEMES) ? savedTheme : 'purple';
    applyAll(isDark.value, themeId.value);

    // 2. Fetch backend preferences and reconcile (backend wins if present)
    try {
      const { data: res } = await api.get<{ data: { themeId: string; darkMode: boolean } | null }>(
        `/user-preferences/${PREF_USER}`
      );
      const prefs = res?.data;
      if (prefs) {
        const remoteTheme = (prefs.themeId in THEMES ? prefs.themeId : 'purple') as ThemeId;
        const remoteDark  = prefs.darkMode ?? false;
        // Only re-apply if different from what we already have
        if (remoteTheme !== themeId.value || remoteDark !== isDark.value) {
          isDark.value  = remoteDark;
          themeId.value = remoteTheme;
          applyAll(isDark.value, themeId.value);
          localStorage.setItem(LS_THEME, themeId.value);
          localStorage.setItem(LS_DARK, isDark.value ? '1' : '0');
        }
      }
    } catch {
      // Backend not available — localStorage preferences are already applied
    }
  }

  return { isDark, themeId, setTheme, toggleDark, setDark, init };
}
