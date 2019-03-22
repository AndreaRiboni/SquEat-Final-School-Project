//3D
PShape grid;
float angle;

//Assets foto
PImage logo;
float logoY;

//Font
PFont createdFont;

//Quale menu mostrare
int MenuToShow = 0; //0 = logo, 1 = ristoranti
int PreviousMenu = 0;

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
  image(logo, 0, logoY+=sin(angle+=0.01)/2);
}

void showAmbient(){
  showGrid();
  switch(MenuToShow){
    case 1:
      drawRestaurants(restaurants);
      break;
    default:
      showLogo();
  }
}

void drawRestaurants(Restaurant[] restaurants){
  for(int i = 0; i < restaurants.length; i++){
    if(i > 4) break;
      switch(i){
        case 0:
          translate(0, 0, 0);
          rotateY(0);
          drawRestaurantPlaceHolder(restaurants[i]);
          continue;
        case 1:
          translate(-500, 0, 300);
          rotateY(1.25*PI);
          drawRestaurantPlaceHolder(restaurants[i]);
          continue;
        case 2:
          rotateY(-1.25*PI);
          translate(1000, 0, 0);
          rotateY(1.75*PI);
          drawRestaurantPlaceHolder(restaurants[i]);
          continue;
        case 3:
          rotateY(-1.75*PI);
          translate(-1300, 0, 500);
          rotateY(0.5*PI);
          drawRestaurantPlaceHolder(restaurants[i]);
          continue;
        case 4:
          rotateY(-0.5*PI);
          translate(1300, 0, 0);
          rotateY(-0.5*PI);
          drawRestaurantPlaceHolder(restaurants[i]);
          continue;
        case 5:
          translate(-60, 0, 60);
          drawRestaurantPlaceHolder(restaurants[i]);
          continue;
        case 6:
          translate(0, 0, 100);
          drawRestaurantPlaceHolder(restaurants[i]);
          continue;
        case 7:
          translate(60, 0, 60);
          drawRestaurantPlaceHolder(restaurants[i]);
          continue;
      }
      //text(restaurants[i].nome, 0, 0);
  }
}

void drawRestaurantPlaceHolder(Restaurant r){
  fill(127);
  String path = getImage(r.pos);
  image(loadImage(path), 0, 0);
}
