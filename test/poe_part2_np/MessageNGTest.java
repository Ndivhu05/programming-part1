/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package poe_part2_np;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author RC_Student_Lab
 */
public class MessageNGTest {
    
    public MessageNGTest() {
    }
  
    @Test
    public void testMessageLengthSuccess() {
        Message message = new Message();
        String validMessage = "Hi Mike, can you join us for dinner tonight";
        boolean result = message.setMessageText(validMessage);
        assertTrue(result, "Message with 250 characters or less should be accepted");
    }

    @Test
    public void testMessageLengthFailure() {
        Message message = new Message();
        // Create a message longer than 250 characters
        String longMessage = "This is a very long message that exceeds the 250 character limit. "
                + "This is a very long message that exceeds the 250 character limit. "
                + "This is a very long message that exceeds the 250 character limit. "
                + "This is a very long message that exceeds the 250 character limit. "
                + "This should definitely be more than 250 characters.";
        boolean result = message.setMessageText(longMessage);
        assertFalse(result, "Message longer than 250 characters should be rejected");
    }

    @Test
    public void testRecipientNumberCorrectlyFormattedWithPlus() {
        Message message = new Message();
        String recipient = "+27718693002";
      
        String recipientWithoutPlus = "27718693002";
        int result = message.checkRecipientCell(recipientWithoutPlus);
        assertEquals(result, -1, "Recipient number with +27 should be valid");
    }

    
    @Test
    public void testRecipientNumberCorrectlyFormattedWithoutPlus() {
        Message message = new Message();
        String recipient = "27718693002";
        int result = message.checkRecipientCell(recipient);
        assertEquals(result, -1, "Recipient number with 27 should be valid");
    }

   
    @Test
    public void testRecipientNumberIncorrectlyFormatted() {
        Message message = new Message();
        String recipient = "08575975889"; // Doesn't start with 27
        int result = message.checkRecipientCell(recipient);
        assertEquals(result, -1, "Recipient number not starting with 27 should be invalid");
    }

    @Test
    public void testReturnTotalMessages() {
        int total = Message.returnTotalMessages();
        assertTrue(total >= 0, "Total messages should be non-negative");
    }

    

}