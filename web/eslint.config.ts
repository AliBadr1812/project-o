import { globalIgnores } from 'eslint/config'
import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript'
import pluginVue from 'eslint-plugin-vue'
import pluginVitest from '@vitest/eslint-plugin'

// To allow more languages other than `ts` in `.vue` files, uncomment the following lines:
// import { configureVueProject } from '@vue/eslint-config-typescript'
// configureVueProject({ scriptLangs: ['ts', 'tsx'] })
// More info at https://github.com/vuejs/eslint-config-typescript/#advanced-setup

export default defineConfigWithVueTs(
  {
    name: 'app/files-to-lint',
    files: ['**/*.{vue,ts,mts,tsx}'],
  },

  globalIgnores(['**/dist/**', '**/dist-ssr/**', '**/coverage/**']),

  ...pluginVue.configs['flat/essential'],
  vueTsConfigs.recommended,

  {
    ...pluginVitest.configs.recommended,
    files: ['src/**/__tests__/*'],
  },

  // ── Project-level rule overrides ──────────────────────────────────────────
  {
    name: 'app/rule-overrides',
    rules: {
      // Many well-established single-word component names are used throughout
      // (Badge, Button, Card, Footer, Modal, Pagination, Sidebar, Sparkline,
      //  Table, Breadcrumb, Dashboard). Renaming them all would be very
      // disruptive, so we allow known single-word exceptions.
      'vue/multi-word-component-names': ['error', {
        ignores: [
          'Badge', 'Button', 'Card', 'Header', 'Footer', 'Modal',
          'Pagination', 'Sidebar', 'Sparkline', 'Table',
          'Breadcrumb', 'Dashboard',
        ],
      }],
    },
  },
)
