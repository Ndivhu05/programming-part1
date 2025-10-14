/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package poe_part2_np;

import java.util.regex.Pattern;

/**
 *
 * @author RC_Student_lab
 */
public class Login {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String cellPhoneNumber;
    private boolean isRegistered = false;
    
    // Constructor
    public Login() {
        // No scanner here - input handling is in Main class
    }
    
    // Method to check username format
    public boolean checkUserName() {
        return username != null && 
               username.contains("_") && 
               username.length() <= 5;
    }
    
    // Method to check password complexity
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = !password.equals(password.toLowerCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        return hasUpperCase && hasDigit && hasSpecialChar;
    }
    
    // Method to check cell phone number format
    // AI Tool Used: ChatGPT (OpenAI)
    // Reference: OpenAI. (2023). ChatGPT (September 25 version) [Large language model]. https://chat.openai.com
    public boolean checkCellPhoneNumber() {
        if (cellPhoneNumber == null) {
            return false;
        }
        
        // Regular expression for South African cell phone number with international code
        // Format: +27 followed by 9 digits (total 11 characters including +)
        // or 27 followed by 9 digits (total 10 characters)
        String regex = "^(\\+27|27)[0-9]{9}$";
        return Pattern.matches(regex, cellPhoneNumber);
    }
    
    // Method to register user and return appropriate message
    public String registerUser() {
        StringBuilder result = new StringBuilder();
        
        boolean usernameValid = checkUserName();
        boolean passwordValid = checkPasswordComplexity();
        boolean phoneValid = checkCellPhoneNumber();
        
        if (!usernameValid) {
            result.append("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.\n");
        } else {
            result.append("Username successfully captured.\n");
        }
        
        if (!passwordValid) {
            result.append("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.\n");
        } else {
            result.append("Password successfully captured.\n");
        }
        
        if (!phoneValid) {
            result.append("Cell phone number incorrectly formatted or does not contain international code.\n");
        } else {
            result.append("Cell phone number successfully added.\n");
        }
        
        // Check if all validations passed
        if (usernameValid && passwordValid && phoneValid) {
            isRegistered = true;
            result.append("User registered successfully!");
        } else {
            result.append("Registration failed. Please correct the errors above.");
        }
        
        return result.toString();
    }
    
    // Method to verify login credentials
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (!isRegistered) {
            return false;
        }
        
        return enteredUsername != null && 
               enteredPassword != null &&
               enteredUsername.equals(this.username) && 
               enteredPassword.equals(this.password);
    }
    
    // Method to return login status message
    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
    
    // Getters and setters
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }
    
    public boolean isRegistered() {
        return isRegistered;
    }
}