# 🌍 Travel Planner Frontend

[![Angular](https://img.shields.io/badge/Angular-21-DD0031?logo=angular&logoColor=white)](https://angular.dev)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.9-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org)
[![Angular Material](https://img.shields.io/badge/Angular%20Material-3-757575?logo=angular&logoColor=white)](https://material.angular.dev)
[![RxJS](https://img.shields.io/badge/RxJS-7.8-B7178C?logo=reactivex&logoColor=white)](https://rxjs.dev)
[![SCSS](https://img.shields.io/badge/SCSS-Enabled-CC6699?logo=sass&logoColor=white)](https://sass-lang.com)

Frontend application for the Travel Planner platform.

Built with Angular 21 and Angular Material 3, this application provides a modern, high-performance interface for managing trips, accommodations, activities, and transportation.

---

## 🚀 Quick Highlights

* ⚡ Angular 21 with Native Standalone Components
* 🎨 Angular Material 3 UI design system integration
* 🔐 Secure JWT authentication storage and lifecycle
* 🛡️ Route protection with functional architecture guards
* 🔄 HTTP interceptor for automatic Bearer token handling
* 📝 Reactive Forms with typed controls and native validation
* 📡 Feature-based modular architecture
* ⚙️ Signals for modern reactive state management

---

## 🛠 Tech Stack

* Angular 21
* TypeScript
* Angular Material 3
* Angular CDK
* RxJS
* Reactive Forms
* Signals
* SCSS

---

## 🧱 Architecture

The application follows a feature-based architecture optimized for maintenance and scalability:

```
src/app
│
├── core
│   └── security
│       ├── guards
│       ├── interceptors
│       └── services
│
├── features
│   ├── auth
│   ├── trips
│   ├── activities
│   ├── accommodations
│   └── transports
│
└── shared
    ├── components
    ├── pipes
    ├── models
    └── utils
```

### Architectural principles

* Feature isolation by domain
* Core, features, and shared resource strict separation
* Single source of truth using typed models for API contracts
* Reactive form-driven components
* Highly reusable UI utilities and layouts
* Centralized state control via Signals

---

## 🔐 Authentication

Authentication is handled through short-lived JWT tokens issued by the backend API.

Implemented:

* Seamless login flow with error tracing
* Token and identity persistence via local storage
* Centralized authentication state management
* Protected application dashboard routes
* Anonymous-only verification routes for login/register pages
* Automatic Bearer token injection using HTTP interceptor
* Automated state clearing on session expiration

Example request header handling:

```http
Authorization: Bearer <token>
```

---

## ✈️ Features

### Authentication

* Secure user login with error indicators
* User registration with strict formatting rules
* Synchronized session management

### Trips

* Responsive dashboard grid of personal trips
* Trip creation with automated chronological bounds
* In-place trip modification and metadata upgrades
* Consolidated rich-domain layout overview

### Accommodations

* Complete stay tracking linked directly to trip schedules
* Input forms optimized for check-in/check-out boundaries
* Cost logging and reservation data control

### Activities

* Dynamic scheduling of tours, sights, and attractions inside trips
* Timepicker controls using granular interval handling
* Visual indicators for timeline events and custom reminders

### Transports

* Transport management inside trips supporting multiple transit profiles
* Automated icon dictionary bindings for ticket classes
* Highly responsive departure and arrival scheduling

---

## 📋 Forms & Validation

The application uses Angular Reactive Forms:

* Strongly typed form controls avoiding runtime bugs
* Built-in and conditional validators matching API constraints
* Visual feedback triggered only on dirty or touched states
* UI isolation during loading states
* Server-side validation error mapping (`ProblemDetails`)

---

## 🎨 UI

The interface is built with Angular Material components matching the modern material spec:

* Cards
* Forms
* Buttons
* Menus & Dropdowns
* Date pickers & Calendars
* Time pickers
* Tabs

---

## 📦 Running Locally

### Requirements

* Node.js
* Angular CLI

Install dependencies:

```bash
npm install
```

Run development server:

```bash
ng serve
```

Application will be available at:

```
http://localhost:4200
```

---

## 🔗 Backend Integration

This frontend consumes the Travel Planner REST API.

Backend requirements:

```
Backend running at:
http://localhost:8081
```

The API provides:

* Authentication
* Trip management
* Activities
* Accommodations
* Transports

---

## 🧪 Testing

Run unit tests:

```bash
ng test
```

---

## 📌 Roadmap

* Global API error handling service (Docker & server downtime fallback)
* Custom date calculation pipes for trip length counters
* Advanced trip statistics dashboard
* Maps integration for itinerary routing
* Expense management interface
* Photo attachments and gallery views

---

## 👤 Author

**Ricardo Vieira dos Santos**

GitHub:
https://github.com/viesant

```
```
