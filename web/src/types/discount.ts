// src/types/discount.ts

export interface Discount {
  id:                  number;
  code:                string;
  description:         string;
  type:                'PERCENTAGE' | 'FIXED';
  value:               number;
  minimumOrderAmount:  number;
  usageLimit:          number | null;  // null = unlimited
  usedCount:           number;
  isActive:            boolean;
  startDate:           string;  // ISO-8601
  endDate:             string;  // ISO-8601
  createdAt:           string;
}
