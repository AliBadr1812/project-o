// src/utils/constants.ts

// API endpoints
export const API_ENDPOINTS = {
  PRODUCTS: '/api/products',
  ORDERS: '/api/orders',
  CUSTOMERS: '/api/customers',
  CATEGORIES: '/api/categories',
  AUTH: '/api/auth',
  UPLOAD: '/api/upload',
} as const;

// Application statuses
export const STATUS = {
  ACTIVE: 'active',
  INACTIVE: 'inactive',
  DRAFT: 'draft',
  PENDING: 'pending',
  COMPLETED: 'completed',
  CANCELLED: 'cancelled',
  SHIPPED: 'shipped',
} as const;

// Order statuses with colors
export const ORDER_STATUS = {
  PENDING: { label: 'Pending', color: 'warning' },
  PROCESSING: { label: 'Processing', color: 'info' },
  SHIPPED: { label: 'Shipped', color: 'success' },
  DELIVERED: { label: 'Delivered', color: 'success' },
  CANCELLED: { label: 'Cancelled', color: 'danger' },
  REFUNDED: { label: 'Refunded', color: 'secondary' },
} as const;

// Product statuses
export const PRODUCT_STATUS = {
  IN_STOCK: 'in_stock',
  OUT_OF_STOCK: 'out_of_stock',
  LOW_STOCK: 'low_stock',
  DISCONTINUED: 'discontinued',
} as const;

// Pagination defaults
export const PAGINATION_DEFAULTS = {
  PAGE_SIZE: 10,
  PAGE_SIZES: [10, 25, 50, 100],
  CURRENT_PAGE: 1,
} as const;

// Validation rules
export const VALIDATION_RULES = {
  EMAIL: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  PHONE: /^[\+]?[1-9][\d]{0,15}$/,
  URL: /^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})([/\w .-]*)*\/?$/,
  PRICE: /^\d+(\.\d{1,2})?$/,
} as const;

// Currency settings
export const CURRENCY = {
  DEFAULT: 'USD',
  SYMBOL: '$',
  LOCALE: 'en-US',
} as const;

// Date formats
export const DATE_FORMATS = {
  DISPLAY: 'MMM DD, YYYY',
  API: 'YYYY-MM-DD',
  DATETIME: 'MMM DD, YYYY HH:mm',
} as const;

// Local storage keys
export const STORAGE_KEYS = {
  AUTH_TOKEN: 'auth_token',
  USER_DATA: 'user_data',
  THEME: 'theme',
  LANGUAGE: 'language',
} as const;

// Types for navigation items
export interface NavigationItem {
  name: string;
  path: string;
  icon: string;
  badge?: {
    text: string;
    variant: 'info' | 'success' | 'warning' | 'danger';
  };
}

// Navigation items
export const NAVIGATION_ITEMS: NavigationItem[] = [
  { name: 'Dashboard', path: '/dashboard', icon: 'fas fa-chart-bar' },
  { name: 'Products', path: '/products', icon: 'fas fa-box' },
  { name: 'Orders', path: '/orders', icon: 'fas fa-shopping-cart' },
  { name: 'Customers', path: '/customers', icon: 'fas fa-users' },
  { name: 'Categories', path: '/categories', icon: 'fas fa-tags' },
  { name: 'Analytics', path: '/analytics', icon: 'fas fa-chart-line' },
  { name: 'Settings', path: '/settings', icon: 'fas fa-cog' },
];

// Types for status values
export type StatusType = typeof STATUS[keyof typeof STATUS];
export type OrderStatusType = keyof typeof ORDER_STATUS;
export type ProductStatusType = typeof PRODUCT_STATUS[keyof typeof PRODUCT_STATUS];

// API response types
export interface ApiResponse<T = unknown> {
  success: boolean;
  data?: T;
  message?: string;
  error?: string;
  meta?: {
    total: number;
    page: number;
    limit: number;
    pages: number;
  };
}

// Pagination params
export interface PaginationParams {
  page?: number;
  limit?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
  search?: string;
  [key: string]: any;
}

// Breadcrumb item type
export interface BreadcrumbItem {
  path: string;
  name: string;
  disabled?: boolean;
}
