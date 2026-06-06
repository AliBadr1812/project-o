// src/types/order.ts
export interface OrderItem {
    id?: number;
    name: string;
    sku: string;
    price: number;
    quantity: number;
    imageUrl?: string;
    total?: number;
}

export interface ShippingAddress {
    name: string;
    street: string;
    city: string;
    state: string;
    zipCode: string;
    country: string;
    phone: string;
}

export interface OrderNote {
    id?: number;
    author: string;
    content: string;
    createdAt?: string;
}

export interface Order {
    id?: number;
    orderNumber: string;
    status: 'pending' | 'processing' | 'shipped' | 'delivered' | 'cancelled' | 'refunded';
    createdAt?: string;
    processedAt?: string | null;
    shippedAt?: string | null;
    deliveredAt?: string | null;

    // Customer info
    customerName: string;
    customerEmail: string;
    customerPhone?: string;

    // Order details
    items: OrderItem[];
    shippingAddress: ShippingAddress;

    // Financials
    subtotal: number;
    shippingCost: number;
    tax: number;
    total: number;
    discount?: number;
    paymentMethod: string;

    // Additional data
    notes?: OrderNote[];
}

export interface OrderStats {
    total: number;
    pending: number;
    processing: number;
    shipped: number;
    delivered: number;
    cancelled: number;
    totalRevenue: number;
}

export interface ApiResponse<T> {
    data: T;
    message?: string;
    status: number;
}
