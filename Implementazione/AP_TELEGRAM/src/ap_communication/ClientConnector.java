/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_communication;

import ap_utility.ConfigurationLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author riboni.andrea
 */
public class ClientConnector {

    private static String IP_SERVER;
    private static final int PORT = 60240;

    public static String request(String request) throws IOException, Exception {
        IP_SERVER = ConfigurationLoader.getNodeValue("ipserver");
        Socket client = new Socket(IP_SERVER, PORT);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.println(request);
        System.out.println("Richiesta inviata");
//        if (Pacchetto.needResponse(request)) {
        System.out.println("risposta:");
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        return br.readLine();
//        }
//        return "null";
    }

}
