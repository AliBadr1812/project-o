// src/utils/constants.ts

export const API_ENDPOINTS = {
  PRODUCTS:   '/api/products',
  ORDERS:     '/api/orders',
  CUSTOMERS:  '/api/customers',
  CATEGORIES: '/api/categories',
  AUTH:       '/api/auth',
  UPLOAD:     '/api/upload',
} as const;

export const STATUS = {
  ACTIVE:    'active',
  INACTIVE:  'inactive',
  DRAFT:     'draft',
  PENDING:   'pending',
  COMPLETED: 'completed',
  CANCELLED: 'cancelled',
  SHIPPED:   'shipped',
} as const;

export const ORDER_STATUS = {
  PENDING:    { label: 'Pending',    color: 'warning' },
  PROCESSING: { label: 'Processing', color: 'info'    },
  SHIPPED:    { label: 'Shipped',    color: 'primary'  },
  DELIVERED:  { label: 'Delivered',  color: 'success' },
  CANCELLED:  { label: 'Cancelled',  color: 'danger'  },
  REFUNDED:   { label: 'Refunded',   color: 'neutral' },
} as const;

export const PRODUCT_STATUS = {
  IN_STOCK:     'in_stock',
  OUT_OF_STOCK: 'out_of_stock',
  LOW_STOCK:    'low_stock',
  DISCONTINUED: 'discontinued',
} as const;

export const PAGINATION_DEFAULTS = {
  PAGE_SIZE:    10,
  PAGE_SIZES:   [10, 25, 50, 100],
  CURRENT_PAGE: 1,
} as const;

export const VALIDATION_RULES = {
  EMAIL: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  PHONE: /^[\+]?[1-9][\d]{0,15}$/,
  URL:   /^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})([/\w .-]*)*\/?$/,
  PRICE: /^\d+(\.\d{1,2})?$/,
} as const;

export const CURRENCY = {
  DEFAULT: 'USD',
  SYMBOL:  '$',
  LOCALE:  'en-US',
} as const;

export const DATE_FORMATS = {
  DISPLAY:  'MMM DD, YYYY',
  API:      'YYYY-MM-DD',
  DATETIME: 'MMM DD, YYYY HH:mm',
} as const;

export const STORAGE_KEYS = {
  AUTH_TOKEN: 'auth_token',
  USER_DATA:  'user_data',
  THEME:      'theme',
  LANGUAGE:   'language',
} as const;

export interface NavigationItem {
  name:      string;
  path:      string;
  icon:      string;
  iconColor: string;
  badge?: {
    text:    string;
    variant: 'info' | 'success' | 'warning' | 'danger';
  };
}

export const NAVIGATION_ITEMS: NavigationItem[] = [
  { name: 'Dashboard',  path: '/dashboard', icon: 'fas fa-gauge-high',  iconColor: 'ni-p' },
  { name: 'Products',   path: '/products',  icon: 'fas fa-box',          iconColor: 'ni-o' },
  { name: 'Orders',     path: '/orders',    icon: 'fas fa-bag-shopping', iconColor: 'ni-g' },
  { name: 'Customers',  path: '/customers', icon: 'fas fa-users',        iconColor: 'ni-t' },
  { name: 'Categories', path: '/categories',icon: 'fas fa-tag',          iconColor: 'ni-r' },
  { name: 'Inventory Alerts', path: '/inventory/alerts', icon: 'fas fa-triangle-exclamation', iconColor: 'ni-o' },
  { name: 'Analytics',  path: '/analytics', icon: 'fas fa-chart-line',   iconColor: 'ni-b' },
  { name: 'Discounts',  path: '/discounts', icon: 'fas fa-ticket',        iconColor: 'ni-p' },
  { name: 'Returns',    path: '/returns',   icon: 'fas fa-rotate-left',   iconColor: 'ni-r' },
  { name: 'Segments',   path: '/segments',  icon: 'fas fa-layer-group',   iconColor: 'ni-t' },
  { name: 'Settings',   path: '/settings',  icon: 'fas fa-gear',         iconColor: 'ni-s' },
];

export type StatusType        = typeof STATUS[keyof typeof STATUS];
export type OrderStatusType   = keyof typeof ORDER_STATUS;
export type ProductStatusType = typeof PRODUCT_STATUS[keyof typeof PRODUCT_STATUS];

export interface ApiResponse<T = unknown> {
  success: boolean;
  data?:   T;
  message?: string;
  error?:  string;
  meta?: {
    total: number;
    page:  number;
    limit: number;
    pages: number;
  };
}

export interface PaginationParams {
  page?:      number;
  limit?:     number;
  sortBy?:    string;
  sortOrder?: 'asc' | 'desc';
  search?:    string;
  [key: string]: any;
}

export interface BreadcrumbItem {
  path:      string;
  name:      string;
  disabled?: boolean;
}
