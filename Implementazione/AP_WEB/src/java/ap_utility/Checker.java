/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_utility;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * @author Utente
 */
public class Checker {

    public static boolean isMailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            System.out.println("    > Mail non valida");
            return false;
        }
        if (pat.matcher(email).matches()) {
            System.out.println("    > Mail valida");
            return true;
        } else {
            System.out.println("    > Mail non valida");
            return false;
        }
    }

    public static boolean isPhoneNumberValid(String PhoneNumb) {
        boolean okay = false;
        //validate phone numbers of format "1234567890"
        if (PhoneNumb.matches("\\d{10}")) {
            okay = true;
        } //validating phone number with -, . or spaces
        else if (PhoneNumb.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            okay = true;
        } //validating phone number with extension length from 3 to 5
        else if (PhoneNumb.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            okay = true;
        } //validating phone number where area code is in braces ()
        else if (PhoneNumb.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            okay = true;
        } //return false if nothing matches the input
        if (okay) {
            System.out.println("    > Numero valido");
            return true;
        } else {
            System.out.println("    > Numero non valido");
            return false;
        }
    }

    /**
     * Controlla se una stringa contenente LATITUDINE e LONGITUDINE è sensata. È
     * sensata se scritta nel formato "XX.XX, YY.YY"
     *
     * @param address
     * @return
     */
    public static boolean isAddressValid(String address) {
        try {
            String addresscopy = new String(address);
            int len = addresscopy.length();
            if (addresscopy.replace(" ", "").replace(",", "").length() == len - 2) {
                String[] latlon = address.replace(",", "").split(" ");
                Float.parseFloat(latlon[0]);
                Float.parseFloat(latlon[1]);
//                System.out.println("Indirizzo valido");
                return true;
            }
        } catch (Exception e) {
            System.out.println("    > Errore. Indirizzo non valido");
            return false;
        }
        System.out.println("    > Indirizzo non valido");
        return false;
    }
    
}
