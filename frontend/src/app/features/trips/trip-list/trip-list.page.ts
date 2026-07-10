import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterLink } from '@angular/router';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { Trip } from '../models/trip';
import { TripService } from '../services/trip.service';

@Component({
  selector: 'app-trip-list',
  standalone: true,
  imports: [MatCardModule, MatProgressSpinnerModule, MatButtonModule, RouterLink, DatePipe],
  templateUrl: './trip-list.page.html',
  styleUrl: './trip-list.page.scss',
})
export class TripListPage implements OnInit {
  private readonly tripService = inject(TripService);

  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly rawTrips = signal<Trip[]>([]);
  readonly sortedTrips = computed(() => {
    return [...this.rawTrips()].sort((a, b) => {
      return new Date(a.startDate).getTime() - new Date(b.startDate).getTime();
    });
  });

  ngOnInit(): void {
    this.loadTrips();
  }

  loadTrips(): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.tripService.findAll().subscribe({
      next: (trips: Trip[]) => {
        this.rawTrips.set(trips);
        this.isLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to load trips');
        this.isLoading.set(false);
      },
    });
  }
}
