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
public class LoginNGTest {
    
    public LoginNGTest() {
    }

    // Test Username correctly formatted
    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login();
        login.setUsername("k_yl1");
        assertTrue(login.checkUserName(), "Username with underscore and <= 5 characters should return true");
    }

    // Test Username incorrectly formatted - no underscore
    @Test
    public void testUsernameIncorrectlyFormattedNoUnderscore() {
        Login login = new Login();
        login.setUsername("kyle1");
        assertFalse(login.checkUserName(), "Username without underscore should return false");
    }

    // Test Username incorrectly formatted - too long
    @Test
    public void testUsernameIncorrectlyFormattedTooLong() {
        Login login = new Login();
        login.setUsername("kyle_123");
        assertFalse(login.checkUserName(), "Username longer than 5 characters should return false");
    }

    // Test Password meets complexity requirements
    @Test
    public void testPasswordMeetsComplexityRequirements() {
        Login login = new Login();
        login.setPassword("Ch&&sec@ke99!");
        assertTrue(login.checkPasswordComplexity(), "Password with 8+ chars, capital, number, special char should return true");
    }

    // Test Password does not meet complexity requirements - too short
    @Test
    public void testPasswordDoesNotMeetComplexityTooShort() {
        Login login = new Login();
        login.setPassword("pass");
        assertFalse(login.checkPasswordComplexity(), "Password shorter than 8 characters should return false");
    }

    // Test Password does not meet complexity requirements - no capital letter
    @Test
    public void testPasswordDoesNotMeetComplexityNoCapital() {
        Login login = new Login();
        login.setPassword("password123!");
        assertFalse(login.checkPasswordComplexity(), "Password without capital letter should return false");
    }

    // Test Password does not meet complexity requirements - no number
    @Test
    public void testPasswordDoesNotMeetComplexityNoNumber() {
        Login login = new Login();
        login.setPassword("Password!");
        assertFalse(login.checkPasswordComplexity(), "Password without number should return false");
    }

    // Test Password does not meet complexity requirements - no special character
    @Test
    public void testPasswordDoesNotMeetComplexityNoSpecialChar() {
        Login login = new Login();
        login.setPassword("Password123");
        assertFalse(login.checkPasswordComplexity(), "Password without special character should return false");
    }

    // Test Cell phone number correctly formatted
    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        Login login = new Login();
        login.setCellPhoneNumber("+27712345678");
        assertTrue(login.checkCellPhoneNumber(), "Cell phone with +27 and 9 digits should return true");
    }


    // Test Cell phone number incorrectly formatted - wrong format
    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        Login login = new Login();
        login.setCellPhoneNumber("08966553");
        assertFalse(login.checkCellPhoneNumber(), "Cell phone without international code should return false");
    }

    // Test Cell phone number incorrectly formatted - too short
    @Test
    public void testCellPhoneNumberIncorrectlyFormattedTooShort() {
        Login login = new Login();
        login.setCellPhoneNumber("+271234");
        assertFalse(login.checkCellPhoneNumber(), "Cell phone with insufficient digits should return false");
    }

    // Test Cell phone number incorrectly formatted - wrong country code
    @Test
    public void testCellPhoneNumberIncorrectlyFormattedWrongCode() {
        Login login = new Login();
        login.setCellPhoneNumber("+447123456789");
        assertFalse(login.checkCellPhoneNumber(), "Cell phone with wrong country code should return false");
    }

    // Test Login Failed - wrong username
    @Test
    public void testLoginFailedWrongUsername() {
        Login login = new Login();
        login.setUsername("u_ser");
        login.setPassword("Pass123!");
        login.setFirstName("John");
        login.setLastName("Doe");
        login.setCellPhoneNumber("+27712345678");
        
        // First register the user
        login.registerUser();
        
        // Then test login with wrong username
        assertFalse(login.loginUser("wrong", "Pass123!"), "Login with wrong username should return false");
    }

    // Test Login Failed - wrong password
    @Test
    public void testLoginFailedWrongPassword() {
        Login login = new Login();
        login.setUsername("u_ser");
        login.setPassword("Pass123!");
        login.setFirstName("John");
        login.setLastName("Doe");
        login.setCellPhoneNumber("+27712345678");
        
        // First register the user
        login.registerUser();
        
        // Then test login with wrong password
        assertFalse(login.loginUser("u_ser", "wrong"), "Login with wrong password should return false");
    }

    // Test returnLoginStatus for successful login
    @Test
    public void testReturnLoginStatusSuccessful() {
        Login login = new Login();
        login.setFirstName("John");
        login.setLastName("Doe");
        
        String expected = "Welcome John, Doe it is great to see you again.";
        String actual = login.returnLoginStatus(true);
        
        assertEquals(actual, expected, "Login status message for successful login should match expected format");
    }

    // Test returnLoginStatus for failed login
    @Test
    public void testReturnLoginStatusFailed() {
        Login login = new Login();
        
        String expected = "Username or password incorrect, please try again.";
        String actual = login.returnLoginStatus(false);
        
        assertEquals(actual, expected, "Login status message for failed login should match expected format");
    }

}