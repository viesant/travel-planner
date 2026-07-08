import { CommonModule } from '@angular/common';
import { Component, computed, inject, input, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Transport } from '../../../shared/models/transport';
import { TransportService } from '../services/transport.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { TRANSPORT_TYPES, TransportType } from '../../../shared/models/transport-type';
import { TransportFormComponent } from '../transport-form/transport-form.component';

@Component({
  selector: 'app-transport-list',
  imports: [
    CommonModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatExpansionModule,
    TransportFormComponent,
  ],
  templateUrl: './transport-list.component.html',
  styleUrl: './transport-list.component.scss',
})
export class TransportListComponent implements OnInit {
  private readonly transportService = inject(TransportService);

  readonly tripId = input.required<number>();

  readonly rawTransports = signal<Transport[]>([]);
  readonly sortedTransports = computed(() => {
    return [...this.rawTransports()].sort((a, b) => {
      return new Date(a.departureDateTime).getTime() - new Date(b.departureDateTime).getTime();
    });
  });
  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly selectedTransport = signal<Transport | undefined>(undefined);
  readonly isFormOpen = signal<boolean>(false);

  ngOnInit(): void {
    const tripId = this.tripId();
    if (tripId && !isNaN(tripId)) {
      this.loadTransports(tripId);
    }
  }

  private loadTransports(tripId: number): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.transportService.findAllByTripId(tripId).subscribe({
      next: (data: Transport[]) => {
        this.rawTransports.set(data);
        this.isLoading.set(false);
        console.log(this.rawTransports());
      },
      error: (error: HttpErrorResponse) => {
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to load transports');
        this.isLoading.set(false);
      },
    });
  }

  openCreateForm() {
    this.selectedTransport.set(undefined);
    this.isFormOpen.set(true);
  }

  openEditForm(item: Transport) {
    this.selectedTransport.set(item);
    this.isFormOpen.set(true);
  }

  closeForm() {
    this.isFormOpen.set(false);
    this.selectedTransport.set(undefined);
    this.loadTransports(this.tripId());
  }

  deleteTransport(item: Transport) {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.transportService.delete(item.tripId, item.id).subscribe({
      next: () => {
        this.loadTransports(item.tripId);
      },
      error: (error: HttpErrorResponse) => {
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to delete transport');
        this.isLoading.set(false);
      },
    });
  }

  getTransportIcon(value: TransportType): string {
    return TRANSPORT_TYPES.find((t) => t.value === value)?.icon || 'explore';
  }
}
