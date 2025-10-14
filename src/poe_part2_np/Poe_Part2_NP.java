/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package poe_part2_np;

import java.util.Scanner;

/**
 *
 * @author RC_Student_lab
 */
public class Poe_Part2_NP {
    private static Scanner scanner = new Scanner(System.in);
    private static Login loginSystem = new Login();
    
    public static void main(String[] args) {
        System.out.println("Welcome to the Registration and Login System!");
        
        boolean running = true;
        boolean loggedIn = false;
        
        while (running && !loggedIn) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    loggedIn = loginUser();
                    if (loggedIn) {
                        running = false; // Stop the program after successful login
                    }
                    break;
                case "3":
                    System.out.println("Thank you for using the system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        
        scanner.close();
    }
    
    private static void registerUser() {
        System.out.println("\n***REGISTRATION***");
        
        // Get user input
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter username (must contain _ and be <= 5 characters): ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password (>= 8 chars, capital letter, number, special char): ");
        String password = scanner.nextLine();
        
        System.out.print("Enter South African cell phone (with international code e.g., +27XXXXXXXXX): ");
        String cellPhone = scanner.nextLine();
        
        // Set user data in Login system
        loginSystem.setFirstName(firstName);
        loginSystem.setLastName(lastName);
        loginSystem.setUsername(username);
        loginSystem.setPassword(password);
        loginSystem.setCellPhoneNumber(cellPhone);
        
        // Attempt registration using Login class methods
        String registrationResult = loginSystem.registerUser();
        System.out.println("\nRegistration Result:");
        System.out.println(registrationResult);
        
        if (registrationResult.contains("User registered successfully")) {
            System.out.println("You can now login with your credentials.");
        } else {
            System.out.println("Please try registration again with correct formatting.");
        }
    }
    
    private static boolean loginUser() {
        System.out.println("\n***LOGIN***");
        
        // Check if user is registered first
        if (!loginSystem.isRegistered()) {
            System.out.println("No user registered yet. Please register first.");
            return false;
        }
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        // Attempt login using Login class methods
        boolean loginSuccess = loginSystem.loginUser(username, password);
        String loginMessage = loginSystem.returnLoginStatus(loginSuccess);
        
        System.out.println("\nLogin Result:");
        System.out.println(loginMessage);
        
        if (loginSuccess) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed. Please check your credentials and try again.");
        }
        
        return loginSuccess;
    }
}