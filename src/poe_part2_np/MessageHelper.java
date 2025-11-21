

package poe_part2_np;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * MessageHelper class to populate and manage message arrays
 */
public class MessageHelper {
    // Arrays as specified in requirements
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHash = new ArrayList<>();
    private static List<String> messageID = new ArrayList<>();
    
    /**
     * Populate all arrays with existing message data
     */
    public static void populateArrays() {
        clearArrays(); // Clear existing data first
        
        // Populate sent messages from Message class
        List<Message> allSentMessages = Message.getSentMessages();
        sentMessages.addAll(allSentMessages);
        
        // Populate stored messages from JSON file
        storedMessages = Message.loadStoredMessages();
        
        // Populate message hashes and IDs from both sent and stored messages
        for (Message msg : sentMessages) {
            if (msg.getMessageHash() != null) {
                messageHash.add(msg.getMessageHash());
            }
            if (msg.getMessageID() != null) {
                messageID.add(msg.getMessageID());
            }
        }
        
        for (Message msg : storedMessages) {
            if (msg.getMessageHash() != null && !messageHash.contains(msg.getMessageHash())) {
                messageHash.add(msg.getMessageHash());
            }
            if (msg.getMessageID() != null && !messageID.contains(msg.getMessageID())) {
                messageID.add(msg.getMessageID());
            }
        }
    }
    
    /**
     * Clear all arrays
     */
    private static void clearArrays() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHash.clear();
        messageID.clear();
    }
    
    // Array access methods
    public static List<Message> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }
    
    public static List<Message> getDisregardedMessages() {
        return new ArrayList<>(disregardedMessages);
    }
    
    public static List<Message> getStoredMessages() {
        return new ArrayList<>(storedMessages);
    }
    
    public static List<String> getMessageHash() {
        return new ArrayList<>(messageHash);
    }
    
    public static List<String> getMessageID() {
        return new ArrayList<>(messageID);
    }
    
    /**
     * a. Display the sender and recipient of all sent messages
     */
    public static String displaySenderRecipientOfAllSentMessages() {
        populateArrays(); // Ensure arrays are up to date
        StringBuilder result = new StringBuilder("Sender and Recipient of All Sent Messages:\n\n");
        
        if (sentMessages.isEmpty()) {
            return "No sent messages found.";
        }
        
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            result.append("Message ").append(i + 1).append(":\n")
                  .append("Recipient: ").append(msg.getRecipient()).append("\n\n");
        }
        
        return result.toString();
    }
    
    /**
     * b. Display the longest sent message
     */
    public static String displayLongestSentMessage() {
        populateArrays(); // Ensure arrays are up to date
        
        if (sentMessages.isEmpty()) {
            return "No sent messages found.";
        }
        
        Message longestMessage = null;
        int maxLength = 0;
        
        for (Message msg : sentMessages) {
            if (msg.getMessageText() != null && msg.getMessageText().length() > maxLength) {
                maxLength = msg.getMessageText().length();
                longestMessage = msg;
            }
        }
        
        if (longestMessage != null) {
            return String.format("Longest Sent Message:\n\nMessage ID: %s\nRecipient: %s\nMessage: %s\nLength: %d characters",
                    longestMessage.getMessageID(),
                    longestMessage.getRecipient(),
                    longestMessage.getMessageText(),
                    maxLength);
        }
        
        return "No valid sent messages found.";
    }
    
    /**
     * c. Search for a message ID and display the corresponding recipient and message
     */
    public static String searchMessageByID(String searchID) {
        populateArrays(); // Ensure arrays are up to date
        
        // Search in sent messages
        for (Message msg : sentMessages) {
            if (msg.getMessageID() != null && msg.getMessageID().equals(searchID)) {
                return String.format("Message Found in Sent Messages:\n\nMessage ID: %s\nRecipient: %s\nMessage: %s",
                        msg.getMessageID(),
                        msg.getRecipient(),
                        msg.getMessageText());
            }
        }
        
        // Search in stored messages
        for (Message msg : storedMessages) {
            if (msg.getMessageID() != null && msg.getMessageID().equals(searchID)) {
                return String.format("Message Found in Stored Messages:\n\nMessage ID: %s\nRecipient: %s\nMessage: %s",
                        msg.getMessageID(),
                        msg.getRecipient(),
                        msg.getMessageText());
            }
        }
        
        return "Message ID not found: " + searchID;
    }
    
    /**
     * d. Search for all the messages sent to a particular recipient
     */
    public static String searchMessagesByRecipient(String recipient) {
        populateArrays(); // Ensure arrays are up to date
        StringBuilder result = new StringBuilder();
        result.append("Messages sent to: ").append(recipient).append("\n\n");
        
        boolean found = false;
        
        // Search in sent messages
        for (Message msg : sentMessages) {
            if (msg.getRecipient() != null && msg.getRecipient().equals(recipient)) {
                result.append("SENT MESSAGE:\n")
                      .append("Message ID: ").append(msg.getMessageID()).append("\n")
                      .append("Message: ").append(msg.getMessageText()).append("\n\n");
                found = true;
            }
        }
        
        if (!found) {
            result.append("No messages found for recipient: ").append(recipient);
        }
        
        return result.toString();
    }
    
    /**
     * e. Delete a message using the message hash
     */
    public static String deleteMessageByHash(String hashToDelete) {
        populateArrays(); // Ensure arrays are up to date
        
        // Search and remove from sent messages
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            if (msg.getMessageHash() != null && msg.getMessageHash().equals(hashToDelete)) {
                sentMessages.remove(i);
                messageHash.remove(hashToDelete);
                messageID.remove(msg.getMessageID());
                return "Message with hash '" + hashToDelete + "' deleted successfully from sent messages.";
            }
        }
        
        // Search and remove from stored messages
        for (int i = 0; i < storedMessages.size(); i++) {
            Message msg = storedMessages.get(i);
            if (msg.getMessageHash() != null && msg.getMessageHash().equals(hashToDelete)) {
                storedMessages.remove(i);
                messageHash.remove(hashToDelete);
                messageID.remove(msg.getMessageID());
                return "Message with hash '" + hashToDelete + "' deleted successfully from stored messages.";
            }
        }
        
        return "Message with hash '" + hashToDelete + "' not found.";
    }
    
    /**
     * f. Display a report that lists the full details of all the sent messages
     */
    public static String displayFullReport() {
        populateArrays(); // Ensure arrays are up to date
        StringBuilder result = new StringBuilder("FULL MESSAGE REPORT\n\n");
        
        result.append("SENT MESSAGES:\n");
        result.append("==============\n");
        
        if (sentMessages.isEmpty()) {
            result.append("No sent messages.\n\n");
        } else {
            for (int i = 0; i < sentMessages.size(); i++) {
                Message msg = sentMessages.get(i);
                result.append("Message ").append(i + 1).append(":\n")
                      .append("  Message ID: ").append(msg.getMessageID()).append("\n")
                      .append("  Message Hash: ").append(msg.getMessageHash()).append("\n")
                      .append("  Recipient: ").append(msg.getRecipient()).append("\n")
                      .append("  Message: ").append(msg.getMessageText()).append("\n\n");
            }
        }
        
        result.append("STORED MESSAGES:\n");
        result.append("================\n");
        
        if (storedMessages.isEmpty()) {
            result.append("No stored messages.\n\n");
        } else {
            for (int i = 0; i < storedMessages.size(); i++) {
                Message msg = storedMessages.get(i);
                result.append("Message ").append(i + 1).append(":\n")
                      .append("  Message ID: ").append(msg.getMessageID()).append("\n")
                      .append("  Message Hash: ").append(msg.getMessageHash()).append("\n")
                      .append("  Recipient: ").append(msg.getRecipient()).append("\n")
                      .append("  Message: ").append(msg.getMessageText()).append("\n\n");
            }
        }
        
        result.append("ARRAY STATISTICS:\n");
        result.append("=================\n");
        result.append("Total Sent Messages: ").append(sentMessages.size()).append("\n");
        result.append("Total Stored Messages: ").append(storedMessages.size()).append("\n");
        result.append("Total Message Hashes: ").append(messageHash.size()).append("\n");
        result.append("Total Message IDs: ").append(messageID.size()).append("\n");
        
        return result.toString();
    }
    
    /**
     * Add a message to disregarded messages
     */
    public static void addToDisregardedMessages(Message message) {
        if (message != null) {
            disregardedMessages.add(message);
        }
    }
    
    /**
     * Get array statistics for reporting
     */
    public static String getArrayStatistics() {
        populateArrays(); // Ensure arrays are up to date
        
        return String.format("Array Statistics:\n\n" +
                "Sent Messages: %d\n" +
                "Disregarded Messages: %d\n" +
                "Stored Messages: %d\n" +
                "Message Hashes: %d\n" +
                "Message IDs: %d",
                sentMessages.size(),
                disregardedMessages.size(),
                storedMessages.size(),
                messageHash.size(),
                messageID.size());
    }
}