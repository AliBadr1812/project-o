<template>
  <div class="analytics-dashboard gap-6">
    <!-- Header -->
    <div class="flex items-center justify-between pb-5">
      <div>
        <h1 class="page-title">Analytics Dashboard</h1>
        <p class="text-[var(--text-secondary)]">Track your business performance and metrics</p>
      </div>
      <div class="flex items-center gap-3">
        <Button class="text-[var(--color-gray-400)] active:text-[var(--color-white)]" variant="outline" size="sm" @click="handleRefresh">
            <i class="fas fa-sync-alt fa-1x mr-1"></i>
            Refresh
        </Button>
        <Button class="btn-accent" variant="outline" size="sm" @click="handleExport">
            <i class="fas fa-arrow-up-from-bracket fa-1x mr-1"></i>
            Export
        </Button>
      </div>
    </div>

    <!-- Time Range Selector -->
    <Card class="mb-6">
      <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4 p-4">
        <div class="flex items-center gap-2">
          <Button
            v-for="range in timeRanges"
            class="text-[var(--color-gray-400)] active:text-[var(--color-white)]"
            :key="range.value"
            :variant="timeRange === range.value ? 'primary' : 'outline'"
            size="sm"
            @click="setTimeRange(range.value)"
          >
            {{ range.label }}
          </Button>
        </div>
        <div class="flex items-center gap-2">
          <input
            type="date"
            v-model="customDateRange.start"
            class="bg-[var(--glass-bg)] border border-[var(--glass-border)] rounded-lg px-3 py-2 text-sm text-[var(--text-secondary)] focus:outline-none focus:ring-2 focus:ring-accent"
            :max="customDateRange.end"
          />
          <span class="text-[var(--text-secondary)]">to</span>
          <input
            type="date"
            v-model="customDateRange.end"
            class="bg-[var(--glass-bg)] border border-[var(--glass-border)] rounded-lg px-3 py-2 text-sm text-[var(--text-secondary)] focus:outline-none focus:ring-2 focus:ring-accent"
            :min="customDateRange.start"
            :max="today"
          />
          <Button
            variant="outline"
            class="flex items-center px-3 py-1.5 bg-[var(--accent)] text-[var(--text-primary)] rounded-lg hover:bg-blue-700 transition-colors duration-200"
            size="sm"
            @click="applyCustomDate"
            :disabled="!isCustomDateValid"
          >
            Apply
          </Button>
        </div>
      </div>
    </Card>

    <!-- Stats Bar -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
      <Card v-for="metric in metrics" :key="metric.key" class="p-5 cursor-pointer hover:bg-[rgba(255,255,255,0.35)] transition-all duration-200 hover:translate-y-[-2px]">
        <div class="flex items-center justify-between mb-4">
          <div :class="['w-12 h-12 rounded-lg flex items-center justify-center', metric.iconBg]">
            <i :class="[metric.icon, metric.iconColor, 'text-xl']"></i>
          </div>
          <div :class="['text-sm flex items-center px-2 py-1 rounded-full', metric.trendBg]">
            <i v-if="metric.trend > 0" class="fas fa-arrow-up fa-2xs text-green-500 mr-1"></i>
            <i v-else class="fas fa-arrow-down fa-2xs text-red-500 mr-1"></i>
            {{ Math.abs(metric.trend) }}%
          </div>
        </div>
        <p class="text-sm font-medium text-[var(--text-secondary)] mb-1">{{ metric.label }}</p>
        <p class="text-2xl font-bold text-[var(--text-primary)] mb-2">{{ formatMetricValue(metric) }}</p>
        <p class="text-xs text-[var(--text-secondary)]">{{ metric.subtext }}</p>
      </Card>
    </div>

    <!-- Charts Section -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
      <!-- Sales Chart -->
      <Card class="lg:col-span-2">
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-lg font-semibold text-[var(--text-primary)]">Sales Trend</h2>
              <p class="text-sm text-[var(--text-secondary)]">Revenue over time</p>
            </div>
            <div class="flex items-center gap-2">
              <select
                v-model="salesChartType"
                class="bg-[var(--glass-bg)] border border-[var(--glass-border)] rounded-lg px-3 py-1.5 text-sm text-[var(--text-secondary)] focus:outline-none focus:ring-2 focus:ring-accent"
              >
                <option value="line">Line Chart</option>
                <option value="bar">Bar Chart</option>
              </select>
            </div>
          </div>
        </div>
        <div class="p-6">
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
              <h2 class="text-lg font-semibold text-[var(--text-primary)]">Revenue by Category</h2>
              <p class="text-sm text-[var(--text-secondary)]">Distribution across categories</p>
            </div>
            <Button variant="ghost" size="sm" @click="exportCategories">
              <i class="fas fa-download"></i>
            </Button>
          </div>
        </div>
        <div class="p-4">
          <div v-if="categoryRevenue.length === 0" class="h-64 flex items-center justify-center">
            <div class="text-center text-[var(--text-secondary)]">
              <svg class="w-12 h-12 mx-auto mb-3" fill="currentColor" viewBox="0 0 20 20">
                <path d="M2 10a8 8 0 018-8v8h8a8 8 0 11-16 0z"/>
                <path d="M12 2.252A8.014 8.014 0 0117.748 8H12V2.252z"/>
              </svg>
              <p>No data available</p>
            </div>
          </div>
          <div v-else class="space-y-4">
            <div v-for="item in categoryRevenue" :key="item.category" class="space-y-1">
              <div class="flex justify-between text-sm">
                <span class="text-[var(--text-secondary)]">{{ item.category }}</span>
                <span class="text-[var(--text-primary)] font-medium">{{ formatCurrency(item.revenue) }}</span>
              </div>
              <div class="h-2 bg-[var(--glass-bg)] rounded-full overflow-hidden">
                <div
                  :style="{ width: item.percentage + '%' }"
                  class="h-full bg-blue-500 rounded-full transition-all duration-500"
                ></div>
              </div>
            </div>
          </div>
        </div>
      </Card>
    </div>

    <!-- Top Products and Recent Orders -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Top Products -->
      <Card>
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-lg font-semibold text-[var(--text-primary)]">Top Products</h2>
              <p class="text-sm text-[var(--text-secondary)]">Best selling products</p>
            </div>
            <router-link to="/products" class="text-sm text-[var(--accent)] hover:text-[var(--text-accent)] transition-colors">
              View all
            </router-link>
          </div>
        </div>
        <Table>
          <template #header>
            <tr class="bg-[var(--glass-bg)] text-[var(--text-secondary)]">
              <th class="py-3 px-6 text-left">Product</th>
              <th class="py-3 px-6 text-left">Sales</th>
              <th class="py-3 px-6 text-left">Revenue</th>
              <th class="py-3 px-6 text-left">Stock</th>
            </tr>
          </template>
          <template #body>
            <tr v-for="product in topProducts" :key="product.id" class="hover:bg-[var(--glass-bg)]">
              <td class="py-3 px-6">
                <div class="flex items-center gap-3">
                  <div class="w-10 h-10 bg-[var(--glass-bg)] rounded-lg flex items-center justify-center">
                    <svg class="w-5 h-5 text-[var(--text-muted)]" fill="currentColor" viewBox="0 0 20 20">
                      <path d="M4 3a2 2 0 100 4h12a2 2 0 100-4H4z"/>
                      <path fill-rule="evenodd" d="M3 8h14v7a2 2 0 01-2 2H5a2 2 0 01-2-2V8zm5 3a1 1 0 011-1h2a1 1 0 110 2H9a1 1 0 01-1-1z" clip-rule="evenodd"/>
                    </svg>
                  </div>
                  <div>
                    <p class="font-medium text-[var(--text-primary)] text-sm">{{ product.name }}</p>
                    <p class="text-xs text-[var(--text-secondary)]">{{ product.category }}</p>
                  </div>
                </div>
              </td>
              <td class="py-3 px-6 text-[var(--text-primary)]">{{ product.sales }}</td>
              <td class="py-3 px-6 text-[var(--text-primary)]">{{ formatCurrency(product.revenue) }}</td>
              <td class="py-3 px-6">
                <Badge
                  :variant="product.stock > 20 ? 'success' : product.stock > 5 ? 'warning' : 'danger'"
                >
                  {{ product.stock }} left
                </Badge>
              </td>
            </tr>
          </template>
        </Table>
        <div class="border-t border-[var(--glass-border)] px-6 py-3 text-right">
          <Button variant="ghost" size="sm" @click="viewAllProducts">
            View All Products
          </Button>
        </div>
      </Card>

      <!-- Recent Orders -->
      <Card>
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-lg font-semibold text-[var(--text-primary)]">Recent Orders</h2>
              <p class="text-sm text-[var(--text-secondary)]">Latest customer orders</p>
            </div>
            <router-link to="/orders" class="text-sm text-[var(--accent)] hover:text-[var(--text-accent)] transition-colors">
              View all
            </router-link>
          </div>
        </div>
        <div class="divide-y divide-[var(--color-border)]">
          <div
            v-for="order in recentOrders"
            :key="order.id"
            class="flex items-center justify-between p-4 hover:bg-[var(--glass-bg)] transition-colors cursor-pointer"
            @click="goToOrder(order.id)"
          >
            <div class="flex items-center gap-3">
              <div
                class="w-10 h-10 rounded-lg flex items-center justify-center text-[var(--text-primary)] text-sm font-medium"
                :style="{ background: `linear-gradient(135deg, ${stringToColor(order.customerName)}, ${stringToColor(order.customerName + '2')})` }"
              >
                {{ getInitials(order.customerName) }}
              </div>
              <div>
                <p class="font-medium text-[var(--text-primary)] text-sm">Order #{{ order.id }}</p>
                <p class="text-xs text-[var(--text-secondary)]">{{ order.customerName }}</p>
              </div>
            </div>
            <div class="text-right">
              <p class="font-medium text-[var(--text-primary)] text-sm">{{ formatCurrency(order.amount) }}</p>
              <Badge :variant="getOrderStatusVariant(order.status)">
                {{ order.status }}
              </Badge>
            </div>
          </div>
        </div>
      </Card>
    </div>

    <!-- Geographic Distribution -->
    <Card class="mt-6">
      <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
        <h2 class="text-lg font-semibold text-[var(--text-primary)]">Geographic Distribution</h2>
        <p class="text-sm text-[var(--text-secondary)]">Customer locations by region</p>
      </div>
      <div class="p-6">
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <div>
                <GeoMap :regions="mapRegions" />
            </div>
          <div class="space-y-4">
            <h3 class="text-sm font-medium text-[var(--text-secondary)] mb-3">Top Regions</h3>
            <div v-for="region in topRegions" :key="region.name" class="space-y-1">
              <div class="flex justify-between text-sm">
                <span class="text-[var(--text-secondary)]">{{ region.name }}</span>
                <span class="text-[var(--text-primary)] font-medium">{{ region.percentage }}%</span>
              </div>
              <div class="h-1.5 bg-[var(--glass-bg)] rounded-full overflow-hidden">
                <div
                  :style="{ width: region.percentage + '%' }"
                  class="h-full bg-blue-500 rounded-full transition-all duration-500"
                ></div>
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
import Button from '@/components/ui/Button.vue';
import Card from '@/components/ui/Card.vue';
import Table from '@/components/ui/Table.vue';
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
  iconBg: string;
  iconColor: string;
  trendBg: string;
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
      iconBg: 'bg-blue-500/10',
      iconColor: 'text-[var(--accent)]',
      trendBg: 'text-green-500 bg-green-500/10',
      format: 'currency'
    },
    {
      key: 'totalOrders',
      label: 'Total Orders',
      value: 1242 * multiplier,
      trend: 8.2,
      subtext: `Avg order: ${formatCurrency(74.53)}`,
      icon: 'fas fa-box',
      iconBg: 'bg-green-500/10',
      iconColor: 'text-green-500',
      trendBg: 'text-green-500 bg-green-500/10',
      format: 'number'
    },
    {
      key: 'totalCustomers',
      label: 'Active Customers',
      value: 2432 * multiplier,
      trend: 15.3,
      subtext: `+${Math.round(128 * multiplier)} new this month`,
      icon: 'fas fa-users',
      iconBg: 'bg-purple-500/10',
      iconColor: 'text-purple-500',
      trendBg: 'text-green-500 bg-green-500/10',
      format: 'number'
    },
    {
      key: 'conversionRate',
      label: 'Conversion Rate',
      value: 3.2,
      trend: -2.1,
      subtext: `From ${Math.round(76025 * multiplier)} visitors`,
      icon: 'fas fa-eye',
      iconBg: 'bg-yellow-500/10',
      iconColor: 'text-yellow-500',
      trendBg: 'text-red-500 bg-red-500/10',
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

<style scoped>
.analytics-dashboard {
  min-height: 100vh;
}

/* Stats card hover effect */
.hover\:shadow-lg {
  transition: box-shadow 0.3s ease;
}

/* Progress bar animations */
.h-2 > div, .h-1\.5 > div {
  transition: width 0.5s ease-in-out;
}

/* Chart container */
canvas {
  max-width: 100%;
  height: auto;
  image-rendering: -webkit-optimize-contrast;
  image-rendering: crisp-edges;
}

/* Date input styling */
input[type="date"] {
  color-scheme: dark;
}

input[type="date"]::-webkit-calendar-picker-indicator {
  filter: invert(1);
  opacity: 0.6;
  cursor: pointer;
}

input[type="date"]::-webkit-calendar-picker-indicator:hover {
  opacity: 1;
}

/* Table styles from CategoryList */
th {
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  font-size: 0.75rem;
}

tbody tr {
  transition: background-color 0.15s ease;
  cursor: pointer;
}

/* Avatar gradient effect from CategoryList */
.w-10.h-10 {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.w-10.h-10:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

/* Select dropdown styling from CategoryList */
select {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 20 20'%3e%3cpath stroke='%236B7280' stroke-linecap='round' stroke-linejoin='round' stroke-width='1.5' d='M6 8l4 4 4-4'/%3e%3c/svg%3e");
  background-position: right 0.5rem center;
  background-repeat: no-repeat;
  background-size: 1.5em 1.5em;
  padding-right: 2.5rem;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}

/* Loading animation */
.animate-spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .gap-6 {
    gap: 1rem;
  }

  .text-2xl {
    font-size: 1.5rem;
  }

  .p-6 {
    padding: 1rem;
  }
}

/* Smooth transitions */
button, a, .cursor-pointer {
  transition: all 0.15s ease-out;
}

/* Focus styles */
button:focus-visible, a:focus-visible {
  outline: 2px solid #3B82F6;
  outline-offset: 2px;
}

/* Disabled state */
button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Icon styling from CategoryList */
.fas {
  font-size: 0.875rem;
}
</style>
