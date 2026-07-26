package com.example.java4main.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class XJPA {
    private static EntityManagerFactory factory;
    
    static {
        try {
            factory = Persistence.createEntityManagerFactory("PolyOE");
        } catch (Exception e) {
            System.err.println("LỖI KHỞI TẠO ENTITY MANAGER FACTORY: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static void shutdown(){
        if(factory != null){
            factory.close();
        }
    }
    public static EntityManager getEntityManager(){
        return factory.createEntityManager();
    }
}
