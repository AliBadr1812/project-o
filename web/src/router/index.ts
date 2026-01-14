import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '@/views/Dashboard.vue';
import ProductList from '@/views/products/ProductList.vue';
import ProductForm from '@/views/products/ProductForm.vue';
import OrderList from '@/views/orders/OrderList.vue';
import CustomerList from '@/views/customers/CustomerList.vue';
import AnalyticsDashboard from '@/views/analytics/AnalyticsDashboard.vue';
import Settings from '@/views/settings/GeneralSettings.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
    path: '/',
    redirect: '/dashboard'
  },
  // {
  //   path: '/login',
  //   name: 'Login',
  //   component: Login,
  //   meta: { public: true }
  // },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { requiresAuth: false }
  },
  {
    path: '/products',
    name: 'Products',
    component: ProductList,
    meta: { requiresAuth: false }
  },
  {
    path: '/products/new',
    name: 'NewProduct',
    component: ProductForm,
    meta: { requiresAuth: false }
  },
  {
    path: '/products/:id/edit',
    name: 'EditProduct',
    component: ProductForm,
    meta: { requiresAuth: false }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: OrderList,
    meta: { requiresAuth: false }
  },
  {
    path: '/customers',
    name: 'Customers',
    component: CustomerList,
    meta: { requiresAuth: false }
  },
  {
    path: '/analytics',
    name: 'Analytics',
    component: AnalyticsDashboard,
    meta: { requiresAuth: false }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: Settings,
    meta: { requiresAuth: false }
  }
  ],
})

// Auth guard
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token');

  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login');
  } else if (to.meta.public && isAuthenticated) {
    next('/dashboard');
  } else {
    next();
  }
});

export default router
