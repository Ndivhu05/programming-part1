package poe_part2_np;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


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
import org.json.JSONTokener;
/**
 *
 * @author RC_Student_Lab
 */
public class Message {

    
    private final String messageID;
    private final String messageHash;
    private final String recipient;
    private final String messageText;
    private static int numMessagesCreated = 0;
    private static final List<String> sentMessages = new ArrayList<>();
    private static final List<String> disregardedMessages = new ArrayList<>();

    public Message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        numMessagesCreated++;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public static int returnTotalMessages() {
        return numMessagesCreated;
    }

    private String generateMessageID() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public boolean checkMessageID() {
        return this.messageID.length() <= 10;
    }

    public int checkRecipientCell() {
        if (this.recipient == null || !this.recipient.startsWith("+") || this.recipient.length() != 11) {
            return 0;
        }
        try {
            Long.parseLong(this.recipient.substring(1));
            return 1;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String createMessageHash() {
        if (messageText == null || messageText.isEmpty()) {
            return (messageID.substring(0, Math.min(2, messageID.length())) + ":" + 0 + "::").toUpperCase().replaceAll("\\s+", "");
        } else {
            String firstTwo = messageID.substring(0, Math.min(2, messageID.length()));
            int messageLength = messageText.length();
            
            String[] words = messageText.split("\\s+");
            String firstWord = words.length > 0 ? words[0] : "";
            String lastWord = words.length > 0 ? words[words.length - 1] : "";
            
            return (firstTwo + ":" + messageLength + ":" + firstWord.toUpperCase() + lastWord.toUpperCase()).replaceAll("\\s+", "");
        }
    }

    public String sendMessage() {
        String[] options = {"Send Message", "Disregard Message", "Store Message to send later"};
        int choice = JOptionPane.showOptionDialog(
            null,
            "Do you want to:",
            "Message Action",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0:
                sentMessages.add(this.toString());
                return "Message successfully sent.";
            case 1:
                disregardedMessages.add(this.toString());
                return "Message discarded.";
            case 2:
                storeMessage();
                return "Message will be stored to send later.";
            default:
                return "Message action cancelled.";
        }
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet in this session.";
        }
        StringBuilder sb = new StringBuilder("List of sent messages:\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            sb.append((i + 1)).append(". ").append(sentMessages.get(i)).append("\n");
        }
        return sb.toString();
    }

    public static String printDisregardedMessages() {
        if (disregardedMessages.isEmpty()) {
            return "No messages have been disregarded yet in this session.";
        }
        StringBuilder sb = new StringBuilder("List of disregarded messages:\n");
        for (int i = 0; i < disregardedMessages.size(); i++) {
            sb.append((i + 1)).append(". ").append(disregardedMessages.get(i)).append("\n");
        }
        return sb.toString();
    }

    public void storeMessage() {
        JSONObject messageJson = new JSONObject();
        messageJson.put("messageID", this.messageID);
        messageJson.put("recipient", this.recipient);
        messageJson.put("messageText", this.messageText);
        messageJson.put("messageHash", this.messageHash);

        try (FileWriter file = new FileWriter("stored_messages.json", true)) {
            file.write(messageJson.toString() + System.lineSeparator());
            JOptionPane.showMessageDialog(null, "Message stored to stored_messages.json", "Storage Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage(), "Storage Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static List<String> loadStoredMessages() {
        List<String> loadedMessages = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("stored_messages.json"));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(new JSONTokener(line));
                    String messageID = jsonObject.getString("messageID");
                    String recipient = jsonObject.getString("recipient");
                    String messageText = jsonObject.getString("messageText");
                    String messageHash = jsonObject.getString("messageHash");
                    loadedMessages.add("ID: " + messageID + ", Hash: " + messageHash + ", Recipient: " + recipient + ", Message: " + messageText);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading stored messages: " + e.getMessage() + "\n(File might not exist or be empty)", "Load Error", JOptionPane.WARNING_MESSAGE);
        } catch (org.json.JSONException e) {
            JOptionPane.showMessageDialog(null, "Error parsing stored messages JSON: " + e.getMessage() + "\n(File might contain malformed JSON)", "JSON Parse Error", JOptionPane.ERROR_MESSAGE);
        }
        return loadedMessages;
    }

    public static String searchMessagesByRecipient(String targetRecipient) {
        StringBuilder sb = new StringBuilder("Messages sent to " + targetRecipient + ":\n");
        boolean found = false;
        for (String msg : sentMessages) {
            if (msg.contains("Recipient: " + targetRecipient)) {
                sb.append("- ").append(msg).append("\n");
                found = true;
            }
        }
        if (!found) {
            return "No messages found for recipient: " + targetRecipient;
        }
        return sb.toString();
    }

    public static String searchMessageByID(String messageId) {
        for (String msg : sentMessages) {
            if (msg.contains("ID: " + messageId)) {
                return "Found in sent messages: " + msg;
            }
        }

        List<String> stored = loadStoredMessages();
        for (String msg : stored) {
            if (msg.contains("ID: " + messageId)) {
                return "Found in stored messages: " + msg;
            }
        }
        return "Message with ID '" + messageId + "' not found.";
    }

    public static String findLongestMessage() {
        String longestMsg = "";
        int maxLength = 0;

        for (String msg : sentMessages) {
            int msgStartIndex = msg.indexOf("Message: ") + "Message: ".length();
            String currentMessageText = msg.substring(msgStartIndex);
            if (currentMessageText.length() > maxLength) {
                maxLength = currentMessageText.length();
                longestMsg = currentMessageText;
            }
        }

        List<String> stored = loadStoredMessages();
        for (String msg : stored) {
            int msgStartIndex = msg.indexOf("Message: ") + "Message: ".length();
            String currentMessageText = msg.substring(msgStartIndex);
            if (currentMessageText.length() > maxLength) {
                maxLength = currentMessageText.length();
                longestMsg = currentMessageText;
            }
        }

        if (longestMsg.isEmpty()) {
            return "No messages available to determine the longest.";
        }
        return "Longest message (" + maxLength + " characters): " + longestMsg;
    }

    public static String deleteMessageByHash(String messageHash) {
        List<String> allLines = new ArrayList<>();
        boolean foundAndDeleted = false;
        try {
            allLines = Files.readAllLines(Paths.get("stored_messages.json"));
            List<String> updatedLines = new ArrayList<>();

            for (String line : allLines) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(new JSONTokener(line));
                    String currentHash = jsonObject.getString("messageHash");
                    if (!currentHash.equals(messageHash)) {
                        updatedLines.add(line);
                    } else {
                        foundAndDeleted = true;
                    }
                }
            }

            if (foundAndDeleted) {
                try (FileWriter file = new FileWriter("stored_messages.json", false)) {
                    for (String line : updatedLines) {
                        file.write(line + System.lineSeparator());
                    }
                }
                return "Message with hash '" + messageHash + "' successfully deleted.";
            } else {
                return "Message with hash '" + messageHash + "' not found in stored messages.";
            }

        } catch (IOException e) {
            return "Error accessing stored messages file for deletion: " + e.getMessage();
        } catch (org.json.JSONException e) {
            return "Error parsing stored messages JSON during deletion: " + e.getMessage();
        }
    }

    public static String generateFullReport() {
        StringBuilder report = new StringBuilder("--- QuickChat Full Message Report ---\n\n");

        report.append("Total Messages Created (in session): ").append(numMessagesCreated).append("\n\n");

        report.append("--- Sent Messages ---\n");
        if (sentMessages.isEmpty()) {
            report.append("No messages sent in this session.\n");
        } else {
            for (String msg : sentMessages) {
                report.append(msg).append("\n");
            }
        }
        report.append("\n");

        report.append("--- Disregarded Messages ---\n");
        if (disregardedMessages.isEmpty()) {
            report.append("No messages disregarded in this session.\n");
        } else {
            for (String msg : disregardedMessages) {
                report.append(msg).append("\n");
            }
        }
        report.append("\n");

        report.append("--- Stored Messages (from file) ---\n");
        List<String> stored = loadStoredMessages();
        if (stored.isEmpty()) {
            report.append("No messages stored in file.\n");
        } else {
            for (String msg : stored) {
                report.append(msg).append("\n");
            }
        }
        report.append("\n");

        return report.toString();
    }

    @Override
    public String toString() {
        return "ID: " + messageID + ", Hash: " + messageHash + ", Recipient: " + recipient + ", Message: " + messageText;
    }
}
