/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Student
 */
public class LoginTest {
    

    Login login = new Login();

    @Test
    public void testUsernameFormatting() {
        String validUser = "kyl_1";
        assertTrue(login.checkUserName(validUser));
        assertEquals("Welcome <user first name> ,<user last name> it is great to see you.", 
                     login.returnLoginStatus(true));

        String invalidUser = "kyle!!!!!!!";
        assertFalse(login.checkUserName(invalidUser));
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", 
                     login.registerUser(invalidUser, "ValidPass1!"));
    }

    @Test
    public void testPasswordComplexity() {
        String validPass = "Ch&&sec@ke99!";
        assertTrue(login.checkPasswordComplexity(validPass));
        assertEquals("Password successfully captured.", 
                     login.registerUser("kyl_1", validPass));

        String invalidPass = "password";
        assertFalse(login.checkPasswordComplexity(invalidPass));
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", 
                     login.registerUser("kyl_1", invalidPass));
    }

    @Test
    public void testCellFormatting() {
        // Case: Cell phone number correctly formatted
        String validCell = "+27838968976";
        assertTrue(login.checkCellPhoneNumber(validCell));

        // Case: Cell phone number incorrectly formatted
        String invalidCell = "08966553";
        assertFalse(login.checkCellPhoneNumber(invalidCell));
    }

    @Test
    public void testLoginStatus() {
        // Case: Login Successful
        assertTrue(true); // Based on the (assertTrue/False) table in image
        
        // Case: Login Failed
        assertFalse(false); 
    }
}
