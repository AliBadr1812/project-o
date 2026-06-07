import api from './api';
import type { Segment } from '@/types/segment';

export class SegmentService {
  private readonly base = '/segments';

  async getAllSegments(): Promise<Segment[]> {
    const { data } = await api.get<Segment[]>(this.base);
    return data;
  }

  async getSegmentById(id: number): Promise<Segment> {
    const { data } = await api.get<Segment>(`${this.base}/${id}`);
    return data;
  }

  async createSegment(payload: Partial<Segment>): Promise<Segment> {
    const { data } = await api.post<Segment>(this.base, payload);
    return data;
  }

  async updateSegment(id: number, payload: Partial<Segment>): Promise<Segment> {
    const { data } = await api.put<Segment>(`${this.base}/${id}`, payload);
    return data;
  }

  async deleteSegment(id: number): Promise<void> {
    await api.delete(`${this.base}/${id}`);
  }
}

export const segmentService = new SegmentService();
