import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Trip } from '../../../shared/models/trip.model';

@Injectable({
  providedIn: 'root',
})
export class TripsService {
  private readonly http = inject(HttpClient);
  private readonly BASE_URL = 'http://localhost:8081/trips';

  findAll(): Observable<Trip[]> {
    return this.http.get<Trip[]>(this.BASE_URL);
  }
}
