import api from './api';
import type { Customer } from '@/types/customer';

export class CustomerService {
  private readonly base = '/customers';

  async getAllCustomers(): Promise<Customer[]> {
    const { data } = await api.get<Customer[]>(this.base);
    return data;
  }

  async getCustomerById(id: number): Promise<Customer> {
    const { data } = await api.get<Customer>(`${this.base}/${id}`);
    return data;
  }

  async createCustomer(payload: Partial<Customer>): Promise<Customer> {
    const { data } = await api.post<Customer>(this.base, payload);
    return data;
  }

  async updateCustomer(id: number, payload: Partial<Customer>): Promise<Customer> {
    const { data } = await api.put<Customer>(`${this.base}/${id}`, payload);
    return data;
  }

  async deleteCustomer(id: number): Promise<void> {
    await api.delete(`${this.base}/${id}`);
  }

  /** Client-side export */
  exportCustomers(customers: Customer[]): void {
    const json = JSON.stringify(customers, null, 2);
    const uri  = 'data:application/json;charset=utf-8,' + encodeURIComponent(json);
    const a    = document.createElement('a');
    a.setAttribute('href', uri);
    a.setAttribute('download', `customers_export_${new Date().toISOString().split('T')[0]}.json`);
    a.click();
  }
}

export const customerService = new CustomerService();
