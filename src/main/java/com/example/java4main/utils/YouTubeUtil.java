package com.example.java4main.utils;

import java.net.URI;
import java.util.regex.Pattern;

/**
 * Restrict video sources to genuine HTTPS YouTube links.
 * No YouTube Data API credentials are needed for iframe and thumbnail URLs.
 */
public final class YouTubeUtil {
    private static final Pattern ID = Pattern.compile("[A-Za-z0-9_-]{11}");
    private YouTubeUtil() {}

    public static String extractId(String input) {
        if (input == null || input.isBlank()) return null;
        try {
            URI uri = URI.create(input.trim());
            if (!"https".equalsIgnoreCase(uri.getScheme())) return null;
            String host = uri.getHost();
            if (host == null || uri.getUserInfo() != null) return null;
            host = host.toLowerCase(java.util.Locale.ROOT);
            String path = uri.getPath();
            if (path == null) return null;
            String value = null;
            if ("youtu.be".equals(host) || "www.youtu.be".equals(host)) {
                value = segment(path, 0);
            } else if ("youtube.com".equals(host) || "www.youtube.com".equals(host)
                    || "m.youtube.com".equals(host) || "www.youtube-nocookie.com".equals(host)) {
                if ("/watch".equals(path) && uri.getRawQuery() != null) {
                    for (String part : uri.getRawQuery().split("&")) {
                        if (part.startsWith("v=")) { value = part.substring(2); break; }
                    }
                } else if (path.startsWith("/shorts/") || path.startsWith("/embed/")
                        || path.startsWith("/live/")) {
                    value = segment(path, 1);
                }
            }
            return value != null && ID.matcher(value).matches() ? value : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String segment(String path, int index) {
        String[] parts = path.replaceFirst("^/", "").split("/");
        return parts.length > index ? parts[index] : null;
    }

    public static String canonicalUrl(String id) {
        return "https://www.youtube.com/watch?v=" + id;
    }

    public static String thumbnailUrl(String id) {
        return "https://img.youtube.com/vi/" + id + "/hqdefault.jpg";
    }
}
