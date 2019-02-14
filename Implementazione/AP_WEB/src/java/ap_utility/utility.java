/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_utility;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

/**
 *
 * @author saponaro.andrea
 */
public class utility {
    
    public static String[] getLatLongPositions(String address) throws Exception {
        int responseCode = 0;
        String key = ConfigurationLoader.getNodeValue("mapsapikey");
        String api = "https://maps.googleapis.com/maps/api/geocode/xml?address=" + address.replace(" ", "%20") + "&sensor=true&key="+key;
        URL url = new URL(api);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.connect();
        responseCode = httpConnection.getResponseCode();
        if (responseCode == 200) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();;
            Document document = (Document) builder.parse(httpConnection.getInputStream());
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("/GeocodeResponse/status");
            String status = (String) expr.evaluate(document, XPathConstants.STRING);
            if (status.equals("OK")) {
                expr = xpath.compile("//geometry/location/lat");
                String latitude = (String) expr.evaluate(document, XPathConstants.STRING);
                expr = xpath.compile("//geometry/location/lng");
                String longitude = (String) expr.evaluate(document, XPathConstants.STRING);
                return new String[]{latitude, longitude};
            } else {
                throw new Exception("Error from the API - response status: " + status);
            }
        }
        return null;
    }
    
}
