import { Component, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Router } from '@angular/router';
import { TripsService } from '../services/trips.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Trip } from '../../../shared/models/trip.model';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-list',
  imports: [MatCardModule, MatProgressSpinnerModule, MatButtonModule],
  templateUrl: './list.page.html',
  styleUrl: './list.page.scss',
})
export class ListPage {
  private readonly router = inject(Router);
  private readonly tripService = inject(TripsService);

  readonly trips = signal<Trip[]>([]);
  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    this.loadTrips();
  }

  loadTrips() {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.tripService.findAll().subscribe({
      next: (trips) => {
        this.trips.set(trips);
        console.log(this.trips());
        this.isLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage.set('Failed to load trips');
        this.isLoading.set(false);
      },
    });
  }

  openCreateTrip(): void {
    this.router.navigate(['/trips/new']);
  }

  openTripDetail(id: number): void {
    this.router.navigate(['/trips', id]);
  }
}
