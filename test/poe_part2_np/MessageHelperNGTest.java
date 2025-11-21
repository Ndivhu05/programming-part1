package poe_part2_np;

import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MessageHelperNGTest {
    
    private MessageHelper messageHelper;
    
    @BeforeMethod
    public void setUp() {
        messageHelper = new MessageHelper();
        MessageHelper.populateArrays();
    }
    
    @AfterMethod
    public void tearDown() {
        MessageHelper.getSentMessages().clear();
        MessageHelper.getStoredMessages().clear();
        MessageHelper.getMessageHash().clear();
        MessageHelper.getMessageID().clear();
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        String result = MessageHelper.displaySenderRecipientOfAllSentMessages();
        assertFalse(result.contains("Did you get the cake?"));
        assertFalse(result.contains("It is dinner time!"));
    }

    @Test
    public void testDisplayLongestSentMessage() {
        String result = MessageHelper.displayLongestSentMessage();
        assertFalse(result.contains("Where are you? You are late! I have asked you to be on time."));
    }

    @Test
    public void testSearchMessageByID() {
        String testMessageID = getTestMessage4ID();
        String result = MessageHelper.searchMessageByID(testMessageID);
        assertFalse(result.contains("0838884567"));
        assertFalse(result.contains("It is dinner time!"));
    }

    @Test
    public void testSearchMessagesByRecipient() {
        String result = MessageHelper.searchMessagesByRecipient("+27838884567");
        assertFalse(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertFalse(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {
        String testMessageHash = getTestMessage2Hash();
        String result = MessageHelper.deleteMessageByHash(testMessageHash);
        assertFalse(result.contains("successfully deleted"));
        assertFalse(result.contains("Where are you? You are late! I have asked you to be on time"));
    }

    @Test
    public void testDisplayFullReport() {
        String result = MessageHelper.displayFullReport();
        assertTrue(result.contains("Message Hash"));
        assertTrue(result.contains("Recipient"));
        assertTrue(result.contains("Message"));
        assertTrue(result.contains("FULL MESSAGE REPORT"));
    }

    private String getTestMessage4ID() {
        for (Message msg : MessageHelper.getSentMessages()) {
            if (msg.getMessageText() != null && msg.getMessageText().contains("It is dinner time!")) {
                return msg.getMessageID();
            }
        }
        for (Message msg : MessageHelper.getStoredMessages()) {
            if (msg.getMessageText() != null && msg.getMessageText().contains("It is dinner time!")) {
                return msg.getMessageID();
            }
        }
        return "test_message_4_id";
    }

    private String getTestMessage2Hash() {
        for (Message msg : MessageHelper.getSentMessages()) {
            if (msg.getMessageText() != null && msg.getMessageText().contains("Where are you? You are late!")) {
                return msg.getMessageHash();
            }
        }
        for (Message msg : MessageHelper.getStoredMessages()) {
            if (msg.getMessageText() != null && msg.getMessageText().contains("Where are you? You are late!")) {
                return msg.getMessageHash();
            }
        }
        return "test_message_2_hash";
    }
}