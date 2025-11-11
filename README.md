# Smart Clinic Management System

A comprehensive clinic management system built with Spring Boot, MySQL, MongoDB, and modern web technologies.

## Project Overview

This is a capstone project for the Java Development course. The Smart Clinic Management System enables efficient management of doctors, patients, and appointments with role-based access control.

## Features

- **Role-Based Authentication**: Admin, Doctor, and Patient roles with JWT authentication
- **Doctor Management**: Add, view, search, and filter doctors by specialty and availability
- **Patient Management**: Patient registration and profile management
- **Appointment System**: Book and manage appointments between doctors and patients
- **Prescription Management**: Doctors can create and manage prescriptions (MongoDB)
- **Reports**: Daily appointment reports and analytics
- **Responsive UI**: Modern, user-friendly interface

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA (MySQL)
- Spring Data MongoDB
- JWT for authentication
- Maven

### Frontend
- HTML5, CSS3, JavaScript
- Thymeleaf template engine
- Responsive design

### Database
- MySQL 8.0 (Relational data)
- MongoDB 7.0 (Document data)

### DevOps
- Docker & Docker Compose
- GitHub Actions CI/CD

## Getting Started

### Prerequisites

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- MongoDB 7.0+
- Docker (optional)

### Running Locally

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd capstonecoursera
   ```

2. **Set up MySQL**
   ```sql
   CREATE DATABASE cms;
   ```

3. **Run the application**
   ```bash
   cd app
   mvn spring-boot:run
   ```

4. **Access the application**
   - Open browser: http://localhost:8080

### Running with Docker

```bash
docker-compose up -d
```

### Default Credentials

**Admin:**
- Username: `admin1`
- Password: `admin123`

**Doctor:**
- Username: `drsmith`
- Password: `doctor123`

**Patient:**
- Username: `alice`
- Password: `patient123`

## Documentation

- Architecture Design: [schema-architecture.md](schema-architecture.md)
- Database Design: [schema-design.md](schema-design.md)
- User Stories: [user_stories.md](user_stories.md)

## Author

Created as part of the Coursera Java Development Capstone Project. 
