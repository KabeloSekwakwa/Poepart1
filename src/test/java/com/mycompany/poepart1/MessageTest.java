/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.poepart1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Student
 */
public class MessageTest {

    @Before
    public void setUp() {
        Message.resetForTesting(); // Clear static counters before each test
    }

    @Test
    public void testCheckMessageID_ValidLength() {
        Message msg = new Message(1, "+27718693002", "Test");
        assertTrue("Message ID should be 10 characters or less", msg.checkMessageID());
        assertEquals("Generated ID must be exactly 10 chars", 10, msg.getMessageId().length());
    }

    @Test
    public void testCheckRecipientCell_Success() {
        // Using +27718693002 - 12 chars total as per rubric example
        Message msg = new Message(1, "+27718693002", "Test");
        String expected = "Cell phone number successfully captured.";
        assertEquals(expected, msg.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCell_FailureNoInternationalCode() {
        Message msg = new Message(1, "08575975889", "Test"); // Test Data 2 from rubric
        String expected = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        assertEquals(expected, msg.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCell_FailureTooLong() {
        Message msg = new Message(1, "+2712345678901", "Test"); // 14 chars, fails 12 char limit
        String expected = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        assertEquals(expected, msg.checkRecipientCell());
    }

    @Test
    public void testCreateMessageHash_RubricExample() {
        // Rubric example: "Hi Mike, can you join us for dinner tonight?"
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String hash = msg.getMessageHash();
        assertTrue("Hash should end with :0:HITONIGHT for first message", hash.endsWith(":0:HITONIGHT"));
        assertEquals("Hash must have 2 colons, 3 parts", 3, hash.split(":").length);
    }

    @Test
    public void testCreateMessageHash_SecondMessageCounter() {
        Message msg1 = new Message(1, "+27718693002", "First");
        msg1.SentMessage("1"); // totalMessagesSent becomes 1
        
        Message msg2 = new Message(2, "+27718693002", "Hi Bob, thanks");
        assertTrue("Second message should use counter 1", msg2.getMessageHash().contains(":1:HITHANKS"));
    }

    @Test
    public void testSentMessage_SendOption() {
        Message msg = new Message(1, "+27718693002", "Test message");
        String result = msg.SentMessage("1");
        assertEquals("Message successfully sent.", result);
        assertEquals(1, Message.returnTotalMessagess());
    }

    @Test
    public void testSentMessage_DisregardOption() {
        Message msg = new Message(1, "+27718693002", "Test message");
        String result = msg.SentMessage("2");
        assertEquals("Press 0 to delete the message.", result);
        assertEquals("Counter should not increment on disregard", 0, Message.returnTotalMessagess());
    }

    @Test
    public void testSentMessage_StoreOption() {
        Message msg = new Message(1, "+27718693002", "Test message");
        String result = msg.SentMessage("3");
        assertEquals("Message successfully stored.", result);
        assertEquals("Counter should not increment on store", 0, Message.returnTotalMessagess());
        java.io.File f = new java.io.File("messages.json");
        assertTrue("messages.json should exist after storing", f.exists());
    }

    @Test
    public void testReturnTotalMessagess() {
        assertEquals(0, Message.returnTotalMessagess());
        Message msg1 = new Message(1, "+27718693002", "First");
        msg1.SentMessage("1");
        assertEquals(1, Message.returnTotalMessagess());
        Message msg2 = new Message(2, "+27718693002", "Second");
        msg2.SentMessage("1");
        assertEquals(2, Message.returnTotalMessagess());
    }

    @Test
    public void testPrintMessages_EmptyInitially() {
        assertEquals("No messages sent yet.", Message.printMessages());
    }

    @Test
    public void testPrintMessages_WithSentMessages() {
        Message msg = new Message(1, "+27718693002", "Test");
        msg.SentMessage("1");
        String output = Message.printMessages();
        assertTrue(output.contains("Message ID:"));
        assertTrue(output.contains("Recipient: +27718693002"));
        assertTrue(output.contains("Message: Test"));
    }
}