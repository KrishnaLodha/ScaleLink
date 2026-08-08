import os

with open('src/main/java/com/scalink/service/RedirectService.java', 'r') as f:
    rs = f.read()
rs = rs.replace("import com.scalink.util.ParsedUserAgent;", "import com.scalink.util.ParsedUserAgent;\nimport com.scalink.util.ReservedPaths;")
with open('src/main/java/com/scalink/service/RedirectService.java', 'w') as f:
    f.write(rs)

ua_content = """package com.scalink.util;

public record ParsedUserAgent(String browser, String device, String operatingSystem) {

    public static ParsedUserAgent parse(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return new ParsedUserAgent("Unknown", "Unknown", "Unknown");
        }
        String ua = userAgent;
        String browser = detectBrowser(ua);
        String os = detectOperatingSystem(ua);
        String device = detectDevice(ua);
        return new ParsedUserAgent(browser, device, os);
    }

    private static String detectBrowser(String ua) {
        if (ua.contains("Edg/")) return "Edge";
        if (ua.contains("Chrome/") && !ua.contains("Edg/")) return "Chrome";
        if (ua.contains("Firefox/")) return "Firefox";
        if (ua.contains("Safari/") && !ua.contains("Chrome/")) return "Safari";
        if (ua.contains("OPR/") || ua.contains("Opera")) return "Opera";
        return "Other";
    }

    private static String detectOperatingSystem(String ua) {
        if (ua.contains("Windows")) return "Windows";
        if (ua.contains("Mac OS X")) return "macOS";
        if (ua.contains("Android")) return "Android";
        if (ua.contains("iPhone") || ua.contains("iPad")) return "iOS";
        if (ua.contains("Linux")) return "Linux";
        return "Other";
    }

    private static String detectDevice(String ua) {
        if (ua.contains("Mobile") || ua.contains("Android") || ua.contains("iPhone")) {
            return "Mobile";
        }
        if (ua.contains("iPad") || ua.contains("Tablet")) {
            return "Tablet";
        }
        return "Desktop";
    }
}
"""

with open('src/main/java/com/scalink/util/ParsedUserAgent.java', 'w') as f:
    f.write(ua_content)

print("Done")
