import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Activity } from '../../../shared/models/activity';
import { ActivityRequest } from '../models/activity-request';

@Injectable({
  providedIn: 'root',
})
export class ActivityService {
  private readonly http = inject(HttpClient);
  private readonly BASE_URL = 'http://localhost:8081/trips';

  create(tripId: number, data: ActivityRequest): Observable<Activity> {
    return this.http.post<Activity>(`${this.BASE_URL}/${tripId}/activities`, data);
  }

  findAllByTripId(tripId: number): Observable<Activity[]> {
    return this.http.get<Activity[]>(`${this.BASE_URL}/${tripId}/activities`);
  }

  findById(tripId: number, id: number): Observable<Activity> {
    return this.http.get<Activity>(`${this.BASE_URL}/${tripId}/activities/${id}`);
  }

  update(tripId: number, id: number, data: ActivityRequest): Observable<Activity> {
    return this.http.put<Activity>(`${this.BASE_URL}/${tripId}/activities/${id}`, data);
  }

  delete(tripId: number, id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_URL}/${tripId}/activities/${id}`);
  }
}
