🩸 Blood Donation Project

A backend system for managing blood donors and recipients using a clean layered architecture, built with Spring Boot.

🚀 Core Technologies

Framework: Spring Boot

Language: Java

Build Tool: Maven

IDE Support: Visual Studio Code (Spring Boot Extensions)

🧩 Backend Stack
Spring Framework Components

Spring Data JPA – Database operations

Spring Transaction Management – @Transactional

Spring Stereotypes – @Service

Dependency Injection – @RequiredArgsConstructor

Database

JPA / Hibernate ORM

Soft Delete Pattern using deletedAt field

Lombok

Reduces boilerplate code

@RequiredArgsConstructor for constructor-based dependency injection

🏗 Architecture Pattern
Layered Architecture

Controller Layer – API endpoints

Service Layer – Business logic

Repository Layer – Data access

DTO Layer – Data transfer objects

Entity Layer – Domain models

✨ Key Features
👤 User Management

User registration with email validation

User profile CRUD operations

Soft delete support

User activation / deactivation

Email uniqueness validation

🩸 Blood Group Management

Filter users by blood group

Enum-based blood group handling (type safety)

📍 Location-Based Search

Filter users by city

🧠 Design Patterns Used

Repository Pattern – Abstraction of data access

DTO Pattern – API request and response handling

Service Pattern – Business logic encapsulation

Transactional Pattern – Ensures data consistency

✅ Best Practices Followed

Clear separation of concerns across layers

Clean and maintainable code structure

Constructor-based dependency injection

Proper transaction management

Exception handling for business logic validation

Use of Enums for domain consistency

Soft delete strategy to preserve data integrity

Service Pattern: for business logic encapsulation

Transactional Pattern: for data consistency
