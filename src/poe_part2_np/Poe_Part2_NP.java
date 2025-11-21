/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package poe_part2_np;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author RC_Student_Lab
 */
public class Poe_Part2_NP {
    private static Scanner scanner = new Scanner(System.in);
    private static Login loginSystem = new Login();
    private static List<Message> messages = new ArrayList<>();
    private static boolean isLoggedIn = false;
    
    public static void main(String[] args) {
        System.out.println("Welcome to the Registration and Login System!");
        
        boolean running = true;
        
        while (running) {
            if (!isLoggedIn) {
                // using Scanner
                showAuthMenu();
            } else {
                //using JOptionPane after successful login
                showMessagingMenu();
            }
        }
        
        scanner.close();
    }
    
    private static void showAuthMenu() {
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
                isLoggedIn = loginUser();
                if (isLoggedIn) {
                    JOptionPane.showMessageDialog(null, "Welcome to QuickChat.", "QuickChat", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            case "3":
                System.out.println("Thank you for using the system. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
        }
    }
    
private static void showMessagingMenu() {
    String[] options = {"Send Messages", "Show recently sent messages", "Message Helper Functions", "Quit"};
    
    int choice = JOptionPane.showOptionDialog(
        null,
        "Welcome to QuickChat. Please choose an option:",
        "QuickChat Menu",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null,
        options,
        options[0]
    );
    
    switch (choice) {
        case 0: // Send Messages
            sendMessages();
            break;
        case 1: // Show recently sent messages
            showRecentMessages();
            break;
        case 2: // Message Helper Functions
            showMessageHelperMenu();
            break;
        case 3: // Quit
            JOptionPane.showMessageDialog(null, "Thank you for using QuickChat. Goodbye!", "Goodbye", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
            break;
        default:
            // If user closes the dialog,it will exit the program
            System.exit(0);
    }
}
private static void showMessageHelperMenu() {
    String[] options = {
        "Display sender/recipient of all sent messages",
        "Display longest sent message", 
        "Search message by ID",
        "Search messages by recipient",
        "Delete message by hash",
        "Display full report",
        "Back to main menu"
    };
    
    int choice = JOptionPane.showOptionDialog(
        null,
        "Message Helper Functions - Please choose an option:",
        "Message Helper",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null,
        options,
        options[0]
    );
    
    switch (choice) {
        case 0: // Display sender/recipient
            String result1 = MessageHelper.displaySenderRecipientOfAllSentMessages();
            JOptionPane.showMessageDialog(null, result1, "Sent Messages Summary", JOptionPane.INFORMATION_MESSAGE);
            break;
        case 1: // Longest message
            String result2 = MessageHelper.displayLongestSentMessage();
            JOptionPane.showMessageDialog(null, result2, "Longest Message", JOptionPane.INFORMATION_MESSAGE);
            break;
        case 2: // Search by ID
            String searchID = JOptionPane.showInputDialog(null, "Enter Message ID to search:", "Search by Message ID", JOptionPane.QUESTION_MESSAGE);
            if (searchID != null && !searchID.trim().isEmpty()) {
                String result3 = MessageHelper.searchMessageByID(searchID.trim());
                JOptionPane.showMessageDialog(null, result3, "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }
            break;
        case 3: // Search by recipient
            String recipient = JOptionPane.showInputDialog(null, "Enter recipient number to search:", "Search by Recipient", JOptionPane.QUESTION_MESSAGE);
            if (recipient != null && !recipient.trim().isEmpty()) {
                String result4 = MessageHelper.searchMessagesByRecipient(recipient.trim());
                JOptionPane.showMessageDialog(null, result4, "Recipient Messages", JOptionPane.INFORMATION_MESSAGE);
            }
            break;
        case 4: // Delete by hash
            String hash = JOptionPane.showInputDialog(null, "Enter message hash to delete:", "Delete by Hash", JOptionPane.QUESTION_MESSAGE);
            if (hash != null && !hash.trim().isEmpty()) {
                String result5 = MessageHelper.deleteMessageByHash(hash.trim());
                JOptionPane.showMessageDialog(null, result5, "Delete Result", JOptionPane.INFORMATION_MESSAGE);
            }
            break;
        case 5: // Full report
            String result6 = MessageHelper.displayFullReport();
            JOptionPane.showMessageDialog(null, result6, "Full Message Report", JOptionPane.INFORMATION_MESSAGE);
            break;
        case 6: // Back to main menu
            return;
        default:
            return;
    }
}
    
    private static void showRecentMessages() {
        String recentMessages = Message.printMessages();
        JOptionPane.showMessageDialog(null, recentMessages, "Recently Sent Messages", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static void sendMessages() {
        String numMessagesStr = JOptionPane.showInputDialog(null, 
            "How many messages do you wish to send?", 
            "Send Messages", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (numMessagesStr == null) {
            return; // User cancelled
        }
        
        int numMessages;
        try {
            numMessages = Integer.parseInt(numMessagesStr);
            if (numMessages <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int successfullySent = 0;
        for (int i = 0; i < numMessages; i++) {
            boolean messageSent = sendSingleMessage(i + 1, numMessages);
            if (messageSent) {
                successfullySent++;
            } else {
                
                break;
            }
        }
        
        // Display total messages sent
        JOptionPane.showMessageDialog(null, 
            String.format("Session Summary:\n\nMessages attempted: %d\nSuccessfully sent: %d\nTotal messages sent in this session: %d", 
                         numMessages, successfullySent, Message.returnTotalMessages()),
            "Session Summary", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static boolean sendSingleMessage(int currentMessage, int totalMessages) {
        Message message = new Message();
        
        // Get recipient cell number
        String recipient;
        boolean validRecipient = false;
        
        do {
            recipient = JOptionPane.showInputDialog(null,
                String.format("Message %d of %d\n\nEnter recipient cell number (must start with '27' and be 10 digits total):", currentMessage, totalMessages),
                "Recipient Details",
                JOptionPane.QUESTION_MESSAGE);
            
            if (recipient == null) {
                return false; // User cancelled
            }
            
           
            int recipientCheck = message.checkRecipientCell(recipient);
            if (recipientCheck == 1) {
                validRecipient = true;
                // Use the setter method
                message.setRecipient(recipient);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Invalid cell number format.\nPlease ensure it:\n- Starts with '27'\n- Has exactly 10 digits total\n- Contains only numbers\n\nExample: 27712345678", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } while (!validRecipient);
        
        // Get message content
        String messageContent;
        boolean validMessage = false;
        
        do {
            messageContent = JOptionPane.showInputDialog(null,
                String.format("Message %d of %d\n\nEnter your message (maximum 250 characters):", currentMessage, totalMessages),
                "Message Content",
                JOptionPane.QUESTION_MESSAGE);
            
            if (messageContent == null) {
                return false; 
            }
            
            if (messageContent.length() <= 250) {
                validMessage = true;
                
                message.setMessageText(messageContent);
            } else {
                JOptionPane.showMessageDialog(null, 
                    String.format("Message too long!\nPlease enter a message of less than 250 characters.\n\nCurrent length: %d characters", 
                                 messageContent.length()), 
                    "Message Too Long", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } while (!validMessage);
        
        // Show message options (send, store, disregard)
        String result = message.sentMessage();
        
        // Handle the result
        if (result.contains("sent successfully")) {
            messages.add(message);
            
            // Display message details using JOptionPane 
            String messageDetails = String.format(
                "Message Details:\n\nMessage ID: %s\nMessage Hash: %s\nRecipient: %s\nMessage: %s",
                message.getMessageID(),
                message.getMessageHash(),
                message.getRecipient(),
                message.getMessageText() // Changed from getMessageContent() to getMessageText()
            );
            
            JOptionPane.showMessageDialog(null, messageDetails, "Message Sent", JOptionPane.INFORMATION_MESSAGE);
        } else if (result.contains("stored")) {
            JOptionPane.showMessageDialog(null, result, "Message Stored", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, result, "Message Action", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return true;
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
        
        // Attempt registration
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
        
        // Attempt login
        boolean loginSuccess = loginSystem.loginUser(username, password);
        String loginMessage = loginSystem.returnLoginStatus(loginSuccess);
        
        System.out.println("\nLogin Result:");
        System.out.println(loginMessage);
        
        if (loginSuccess) {
            System.out.println("Login successful! Welcome to the system.");
        } else {
            System.out.println("Login failed. Please check your credentials and try again.");
        }
        
        return loginSuccess;
    }
}