// Matches CustomerResponse.java
export interface Customer {
  id: number;
  fullName: string;        // note: backend uses fullName, not name
  email: string;
  phone?: string;
  avatarUrl?: string;
  orderCount: number;
  totalSpent: number;
  lastOrderDate?: string;  // ISO-8601 string
  status: string;          // "active" | "inactive"
  type: string;            // "vip" | "returning" | "new"
  createdAt?: string;      // ISO-8601 string
}
