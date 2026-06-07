import api from './api';
import type { Return } from '@/types/return';

export class ReturnService {
  private readonly base = '/returns';

  async getAllReturns(): Promise<Return[]> {
    const { data } = await api.get<Return[]>(this.base);
    return data;
  }

  async getReturnById(id: number): Promise<Return> {
    const { data } = await api.get<Return>(`${this.base}/${id}`);
    return data;
  }

  async createReturn(payload: Partial<Return>): Promise<Return> {
    const { data } = await api.post<Return>(this.base, payload);
    return data;
  }

  async updateReturn(id: number, payload: Partial<Return>): Promise<Return> {
    const { data } = await api.put<Return>(`${this.base}/${id}`, payload);
    return data;
  }

  async deleteReturn(id: number): Promise<void> {
    await api.delete(`${this.base}/${id}`);
  }
}

export const returnService = new ReturnService();
