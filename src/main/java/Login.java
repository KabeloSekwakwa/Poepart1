//Name: Kabelo Sekwakwa
//Student: 10509287
//Assistance with Regex logic provided by Google Gemini (2026).
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */
import java.util.regex.Pattern;
public class Login {

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }
    //This method validates that the username is 5 characters and contains an underscore.
    
    public boolean checkPasswordComplexity(String password) {
        boolean hasCap = !password.equals(password.toLowerCase());
        boolean hasNum = password.matches(".*\\d.*");
        boolean hasSpec = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE).matcher(password).find();
        
        return password.length() >= 8 && hasCap && hasNum && hasSpec;
    }

    public boolean checkCellPhoneNumber(String cellNumber) {
        return cellNumber.startsWith("+") && cellNumber.length() == 12;
    }

    public String registerUser(String username, String password) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        return "Password successfully captured.";
    }

    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            return "Welcome <user first name> ,<user last name> it is great to see you.";
        } else {
            return "Username or password incorrect, please try again";
            //Final Version for Poepart1.
        }
    }
}