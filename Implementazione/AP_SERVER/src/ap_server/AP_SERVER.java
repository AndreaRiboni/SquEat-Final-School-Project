package ap_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Il progetto gestisce il server relativo al progetto "Food Delivery and
 * Reviewing".
 *
 * @author Andrea Riboni
 */
public class AP_SERVER {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Inizializzo i driver
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        //Inizializzo il server socket
        ServerSocket server = null;
        try {
            server = new ServerSocket(60240);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //gestisco le richieste dei client
        while (true) {
            Socket client = null;
            try {
                client = server.accept();
                System.out.println("Connesso: " + client);
                new ServerProcess(client).start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}