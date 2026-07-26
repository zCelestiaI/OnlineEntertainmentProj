package com.example.java4main.utils;

public class EmailUtil {

    private EmailUtil() {
    }

    public static void send(String to,
                            String subject,
                            String content) {

        // TODO: JavaMail sẽ triển khai sau
        System.out.println("To      : " + to);
        System.out.println("Subject : " + subject);
        System.out.println("Content : " + content);
    }
}