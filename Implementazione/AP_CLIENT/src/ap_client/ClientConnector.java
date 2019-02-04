/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_client;

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

    private static final String IP_SERVER = "130.25.67.214";
    private static final int PORT = 60240;

    public static String request(String request) throws IOException, ClientException {
        Socket client = new Socket(IP_SERVER, PORT);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.println(request);
        if (Pacchetto.needResponse(request)) {
            System.out.println("risposta:");
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            return br.readLine();
        }
        return "null";
    }

}
