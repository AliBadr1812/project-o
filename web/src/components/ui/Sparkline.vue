<template>
  <svg
    :width="width"
    :height="height"
    :viewBox="`0 0 ${width} ${height}`"
    preserveAspectRatio="none"
    class="sparkline"
    :aria-label="label"
    role="img"
  >
    <!-- Gradient fill under the line -->
    <defs>
      <linearGradient :id="gradId" x1="0" y1="0" x2="0" y2="1">
        <stop offset="0%" :stop-color="color" stop-opacity="0.35" />
        <stop offset="100%" :stop-color="color" stop-opacity="0.02" />
      </linearGradient>
    </defs>

    <!-- Fill area -->
    <path v-if="areaPath" :d="areaPath" :fill="`url(#${gradId})`" />

    <!-- The line itself -->
    <path v-if="linePath" :d="linePath"
      :stroke="color"
      stroke-width="1.8"
      stroke-linecap="round"
      stroke-linejoin="round"
      fill="none"
    />

    <!-- End-point dot -->
    <circle v-if="lastPoint" :cx="lastPoint.x" :cy="lastPoint.y" r="2.5"
      :fill="color" />
  </svg>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  data:   { type: Array as () => number[], required: true },
  width:  { type: Number, default: 100 },
  height: { type: Number, default: 36 },
  color:  { type: String, default: 'var(--accent)' },
  label:  { type: String, default: 'Sparkline' },
});

// Unique gradient id so multiple sparklines on the same page don't collide
const gradId = `sg-${Math.random().toString(36).slice(2, 7)}`;

const padding = 3;

const points = computed(() => {
  const d = props.data;
  if (!d || d.length < 2) return [];
  const min  = Math.min(...d);
  const max  = Math.max(...d);
  const range = max - min || 1;
  const w = props.width  - padding * 2;
  const h = props.height - padding * 2;
  return d.map((v, i) => ({
    x: padding + (i / (d.length - 1)) * w,
    y: padding + (1 - (v - min) / range) * h,
  }));
});

const lastPoint = computed(() => points.value[points.value.length - 1] ?? null);

function toSmoothPath(pts: { x: number; y: number }[]): string {
  if (pts.length < 2) return '';
  let d = `M ${pts[0].x} ${pts[0].y}`;
  for (let i = 1; i < pts.length; i++) {
    const prev = pts[i - 1];
    const curr = pts[i];
    const cpx  = (prev.x + curr.x) / 2;
    d += ` C ${cpx} ${prev.y}, ${cpx} ${curr.y}, ${curr.x} ${curr.y}`;
  }
  return d;
}

const linePath = computed(() => toSmoothPath(points.value));

const areaPath = computed(() => {
  const pts = points.value;
  if (pts.length < 2) return '';
  const base = props.height - padding + 2;
  const first = pts[0];
  const last  = pts[pts.length - 1];
  return `${toSmoothPath(pts)} L ${last.x} ${base} L ${first.x} ${base} Z`;
});
</script>

<style scoped>
.sparkline { display: block; overflow: visible; }
</style>
