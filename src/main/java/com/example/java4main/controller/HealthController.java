package com.example.java4main.controller;

import com.example.java4main.utils.XJPA;
import jakarta.persistence.EntityManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/health")
public class HealthController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.setHeader("Cache-Control", "no-store");
        try (EntityManager em = XJPA.getEntityManager()) {
            em.createNativeQuery("SELECT 1").getSingleResult();
            resp.setStatus(200);
            resp.getWriter().write("{\"status\":\"ok\",\"database\":\"connected\"}");
        } catch (Exception e) {
            getServletContext().log("Health check failed", e);
            resp.setStatus(503);
            resp.getWriter().write("{\"status\":\"unavailable\",\"database\":\"unavailable\"}");
        }
    }
}
