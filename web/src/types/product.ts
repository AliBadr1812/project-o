// Matches ProductResponse.java
export interface Product {
  id: number;
  name: string;
  sku: string;
  status: string;          // "active" | "inactive" | "draft" | "archived"
  description?: string;
  images?: string[];       // first element used as primary image
  price: number;
  cost?: number;
  compareAtPrice?: number;
  stock: number;
  lowStockThreshold?: number;
  trackInventory?: boolean;
  inStock?: boolean;
  categories?: string;     // category name string
  isVisible?: boolean;
  isFeatured?: boolean;
  createdAt?: string;
  updatedAt?: string;
  averageRating?: number;
  reviewCount?: number;
  weight?: number;
  weightUnit?: string;
  dimensions?: string;
}

export interface ProductReview {
  id:        number;
  author:    string;
  rating:    number;
  date:      string;
  comment:   string;
}

export interface ProductStats {
  total: number;
  inStock: number;
  lowStock: number;
  outOfStock: number;
}
