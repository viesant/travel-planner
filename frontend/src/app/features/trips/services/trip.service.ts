import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Trip } from '../../../shared/models/trip.model';
import { TripRequest } from '../models/trip-request.model';

@Injectable({
  providedIn: 'root',
})
export class TripService {
  private readonly http = inject(HttpClient);
  private readonly BASE_URL = 'http://localhost:8081/trips';

  findAll(): Observable<Trip[]> {
    return this.http.get<Trip[]>(this.BASE_URL);
  }

  create(tripData: TripRequest): Observable<Trip> {
    return this.http.post<Trip>(this.BASE_URL, tripData);
  }
}
