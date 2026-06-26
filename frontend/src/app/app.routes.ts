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
    loadComponent: () => import('./features/auth/login/login.page').then((m) => m.LoginPage),
    canActivate: [anonGuard],
  },
  {
    path: 'auth/register',
    loadComponent: () =>
      import('./features/auth/register/register.page').then((m) => m.RegisterPage),
    canActivate: [anonGuard],
  },
  {
    path: 'trips',
    loadComponent: () => import('./features/trips/list/list.page').then((m) => m.ListPage),
    canActivate: [authGuard],
  },
  {
    path: '**',
    redirectTo: 'trips',
    pathMatch: 'full',
  },
];
