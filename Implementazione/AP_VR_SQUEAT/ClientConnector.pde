import java.net.Socket;
import java.io.InputStreamReader;

String answer;

void request(String request) {
  final String message = request;
  Thread t = new Thread(new Runnable(){
    public void run(){
      try{
        Socket client = new Socket("51.68.124.94", 60240);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.println(message);
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String msg = br.readLine();
        println("Richiesta inviata.\n  Risposta: "+msg);
        processAnswer(msg);
      } catch (Exception e){
        e.printStackTrace();
      }
    }
  });
  t.start();
}

void processSpeech(String speech){
  if(speech.length() < 2) return;
  //TROVA RISTORANTI
  if(speech.toLowerCase().contains("trova ristorant")){
    askForRestaurants();
  }
}

void processAnswer(String message){
  answer = message;
  String[] response = message.split(";");
  switch(response[0]){
    case "006":
      MenuToShow = 1;
      drawRestaurants(getRestaurants(response));
      break;
    default:
      MenuToShow = 0;
  }
}

void askForRestaurants(){
  if (location.getProvider() == "none") println("noooo");
  else request("5;" + latitude + ", " + longitude);
}
