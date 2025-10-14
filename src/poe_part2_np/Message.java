/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */

package poe_part2_np;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author RC_Student_Lab
 */
public class Message {
    private String messageID;
    private String messageHash;
    private String recipient;
    private String messageText;
    private static int totalMessagesSent = 0;
    private static int messageCounter = 0;
    private static final List<Message> sentMessages = new ArrayList<>();

    public Message() {
        this.messageID = generateMessageID();
        messageCounter++;
    }

    // Getters
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public static int returnTotalMessages() { return totalMessagesSent; }

    // Generate random 10-digit message ID
    private String generateMessageID() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // Check if message ID is valid (not more than 10 characters)
    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() == 10;
    }

    // Check recipient cell number format
    public int checkRecipientCell(String recipient) {
        if (recipient == null || recipient.length() > 10 || !recipient.startsWith("27")) {
            return -1; // Invalid format
        }
        
        // Check if it's all digits after "27"
        String digits = recipient.substring(2);
        if (!digits.matches("\\d+") || digits.length() != 8) {
            return -1;
        }
        
        return 1; // Valid format
    }

    // Create message hash according to specification
    public String createMessageHash() {
        if (messageText == null || messageText.trim().isEmpty()) {
            return "00:0:EMPTY";
        }
        
        // First two numbers of message ID
        String firstTwoDigits = messageID.substring(0, 2);
        
        // The number of the message (counter)
        int messageNumber = messageCounter;
        
        // First and last words in the message
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        // Combine and convert to uppercase
        this.messageHash = firstTwoDigits + ":" + messageNumber + ":" + 
                          firstWord.toUpperCase() + lastWord.toUpperCase();
        return this.messageHash;
    }

    // Handle message sending options
    public String sentMessage() {
        String[] options = {"Send Message", "Disregard Message", "Store Message to send later"};
        int choice = JOptionPane.showOptionDialog(
            null,
            "What would you like to do with this message?",
            "Message Options",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0: // Send Message
                totalMessagesSent++;
                sentMessages.add(this);
                return "Message sent successfully!";
            case 1: // Disregard Message
                return "Message disregarded.";
            case 2: // Store Message
                return storeMessage();
            default:
                return "Message action cancelled.";
        }
    }

    // Print all sent messages
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        
        StringBuilder sb = new StringBuilder("Recently Sent Messages:\n\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            sb.append("Message ").append(i + 1).append(":\n")
              .append("Message ID: ").append(msg.getMessageID()).append("\n")
              .append("Message Hash: ").append(msg.getMessageHash()).append("\n")
              .append("Recipient: ").append(msg.getRecipient()).append("\n")
              .append("Message: ").append(msg.getMessageText()).append("\n\n");
        }
        return sb.toString();
    }

    // Store message in JSON file
    public String storeMessage() {
        try {
            // Read existing messages from file
            JSONArray messagesArray;
            if (Files.exists(Paths.get("stored_messages.json"))) {
                String content = new String(Files.readAllBytes(Paths.get("stored_messages.json")));
                messagesArray = new JSONArray(content);
            } else {
                messagesArray = new JSONArray();
            }
            
            // Create JSON object for current message
            JSONObject messageJson = new JSONObject();
            messageJson.put("messageID", this.messageID);
            messageJson.put("messageHash", this.messageHash);
            messageJson.put("recipient", this.recipient);
            messageJson.put("messageText", this.messageText);
            
            // Add to array and write back to file
            messagesArray.put(messageJson);
            
            try (FileWriter file = new FileWriter("stored_messages.json")) {
                file.write(messagesArray.toString(4)); // Pretty print with 4 spaces indentation
            }
            
            return "Message stored successfully in JSON file!";
            
        } catch (IOException e) {
            return "Error storing message: " + e.getMessage();
        }
    }

    // Set recipient with validation
    public boolean setRecipient(String recipient) {
        int checkResult = checkRecipientCell(recipient);
        if (checkResult == 1) {
            this.recipient = recipient;
            return true;
        }
        return false;
    }

    // Set message text with validation (max 250 characters)
    public boolean setMessageText(String messageText) {
        if (messageText != null && messageText.length() <= 250) {
            this.messageText = messageText;
            // Auto-create hash when message text is set
            createMessageHash();
            return true;
        }
        return false;
    }

    // Get formatted message details for display
    public String getFormattedDetails() {
        return String.format(
            "Message Details:\n\nMessage ID: %s\nMessage Hash: %s\nRecipient: %s\nMessage: %s",
            messageID, messageHash, recipient, messageText
        );
    }

    // Static method to get total number of messages created
    public static int getMessageCounter() {
        return messageCounter;
    }

    // Static method to get all sent messages
    public static List<Message> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }

    // Load stored messages from JSON file
    public static List<Message> loadStoredMessages() {
        List<Message> storedMessages = new ArrayList<>();
        try {
            if (Files.exists(Paths.get("stored_messages.json"))) {
                String content = new String(Files.readAllBytes(Paths.get("stored_messages.json")));
                JSONArray messagesArray = new JSONArray(content);
                
                for (int i = 0; i < messagesArray.length(); i++) {
                    JSONObject jsonObject = messagesArray.getJSONObject(i);
                    Message message = new Message();
                    message.messageID = jsonObject.getString("messageID");
                    message.messageHash = jsonObject.getString("messageHash");
                    message.recipient = jsonObject.getString("recipient");
                    message.messageText = jsonObject.getString("messageText");
                    storedMessages.add(message);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error loading stored messages: " + e.getMessage(), 
                "Load Error", 
                JOptionPane.WARNING_MESSAGE);
        }
        return storedMessages;
    }

  

  
}