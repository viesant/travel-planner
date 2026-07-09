import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, input, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { formatToLocalDate } from '../../../shared/utils/date.util';
import { parseAndValidateId } from '../../../shared/utils/number.util';
import { Trip } from '../models/trip';
import { TripRequest } from '../models/trip-request';
import { TripService } from '../services/trip.service';

@Component({
  selector: 'app-trip-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatDatepickerModule,
    RouterLink,
  ],
  templateUrl: './trip-form.page.html',
  styleUrl: './trip-form.page.scss',
})
export class TripFormPage implements OnInit {
  private readonly router = inject(Router);
  private readonly tripService = inject(TripService);
  private readonly fb = inject(FormBuilder);

  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly id = input<string | undefined>();
  readonly isEditMode = computed<boolean>(() => !!this.id());

  readonly tripForm = this.fb.nonNullable.group({
    title: ['', [Validators.required]],
    description: [''],
    startDate: ['', [Validators.required]],
    endDate: ['', [Validators.required]],
  });

  ngOnInit(): void {
    if (this.isEditMode()) {
      this.loadTripIntoForm();
    }
  }

  private loadTripIntoForm(): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    const numericId = parseAndValidateId(this.id());

    if (!numericId) {
      console.warn(`Invalid ID detected: "${this.id()}". Redirecting to safety.`);
      this.router.navigate(['/trips']);
      return;
    }

    this.tripService.findById(numericId).subscribe({
      next: (trip: Trip) => {
        this.tripForm.patchValue({
          title: trip.title,
          description: trip.description,
          startDate: trip.startDate,
          endDate: trip.endDate,
        });
        this.isLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to load trip');
        this.isLoading.set(false);
        this.router.navigate(['/trips']);
      },
    });
  }

  onSubmit(): void {
    if (this.tripForm.invalid) {
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);

    const rawValue = this.tripForm.getRawValue();

    const tripData: TripRequest = {
      title: rawValue.title,
      description: rawValue.description,
      startDate: formatToLocalDate(rawValue.startDate),
      endDate: formatToLocalDate(rawValue.endDate),
    };

    if (this.isEditMode()) {
      this.updateTrip(tripData);
    } else {
      this.createTrip(tripData);
    }
  }

  updateTrip(tripData: TripRequest): void {
    const numericId = parseAndValidateId(this.id());
    if (!numericId) return;

    this.tripService.update(numericId, tripData).subscribe({
      next: (trip: Trip) => {
        this.router.navigate(['trips', trip.id]);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Trip update failed:', error);
        this.isLoading.set(false);
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to update trip. Please check your data');
      },
    });
  }

  createTrip(tripData: TripRequest): void {
    this.tripService.create(tripData).subscribe({
      next: (trip: Trip) => {
        this.router.navigate(['trips', trip.id]);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Trip creation failed:', error);
        this.isLoading.set(false);
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(
          problem?.detail || 'Failed to create new trip. Please check your data',
        );
      },
    });
  }
}
