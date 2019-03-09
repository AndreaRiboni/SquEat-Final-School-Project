/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_utility;

import ap_communication.ClientConnector;
import ap_telegram.BotLogic;
import com.vdurmont.emoji.EmojiParser;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Utente
 */
public class Utility {

    public static boolean areNumbers(String... strings) {
        try {
            for (String string : strings) {
                Float.parseFloat(string);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getCart(long idchat) {
        try {
            String cart = ClientConnector.request("41;" + idchat + ";7").split(";")[1];
            if (cart == null || cart.equalsIgnoreCase("null")) {
                throw new Exception();
            }
            return cart;
        } catch (Exception ex) {
            return "carrello vuoto";
        }
    }

    public static String[] getProductsFrom(String cart) {
        return cart.split("-");
    }

    public static void addToCart(long idchat, String idloc, String idprod, String costo) {
        String cart = getCart(idchat);
        if (cart.equalsIgnoreCase("carrello vuoto")) {
            cart = "";
        }
        cart += idloc + "-" + idprod + "-" + costo + "-";
        try {
            ClientConnector.request("40;" + idchat + ";7;" + cart);
        } catch (Exception ex) {
            System.out.println("Impossibile aggiungere al carrello");
        }
    }

    public static void removeFromCart(long idchat, String idloc, String idprod, String costo) {
        String[] cart = getProductsFrom(getCart(idchat));
        System.out.println("carrello: " + cart.length);
        if (cart.length < 2) {
            return; //no prodotti nel carrello
        }
        String newcart = "";
        int toremove = -1;
        for (int i = 0; i < cart.length; i += 3) {

            System.out.println(cart[i] + "  " + idloc);
            System.out.println(cart[i + 1] + "  " + idprod);
            System.out.println(cart[i + 2] + "  " + costo);

            if (idloc.equalsIgnoreCase(cart[i])
                    && idprod.equalsIgnoreCase(cart[i + 1])
                    && costo.equalsIgnoreCase(cart[i + 2])) {
                System.out.println("NON SERVE");
                //da rimuovere
                toremove = i;
                break;
            }
        }
        if (toremove != -1) {
            for (int i = 0; i < cart.length; i += 3) {
                if (i != toremove) {
                    newcart += cart[i] + "-" + cart[i + 1] + "-" + cart[i + 2] + "-";
                }
            }
            try {
                ClientConnector.request("40;" + idchat + ";7;" + (newcart.equalsIgnoreCase("") ? "null" : newcart));
            } catch (Exception ex) {
                System.out.println("Impossibile rimuovere dal carrello");
            }
        }
    }

    public static String getCartMessages(String cart) {
        String[] products = cart.split("-");
        StringBuilder msg = new StringBuilder();
        float costo = 0;
        for (int i = 0; i < products.length; i += 3) {
            try {
                String nomeprod = ClientConnector.request("47;" + products[i + 1]).split(";")[1];
                msg.append("\n\n*Prodotto:* ").append(nomeprod).append("\n");
                msg.append("*Costo:* ").append(products[i + 2]).append("€");
                costo += Float.parseFloat(products[i + 2]);
            } catch (Exception ex) {
                System.out.println("Errore nel carrello");
            }
        }
        msg.append("\n\n*Costo Totale:* ").append(costo).append("€%SHOWCARTKB");
        return msg.toString();
    }

    public static String[] formatLatLong(String latlon) {
        String[] formatted = latlon.split(" ");
        formatted[0] = formatted[0].replace(",", "");
        return formatted;
    }

    public static String getAddress(long ChatID) {
        try {
            //ottengo idcliente
            String IDCliente = getIDCliente(ChatID);
            //ottengo cognome
            String latlon = ClientConnector.request("50;" + IDCliente + ";3").split(";")[1];
            String[] formll = formatLatLong(latlon);
            return ClientConnector.request("32;" + formll[0] + ";" + formll[1]).split(";")[1];
        } catch (Exception ex) {
            return "address_not_available_:(";
        }
    }

    public static String getIDCliente(long ChatID) {
        try {
            return ClientConnector.request("41;" + ChatID + ";" + 6).split(";")[1];
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getSurname(long ChatID) {
        try {
            //ottengo idcliente
            String IDCliente = getIDCliente(ChatID);
            //ottengo cognome
            return ClientConnector.request("50;" + IDCliente + ";1").split(";")[1];
        } catch (Exception ex) {
            return "surname_not_available_:(";
        }
    }

    public static boolean isLogged(long mittente) {
        try {
            return !ClientConnector.request("41;" + mittente + ";6").contains("null");
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getTelegramAddress(long mittente) {
        try {
            String[] latlon = ClientConnector.request("41;" + mittente + ";3").split(";")[1].split(" ");
            String lat = latlon[0].replace(",", "");
            String lon = latlon[1];
            return ClientConnector.request("32;" + lat + ";" + lon).split(";")[1];
        } catch (Exception ex) {
            return "casa tua";
        }
    }

    public static String latLonToAddress(String latlon) {
        try {
            String[] geo = formatLatLong(latlon);
            return ClientConnector.request("32;" + geo[0] + ";" + geo[1]).split(";")[1];
        } catch (Exception ex) {
            return "indirizzo sconosciuto";
        }
    }

    public static String[] getOrders(String msgx) {
        //idordine;nome locale; idlocale; nome prod; timestamp; costo; idfatto; indirizzo
        String[] params = popFirstElement(msgx.split(";"));
        if (params.length < 7) {
            return new String[]{"Nessun ordine disponibile"};
        }
        StringBuilder msg = new StringBuilder("*Ultimi ordini effettuati:*\n\n");
        for (int i = 0; i < params.length; i += 8) {
            msg.append(":star: _ordine n.#").append(params[i]).append("_   -  (").append(params[i+4]).append(")\n");
            msg.append("  *").append(params[i + 3]).append("* (").append(params[i+5]).append("€)\n");
            msg.append("  *presso:* ").append(params[i + 1]).append("\n");
            msg.append("  *consegnato in:* ").append(Utility.latLonToAddress(params[i + 7])).append("\n\n");
        }
        return new String[]{EmojiParser.parseToUnicode(msg.toString())};
    }

    public static String[] popFirstElement(String[] array) {
        String[] elements = new String[array.length - 1];
        for (int i = 1; i < array.length; i++) {
            elements[i - 1] = array[i];
        }
        return elements;
    }

    public static String addressToLatLon(String address) {
        try {
            return ClientConnector.request("29;"+address).split(";")[1];
        } catch (Exception ex) {
            return "0, 0";
        }
    }
}
