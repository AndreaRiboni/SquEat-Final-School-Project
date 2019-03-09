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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

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
    private org.apache.log4j.Logger log;

    /**
     * Costruttore
     *
     * @param client Socket relativo alla richiesta da eseguire
     */
    public ServerProcess(Socket client) {
        log = AP_SERVER.log;
        System.out.println("Client connesso");
        this.client = client;
        db = new Database();
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            log.error(e);
        }
    }

    /**
     * Processa una singola richiesta
     */
    @Override
    public void run() {
        try {
            String msg = in.readLine();
            log.info("ricevuto: " + msg);
            process(msg);
        } catch (Exception e) {
            log.error(e);
        } finally {
            try {
                client.close();
                db.close();
                log.debug("chiusura connessioni terminata: " + client);
            } catch (IOException ex) {
                log.error("impossibile chiudere i canali di comunicazione");
            }
        }
    }

    private void send(String msg) {
        log.info("invio di un messaggio: "+msg);
        out.println(msg);
    }

    /**
     * Decapsula una richiesta e la processa.
     *
     * @param input richiesta da processare
     * @throws ServerException errore
     */
    private void process(String input) throws Exception {
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
                case 2:
                    register(msg);
                    break;
                case 4:
                    break;
                case 5:
                    inviaLocali(msg);
                    break;
                case 7:
                    inviaInfoLocale(msg);
                    break;
                case 11:
                    acquista(msg);
                    break;
                case 13:
                    recensici(msg);
                    break;
                case 14:
                    track(msg);
                    break;
                case 16:
                    aggiungiLocale(msg);
                    break;
                case 19:
                    aggiungiNuovoProdMenu(msg);
                    break;
                case 20:
                    getProdottiComprati(msg);
                    break;
                case 22:
                    getOrdiniRicevuti(msg);
                    break;
                case 24:
                    aggiungiPost(msg);
                    break;
                case 25:
                    getFeed(msg);
                    break;
                case 27:
                    aggiungiCommento(msg);
                    break;
                case 28:
                    aggiungiVoto(msg);
                    break;
                case 31:
                    aggiungiProdEsistMenu(msg);
                    break;
                case 29:
                    fromAddressToLatLong(msg);
                    break;
                case 32:
                    fromLatLongToAddress(msg);
                    break;
                case 34:
                    getStatoUtente(msg);
                    break;
                case 36:
                    aggiornaStatoUtente(msg);
                    break;
                case 37:
                    setMailById(msg);
                    break;
                case 38:
                    getMailById(msg);
                    break;
                case 40:
                    setTelegramUserValue(msg);
                    break;
                case 41:
                    returnTelegramUserValue(msg);
                    break;
                case 43:
                    logTelegramUserOut(msg);
                    break;
                case 45:
                    calculateDistance(msg);
                    break;
                case 47:
                    getNomeProdotto(msg);
                    break;
                case 49:
                    svotaCarrello(msg);
                    break;
                case 50:
                    returnUserValue(msg);
                    break;
                default:
                    log.warn("ricevuto un messaggio sconosciuto: " + msg[0]);
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
    private void login(String[] msg) throws Exception {
        ResultSet table = db.select("select * from Utente where Mail = '" + msg[1] + "' and Psw = '" + msg[2] + "'");
        if (db.count(table) > 0) {
            table.first();
            String idutente = table.getString(1);
            //l'utente esiste
            send(Pacchetto.incapsula(1, "true", idutente));
        } else {
            //l'utente non esiste
            send(Pacchetto.incapsula(1, "false", "null"));
        }

    }

    private void register(String[] msg) throws ServerException {
        boolean[] esito = {true, true, true, true};
        if (!Checker.isMailValid(msg[1]) || !Checker.isMailUnique(db, msg[1])) {
            esito[0] = false;
            log.warn("mail non valida");
        } else if (!Checker.isPhoneNumberValid(msg[5]) || !Checker.isPhoneNumberUnique(db, msg[5])) {
            esito[2] = false;
            log.warn("cellulare non valido");
        } else if (!Checker.isAddressValid(msg[6])) {
            esito[3] = false;
            log.warn("cellulare non valido");
        }
        if (esito[0] && esito[1] && esito[2] && esito[3]) {
            if (db.register(msg)) {
                String idutente = db.stringify(db.select("select id from Utente where mail = '" + msg[1] + "'")).get(0)[0];
                send(Pacchetto.incapsula(003, esito[0] + "", esito[1] + "", esito[2] + "", esito[3] + "", idutente));
            }
        } else {
            send(Pacchetto.incapsula(003, esito[0] + "", esito[1] + "", esito[2] + "", esito[3] + "", "null"));
        }
    }

    private void inviaLocali(String[] msg) {
        //nel raggio di 8km
        ArrayList<String[]> locali = db.stringify(db.select("select Nome, Indirizzo, Punteggio, NumRecensioni, ID from Locale"));
        ArrayList<String> query = new ArrayList<>();
        for (String[] locale : locali) {
            if (Utility.integerDistance(WebUtility.getDistance(msg[1], locale[1])) <= 8) {
                String punteggio = locale[3].equalsIgnoreCase("0") ? "Nessuna Recensione" : Integer.parseInt(locale[2]) / Integer.parseInt(locale[3]) + "";
                query.add(locale[0] + ";" + locale[1] + ";" + punteggio + ";" + locale[4]);
            }
        }
        StringBuilder response = new StringBuilder("006");
        for (String locale : query) {
            response.append(";").append(locale);
        }
        send(response.toString());
    }

    /**
     *
     * @param msg MSG 0: IDLOCALE
     */
    private void inviaInfoLocale(String[] msg) {
        //008,nome,indirizzo,punteggio,cellulare,menu
        ArrayList<String[]> locale = db.stringify(db.select("select Nome, Indirizzo, Cellulare, Punteggio, NumRecensioni from Locale where ID = " + msg[1]));
        int punteggio = 0;
        if (Integer.parseInt(locale.get(0)[4]) != 0) {
            punteggio = Integer.parseInt(locale.get(0)[3]) / Integer.parseInt(locale.get(0)[4]);
        }
        String nome = locale.get(0)[0];
        String cellulare = locale.get(0)[2];
        String indirizzo = locale.get(0)[1];
        //ottengo il menu
        ArrayList<String[]> menu = db.stringify(db.select("select IDProdotto, Costo from MenuLoc where IDLocale = " + msg[1]));
        StringBuilder prodotti = new StringBuilder();
        for (int i = 0; i < menu.size(); i++) {
            String[] prodotto = db.stringify(db.select("select Nome, Ingredienti from Prodotto where ID = " + menu.get(i)[0])).get(0);
            prodotti.append(menu.get(i)[0]).append("#").append(menu.get(i)[1]).append("#").append(prodotto[0]).append("#").append(prodotto[1]);
            if (i < menu.size() - 1) {
                prodotti.append("-");
            }
        }
        try {
            send(Pacchetto.incapsula(8, nome, indirizzo, punteggio + "", cellulare, prodotti.toString()));
        } catch (ServerException ex) {
            send("8;null;null;null;null;null");
            log.error(ex);
        }

    }

    private void acquista(String[] msg) {
        //msg[1] = idcliente, msg[2] = carrello
        //carrello: idlocale, idprodotto, costo
        String IdCliente = msg[1];
        String indirizzo = msg[3];
        boolean esito = true;
        //esiste?
        if (db.userIdExists(IdCliente)) {
            //salvo il carrello
            String[] carrello = msg[2].split("-");
            //acquisto ogni prodotto del carrello
            for (int i = 0; i < carrello.length; i += 3) {
                //ottengo l'id fattorino del locale
                String IdFattorino = db.getIdFattorinoFromLocale(carrello[i]);
                String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new GregorianCalendar().getTime());
                int val = db.update("insert into Ordine ("
                        + "IDCliente,"
                        + "IDLocale,"
                        + "IDProdotto,"
                        + "Timestamp,"
                        + "Costo,"
                        + "Stato,"
                        + "IDFattorino,"
                        + "Indirizzo) values (" + IdCliente + ", " + carrello[i] + ", " + carrello[i + 1] + ", '" + timestamp + "', " + carrello[i + 2] + ", 0, " + IdFattorino + ", '" + indirizzo + "')");
                if (val <= 0) {
                    esito = false;
                }
            }
            send("44;" + esito);
        } else {
            send("44;false");
        }

    }

    private void recensici(String[] msg) {
        //IDLOCALE, STELLINE
        if (db.update("update Locale set Punteggio = Punteggio + " + msg[2] + ", NumRecensioni = NumRecensioni + 1 where ID = " + msg[1]) > -1) {
            send("44;true");
        } else {
            send("44;false");
        }
    }

    private void track(String[] msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void aggiungiLocale(String[] msg) {
        try {
            if (Checker.isNumber(msg[2]) && Checker.isNumber(msg[3]) && Checker.isPhoneNumberValid(msg[4]) && (Checker.isThatTypeOfUser(db, msg[5], "1") || Checker.isThatTypeOfUser(db, msg[5], "0"))) {
                if (db.addLocale(msg)) {
                    send(Pacchetto.incapsula(44, "true"));
                } else {
                    send(Pacchetto.incapsula(44, "false"));
                }
            }
        } catch (ServerException ex) {
            log.warn("impossibile aggiungere un locale: " + ex);
            try {
                send(Pacchetto.incapsula(44, "false"));
            } catch (ServerException ex1) {
            }
        }
    }

    private void aggiungiProdEsistMenu(String[] msg) {
        String IdLocale = msg[1];
        String IdProdotto = msg[2];
        String costo = msg[3];
        boolean esito = db.update("insert into MenuLoc (IDLocale, IDProdotto, Costo) values ("
                + IdLocale + ","
                + IdProdotto + ","
                + costo) > 0;
        send("44;" + esito);
    }

    private void getProdottiComprati(String[] msg) {
        //msg1 = idcliente
        ArrayList<String[]> table = db.stringify(db.select("SELECT IDOrdine, Nome, IDLocale, IDProdotto, Timestamp, Costo, IDFattorino, Ordine.Indirizzo from Ordine, Locale where Locale.ID = Ordine.IDLocale and IDCliente = " + msg[1] + " order by IDOrdine DESC LIMIT 10"));
        StringBuilder response = new StringBuilder();
        for (String[] row : table) {
            int index = 0;
            for (String val : row) {
                if (index == 3) {
                    //ottengo il nome del prodotto a partire dall'id
                    String nome = db.getFirstRow(db.select("SELECT Nome from Prodotto where ID = " + val))[0];
                    response.append(nome).append(";");
                } else {
                    response.append(val).append(";");
                }
                index++;
            }
        }
        String risp = response.toString();
        try{
        send("21;" + risp.substring(0, risp.length() - 1));
        } catch (Exception e){
            send("21;null");
        }
    }

    private void getOrdiniRicevuti(String[] msg) {
        //msg1 = idcliente
        ArrayList<String[]> table = db.stringify(db.select("SELECT IDOrdine, IDProdotto, IDCliente, Timestamp, Costo, IDFattorino from Ordine, Locale where locale.ID = ordine.IDLocale and IDLocale = " + msg[1]));
        StringBuilder response = new StringBuilder();
        for (String[] row : table) {
            int index = 0;
            for (String val : row) {
                switch (index) {
                    case 1:
                        //ottengo il nome del prodotto a partire dall'id
                        String nome = db.getFirstRow(db.select("SELECT Nome from Prodotto where ID = " + val))[0];
                        response.append(nome).append(";");
                        break;
                    case 2:
                        //ottengo l'indirizzo cliente a partire dall'id
                        String indirizzo = db.getFirstRow(db.select("SELECT Indirizzo from Utente where ID = " + val))[0];
                        response.append(indirizzo).append(";");
                        break;
                    default:
                        response.append(val).append(";");
                        break;
                }
                index++;
            }
        }
        String risp = response.toString();
        send("23;" + risp.substring(0, risp.length() - 1));
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

    private void aggiungiNuovoProdMenu(String[] msg) {
        String nomeprod = msg[1];
        String ingredienti = msg[2].replace("-", ",");
        String idlocale = msg[3];
        String costo = msg[4];
        boolean esito = true;
        //aggiungo il prodotto al db
        esito = db.update("insert into Prodotto (Nome, Ingredienti) values ('" + nomeprod + "', '" + ingredienti + "')") > 0;

        ArrayList<String[]> prodotti = db.stringify(db.select("select ID from Prodotto"));
        String IdProdotto = null;
        try {
            IdProdotto = prodotti.get(prodotti.size() - 1)[0];
        } catch (Exception e) {
            send("44;false");
        }
        //aggiungo il prodotto al menu del locale
        esito = esito && db.update("insert into MenuLoc (IDLocale, IDProdotto, Costo) values ("
                + idlocale + ", "
                + IdProdotto + ","
                + costo + ")") > 0;
        send("44;" + esito);
    }

    private void fromAddressToLatLong(String[] msg) {
        send("30;" + WebUtility.getLatLong(msg[1]));
    }

    private void fromLatLongToAddress(String[] msg) {
        send("33;" + WebUtility.getAddress(msg[1], msg[2]));
    }

    private void getStatoUtente(String[] msg) {
        try {
            ResultSet table = db.select("select Stato from Telegram where IDChat = " + msg[1]);
            if (db.count(table) > 0) {
                table.first();
                send("35;" + table.getString(1));
            } else {
                db.update("insert into Telegram (IDChat, stato) values (" + msg[1] + ", 0)");
                send("35;0");
            }
        } catch (SQLException ex) {
            send("35;-1");
        }
    }

    private void aggiornaStatoUtente(String[] msg) {
        db.update("update Telegram set stato = " + msg[2] + " where IDChat = " + msg[1]);
        send("44;idc");
    }

    private void setMailById(String[] msg) {
        db.update("update Telegram set mail = '" + msg[2] + "' where IDChat = " + msg[1]);
        send("44;idc");
    }

    private void getMailById(String[] msg) {
        try {
            send("39;" + db.stringify(db.select("select mail from Telegram where IDChat = " + msg[1])).get(0)[0]);
        } catch (Exception e) {
            send("39;null");
        }
    }

    private String getUserValue(String num) {
        switch (num) {
            case "0":
                return "nome";
            case "1":
                return "cognome";
            case "2":
                return "cellulare";
            case "3":
                return "indirizzo";
            case "4":
                return "mail";
            case "6":
                return "IDUtente";
            case "7":
                return "carrello";
            default:
                return "null";
        }
    }

    private void setTelegramUserValue(String[] msg) {
        if (msg[3].equalsIgnoreCase("null")) {
            send("44;" + ((db.update("update Telegram set " + getUserValue(msg[2]) + " = null where IDChat = " + msg[1]) > 0) ? "true" : "false"));
        } else {
            send("44;" + ((db.update("update Telegram set " + getUserValue(msg[2]) + " = '" + msg[3] + "' where IDChat = " + msg[1]) > 0) ? "true" : "false"));
        }
    }

    private void returnTelegramUserValue(String[] msg) {
        send("42;" + db.stringify(db.select("select " + getUserValue(msg[2]) + " from Telegram where IDChat = " + msg[1])).get(0)[0]);
    }

    private void logTelegramUserOut(String[] msg) {
        db.update("update Telegram set carrello = null, IDUtente = null, stato = 0 where IDChat = " + msg[1]);
        send("44;idc");
    }

    private void calculateDistance(String[] msg) {
        send("46;" + WebUtility.getDistance(msg[1], msg[2]));
    }

    private void getNomeProdotto(String[] msg) throws Exception {
        ResultSet res = db.select("select Nome from Prodotto where ID = " + msg[1]);
        res.first();
        send("48;" + res.getString(1));
    }

    private void svotaCarrello(String[] msg) {
        db.update("update Telegram set carrello = null where IDChat = " + msg[1]);
        send("44;idc");
    }

    private void returnUserValue(String[] msg) {
        send("51;" + db.stringify(db.select("select " + getUserValue(msg[2]) + " from Utente where ID = " + msg[1])).get(0)[0]);
    }

}
