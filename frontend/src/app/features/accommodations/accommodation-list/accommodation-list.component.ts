import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, input, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Accommodation } from '../../../shared/models/accommodation';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { AccommodationService } from '../services/accommodation.service';
import { MatListModule } from '@angular/material/list';
import { MatExpansionModule } from '@angular/material/expansion';
import { AccommodationFormComponent } from '../accommodation-form/accommodation-form.component';

@Component({
  selector: 'app-accommodation-list',
  imports: [
    CommonModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatExpansionModule,
    AccommodationFormComponent,
  ],
  templateUrl: './accommodation-list.component.html',
  styleUrl: './accommodation-list.component.scss',
})
export class AccommodationListComponent implements OnInit {
  private readonly accommodationService = inject(AccommodationService);

  readonly tripId = input.required<number>();

  readonly rawAccommodations = signal<Accommodation[]>([]);
  readonly sortedAccommodations = computed(() => {
    return [...this.rawAccommodations()].sort((a, b) => {
      return new Date(a.checkInDate).getTime() - new Date(b.checkInDate).getTime();
    });
  });
  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly selectedAccommodation = signal<Accommodation | undefined>(undefined);
  readonly isFormOpen = signal<boolean>(false);

  ngOnInit(): void {
    const tripId = this.tripId();
    if (tripId && !isNaN(tripId)) {
      this.loadAccommodations(tripId);
    }
  }

  private loadAccommodations(tripId: number): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.accommodationService.findAllByTripId(tripId).subscribe({
      next: (data: Accommodation[]) => {
        this.rawAccommodations.set(data);
        this.isLoading.set(false);
        console.log(this.rawAccommodations());
      },
      error: (error: HttpErrorResponse) => {
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to load accommodations');
        this.isLoading.set(false);
      },
    });
  }

  openCreateForm() {
    this.selectedAccommodation.set(undefined);
    this.isFormOpen.set(true);
  }

  openEditForm(item: Accommodation) {
    this.selectedAccommodation.set(item);
    this.isFormOpen.set(true);
  }

  closeForm() {
    this.isFormOpen.set(false);
    this.selectedAccommodation.set(undefined);
    this.loadAccommodations(this.tripId());
  }

  deleteAccommodation(item: Accommodation) {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.accommodationService.delete(item.tripId, item.id).subscribe({
      next: () => {
        this.loadAccommodations(item.tripId);
      },
      error: (error: HttpErrorResponse) => {
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to delete accommodation');
        this.isLoading.set(false);
      },
    });
  }
}
