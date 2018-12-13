/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webtest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

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
