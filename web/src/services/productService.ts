import api from './api';
import type { Product } from '@/types/product';

export class ProductService {
  private readonly base = '/products';

  async getAllProducts(): Promise<Product[]> {
    const { data } = await api.get<Product[]>(this.base);
    return data;
  }

  async getProductById(id: number): Promise<Product> {
    const { data } = await api.get<Product>(`${this.base}/${id}`);
    return data;
  }

  async createProduct(payload: Partial<Product>): Promise<Product> {
    const { data } = await api.post<Product>(`${this.base}/create`, payload);
    return data;
  }

  async updateProduct(id: number, payload: Partial<Product>): Promise<Product> {
    const { data } = await api.put<Product>(`${this.base}/${id}`, payload);
    return data;
  }

  async deleteProduct(id: number): Promise<void> {
    await api.delete(`${this.base}/${id}`);
  }

  /** Client-side export — no backend call needed */
  exportProducts(products: Product[]): void {
    const json = JSON.stringify(products, null, 2);
    const uri  = 'data:application/json;charset=utf-8,' + encodeURIComponent(json);
    const a    = document.createElement('a');
    a.setAttribute('href', uri);
    a.setAttribute('download', `products_export_${new Date().toISOString().split('T')[0]}.json`);
    a.click();
  }
}

export const productService = new ProductService();
