// src/services/orderService.ts
import api from './api';
import type { Order, OrderStats, OrderNote } from '@/types/order';

export class OrderService {
    private readonly baseEndpoint = '/orders';

    async getAllOrders(): Promise<Order[]> {
        try {
            const { data } = await api.get<Order[]>(this.baseEndpoint);
            return data;
        } catch (error) {
            console.error('Error fetching orders:', error);
            throw error;
        }
    }

    async getOrderById(id: number): Promise<Order> {
        try {
            const { data } = await api.get<Order>(`${this.baseEndpoint}/${id}`);
            return data;
        } catch (error) {
            console.error(`Error fetching order ${id}:`, error);
            throw error;
        }
    }

    async getOrderByNumber(orderNumber: string): Promise<Order> {
        try {
            const { data } = await api.get<Order>(`${this.baseEndpoint}/number/${orderNumber}`);
            return data;
        } catch (error) {
            console.error(`Error fetching order ${orderNumber}:`, error);
            throw error;
        }
    }

    async getOrdersByStatus(status: string): Promise<Order[]> {
        try {
            const { data } = await api.get<Order[]>(`${this.baseEndpoint}/status/${status}`);
            return data;
        } catch (error) {
            console.error(`Error fetching orders with status ${status}:`, error);
            throw error;
        }
    }

    async createOrder(orderData: Partial<Order>): Promise<Order> {
        try {
            if (!orderData.total && orderData.items) {
                const subtotal = orderData.items.reduce(
                    (sum, item) => sum + item.price * item.quantity,
                    0
                );
                orderData.subtotal = subtotal;
                orderData.total = subtotal + (orderData.shippingCost || 0) + (orderData.tax || 0);
            }

            const { data } = await api.post<Order>(this.baseEndpoint, orderData);
            return data;
        } catch (error) {
            console.error('Error creating order:', error);
            throw error;
        }
    }

    async updateOrder(id: number, orderData: Partial<Order>): Promise<Order> {
        try {
            const { data } = await api.put<Order>(`${this.baseEndpoint}/${id}`, orderData);
            return data;
        } catch (error) {
            console.error(`Error updating order ${id}:`, error);
            throw error;
        }
    }

    async updateOrderStatus(id: number, status: Order['status']): Promise<Order> {
        try {
            const currentOrder = await this.getOrderById(id);

            const updatedOrder = {
                ...currentOrder,
                status,
                ...(status === 'processing' && { processedAt: new Date().toISOString() }),
                ...(status === 'shipped' && { shippedAt: new Date().toISOString() }),
                ...(status === 'delivered' && { deliveredAt: new Date().toISOString() }),
            };

            const { data } = await api.put<Order>(`${this.baseEndpoint}/${id}`, updatedOrder);
            return data;
        } catch (error) {
            console.error(`Error updating order status for ${id}:`, error);
            throw error;
        }
    }

    async deleteOrder(id: number): Promise<void> {
        try {
            await api.delete(`${this.baseEndpoint}/${id}`);
        } catch (error) {
            console.error(`Error deleting order ${id}:`, error);
            throw error;
        }
    }

    async addOrderNote(orderId: number, note: Partial<OrderNote>): Promise<Order> {
        try {
            const order = await this.getOrderById(orderId);

            const newNote: OrderNote = {
                id: Date.now(),
                author: note.author || 'Current User',
                content: note.content || '',
                createdAt: new Date().toISOString(),
            };

            const updatedNotes = [...(order.notes || []), newNote];
            return this.updateOrder(orderId, { notes: updatedNotes });
        } catch (error) {
            console.error(`Error adding note to order ${orderId}:`, error);
            throw error;
        }
    }

    async getOrderStats(): Promise<OrderStats> {
        try {
            const { data } = await api.get<OrderStats>(`${this.baseEndpoint}/stats`);
            return data;
        } catch (error) {
            console.error('Error fetching order stats:', error);
            throw error;
        }
    }

    async searchOrders(query: string): Promise<Order[]> {
        try {
            const allOrders = await this.getAllOrders();
            return allOrders.filter(
                (order) =>
                    order.orderNumber.toLowerCase().includes(query.toLowerCase()) ||
                    order.customerName.toLowerCase().includes(query.toLowerCase()) ||
                    order.customerEmail.toLowerCase().includes(query.toLowerCase())
            );
        } catch (error) {
            console.error('Error searching orders:', error);
            throw error;
        }
    }

    async exportOrders(orders: Order[]): Promise<void> {
        try {
            const dataStr = JSON.stringify(orders, null, 2);
            const dataUri = 'data:application/json;charset=utf-8,' + encodeURIComponent(dataStr);
            const exportFileDefaultName = `orders_export_${new Date().toISOString().split('T')[0]}.json`;

            const linkElement = document.createElement('a');
            linkElement.setAttribute('href', dataUri);
            linkElement.setAttribute('download', exportFileDefaultName);
            linkElement.click();
        } catch (error) {
            console.error('Error exporting orders:', error);
            throw error;
        }
    }

    async getOrdersByDateRange(startDate: string, endDate: string): Promise<Order[]> {
        try {
            const allOrders = await this.getAllOrders();
            const start = new Date(startDate).getTime();
            const end = new Date(endDate).getTime();
            return allOrders.filter((order) => {
                const orderDate = new Date(order.createdAt || '').getTime();
                return orderDate >= start && orderDate <= end;
            });
        } catch (error) {
            console.error('Error fetching orders by date range:', error);
            throw error;
        }
    }

    async getCustomerOrders(customerEmail: string): Promise<Order[]> {
        try {
            const { data } = await api.get<Order[]>(`${this.baseEndpoint}/customer/${customerEmail}`);
            return data;
        } catch (error) {
            console.error(`Error fetching orders for customer ${customerEmail}:`, error);
            throw error;
        }
    }
}

export const orderService = new OrderService();
