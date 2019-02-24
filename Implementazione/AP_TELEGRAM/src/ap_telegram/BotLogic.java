/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_telegram;

import ap_communication.ClientConnector;
import ap_utility.ConfigurationLoader;
import ap_utility.Utility;
import com.vdurmont.emoji.EmojiParser;
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
                case "find_restaurant": //imposto lo stato a 12
                    System.out.println(ClientConnector.request("36;" + mittente + ";12"));
                    break;
                case "review":
                    System.out.println(ClientConnector.request("36;" + mittente + ";13"));
                    break;
                case "show_cart":
                    System.out.println(ClientConnector.request("36;" + mittente + ";14"));
                    break;
                case "show_orders":
                    System.out.println(ClientConnector.request("36;" + mittente + ";15"));
                    break;
                case "show_profile":
                    System.out.println(ClientConnector.request("36;" + mittente + ";16"));
                    break;
                case "logout":
                    //svuoto carrello
                    ClientConnector.request("43;" + mittente);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore #2");
        }
    }

    public static SendMessage[] askLocale(String message, long mittente) {
        try {
            String IDLocale = message.replace("ASKDATA", "");
            String[] locale = ClientConnector.request("7;" + IDLocale).split(";");
            String nomelocale = locale[1];
            String[] latlon = locale[2].split(",");
            String indirizzo = ClientConnector.request("32;" + latlon[0].trim() + ";" + latlon[1].trim());
            String recensioni = locale[3];
            String cell = locale[4];
            String[] menu = locale[5].split("-");
            SendMessage[] messages = new SendMessage[menu.length + 1];
//        for(String s : locale) System.out.println(s);
            StringBuilder msg = new StringBuilder();

            messages[0] = new SendMessage(mittente, "INFORMAZIONI SUL LOCALE\nNome: " + nomelocale + "\nIndirizzo: " + indirizzo + "\nTelefono: " + cell);
            for (int i = 1; i < messages.length; i++) {
                //menu
                messages[i] = new SendMessage(mittente, "configura questo messaggio nel BOTLOGIC");
            }
            return messages;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
                        ClientConnector.request("40;" + ChatID + ";6;" + responseid[responseid.length - 1]);
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
                    ClientConnector.request("40;" + ChatID + ";0;" + message);
                    //vado al livello successivo
                    ClientConnector.request("36;" + ChatID + ";6");
                    return getMessages("reg2");
                case "6": //ho ricevuto il cognome; chiedo mail
                    //salvo cognome
                    ClientConnector.request("40;" + ChatID + ";1;" + message);
                    //vado al livello successivo
                    ClientConnector.request("36;" + ChatID + ";7");
                    return getMessages("reg3");
                case "7": //ricevuto mail; chiedo indirizzo
                    //salvo mail
                    ClientConnector.request("40;" + ChatID + ";4;" + message);
                    //vado al livello successivo
                    ClientConnector.request("36;" + ChatID + ";8");
                    return getMessages("reg4");
                case "8": //ho ricevuto indirizzo; chiedo cell
                    //salvo indirizzo
                    ClientConnector.request("40;" + ChatID + ";3;" + message);
                    //vado al livello successivo
                    ClientConnector.request("36;" + ChatID + ";9");
                    return getMessages("reg5");
                case "9": //ho ricevuto cell; chiedo psw
                    //salvo cell
                    ClientConnector.request("40;" + ChatID + ";2;" + message);
                    //vado al livello successivo
                    ClientConnector.request("36;" + ChatID + ";10");
                    return getMessages("reg6");
                case "10": //ho ricevuto psw. testo
                    String email = ClientConnector.request("41;" + ChatID + ";4").split(";")[1];
                    String psw = message;
                    String nome = ClientConnector.request("41;" + ChatID + ";0").split(";")[1];
                    String cognome = ClientConnector.request("41;" + ChatID + ";1").split(";")[1];
                    String cell = ClientConnector.request("41;" + ChatID + ";2").split(";")[1];
                    String indirizzo = ClientConnector.request("41;" + ChatID + ";3").split(";")[1];
                    indirizzo = ClientConnector.request("29;" + indirizzo).split(";")[1];
                    String response = ClientConnector.request("2;" + email + ";" + psw + ";" + nome + ";" + cognome + ";" + cell + ";" + indirizzo + ";1");
                    System.out.println("RESPONSE REGISTER: "+response);
                    if (response.contains("false")) { //registrazione fallita
                        return concat(getMessages("regERR"), getMessages("welcome"));
                    } else { //registrazione ok
                        //salvo id utente
                        String[] resp = response.split(";");
                        String ID = resp[resp.length - 1];
                        //salvo nel db la relazione ChatID <--> ID Utente
                        ClientConnector.request("40;" + ChatID + ";6;" + ID);
                        //vado all'homepage
                        ClientConnector.request("36;" + ChatID + ";11");
                        return concat(getMessages("regOK"), getMessages("homepage"));
                    }
                case "11":
                    //Cerco di indovinare il messaggio con AI
                    //classificazione tra TROVA RISTO, RECENSISCI, MOSTRA CARRELLO, ORDINI
                    return getMessages("homepage");
                case "12": //Ho ricevuto "find-restaurant". chiedo la posizione
                    return getMessages("findrest");
                case "19": //ho ricevuto la richiesta di informazioni su un ristorante
                    if (message.startsWith("ASKDATA")) {
                        //vado al livello successivo
                        ClientConnector.request("36;" + ChatID + ";20");
                        return sendInfo(ChatID, message.replace("ASKDATA", ""));
                    } else {
                        return getMessages("NoRest");
                    }
                case "20":
                    if(message.startsWith("CART")){
                        String[] prod = message.split(";");
                        if(prod[1].equalsIgnoreCase("ADD")){
                            Utility.addToCart(ChatID, prod[2], prod[3], prod[4]);
                            return getMessages("addcart");
                        } else if(prod[1].equalsIgnoreCase("REMOVE")){
                            Utility.removeFromCart(ChatID, prod[2], prod[3], prod[4]);
                            return getMessages("removecart");
                        } else {
                            return getMessages("notvalidprod");
                        }
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

    public static SendMessage[] sendRestaurant(long mittente, float lat, float lon) {
        //attendo la ricezione di un ristorante
        SendMessage error = new SendMessage(mittente, "Non ci sono ristoranti nella tua zona.");
        try {
            String[] restaurants = popFirstElement(ClientConnector.request("5;" + lat + ", " + lon).split(";"));
            if (restaurants.length > 3) {
                //vado alla ricezione di ristoranti
                ClientConnector.request("36;" + mittente + ";19");
                //Ottengo il numero di ristoranti. Ogni ristorante ha 4 parametri.
                SendMessage[] messages = new SendMessage[(restaurants.length) / 4];
                StringBuilder[] contents = new StringBuilder[messages.length];
                int RestaurantNumber = 0;
                for (int i = 0; i < restaurants.length; i++) {
                    switch (i % 4) {
                        case 0:
                            contents[RestaurantNumber] = new StringBuilder("RISTORANTE #");
                            contents[RestaurantNumber].append(RestaurantNumber + 1).append("NEWLINENome: ").append(restaurants[i]);
                            break;
                        case 1:
                            String distance = ClientConnector.request("45;" + lat + ", " + lon + ";" + restaurants[i]).split(";")[1];
                            contents[RestaurantNumber].append("NEWLINEDistanza: ").append(distance);
                            break;
                        case 2:
                            contents[RestaurantNumber].append("NEWLINEPunteggio: ").append(restaurants[i]);
                            if (!restaurants[i].toLowerCase().contains("recension")) {
                                for (int o = 0; o < (int) (Float.parseFloat(restaurants[i])); o++) {
                                    contents[RestaurantNumber].append(":star:");
                                }
                            }
                            break;
                        case 3:
                            messages[RestaurantNumber] = new SendMessage(mittente, EmojiParser.parseToUnicode(contents[RestaurantNumber].toString().replace("NEWLINE", "\n")));
                            setKeyboard(messages[RestaurantNumber], "ASKREST" + restaurants[i]);
                            RestaurantNumber++;
                            break;
                    }
                }
                return messages;
            } else {
                System.out.println("pochi risto");
                return new SendMessage[]{error};
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new SendMessage[]{error};
        }
    }

    private static String[] popFirstElement(String[] array) {
        String[] elements = new String[array.length - 1];
        for (int i = 1; i < array.length; i++) {
            elements[i - 1] = array[i];
        }
        return elements;
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

    private static String[] sendInfo(long mittente, String IDLocale) throws Exception {
        //Richiedo i dati del locale e li ordino in stringhe identificative
        String[] info = ClientConnector.request("7;" + IDLocale).split(";");
        String nome = info[1];
        String address = info[2];
        String lat = address.substring(0, address.indexOf(","));
        String lon = address.substring(address.indexOf(",") + 1);
        //traduco lat lon in un indirizzo
        address = ClientConnector.request("32;" + lat + ";" + lon).split(";")[1];
        String punteggio = info[3].equalsIgnoreCase("0") ? "Nessuna recensione" : info[3];
        String cell = info[4];
        String[] menu = info[5].split("-");
        //invio un messaggio per prodotto, più quello di descrizione del locale
        String[] response = new String[menu.length + 1];
        StringBuilder firstmsg = new StringBuilder();
        //formatto la stringa locale
        firstmsg.append("IMG" + lat + ", " + lon + "*Nome:* ").append(nome).append("\n");
        firstmsg.append("*Indirizzo:* ").append(address).append("\n");
        firstmsg.append("*Media Recensioni:* ").append(punteggio).append("\n");
        firstmsg.append("*Telefono:* ").append(cell);
        response[0] = firstmsg.toString();
        //mi occupo dei messggi di menù
        int num = 1;
        for (String prodotto : menu) {
            //Divido e ordino per identificativi
            String[] data = prodotto.split("#");
            String IDProdotto = data[0];
            String costo = data[1];
            String nomeprod = data[2];
            String ingredienti = data[3];
            StringBuilder msgprod = new StringBuilder();
            //formatto la stringa prodotto
            msgprod.append("*").append(nomeprod).append("*\n*Costo:* ");
            msgprod.append(costo).append("€\n*Ingredienti:* ");
            //aggiungo la stringa ;idlocale;idprodotto;costo subito dopo gli ingredienti, per la callback
            msgprod.append(ingredienti).append(";").append(IDLocale).append(";").append(IDProdotto).append(";").append(costo);
            response[num++] = msgprod.toString();
        }
        return response;
    }

    public static void setKeyboard(SendMessage sender, String type) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        if (type.startsWith("ASKREST")) {
            String IDRestaurant = type.replace("ASKREST", "");
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(new InlineKeyboardButton().setText("Scegli").setCallbackData("ASKDATA" + IDRestaurant));
            buttons.add(row);
            keyboard.setKeyboard(buttons);
            sender.setReplyMarkup(keyboard);
            return;
        }
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
            case "homepage":
                List<List<InlineKeyboardButton>> homepagekb = new ArrayList<>();
                List<InlineKeyboardButton> hp1 = new ArrayList<>();
                hp1.add(new InlineKeyboardButton().setText("Trova Ristoranti").setCallbackData("find_restaurant"));
                homepagekb.add(hp1);
                List<InlineKeyboardButton> hp2 = new ArrayList<>();
                hp2.add(new InlineKeyboardButton().setText("Recensisci").setCallbackData("review"));
                homepagekb.add(hp2);
                List<InlineKeyboardButton> hp3 = new ArrayList<>();
                hp3.add(new InlineKeyboardButton().setText("Carrello").setCallbackData("show_cart"));
                hp3.add(new InlineKeyboardButton().setText("Ordini").setCallbackData("show_orders"));
                homepagekb.add(hp3);
                List<InlineKeyboardButton> hp5 = new ArrayList<>();
                hp5.add(new InlineKeyboardButton().setText("Mostra Profilo").setCallbackData("show_profile"));
                hp5.add(new InlineKeyboardButton().setText("Log Out").setCallbackData("logout"));
                homepagekb.add(hp5);
                keyboard.setKeyboard(homepagekb);
                break;
        }
        sender.setReplyMarkup(keyboard);
    }

    static void setFoodKeyboard(SendMessage msg, String idlocale, String idprodotto, String costo) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode("Aggiungi:gift:")).setCallbackData("CART;ADD;"+idlocale+";"+idprodotto+";"+costo));
        row.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode("Rimuovi:gift:")).setCallbackData("CART;REMOVE;"+idlocale+";"+idprodotto+";"+costo));
        buttons.add(row);
        keyboard.setKeyboard(buttons);
        msg.setReplyMarkup(keyboard);
    }
}
