package com.example.java4main.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    private SessionUtil() {
    }

    public static void set(HttpServletRequest request, String name, Object value) {
        request.getSession().setAttribute(name, value);
    }

    public static Object get(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(false);
        return session == null ? null : session.getAttribute(name);
    }

    public static void remove(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(name);
        }
    }

    public static void invalidate(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}