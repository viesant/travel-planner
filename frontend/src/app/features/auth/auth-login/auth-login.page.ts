import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/security/services/auth.service';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { AuthRequest } from '../models/auth-request';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-auth-login',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatCardModule,
    RouterLink,
    MatProgressSpinner,
  ],
  templateUrl: './auth-login.page.html',
  styleUrl: './auth-login.page.scss',
})
export class AuthLoginPage {
  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);
  private readonly fb = inject(FormBuilder);

  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly hidePassword = signal(true);

  readonly loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);
    const credentials: AuthRequest = this.loginForm.getRawValue();

    this.authService
      .login(credentials)
      .pipe(switchMap(() => this.authService.fetchMe()))
      .subscribe({
        next: () => {
          this.isLoading.set(false);
          this.router.navigate(['/trips']);
        },
        error: (error: HttpErrorResponse) => {
          console.error('Authentication or profile fetch failed: ', error);
          this.isLoading.set(false);

          if (error.status === 0) {
            this.errorMessage.set(
              'Cannot connect to the server. Please check if the backend is running.',
            );
          } else if (error.status === 500) {
            this.errorMessage.set(
              'Database connection failed. Please ensure the Docker containers are healthy.',
            );
          } else {
            const problem: ProblemDetails = error.error;
            this.errorMessage.set(
              problem?.detail || 'Invalid email or password. Please try again.',
            );
          }
        },
      });
  }
}
