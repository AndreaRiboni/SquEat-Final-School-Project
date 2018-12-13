/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webtest;

import java.io.IOException;

/**
 *
 * @author Utente
 */
public class WebTest {

    

    public static void main(String[] args) throws IOException {
        System.out.println(
                WebUtility.getHTMLTagValue(
                        WebUtility.stringToHTML(
                                WebUtility.getHTMLPage(
                                        WebUtility.getParametricURL(
                                                "localhost/HTTP4Java/index.php",
                                                "nome",
                                                "andrea"
                                        )
                                )
                        ),
                        "nome"
                )
        );
    }

}
