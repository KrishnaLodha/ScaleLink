import os

def replace_in_file(path, old, new):
    with open(path, 'r') as f:
        content = f.read()
    content = content.replace(old, new)
    with open(path, 'w') as f:
        f.write(content)

# 1. UserAgentParser
replace_in_file('src/main/java/com/scalink/util/UserAgentParser.java', 'public record ParsedUserAgent', 'record ParsedUserAgent')

# 2. RateLimitFilter
rlf_old = """@Slf4j
@Profile("!test")
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {"""
rlf_new = """@org.springframework.context.annotation.Profile("!test")
public class RateLimitFilter extends org.springframework.web.filter.OncePerRequestFilter {
    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }"""
replace_in_file('src/main/java/com/scalink/ratelimit/RateLimitFilter.java', rlf_old, rlf_new)

# 3. RateLimitService
replace_in_file('src/main/java/com/scalink/ratelimit/RateLimitService.java', '@Slf4j', '')

# 4. GlobalExceptionHandler
replace_in_file('src/main/java/com/scalink/exception/GlobalExceptionHandler.java', '@Slf4j', '')

# 5. SecurityUser
su_old = """    public String getRole() {
        return role;
    }"""
su_new = """    public Long getId() { return id; }
    public String getEmail() { return email; }
"""
replace_in_file('src/main/java/com/scalink/security/SecurityUser.java', 'public class SecurityUser implements UserDetails {', 'public class SecurityUser implements UserDetails {\n' + su_new)

# 6. PagedResponse
pr_old = """    public static PagedResponseBuilder builder() { return new PagedResponseBuilder(); }
    public static class PagedResponseBuilder {"""
pr_new = """    public static <T> PagedResponseBuilder<T> builder() { return new PagedResponseBuilder<T>(); }
    public static class PagedResponseBuilder<T> {"""
replace_in_file('src/main/java/com/scalink/dto/response/PagedResponse.java', pr_old, pr_new)

# 7. ErrorResponse
er_old = """        public ErrorResponse build() { return new ErrorResponse(timestamp, status, error, message, path); }"""
er_new = """        private java.util.Map<String, String> validationErrors;
        public ErrorResponseBuilder validationErrors(java.util.Map<String, String> validationErrors) { this.validationErrors = validationErrors; return this; }
        public ErrorResponse build() { ErrorResponse e = new ErrorResponse(timestamp, status, error, message, path); e.setValidationErrors(validationErrors); return e; }"""
replace_in_file('src/main/java/com/scalink/exception/ErrorResponse.java', er_old, er_new)

# 8. RateLimitProperties
rlp_old = """@Data"""
rlp_new = """"""
replace_in_file('src/main/java/com/scalink/config/RateLimitProperties.java', '@Data', '')
with open('src/main/java/com/scalink/config/RateLimitProperties.java', 'a') as f:
    f.write("\n    public boolean isEnabled() { return enabled; }\n    public void setEnabled(boolean enabled) { this.enabled = enabled; }\n")

# 9. DailyClicksResponse
dcr = """package com.scalink.dto.response;

import java.time.LocalDate;
import java.util.List;

public class DailyClicksResponse {

    private Long urlId;
    private List<DailyCount> dailyCounts;

    public Long getUrlId() { return urlId; }
    public void setUrlId(Long urlId) { this.urlId = urlId; }
    public List<DailyCount> getDailyCounts() { return dailyCounts; }
    public void setDailyCounts(List<DailyCount> dailyCounts) { this.dailyCounts = dailyCounts; }

    public DailyClicksResponse() {}
    public DailyClicksResponse(Long urlId, List<DailyCount> dailyCounts) { this.urlId = urlId; this.dailyCounts = dailyCounts; }

    public static DailyClicksResponseBuilder builder() { return new DailyClicksResponseBuilder(); }

    public static class DailyClicksResponseBuilder {
        private Long urlId; private List<DailyCount> dailyCounts;
        public DailyClicksResponseBuilder urlId(Long urlId) { this.urlId = urlId; return this; }
        public DailyClicksResponseBuilder dailyCounts(List<DailyCount> dailyCounts) { this.dailyCounts = dailyCounts; return this; }
        public DailyClicksResponse build() { return new DailyClicksResponse(urlId, dailyCounts); }
    }

    public static class DailyCount {
        private LocalDate date;
        private long clicks;

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public long getClicks() { return clicks; }
        public void setClicks(long clicks) { this.clicks = clicks; }

        public DailyCount() {}
        public DailyCount(LocalDate date, long clicks) { this.date = date; this.clicks = clicks; }

        public static DailyCountBuilder builder() { return new DailyCountBuilder(); }

        public static class DailyCountBuilder {
            private LocalDate date; private long clicks;
            public DailyCountBuilder date(LocalDate date) { this.date = date; return this; }
            public DailyCountBuilder clicks(long clicks) { this.clicks = clicks; return this; }
            public DailyCount build() { return new DailyCount(date, clicks); }
        }
    }
}
"""
with open('src/main/java/com/scalink/dto/response/DailyClicksResponse.java', 'w') as f:
    f.write(dcr)

print("Done")
