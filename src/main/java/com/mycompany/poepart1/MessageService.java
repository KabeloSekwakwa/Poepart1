/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poepart1;

/**
 *
 * @author Student
 */
import java.util.Random;

/**
 * PROG5121 POE Part 1 - Final Class
 * Handles parallel arrays for messages as per Task 1 rubric
 * Student Name: [Your Name]
 * Student Number: [Your Number]
 */
public class MessageService {
    
    private static final int MAX_MESSAGES = 100;
    private static String[] sentMessages = new String[MAX_MESSAGES];
    private static String[] messageIDs = new String[MAX_MESSAGES];
    private static String[] messageHashes = new String[MAX_MESSAGES];
    private static String[] recipients = new String[MAX_MESSAGES];
    private static int messageCount = 0;
    private static final Random random = new Random();

    // Resets arrays for testing
    public static void resetForTesting() {
        messageCount = 0;
        sentMessages = new String[MAX_MESSAGES];
        messageIDs = new String[MAX_MESSAGES];
        messageHashes = new String[MAX_MESSAGES];
        recipients = new String[MAX_MESSAGES];
    }

    // Loads exact test data from rubric screenshots
    public static void loadTestData() {
        // Test Data Message 1: Sent
        addMessage("Did you get the cake?", "+27834557896", "1234567890");
        
        // Test Data Message 2: Stored - Longest
        addMessage("Where are you? You are late! I have asked you to be on time.", "+27838884567", "2345678901");
        
        // Test Data Message 3: Disregard - but we add for search tests
        addMessage("Yohoooo, I am at your gate.", "+27834484567", "3456789012");
        
        // Test Data Message 4: Sent - Note: 0838884567 not +27 per rubric
        addMessage("It is dinner time!", "0838884567", "4567890123");
        
        // Test Data Message 5: Stored
        addMessage("Ok, I am leaving without you.", "+27838884567", "5678901234");
    }

    public static void addMessage(String text, String recipient, String id) {
        if (messageCount < MAX_MESSAGES) {
            sentMessages[messageCount] = text;
            recipients[messageCount] = recipient;
            messageIDs[messageCount] = id;
            messageHashes[messageCount] = buildMessageHash(id, messageCount, text);
            messageCount++;
        }
    }

    public static String buildMessageHash(String msgId, int count, String text) {
        String idPrefix = msgId.substring(0, 2);
        String uniformText = text.trim().replaceAll("\\s+", " ");
        String wordsConstruct = "";

        if (!uniformText.isEmpty()) {
            String[] segments = uniformText.split(" ");
            String firstWord = segments[0];
            String lastWord = segments[segments.length - 1];
            wordsConstruct = (firstWord + lastWord).toUpperCase().replaceAll("[^A-Z0-9!?]", "");
        } else {
            wordsConstruct = "EMPTY";
        }
        return idPrefix + ":" + count + ":" + wordsConstruct;
    }

    public static String createTenDigitUniqueId() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    // Task 2b: Display the longest Message
    public static String getLongestMessage() {
        if (messageCount == 0) return null;
        int longestIndex = 0;
        for (int i = 1; i < messageCount; i++) {
            if (sentMessages[i].length() > sentMessages[longestIndex].length()) {
                longestIndex = i;
            }
        }
        return sentMessages[longestIndex];
    }

    // Task 2c: Search for messageID
    public static String searchByMessageID(String id) {
        for (int i = 0; i < messageCount; i++) {
            if (messageIDs[i].equals(id)) {
                return sentMessages[i];
            }
        }
        return null;
    }

    // Task 2d: Search all messages for particular recipient
    public static String[] searchByRecipient(String recipient) {
        int count = 0;
        for (int i = 0; i < messageCount; i++) {
            if (recipients[i].equals(recipient)) count++;
        }
        String[] results = new String[count];
        int idx = 0;
        for (int i = 0; i < messageCount; i++) {
            if (recipients[i].equals(recipient)) {
                results[idx++] = sentMessages[i];
            }
        }
        return results;
    }

    // Task 2e: Delete by hash
    public static boolean deleteByHash(String hash) {
        for (int i = 0; i < messageCount; i++) {
            if (messageHashes[i].equalsIgnoreCase(hash)) {
                for (int j = i; j < messageCount - 1; j++) {
                    sentMessages[j] = sentMessages[j + 1];
                    messageIDs[j] = messageIDs[j + 1];
                    messageHashes[j] = messageHashes[j + 1];
                    recipients[j] = recipients[j + 1];
                }
                messageCount--;
                return true;
            }
        }
        return false;
    }

    // Getters for testing
    public static String[] getSentMessages() { return sentMessages; }
    public static String[] getRecipients() { return recipients; }
    public static String[] getMessageIDs() { return messageIDs; }
    public static String[] getMessageHashes() { return messageHashes; }
    public static int getMessageCount() { return messageCount; }
}
