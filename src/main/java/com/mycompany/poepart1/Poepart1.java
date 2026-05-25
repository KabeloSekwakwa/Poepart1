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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Poepart1 {

    // Inner class to store message fields as per PAT spec
    static class MessageDetails {
        int messageNumber; 
        String messageId;
        String messageHash;
        String recipient;
        String messageText;

        public MessageDetails(int messageNumber, String messageId, String messageHash, String recipient, String messageText) {
            this.messageNumber = messageNumber;
            this.messageId = messageId;
            this.messageHash = messageHash;
            this.recipient = recipient;
            this.messageText = messageText;
        }
    }

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static int totalMessagesSentCounter = 0; 

    // Global variables to capture registration details for verification later
    private static String registeredFirstName = "";
    private static String registeredLastName = "";
    private static String registeredUsername = "";
    private static String registeredPassword = "";
    private static String registeredPhoneNumber = "";

    public static void main(String[] args) {
        // Step 1: Run Registration Phase
        performRegistration();

        // Step 2: Run Login Phase using the registered details
        if (!performLoginVerification()) {
            System.out.println("Login verification failed. Exiting program.");
            return;
        }

        // Step 3: Launch Main Application Menu
        boolean applicationRunning = true;
        while (applicationRunning) {
            System.out.println("\n=================================");
            System.out.println("Welcome to QuickChat.");
            System.out.println("=================================");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Please select an option (1-3): ");

            String menuChoice = scanner.nextLine().trim();

            switch (menuChoice) {
                case "1" -> handleSendMessagesFeature();
                case "2" -> System.out.println("\nComing Soon.");
                case "3" -> {
                    System.out.println("Exiting QuickChat application. Goodbye!");
                    applicationRunning = false;
                }
                default -> System.out.println("Invalid selection. Please input 1, 2, or 3.");
            }
        }
        
        scanner.close();
    }

    private static void performRegistration() {
        System.out.println("\n------Registration------");

        System.out.print("First Name: ");
        registeredFirstName = scanner.nextLine().trim();
    
        System.out.print("Last Name: ");
        registeredLastName = scanner.nextLine().trim();
        
        System.out.println("-----------------------------------");
        
        // --- USERNAME VALIDATION ---
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

        // --- PASSWORD VALIDATION ---
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
        
        // --- CELLPHONE VALIDATION ---
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
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input. Returning to menu selection.");
            return;
        }

        List<MessageDetails> sessionBatchList = new ArrayList<>();

        for (int currentLoopIndex = 1; currentLoopIndex <= totalToEnter; currentLoopIndex++) {
            System.out.println("\n--- Entering Message Details [" + currentLoopIndex + " of " + totalToEnter + "] ---");

            String fullyGeneratedId = createTenDigitUniqueId();
            String verifiedMessageId = fullyGeneratedId.substring(0, 10);

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
                    MessageDetails newMessage = new MessageDetails(currentLoopIndex, verifiedMessageId, messageHash, recipientCell, textBody);
                    sessionBatchList.add(newMessage);
                    System.out.println("\n--- SCREEN DISPLAY REPORT ---");
                    System.out.println("Message ID: " + newMessage.messageId);
                    System.out.println("Message Hash: " + newMessage.messageHash);
                    System.out.println("Recipient: " + newMessage.recipient);
                    System.out.println("Message: " + newMessage.messageText);
                    System.out.println("-----------------------------");
                    totalMessagesSentCounter++;
                }
                case "2", "0" -> {
                    System.out.println("\n\"Press 0 to delete the message\"");
                    System.out.println("Process cleared. Message abandoned successfully.");
                }
                case "3" -> {
                    System.out.println("\n\"Message successfully stored\"");
                    MessageDetails storedMessage = new MessageDetails(currentLoopIndex, verifiedMessageId, messageHash, recipientCell, textBody);
                    saveToJsonFile(storedMessage);
                }
                default -> System.out.println("Invalid selection. Message scrapped.");
            }
        }

        System.out.println("\n=========================================");
        System.out.println("BATCH CYCLES COMPLETED");
        System.out.println("Messages logged this batch process: " + sessionBatchList.size());
        System.out.println("Total running global message tracker: " + totalMessagesSentCounter);
        System.out.println("=========================================");
    }

    private static String createTenDigitUniqueId() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 12; i++) { 
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

    private static void saveToJsonFile(MessageDetails message) {
        String jsonOutput = """
                            {
                             "messageNumber": """ + message.messageNumber + ",\n" +
                " \"messageId\": \"" + message.messageId + "\",\n" +
                " \"messageHash\": \"" + message.messageHash + "\",\n" +
                " \"recipient\": \"" + message.recipient + "\",\n" +
                " \"messageText\": \"" + message.messageText + "\"\n" +
                "}";

        try (FileWriter fileWriter = new FileWriter("messages.json", true)) {
            fileWriter.write(jsonOutput + "\n\n");
            System.out.println("JSON logged to 'messages.json' safely.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}