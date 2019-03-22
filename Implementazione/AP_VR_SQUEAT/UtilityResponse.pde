import java.io.FileOutputStream;
import java.net.URL;

public class Restaurant{
  String nome;
  String pos;
  String recensioni;
  String ID;
}

Restaurant[] restaurants;

Restaurant[] getRestaurants(String[] response){
  restaurants = new Restaurant[(response.length - 1) / 4];
  int x = 0;
  for(int i = 1; i < response.length; i+=4){
    restaurants[x] = new Restaurant();
    restaurants[x].nome = response[i];
    restaurants[x].pos = response[i+1];
    restaurants[x].recensioni = response[i+2];
    restaurants[x++].ID = response[i+3];
  }
  return restaurants;
}

public String getImage(String latlong) {
  try {
    File pic = new File(dataPath(latlong+"xx.png"));
    if(pic.exists()) return pic.getPath();
    URL url = new URL("https://maps.googleapis.com/maps/api/streetview?"
      + "size=300x300&"
      + "location=" + latlong.replace(" ", "%20")
      + "&fov=90&"
      + "pitch=10&"
      + "key=AIzaSyAfc1SJtwKkoBUoy6Ri73hg-HUgfhphjHU");
        
        InputStream is = url.openStream();
        
        OutputStream os = new FileOutputStream(pic);
        
        byte[] b = new byte[2048];
        int length;
        
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        
        is.close();
        os.close();
        return pic.getPath();
    } catch (Exception ex) {
        return null;
    }
}
