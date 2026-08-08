# ScaleLink

Production-grade distributed URL shortening platform

## Documentation

| Document | Description |
|----------|-------------|
| [SYSTEM_DESIGN.md](SYSTEM_DESIGN.md) | Full system design with scaling analysis |
| [ARCHITECTURE.md](ARCHITECTURE.md) | Component architecture and deployment |
| [FAQ.md](FAQ.md) | System Design Q&A |
| [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md) | AWS EC2 deployment guide |
| [docs/CI_CD.md](docs/CI_CD.md) | GitHub Actions pipeline |
| [docs/MONITORING.md](docs/MONITORING.md) | Metrics and alerting |
| [docs/PRODUCTION_CHECKLIST.md](docs/PRODUCTION_CHECKLIST.md) | Pre-launch checklist |

## Quick Start

```bash
cp .env.example .env
docker compose up --build
```

| Service | URL |
|---------|-----|
| API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Health | http://localhost:8080/actuator/health |
| Prometheus | http://localhost:8080/actuator/prometheus |

## Tech Stack

Java 21 · Spring Boot 3 · PostgreSQL · Redis · JWT · Docker · GitHub Actions

## API Overview

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/auth/register` | No | Register |
| POST | `/api/v1/auth/login` | No | Login |
| GET | `/api/v1/auth/me` | JWT | Current user |
| POST | `/api/v1/urls` | JWT | Create short URL |
| GET | `/api/v1/urls?page=0&size=20` | JWT | List URLs (paginated) |
| GET | `/{code}` | No | Redirect |
| GET | `/api/v1/analytics/{urlId}` | JWT | Analytics summary |
| GET | `/api/v1/dashboard` | JWT | User dashboard |

## Load Testing

```bash
k6 run load-tests/k6-redirect.js -e BASE_URL=http://localhost:8080 -e SHORT_CODE=abc1234
```

Simulates 1,000 concurrent users with cache-hit workload analysis.

## Run Tests

```bash
mvn test jacoco:report
```

## Project Phases

| Phase | Status | Scope |
|-------|--------|-------|
| 1 | Done | Scaffold, schema, Docker, Redis, Swagger |
| 2 | Done | JWT auth, register/login, validation |
| 3 | Done | URL shortening, Base62, CRUD, redirect |
| 4 | Done | Redis cache-aside, metrics, invalidation |
| 5 | Done | Async analytics, dashboard APIs |
| 6 | Done | Rate limiting, pagination, load testing |
| 7 | Done | CI/CD, observability, deployment |
| 8 | Done | System design documentation |

## Spring Profiles

| Profile | Use |
|---------|-----|
| `local` | Local dev, rate limiting disabled |
| `dev` | Development environment |
| `production` | Production with JSON logging, rate limits |

```bash
SPRING_PROFILES_ACTIVE=local mvn spring-boot:run
```

## Key Architecture Decisions

- **Cache-aside** Redis for sub-ms redirects
- **Token bucket** rate limiting in Redis (100/500 req/min)
- **Async analytics** — redirects never wait for DB writes
- **Stateless JWT** — horizontal scaling without session store
- **Base62** short codes with 3-layer collision prevention
- **Blue-green** deployment on AWS EC2

## Topics Covered

Distributed systems · Caching · Scalability · Rate limiting · System design · Performance engineering · CI/CD · Observability
