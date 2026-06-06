<template>
  <footer
    class="px-6 py-4 mt-auto"
    style="border-top: 1px solid var(--glass-border);
           background: var(--glass-bg-header);
           backdrop-filter: blur(24px) saturate(180%);
           -webkit-backdrop-filter: blur(24px) saturate(180%);"
  >
    <div class="flex flex-col md:flex-row justify-between items-center gap-3">
      <div>
        <p class="text-sm font-medium" style="color: var(--text-secondary);">
          © {{ currentYear }} ShopAdmin. All rights reserved.
        </p>
        <p class="text-xs mt-0.5" style="color: var(--text-muted);">Version {{ appVersion }}</p>
      </div>

      <div class="flex items-center gap-5">
        <a v-for="link in links" :key="link" href="#"
           class="text-sm transition-colors hover:text-[var(--accent)]"
           style="color: var(--text-muted);">
          {{ link }}
        </a>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
const currentYear = new Date().getFullYear();
const appVersion  = 'v1.0.0';
const links       = ['Documentation', 'Support', 'API', 'Status'];
</script>
