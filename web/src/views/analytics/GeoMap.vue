<!-- components/analytics/GeoMap.vue -->
<template>
  <div class="geo-map">
    <div v-if="loading" class="absolute inset-0 flex items-center justify-center bg-[var(--glass-bg)]/80 z-10 rounded-lg">
      <i class="fas fa-spinner fa-spin text-3xl text-[var(--accent)]"></i>
    </div>
    <div class="relative" style="height: 200px; width: 100%;">
      <l-map
        ref="map"
        v-model:zoom="zoom"
        :center="center"
        :use-global-leaflet="false"
        @ready="onMapReady"
      >
        <l-tile-layer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        ></l-tile-layer>

        <!-- Markers for top regions -->
        <l-marker
          v-for="region in regions"
          :key="region.name"
          :lat-lng="region.coordinates"
          @click="showRegionDetails(region)"
        >
          <l-popup>
            <div class="text-gray-900 p-2">
              <h3 class="font-bold">{{ region.name }}</h3>
              <p class="text-sm">Revenue: {{ formatCurrency(region.revenue) }}</p>
              <p class="text-sm">Orders: {{ region.orders }}</p>
              <p class="text-xs text-[var(--text-secondary)] mt-1">{{ region.percentage }}% of total</p>
            </div>
          </l-popup>

          <!-- Custom marker with revenue indicator -->
          <l-icon
            :icon-size="[40, 40]"
            :icon-anchor="[20, 20]"
          >
            <div
              class="relative flex items-center justify-center"
              :style="{
                width: '40px',
                height: '40px'
              }"
            >
              <!-- Outer ring with size based on revenue -->
              <div
                class="absolute rounded-full animate-pulse"
                :style="{
                  width: `${30 + (region.percentage * 0.5)}px`,
                  height: `${30 + (region.percentage * 0.5)}px`,
                  backgroundColor: 'rgba(59, 130, 246, 0.2)',
                  border: '2px solid #3B82F6'
                }"
              ></div>
              <!-- Inner circle -->
              <div
                class="absolute rounded-full flex items-center justify-center text-[var(--text-primary)] text-xs font-bold"
                :style="{
                  width: '30px',
                  height: '30px',
                  background: 'linear-gradient(135deg, #3B82F6, #2563EB)',
                  boxShadow: '0 2px 10px rgba(59, 130, 246, 0.5)'
                }"
              >
                {{ region.percentage }}%
              </div>
            </div>
          </l-icon>
        </l-marker>

        <!-- Heatmap layer (optional, requires additional plugin) -->
        <l-control position="topright">
          <div class="bg-[var(--glass-bg)] border border-[var(--glass-border)] rounded-lg shadow-lg p-2">
            <button
              @click="toggleHeatmap"
              class="px-3 py-1.5 text-sm text-[var(--text-secondary)] hover:bg-[rgba(255,255,255,0.35)] rounded transition-colors"
            >
              <i class="fas fa-fire mr-2" :class="{ 'text-red-500': showHeatmap }"></i>
              {{ showHeatmap ? 'Hide' : 'Show' }} Heatmap
            </button>
          </div>
        </l-control>
      </l-map>
    </div>

    <!-- Legend -->
    <div class="mt-4 p-4 bg-[var(--glass-bg)] border border-[var(--glass-border)] rounded-lg">
      <div class="flex items-center justify-between">
        <h3 class="text-sm font-medium text-[var(--text-secondary)]">Market Distribution</h3>
        <div class="flex items-center gap-4">
          <div class="flex items-center gap-2">
            <span class="w-3 h-3 bg-blue-500 rounded-full"></span>
            <span class="text-xs text-[var(--text-secondary)]">Revenue centers</span>
          </div>
          <div class="flex items-center gap-2">
            <div class="w-3 h-3 border-2 border-[var(--accent)] rounded-full animate-pulse"></div>
            <span class="text-xs text-[var(--text-secondary)]">Active regions</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { LMap, LTileLayer, LMarker, LPopup, LIcon, LControl } from '@vue-leaflet/vue-leaflet';
import 'leaflet/dist/leaflet.css';
import { formatCurrency } from '@/utils/formatters';

// Fix Leaflet's default icon issue
import L from 'leaflet';
delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png',
});

const props = defineProps<{
  regions: Array<{
    name: string;
    coordinates: [number, number];
    revenue: number;
    orders: number;
    percentage: number;
  }>;
}>();

const zoom = ref(2);
const center = ref<[number, number]>([30, 0]); // Center on world
const loading = ref(false);
const showHeatmap = ref(false);
const map = ref<typeof LMap | null>(null);

const onMapReady = () => {
  loading.value = false;
};

const showRegionDetails = (region: any) => {
  console.log('Region clicked:', region);
  // You can emit an event to show more details
};

const toggleHeatmap = () => {
  showHeatmap.value = !showHeatmap.value;
  // Implement heatmap logic here
};

// Watch for region changes
watch(() => props.regions, (newRegions) => {
    if (newRegions.length > 0) {
        // Center map on first region or calculate bounds
        center.value = newRegions[0].coordinates;
    }
}, { deep: true });
</script>

<style scoped>
.geo-map {
  position: relative;
  width: 100%;
  border-radius: 0.5rem;
  overflow: hidden;
}

/* Custom marker animations */
@keyframes pulse-ring {
  0% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.2;
  }
  100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
}

/* Fix Leaflet popup styling to match your theme */
:deep(.leaflet-popup-content-wrapper) {
  background: var(--glass-bg);
  color: white;
  border-radius: 0.5rem;
  border: 1px solid var(--color-border);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
}

:deep(.leaflet-popup-tip) {
  background: var(--glass-bg);
  border: 1px solid var(--color-border);
}

:deep(.leaflet-popup-close-button) {
  color: var(--color-icon) !important;
}

:deep(.leaflet-popup-close-button:hover) {
  color: white !important;
}

:deep(.leaflet-control-attribution) {
  background: rgba(0, 0, 0, 0.5) !important;
  color: var(--color-icon) !important;
  font-size: 0.7rem !important;
}

:deep(.leaflet-control-attribution a) {
  color: #3B82F6 !important;
}
</style>
