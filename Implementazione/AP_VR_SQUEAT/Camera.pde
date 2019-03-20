import ketai.camera.*;

KetaiCamera cam;
long LastActivation = millis(); //millis() dell'ultima attivazione del controllo vocale
final int ActivationInterval = 2500; //ogni quanti millis() possiamo attivarlo

void initializeCamera(){
  cam = new KetaiCamera(this, 176, 144, 15);
}

void onCameraPreviewEvent() {
  cam.read();
  //vedo se la foto è scura
  int bright = 0;
  cam.loadPixels();
  for(int i = 0; i < cam.pixels.length; i+=5){
    bright += brightness(cam.pixels[i]);
  }
  bright /= cam.pixels.length/5;
  println(bright);
  //se è molto scura e posso lanciare il controllo vocale, lo lancio
  if(bright < 25 && millis()-LastActivation > ActivationInterval){
    vibrate(200);
    LastActivation = millis();
    Activity act = (Activity)getContext();
    act.runOnUiThread(new Runnable() {
      @ Override
      public void run() {
        println("runnn");
        //Initialize the recognizer on the UI thread
        initRecognizer();
      }
    });
  }
}
