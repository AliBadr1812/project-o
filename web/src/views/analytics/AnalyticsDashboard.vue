<template>
  <div class="flex flex-col gap-6">

    <!-- ── Page header ─────────────────────────────────────────────────── -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Analytics</h1>
        <p class="page-subtitle">Track performance across your entire store</p>
      </div>
      <div class="flex items-center gap-2">
        <!-- Time range pill tabs -->
        <div
          class="flex gap-1 p-1 rounded-2xl"
          style="background: rgba(255,255,255,0.30); border: 1px solid var(--glass-border);"
        >
          <button
            v-for="range in timeRanges"
            :key="range.value"
            @click="setTimeRange(range.value)"
            class="px-3 py-1.5 rounded-xl text-[13px] font-medium transition-all duration-200"
            :style="timeRange === range.value
              ? 'background: rgba(255,255,255,0.75); color: var(--accent); box-shadow: 0 1px 6px rgba(100,80,160,0.15);'
              : 'color: var(--text-secondary);'"
          >{{ range.label }}</button>
        </div>

        <!-- Custom date range -->
        <div class="flex items-center gap-1.5 px-3 py-1.5 rounded-2xl" style="background: rgba(255,255,255,0.30); border: 1px solid var(--glass-border);">
          <input type="date" v-model="customDateRange.start" :max="customDateRange.end" class="glass-input text-[12px] py-1 px-2" style="width:130px;">
          <span class="text-[12px]" style="color:var(--text-muted);">—</span>
          <input type="date" v-model="customDateRange.end" :min="customDateRange.start" :max="today" class="glass-input text-[12px] py-1 px-2" style="width:130px;">
          <button @click="applyCustomDate" :disabled="!isCustomDateValid" class="btn-accent text-[12px] px-3 py-1.5">Apply</button>
        </div>

        <button @click="handleRefresh" class="btn-glass">
          <i class="fas fa-rotate-right text-xs"></i>
        </button>
        <button @click="handleExport" class="btn-accent">
          <i class="fas fa-arrow-up-from-bracket text-xs mr-1.5"></i>Export
        </button>
      </div>
    </div>

    <!-- ── KPI cards ───────────────────────────────────────────────────── -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <Card v-for="metric in metrics" :key="metric.key">
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon" :class="metric.iconClass">
              <i :class="metric.icon"></i>
            </div>
            <span class="text-xs px-2 py-0.5 rounded-full font-semibold" :style="metric.trendStyle">
              <i v-if="metric.trend > 0" class="fas fa-arrow-up text-[9px] mr-0.5"></i>
              <i v-else class="fas fa-arrow-down text-[9px] mr-0.5"></i>
              {{ Math.abs(metric.trend) }}%
            </span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">{{ metric.label }}</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ formatMetricValue(metric) }}</p>
          <p class="text-xs mt-2 pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">{{ metric.subtext }}</p>
        </div>
      </Card>
    </div>

    <!-- ── Sales chart + Category breakdown ───────────────────────────── -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

      <!-- Sales chart (wide) -->
      <Card class="lg:col-span-2">
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Revenue Trend</h2>
              <p class="text-xs mt-0.5" style="color: var(--text-muted);">{{ timeRanges.find(r => r.value === timeRange)?.label ?? 'Custom range' }}</p>
            </div>
            <div
              class="flex gap-1 p-1 rounded-xl"
              style="background: rgba(255,255,255,0.30); border: 1px solid var(--glass-border);"
            >
              <button
                @click="salesChartType = 'line'"
                class="px-3 py-1 rounded-lg text-[12px] font-medium transition-all"
                :style="salesChartType === 'line'
                  ? 'background: rgba(255,255,255,0.75); color: var(--accent);'
                  : 'color: var(--text-secondary);'"
              ><i class="fas fa-chart-line text-[11px] mr-1"></i>Line</button>
              <button
                @click="salesChartType = 'bar'"
                class="px-3 py-1 rounded-lg text-[12px] font-medium transition-all"
                :style="salesChartType === 'bar'
                  ? 'background: rgba(255,255,255,0.75); color: var(--accent);'
                  : 'color: var(--text-secondary);'"
              ><i class="fas fa-chart-bar text-[11px] mr-1"></i>Bar</button>
            </div>
          </div>
        </div>
        <div class="p-6">
          <div style="height: 280px; position: relative;">
            <canvas id="salesChart"></canvas>
          </div>
        </div>
      </Card>

      <!-- Revenue by Category -->
      <Card>
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">By Category</h2>
              <p class="text-xs mt-0.5" style="color: var(--text-muted);">Revenue distribution</p>
            </div>
            <button @click="exportCategories" class="btn-glass-icon w-7 h-7 rounded-lg text-xs" title="Export">
              <i class="fas fa-download"></i>
            </button>
          </div>
        </div>
        <div class="p-5 flex flex-col gap-4">
          <div v-for="(item, i) in categoryRevenue" :key="item.category">
            <div class="flex items-center justify-between mb-1.5">
              <div class="flex items-center gap-2">
                <span class="w-2.5 h-2.5 rounded-full flex-shrink-0" :style="`background: ${categoryColors[i % categoryColors.length]};`"></span>
                <span class="text-[13px]" style="color: var(--text-secondary);">{{ item.category }}</span>
              </div>
              <div class="flex items-center gap-2">
                <span class="text-[12px]" style="color: var(--text-muted);">{{ item.percentage }}%</span>
                <span class="text-[13px] font-semibold" style="color: var(--text-primary);">{{ formatCurrency(item.revenue) }}</span>
              </div>
            </div>
            <div class="progress-track">
              <div class="progress-fill" :style="{ width: item.percentage + '%', background: categoryColors[i % categoryColors.length] }"></div>
            </div>
          </div>
        </div>
      </Card>
    </div>

    <!-- ── Top products + Recent orders ───────────────────────────────── -->
    <div class="grid grid-cols-1 lg:grid-cols-5 gap-6">

      <!-- Top Products (wider) -->
      <Card class="lg:col-span-3 overflow-hidden">
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Top Products</h2>
              <p class="text-xs mt-0.5" style="color: var(--text-muted);">Best performing this period</p>
            </div>
            <router-link to="/products" class="text-sm font-medium hover:opacity-75 transition-opacity" style="color: var(--text-accent);">
              View all →
            </router-link>
          </div>
        </div>
        <div class="overflow-x-auto">
          <table class="glass-table w-full">
            <thead>
              <tr>
                <th>#</th>
                <th>Product</th>
                <th>Sales</th>
                <th>Revenue</th>
                <th>Stock</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(product, i) in topProducts" :key="product.id">
                <td>
                  <span
                    class="w-6 h-6 rounded-lg flex items-center justify-center text-[11px] font-bold"
                    :style="i === 0
                      ? 'background: rgba(255,184,0,0.15); color: #b8860b;'
                      : i === 1
                        ? 'background: rgba(156,163,175,0.15); color: #6b7280;'
                        : i === 2
                          ? 'background: rgba(180,130,90,0.15); color: #92400e;'
                          : 'background: var(--glass-bg); color: var(--text-muted);'"
                  >{{ i + 1 }}</span>
                </td>
                <td>
                  <div class="flex items-center gap-3">
                    <div class="w-9 h-9 rounded-xl flex items-center justify-center flex-shrink-0" :class="`ni-${['p','g','b','o','t'][i % 5]}`" style="border: 1px solid rgba(255,255,255,0.4);">
                      <i class="fas fa-box text-xs"></i>
                    </div>
                    <div>
                      <p class="td-primary">{{ product.name }}</p>
                      <p class="text-xs" style="color: var(--text-muted);">{{ product.category }}</p>
                    </div>
                  </div>
                </td>
                <td><span class="text-sm font-medium" style="color: var(--text-primary);">{{ product.sales.toLocaleString() }}</span></td>
                <td class="td-accent">{{ formatCurrency(product.revenue) }}</td>
                <td>
                  <Badge :variant="product.stock > 20 ? 'success' : product.stock > 5 ? 'warning' : 'danger'">
                    {{ product.stock }} left
                  </Badge>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </Card>

      <!-- Recent Orders (narrower) -->
      <Card class="lg:col-span-2">
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Recent Orders</h2>
              <p class="text-xs mt-0.5" style="color: var(--text-muted);">Latest activity</p>
            </div>
            <router-link to="/orders" class="text-sm font-medium hover:opacity-75 transition-opacity" style="color: var(--text-accent);">
              View all →
            </router-link>
          </div>
        </div>
        <div class="flex flex-col">
          <div
            v-for="order in recentOrders"
            :key="order.id"
            class="flex items-center gap-3 px-5 py-3.5 cursor-pointer transition-all duration-150"
            style="border-bottom: 1px solid var(--glass-border);"
            @mouseenter="(e: MouseEvent) => (e.currentTarget as HTMLElement).style.background = 'rgba(255,255,255,0.3)'"
            @mouseleave="(e: MouseEvent) => (e.currentTarget as HTMLElement).style.background = 'transparent'"
            @click="goToOrder(order.id)"
          >
            <div
              class="w-9 h-9 rounded-xl flex items-center justify-center text-xs font-bold flex-shrink-0"
              :style="`background: linear-gradient(135deg, ${stringToColor(order.customerName)}, ${stringToColor(order.customerName + '2')}); color:#fff;`"
            >{{ getInitials(order.customerName) }}</div>
            <div class="flex-1 min-w-0">
              <p class="text-[13px] font-medium truncate" style="color: var(--text-primary);">{{ order.customerName }}</p>
              <p class="text-[11px]" style="color: var(--text-muted);">#{{ order.orderNumber }} · {{ formatDateShort(order.createdAt ?? '') }}</p>
            </div>
            <div class="text-right flex-shrink-0">
              <p class="text-[13px] font-semibold" style="color: var(--text-accent);">{{ formatCurrency(order.total) }}</p>
              <Badge :variant="getOrderStatusVariant(order.status)" class="text-[10px] mt-0.5">{{ order.status }}</Badge>
            </div>
          </div>
        </div>
      </Card>
    </div>

    <!-- ── Geographic Distribution ─────────────────────────────────────── -->
    <Card class="overflow-hidden">
      <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
        <div class="flex items-center justify-between">
          <div>
            <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Geographic Distribution</h2>
            <p class="text-xs mt-0.5" style="color: var(--text-muted);">Revenue and orders by region — click a bubble for details</p>
          </div>
          <span class="badge badge-info text-[11px]">{{ topRegions.length }} regions</span>
        </div>
      </div>

      <!-- Map -->
      <GeoMap :regions="mapRegions" />

      <!-- Region breakdown below the map -->
      <div class="px-6 pb-5 pt-4 flex flex-col gap-3">
        <div v-for="(region, i) in topRegions" :key="region.name">
          <div class="flex items-center justify-between mb-1.5">
            <div class="flex items-center gap-2.5">
              <span class="w-2.5 h-2.5 rounded-full flex-shrink-0" :style="`background: ${regionColors[i % regionColors.length]};`"></span>
              <span class="text-[13px] font-medium" style="color: var(--text-primary);">{{ region.name }}</span>
            </div>
            <div class="flex items-center gap-3">
              <span class="text-[12px]" style="color: var(--text-muted);">{{ mapRegions[i]?.orders ?? 0 }} orders</span>
              <span class="text-[12px]" style="color: var(--text-muted);">{{ formatCurrency(region.revenue) }}</span>
              <span class="text-[13px] font-bold w-9 text-right" style="color: var(--text-primary);">{{ region.percentage }}%</span>
            </div>
          </div>
          <div class="progress-track">
            <div class="progress-fill" :style="{ width: region.percentage + '%', background: regionColors[i % regionColors.length] }"></div>
          </div>
        </div>
      </div>
    </Card>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, onUnmounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatCurrency, getInitials } from '@/utils/formatters';
import { exportToCsv, datestampedFilename } from '@/utils/csvExport';
import Chart from 'chart.js/auto';
import GeoMap from './GeoMap.vue';
import { useOrderStore }    from '@/stores/orderStore';
import { useProductStore }  from '@/stores/productStore';
import { useCustomerStore } from '@/stores/customerStore';

const router = useRouter();

// ── Stores ────────────────────────────────────────────────────────────────
const orderStore    = useOrderStore();
const productStore  = useProductStore();
const customerStore = useCustomerStore();

const { items: storeOrders }    = storeToRefs(orderStore);
const { items: storeProducts }  = storeToRefs(productStore);
const { items: storeCustomers } = storeToRefs(customerStore);

// ── Design constants ──────────────────────────────────────────────────────
const categoryColors = [
  'linear-gradient(135deg,#b97fff,#7c5ef0)',
  'linear-gradient(135deg,#5ac8fa,#007aff)',
  'linear-gradient(135deg,#34c759,#30a84b)',
  'linear-gradient(135deg,#ff9f0a,#e08800)',
  'linear-gradient(135deg,#ff6b6b,#ee5a24)',
];

const regionColors = [
  'linear-gradient(135deg,#7c5ef0,#5e5ce6)',
  'linear-gradient(135deg,#007aff,#0055ff)',
  'linear-gradient(135deg,#34c759,#30a84b)',
  'linear-gradient(135deg,#ff9f0a,#e08800)',
  'linear-gradient(135deg,#ff6b6b,#ee5a24)',
];

const regionFlags = ['🌎', '🌍', '🌏', '🌎', '🕌'];

// Types
interface Metric {
  key: string;
  label: string;
  value: number;
  trend: number;
  subtext: string;
  icon: string;
  iconClass: string;
  trendStyle: string;
  format?: 'currency' | 'number' | 'percent';
}

interface CategoryRevenue {
  category: string;
  revenue: number;
  percentage: number;
}

interface Product {
  id: number;
  name: string;
  category: string;
  revenue: number;
  sales: number;
  stock: number;
}

interface Order {
  id: string;
  customerName: string;
  date: string;
  amount: number;
  status: 'completed' | 'processing' | 'pending' | 'shipped' | 'cancelled';
}

interface Region {
  name: string;
  percentage: number;
  revenue: number;
}

// Helper functions for dates (replacing date-fns)
function formatDateForInput(date: Date): string {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

function getTodayFormatted(): string {
  return formatDateForInput(new Date());
}

function getDaysAgoFormatted(days: number): string {
  const date = new Date();
  date.setDate(date.getDate() - days);
  return formatDateForInput(date);
}

function parseDate(dateString: string): Date {
  return new Date(dateString + 'T00:00:00');
}

function isDateAfter(date1: string, date2: string): boolean {
  return parseDate(date1) > parseDate(date2);
}

// Time range options
const timeRanges = [
  { label: 'Today', value: 'today' },
  { label: 'Week', value: 'week' },
  { label: 'Month', value: 'month' },
  { label: 'Year', value: 'year' }
] as const;

type TimeRange = 'today' | 'week' | 'month' | 'year' | 'custom';

// State
const timeRange = ref<TimeRange>('month');
const salesChartType = ref<'line' | 'bar'>('line');
const loading = ref(false);
const customDateRange = reactive({
  start: getDaysAgoFormatted(30),
  end: getTodayFormatted()
});

// Computed
const today = computed(() => getTodayFormatted());
const isCustomDateValid = computed(() => {
  return customDateRange.start && customDateRange.end && customDateRange.start <= customDateRange.end;
});

// ── Real store metrics ────────────────────────────────────────────────────
const storeMetrics = computed(() => {
  const activeOrders = storeOrders.value.filter(
    o => o.status !== 'cancelled' && o.status !== 'refunded',
  );
  const totalRevenue  = activeOrders.reduce((s, o) => s + o.total, 0);
  const totalOrders   = storeOrders.value.length;
  const avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0;
  const activeCustomers = storeCustomers.value.filter(c => c.status === 'active').length;
  const msPerMonth = 30 * 24 * 60 * 60 * 1000;
  const now = Date.now();
  const newCustomers = storeCustomers.value.filter(c =>
    c.lastOrderDate && (now - new Date(c.lastOrderDate).getTime()) <= msPerMonth,
  ).length;
  return { totalRevenue, totalOrders, avgOrderValue, activeCustomers, newCustomers };
});

// Metrics data
const metrics = computed<Metric[]>(() => {
  const m = storeMetrics.value;
  return [
    {
      key: 'totalSales',
      label: 'Total Sales',
      value: m.totalRevenue,
      trend: 12.5,
      subtext: 'all-time revenue',
      icon: 'fas fa-shopping-cart',
      iconClass: 'ni-b',
      trendStyle: `color: var(--ni-green); background: rgba(var(--ni-green-rgb, 34,197,94), 0.1);`,
      format: 'currency'
    },
    {
      key: 'totalOrders',
      label: 'Total Orders',
      value: m.totalOrders,
      trend: 8.2,
      subtext: `Avg order: ${formatCurrency(m.avgOrderValue)}`,
      icon: 'fas fa-box',
      iconClass: 'ni-g',
      trendStyle: `color: var(--ni-green); background: rgba(var(--ni-green-rgb, 34,197,94), 0.1);`,
      format: 'number'
    },
    {
      key: 'totalCustomers',
      label: 'Active Customers',
      value: m.activeCustomers,
      trend: 15.3,
      subtext: `+${m.newCustomers} new this month`,
      icon: 'fas fa-users',
      iconClass: 'ni-p',
      trendStyle: `color: var(--ni-green); background: rgba(var(--ni-green-rgb, 34,197,94), 0.1);`,
      format: 'number'
    },
    {
      key: 'conversionRate',
      label: 'Conversion Rate',
      value: 3.2,
      trend: -2.1,
      subtext: `From 76,025 visitors`,
      icon: 'fas fa-eye',
      iconClass: 'ni-o',
      trendStyle: `color: var(--ni-red); background: rgba(var(--ni-red-rgb, 239,68,68), 0.1);`,
      format: 'percent'
    }
  ];
});

// Add to script setup
const mapRegions = computed(() => [
  {
    name: 'North America',
    coordinates: [40, -100] as [number, number],
    revenue: 45230 * getTimeMultiplier(),
    orders: 342,
    percentage: 45
  },
  {
    name: 'Europe',
    coordinates: [50, 10] as [number, number],
    revenue: 28900 * getTimeMultiplier(),
    orders: 218,
    percentage: 28
  },
  {
    name: 'Asia Pacific',
    coordinates: [20, 100] as [number, number],
    revenue: 18500 * getTimeMultiplier(),
    orders: 156,
    percentage: 18
  },
  {
    name: 'Latin America',
    coordinates: [-15, -60] as [number, number],
    revenue: 6500 * getTimeMultiplier(),
    orders: 52,
    percentage: 6
  },
  {
    name: 'Middle East',
    coordinates: [25, 45] as [number, number],
    revenue: 3200 * getTimeMultiplier(),
    orders: 28,
    percentage: 3
  }
]);

// Category revenue data — group products by category and compute price*stock
const categoryRevenue = computed<CategoryRevenue[]>(() => {
  const map = new Map<string, number>();
  for (const p of storeProducts.value) {
    const cat = p.categories || 'Uncategorized';
    map.set(cat, (map.get(cat) ?? 0) + p.price * Math.max(p.stock, 1));
  }
  if (map.size === 0) {
    // fallback while products are loading
    const fallback = [
      { category: 'Electronics', revenue: 45230 },
      { category: 'Furniture',   revenue: 32180 },
      { category: 'Clothing',    revenue: 28450 },
    ];
    const total = fallback.reduce((s, x) => s + x.revenue, 0);
    return fallback.map(x => ({ ...x, percentage: Math.round((x.revenue / total) * 100) }));
  }
  const entries = [...map.entries()]
    .map(([category, revenue]) => ({ category, revenue }))
    .sort((a, b) => b.revenue - a.revenue);
  const total = entries.reduce((s, x) => s + x.revenue, 0);
  return entries.map(x => ({ ...x, percentage: total > 0 ? Math.round((x.revenue / total) * 100) : 0 }));
});

// Top products — real store, sorted by price desc
const topProducts = computed<Product[]>(() =>
  [...storeProducts.value]
    .sort((a, b) => b.price - a.price)
    .slice(0, 5)
    .map(p => ({
      id:       p.id,
      name:     p.name,
      category: p.categories || '—',
      revenue:  p.price * Math.max(p.stock, 1),
      sales:    p.stock,
      stock:    p.stock,
    })),
);

// Recent orders — real store, last 6 by createdAt
const recentOrders = computed(() =>
  [...storeOrders.value]
    .sort((a, b) => {
      const ta = a.createdAt ? new Date(a.createdAt).getTime() : 0;
      const tb = b.createdAt ? new Date(b.createdAt).getTime() : 0;
      return tb - ta;
    })
    .slice(0, 6),
);

// Top regions
const topRegions = computed<Region[]>(() => {
  const multiplier = getTimeMultiplier();
  return [
    { name: 'North America', revenue: 45230 * multiplier, percentage: 45 },
    { name: 'Europe', revenue: 28900 * multiplier, percentage: 28 },
    { name: 'Asia Pacific', revenue: 18500 * multiplier, percentage: 18 },
    { name: 'Latin America', revenue: 6500 * multiplier, percentage: 6 },
    { name: 'Middle East', revenue: 3200 * multiplier, percentage: 3 }
  ];
});

// Chart data
const salesChartData = computed(() => {
  const multiplier = getTimeMultiplier();

  const ranges = {
    today: {
      labels: Array.from({ length: 24 }, (_, i) => `${i}:00`),
      data: Array.from({ length: 24 }, () => Math.floor(Math.random() * 1000) + 500)
    },
    week: {
      labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      data: [3200, 4200, 3800, 4500, 5200, 6100, 5800].map(v => v * multiplier)
    },
    month: {
      labels: Array.from({ length: 30 }, (_, i) => `Day ${i + 1}`),
      data: Array.from({ length: 30 }, () => Math.floor(Math.random() * 2000) + 1000)
    },
    year: {
      labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
      data: [12500, 14200, 13800, 15400, 16800, 17200, 18900, 19500, 18200, 17600, 19200, 20800].map(v => v * multiplier)
    }
  };

  if (timeRange.value === 'custom') {
    return buildDataset(ranges.month.labels, ranges.month.data);
  }

  const range = ranges[timeRange.value];
  return buildDataset(range.labels, range.data);
});

function buildDataset(labels: string[], data: number[]) {
  return {
    labels,
    datasets: [{
      label: 'Revenue',
      data,
      borderColor: '#7c3aed',
      backgroundColor: salesChartType.value === 'line'
        ? 'rgba(124,58,237,0.08)'
        : 'rgba(124,58,237,0.65)',
      fill: salesChartType.value === 'line',
      tension: 0.4,
      borderWidth: 2,
      pointBackgroundColor: '#7c3aed',
      pointBorderColor: '#ffffff',
      pointBorderWidth: 2,
      pointRadius: 3,
      pointHoverRadius: 5,
      borderRadius: salesChartType.value === 'bar' ? 6 : 0,
    }]
  };
}

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: 'rgba(30,10,60,0.92)',
      titleColor: '#f0e8ff',
      bodyColor: 'rgba(200,180,240,0.85)',
      borderColor: 'rgba(255,255,255,0.15)',
      borderWidth: 1,
      cornerRadius: 10,
      displayColors: false,
      callbacks: {
        label: (context: any) => `$${context.parsed.y.toLocaleString()}`
      }
    }
  },
  scales: {
    y: {
      beginAtZero: true,
      grid: { color: 'rgba(255,255,255,0.06)' },
      ticks: { color: 'rgba(60,30,100,0.5)', callback: (value: any) => '$' + value.toLocaleString() }
    },
    x: {
      grid: { color: 'rgba(255,255,255,0.06)' },
      ticks: { color: 'rgba(60,30,100,0.5)', maxTicksLimit: 10 }
    }
  }
};

let chart: Chart | null = null;

// Helper functions
function getTimeMultiplier(): number {
  const multipliers = { today: 0.1, week: 0.5, month: 1, year: 12, custom: 1 };
  return multipliers[timeRange.value] || 1;
}

function formatMetricValue(metric: Metric): string {
  const value = metric.value;
  switch (metric.format) {
    case 'currency': return formatCurrency(value);
    case 'percent':  return value.toFixed(1) + '%';
    default:         return Math.round(value).toLocaleString();
  }
}

function formatDateShort(dateString: string): string {
  if (!dateString) return '—';
  return new Date(dateString).toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
}

function stringToColor(str: string): string {
  let hash = 0;
  for (let i = 0; i < str.length; i++) hash = str.charCodeAt(i) + ((hash << 5) - hash);
  return `hsl(${hash % 360}, 70%, 50%)`;
}

function getOrderStatusVariant(status: string): string {
  const map: Record<string, string> = {
    completed: 'success', processing: 'info', shipped: 'primary', pending: 'warning', cancelled: 'danger'
  };
  return map[status] || 'neutral';
}

// Methods
function setTimeRange(range: TimeRange) {
  timeRange.value = range;
  loading.value = true;
  setTimeout(() => { loading.value = false; }, 500);
}

function applyCustomDate() {
  if (isCustomDateValid.value) {
    timeRange.value = 'custom' as TimeRange;
    setTimeRange('custom' as TimeRange);
  }
}

function handleExport() {
  exportToCsv(
    datestampedFilename('analytics_orders'),
    storeOrders.value,
    ['id', 'orderNumber', 'customerName', 'status', 'total', 'createdAt'],
  );
}

function handleRefresh() {
  orderStore.fetchAll(true);
  productStore.fetchAll(true);
  customerStore.fetchAll(true);
}

function exportCategories() {
  exportToCsv(datestampedFilename('category_revenue'), categoryRevenue.value);
}

function viewAllProducts() { router.push('/products'); }
function goToOrder(orderId: number | undefined) { if (orderId) router.push(`/orders/${orderId}`); }

function updateChart() {
  if (chart) {
    chart.destroy();
    const ctx = document.getElementById('salesChart') as HTMLCanvasElement;
    if (ctx) {
      chart = new Chart(ctx, { type: salesChartType.value, data: salesChartData.value, options: chartOptions });
    }
  }
}

onMounted(() => {
  orderStore.fetchAll();
  productStore.fetchAll();
  customerStore.fetchAll();
  const ctx = document.getElementById('salesChart') as HTMLCanvasElement;
  if (ctx) chart = new Chart(ctx, { type: salesChartType.value, data: salesChartData.value, options: chartOptions });
});

onUnmounted(() => { if (chart) chart.destroy(); });

watch([timeRange, salesChartType], () => { updateChart(); });
</script>
