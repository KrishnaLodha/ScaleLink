# Monitoring Guide

## Endpoints

| Endpoint | Auth | Description |
|----------|------|-------------|
| `/actuator/health` | Public (basic) | Overall health |
| `/actuator/health/liveness` | Public | Liveness probe |
| `/actuator/health/readiness` | Public | Readiness probe |
| `/actuator/metrics` | Authorized | All metrics |
| `/actuator/prometheus` | Authorized | Prometheus scrape |
| `/actuator/info` | Public | App info |

## Key Metrics

### HTTP Metrics
| Metric | Type | Description |
|--------|------|-------------|
| `scalink.http.request` | Timer | Request duration |
| `scalink.http.errors` | Counter | 5xx responses |
| `http.server.requests` | Timer | Spring Boot default |

### Cache Metrics
| Metric | Type | Description |
|--------|------|-------------|
| `scalink.cache.hits` | Counter | URL cache hits |
| `scalink.cache.misses` | Counter | URL cache misses |
| `scalink.cache.hit.ratio` | Gauge | Hit ratio (0-1) |
| `scalink.cache.lookup` | Timer | Cache lookup latency |

### Rate Limit Metrics
| Metric | Type | Description |
|--------|------|-------------|
| `scalink.rate_limit.exceeded` | Counter | 429 responses |

## Structured Logging

Production uses JSON logging via Logstash encoder:

```json
{
  "timestamp": "2026-06-18T10:00:00.000Z",
  "level": "INFO",
  "service": "scalink",
  "correlationId": "abc-123-def",
  "message": "Redirect resolved for code=abc1234"
}
```

Correlation ID is set via `X-Correlation-Id` header or auto-generated.

## Alerting Thresholds (Recommended)

| Alert | Condition | Severity |
|-------|-----------|----------|
| High error rate | 5xx > 1% for 5min | Critical |
| High latency | P95 > 500ms for 5min | Warning |
| Low cache hit rate | hit.ratio < 0.8 for 10min | Warning |
| Rate limit spike | exceeded > 100/min | Info |
| Health check fail | readiness != UP | Critical |
| Redis down | circuit breaker open | Critical |

## Prometheus Scrape Config

```yaml
scrape_configs:
  - job_name: scalink
    metrics_path: /actuator/prometheus
    static_configs:
      - targets: ['scalink:8080']
```

## CloudWatch Integration

Export Prometheus metrics to CloudWatch via ADOT collector or use Micrometer CloudWatch registry for native integration.

## Load Test Baselines

Run `k6 run load-tests/k6-redirect.js` and compare:

| Metric | Target |
|--------|--------|
| P95 latency | < 100ms |
| P99 latency | < 250ms |
| Error rate | < 1% |
| Cache hit rate | > 90% |
