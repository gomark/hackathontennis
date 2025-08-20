# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Building and Running
- **Build**: `mvn package` or `mvn package -DskipTests` (skip tests)
- **Run locally**: `mvn spring-boot:run`
- **Run tests**: `mvn test`
- **Clean build**: `mvn clean package`

### Docker and Deployment
- **Build Docker image**: `docker build -t hackathontennis .`
- **Cloud Build**: Configured in `cloudbuild.yaml` for automatic CI/CD to Google Cloud Run
- **Deploy region**: asia-southeast1

## Architecture Overview

This is a Spring Boot 3.5.4 application using Java 21 that provides a tennis booking system with Firebase authentication.

### Key Components

**Authentication Layer**:
- `AuthInterceptor.java`: Firebase JWT token validation interceptor
- `MyConfig.java`: Web configuration for interceptor registration
- `HackathontennisApplication.java`: Firebase initialization with tenant-aware auth

**Controllers**:
- `FirstController.java` (`/first/*`): Public endpoints including database testing and environment variable access
- `SecureController.java` (`/api/*`): Protected endpoints requiring Firebase authentication (booking functionality)

**Infrastructure Integration**:
- **Database**: PostgreSQL on Google Cloud SQL with connection pooling (Hikari)
- **Authentication**: Firebase Auth with multi-tenancy support
- **Frontend**: CORS enabled for localhost:5173 and localhost:3000
- **JSON Handling**: Gson with custom `OffsetDateTimeAdapter`

### Configuration
- Environment variables required: `GOOGLE_CLOUD_PROJECT`, `tenantId`, `HKT_POSTGRES_*`
- Database connection via Cloud SQL connector
- Firebase credentials via Application Default Credentials

### Project Structure
```
src/main/java/org/noxnox/hackathontennis/
├── HackathontennisApplication.java  # Main application + Firebase setup
├── FirstController.java             # Public API endpoints
├── SecureController.java           # Protected API endpoints
├── AuthInterceptor.java            # Firebase auth validation
├── MyConfig.java                   # Web configuration
└── OffsetDateTimeAdapter.java      # JSON date handling
```

### Testing
- Test framework: Spring Boot Test
- Test location: `src/test/java/org/noxnox/hackathontennis/`

### Deployment Notes
- Multi-stage Docker build with Maven and Eclipse Temurin 21
- Deployed to Google Cloud Run
- Cloud Build pipeline configured for automatic deployment from git commits