# Overview

This application is using the MVVM (Model-View-ViewModel) clean architecture. The primary functionality of the app is to display a list of repositories, and when a user selects a repository, it navigates to a details screen to show more information about that repository.

## Features

MVVM Clean Architecture: The app is structured into three primary layers: Data, Domain, and Presentation.
Paging 3: The app uses Paging 3 to load and display data in small chunks. Initially, it loads 40 items, with subsequent pages loading 20 items each. This optimizes performance and reduces memory usage when dealing with large datasets.
Retrofit and OkHttp: For network operations, Retrofit is used alongside OkHttpClient. OkHttpClient is configured to log all data traffic, which helps in debugging and monitoring network calls.
Dependency Injection with Hilt: Hilt is used for dependency injection, which simplifies the process of providing dependencies across different layers of the app.
Compose Navigation: The app uses Jetpack Compose for UI, with Compose Navigation to handle navigation between the home screen and the repository details screen.
Testing with Mockk: The app's testing is implemented using Mockk test ViewModels and other components.

## Architecture

### Layers

Data Layer:
This layer is responsible for all data operations. It includes the repository implementation, API definitions, and everything related to network communication and dependency injection.

Domain Layer:
This is the business logic layer, where repository interfaces, models, and use cases are defined.

Presentation Layer:
This layer contains the ViewModel and UI components. It interacts with the domain layer through ViewModels, which hold the state and logic for the UI.

#### Details 

Repository Interface (Domain Layer):
Defines the contract for data operations, allowing the domain layer to be independent of the actual data source.

Repository Implementation (Data Layer):
Implements the repository interface.

ViewModel (Presentation Layer):
Holds the UI state and interacts with the domain layer to fetch and manage data. It provides data to the UI via StateFlow.

UI (Presentation Layer):
Composed using Jetpack Compose, the UI layer displays data to the user and handles user interactions.

## Navigation

Home Screen:
Displays a paginated list of repositories. Users can scroll through the list, with new items being loaded as they reach the end of the list.

Repository Details Screen:
When a user selects a repository from the list, they are navigated to the details screen, which displays more information about the selected repository.

## Dependencies

Retrofit: For making network requests.
OkHttpClient: For logging network traffic.
Paging 3: For loading data in small, manageable chunks.
Hilt: For dependency injection.
Jetpack Compose: For building the UI.
Mockk: For unit testing.