import ketai.sensors.*; 

double longitude, latitude, altitude;
KetaiLocation location;

void localize(){
  location = new KetaiLocation(this);
}

void onLocationEvent(double _latitude, double _longitude, double _altitude) {
  longitude = _longitude;
  latitude = _latitude;
  altitude = _altitude;
  println("lat/lon/alt: " + latitude + "/" + longitude + "/" + altitude);
}
