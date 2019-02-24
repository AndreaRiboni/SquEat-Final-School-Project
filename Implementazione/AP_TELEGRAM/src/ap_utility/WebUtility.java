/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author Utente
 */
public class WebUtility {

    public static File getImage(String latlong) {
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/streetview?"
                    + "size=400x400&"
                    + "location=" + latlong.replace(" ", "%20")
                    + "&fov=90&"
                            + "heading=235&"
                            + "pitch=10&"
                            + "key=" + ConfigurationLoader.getNodeValue("maps"));
            
            InputStream is = url.openStream();
            File pic = new File(latlong+".png");
            OutputStream os = new FileOutputStream(pic);
            
            byte[] b = new byte[2048];
            int length;
            
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            
            is.close();
            os.close();
            return pic;
        } catch (Exception ex) {
            return null;
        }
    }

}
