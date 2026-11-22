package com.example.java4main.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

/** Database credentials are supplied through environment variables. */
public class XJPA {
    private static final EntityManagerFactory factory;

    static {
        Map<String, Object> overrides = new HashMap<>();
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (url == null || url.isBlank() || user == null || user.isBlank() || password == null) {
            throw new IllegalStateException("Set DB_URL, DB_USER and DB_PASSWORD before starting the application");
        }
        overrides.put("jakarta.persistence.jdbc.url", url);
        overrides.put("jakarta.persistence.jdbc.user", user);
        overrides.put("jakarta.persistence.jdbc.password", password);
        factory = Persistence.createEntityManagerFactory("PolyOE", overrides);
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void shutdown() {
        factory.close();
    }
}
