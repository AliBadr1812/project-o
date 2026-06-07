export interface SegmentCriteria {
  type:  string;
  value: string;
  label: string;
}

export interface Segment {
  id:            number;
  name:          string;
  description:   string;
  color:         string;
  icon:          string;
  criteria:      SegmentCriteria[];
  customerCount: number;
  totalRevenue:  number;
  avgOrderValue: number;
  isSystem:      boolean;
  createdAt:     string;
  updatedAt:     string;
}
