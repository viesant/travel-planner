import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AccommodationRequest } from '../models/accommodation-request';
import { Observable } from 'rxjs';
import { Accommodation } from '../../../shared/models/accommodation';

@Injectable({
  providedIn: 'root',
})
export class AccommodationService {
  private readonly http = inject(HttpClient);
  private readonly BASE_URL = 'http://localhost:8081/trips';

  create(tripId: number, data: AccommodationRequest): Observable<Accommodation> {
    return this.http.post<Accommodation>(`${this.BASE_URL}/${tripId}/accommodations`, data);
  }

  findAllByTripId(tripId: number): Observable<Accommodation[]> {
    return this.http.get<Accommodation[]>(`${this.BASE_URL}/${tripId}/accommodations`);
  }

  findById(tripId: number, id: number): Observable<Accommodation> {
    return this.http.get<Accommodation>(`${this.BASE_URL}/${tripId}/accommodations/${id}`);
  }

  update(tripId: number, id: number, data: AccommodationRequest): Observable<Accommodation> {
    return this.http.put<Accommodation>(`${this.BASE_URL}/${tripId}/accommodations/${id}`, data);
  }

  delete(tripId: number, id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_URL}/${tripId}/accommodations/${id}`);
  }
}
