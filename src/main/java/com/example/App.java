package com.example;

import java.time.LocalDateTime;

public class App {
    public static void main(String[] args) {
        System.out.println("=== Java CI/CD Demo ===");
        System.out.println("Current time: " + LocalDateTime.now());
        System.out.println("Running inside a simple CI/CD pipeline build.");
    }
}
