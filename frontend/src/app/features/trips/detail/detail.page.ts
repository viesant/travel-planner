import { Component, inject, input, numberAttribute, OnInit, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TripService } from '../services/trip.service';
import { Trip } from '../../../shared/models/trip.model';
import { HttpErrorResponse } from '@angular/common/http';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-detail',
  imports: [MatCardModule, MatProgressSpinnerModule, MatButtonModule, RouterLink, DatePipe],
  templateUrl: './detail.page.html',
  styleUrl: './detail.page.scss',
})
export class DetailPage implements OnInit {
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
}
