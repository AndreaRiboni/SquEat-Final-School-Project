package ap_web;

import ap_utility.ConfigurationLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Riboni Andrea
 */
public class WebUtility {

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
            result.append(line);
        }
        rd.close();
        return result.toString();
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

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJSONFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url.replace(" ", "%20")).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    /**
     * Ritorna la distanza espressa in "xx km"
     *
     * @param start
     * @param end
     * @return
     */
    public static String getDistance(String start, String end) {
        try {
            JSONObject dist = readJSONFromUrl("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&"
                    + "origins=" + start + "&"
                    + "destinations=" + end + "&"
                    + "key=" + ConfigurationLoader.getNodeValue("mapsapikey"));
            String row = dist.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text");
            return row;
        } catch (Exception ex) {
            Logger.getLogger(WebUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getLatLong(String address) {
        try {
            JSONObject info = readJSONFromUrl("https://maps.googleapis.com/maps/api/geocode/json?"
                    + "address=" + address.replace(" ", "%20")
                    + "&key=" + ConfigurationLoader.getNodeValue("mapsapikey"));
            float lat = info.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("viewport").getJSONObject("southwest").getFloat("lat");
            float lon = info.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("viewport").getJSONObject("southwest").getFloat("lng");
            return lat+", "+lon;
        } catch (Exception ex) {
            Logger.getLogger(WebUtility.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static String getAddress(String lat, String lon){
        try {
            return readJSONFromUrl("https://maps.googleapis.com/maps/api/geocode/json?"
                    + "address=" + lat+"%20,"+lon
                    + "&key=" + ConfigurationLoader.getNodeValue("mapsapikey")).getJSONArray("results").getJSONObject(0).getString("formatted_address");
        } catch (Exception ex) {
            Logger.getLogger(WebUtility.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
