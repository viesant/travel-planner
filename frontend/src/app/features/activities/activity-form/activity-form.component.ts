import { DatePipe } from '@angular/common';
import { Component, computed, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTimepickerModule } from '@angular/material/timepicker';
import { ActivityService } from '../services/activity.service';
import { Router } from '@angular/router';
import { Activity } from '../models/activity';
import { ActivityRequest } from '../models/activity-request';
import { HttpErrorResponse } from '@angular/common/http';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { parseAndValidateId } from '../../../shared/utils/number.utils';
import { mergeDateAndTime } from '../../../shared/utils/date.util';

@Component({
  selector: 'app-activity-form',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatDatepickerModule,
    MatSelectModule,
    MatTimepickerModule,
  ],
  templateUrl: './activity-form.component.html',
  styleUrl: './activity-form.component.scss',
})
export class ActivityFormComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly activityService = inject(ActivityService);
  private readonly fb = inject(FormBuilder);

  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly tripId = input.required<number>();
  readonly activityToEdit = input<Activity | undefined>(undefined);
  readonly isEditMode = computed<boolean>(() => !!this.activityToEdit());

  readonly formClosed = output<void>();

  readonly activityForm = this.fb.nonNullable.group({
    name: ['', [Validators.required]],
    location: [''],
    address: [''],
    startDate: ['', [Validators.required]],
    startTime: ['', [Validators.required]],
    endDate: ['', [Validators.required]],
    endTime: ['', [Validators.required]],
    bookingNumber: [''],
    price: ['', [Validators.min(0)]],
    notes: [''],
  });

  ngOnInit(): void {
    if (!parseAndValidateId(this.tripId())) {
      console.warn('Invalid Trip ID on form initialization. Redirecting.');
      this.router.navigate(['/trips']);
      this.formClosed.emit();
      return;
    }

    if (this.isEditMode()) {
      this.loadActivityIntoForm();
    }
  }

  private loadActivityIntoForm(): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    const target = this.activityToEdit();

    if (!target) {
      return;
    }

    this.activityForm.patchValue({
      name: target.name,
      location: target.location,
      address: target.address,
      startDate: target.startDateTime,
      startTime: target.startDateTime,
      endDate: target.endDateTime,
      endTime: target.endDateTime,
      bookingNumber: target.bookingNumber,
      price: target.price as any,
      notes: target.notes,
    });
    this.isLoading.set(false);
  }

  onSubmit(): void {
    if (this.activityForm.invalid) {
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);
    const rawValue = this.activityForm.getRawValue();

    const requestData: ActivityRequest = {
      name: rawValue.name,
      location: rawValue.location,
      address: rawValue.address,
      startDateTime: mergeDateAndTime(rawValue.startDate, rawValue.startTime),
      endDateTime: mergeDateAndTime(rawValue.endDate, rawValue.endTime),
      bookingNumber: rawValue.bookingNumber,
      price: Number(rawValue.price),
      notes: rawValue.notes,
    };

    if (this.isEditMode()) {
      this.updateActivity(requestData);
    } else {
      this.createActivity(requestData);
    }
  }

  private createActivity(data: ActivityRequest): void {
    this.activityService.create(this.tripId(), data).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.formClosed.emit();
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to register activity.');
      },
    });
  }

  private updateActivity(data: ActivityRequest): void {
    const activityId = this.activityToEdit()?.id;
    if (!activityId) return;

    this.activityService.update(this.tripId(), activityId, data).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.formClosed.emit();
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to update activity.');
      },
    });
  }
}
