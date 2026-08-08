# Production Checklist

## Pre-Deployment

- [ ] All tests pass (`mvn test`)
- [ ] Coverage report reviewed (`target/site/jacoco/index.html`)
- [ ] OWASP dependency scan clean (no CVSS ≥ 9)
- [ ] No secrets in codebase (Gitleaks clean)
- [ ] `JWT_SECRET` is cryptographically random (≥ 256 bits)
- [ ] Database passwords rotated from defaults
- [ ] `SPRING_PROFILES_ACTIVE=production`
- [ ] Rate limiting enabled
- [ ] SSL/TLS configured (Let's Encrypt or ACM)

## Infrastructure

- [ ] EC2 instance in correct VPC/subnet
- [ ] Security groups restrict DB/Redis to app tier only
- [ ] RDS Multi-AZ enabled
- [ ] ElastiCache in same VPC
- [ ] Nginx reverse proxy configured
- [ ] Health check endpoints responding
- [ ] Auto Scaling Group configured (if applicable)
- [ ] S3 backup bucket for DB dumps

## Application

- [ ] Graceful shutdown enabled (`server.shutdown=graceful`)
- [ ] Connection pool sized for instance count
- [ ] Redis connection pool configured
- [ ] Flyway migrations applied
- [ ] Actuator endpoints secured (not publicly exposed except health)
- [ ] Swagger UI disabled or auth-protected in production
- [ ] Log level set to INFO/WARN in production
- [ ] JSON structured logging enabled

## Monitoring

- [ ] Prometheus scraping configured
- [ ] CloudWatch alarms set
- [ ] Error rate alert (> 1%)
- [ ] Latency alert (P95 > 500ms)
- [ ] Cache hit ratio alert (< 80%)
- [ ] Disk space monitoring on EC2
- [ ] RDS storage monitoring

## Post-Deployment

- [ ] Smoke test: register, login, create URL, redirect
- [ ] Health endpoints return UP
- [ ] Metrics flowing to Prometheus/CloudWatch
- [ ] Load test baseline recorded
- [ ] Rollback procedure tested
- [ ] On-call runbook updated

## Disaster Recovery

- [ ] RDS automated backups enabled (7-day retention)
- [ ] Cross-region backup replication configured
- [ ] Redis persistence (AOF) enabled
- [ ] Recovery procedure documented and tested
- [ ] RTO/RPO targets defined (RTO: 1h, RPO: 5min)
