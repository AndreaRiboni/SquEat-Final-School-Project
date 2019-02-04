package ap_server;

/**
 * Eccezione personalizzata
 *
 * @author Andrea Riboni
 */
public class ServerException extends Exception {

    public ServerException(String msg) {
        super(msg);
    }
}
