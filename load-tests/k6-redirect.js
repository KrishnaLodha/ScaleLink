import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const SHORT_CODE = __ENV.SHORT_CODE || 'abc1234';

const redirectDuration = new Trend('redirect_duration', true);
const cacheHitRate = new Rate('cache_hit_rate');
const redirectErrors = new Counter('redirect_errors');

export const options = {
  scenarios: {
    cache_hit_load: {
      executor: 'constant-vus',
      vus: 1000,
      duration: '2m',
      exec: 'redirectTest',
    },
  },
  thresholds: {
    redirect_duration: ['p(95)<100', 'p(99)<250'],
    http_req_failed: ['rate<0.01'],
    cache_hit_rate: ['rate>0.90'],
  },
};

export function redirectTest() {
  const start = Date.now();
  const res = http.get(`${BASE_URL}/${SHORT_CODE}`, { redirects: 0 });
  redirectDuration.add(Date.now() - start);

  const success = check(res, {
    'status is 302': (r) => r.status === 302,
    'has location header': (r) => r.headers.Location !== undefined,
  });

  if (!success) {
    redirectErrors.add(1);
  }

  // Assume cache hit when response is fast (< 20ms indicates Redis hit)
  cacheHitRate.add(Date.now() - start < 20);

  sleep(0.1);
}

export function handleSummary(data) {
  const p95 = data.metrics.redirect_duration?.values['p(95)'] || 0;
  const p99 = data.metrics.redirect_duration?.values['p(99)'] || 0;
  const totalReqs = data.metrics.http_reqs?.values.count || 0;
  const hitRate = data.metrics.cache_hit_rate?.values.rate || 0;

  console.log('\n=== ScaleLink Load Test Results ===');
  console.log(`Total requests:     ${totalReqs}`);
  console.log(`P95 latency:        ${p95.toFixed(2)}ms`);
  console.log(`P99 latency:        ${p99.toFixed(2)}ms`);
  console.log(`Est. cache hit rate:${(hitRate * 100).toFixed(1)}%`);
  console.log('===================================\n');

  return {
  stdout: textSummary(data, { indent: ' ', enableColors: true }),
  };
}

function textSummary(data, opts) {
  return JSON.stringify(data.metrics, null, 2);
}
