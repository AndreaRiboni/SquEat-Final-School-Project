/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_utility;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Riboni Andrea
 */
public class ConfigurationLoader {

    private static final String CONFIG_PATH = "config.xml";
    private static File CONFIG_FILE;

    private static File getConfigFile() {
        if (CONFIG_FILE == null) {
            CONFIG_FILE = new File(CONFIG_PATH);
        }
        return CONFIG_FILE;
    }

    public static String getNodeValue(String node) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(getConfigFile());
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();
            NodeList nList = root.getElementsByTagName(node);
            return nList.item(0).getTextContent();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ConfigurationLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
