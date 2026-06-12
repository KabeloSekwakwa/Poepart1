/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poepart1;

/**
 *
 * @author Student
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Poepart1 {

    // --- TASK 1: Global Parallel Arrays  ---
    private static final int MAX_MESSAGES = 100; 
    private static String[] sentMessages = new String[MAX_MESSAGES];
    private static String[] disregardedMessages = new String[MAX_MESSAGES];
    private static String[] messageIDs = new String[MAX_MESSAGES];
    private static String[] messageHashes = new String[MAX_MESSAGES];
    private static String[] recipients = new String[MAX_MESSAGES];
    private static int messageCount = 0; // sent messages
    private static int disregardedCount = 0; // disregarded messages

    // Class for JSON 
    static class MessageDetails {
        String messageId;
        String messageHash;
        String recipient;
        String messageText;

        public MessageDetails(String messageId, String messageHash, String recipient, String messageText) {
            this.messageId = messageId;
            this.messageHash = messageHash;
            this.recipient = recipient;
            this.messageText = messageText;
        }
    }

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static int totalMessagesSentCounter = 0; 
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String JSON_FILE = "messages.json";

    private static String registeredFirstName = "";
    private static String registeredLastName = "";
    private static String registeredUsername = "";
    private static String registeredPassword = "";
    private static String registeredPhoneNumber = "";

    public static void main(String[] args) {
        syncArraysFromJSON(); // Load stored messages into parallel arrays

        try (scanner) {
            performRegistration();
            
            if (!performLoginVerification()) {
                System.out.println("Login verification failed. Exiting program.");
                return;
            }
            
            boolean applicationRunning = true;
            while (applicationRunning) {
                System.out.println("\n=================================");
                System.out.println("Welcome to QuickChat.");
                System.out.println("=================================");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.println("4) Stored Messages"); 
                System.out.print("Please select an option (1-4): ");
                
                String menuChoice = scanner.nextLine().trim();
                
                switch (menuChoice) {
                    case "1" -> handleSendMessagesFeature();
                    case "2" -> displayRecentlySentMessages();
                    case "3" -> {
                        System.out.println("Exiting QuickChat application. Goodbye!");
                        applicationRunning = false;
                    }
                    case "4" -> handleStoredMessagesMenu(); 
                    default -> System.out.println("Invalid selection. Please input 1, 2, 3, or 4.");
                }
            }
        }
    }

    private static void performRegistration() {
        System.out.println("\n------Registration------");
        System.out.print("First Name: ");
        registeredFirstName = scanner.nextLine().trim();
        System.out.print("Last Name: ");
        registeredLastName = scanner.nextLine().trim();
        System.out.println("-----------------------------------");
        
        while (true) {
            System.out.print("Enter Username: ");
            registeredUsername = scanner.nextLine().trim();
            boolean isUsernameValid = registeredUsername.contains("_") && registeredUsername.length() <= 5;
            if (isUsernameValid) {
                System.out.println("Username successfully captured.");
                break;
            } else {
                System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            }
        }

        System.out.println("-----------------------------------");

        while (true) {
            System.out.print("Enter Password: ");
            registeredPassword = scanner.nextLine().trim();
            boolean isPasswordValid = registeredPassword.length() >= 8 &&
                                     registeredPassword.matches(".*[A-Z].*") &&
                                     registeredPassword.matches(".*[0-9].*") &&
                                     registeredPassword.matches(".*[^a-zA-Z0-9].*");
            if (isPasswordValid) {
                System.out.println("Password successfully captured.");
                break;
            } else {
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            }
        }
        
        System.out.println("-----------------------------------");
        
        while (true) {
            System.out.print("Enter cellphone number: ");
            registeredPhoneNumber = scanner.nextLine().trim();
            boolean isFormattedCorrectly = registeredPhoneNumber.startsWith("+27") && registeredPhoneNumber.length() <= 13;
            if (isFormattedCorrectly) {
                System.out.println("Cellphone number successfully added."); 
                break;
            } else { 
                System.out.println("Cellphone number incorrectly formatted or does not contain international code.");
            }
        }
    }

    private static boolean performLoginVerification() {
        System.out.println("\n------------Login------------");
        System.out.print("Enter Username: ");
        String loginUser = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String loginPass = scanner.nextLine().trim();
        System.out.println("\n-----------------------------");
        
        if (loginUser.equals(registeredUsername) && loginPass.equals(registeredPassword)) {
            System.out.println("Welcome " + registeredFirstName + " " + registeredLastName + ", it is great to see you again.");
            return true;
        } else {
            System.out.println("Username or password incorrect.");
            return false;
        }
    }

    private static void handleSendMessagesFeature() {
        System.out.print("\nHow many messages would you like to enter? ");
        int totalToEnter;
        try {
            totalToEnter = Integer.parseInt(scanner.nextLine().trim());
            if (totalToEnter <= 0) {
                System.out.println("Count must be greater than zero.");
                return;
            }
            if (messageCount + totalToEnter > MAX_MESSAGES) {
                System.out.println("Error: Not enough space. Max messages is " + MAX_MESSAGES);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input. Returning to menu selection.");
            return;
        }

        int sessionBatchCount = 0;

        for (int currentLoopIndex = 1; currentLoopIndex <= totalToEnter; currentLoopIndex++) {
            System.out.println("\n--- Entering Message Details [" + currentLoopIndex + " of " + totalToEnter + "] ---");

            String verifiedMessageId = createTenDigitUniqueId();

            String recipientCell = "";
            while (true) {
                System.out.print("Enter Recipient Cell Number (e.g. +27831234567): ");
                recipientCell = scanner.nextLine().trim();
                if (recipientCell.matches("\\+\\d{1,12}")) { 
                    break;
                }
                System.out.println("Error: Use international format e.g. +27831234567");
            }

            String textBody = "";
            while (true) {
                System.out.print("Enter Message Content (Max 250 characters): ");
                textBody = scanner.nextLine().trim();
                
                if (textBody.isEmpty()) {
                    System.out.println("Error: Message cannot be empty.");
                } else if (textBody.length() > 250) {
                    System.out.println("\"Please enter a message of less than 250 characters.\"");
                } else {
                    System.out.println("\"Message sent\"");
                    break;
                }
            }

            String messageHash = buildMessageHash(verifiedMessageId, totalMessagesSentCounter, textBody);

            System.out.println("\nSelect an action for this message:");
            System.out.println("1. Send Message");
            System.out.println("2. Disregard Message (Press 0 to delete)");
            System.out.println("3. Store Message to send later");
            System.out.print("Choice Selection: ");
            String action = scanner.nextLine().trim();

            switch (action) {
                case "1" -> {
                    System.out.println("\n\"Message successfully sent\"");
                    
                    // Populate parallel arrays - Task 1 requirement
                    sentMessages[messageCount] = textBody;
                    messageHashes[messageCount] = messageHash;
                    messageIDs[messageCount] = verifiedMessageId;
                    recipients[messageCount] = recipientCell;
                    messageCount++;
                    sessionBatchCount++;

                    System.out.println("\n--- SCREEN DISPLAY REPORT ---");
                    System.out.println("Message ID: " + verifiedMessageId);
                    System.out.println("Message Hash: " + messageHash);
                    System.out.println("Recipient: " + recipientCell);
                    System.out.println("Message: " + textBody);
                    System.out.println("-----------------------------");
                    totalMessagesSentCounter++;
                    saveAllToJsonFile(); // Save after each send
                }
                case "2", "0" -> {
                    System.out.println("\n\"Press 0 to delete the message\"");
                    disregardedMessages[disregardedCount] = textBody;
                    disregardedCount++;
                    System.out.println("Process cleared. Message abandoned successfully.");
                }
                case "3" -> {
                    System.out.println("\n\"Message successfully stored\"");
                    // For "store", we still add to sent arrays but mark as stored via JSON
                    sentMessages[messageCount] = textBody;
                    messageHashes[messageCount] = messageHash;
                    messageIDs[messageCount] = verifiedMessageId;
                    recipients[messageCount] = recipientCell;
                    messageCount++;
                    sessionBatchCount++;
                    saveAllToJsonFile();
                }
                default -> {
                    System.out.println("Invalid selection. Message scrapped.");
                    disregardedMessages[disregardedCount] = textBody;
                    disregardedCount++;
                }
            }
        }

        System.out.println("\n=========================================");
        System.out.println("BATCH CYCLES COMPLETED");
        System.out.println("Messages logged this batch process: " + sessionBatchCount);
        System.out.println("Total running global message tracker: " + totalMessagesSentCounter);
        System.out.println("=========================================");
    }

    private static void displayRecentlySentMessages() {
        System.out.println("\n--- RECENTLY SENT MESSAGES ---");
        if (messageCount == 0) {
            System.out.println("No messages sent during this execution lifecycle.");
            return;
        }
        for (int i = 0; i < messageCount; i++) {
            System.out.println((i + 1) + ". ID: " + messageIDs[i] + " | Hash: " + messageHashes[i]);
            System.out.println(" Content: " + sentMessages[i]);
        }
    }

    // --- TASK 2: Stored Messages Secondary Menu ---
    private static void handleStoredMessagesMenu() {
        boolean insideStoredMenu = true;
        while (insideStoredMenu) {
            System.out.println("\n--- STORED MESSAGES SUB-MENU ---");
            System.out.println("a. Display sender and recipient of all stored messages");
            System.out.println("b. Display the longest stored message");
            System.out.println("c. Search for a message ID and display recipient & message");
            System.out.println("d. Search for all messages stored for a particular recipient");
            System.out.println("e. Delete a message using the message hash");
            System.out.println("f. Display a report listing full details of all stored messages");
            System.out.println("g. Return to Main Menu");
            System.out.print("Select an option (a-g): ");
            
            String choice = scanner.nextLine().trim().toLowerCase();
            switch (choice) {
                case "a" -> displaySendersAndRecipients();
                case "b" -> displayLongestMessage();
                case "c" -> searchByMessageId();
                case "d" -> searchByRecipient();
                case "e" -> deleteMessageByHash();
                case "f" -> displayFullStoredReport();
                case "g" -> insideStoredMenu = false;
                default -> System.out.println("Invalid sub-menu option. Please try again.");
            }
        }
    }

    // Option a: Display sender and recipient - searches parallel arrays
    private static void displaySendersAndRecipients() {
        System.out.println("\n--- Senders and Recipients of Stored Messages ---");
        if (messageCount == 0) {
            System.out.println("No stored messages found.");
            return;
        }
        for (int i = 0; i < messageCount; i++) {
            System.out.println("Sender: " + registeredFirstName + " " + registeredLastName + " -> Recipient: " + recipients[i]);
        }
    }

    // Option b: Display longest message - searches parallel arrays - 4-5 marks
    private static void displayLongestMessage() {
        System.out.println("\n--- Longest Stored Message ---");
        if (messageCount == 0) {
            System.out.println("No stored messages available.");
            return;
        }
        int longestIndex = 0;
        for (int i = 1; i < messageCount; i++) {
            if (sentMessages[i].length() > sentMessages[longestIndex].length()) {
                longestIndex = i;
            }
        }
        System.out.println("Longest Message (" + sentMessages[longestIndex].length() + " characters):");
        System.out.println("\"" + sentMessages[longestIndex] + "\" (Sent to: " + recipients[longestIndex] + ")");
    }

    // Option c: Search by Message ID - searches parallel arrays
    private static void searchByMessageId() {
        System.out.println("\n--- Search by Message ID ---");
        System.out.print("Enter Message ID to search: ");
        String idInput = scanner.nextLine().trim();
        boolean found = false;

        for (int i = 0; i < messageCount; i++) {
            if (messageIDs[i].equals(idInput)) {
                System.out.println("\nMatch Found!");
                System.out.println("Recipient: " + recipients[i]);
                System.out.println("Message Content: " + sentMessages[i]);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("No message discovered matching ID: " + idInput);
        }
    }

    // Option d: Search by recipient - searches parallel arrays - 8-10 marks
    private static void searchByRecipient() {
        System.out.println("\n--- Search by Recipient Cell ---");
        System.out.print("Enter Recipient Cell Number: ");
        String recipientInput = scanner.nextLine().trim();
        boolean found = false;
        int displayIndex = 1;

        for (int i = 0; i < messageCount; i++) {
            if (recipients[i].equals(recipientInput)) {
                System.out.println("\n[" + displayIndex + "] Message ID: " + messageIDs[i]);
                System.out.println(" Text: " + sentMessages[i]);
                found = true;
                displayIndex++;
            }
        }
        if (!found) {
            System.out.println("No records found corresponding to recipient: " + recipientInput);
        }
    }

    // Option e: Delete by hash - searches parallel arrays
    private static void deleteMessageByHash() {
        System.out.println("\n--- Delete Message by Hash ---");
        System.out.print("Enter Message Hash to wipe out: ");
        String hashInput = scanner.nextLine().trim();
        boolean removed = false;

        for (int i = 0; i < messageCount; i++) {
            if (messageHashes[i].equalsIgnoreCase(hashInput)) {
                // Shift all elements left to delete
                for (int j = i; j < messageCount - 1; j++) {
                    sentMessages[j] = sentMessages[j + 1];
                    messageIDs[j] = messageIDs[j + 1];
                    messageHashes[j] = messageHashes[j + 1];
                    recipients[j] = recipients[j + 1];
                }
                messageCount--;
                removed = true;
                break;
            }
        }

        if (removed) {
            saveAllToJsonFile();
            System.out.println("Message matching hash was successfully eradicated from storage logs.");
        } else {
            System.out.println("No matches found for hash string provided.");
        }
    }

    // Option f: Full report - searches parallel arrays
    private static void displayFullStoredReport() {
        System.out.println("\n=======================================================");
        System.out.println(" STORED MESSAGES COMPREHENSIVE REPORT ");
        System.out.println("=======================================================");
        if (messageCount == 0) {
            System.out.println(" No stored payload data registers found. ");
            System.out.println("=======================================================");
            return;
        }
        for (int i = 0; i < messageCount; i++) {
            System.out.println("Record Entry #" + (i + 1));
            System.out.println(" -> ID : " + messageIDs[i]);
            System.out.println(" -> Hash : " + messageHashes[i]);
            System.out.println(" -> Recipient : " + recipients[i]);
            System.out.println(" -> Message : " + sentMessages[i]);
            System.out.println("-------------------------------------------------------");
        }
    }

    private static String createTenDigitUniqueId() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10; i++) { 
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    private static String buildMessageHash(String msgId, int count, String text) {
        String idPrefix = msgId.substring(0, 2);
        String uniformText = text.trim().replaceAll("\\s+", " ");
        String wordsConstruct = "";

        if (!uniformText.isEmpty()) {
            String[] segments = uniformText.split(" ");
            String firstWord = segments[0];
            String lastWord = segments[segments.length - 1];
            wordsConstruct = (firstWord + lastWord).toUpperCase().replaceAll("[^A-Z0-9]", "");
        } else {
            wordsConstruct = "EMPTY";
        }

        return idPrefix + ":" + count + ":" + wordsConstruct;
    }

    // Convert arrays to MessageDetails[] for JSON
    private static void saveAllToJsonFile() {
        MessageDetails[] detailsArray = new MessageDetails[messageCount];
        for (int i = 0; i < messageCount; i++) {
            detailsArray[i] = new MessageDetails(messageIDs[i], messageHashes[i], recipients[i], sentMessages[i]);
        }
        try (FileWriter fileWriter = new FileWriter(JSON_FILE)) {
            gson.toJson(detailsArray, fileWriter);
        } catch (IOException e) {
            System.out.println("Error writing data to JSON: " + e.getMessage());
        }
    }
    
    // Load JSON into parallel arrays on startup
    private static void syncArraysFromJSON() {
        try (FileReader reader = new FileReader(JSON_FILE)) {
            MessageDetails[] loaded = gson.fromJson(reader, MessageDetails[].class);
            if (loaded!= null) {
                messageCount = 0;
                for (MessageDetails msg : loaded) {
                    if (messageCount < MAX_MESSAGES) {
                        sentMessages[messageCount] = msg.messageText;
                        messageIDs[messageCount] = msg.messageId;
                        messageHashes[messageCount] = msg.messageHash;
                        recipients[messageCount] = msg.recipient;
                        messageCount++;
                    }
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet - fine for first run
        }
    }
}