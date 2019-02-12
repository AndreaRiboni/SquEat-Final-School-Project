package ap_utility;

import ap_server.ServerException;

/**
 * Classe di utility per la gestione di richieste
 *
 * @author Andrea Riboni
 */
public class Pacchetto {

    //l'array contiene il numero di parametri che ogni messaggio deve avere, secondo il protocollo di comunicazione prestabilito.
    //si utilizzi il codice del messaggio come index.
    //es: PARAM_NUM[0] ritorna il numero di parametri che deve avere la richiesta con codice 000, ovvero 2.
    private final static int[] PARAM_NUM = {2, 1, 7, 4, 0, 1, 3, 1, 5, 3, 3, 1, 1, 2, 2, 2, 5, 2, 1, 5, 1, 4, 1, 5, 4, 1, 4, 3, 3};

    /**
     * Incapsula e formatta una richiesta
     *
     * @param codice codice della richiesta
     * @param parametri parametri della richiesta
     * @return richiesta correttamente formattata
     * @throws ServerException errore
     */
    public static String incapsula(int codice, String... parametri) throws ServerException {
        if (codice < 0 || codice >= PARAM_NUM.length) {
            throw new ServerException("Codice inesistente.");
        }
        if (PARAM_NUM[codice] != parametri.length) {
            throw new ServerException("Numero di parametri errato per il tipo di messaggio selezionato. Riprovare utilizzando " + PARAM_NUM[codice] + " parametri.");
        }
        StringBuilder msg = new StringBuilder(Integer.toString(codice));
        for (String s : parametri) {
            msg.append(";").append(s);
        }
        return msg.toString();
    }

    public static String incapsulaLoop(int codice, String[]... parametri) {
        StringBuilder msg = new StringBuilder(Integer.toString(codice));
        for (String[] param : parametri) {
            for (String attribute : param) {
                msg.append(";").append(attribute);
            }
        }
        return msg.toString();
    }

    /**
     * estrae una richiesta correttamente formattata in un array.
     *
     * @param msg richiesta formattata
     * @return array richiesta
     */
    public static String[] estrai(String msg) {
        return msg.split(";");
    }

    /**
     * verifica la validità di una richiesta
     *
     * @param msg richiesta
     * @return true se ha un numero di parametri corretto
     * @throws ServerException errore
     */
    public static boolean verifica(String[] msg) throws ServerException {
        try {
            return (msg.length - 1 == PARAM_NUM[Integer.parseInt(msg[0])]);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new ServerException("Codice del messaggio inesistente");
        }
    }

}
