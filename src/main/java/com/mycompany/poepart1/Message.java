/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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


public final class Message {
    private final int messageNumber;
    private final String messageId;
    private String messageHash;
    private final String recipient;
    private final String messageText;
    
    
    private static int totalMessagesSent = 0;
    private static final List<Message> sentMessages = new ArrayList<>();

    /**
     * 
     * @param messageNumber 
     * @param recipient 
     * @param messageText 
     */
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageId = createMessageID();
        this.messageHash = createMessageHash();
    }

    /**
     * .
     * @return 
     */
    public Boolean checkMessageID() {
        return this.messageId!= null && this.messageId.length() <= 10;
    }

    /**
     
     * @return 
     */
    public String checkRecipientCell() {
        if (recipient!= null && recipient.matches("\\+\\d{1,11}") && recipient.length() <= 12) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    
    public String createMessageHash() {
        String idPrefix = this.messageId.substring(0, 2);
        String[] words = this.messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        return (idPrefix + ":" + totalMessagesSent + ":" + (firstWord + lastWord)).toUpperCase();
    }

    /**
     
     * @param userSelection "1"/"send", "2"/"disregard", "3"/"store"
     * @return 
     */
    public String SentMessage(String userSelection) {
        if (userSelection == null) return "Invalid selection.";
        
        String sel = userSelection.toLowerCase();
        
        if (sel.equals("1") || sel.contains("send")) {
            totalMessagesSent++;
            sentMessages.add(this);
            this.messageHash = createMessageHash(); // Regenerate with updated counter
            return "Message successfully sent.";
        } else if (sel.equals("2") || sel.equals("0") || sel.contains("disregard") || sel.contains("discard")) {
            return "Press 0 to delete the message.";
        } else if (sel.equals("3") || sel.contains("store")) {
            storeMessage();
            return "Message successfully stored.";
        }
        return "Invalid selection.";
    }

    /**
     
     * @return String formatted list of sent messages
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) return "No messages sent yet.";
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID: ").append(m.messageId).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient: ").append(m.recipient).append("\n");
            sb.append("Message: ").append(m.messageText).append("\n\n");
        }
        return sb.toString();
    }

    /**
     
     * @return int total count
     */
    public static int returnTotalMessagess() {
        return totalMessagesSent;
    }

    
    public void storeMessage() {
        String jsonOutput = "{\n" +
                " \"messageNumber\": " + messageNumber + ",\n" +
                " \"messageId\": \"" + messageId + "\",\n" +
                " \"messageHash\": \"" + messageHash + "\",\n" +
                " \"recipient\": \"" + recipient + "\",\n" +
                " \"messageText\": \"" + messageText + "\"\n" +
                "}";
        
        try (FileWriter fileWriter = new FileWriter("messages.json", true)) {
            fileWriter.write(jsonOutput + "\n\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private String createMessageID() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

   
    public String getMessageId() { return messageId; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    
   
    public static void resetForTesting() {
        totalMessagesSent = 0;
        sentMessages.clear();
    }
}
