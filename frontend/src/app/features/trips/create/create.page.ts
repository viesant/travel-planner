import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TripService } from '../services/trip.service';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { TripRequest } from '../models/trip-request.model';
import { Trip } from '../../../shared/models/trip.model';
import { HttpErrorResponse } from '@angular/common/http';
import { ProblemDetails } from '../../../shared/models/problem-details.model';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';

@Component({
  selector: 'app-create',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatDatepickerModule,
    RouterLink,
  ],
  templateUrl: './create.page.html',
  styleUrl: './create.page.scss',
})
export class CreatePage {
  private readonly router = inject(Router);
  private readonly tripService = inject(TripService);
  private readonly fb = inject(FormBuilder);

  readonly errorMessage = signal<string | null>(null);

  readonly createForm = this.fb.nonNullable.group({
    title: ['', [Validators.required]],
    description: [''],
    startDate: ['', [Validators.required]],
    endDate: ['', [Validators.required]],
  });

  onSubmit(): void {
    if (this.createForm.invalid) {
      return;
    }

    this.errorMessage.set(null);

    const tripData: TripRequest = this.createForm.getRawValue();

    this.tripService.create(tripData).subscribe({
      next: (trip: Trip) => {
        console.log(trip);

        this.router.navigate(['trips', trip.id]);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Trip creation failed:', error);

        const problem: ProblemDetails = error.error;
        this.errorMessage.set(
          problem?.detail || 'Failed to create new trip. Please check your data',
        );
      },
    });
  }
}
