import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '@/views/Dashboard.vue';
import ProductList from '@/views/products/ProductList.vue';
import ProductForm from '@/views/products/ProductForm.vue';
import OrderList from '@/views/orders/OrderList.vue';
import OrderEdit from '@/views/orders/OrderEdit.vue';
import CustomerList from '@/views/customers/CustomerList.vue';
import CustomerEdit from '@/views/customers/CustomerEdit.vue';
import AnalyticsDashboard from '@/views/analytics/AnalyticsDashboard.vue';
import Settings from '@/views/settings/GeneralSettings.vue';
import OrderDetail from '@/views/orders/OrderDetail.vue';
import ProductDetail from '@/views/products/ProductDetail.vue';
import CategoryList from '@/views/categories/CategoryList.vue';
import UserProfile from '@/views/settings/UserProfile.vue';
import CustomerDetail from '@/views/customers/CustomerDetail.vue';
import CategoryForm from '@/views/categories/CategoryForm.vue';
import NotificationList from '@/views/notifications/NotificationList.vue';
import AuditLog from '@/views/audit/AuditLog.vue';

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
      meta: {
        requiresAuth: false,
        breadcrumb: 'Dashboard'
      }
    },
    {
      path: '/products',
      name: 'Products',
      component: ProductList,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Products'
      }
    },
    {
      path: '/products/create',
      name: 'CreateProduct',
      component: ProductForm,
      meta: {
        requiresAuth: false,
        breadcrumb: 'New',
        parent: {
          path: '/products',
          name: 'Products'
        }
      }
    },
    {
      path: '/products/:id',
      name: 'ProductDetail',
      component: ProductDetail,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Product Detail',
        parent: {
          path: '/products',
          name: 'Products'
        }
      }
    },
    {
      path: '/products/:id/edit',
      name: 'EditProduct',
      component: ProductForm,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Edit Product',
        parent: {
          path: '/products/:id',
          name: 'ProductDetail'
        }
      }
    },
    {
      path: '/orders',
      name: 'Orders',
      component: OrderList,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Orders'
      }
    },
    {
      path: '/orders/edit/:id',
      name: 'EditOrder',
      component: OrderEdit,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Edit Order',
        parent: { path: '/orders', name: 'Orders' }
      }
    },
    {
      path: '/customers',
      name: 'Customers',
      component: CustomerList,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Customers'
      }
    },
    {
      path: '/customers/edit/:id',
      name: 'EditCustomer',
      component: CustomerEdit,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Edit Customer',
        parent: { path: '/customers', name: 'Customers' }
      }
    },
    {
      path: '/customers/:id',
      name: 'CustomerDetail',
      component: CustomerDetail,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Customer Detail',
        parent: {
          path: '/customers',
          name: 'Customer'
        }
      }
    },
    {
      path: '/analytics',
      name: 'Analytics',
      component: AnalyticsDashboard,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Analytics'
      }
    },
    {
      path: '/settings',
      name: 'Settings',
      component: Settings,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Settings',
      }
    },
    {
      path: '/settings/profile',
      name: 'UserProfile',
      component: UserProfile,
      meta: {
        requiresAuth: false,
        breadcrumb: 'User Profile',
        parent: {
          path: '/settings',
          name: 'Settings'
        }
      }
    },
    {
      path: '/order/:orderId',
      name: 'OrderDetails',
      component: OrderDetail,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Order Details',
        parent: {
          path: '/orders',
          name: 'Orders'
        }
      }
    },
    {
      path: '/categories',
      name: 'Categories',
      component: CategoryList,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Categories'
      }
    },
    {
        path: '/categories/create',
        name: 'CreateCategory',
        component: CategoryForm,
        meta: {
            requiresAuth: false,
            breadcrumb: 'Create Category',
            parent: { path: '/categories', name: 'Categories' }
        }
    },
    {
        path: '/categories/edit/:id',
        name: 'EditCategory',
        component: CategoryForm,
        meta: {
            requiresAuth: false,
            breadcrumb: 'Edit Category',
            parent: { path: '/categories', name: 'Categories' }
        }
    },
    {
      path: '/notifications',
      name: 'Notifications',
      component: NotificationList,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Notifications',
      }
    },
    {
      path: '/audit-log',
      name: 'AuditLog',
      component: AuditLog,
      meta: {
        requiresAuth: false,
        breadcrumb: 'Audit Log',
      }
    },
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
