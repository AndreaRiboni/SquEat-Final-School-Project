package telegrambottest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Riboni Andrea
 */
public class WebUtility {

    /**
     * Ritorna un oggetto URL basato sul link, sui parametri e sui valori
     * forniti. Funziona con una richiesta GET.
     *
     * @param link link a cui collegarsi, senza parametri ma che includa tutte
     * le directory (es: localhost/HTTPTest/index.php)
     * @param param passare sempre 2*n stringhe. la prima rappresenta il nome
     * del parametro; la seconda il valore che esso deve assumere
     * @return java.net.URL relativo
     * @throws java.io.IOException URL non corretto o numero di parametri
     * differente da numero di valori
     */
    public static URL getParametricURL(String link, String... param) throws IOException {
        if (param.length % 2 == 1) {
            throw new IOException("Numero di parametri diverso da numero di valori");
        }
        StringBuilder url = new StringBuilder("http://");
        url.append(link).append("?");
        for (int i = 0; i < param.length; i += 2) {
            url.append(param[i]).append("=").append(param[i + 1]);
            if (i < param.length - 2) {
                url.append("&");
            }
        }
        return new URL(url.toString());
    }

    /**
     * Ritorna un oggetto URL basato sul link fornito.
     *
     * @param link link a cui collegarsi, senza parametri ma che includa tutte
     * le directory (es: localhost/HTTPTest/index.php)
     * @return java.net.URL relativo
     * @throws MalformedURLException URL non corretto
     */
    public static URL getPlainURL(String link) throws MalformedURLException {
        if(link.startsWith("https://")) link = link.substring(8);
        else if(link.startsWith("http://")) link = link.substring(7);
        return new URL("http://" + link);
    }

    /**
     * Effettua una connessione (HTTP GET) all'URL passato come parametro e
     * ritorna la pagina HTML visualizzata sottoforma di stringa
     *
     * @param url url della pagina da scaricare
     * @return stringa contenente la pagina html corrispondente all'URL passato
     * come parametro
     * @throws IOException errore
     */
    public static String getHTMLPage(URL url) throws IOException {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line).append("--@NEWLINE#--");
        }
        rd.close();
        return result.toString().replace("--@NEWLINE#--", "\n");
    }

    /**
     * Ritorna la pagina HTML contenuta nella stringa parsata per essere
     * utilizzata. Richiede la libreria Jsoup.
     *
     * @param html pagina html
     * @return documento html
     */
    public static Document stringToHTML(String html) {
        return Jsoup.parse(html);
    }

    /**
     * Ritorna il valore di un tag, dato il suo id. Richiede la libreria Jsoup.
     *
     * @param html documento HTML
     * @param id id del tag
     * @return contenuto del tag
     */
    public static String getHTMLTagValue(Document html, String id) {
        return html.getElementById(id).html();
    }
}
