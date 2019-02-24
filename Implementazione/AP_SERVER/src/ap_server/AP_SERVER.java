package ap_server;

import ap_utility.ConfigurationLoader;
import ap_web.WebUtility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

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
    public static void main(String[] args) throws IOException {
        Thread console = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    try {
                        if (in.readLine().equalsIgnoreCase("end")) {
                            System.exit(0);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(AP_SERVER.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        console.start();
        //Inizializzo i driver
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("Impossibile caricare i driver");
        }
        //Inizializzo il server socket
        ServerSocket server = null;
        try {
            server = new ServerSocket(60240);
            System.out.println("Server attivo.");
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
                System.out.println("Errore nel main-loop");
            }
        }
    }

}
