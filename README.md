Blood Donation Project 

Core Technologies
Framework: Spring Boot
Language: Java
Build Tool: Maven
IDE Support: Visual Studio Code with Spring Boot extensions
Backend Stack
Spring Framework Components:

Spring Data JPA (for database operations)
Spring Transaction Management (@Transactional)
Spring Stereotypes (@Service)
Spring Dependency Injection (@RequiredArgsConstructor)
Database:

JPA/Hibernate ORM
Soft delete pattern implementation (using deletedAt field)
Lombok Library:

Annotation processing for boilerplate reduction
@RequiredArgsConstructor for constructor generation

Architecture Pattern
Layered Architecture:
Service Layer (Business Logic)
Repository Layer (Data Access)
DTO Layer (Data Transfer Objects)
Entity Layer (Domain Models)
Key Features Implemented
User Management System:

User registration with email validation
User profile management (CRUD operations)
Soft delete functionality
User activation/deactivation
Blood Group Management: Filter users by blood group

Location-Based Search: Filter users by city

Data Validation: Email uniqueness checks

Design Patterns Used
Repository Pattern: for data access abstraction

Best Practices
Separation of concerns (Service, Repository, Entity, DTO layers)
Dependency injection using Spring's IoC container
Transaction management for data integrity
Enum usage for BloodGroup (type safety)
Exception handling for business logic validation
DTO Pattern: for API request/response handling
Service Pattern: for business logic encapsulation
Transactional Pattern: for data consistency
