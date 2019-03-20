//3D
PShape grid;
float angle;

//Assets foto
PImage logo;
float logoY;

void initializeAmbient(){
  imageMode(CENTER);
  logo = loadImage("logoVR.png");
  logo.resize(height/3, height/3);
  logoY = 0;
  angle = 0;
  wallpaper();
}

void wallpaper(){
  //Griglia
  grid = createShape();
  grid.beginShape(LINES);
  grid.stroke(200);
  grid.strokeWeight(1);
  for (int x = -10000; x < +10000; x += 250) {
    grid.vertex(x, +1000, +10000);
    grid.vertex(x, +1000, -10000);
  }
  for (int z = -10000; z < +10000; z += 250) {
    grid.vertex(+10000, +1000, z);
    grid.vertex(-10000, +1000, z);      
  }  
  grid.endShape();
}

void showGrid(){
  shape(grid);
}

void showLogo(){
  image(logo, 0, logoY+=sin(angle+=0.01));
}

void showAmbient(){
  showGrid();
  showLogo();
}
