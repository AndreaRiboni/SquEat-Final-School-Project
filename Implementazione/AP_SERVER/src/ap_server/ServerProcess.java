package ap_server;

import ap_utility.Pacchetto;
import ap_database.Database;
import ap_utility.Checker;
import ap_utility.Utility;
import ap_web.WebUtility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
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
    private Database db;

    /**
     * Costruttore
     *
     * @param client Socket relativo alla richiesta da eseguire
     */
    public ServerProcess(Socket client) {
        this.client = client;
        db = new Database();
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
            System.out.println("msg: " + msg);
            process(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                System.out.println("Impossibile chiudere il canale di comunicazione");
            }
        }
    }

    private void send(String msg) {
        out.println(msg);
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
        try {
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
        } catch (NumberFormatException e) {
            throw new ServerException("Tipo di messaggio non valido. Codice messaggio: " + msg[0]);
        }
    }

    /**
     * Verifica la validità di un account e la comunica al client richiedente
     *
     * @param msg 000;mail;password
     */
    private void login(String[] msg) throws ServerException {
        System.out.println("  > " + client + " chiede LOGIN");
        if (db.count(
                db.select("select * from Utente where Mail = '" + msg[1] + "' and Psw = '" + msg[2] + "'")
        ) > 0) {
            System.out.println("   > " + client + " login SUCCESSO");
            //l'utente esiste
            send(Pacchetto.incapsula(001, "true"));
        } else {
            System.out.println("   > " + client + " login RIFIUTATO");
            //l'utente non esiste
            send(Pacchetto.incapsula(001, "false"));
        }

    }

    private void register(String[] msg) throws ServerException {
        System.out.println("  > " + client + " chiede REGISTRAZIONE");
        boolean[] esito = {true, true, true, true};
        if (!Checker.isMailValid(msg[1]) || !Checker.isMailUnique(db, msg[1])) {
            esito[0] = false;
            System.out.println("Mail non valida");
        } else if (Checker.hasWhitespaces(msg[2])) {
            System.out.println("password non valida");
            esito[1] = false;
        } else if (!Checker.isPhoneNumberValid(msg[5]) || !Checker.isPhoneNumberUnique(db, msg[5])) {
            System.out.println("numero non valido");
            esito[2] = false;
        } else if (!Checker.isAddressValid(msg[6])) {
            System.out.println("indirizzo non valido");
            esito[3] = false;
        }
        if (esito[0] && esito[1] && esito[2] && esito[3]) {
            if (db.register(msg)) {
                System.out.println("   > " + client + " registrazione SUCCESSO");
                send(Pacchetto.incapsula(003, esito[0] + "", esito[1] + "", esito[2] + "", esito[3] + ""));
            }
        } else {
            System.out.println("   > " + client + " registrazione RIFIUTATA");
            send(Pacchetto.incapsula(003, esito[0] + "", esito[1] + "", esito[2] + "", esito[3] + ""));
        }
    }

    private void inviaLocali(String[] msg) {
        //nel raggio di 8km
        ArrayList<String[]> locali = db.stringify(db.select("select Nome, Indirizzo, Punteggio, NumRecensioni from Locale"));
        ArrayList<String> query = new ArrayList<>();
        for (String[] locale : locali) {
            if (Utility.integerDistance(WebUtility.getDistance(msg[1], locale[1])) <= 8) {
                String punteggio = locale[3].equalsIgnoreCase("0") ? "Nessuna Recensione" : Integer.parseInt(locale[2]) / Integer.parseInt(locale[4]) + "";
                query.add(locale[0] + ";" + locale[1] + ";" + punteggio);
            }
        }
        StringBuilder response = new StringBuilder("006");
        for (String locale : query) {
            response.append(";").append(locale);
        }
        send(response.toString());
    }

    private void inviaInfoLocale(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void aggiungiAlCarrello(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void rimuoviDaCarrello(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void acquista(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void recensici(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void track(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void aggiungiLocale(String[] msg) {
        System.out.println("  > " + client + " chiede AGGIUNTA LOCALE");
        try {
            if (Checker.isNumber(msg[2]) && Checker.isNumber(msg[3]) && Checker.isPhoneNumberValid(msg[4]) && (Checker.isThatTypeOfUser(db, msg[5], "1") || Checker.isThatTypeOfUser(db, msg[5], "0"))) {
                if (db.addLocale(msg)) {
                    System.out.println("   > " + client + " aggiunta locale RIUSCITA");
                    send(Pacchetto.incapsula(001, "true"));
                } else {
                    System.out.println("   > " + client + " aggiunta locale FALLITA");
                    send(Pacchetto.incapsula(001, "false"));
                }
            }
        } catch (ServerException ex) {
            System.out.println(ex.getMessage());
            System.out.println("   > " + client + " aggiunta locale FALLITA");
            try {
                send(Pacchetto.incapsula(001, "false"));
            } catch (ServerException ex1) {
                System.out.println(ex1.getMessage());
            }

        }
    }

    private void assegnaOrdine(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void aggiungiProdMenu(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void getProdottiComprati(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void getOrdiniRicevuti(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void aggiungiPost(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void getFeed(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void aggiungiCommento(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void aggiungiVoto(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
