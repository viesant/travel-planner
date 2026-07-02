import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/auth/services/auth.service';
import { ProblemDetails } from '../../../shared/models/problem-details';
import { RegisterRequest } from '../models/register-request';

@Component({
  selector: 'app-auth-register',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    RouterLink,
  ],
  templateUrl: './auth-register.page.html',
  styleUrl: './auth-register.page.scss',
})
export class AuthRegisterPage {
  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);
  private readonly fb = inject(FormBuilder);

  readonly errorMessage = signal<string | null>(null);

  readonly registerForm = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }

    this.errorMessage.set(null);

    const userData: RegisterRequest = this.registerForm.getRawValue();

    this.authService.register(userData).subscribe({
      next: () => {
        this.router.navigate(['/auth/login']);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Registration failed:', error);

        const problem: ProblemDetails = error.error;
        this.errorMessage.set(
          problem?.detail || 'Failed to create account. Please check your data',
        );
      },
    });
  }
}
