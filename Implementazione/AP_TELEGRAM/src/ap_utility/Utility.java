/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_utility;

import ap_communication.ClientConnector;
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
            if(cart == null || cart.equalsIgnoreCase("null")) throw new Exception();
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
        System.out.println("cart: "+cart);
        cart += idloc + "-" + idprod + "-" + costo + "-";
        try {
            ClientConnector.request("40;" + idchat + ";7;" + cart);
        } catch (Exception ex) {
            System.out.println("Impossibile aggiungere al carrello");
        }
    }

    public static void removeFromCart(long idchat, String idloc, String idprod, String costo) {
        String[] cart = getProductsFrom(getCart(idchat));
        if (cart.length == 1) {
            return; //no prodotti nel carrello
        }
        String[] newcart = new String[cart.length - 3];
        int toremove = -1;
        for (int i = 0; i < cart.length; i += 3) {
            if (idloc.equalsIgnoreCase(cart[i])
                    && idprod.equalsIgnoreCase(cart[i + 1])
                    && costo.equalsIgnoreCase(cart[i + 2])) {
                //da rimuovere
                toremove = i;
                break;
            }
        }
        if (toremove != -1) {
            int index = 0;
            for (int i = 0; i < cart.length; i += 3) {
                if (i != toremove) {
                    newcart[index++] = cart[i];
                }
            }
            try {
                ClientConnector.request("40;" + idchat + ";7;" + cart);
            } catch (Exception ex) {
                System.out.println("Impossibile rimuovere dal carrello");
            }
        }
    }
}
