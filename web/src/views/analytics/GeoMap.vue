<template>
  <div ref="mapContainer" class="geo-map-container"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { formatCurrency } from '@/utils/formatters';

const props = defineProps<{
  regions: Array<{
    name:        string;
    coordinates: [number, number];
    revenue:     number;
    orders:      number;
    percentage:  number;
  }>;
}>();

const mapContainer = ref<HTMLDivElement | null>(null);
let map: L.Map | null = null;
const markers: L.Marker[] = [];

// Accent colours for the bubbles (cycles if there are more than 5 regions)
const bubbleColors = [
  { bg: '#7c3aed', pulse: 'rgba(124,58,237,0.25)' },
  { bg: '#2563eb', pulse: 'rgba(37, 99,235,0.25)'  },
  { bg: '#059669', pulse: 'rgba(  5,150,105,0.25)' },
  { bg: '#d97706', pulse: 'rgba(217,119,  6,0.25)' },
  { bg: '#dc2626', pulse: 'rgba(220, 38, 38,0.25)' },
];

function buildIcon(region: typeof props.regions[number], index: number): L.DivIcon {
  const color = bubbleColors[index % bubbleColors.length]!;
  const size  = 28 + Math.round(region.percentage * 0.55); // 28–57 px

  return L.divIcon({
    className: '',   // no leaflet default white box
    iconSize:    [size, size],
    iconAnchor:  [size / 2, size / 2],
    popupAnchor: [0, -(size / 2 + 6)],
    html: `
      <div style="
        position: relative;
        width:  ${size}px;
        height: ${size}px;
        display: flex;
        align-items: center;
        justify-content: center;
      ">
        <!-- pulsing ring -->
        <div style="
          position: absolute;
          inset: -6px;
          border-radius: 50%;
          background: ${color.pulse};
          animation: geo-pulse 2.4s ease-in-out infinite;
        "></div>
        <!-- main circle -->
        <div style="
          position: relative;
          width:  ${size}px;
          height: ${size}px;
          border-radius: 50%;
          background: ${color.bg};
          box-shadow: 0 2px 12px ${color.pulse.replace('0.25','0.55')};
          border: 2px solid rgba(255,255,255,0.55);
          display: flex;
          align-items: center;
          justify-content: center;
          color: #fff;
          font-size: ${Math.max(9, size * 0.28)}px;
          font-weight: 700;
          font-family: -apple-system, BlinkMacSystemFont, 'SF Pro Display', sans-serif;
          line-height: 1;
        ">${region.percentage}%</div>
      </div>`,
  });
}

function buildPopup(region: typeof props.regions[number]): string {
  return `
    <div class="geo-popup">
      <p class="geo-popup-title">${region.name}</p>
      <div class="geo-popup-row"><span>Revenue</span><strong>${formatCurrency(region.revenue)}</strong></div>
      <div class="geo-popup-row"><span>Orders</span><strong>${region.orders.toLocaleString()}</strong></div>
      <div class="geo-popup-row"><span>Share</span><strong>${region.percentage}%</strong></div>
    </div>`;
}

function addMarkers() {
  if (!map) return;
  markers.forEach(m => m.remove());
  markers.length = 0;

  props.regions.forEach((region, i) => {
    const marker = L.marker(region.coordinates, { icon: buildIcon(region, i) })
      .bindPopup(buildPopup(region), {
        className: 'geo-popup-wrapper',
        maxWidth: 200,
        offset: [0, 0],
      });
    marker.addTo(map!);
    markers.push(marker);
  });
}

onMounted(() => {
  if (!mapContainer.value) return;

  map = L.map(mapContainer.value, {
    center:          [20, 10],
    zoom:            2,
    zoomControl:     false,
    attributionControl: false,
    scrollWheelZoom: false,
    doubleClickZoom: false,
  });

  // Carto Positron — clean, minimal, light tiles (no API key required)
  L.tileLayer(
    'https://{s}.basemaps.cartocdn.com/rastertiles/voyager_nolabels/{z}/{x}/{y}{r}.png',
    { subdomains: 'abcd', maxZoom: 19 },
  ).addTo(map);

  // Attribution in a tiny corner
  L.control.attribution({ position: 'bottomright', prefix: false })
    .addAttribution('© <a href="https://carto.com">CARTO</a>')
    .addTo(map);

  addMarkers();
});

onUnmounted(() => {
  map?.remove();
  map = null;
});

watch(() => props.regions, addMarkers, { deep: true });
</script>

<style>
/* Pulse animation — must be global so the injected HTML can reach it */
@keyframes geo-pulse {
  0%, 100% { transform: scale(1);   opacity: 0.6; }
  50%       { transform: scale(1.5); opacity: 0;   }
}

/* Popup chrome */
.geo-popup-wrapper .leaflet-popup-content-wrapper {
  background: rgba(245, 240, 255, 0.88) !important;
  backdrop-filter: blur(12px) saturate(1.6);
  -webkit-backdrop-filter: blur(12px) saturate(1.6);
  border: 1px solid rgba(255, 255, 255, 0.55) !important;
  border-radius: 14px !important;
  box-shadow: 0 8px 32px rgba(80, 40, 160, 0.18) !important;
  padding: 0 !important;
}
.geo-popup-wrapper .leaflet-popup-tip-container { display: none; }
.geo-popup-wrapper .leaflet-popup-close-button {
  color: rgba(80, 40, 160, 0.45) !important;
  font-size: 16px !important;
  top: 6px !important;
  right: 8px !important;
}
.geo-popup-wrapper .leaflet-popup-close-button:hover {
  color: rgba(80, 40, 160, 0.85) !important;
}
.geo-popup-wrapper .leaflet-popup-content { margin: 0 !important; }

/* Popup inner layout */
.geo-popup {
  padding: 14px 16px 12px;
  min-width: 160px;
}
.geo-popup-title {
  font-size: 13px;
  font-weight: 700;
  color: #3b1d8a;
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px solid rgba(124, 58, 237, 0.12);
}
.geo-popup-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 3px 0;
  font-size: 12px;
}
.geo-popup-row span   { color: rgba(60, 30, 100, 0.55); }
.geo-popup-row strong { color: #3b1d8a; font-weight: 600; }

/* Dark-mode overrides */
.dark .geo-popup-wrapper .leaflet-popup-content-wrapper {
  background: rgba(30, 15, 60, 0.88) !important;
}
.dark .geo-popup-title,
.dark .geo-popup-row strong { color: #e0d0ff; }
.dark .geo-popup-row span   { color: rgba(200, 180, 240, 0.55); }
.dark .geo-popup-wrapper .leaflet-popup-close-button { color: rgba(180, 150, 255, 0.5) !important; }
</style>

<style scoped>
.geo-map-container {
  width: 100%;
  height: 320px;
  border-radius: 0;       /* card already clips */
  overflow: hidden;
}

/* Soften the default Leaflet tile borders */
:deep(.leaflet-tile) {
  filter: saturate(0.75) brightness(1.02);
}
</style>
