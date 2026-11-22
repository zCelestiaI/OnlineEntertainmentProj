package com.example.java4main.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class CookieUtil {

    public static Cookie add(String name, String value, int hours, HttpServletResponse response) {
        // Encode giá trị để đảm bảo an toàn và hợp lệ cho cookie
        String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
        Cookie cookie = new Cookie(name, encodedValue);
        cookie.setMaxAge(hours * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return cookie;
    }

    public static String get(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    // Decode giá trị khi đọc từ cookie
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                }
            }
        }
        return "";
    }

    public static void remove(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0); // Đặt thời gian sống là 0 để xóa cookie
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
