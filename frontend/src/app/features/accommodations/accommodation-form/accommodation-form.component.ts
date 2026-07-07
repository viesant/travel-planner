import { Component, computed, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { AccommodationService } from '../services/accommodation.service';
import { Accommodation } from '../../../shared/models/accommodation';
import { HttpErrorResponse } from '@angular/common/http';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { AccommodationRequest } from '../models/accommodation-request';

@Component({
  selector: 'app-accommodation-form',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatDatepickerModule,
    RouterLink,
  ],
  templateUrl: './accommodation-form.component.html',
  styleUrl: './accommodation-form.component.scss',
})
export class AccommodationFormComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly accommodationService = inject(AccommodationService);
  private readonly fb = inject(FormBuilder);

  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly tripId = input.required<number>();
  readonly accommodationToEdit = input<Accommodation | undefined>(undefined);
  readonly isEditMode = computed<boolean>(() => !!this.accommodationToEdit());

  readonly formClosed = output<void>();

  readonly accommodationForm = this.fb.nonNullable.group({
    name: ['', [Validators.required]],
    location: [''],
    address: [''],
    bookingNumber: [''],
    checkInDate: ['', [Validators.required]],
    checkOutDate: ['', [Validators.required]],
    price: [0, [Validators.min(0)]],
    notes: [''],
  });

  ngOnInit(): void {
    if (!this.parseAndValidateId(this.tripId())) {
      console.warn('Invalid Trip ID on form initialization. Redirecting.');
      this.router.navigate(['/trips']);
      return;
    }
    if (this.isEditMode()) {
      this.loadAccommodationIntoForm();
    }
  }

  private parseAndValidateId(id: any): number | null {
    if (id === null || id === undefined) {
      return null;
    }

    const numericId = Number(id);
    if (!Number.isInteger(numericId) || numericId <= 0) {
      return null;
    }

    return numericId;
  }

  private loadAccommodationIntoForm(): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    const target = this.accommodationToEdit();

    if (!target) {
      return;
    }

    console.log(target);

    this.accommodationForm.patchValue({
      name: target.name,
      location: target.location,
      address: target.address,
      bookingNumber: target.bookingNumber,
      checkInDate: target.checkInDate,
      checkOutDate: target.checkOutDate,
      price: target.price,
      notes: target.notes,
    });
    this.isLoading.set(false);
  }

  onSubmit(): void {
    if (this.accommodationForm.invalid) {
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);

    const accommodationData: AccommodationRequest = this.accommodationForm.getRawValue();

    if (this.isEditMode()) {
      this.updateAccommodation(accommodationData);
    } else {
      this.createAccommodation(accommodationData);
    }
  }

  private updateAccommodation(data: AccommodationRequest): void {
    const accommodationId = this.accommodationToEdit()?.id;
    if (!accommodationId) return;

    this.accommodationService.update(this.tripId(), accommodationId, data).subscribe({
      next: (res: Accommodation) => {
        this.isLoading.set(false);
        this.handleSuccess();
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to update accommodation.');
      },
    });
  }

  private createAccommodation(data: AccommodationRequest): void {
    this.accommodationService.create(this.tripId(), data).subscribe({
      next: (res: Accommodation) => {
        this.isLoading.set(false);
        this.handleSuccess();
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to add accommodation.');
      },
    });
  }

  private handleSuccess() {
    this.formClosed.emit();
  }
}
