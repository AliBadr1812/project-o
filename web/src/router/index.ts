import { createRouter, createWebHistory } from 'vue-router'
import Dashboard        from '@/views/Dashboard.vue';
import ProductList      from '@/views/products/ProductList.vue';
import ProductForm      from '@/views/products/ProductForm.vue';
import OrderList        from '@/views/orders/OrderList.vue';
import OrderEdit        from '@/views/orders/OrderEdit.vue';
import OrderStats       from '@/views/orders/OrderStats.vue';
import CustomerList     from '@/views/customers/CustomerList.vue';
import CustomerEdit     from '@/views/customers/CustomerEdit.vue';
import AnalyticsDashboard from '@/views/analytics/AnalyticsDashboard.vue';
import Settings         from '@/views/settings/GeneralSettings.vue';
import OrderDetail      from '@/views/orders/OrderDetail.vue';
import ProductDetail    from '@/views/products/ProductDetail.vue';
import CategoryList     from '@/views/categories/CategoryList.vue';
import UserProfile      from '@/views/settings/UserProfile.vue';
import CustomerDetail   from '@/views/customers/CustomerDetail.vue';
import CategoryForm     from '@/views/categories/CategoryForm.vue';
import NotificationList from '@/views/notifications/NotificationList.vue';
import DiscountList     from '@/views/discounts/DiscountList.vue';
import DiscountForm     from '@/views/discounts/DiscountForm.vue';
import InventoryAlerts  from '@/views/inventory/InventoryAlerts.vue';
import ReturnList       from '@/views/returns/ReturnList.vue';
import SegmentList      from '@/views/segments/SegmentList.vue';
import AuditLog         from '@/views/audit/AuditLog.vue';
import LoginView        from '@/views/auth/LoginView.vue';
import NotFound         from '@/views/NotFound.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/dashboard' },

    // ── Public ──────────────────────────────────────────────────────────
    {
      path: '/login',
      name: 'Login',
      component: LoginView,
      meta: { public: true, title: 'Sign In' },
    },

    // ── Dashboard ───────────────────────────────────────────────────────
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: Dashboard,
      meta: { requiresAuth: true, breadcrumb: 'Dashboard', title: 'Dashboard' },
    },

    // ── Products ────────────────────────────────────────────────────────
    {
      path: '/products',
      name: 'Products',
      component: ProductList,
      meta: { requiresAuth: true, breadcrumb: 'Products', title: 'Products' },
    },
    {
      path: '/products/create',
      name: 'CreateProduct',
      component: ProductForm,
      meta: { requiresAuth: true, breadcrumb: 'New Product', title: 'New Product', parent: { path: '/products', name: 'Products' } },
    },
    {
      path: '/products/:id',
      name: 'ProductDetail',
      component: ProductDetail,
      meta: { requiresAuth: true, breadcrumb: 'Product Detail', title: 'Product Detail', parent: { path: '/products', name: 'Products' } },
    },
    {
      path: '/products/:id/edit',
      name: 'EditProduct',
      component: ProductForm,
      meta: { requiresAuth: true, breadcrumb: 'Edit Product', title: 'Edit Product', parent: { path: '/products/:id', name: 'ProductDetail' } },
    },

    // ── Orders ──────────────────────────────────────────────────────────
    {
      path: '/orders',
      name: 'Orders',
      component: OrderList,
      meta: { requiresAuth: true, breadcrumb: 'Orders', title: 'Orders' },
    },
    {
      path: '/orders/stats',
      name: 'OrderStats',
      component: OrderStats,
      meta: { requiresAuth: true, breadcrumb: 'Order Analytics', title: 'Order Analytics', parent: { path: '/orders', name: 'Orders' } },
    },
    {
      path: '/orders/edit/:id',
      name: 'EditOrder',
      component: OrderEdit,
      meta: { requiresAuth: true, breadcrumb: 'Edit Order', title: 'Edit Order', parent: { path: '/orders', name: 'Orders' } },
    },
    {
      path: '/order/:orderId',
      name: 'OrderDetails',
      component: OrderDetail,
      meta: { requiresAuth: true, breadcrumb: 'Order Details', title: 'Order Details', parent: { path: '/orders', name: 'Orders' } },
    },

    // ── Customers ───────────────────────────────────────────────────────
    {
      path: '/customers',
      name: 'Customers',
      component: CustomerList,
      meta: { requiresAuth: true, breadcrumb: 'Customers', title: 'Customers' },
    },
    {
      path: '/customers/edit/:id',
      name: 'EditCustomer',
      component: CustomerEdit,
      meta: { requiresAuth: true, breadcrumb: 'Edit Customer', title: 'Edit Customer', parent: { path: '/customers', name: 'Customers' } },
    },
    {
      path: '/customers/:id',
      name: 'CustomerDetail',
      component: CustomerDetail,
      meta: { requiresAuth: true, breadcrumb: 'Customer Detail', title: 'Customer Detail', parent: { path: '/customers', name: 'Customers' } },
    },

    // ── Analytics ───────────────────────────────────────────────────────
    {
      path: '/analytics',
      name: 'Analytics',
      component: AnalyticsDashboard,
      meta: { requiresAuth: true, breadcrumb: 'Analytics', title: 'Analytics' },
    },

    // ── Categories ──────────────────────────────────────────────────────
    {
      path: '/categories',
      name: 'Categories',
      component: CategoryList,
      meta: { requiresAuth: true, breadcrumb: 'Categories', title: 'Categories' },
    },
    {
      path: '/categories/create',
      name: 'CreateCategory',
      component: CategoryForm,
      meta: { requiresAuth: true, breadcrumb: 'Create Category', title: 'New Category', parent: { path: '/categories', name: 'Categories' } },
    },
    {
      path: '/categories/edit/:id',
      name: 'EditCategory',
      component: CategoryForm,
      meta: { requiresAuth: true, breadcrumb: 'Edit Category', title: 'Edit Category', parent: { path: '/categories', name: 'Categories' } },
    },

    // ── Discounts ───────────────────────────────────────────────────────
    {
      path: '/discounts',
      name: 'Discounts',
      component: DiscountList,
      meta: { requiresAuth: true, breadcrumb: 'Discounts', title: 'Discounts' },
    },
    {
      path: '/discounts/create',
      name: 'CreateDiscount',
      component: DiscountForm,
      meta: { requiresAuth: true, breadcrumb: 'New Discount', title: 'New Discount', parent: { path: '/discounts', name: 'Discounts' } },
    },
    {
      path: '/discounts/:id/edit',
      name: 'EditDiscount',
      component: DiscountForm,
      meta: { requiresAuth: true, breadcrumb: 'Edit Discount', title: 'Edit Discount', parent: { path: '/discounts', name: 'Discounts' } },
    },

    // ── Other commerce ──────────────────────────────────────────────────
    {
      path: '/notifications',
      name: 'Notifications',
      component: NotificationList,
      meta: { requiresAuth: true, breadcrumb: 'Notifications', title: 'Notifications' },
    },
    {
      path: '/inventory/alerts',
      name: 'InventoryAlerts',
      component: InventoryAlerts,
      meta: { requiresAuth: true, breadcrumb: 'Inventory Alerts', title: 'Inventory Alerts', parent: { path: '/products', name: 'Products' } },
    },
    {
      path: '/returns',
      name: 'Returns',
      component: ReturnList,
      meta: { requiresAuth: true, breadcrumb: 'Returns & Refunds', title: 'Returns & Refunds' },
    },
    {
      path: '/segments',
      name: 'Segments',
      component: SegmentList,
      meta: { requiresAuth: true, breadcrumb: 'Customer Segments', title: 'Customer Segments' },
    },
    {
      path: '/audit-log',
      name: 'AuditLog',
      component: AuditLog,
      meta: { requiresAuth: true, breadcrumb: 'Audit Log', title: 'Audit Log' },
    },

    // ── Settings ────────────────────────────────────────────────────────
    {
      path: '/settings',
      name: 'Settings',
      component: Settings,
      meta: { requiresAuth: true, breadcrumb: 'Settings', title: 'Settings' },
    },
    {
      path: '/settings/profile',
      name: 'UserProfile',
      component: UserProfile,
      meta: { requiresAuth: true, breadcrumb: 'User Profile', title: 'User Profile', parent: { path: '/settings', name: 'Settings' } },
    },

    // ── 404 catch-all ───────────────────────────────────────────────────
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: NotFound,
      meta: { title: '404 Not Found' },
    },
  ],
});

// ── Auth guard ──────────────────────────────────────────────────────────────
router.beforeEach((to, _from, next) => {
  const isAuthenticated = !!localStorage.getItem('auth_token');

  if (to.meta.requiresAuth && !isAuthenticated) {
    // Redirect to login, preserve intended destination
    next({ name: 'Login', query: { redirect: to.fullPath } });
  } else if (to.meta.public && isAuthenticated) {
    // Already logged in — skip login page
    next({ name: 'Dashboard' });
  } else {
    next();
  }
});

// ── Document title ──────────────────────────────────────────────────────────
router.afterEach((to) => {
  const title = to.meta.title as string | undefined;
  document.title = title ? `${title} — ShopAdmin` : 'ShopAdmin';
});

export default router;
