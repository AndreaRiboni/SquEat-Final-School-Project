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
                case "login":
                    System.out.println(ClientConnector.request("36;" + mittente + ";1"));
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
                    if (ClientConnector.request("000;" + mail + ";" + message).contains("true")) {
                        //login corretto
                        //imposto lo stato per andare alla home
                        ClientConnector.request("36;" + ChatID + ";4");
                        return getMessages("accessDone");
                    } else {
                        ClientConnector.request("36;" + ChatID + ";0");                        
                        return getMessages("loginERR");
                    }
                default:
                    return getMessages("unknown");
            }
        } catch (Exception ex) {
            System.err.println("ERRORE");
            return getMessages("unknown");
        }
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
                buttons.add(row);
                keyboard.setKeyboard(buttons);
                sender.setReplyMarkup(keyboard);
                break;
        }
    }
}
