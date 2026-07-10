import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Transport } from '../models/transport';
import { TransportRequest } from '../models/transport-request';

@Injectable({
  providedIn: 'root',
})
export class TransportService {
  private readonly http = inject(HttpClient);
  private readonly BASE_URL = 'http://localhost:8081/trips';

  create(tripId: number, data: TransportRequest): Observable<Transport> {
    return this.http.post<Transport>(`${this.BASE_URL}/${tripId}/transports`, data);
  }

  findAllByTripId(tripId: number): Observable<Transport[]> {
    return this.http.get<Transport[]>(`${this.BASE_URL}/${tripId}/transports`);
  }

  findById(tripId: number, id: number): Observable<Transport> {
    return this.http.get<Transport>(`${this.BASE_URL}/${tripId}/transports/${id}`);
  }

  update(tripId: number, id: number, data: TransportRequest): Observable<Transport> {
    return this.http.put<Transport>(`${this.BASE_URL}/${tripId}/transports/${id}`, data);
  }

  delete(tripId: number, id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_URL}/${tripId}/transports/${id}`);
  }
}
