/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_utility;

import java.util.ArrayList;

/**
 *
 * @author Utente
 */
public class Utility {

    public static int integerDistance(String dist) {
        return (int) (Float.parseFloat(dist.substring(0, dist.length() - 3)));
    }

    public static void print(ArrayList<String[]> table) {
        for (int i = 0; i < table.size(); i++) {
            for (int o = 0; o < table.get(i).length; o++) {
                System.out.print(table.get(i)[o] + "   ");
            }
            System.out.println("");
        }
    }
}
