import java.net.Socket;
import java.io.InputStreamReader;

public String request(String request) {
  try{
    Socket client = new Socket("51.68.124.94", 60240);
    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
    out.println(request);
    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
    String msg = br.readLine();
    System.out.println("Richiesta inviata.\n  Risposta: "+msg);
    return msg;
  } catch (Exception e){
    e.printStackTrace();
    return "empty response";
  }
}
