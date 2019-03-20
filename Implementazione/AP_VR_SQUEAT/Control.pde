import ketai.ui.*;

KetaiVibrate vibe;

void initializeControl(){
  vibe = new KetaiVibrate(this); 
}

void vibrate(long duration){
  vibe.vibrate(duration);  
}
