/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_telegram;

import ap_communication.ClientConnector;
import ap_utility.ConfigurationLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 *
 * @author ribon
 */
public class BotLogic {

    public static void callAction(String message, long mittente) {
        try {
            switch (message) {
                case "login": //se ricevo "login", imposto lo stato a 1
                    System.out.println(ClientConnector.request("36;" + mittente + ";1"));
                    break; //se ricevo "register", imposto lo stato a 4
                case "register":
                    System.out.println(ClientConnector.request("36;" + mittente + ";4"));
                    break;
            }
        } catch (Exception e) {
            System.out.println("Errore #2");
        }
    }

    public static String[] getAnswer(String message, long ChatID, String status) {
        try {
            System.out.println("Ottengo le risposte per " + message + ". STATO = " + status);
            if (status == null) {
                return new String[]{"Errore #0"};
            }
            switch (status) {
                case "0": //Messaggio di Benvenuto
                    return getMessages("welcome");
                case "1": //Primo messaggio di Login: chiedo mail
                    //imposto lo stato al livello successivo
                    ClientConnector.request("36;" + ChatID + ";2");
                    return getMessages("login1");
                case "2":
                    //salvo la mail appena ricevuta nel db
                    ClientConnector.request("37;" + ChatID + ";" + message);
                    //aggiorno lo stato
                    ClientConnector.request("36;" + ChatID + ";3");
                    return getMessages("login2");
                case "3": //Ho appena ricevuto la password
                    //verifico mail e psw
                    //ottengo la mail
                    String mail = ClientConnector.request("38;" + ChatID).split(";")[1];
                    //Testo il login
                    String esito = ClientConnector.request("000;" + mail + ";" + message);
                    if (esito.contains("true")) {
                        //login corretto
                        //imposto lo stato per andare alla home
                        ClientConnector.request("36;" + ChatID + ";11");
                        //salvo nel db la relazione ChatID <--> ID Utente
                        String[] responseid = esito.split(";");
                        ClientConnector.request("40;"+ChatID+";6;"+responseid[responseid.length-1]);
                        return concat(getMessages("loginOK"), getMessages("homepage"));
                    } else {
                        //Imposto lo stato iniziale
                        ClientConnector.request("36;" + ChatID + ";0");
                        return concat(getMessages("loginERR"), getMessages("welcome"));
                    }
                case "4": //Primo messaggio della registrazione
                    //vado al livello successivo
                    ClientConnector.request("36;" + ChatID + ";5");
                    return getMessages("reg1");
                case "5": //ho ricevuto il nome; chiedo cognome
                    //salvo il nome nel db
                    ClientConnector.request("40;"+ChatID+";0;"+message);
                    //vado al livello successivo
                    ClientConnector.request("36;" + ChatID + ";6");
                    return getMessages("reg2");
                case "6": //ho ricevuto il cognome; chiedo mail
                    //salvo cognome
                    ClientConnector.request("40;"+ChatID+";1;"+message);
                    //vado al livello successivo
                    ClientConnector.request("36;" + ChatID + ";7");
                    return getMessages("reg3");
                case "7": //ricevuto mail; chiedo indirizzo
                    //salvo mail
                    ClientConnector.request("40;"+ChatID+";4;"+message);
                    //vado al livello successivo
                    ClientConnector.request("36;" + ChatID + ";8");
                    return getMessages("reg4");
                case "8": //ho ricevuto indirizzo; chiedo cell
                    //salvo indirizzo
                    ClientConnector.request("40;"+ChatID+";3;"+message);
                    //vado al livello successivo
                    ClientConnector.request("36;" + ChatID + ";9");
                    return getMessages("reg5");
                case "9": //ho ricevuto cell; chiedo psw
                    //salvo cell
                    ClientConnector.request("40;"+ChatID+";2;"+message);
                    //vado al livello successivo
                    ClientConnector.request("36;" + ChatID + ";10");
                    return getMessages("reg6");
                case "10": //ho ricevuto psw. testo
                    String email = ClientConnector.request("41;"+ChatID+";4").split(";")[1];
                    String psw = message;
                    String nome = ClientConnector.request("41;"+ChatID+";0").split(";")[1];
                    String cognome = ClientConnector.request("41;"+ChatID+";1").split(";")[1];
                    String cell = ClientConnector.request("41;"+ChatID+";2").split(";")[1];
                    String indirizzo = ClientConnector.request("41;"+ChatID+";3").split(";")[1];
                    indirizzo = ClientConnector.request("29;"+indirizzo).split(";")[1];
                    String response = ClientConnector.request("2;"+email+";"+psw+";"+nome+";"+cognome+";"+cell+";"+indirizzo+";1");
                    if(response.contains("false")){ //registrazione fallita
                        return concat(getMessages("regERR"), getMessages("welcome"));
                    } else { //registrazione ok
                        //salvo id utente
                        String[] resp = response.split(";");
                        String ID = resp[resp.length-1];
                        //salvo nel db la relazione ChatID <--> ID Utente
                        ClientConnector.request("40;"+ChatID+";6;"+ID);
                        return concat(getMessages("regOK"), getMessages("homepage"));
                    }
                default:
                    return getMessages("unknown");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("ERRORE");
            return getMessages("unknown");
        }
    }

    private static String[] concat(String[]... answers) {
        //calcolo lunghezza totale
        int len = 0;
        for (String[] ans : answers) {
            for (String val : ans) {
                len++;
            }
        }
        String[] concat = new String[len];
        len = 0;
        for (String[] ans : answers) {
            for (String val : ans) {
                concat[len++] = val;
            }
        }
        return concat;
    }

    public static String getStatus(long ChatID) {
        try {
            String response = ClientConnector.request("34;" + ChatID);
            System.out.println("Response: " + response);
            return response.split(";")[1];
        } catch (Exception ex) {
            return null;
        }
    }

    public static String[] getMessages(String NodeName) {
        try {
            return ConfigurationLoader.getNodeValue(NodeName).split("END");
        } catch (Exception e) {
            return new String[]{"Errore #1"};
        }
    }

    public static void setKeyboard(SendMessage sender, String type) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        switch (type) {
            case "LogReg":
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                List<InlineKeyboardButton> row = new ArrayList<>();
                row.add(new InlineKeyboardButton().setText("Login").setCallbackData("login"));
                row.add(new InlineKeyboardButton().setText("Registrati").setCallbackData("register"));
                buttons.add(row);
                keyboard.setKeyboard(buttons);
                sender.setReplyMarkup(keyboard);
                break;
        }
    }
}
