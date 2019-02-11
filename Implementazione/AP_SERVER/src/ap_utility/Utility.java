/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_utility;

/**
 *
 * @author Utente
 */
public class Utility {
    
    public static int integerDistance(String dist){
        System.out.println("DIST: "+dist);
        System.out.println("DISTTT: "+dist.substring(0, dist.length()-3));
        return (int)(Float.parseFloat(dist.substring(0, dist.length()-3)));
    }
}
