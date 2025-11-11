# Smart Clinic Management System - Architecture Design Document

## Section 1: Architecture Summary

The Smart Clinic Management System follows a three-tier architectural pattern that cleanly separates concerns across presentation, application, and data layers. The presentation layer serves dual purposes: it uses Thymeleaf templates to render dynamic HTML dashboards for web users while simultaneously exposing RESTful APIs for external client applications. The application layer is built with Spring Boot, which manages business logic through organized MVC controllers for web interactions and REST controllers for API endpoints. The data layer implements a strategic dual-database approach, utilizing MySQL with Spring Data JPA for structured relational data requiring ACID transactions, and MongoDB with Spring Data MongoDB for flexible document storage of semi-structured clinical data. This architecture ensures scalability, maintainability, and flexibility in serving different client needs.

## Section 2: Numbered Flow - Request/Response Cycle

1. **Client Request Initiation**: A user interacts with the web interface through a browser or an external system calls a REST API endpoint

2. **Presentation Layer Routing**:
   - Web requests are handled by Spring MVC controllers mapped to specific URLs
   - API requests are processed by REST controllers with appropriate HTTP method mappings

3. **Request Processing**: Spring Boot's DispatcherServlet routes incoming requests to the appropriate controller based on URL patterns and annotations

4. **Controller Handling**: The designated controller processes the request, validates input parameters, and determines the required business operations

5. **Service Layer Invocation**: Controllers delegate business logic to service layer components that contain the core application rules and processing logic

6. **Data Access Coordination**: Service components interact with repository interfaces to perform data operations, applying business validations and transformations

7. **Database Operations**:
   - Structured data operations use Spring Data JPA repositories to interact with MySQL
   - Document-based data operations use Spring Data MongoDB repositories to interact with MongoDB

8. **Data Persistence/Retrieval**:
   - MySQL handles relational data with JPA entities and transactions
   - MongoDB stores and retrieves document collections with flexible schemas

9. **Data Return**: Database results are returned through repository interfaces to the service layer

10. **Business Logic Application**: Service components process the data, apply additional business rules, and prepare the response objects

11. **Response Preparation**:
    - For web requests: Controllers add data to the model and return view names for Thymeleaf rendering
    - For API requests: REST controllers return data objects that Spring automatically serializes to JSON

12. **View Rendering** (Web only): Thymeleaf template engine processes HTML templates with model data to generate dynamic web pages

13. **HTTP Response**: The completed response (HTML content or JSON data) is sent back to the client through the HTTP response

14. **Client Processing**: The web browser renders the HTML content or the API client processes the JSON response data