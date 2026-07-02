import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Router, RouterLink } from '@angular/router';
import { TripService } from '../services/trip.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Trip } from '../../../shared/models/trip.model';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-list',
  imports: [MatCardModule, MatProgressSpinnerModule, MatButtonModule, RouterLink, DatePipe],
  templateUrl: './list.page.html',
  styleUrl: './list.page.scss',
})
export class ListPage implements OnInit {
  private readonly router = inject(Router);
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

  loadTrips() {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.tripService.findAll().subscribe({
      next: (trips: Trip[]) => {
        this.rawTrips.set(trips);
        console.log(this.rawTrips());
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
