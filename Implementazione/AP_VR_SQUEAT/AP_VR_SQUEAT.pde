import processing.vr.*;
import android.os.Environment;

void setup() {
  fullScreen(STEREO);
  textSize(50);
  initializeControl();
  initializeCamera();
  initializeAmbient();
}
 
void draw() {
  if(!cam.isStarted()) cam.start();
  background(0);
  translate(width/2, height/2, 0);
  showAmbient();
  translate(width/2, height/2, -100);
  text(ListenedText, -200, -200);
}
