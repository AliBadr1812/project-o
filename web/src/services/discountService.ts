import api from './api';
import type { Discount } from '@/types/discount';

export class DiscountService {
  private readonly base = '/discounts';

  async getAllDiscounts(): Promise<Discount[]> {
    const { data } = await api.get<Discount[]>(this.base);
    return data;
  }

  async getDiscountById(id: number): Promise<Discount> {
    const { data } = await api.get<Discount>(`${this.base}/${id}`);
    return data;
  }

  async validateCode(code: string): Promise<Discount> {
    const { data } = await api.get<Discount>(`${this.base}/validate/${code}`);
    return data;
  }

  async createDiscount(payload: Partial<Discount>): Promise<Discount> {
    const { data } = await api.post<Discount>(this.base, payload);
    return data;
  }

  async updateDiscount(id: number, payload: Partial<Discount>): Promise<Discount> {
    const { data } = await api.put<Discount>(`${this.base}/${id}`, payload);
    return data;
  }

  async deleteDiscount(id: number): Promise<void> {
    await api.delete(`${this.base}/${id}`);
  }
}

export const discountService = new DiscountService();
