import { CommonModule } from '@angular/common';
import { Component, computed, inject, input, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ActivityService } from '../services/activity.service';
import { Activity } from '../models/activity';
import { HttpErrorResponse } from '@angular/common/http';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { ActivityFormComponent } from '../activity-form/activity-form.component';

@Component({
  selector: 'app-activity-list',
  imports: [
    CommonModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatExpansionModule,
    ActivityFormComponent,
  ],
  templateUrl: './activity-list.component.html',
  styleUrl: './activity-list.component.scss',
})
export class ActivityListComponent implements OnInit {
  private readonly activityService = inject(ActivityService);

  readonly tripId = input.required<number>();

  readonly rawActivities = signal<Activity[]>([]);
  readonly sortedActivities = computed(() => {
    return [...this.rawActivities()].sort((a, b) => {
      return new Date(a.startDateTime).getTime() - new Date(b.startDateTime).getTime();
    });
  });
  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly selectedActivity = signal<Activity | undefined>(undefined);
  readonly isFormOpen = signal<boolean>(false);

  ngOnInit(): void {
    const tripId = this.tripId();
    if (tripId && !isNaN(tripId)) {
      this.loadActivities(tripId);
    }
  }

  private loadActivities(tripId: number): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.activityService.findAllByTripId(tripId).subscribe({
      next: (data: Activity[]) => {
        this.rawActivities.set(data);
        this.isLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to load activities');
        this.isLoading.set(false);
      },
    });
  }

  openCreateForm() {
    this.selectedActivity.set(undefined);
    this.isFormOpen.set(true);
  }

  openEditForm(item: Activity) {
    this.selectedActivity.set(item);
    this.isFormOpen.set(true);
  }

  closeForm() {
    this.isFormOpen.set(false);
    this.selectedActivity.set(undefined);
    this.loadActivities(this.tripId());
  }

  deleteActivity(item: Activity) {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.activityService.delete(item.tripId, item.id).subscribe({
      next: () => {
        this.loadActivities(item.tripId);
      },
      error: (error: HttpErrorResponse) => {
        const problem: ProblemDetails = error.error;
        this.errorMessage.set(problem?.detail || 'Failed to delete activity');
        this.isLoading.set(false);
      },
    });
  }
}
