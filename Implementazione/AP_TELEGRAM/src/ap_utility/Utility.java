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
        StringBuilder msg = new StringBuilder("*CARRELLO* END ");
        for (int i = 0; i < products.length; i += 3) {
            try {
                String nomeprod = ClientConnector.request("47;" + products[i + 1]).split(";")[1];
                msg.append("*Prodotto:* ").append(nomeprod).append("\n");
                msg.append("*Costo:* ").append(products[i + 2]).append("€ END");
            } catch (Exception ex) {
                System.out.println("Errore nel carrello");
            }
        }
        return msg.toString();
    }
}
