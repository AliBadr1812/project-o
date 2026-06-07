export interface ReturnItem {
  productId:   number;
  productName: string;
  sku:         string;
  quantity:    number;
  unitPrice:   number;
}

export interface Return {
  id:           number;
  returnNumber: string;
  orderId:      number;
  orderNumber:  string;
  customerId:   number;
  customerName: string;
  customerEmail:string;
  reason:       'DEFECTIVE' | 'WRONG_ITEM' | 'NOT_AS_DESCRIBED' | 'CHANGED_MIND' | 'OTHER';
  reasonDetail: string;
  /** PENDING | APPROVED | REJECTED | REFUNDED | IN_TRANSIT */
  status:       string;
  refundAmount: number;
  /** ORIGINAL_PAYMENT | STORE_CREDIT | BANK_TRANSFER */
  refundMethod: string;
  items:        ReturnItem[];
  createdAt:    string;
  updatedAt:    string;
  staffNotes:   string;
}
