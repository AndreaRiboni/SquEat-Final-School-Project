package ap_server;

import ap_utility.ConfigurationLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.PropertyConfigurator;

/**
 * Il progetto gestisce il server relativo al progetto "Food Delivery and
 * Reviewing".
 *
 * @author Andrea Riboni
 */
public class AP_SERVER {

    public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AP_SERVER.class);

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
                            log.info("server shutdown\n\n");
                            System.exit(0);
                        }
                    } catch (IOException ex) {
                        log.error("errore nella ricezione dell'input da tastiera");
                    }
                }
            }
        });
        console.start();

        //Inizializzo LOG4J
        PropertyConfigurator.configure(ConfigurationLoader.getNodeValue("log4j"));

        //Inizializzo i driver
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
        } catch (ClassNotFoundException ex) {
            log.error("errore nel caricamento dei driver di JDBC");
        }

        //Inizializzo il server socket
        ServerSocket server = null;
        try {
            server = new ServerSocket(60240);
            log.info("Server attivo");
        } catch (IOException ex) {
            log.error(ex);
        }
        //gestisco le richieste dei client
        while (true) {
            Socket client = null;
            try {
                client = server.accept();
                log.info("Connesso: " + client);
                new ServerProcess(client).start();
            } catch (IOException ex) {
                log.error("errore nel main loop");
            }
        }
    }

}
