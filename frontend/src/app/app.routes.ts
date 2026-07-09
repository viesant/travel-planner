import { Routes } from '@angular/router';
import { anonGuard } from './core/security/guards/anon-guard';
import { authGuard } from './core/security/guards/auth-guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'auth/login',
    pathMatch: 'full',
  },
  {
    path: 'auth/login',
    loadComponent: () =>
      import('./features/auth/auth-login/auth-login.page').then((m) => m.AuthLoginPage),
    canActivate: [anonGuard],
  },
  {
    path: 'auth/register',
    loadComponent: () =>
      import('./features/auth/auth-register/auth-register.page').then((m) => m.AuthRegisterPage),
    canActivate: [anonGuard],
  },
  {
    path: 'trips',
    loadComponent: () =>
      import('./features/trips/trip-list/trip-list.page').then((m) => m.TripListPage),
    canActivate: [authGuard],
  },
  {
    path: 'trips/new',
    loadComponent: () =>
      import('./features/trips/trip-form/trip-form.page').then((m) => m.TripFormPage),
    canActivate: [authGuard],
  },
  {
    path: 'trips/:id',
    loadComponent: () =>
      import('./features/trips/trip-detail/trip-detail.page').then((m) => m.TripDetailPage),
    canActivate: [authGuard],
  },
  {
    path: 'trips/:id/edit',
    loadComponent: () =>
      import('./features/trips/trip-form/trip-form.page').then((m) => m.TripFormPage),
    canActivate: [authGuard],
  },
  {
    path: '**',
    redirectTo: 'trips',
    pathMatch: 'full',
  },
];
