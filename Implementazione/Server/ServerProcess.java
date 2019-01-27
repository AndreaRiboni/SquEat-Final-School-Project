package ap_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread utilizzato per gestire le richieste
 *
 * @author Andrea Riboni
 */
public class ServerProcess extends Thread {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    @Deprecated
    private boolean status;

    /**
     * Costruttore
     *
     * @param client Socket relativo alla richiesta da eseguire
     */
    public ServerProcess(Socket client) {
        this.client = client;
        status = true;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processa una singola richiesta
     */
    public void run() {
        try {
            String msg = in.readLine();
            process(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Decapsula una richiesta e la processa.
     * 
     * @param input richiesta da processare
     * @throws ServerException errore
     */
    private void process(String input) throws ServerException {
        System.out.println("> From: " + client + "\n--> Message: " + input);
        String msg[] = null;
        msg = Pacchetto.estrai(input);
        if (!Pacchetto.verifica(msg)) {
            throw new ServerException("è stato riscontrato un errore con il seguente pacchetto: " + input);
        }
        switch (Integer.parseInt(msg[0])) {
            case 0:
                login(msg);
                break;
            case 1:
                //richiesta SC
                break;
            case 2:
                register(msg);
                break;
            case 3:
                //richiesta alla servlet
                break;
            case 4:
                break;
            case 5:
                inviaLocali(msg);
                break;
            case 6:
                //richiesta sc
                break;
            case 7:
                inviaInfoLocale(msg);
                break;
            case 8:
                //richiesta sc
                break;
            case 9:
                aggiungiAlCarrello(msg);
                break;
            case 10:
                rimuoviDaCarrello(msg);
                break;
            case 11:
                acquista(msg);
                break;
            case 12:
                //richiesta sc
                break;
            case 13:
                recensici(msg);
                break;
            case 14:
                track(msg);
                break;
            case 15:
                //richiesta sc
                break;
            case 16:
                aggiungiLocale(msg);
                break;
            case 17:
                //richiesta sc
                break;
            case 18:
                assegnaOrdine(msg); //serve?
                break;
            case 19:
                aggiungiProdMenu(msg);
                break;
            case 20:
                getProdottiComprati(msg);
                break;
            case 21:
                //richiesta sc
                break;
            case 22:
                getOrdiniRicevuti(msg);
                break;
            case 23:
                //richiesta sc
                break;
            case 24:
                aggiungiPost(msg);
                break;
            case 25:
                getFeed(msg);
                break;
            case 26:
                //richiesta sc
                break;
            case 27:
                aggiungiCommento(msg);
                break;
            case 28:
                aggiungiVoto(msg);
                break;
            default:
                throw new ServerException("Tipo di messaggio non riconosciuto. Codice messaggio: " + msg[0]);
        }
    }
}
