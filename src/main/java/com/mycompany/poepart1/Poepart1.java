/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poepart1;

/**
 *
 * @author Student
 */
import java.util.Scanner;
public class Poepart1 {



    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        // --- Registration Phase ---
        System.out.println("\n------Registration------");

        System.out.print("First Name: ");
        String firstName = input.nextLine();
    
        System.out.print("Last Name: ");
        String lastName = input.nextLine();
        
        System.out.println("-----------------------------------");
        
        // --- USERNAME VALIDATION ---
        System.out.print("Enter Username: ");
        String username = input.nextLine();

        boolean isUsernameValid = username.contains("_") && username.length() <= 5;

        if (isUsernameValid) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
        }

        System.out.println("-----------------------------------");

        // --- PASSWORD VALIDATION ---
        System.out.print("Enter Password: ");
        String password = input.nextLine();

        boolean isPasswordValid = password.length() >= 8 &&
                                 password.matches(".*[A-Z].*") &&
                                 password.matches(".*[0-9].*") &&
                                 password.matches(".*[^a-zA-Z0-9].*");

        if (isPasswordValid) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
        }
        
        System.out.println("-----------------------------------");
        
        System.out.print("Enter cellphone number: ");
        String phoneNumber = input.nextLine();
        
        boolean isFormattedCorrectly = phoneNumber.startsWith("+27") && phoneNumber.length() <=13;
        
        if (isFormattedCorrectly) {
        System.out.println("Cellphone number succesfully added.");    
        }
        else{ System.out.println("Cellphone number incorrectly formatted or does not contain international code.");
        }
        

        System.out.println("\n------------Login------------");
        System.out.print("Enter Username: ");
        String loginUser = input.nextLine();
        System.out.print("Enter Password: ");
        String loginPass = input.nextLine();

        System.out.println("\n-----------------------------");
        
        if (loginUser.equals(username) && loginPass.equals(password)) {
            System.out.println("Welcome " + firstName + ", " + lastName + " it is great to see you again.");
        } else {
            System.out.println("Username or password incorrect, please try again.");
        }

        input.close();
    }
}
