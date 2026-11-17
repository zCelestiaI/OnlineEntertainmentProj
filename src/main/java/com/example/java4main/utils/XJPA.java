package com.example.java4main.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Dual-database JDBC configuration for the Java 4 SQL Server project and
 * the free, temporary PostgreSQL demo hosted off-machine.
 *
 * Environment variables:
 * DB_URL, DB_USER, DB_PASSWORD (mandatory)
 * DB_SCHEMA_MODE (optional: validate by default; update for *new* demo DB only)
 */
public final class XJPA {
    private static final EntityManagerFactory FACTORY;

    static {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (url == null || url.isBlank() || user == null || user.isBlank() || password == null) {
            throw new IllegalStateException("DB_URL, DB_USER and DB_PASSWORD must all be configured");
        }

        Map<String, Object> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.url", url);
        props.put("jakarta.persistence.jdbc.user", user);
        props.put("jakarta.persistence.jdbc.password", password);

        if (url.startsWith("jdbc:postgresql:")) {
            props.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        } else if (url.startsWith("jdbc:sqlserver:")) {
            props.put("jakarta.persistence.jdbc.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
            props.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        } else {
            throw new IllegalArgumentException("Only jdbc:postgresql: and jdbc:sqlserver: are supported");
        }

        String mode = System.getenv("DB_SCHEMA_MODE");
        if (mode == null || mode.isBlank()) mode = "validate";
        if (!mode.equals("validate") && !mode.equals("update")) {
            throw new IllegalArgumentException("DB_SCHEMA_MODE must be validate or update");
        }
        props.put("hibernate.hbm2ddl.auto", mode);

        FACTORY = Persistence.createEntityManagerFactory("PolyOE", props);
    }

    private XJPA() {}

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void shutdown() {
        if (FACTORY.isOpen()) FACTORY.close();
    }
}
