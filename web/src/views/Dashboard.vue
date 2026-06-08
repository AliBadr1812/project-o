<template>
  <div class="grid grid-cols-1 gap-6">

    <!-- Page header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Good evening 👋</h1>
        <p class="page-subtitle">Here's what's happening with your store today.</p>
      </div>
      <div class="flex items-center gap-3">
        <div class="relative">
          <select v-model="selectedRange" class="glass-select text-sm">
            <option>Today</option>
            <option>Last 7 days</option>
            <option>Last 30 days</option>
            <option>Last Quarter</option>
          </select>
          <i class="fas fa-angle-down absolute right-3 top-1/2 -translate-y-1/2 text-xs pointer-events-none"
             style="color: var(--text-muted);"></i>
        </div>
        <button @click="refreshData" class="btn-glass gap-2">
          <i class="fas fa-rotate-right text-xs"></i>
          Refresh
        </button>
      </div>
    </div>

    <!-- Stat cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">

      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-p">
              <i class="fas fa-dollar-sign"></i>
            </div>
            <span class="badge badge-success">
              <i class="fas fa-arrow-up text-[9px] mr-1"></i>12.5%
            </span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Total Revenue</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ formatCurrency(stats.totalRevenue) }}</p>
          <Sparkline :data="revenueSparkline" :width="120" :height="36" color="var(--ni-purple)" class="my-2 w-full" />
          <p class="text-xs pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">
            vs {{ formatCurrency(stats.lastMonthRevenue) }} last month
          </p>
        </div>
      </Card>

      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-g">
              <i class="fas fa-bag-shopping"></i>
            </div>
            <span class="badge badge-success">
              <i class="fas fa-arrow-up text-[9px] mr-1"></i>8.2%
            </span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Total Orders</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.totalOrders }}</p>
          <Sparkline :data="ordersSparkline" :width="120" :height="36" color="var(--ni-green)" class="my-2 w-full" />
          <p class="text-xs pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">
            ${{ stats.avgOrderValue }} avg order value
          </p>
        </div>
      </Card>

      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-t">
              <i class="fas fa-users"></i>
            </div>
            <span class="badge badge-success">
              <i class="fas fa-arrow-up text-[9px] mr-1"></i>15.3%
            </span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Active Customers</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.totalCustomers }}</p>
          <Sparkline :data="customersSparkline" :width="120" :height="36" color="var(--ni-teal)" class="my-2 w-full" />
          <p class="text-xs pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">
            {{ stats.newCustomers }} new this month
          </p>
        </div>
      </Card>

      <Card>
        <div class="p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="stat-icon ni-o">
              <i class="fas fa-chart-line"></i>
            </div>
            <span class="badge badge-danger">
              <i class="fas fa-arrow-down text-[9px] mr-1"></i>2.1%
            </span>
          </div>
          <p class="text-sm font-medium mb-1" style="color: var(--text-secondary);">Conversion Rate</p>
          <p class="text-2xl font-bold tracking-tight" style="color: var(--text-primary);">{{ stats.conversionRate }}%</p>
          <Sparkline :data="conversionSparkline" :width="120" :height="36" color="var(--ni-orange)" class="my-2 w-full" />
          <p class="text-xs pt-2" style="color: var(--text-muted); border-top: 1px solid var(--glass-border);">
            From {{ stats.totalVisitors.toLocaleString() }} visitors
          </p>
        </div>
      </Card>

    </div>

    <!-- Charts row -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

      <!-- Revenue chart -->
      <Card class="lg:col-span-2">
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Revenue Overview</h2>
              <p class="text-xs mt-0.5" style="color: var(--text-muted);">Total revenue and growth over time</p>
            </div>
            <div class="flex items-center gap-1.5">
              <button
                v-for="r in ['week','month','quarter']" :key="r"
                @click="setChartRange(r as 'week' | 'month' | 'quarter')"
                class="px-3 py-1.5 text-xs font-semibold rounded-lg transition-all"
                :class="chartRange === r ? 'btn-accent' : 'btn-glass'"
              >{{ r.charAt(0).toUpperCase() + r.slice(1) }}</button>
            </div>
          </div>
        </div>
        <div class="p-6">
          <div class="h-64"><canvas id="revenueChart"></canvas></div>
        </div>
      </Card>

      <!-- Top products -->
      <Card>
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Top Products</h2>
          <p class="text-xs mt-0.5" style="color: var(--text-muted);">Best selling this month</p>
        </div>
        <div class="p-4 flex flex-col gap-1">
          <div
            v-for="product in topProducts" :key="product.id"
            class="flex items-center justify-between p-3 rounded-xl transition-all"
            style="transition: background 0.15s;"
            @mouseenter="(e: MouseEvent) => (e.currentTarget as HTMLElement).style.background = 'rgba(255,255,255,0.4)'"
            @mouseleave="(e: MouseEvent) => (e.currentTarget as HTMLElement).style.background = 'transparent'"
          >
            <div class="flex items-center gap-3">
              <div class="stat-icon ni-o text-sm">
                <i class="fas fa-box"></i>
              </div>
              <div>
                <p class="text-sm font-medium" style="color: var(--text-primary);">{{ product.name }}</p>
                <p class="text-xs" style="color: var(--text-muted);">{{ product.category }}</p>
              </div>
            </div>
            <div class="text-right">
              <p class="text-sm font-semibold" style="color: var(--text-accent);">{{ formatCurrency(product.revenue) }}</p>
              <p class="text-xs" style="color: var(--text-muted);">{{ product.sales }} sold</p>
            </div>
          </div>
          <router-link to="/products" class="block mt-2 text-center text-sm font-medium hover:opacity-80 transition-opacity" style="color: var(--text-accent);">
            View all products →
          </router-link>
        </div>
      </Card>

    </div>

    <!-- Activity row -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

      <!-- Recent orders -->
      <Card class="lg:col-span-2 overflow-hidden">
        <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Recent Orders</h2>
              <p class="text-xs mt-0.5" style="color: var(--text-muted);">Latest customer orders</p>
            </div>
            <router-link to="/orders" class="text-sm font-medium hover:opacity-80 transition-opacity" style="color: var(--text-accent);">
              View all
            </router-link>
          </div>
        </div>
        <div class="overflow-x-auto">
          <table class="glass-table w-full">
            <thead>
              <tr>
                <th>Order ID</th>
                <th>Customer</th>
                <th>Date</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="order in recentOrders" :key="order.id"
                class="cursor-pointer"
                @click="viewOrder(order.id)"
              >
                <td>
                  <span class="font-medium" style="color: var(--text-accent);">#{{ formatOrderNumber(order.id) }}</span>
                </td>
                <td>
                  <div class="flex items-center gap-2.5">
                    <div
                      class="w-7 h-7 rounded-full flex items-center justify-center text-[var(--text-primary)] text-xs font-semibold flex-shrink-0"
                      :style="{ background: `linear-gradient(135deg, ${stringToColor(order.customerName)}, ${stringToColor(order.customerName+'2')})` }"
                    >{{ getInitials(order.customerName) }}</div>
                    <span class="td-primary">{{ order.customerName }}</span>
                  </div>
                </td>
                <td>{{ formatDate(order.createdAt ?? '') }}</td>
                <td class="td-accent">{{ formatCurrency(order.total) }}</td>
                <td>
                  <Badge :variant="getStatusVariant(order.status)">{{ order.status }}</Badge>
                </td>
                <td>
                  <div class="flex items-center gap-1" @click.stop>
                    <button @click="viewOrder(order.id)" class="btn-glass-icon w-7 h-7 rounded-lg text-xs">
                      <i class="fas fa-eye"></i>
                    </button>
                    <button
                      v-if="order.status === 'pending'"
                      @click="processOrder(order.id)"
                      class="btn-glass-icon w-7 h-7 rounded-lg text-xs"
                      style="color: var(--ni-green);"
                    >
                      <i class="fas fa-check"></i>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </Card>

      <!-- Right column -->
      <div class="flex flex-col gap-6">

        <!-- Quick actions -->
        <Card>
          <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Quick Actions</h2>
          </div>
          <div class="p-4 grid grid-cols-2 gap-2">
            <button
              v-for="action in quickActions" :key="action.label"
              @click="goToPage(action.path)"
              class="flex flex-col items-center gap-2 p-4 rounded-xl transition-all"
              style="transition: background 0.15s;"
              @mouseenter="(e: MouseEvent) => (e.currentTarget as HTMLElement).style.background = 'rgba(255,255,255,0.4)'"
              @mouseleave="(e: MouseEvent) => (e.currentTarget as HTMLElement).style.background = 'transparent'"
            >
              <div class="stat-icon text-sm" :class="action.color">
                <i :class="action.icon"></i>
              </div>
              <span class="text-xs font-medium" style="color: var(--text-secondary);">{{ action.label }}</span>
            </button>
          </div>
        </Card>

        <!-- Store performance -->
        <Card>
          <div class="px-6 py-4" style="border-bottom: 1px solid var(--glass-border);">
            <h2 class="text-[15px] font-semibold" style="color: var(--text-primary);">Store Performance</h2>
          </div>
          <div class="p-5 flex flex-col gap-4">
            <div v-for="perf in performance" :key="perf.label">
              <div class="flex justify-between text-sm mb-2">
                <span style="color: var(--text-secondary);">{{ perf.label }}</span>
                <span class="font-semibold" style="color: var(--text-primary);">{{ perf.value }}%</span>
              </div>
              <div class="progress-track">
                <div class="progress-fill" :style="{ width: perf.value + '%', background: perf.gradient }"></div>
              </div>
            </div>
          </div>
        </Card>

      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import { storeToRefs } from 'pinia';
import Card      from '@/components/ui/Card.vue';
import Badge     from '@/components/ui/Badge.vue';
import Sparkline from '@/components/ui/Sparkline.vue';
import { formatCurrency, formatOrderNumber, getInitials } from '@/utils/formatters';
import { Chart, registerables } from 'chart.js';
import { useRouter } from 'vue-router';
import { useOrderStore }    from '@/stores/orderStore';
import { useProductStore }  from '@/stores/productStore';
import { useCustomerStore } from '@/stores/customerStore';

Chart.register(...registerables);
const router = useRouter();

// ── Stores ────────────────────────────────────────────────────────────────
const orderStore    = useOrderStore();
const productStore  = useProductStore();
const customerStore = useCustomerStore();

const { items: orders }    = storeToRefs(orderStore);
const { items: products }  = storeToRefs(productStore);
const { items: customers } = storeToRefs(customerStore);

onMounted(() => {
  orderStore.fetchAll();
  productStore.fetchAll();
  customerStore.fetchAll();
});

const chartRange    = ref<'week' | 'month' | 'quarter'>('month');
const selectedRange = ref('Last 30 days');

// ── Computed stats from real data ─────────────────────────────────────────
const stats = computed(() => {
  const now        = new Date();
  const msPerMonth = 30 * 24 * 60 * 60 * 1000;

  const activeOrders = orders.value.filter(
    o => o.status !== 'cancelled' && o.status !== 'refunded',
  );
  const totalRevenue    = activeOrders.reduce((s, o) => s + o.total, 0);
  const totalOrders     = orders.value.length;
  const avgOrderValue   = totalOrders > 0 ? Math.round((totalRevenue / totalOrders) * 100) / 100 : 0;

  // "last month" = orders placed 30–60 days ago
  const lastMonthOrders = orders.value.filter(o => {
    if (!o.createdAt) return false;
    const age = now.getTime() - new Date(o.createdAt).getTime();
    return age > msPerMonth && age <= 2 * msPerMonth;
  });
  const lastMonthRevenue = lastMonthOrders
    .filter(o => o.status !== 'cancelled' && o.status !== 'refunded')
    .reduce((s, o) => s + o.total, 0);

  const totalCustomers = customers.value.filter(c => c.status === 'active').length;
  const newCustomers   = customers.value.filter(c => {
    if (!c.lastOrderDate) return false;
    return now.getTime() - new Date(c.lastOrderDate).getTime() <= msPerMonth;
  }).length;

  const completionRate = totalOrders > 0
    ? Math.round((orders.value.filter(o => o.status === 'delivered').length / totalOrders) * 100)
    : 0;

  const inStockProducts = products.value.filter(p => p.stock > 0).length;
  const inventoryHealth = products.value.length > 0
    ? Math.round((inStockProducts / products.value.length) * 100)
    : 0;

  // Satisfaction: proxy — % of VIP + returning customers
  const happyCustomers = customers.value.filter(c => c.type === 'vip' || c.type === 'returning').length;
  const satisfactionRate = customers.value.length > 0
    ? Math.round((happyCustomers / customers.value.length) * 100)
    : 0;

  return {
    totalRevenue,
    lastMonthRevenue,
    totalOrders,
    avgOrderValue,
    totalCustomers,
    newCustomers,
    conversionRate:  3.2,   // no visitor tracking — keep static
    totalVisitors:   76025, // no visitor tracking — keep static
    completionRate,
    satisfactionRate,
    inventoryHealth,
  };
});

// ── 7-day sparklines derived from real order data ─────────────────────────
function last7DaysBuckets<T>(items: T[], getDate: (i: T) => string): number[] {
  const buckets = new Array(7).fill(0);
  const now = Date.now();
  const dayMs = 86_400_000;
  for (const item of items) {
    const age = now - new Date(getDate(item)).getTime();
    const dayIdx = Math.floor(age / dayMs);
    if (dayIdx >= 0 && dayIdx < 7) buckets[6 - dayIdx]++;
  }
  return buckets;
}

const revenueSparkline = computed(() => {
  const buckets = new Array(7).fill(0);
  const now = Date.now();
  const dayMs = 86_400_000;
  for (const o of orders.value) {
    if (o.status === 'cancelled' || o.status === 'refunded') continue;
    const age = now - new Date(o.createdAt ?? '').getTime();
    const idx = Math.floor(age / dayMs);
    if (idx >= 0 && idx < 7) buckets[6 - idx] += o.total;
  }
  return buckets;
});

const ordersSparkline   = computed(() => last7DaysBuckets(orders.value, o => o.createdAt!));
const customersSparkline = computed(() => last7DaysBuckets(
  customers.value.filter(c => c.lastOrderDate), c => c.lastOrderDate!,
));

// Conversion: static 7-day trend (no visitor data available)
const conversionSparkline = [3.5, 3.2, 3.8, 3.1, 3.4, 3.0, 3.2];

// ── Top products: top 4 by price (proxy for revenue potential) ────────────
const topProducts = computed(() =>
  [...products.value]
    .sort((a, b) => b.price - a.price)
    .slice(0, 4)
    .map(p => ({
      id:      p.id,
      name:    p.name,
      category: p.categories || '—',
      revenue: p.price * Math.max(p.stock, 1),   // price × stock as revenue estimate
      sales:   p.stock,
    })),
);

// ── Recent orders: last 6 by createdAt ───────────────────────────────────
const recentOrders = computed(() =>
  [...orders.value]
    .sort((a, b) => {
      const ta = a.createdAt ? new Date(a.createdAt).getTime() : 0;
      const tb = b.createdAt ? new Date(b.createdAt).getTime() : 0;
      return tb - ta;
    })
    .slice(0, 6),
);

const quickActions = [
  { label: 'Add Product',  path: '/products/create', icon: 'fas fa-plus',        color: 'ni-p' },
  { label: 'View Orders',  path: '/orders',           icon: 'fas fa-bag-shopping',color: 'ni-g' },
  { label: 'Customers',    path: '/customers',        icon: 'fas fa-users',       color: 'ni-t' },
  { label: 'Analytics',   path: '/analytics',        icon: 'fas fa-chart-line',  color: 'ni-b' },
];

const performance = computed(() => [
  { label: 'Order Completion',      value: stats.value.completionRate,   gradient: 'var(--progress-success)' },
  { label: 'Customer Satisfaction', value: stats.value.satisfactionRate, gradient: 'var(--progress-primary)' },
  { label: 'Inventory Health',      value: stats.value.inventoryHealth,  gradient: 'var(--progress-warning)' },
]);

const chartData = computed(() => {
  const ranges = {
    week:    { labels: ['Mon','Tue','Wed','Thu','Fri','Sat','Sun'],   data: [3200,4200,3800,4500,5200,6100,5800] },
    month:   { labels: ['Week 1','Week 2','Week 3','Week 4'],         data: [12500,14200,13800,15400] },
    quarter: { labels: ['Jan','Feb','Mar'],                           data: [28500,32400,34800] },
  };
  const r = ranges[chartRange.value];
  return {
    labels: r.labels,
    datasets: [{
      label:            'Revenue',
      data:             r.data,
      borderColor:      '#7c3aed',
      backgroundColor:  'rgba(124,58,237,0.08)',
      fill:             true,
      tension:          0.4,
      borderWidth:      2,
      pointBackgroundColor: '#7c3aed',
      pointBorderColor: '#ffffff',
      pointBorderWidth: 2,
      pointRadius:      3,
      pointHoverRadius: 5,
    }],
  };
});

const chartOptions = {
  responsive:          true,
  maintainAspectRatio: false,
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: 'rgba(30,10,60,0.9)',
      titleColor:      '#f0e8ff',
      bodyColor:       'rgba(200,180,240,0.8)',
      borderColor:     'rgba(255,255,255,0.15)',
      borderWidth:     1,
      cornerRadius:    10,
      displayColors:   false,
      callbacks: {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        label: (ctx: any) => `$${ctx.parsed.y.toLocaleString()}`,
      },
    },
  },
  scales: {
    y: {
      beginAtZero: true,
      grid:  { color: 'rgba(255,255,255,0.06)' },
      ticks: { color: 'rgba(60,30,100,0.45)', callback: (v: number | string) => '$' + Number(v).toLocaleString() },
    },
    x: {
      grid:  { color: 'rgba(255,255,255,0.06)' },
      ticks: { color: 'rgba(60,30,100,0.45)' },
    },
  },
};

let chart: Chart | null = null;

const stringToColor = (str: string) => {
  let hash = 0;
  for (let i = 0; i < str.length; i++) hash = str.charCodeAt(i) + ((hash << 5) - hash);
  return `hsl(${hash % 360}, 70%, 55%)`;
};

const getStatusVariant = (status: string) => {
  const map: Record<string, string> = {
    completed:  'success',
    processing: 'info',
    shipped:    'primary',
    pending:    'warning',
    cancelled:  'danger',
  };
  return map[status] || 'neutral';
};

const formatDate = (d: string) =>
  new Date(d).toLocaleDateString('en-US', { month: 'short', day: 'numeric' });

const setChartRange = (range: 'week' | 'month' | 'quarter') => {
  chartRange.value = range;
  updateChart();
};
const updateChart = () => { if (chart) { chart.data = chartData.value; chart.update(); } };
const refreshData = () => {
  orderStore.fetchAll(true);
  productStore.fetchAll(true);
  customerStore.fetchAll(true);
};
const viewOrder   = (id: number | undefined) => { if (id) router.push(`/orders/${id}`); };
const goToPage    = (path: string) => router.push(path);
const processOrder = (id: number | undefined) => {
  if (id) orderStore.updateItem(id, { status: 'processing' });
};

onMounted(() => {
  const ctx = document.getElementById('revenueChart') as HTMLCanvasElement;
  if (ctx) chart = new Chart(ctx, { type: 'line', data: chartData.value, options: chartOptions });
});

watch(chartRange, updateChart);
</script>
