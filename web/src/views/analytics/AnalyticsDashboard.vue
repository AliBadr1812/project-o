<template>
  <div class="flex flex-col gap-6">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Analytics Dashboard</h1>
        <p class="page-subtitle">Track your business performance and metrics</p>
      </div>
      <div class="flex items-center gap-3">
        <button @click="handleRefresh" class="btn-glass text-sm">
          <i class="fas fa-sync-alt text-xs mr-1"></i>Refresh
        </button>
        <button @click="handleExport" class="btn-accent text-sm">
          <i class="fas fa-arrow-up-from-bracket text-xs mr-1"></i>Export
        </button>
      </div>
    </div>

    <!-- Time Range Selector -->
    <Card>
      <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4 p-5">
        <div class="flex items-center gap-2 flex-wrap">
          <button
            v-for="range in timeRanges"
            :key="range.value"
            @click="setTimeRange(range.value)"
            class="text-sm px-3 py-1.5 rounded-lg transition-colors"
            :class="timeRange === range.value ? 'btn-accent' : 'btn-glass'"
          >
            {{ range.label }}
          </button>
        </div>
        <div class="flex items-center gap-2 flex-wrap">
          <input type="date" v-model="customDateRange.start" :max="customDateRange.end"
            class="glass-input text-sm" />
          <span class="text-sm" style="color: var(--text-secondary);">to</span>
          <input type="date" v-model="customDateRange.end" :min="customDateRange.start" :max="today"
            class="glass-input text-sm" />
          <button @click="applyCustomDate" :disabled="!isCustomDateValid" class="btn-accent text-sm">Apply</button>
        </div>
      </div>
    </Card>

    <!-- Stats Bar -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <Card v-for="metric in metrics" :key="metric.key">
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon" :class="metric.iconClass">
              <i :class="metric.icon"></i>
            </div>
            <span class="text-xs px-2 py-0.5 rounded-full font-medium" :style="metric.trendStyle">
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

    <!-- Charts Section -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Sales Chart -->
      <Card class="lg:col-span-2">
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Sales Trend</h2>
              <p class="text-xs mt-0.5" style="color: var(--text-muted);">Revenue over time</p>
            </div>
            <select v-model="salesChartType" class="glass-select text-sm">
              <option value="line">Line Chart</option>
              <option value="bar">Bar Chart</option>
            </select>
          </div>
        </div>
        <div class="p-5">
          <div class="h-72 flex items-center justify-center">
            <canvas id="salesChart"></canvas>
          </div>
        </div>
      </Card>

      <!-- Revenue by Category -->
      <Card>
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Revenue by Category</h2>
              <p class="text-xs mt-0.5" style="color: var(--text-muted);">Distribution across categories</p>
            </div>
            <button @click="exportCategories" class="btn-glass-icon w-7 h-7 rounded-lg text-xs">
              <i class="fas fa-download"></i>
            </button>
          </div>
        </div>
        <div class="p-5">
          <div v-if="categoryRevenue.length === 0" class="h-48 flex items-center justify-center">
            <p class="text-sm" style="color: var(--text-muted);">No data available</p>
          </div>
          <div v-else class="flex flex-col gap-4">
            <div v-for="item in categoryRevenue" :key="item.category">
              <div class="flex justify-between text-sm mb-1">
                <span style="color: var(--text-secondary);">{{ item.category }}</span>
                <span class="font-medium" style="color: var(--text-primary);">{{ formatCurrency(item.revenue) }}</span>
              </div>
              <div class="progress-track">
                <div class="progress-fill" :style="{ width: item.percentage + '%', background: 'var(--progress-primary)' }"></div>
              </div>
            </div>
          </div>
        </div>
      </Card>
    </div>

    <!-- Top Products + Recent Orders -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Top Products -->
      <Card>
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Top Products</h2>
              <p class="text-xs mt-0.5" style="color: var(--text-muted);">Best selling products</p>
            </div>
            <router-link to="/products" class="text-sm font-medium" style="color: var(--text-accent);">View all →</router-link>
          </div>
        </div>
        <div class="overflow-x-auto">
          <table class="glass-table w-full">
            <thead>
              <tr>
                <th>Product</th>
                <th>Sales</th>
                <th>Revenue</th>
                <th>Stock</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in topProducts" :key="product.id">
                <td>
                  <div class="flex items-center gap-3">
                    <div class="w-9 h-9 rounded-lg flex items-center justify-center" style="background: var(--glass-bg); border: 1px solid var(--glass-border);">
                      <i class="fas fa-box text-xs" style="color: var(--text-muted);"></i>
                    </div>
                    <div>
                      <p class="td-primary">{{ product.name }}</p>
                      <p class="text-xs" style="color: var(--text-muted);">{{ product.category }}</p>
                    </div>
                  </div>
                </td>
                <td><span class="text-sm font-medium" style="color: var(--text-primary);">{{ product.sales }}</span></td>
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
        <div class="px-6 py-3 text-right" style="border-top: 1px solid var(--glass-border);">
          <button @click="viewAllProducts" class="btn-glass text-sm">View All Products</button>
        </div>
      </Card>

      <!-- Recent Orders -->
      <Card>
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Recent Orders</h2>
              <p class="text-xs mt-0.5" style="color: var(--text-muted);">Latest customer orders</p>
            </div>
            <router-link to="/orders" class="text-sm font-medium" style="color: var(--text-accent);">View all →</router-link>
          </div>
        </div>
        <div class="flex flex-col" style="divide-y: 1px solid var(--glass-border);">
          <div v-for="order in recentOrders" :key="order.id"
            class="flex items-center justify-between px-5 py-3 cursor-pointer transition-colors"
            style="border-bottom: 1px solid var(--glass-border);"
            @click="goToOrder(order.id)">
            <div class="flex items-center gap-3">
              <div class="w-9 h-9 rounded-lg flex items-center justify-center text-xs font-semibold"
                style="color: var(--text-primary);"
                :style="{ background: `linear-gradient(135deg, ${stringToColor(order.customerName)}, ${stringToColor(order.customerName + '2')})` }">
                {{ getInitials(order.customerName) }}
              </div>
              <div>
                <p class="td-primary">Order #{{ order.id }}</p>
                <p class="text-xs" style="color: var(--text-secondary);">{{ order.customerName }}</p>
              </div>
            </div>
            <div class="text-right">
              <p class="font-medium text-sm td-accent">{{ formatCurrency(order.amount) }}</p>
              <Badge :variant="getOrderStatusVariant(order.status)" class="mt-1">{{ order.status }}</Badge>
            </div>
          </div>
        </div>
      </Card>
    </div>

    <!-- Geographic Distribution -->
    <Card>
      <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
        <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Geographic Distribution</h2>
        <p class="text-xs mt-0.5" style="color: var(--text-muted);">Customer locations by region</p>
      </div>
      <div class="p-5">
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div>
            <GeoMap :regions="mapRegions" />
          </div>
          <div class="flex flex-col gap-3">
            <p class="text-xs font-medium mb-1" style="color: var(--text-secondary);">Top Regions</p>
            <div v-for="region in topRegions" :key="region.name">
              <div class="flex justify-between text-sm mb-1">
                <span style="color: var(--text-secondary);">{{ region.name }}</span>
                <span class="font-medium" style="color: var(--text-primary);">{{ region.percentage }}%</span>
              </div>
              <div class="progress-track">
                <div class="progress-fill" :style="{ width: region.percentage + '%', background: 'var(--progress-primary)' }"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import Card from '@/components/ui/Card.vue';
import Badge from '@/components/ui/Badge.vue';
import { formatCurrency, getInitials } from '@/utils/formatters';
import Chart from 'chart.js/auto';
import GeoMap from './GeoMap.vue';

const router = useRouter();

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
  { label: 'This Week', value: 'week' },
  { label: 'This Month', value: 'month' },
  { label: 'This Year', value: 'year' }
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

// Metrics data
const metrics = computed<Metric[]>(() => {
  const multiplier = getTimeMultiplier();

  return [
    {
      key: 'totalSales',
      label: 'Total Sales',
      value: 152489.67 * multiplier,
      trend: 12.5,
      subtext: 'vs previous period',
      icon: 'fas fa-shopping-cart',
      iconClass: 'ni-b',
      trendStyle: `color: var(--ni-green); background: rgba(var(--ni-green-rgb, 34,197,94), 0.1);`,
      format: 'currency'
    },
    {
      key: 'totalOrders',
      label: 'Total Orders',
      value: 1242 * multiplier,
      trend: 8.2,
      subtext: `Avg order: ${formatCurrency(74.53)}`,
      icon: 'fas fa-box',
      iconClass: 'ni-g',
      trendStyle: `color: var(--ni-green); background: rgba(var(--ni-green-rgb, 34,197,94), 0.1);`,
      format: 'number'
    },
    {
      key: 'totalCustomers',
      label: 'Active Customers',
      value: 2432 * multiplier,
      trend: 15.3,
      subtext: `+${Math.round(128 * multiplier)} new this month`,
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
      subtext: `From ${Math.round(76025 * multiplier)} visitors`,
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
    coordinates: [40, -100] as [number, number], // Approximate center
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

// Category revenue data
const categoryRevenue = computed<CategoryRevenue[]>(() => {
  const multiplier = getTimeMultiplier();
  const data = [
    { category: 'Electronics', revenue: 45230 * multiplier },
    { category: 'Furniture', revenue: 32180 * multiplier },
    { category: 'Clothing', revenue: 28450 * multiplier },
    { category: 'Food & Drink', revenue: 19890 * multiplier },
    { category: 'Books', revenue: 12450 * multiplier }
  ];

  const total = data.reduce((sum, item) => sum + item.revenue, 0);

  return data.map(item => ({
    ...item,
    percentage: Math.round((item.revenue / total) * 100)
  })).sort((a, b) => b.revenue - a.revenue);
});

// Top products
const topProducts = computed<Product[]>(() => {
  const multiplier = getTimeMultiplier();
  return [
    { id: 1, name: 'Wireless Earbuds Pro', category: 'Electronics', revenue: 12450 * multiplier, sales: Math.round(89 * multiplier), stock: 45 },
    { id: 2, name: 'Ergonomic Office Chair', category: 'Furniture', revenue: 8999 * multiplier, sales: Math.round(23 * multiplier), stock: 12 },
    { id: 3, name: 'Organic Coffee Beans', category: 'Food & Drink', revenue: 7245 * multiplier, sales: Math.round(178 * multiplier), stock: 234 },
    { id: 4, name: 'Fitness Tracker Watch', category: 'Wearables', revenue: 6120 * multiplier, sales: Math.round(56 * multiplier), stock: 78 },
    { id: 5, name: 'Designer Backpack', category: 'Accessories', revenue: 4590 * multiplier, sales: Math.round(34 * multiplier), stock: 23 }
  ].sort((a, b) => b.revenue - a.revenue);
});

// Recent orders
const recentOrders = computed<Order[]>(() => {
  const baseOrders: Order[] = [
    { id: '7842', customerName: 'Alex Johnson', date: '2024-01-15', amount: 124.99, status: 'completed' },
    { id: '7841', customerName: 'Maria Garcia', date: '2024-01-15', amount: 89.50, status: 'completed' },
    { id: '7840', customerName: 'David Chen', date: '2024-01-14', amount: 245.75, status: 'processing' },
    { id: '7839', customerName: 'Sarah Williams', date: '2024-01-14', amount: 67.25, status: 'completed' },
    { id: '7838', customerName: 'James Wilson', date: '2024-01-13', amount: 189.99, status: 'pending' },
    { id: '7837', customerName: 'Lisa Anderson', date: '2024-01-13', amount: 320.50, status: 'shipped' }
  ];

  if (timeRange.value === 'custom' && customDateRange.start && customDateRange.end) {
    return baseOrders.filter(order => {
      return isDateAfter(order.date, customDateRange.start) &&
             isDateAfter(customDateRange.end, order.date);
    });
  }

  return baseOrders;
});

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

  // Handle custom range by using month data as default
  if (timeRange.value === 'custom') {
    return {
      labels: ranges.month.labels,
      datasets: [{
        label: 'Revenue',
        data: ranges.month.data,
        borderColor: '#3B82F6',
        backgroundColor: salesChartType.value === 'line'
          ? 'rgba(59, 130, 246, 0.1)'
          : 'rgba(59, 130, 246, 0.8)',
        fill: salesChartType.value === 'line',
        tension: 0.4,
        borderWidth: 2,
        pointBackgroundColor: '#3B82F6',
        pointBorderColor: '#ffffff',
        pointBorderWidth: 2,
        pointRadius: 3,
        pointHoverRadius: 5
      }]
    };
  }

  const range = ranges[timeRange.value];

  return {
    labels: range.labels,
    datasets: [{
      label: 'Revenue',
      data: range.data,
      borderColor: '#3B82F6',
      backgroundColor: salesChartType.value === 'line'
        ? 'rgba(59, 130, 246, 0.1)'
        : 'rgba(59, 130, 246, 0.8)',
      fill: salesChartType.value === 'line',
      tension: 0.4,
      borderWidth: 2,
      pointBackgroundColor: '#3B82F6',
      pointBorderColor: '#ffffff',
      pointBorderWidth: 2,
      pointRadius: 3,
      pointHoverRadius: 5
    }]
  };
});

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: false
    },
    tooltip: {
      backgroundColor: 'rgba(17, 24, 39, 0.95)',
      titleColor: '#ffffff',
      bodyColor: '#D1D5DB',
      borderColor: '#374151',
      borderWidth: 1,
      cornerRadius: 6,
      displayColors: false,
      callbacks: {
        label: (context: any) => `$${context.parsed.y.toLocaleString()}`
      }
    }
  },
  scales: {
    y: {
      beginAtZero: true,
      grid: {
        color: 'rgba(255, 255, 255, 0.05)'
      },
      ticks: {
        color: 'rgba(255, 255, 255, 0.6)',
        callback: (value: any) => '$' + value.toLocaleString()
      }
    },
    x: {
      grid: {
        color: 'rgba(255, 255, 255, 0.05)'
      },
      ticks: {
        color: 'rgba(255, 255, 255, 0.6)'
      }
    }
  }
};

let chart: Chart | null = null;

// Helper functions
function getTimeMultiplier(): number {
  const multipliers = {
    today: 0.1,
    week: 0.5,
    month: 1,
    year: 12,
    custom: 1
  };
  return multipliers[timeRange.value] || 1;
}

function formatMetricValue(metric: Metric): string {
  const value = metric.value;

  switch (metric.format) {
    case 'currency':
      return formatCurrency(value);
    case 'percent':
      return value.toFixed(1) + '%';
    default:
      return Math.round(value).toLocaleString();
  }
}

function stringToColor(str: string): string {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  const hue = hash % 360;
  return `hsl(${hue}, 70%, 50%)`;
}

function getOrderStatusVariant(status: string): string {
  switch (status) {
    case 'completed': return 'success';
    case 'processing': return 'secondary';
    case 'shipped': return 'primary';
    case 'pending': return 'warning';
    case 'cancelled': return 'danger';
    default: return 'secondary';
  }
}

// Methods
function setTimeRange(range: TimeRange) {
  timeRange.value = range;
  loading.value = true;

  setTimeout(() => {
    loading.value = false;
  }, 500);
}

function applyCustomDate() {
  if (isCustomDateValid.value) {
    timeRange.value = 'custom' as TimeRange;
    setTimeRange('custom' as TimeRange);
  }
}

function handleExport() {
  const data = {
    metrics: metrics.value,
    categories: categoryRevenue.value,
    products: topProducts.value,
    orders: recentOrders.value,
    regions: topRegions.value,
    timeRange: timeRange.value,
    exportDate: new Date().toISOString()
  };

  const dataStr = JSON.stringify(data, null, 2);
  const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr);

  const date = new Date();
  const fileName = `analytics_export_${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}.json`;

  const linkElement = document.createElement('a');
  linkElement.setAttribute('href', dataUri);
  linkElement.setAttribute('download', fileName);
  linkElement.click();
}

function handleRefresh() {
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
  }, 500);
}

function exportCategories() {
  const dataStr = JSON.stringify(categoryRevenue.value, null, 2);
  const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr);

  const date = new Date();
  const fileName = `category_revenue_${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}.json`;

  const linkElement = document.createElement('a');
  linkElement.setAttribute('href', dataUri);
  linkElement.setAttribute('download', fileName);
  linkElement.click();
}

function viewAllProducts() {
  router.push('/products');
}

function goToOrder(orderId: string) {
  router.push(`/orders/${orderId}`);
}

function updateChart() {
  if (chart) {
    chart.destroy();
    const ctx = document.getElementById('salesChart') as HTMLCanvasElement;
    if (ctx) {
      chart = new Chart(ctx, {
        type: salesChartType.value,
        data: salesChartData.value,
        options: chartOptions
      });
    }
  }
}

// Lifecycle
onMounted(() => {
  const ctx = document.getElementById('salesChart') as HTMLCanvasElement;
  if (ctx) {
    chart = new Chart(ctx, {
      type: salesChartType.value,
      data: salesChartData.value,
      options: chartOptions
    });
  }
});

onUnmounted(() => {
  if (chart) {
    chart.destroy();
  }
});

// Watchers
watch([timeRange, salesChartType], () => {
  updateChart();
});
</script>

