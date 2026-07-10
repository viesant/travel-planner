import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { Observable, switchMap, tap } from 'rxjs';
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
  private readonly URL_ME = 'http://localhost:8081/users/me';

  private readonly tokenSignal = signal<string | null>(localStorage.getItem('token'));
  private readonly userSignal = signal<string | null>(localStorage.getItem('username'));

  readonly accessToken = computed(() => this.tokenSignal());
  readonly isAuthenticated = computed(() => !!this.tokenSignal() && !!this.userSignal());

  readonly currentUser = computed(() => this.userSignal());

  login(credentials: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.URL_LOGIN, credentials).pipe(
      tap((response) => {
        localStorage.setItem('token', response.accessToken);
        this.tokenSignal.set(response.accessToken);
      }),
    );
  }

  fetchMe(): Observable<User> {
    return this.http.get<User>(this.URL_ME).pipe(
      tap((user) => {
        localStorage.setItem('username', user.name);
        this.userSignal.set(user.name);
      }),
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.tokenSignal.set(null);
    this.userSignal.set(null);
  }

  register(userData: RegisterRequest): Observable<User> {
    return this.http.post<User>(this.URL_REGISTER, userData);
  }
}
