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
  // Detail-view fields (enriched / client-side)
  addresses?: CustomerAddress[];
  notes?:     CustomerNote[];
  tags?:      string[];
}

export interface CustomerAddress {
  id:       number;
  label:    string;         // e.g. "Home", "Work"
  street:   string;
  city:     string;
  state?:   string;
  zip?:     string;
  country:  string;
  isDefault?: boolean;
}

export interface CustomerNote {
  id:        number;
  text:      string;
  createdAt: string;
  author?:   string;
}
