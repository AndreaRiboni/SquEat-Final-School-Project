package ap_client;

/**
 * Classe di utility per la gestione di richieste
 *
 * @author Andrea Riboni
 */
public class Pacchetto {

    //l'array contiene il numero di parametri che ogni messaggio deve avere, secondo il protocollo di comunicazione prestabilito.
    //si utilizzi il codice del messaggio come index.
    //es: PARAM_NUM[0] ritorna il numero di parametri che deve avere la richiesta con codice 000, ovvero 2.
    private final static int[] PARAM_NUM = {2, 1, 7, 1, 0, 1, 3, 1, 1, 3, 3, 1, 1, 3, 2, 2, 5, 2, 1, 5, 1, 4, 1, 5, 4, 1, 4, 3, 3};

    /**
     * Incapsula e formatta una richiesta
     *
     * @param codice codice della richiesta
     * @param parametri parametri della richiesta
     * @return richiesta correttamente formattata
     * @throws ClientException errore
     */
    public static String incapsula(int codice, String... parametri) throws ClientException {
        if (codice < 0 || codice >= PARAM_NUM.length) {
            throw new ClientException("Codice inesistente.");
        }
        if (PARAM_NUM[codice] != parametri.length) {
            throw new ClientException("Numero di parametri errato per il tipo di messaggio selezionato. Riprovare utilizzando " + PARAM_NUM[codice] + " parametri.");
        }
        StringBuilder msg = new StringBuilder(Integer.toString(codice));
        for (String s : parametri) {
            msg.append(";").append(s);
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
     * @throws ClientException errore
     */
    public static boolean verifica(String[] msg) throws ClientException {
        try {
            return (msg.length - 1 == PARAM_NUM[Integer.parseInt(msg[0])]);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new ClientException("Codice del messaggio inesistente");
        }
    }

    /**
     * è prassi inviare una response per ogni richiesta effettuata. si utilizzi
     * il codice 001
     *
     * @param request
     * @return
     * @throws ClientException
     * @deprecated
     */
    @Deprecated
    public static boolean needResponse(String request) throws ClientException {
        try {
            int code = Integer.parseInt(Pacchetto.estrai(request)[0]);
            switch (code) {
                case 0:
                case 2:
                case 5:
                case 7:
                case 11:
                case 14:
                case 20:
                case 22:
                case 25:
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            throw new ClientException("Codice errato");
        }
    }
}
