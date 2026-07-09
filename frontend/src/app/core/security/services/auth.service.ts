import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthRequest } from '../../../features/auth/models/auth-request';
import { AuthResponse } from '../../../features/auth/models/auth-response';
import { RegisterRequest } from '../../../features/auth/models/register-request';
import { User } from '../../../shared/models/user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly URL_LOGIN = 'http://localhost:8081/auth/login';
  private readonly URL_REGISTER = 'http://localhost:8081/users';
  private readonly tokenSignal = signal<string | null>(localStorage.getItem('token'));

  readonly accessToken = computed(() => this.tokenSignal());
  readonly isAuthenticated = computed(() => !!this.tokenSignal());

  login(credentials: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.URL_LOGIN, credentials).pipe(
      tap((response) => {
        localStorage.setItem('token', response.accessToken);
        this.tokenSignal.set(response.accessToken);
      }),
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.tokenSignal.set(null);
  }

  register(userData: RegisterRequest): Observable<User> {
    return this.http.post<User>(this.URL_REGISTER, userData);
  }
}
