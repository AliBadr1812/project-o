// Matches CategoryResponse.java
export interface Category {
  id: number;
  name: string;
  description?: string;
  icon?: string;
  productCount?: number;
  isActive: boolean;       // note: backend uses isActive, not status string
  createdAt?: string;      // ISO-8601 string
}
