import { Routes } from '@angular/router';
import { anonGuard } from './core/auth/guards/anon-guard';
import { authGuard } from './core/auth/guards/auth-guard';

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
    loadComponent: () => import('./features/trips/list/list.page').then((m) => m.ListPage),
    canActivate: [authGuard],
  },
  {
    path: 'trips/new',
    loadComponent: () => import('./features/trips/form/form.page').then((m) => m.FormPage),
    canActivate: [authGuard],
  },
  {
    path: 'trips/:id',
    loadComponent: () => import('./features/trips/detail/detail.page').then((m) => m.DetailPage),
    canActivate: [authGuard],
  },
  {
    path: 'trips/:id/edit',
    loadComponent: () => import('./features/trips/form/form.page').then((m) => m.FormPage),
    canActivate: [authGuard],
  },
  {
    path: '**',
    redirectTo: 'trips',
    pathMatch: 'full',
  },
];
