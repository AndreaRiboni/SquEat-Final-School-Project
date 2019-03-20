import java.util.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.view.View;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.util.ArrayList;
import android.util.Log;

//Variabili Sketch
String ListenedText = "";

//Variabili SpeechRecognition
int VOICE_RECOGNITION_REQUEST_CODE = 1234;
Intent intent;
boolean mIsListening = false; 
final static int DIM = 20, DELAY = 1000;
int nextTimer, counter;
public SpeechRecognizer sr;

void initRecognizer() {
  ListenedText = "";
  sr = SpeechRecognizer.createSpeechRecognizer(getContext());
  sr.setRecognitionListener(new listener());
 
  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);        
  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
  intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
 
  intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5); 
  sr.startListening(intent);
  println("OK INITIALIZED");
}

public class listener implements RecognitionListener {
  public void onReadyForSpeech(Bundle params){
    println("READY");
  }
  
  public void onBeginningOfSpeech(){
    println("SPEECH BEGIN");
  }
  
  public void onRmsChanged(float rmsdB){
  }
  
  public void onBufferReceived(byte[] buffer){
  }
              
  public void onEndOfSpeech(){
    println("SPEECH END");
  }
              
  public void onError(int error) {
    vibrate(300);
    switch(error){
      case 1:
        println("timeout");
      break;
      case 2:
        println("no network");
      break;
      case 3:
        println("audio recording error");
      break;
      case 4:
      case 5:
        println("clientserver error");
      break;
      case 6:
        println("speech timeout");
      break;
      case 7:
        println("no match");
      break;
      case 8:
        println("recognizer busy");
      break;
      case 9:
        println("insufficient permission");
      break;
      default:
        println("lazy error");
    }
  }
              
  public void onResults(Bundle results) {
    ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
    if(data.size() > 0){
      ListenedText = data.get(0).toString();
      println(data.get(0));
    } else println("not enough text");
  }
  
  public void onPartialResults(Bundle partialResults){
  }
  
  public void onEvent(int eventType, Bundle params){
  }
}
