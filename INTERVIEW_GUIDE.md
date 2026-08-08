# ScaleLink Interview Guide

## Google SWE Interview Discussion Questions

### Why Redis?

**Short answer:** Redis handles the redirect hot path at sub-millisecond latency and provides distributed rate limiting across multiple app instances.

**Detailed answer:**
- 99%+ of ScaleLink traffic is `GET /{shortCode}` redirects — a pure read operation ideal for caching
- Redis GET operations complete in ~0.5ms vs PostgreSQL ~2-5ms — 4-10× improvement
- Token bucket rate limiting requires atomic operations (`INCR`, Lua scripts) that Redis provides natively
- All instances behind a load balancer share the same Redis — rate limits are globally consistent
- TTL support automatically expires stale cache entries without manual cleanup
- Trade-off: eventual consistency — acceptable for redirects where stale data is bounded by TTL and explicit invalidation on updates

---

### Why PostgreSQL?

**Short answer:** ACID guarantees for unique short codes and relational data model for users → URLs → analytics.

**Detailed answer:**
- Creating short URLs requires **strong uniqueness** on `short_code` and `custom_alias` — PostgreSQL UNIQUE constraints + transactions prevent race conditions
- Natural relational model: users own URLs, URLs have analytics events
- Rich indexing: B-tree, partial indexes, composite indexes for redirect and aggregation queries
- Read replicas scale analytics reads without impacting write path
- At 10M+ redirects/day, analytics table partitions by month — PostgreSQL handles this well
- Alternative considered: DynamoDB — good for key-value reads but awkward for analytics aggregations and joins

---

### Why Docker?

**Short answer:** Reproducible environments from laptop to production with identical dependencies.

**Detailed answer:**
- `docker compose up` gives developers PostgreSQL + Redis + App in minutes
- Multi-stage builds produce optimized production images (~200MB JRE-only)
- CI/CD pipeline builds identical images tested in CI and deployed to EC2
- Blue-green deployment swaps containers without downtime
- Environment variables configure each environment without code changes

---

### How would you scale to 10M requests/day?

**10M redirects/day ≈ 120 RPS average, ~1,000 RPS peak**

| Layer | Strategy |
|-------|----------|
| **CDN** | Cache 302 redirects at CloudFront/Fastly edge — eliminates origin traffic for viral links |
| **App tier** | 10-20 stateless instances behind ALB with auto-scaling |
| **Redis** | ElastiCache Cluster Mode — shard redirect cache, read replicas for hot keys |
| **PostgreSQL** | Primary + 2-3 read replicas; redirect reads off replicas |
| **Analytics** | Kafka event stream → dedicated analytics workers → ClickHouse |
| **Partitioning** | `analytics` table partitioned by month; archive old partitions |

**Bottleneck order:**
1. Analytics writes → async + message queue
2. Redis memory → CDN + LRU eviction
3. PostgreSQL connections → read replicas + connection pool tuning

---

### What happens if Redis fails?

**Impact:**
- All cache misses → every redirect hits PostgreSQL
- Rate limiting fails open or closed (configurable — we fail closed with 503 or degrade to in-memory)

**Mitigation:**
1. **Immediate:** PostgreSQL handles redirect lookups (degraded latency, system stays up)
2. **Circuit breaker:** Resilience4j opens circuit after failure threshold, skips Redis calls
3. **Recovery:** Redis Sentinel/Cluster auto-failover to replica
4. **Prevention:** ElastiCache Multi-AZ, health checks remove unhealthy Redis from path

**Key design decision:** Cache-aside means Redis is an optimization, not a dependency for correctness.

---

### How do you prevent collisions?

**Three layers:**

1. **Application pre-check:** `existsByShortCode()` before insert
2. **Retry loop:** Generate new random Base62 code up to 5 times on collision
3. **Database UNIQUE constraint:** Final authority — catches concurrent insert races

For custom aliases: explicit uniqueness check + UNIQUE constraint on `custom_alias` column.

**Why not just rely on DB?** Pre-checks avoid unnecessary failed transactions. DB constraint is the safety net.

**Collision probability:** With 7-char Base62 (3.5 trillion combinations), collision probability is negligible until hundreds of millions of URLs.

---

### Why use Base62?

- **URL-safe:** Only `[0-9A-Za-z]` — no encoding needed in URLs
- **Compact:** 7 chars vs 36-char UUID
- **Human-shareable:** `scalink.io/abc1234` vs `scalink.io/550e8400-e29b-41d4-a716-446655440000`
- **Two modes:** Random generation for auto codes; `encode(id)` for deterministic codes from sequential IDs
- **Not Base64:** Avoids `+`, `/`, `=` which require URL encoding

---

### How does rate limiting work?

**Algorithm:** Token Bucket (implemented in Redis via Lua script)

```
capacity = 100 (anonymous) or 500 (authenticated)
refill_rate = capacity / 60 tokens/second
on each request:
  elapsed = now - last_refill
  tokens = min(capacity, tokens + elapsed * refill_rate)
  if tokens >= 1: consume 1, allow
  else: reject with HTTP 429
```

**Distributed:** All instances share Redis keys — globally consistent limits.

**Tiers:**
- Anonymous (IP-based): 100 req/min
- Authenticated (user ID): 500 req/min
- Admin: unlimited

**Why token bucket vs sliding window?** Token bucket allows burst traffic (up to capacity) while maintaining average rate — better UX than strict sliding window.

---

### What are the system bottlenecks?

| Bottleneck | When | Mitigation |
|------------|------|------------|
| Analytics writes | >1M clicks/day | Async processing, Kafka queue |
| Analytics table size | >100M rows | Monthly partitioning, archival |
| Redis memory | Many unique URLs | LRU eviction, CDN for hot URLs |
| PostgreSQL connections | Many app instances | PgBouncer, read replicas |
| BCrypt login | High login traffic | Rate limiting, consider caching auth |
| Single hot URL | Viral link | CDN edge caching of 302 |

---

## Additional Interview Topics

### CAP Theorem Trade-offs
- **Redis cache:** AP system — availability over consistency (stale reads acceptable)
- **PostgreSQL:** CP system — consistency over availability during failover
- **Overall:** Eventual consistency model with bounded staleness (TTL)

### Idempotency
- URL creation is NOT idempotent (each call creates new URL)
- Redirect is naturally idempotent (same result every time)
- Analytics events are append-only (duplicates possible but acceptable)

### Observability
- Correlation IDs trace requests across logs
- Prometheus metrics for cache, rate limits, HTTP latency
- Structured JSON logging in production

### Security Deep Dive
- JWT: HS256 signed, 24h expiry, claims: userId, email
- BCrypt strength 12: ~250ms hash time per login
- IP hashed with SHA-256 before storage (privacy)
- Rate limiting prevents abuse and DDoS amplification
