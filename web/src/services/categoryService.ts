import api from './api';
import type { Category } from '@/types/category';

export class CategoryService {
  private readonly base = '/categories';

  async getAllCategories(): Promise<Category[]> {
    const { data } = await api.get<Category[]>(this.base);
    return data;
  }

  async getCategoryById(id: number): Promise<Category> {
    const { data } = await api.get<Category>(`${this.base}/${id}`);
    return data;
  }

  async createCategory(payload: Partial<Category>): Promise<Category> {
    const { data } = await api.post<Category>(this.base, payload);
    return data;
  }

  async updateCategory(id: number, payload: Partial<Category>): Promise<Category> {
    const { data } = await api.put<Category>(`${this.base}/${id}`, payload);
    return data;
  }

  async deleteCategory(id: number): Promise<void> {
    await api.delete(`${this.base}/${id}`);
  }

  /** Client-side export */
  exportCategories(categories: Category[]): void {
    const json = JSON.stringify(categories, null, 2);
    const uri  = 'data:application/json;charset=utf-8,' + encodeURIComponent(json);
    const a    = document.createElement('a');
    a.setAttribute('href', uri);
    a.setAttribute('download', `categories_export_${new Date().toISOString().split('T')[0]}.json`);
    a.click();
  }
}

export const categoryService = new CategoryService();
