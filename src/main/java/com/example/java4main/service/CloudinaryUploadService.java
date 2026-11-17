package com.example.java4main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

/**
 * Server-side signed uploads to Cloudinary (no files stored on ephemeral Tomcat disk).
 * Supports small MVP video files; production uploads should use browser-to-cloud chunking.
 */
public class CloudinaryUploadService {
    private static final int MAX_SIZE = 15 * 1024 * 1024;
    private static final ObjectMapper JSON = new ObjectMapper();

    public record UploadedMedia(String url, String thumbnailUrl) {}

    public static boolean configured() {
        return valid(System.getenv("CLOUDINARY_CLOUD_NAME"))
                && valid(System.getenv("CLOUDINARY_API_KEY"))
                && valid(System.getenv("CLOUDINARY_API_SECRET"));
    }

    private static boolean valid(String s) { return s != null && !s.isBlank(); }

    public UploadedMedia upload(Part part) throws IOException, InterruptedException {
        if (!configured()) throw new IllegalStateException(
                "Chua cau hinh CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, CLOUDINARY_API_SECRET");
        if (part == null || part.getSize() == 0 || part.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("Chi ho tro video tu 1 byte den 15 MB");
        }
        String mime = part.getContentType();
        String ext;
        if ("video/mp4".equalsIgnoreCase(mime)) ext = "mp4";
        else if ("video/webm".equalsIgnoreCase(mime)) ext = "webm";
        else if ("video/quicktime".equalsIgnoreCase(mime)) ext = "mov";
        else throw new IllegalArgumentException("Chi ho tro video MP4, WebM hoac MOV");

        String cloud = System.getenv("CLOUDINARY_CLOUD_NAME");
        if (!cloud.matches("[A-Za-z0-9_-]+")) throw new IllegalStateException("Invalid cloud name");
        String key = System.getenv("CLOUDINARY_API_KEY");
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        String signature = signature("timestamp=" + timestamp, System.getenv("CLOUDINARY_API_SECRET"));
        String boundary = "OE_" + UUID.randomUUID().toString().replace("-", "");
        ByteArrayOutputStream data = new ByteArrayOutputStream((int)part.getSize() + 4096);
        addField(data, boundary, "timestamp", timestamp);
        addField(data, boundary, "api_key", key);
        addField(data, boundary, "signature", signature);
        data.write(("--" + boundary + "\r\nContent-Disposition: form-data; name=\"file\"; filename=\"video." +
                ext + "\"\r\nContent-Type: " + mime + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        try (var input = part.getInputStream()) { input.transferTo(data); }
        data.write(("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.cloudinary.com/v1_1/" + cloud + "/video/upload"))
                .timeout(Duration.ofSeconds(120))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(data.toByteArray())).build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Cloudinary upload HTTP " + response.statusCode()
                    + ": " + response.body().substring(0, Math.min(160, response.body().length())));
        }
        JsonNode result = JSON.readTree(response.body());
        String url = result.path("secure_url").asText("");
        if (!url.startsWith("https://res.cloudinary.com/" + cloud + "/video/upload/")) {
            throw new IOException("Cloudinary did not return a valid video URL");
        }
        String poster = url.replace("/video/upload/", "/video/upload/so_0/");
        int extension = poster.lastIndexOf('.');
        if (extension > poster.lastIndexOf('/')) poster = poster.substring(0, extension) + ".jpg";
        else poster = null;
        if (poster != null && poster.length() > 255) poster = null;
        return new UploadedMedia(url, poster);
    }

    private static String signature(String params, String secret) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-1")
                    .digest((params + secret).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (Exception e) { throw new IllegalStateException(e); }
    }

    private static void addField(ByteArrayOutputStream out, String b, String name, String value)
            throws IOException {
        out.write(("--" + b + "\r\nContent-Disposition: form-data; name=\"" + name
                + "\"\r\n\r\n" + value + "\r\n").getBytes(StandardCharsets.UTF_8));
    }
}
