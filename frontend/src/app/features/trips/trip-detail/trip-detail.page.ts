import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, input, numberAttribute, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router, RouterLink } from '@angular/router';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { Trip } from '../models/trip';
import { TripService } from '../services/trip.service';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { AccommodationListComponent } from '../../accommodations/accommodation-list/accommodation-list.component';
import { TransportListComponent } from '../../transports/transport-list/transport-list.component';
import { ActivityListComponent } from '../../activities/activity-list/activity-list.component';

@Component({
  selector: 'app-trip-detail',
  imports: [
    MatCardModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    RouterLink,
    DatePipe,
    MatTabsModule,
    MatIconModule,
    AccommodationListComponent,
    TransportListComponent,
    ActivityListComponent,
  ],
  templateUrl: './trip-detail.page.html',
  styleUrl: './trip-detail.page.scss',
})
export class TripDetailPage implements OnInit {
  private readonly router = inject(Router);
  private readonly tripService = inject(TripService);

  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly trip = signal<Trip | null>(null);
  readonly id = input.required({ transform: numberAttribute });

  ngOnInit(): void {
    this.loadTripById();
  }

  loadTripById() {
    const numericId = Number(this.id());

    if (isNaN(numericId)) {
      console.warn(`Invalid ID detected: "${numericId}". Redirecting to safety.`);
      this.router.navigate(['/trips']);
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.tripService.findById(numericId).subscribe({
      next: (trip: Trip) => {
        this.trip.set(trip);
        console.log(this.trip());
        this.isLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to load trip');
        this.isLoading.set(false);
      },
    });
  }

  deleteTrip(itemId: number) {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.tripService.delete(itemId).subscribe({
      next: () => {
        this.router.navigate(['/trips']);
      },
      error: (error: HttpErrorResponse) => {
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to delete trip');
        this.isLoading.set(false);
      },
    });
  }
}
