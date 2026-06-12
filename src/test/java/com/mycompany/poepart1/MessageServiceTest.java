/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.poepart1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageServiceTest {

    @Before
    public void setUp() {
        MessageService.resetForTesting();
        MessageService.loadTestData(); // Loads messages 1-5 
    }

    @Test 
    public void testSentMessagesArray_CorrectlyPopulated() {
        assertEquals("Did you get the cake?", MessageService.getSentMessages()[0]);
        assertEquals("It is dinner time!", MessageService.getSentMessages()[3]);
    }

    @Test //  Display the longest Message
    public void testDisplayLongestMessage() {
        String expected = "Where are you? You are late! I have asked you to be on time.";
        assertEquals(expected, MessageService.getLongestMessage());
    }

    @Test // Search for messageID - Test Data: message 4 "0838884567"
    public void testSearchByMessageID() {
        String expected = "It is dinner time!";
        assertEquals(expected, MessageService.searchByMessageID("4567890123"));
    }

    @Test //  Search all messages for +27838884567
    public void testSearchByRecipient() {
        String[] results = MessageService.searchByRecipient("+27838884567");
        assertEquals(2, results.length);
        assertEquals("Where are you? You are late! I have asked you to be on time.", results[0]);
        assertEquals("Ok, I am leaving without you.", results[1]);
    }

    @Test // Delete a message using message hash - Test Message 2
    public void testDeleteByHash() {
        String hashToDelete = MessageService.getMessageHashes()[1]; // Hash for message 2
        int countBefore = MessageService.getMessageCount();
        assertTrue(MessageService.deleteByHash(hashToDelete));
        assertEquals(countBefore - 1, MessageService.getMessageCount());
    }

    @Test //  Message Hash string manipulation
    public void testMessageHashFormat() {
        String hash = MessageService.buildMessageHash("1234567890", 0, "Did you get the cake?");
        assertTrue(hash.startsWith("12:0:"));
        assertTrue(hash.contains("DID"));
        assertTrue(hash.contains("CAKE"));
    }

    @Test // Message ID 10 digits
    public void testMessageID_Length() {
        String id = MessageService.createTenDigitUniqueId();
        assertEquals(10, id.length());
        assertTrue(id.matches("\\d{10}"));
    }

    @Test //  Display Report shows Hash, Recipient, Message
    public void testReportDataExists() {
        assertNotNull(MessageService.getMessageHashes()[0]);
        assertNotNull(MessageService.getRecipients()[0]);
        assertNotNull(MessageService.getSentMessages()[0]);
    }
}