# Hackathon Tennis Booking System

A tennis court booking system built with Spring Boot and Firebase authentication for seamless court reservations.

## 🏗️ Architecture

**Backend**: Spring Boot 3.5.4 with Java 21  
**Database**: PostgreSQL on Google Cloud SQL  
**Authentication**: Firebase Auth with multi-tenancy support  
**Infrastructure**: Google Cloud Run with automated CI/CD  
**Frontend**: Bundled static assets (Vite-based)

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.6+
- Docker (optional)
- Google Cloud Project with Cloud SQL and Firebase configured

### Environment Variables
Set the following environment variables:
```bash
GOOGLE_CLOUD_PROJECT=your-project-id
tenantId=your-firebase-tenant-id
HKT_POSTGRES_HOST=your-postgres-host
HKT_POSTGRES_PORT=5432
HKT_POSTGRES_DATABASE=your-database
HKT_POSTGRES_USERNAME=your-username
HKT_POSTGRES_PASSWORD=your-password
```

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd hackathontennis
   ```

2. **Build the project**
   ```bash
   mvn package
   ```
   
   Skip tests during build:
   ```bash
   mvn package -DskipTests
   ```

3. **Run locally**
   ```bash
   mvn spring-boot:run
   ```

4. **Run tests**
   ```bash
   mvn test
   ```

5. **Clean build**
   ```bash
   mvn clean package
   ```

## 🐳 Docker Deployment

### Build Docker Image
```bash
docker build -t hackathontennis .
```

### Google Cloud Run Deployment
The project includes automated CI/CD via Cloud Build (`cloudbuild.yaml`). Pushes to the main branch automatically deploy to Google Cloud Run in the `asia-southeast1` region.

## 📁 Project Structure

```
src/main/java/org/noxnox/hackathontennis/
├── HackathontennisApplication.java  # Main application + Firebase setup
├── AuthInterceptor.java            # Firebase JWT token validation
├── MyConfig.java                   # Web configuration
├── OffsetDateTimeAdapter.java      # JSON date handling
├── FirstController.java            # Public API endpoints (/first/*)
├── SecureController.java           # Protected API endpoints (/api/*)
├── entity/                         # JPA entities
│   ├── Booking.java
│   ├── Court.java
│   └── User.java
├── repository/                     # Data access layer
│   ├── BookingRepository.java
│   ├── CourtRepository.java
│   └── UserRepository.java
└── service/                        # Business logic layer
    ├── BookingService.java
    ├── CourtService.java
    └── UserService.java
```

## 🔗 API Endpoints

### Public Endpoints (`/first/*`)
- Database testing
- Environment variable access
- Health checks

### Protected Endpoints (`/api/*`)
- Court management
- Booking functionality
- User management

*Requires Firebase authentication token in request headers*

## 🔧 Key Technologies

- **Spring Boot 3.5.4** - Main framework
- **Spring Data JPA** - Database abstraction
- **Spring Session JDBC** - Session management
- **Firebase Admin SDK 9.2.0** - Authentication
- **PostgreSQL** - Database
- **Google Cloud SQL Connector** - Database connectivity
- **Hibernate Validator** - Input validation
- **Gson** - JSON serialization

## 🌐 CORS Configuration

Configured for frontend development:
- `localhost:5173` (Vite dev server)
- `localhost:3000` (React dev server)

## 🔐 Authentication

The application uses Firebase Authentication with JWT token validation. All `/api/*` endpoints require valid Firebase tokens. Multi-tenancy is supported through tenant-aware configuration.

## 🏗️ Database

- **Connection**: Google Cloud SQL PostgreSQL with HikariCP connection pooling
- **ORM**: Spring Data JPA with Hibernate
- **Entities**: User, Court, Booking with appropriate relationships

## 📊 Monitoring & Deployment

- **Region**: asia-southeast1
- **Platform**: Google Cloud Run
- **CI/CD**: Automated via Cloud Build
- **Build**: Multi-stage Docker build with Maven caching

## 🤝 Contributing

1. Create feature branch from main
2. Make changes and test locally
3. Ensure all tests pass: `mvn test`
4. Submit pull request
5. Automated deployment on merge to main