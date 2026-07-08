import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { Transport } from '../../../shared/models/transport';
import { TRANSPORT_TYPES, TransportType } from '../../../shared/models/transport-type';
import { TransportRequest } from '../models/transport-request';
import { TransportService } from '../services/transport.service';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTimepickerModule } from '@angular/material/timepicker';

@Component({
  selector: 'app-transport-form',
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
  templateUrl: './transport-form.component.html',
  styleUrl: './transport-form.component.scss',
})
export class TransportFormComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly transportService = inject(TransportService);
  private readonly fb = inject(FormBuilder);

  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly tripId = input.required<number>();
  readonly transportToEdit = input<Transport | undefined>(undefined);
  readonly isEditMode = computed<boolean>(() => !!this.transportToEdit());

  readonly formClosed = output<void>();

  readonly transportTypes = TRANSPORT_TYPES;

  readonly transportForm = this.fb.nonNullable.group({
    type: [TRANSPORT_TYPES[0] as TransportType, [Validators.required]],
    carrier: [''],
    departureLocation: ['', [Validators.required]],
    departureAddress: [''],
    arrivalLocation: ['', [Validators.required]],
    arrivalAddress: [''],
    departureDateTime: ['', [Validators.required]],
    arrivalDateTime: ['', [Validators.required]],
    bookingNumber: [''],
    price: ['', [Validators.min(0)]],
    notes: [''],
  });

  ngOnInit(): void {
    if (!this.parseAndValidateId(this.tripId())) {
      console.warn('Invalid Trip ID on form initialization. Redirecting.');
      this.router.navigate(['/trips']);
      this.formClosed.emit();
      return;
    }

    if (this.isEditMode()) {
      this.loadTransportIntoForm();
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

  private loadTransportIntoForm(): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    const target = this.transportToEdit();

    if (!target) {
      return;
    }

    this.transportForm.patchValue({
      type: target.type,
      carrier: target.carrier,
      departureLocation: target.departureLocation,
      departureAddress: target.departureAddress,
      arrivalLocation: target.arrivalLocation,
      arrivalAddress: target.arrivalAddress,
      departureDateTime: target.departureDateTime,
      arrivalDateTime: target.arrivalDateTime,
      bookingNumber: target.bookingNumber,
      price: target.price as any,
      notes: target.notes,
    });
    this.isLoading.set(false);
  }

  onSubmit(): void {
    if (this.transportForm.invalid) {
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);
    const rawValue = this.transportForm.getRawValue();

    const requestData: TransportRequest = {
      type: rawValue.type,
      carrier: rawValue.carrier,
      departureLocation: rawValue.departureLocation,
      departureAddress: rawValue.departureAddress,
      arrivalLocation: rawValue.arrivalLocation,
      arrivalAddress: rawValue.arrivalAddress,
      departureDateTime: rawValue.departureDateTime,
      arrivalDateTime: rawValue.arrivalDateTime,
      bookingNumber: rawValue.bookingNumber,
      price: Number(rawValue.price),
      notes: rawValue.notes,
    };

    if (this.isEditMode()) {
      this.updateTransport(requestData);
    } else {
      this.createTransport(requestData);
    }
  }

  private createTransport(data: TransportRequest): void {
    this.transportService.create(this.tripId(), data).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.formClosed.emit();
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to register transport ticket.');
      },
    });
  }

  private updateTransport(data: TransportRequest): void {
    const transportId = this.transportToEdit()?.id;
    if (!transportId) return;

    this.transportService.update(this.tripId(), transportId, data).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.formClosed.emit();
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to update transport ticket.');
      },
    });
  }
}
