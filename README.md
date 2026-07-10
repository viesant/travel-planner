# Travel Planner

A full-stack travel planning application that helps users organize trips, accommodations, activities, and transportation.

This repository is a monorepo containing the backend API and frontend application.

## Tech Stack

### Backend

* Java 21
* Spring Boot 3
* Spring Security
* JWT Authentication
* PostgreSQL
* Docker

### Frontend

* Angular 21
* TypeScript
* Angular Material
* Reactive Forms
* Signals
* Standalone Components

## Features

### Authentication

* User login
* JWT-based authentication
* Protected routes
* Session management

### Trip Management

* Create, update, view and manage trips
* Trip details
* Travel dates and descriptions

### Travel Components

* Accommodation management
* Activities management
* Transportation management

## Project Structure

```
travel-planner/
│
├── backend/
│   └── Spring Boot REST API
│
└── frontend/
    └── Angular application
```

## Getting Started

### Backend

Requirements:

* Java 21
* Docker

Start PostgreSQL using Docker and run the Spring Boot application.

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend

Requirements:

* Node.js
* Angular CLI

Install dependencies and run:

```bash
cd frontend
npm install
ng serve
```

The application will be available at:

```
http://localhost:4200
```

## Architecture

The frontend follows a feature-based architecture:

```
src/app
├── core
│   └── security
├── features
│   ├── auth
│   ├── trips
│   ├── activities
│   ├── accommodations
│   └── transports
└── shared
```

## Status

🚧 In development

Current focus:

* Completing travel component management
* Improving validation and error handling
* Adding additional user experience improvements

## Future Improvements

* Expense management
* Maps integration
* Trip sharing
* File/photo attachments
* Advanced search and filters

