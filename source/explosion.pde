AlienExplosion alienExplosion;
ArrayList<AlienExplosion> alienExplosionArray = new ArrayList<AlienExplosion>();
MysteryExplosion mysteryExplosion;
ArrayList<MysteryExplosion> mysteryExplosionArray = new ArrayList<MysteryExplosion>();

class AlienExplosion {
  int x;
  int y;
  int alienRow;
  int explosionStartTime;
  boolean exists;
  
  /*** AlienExplosion constructor ******************************
  * Sets the variables for the alien death animation.          *
  *************************************************************/
  AlienExplosion(int x, int y, int row) {
    this.x = x;
    this.y = y;
    this.alienRow = row;
    exists = true;
    explosionStartTime = millis();
  }

  /*** display1 ************************************************
  * Displays the explosion image with pixel size 50 by 45.     *
  * The colour of the explosion changes based on the row       *
  *************************************************************/
  void display1(int row) {
    if (row == 0) {
      image(explosion_purple, x, y, 50, 45);
    }
    if (row == 1 || row == 2) {
      image(explosion_blue, x, y, 50, 45);
    }
    if (row == 3 || row == 4) {
      image(explosion_green, x, y, 50, 45);
    }
  }
  
  /*** display2 ************************************************
  * Displays the explosion image with pixel size 60 by 55.     *
  * The colour of the explosion changes based on the row       *
  *************************************************************/
  void display2(int row) {
    if (row == 0) {
      image(explosion_purple, x - 5, y - 5, 60, 55);
    }
    if (row == 1 || row == 2) {
      image(explosion_blue, x - 5, y - 5, 60, 55);
    }
    if (row == 3 || row == 4) {
      image(explosion_green, x - 5, y - 5, 60, 55);
    }
  }
  
  /*** update **************************************************
  * Calls display1 until timePassed reaches 300ms, and then    *
  * calls display2 to create animation of explosion. After     *
  * 450ms, the explosion disappears.                           *
  *************************************************************/
  void update(int row) {
    int timePassed = millis() - explosionStartTime;

    if (exists) {
      if (timePassed <= 150) {
        display1(row);
      } else if (timePassed <= 300) {
        display2(row);
      } else if (timePassed > 450) {
        exists = false;
      }
    }
  }
}

class MysteryExplosion {
  int x;
  int y;
  int score;
  boolean exists;
  int explosionStartTime;
  
  /*** AlienExplosion constructor ******************************
  * Sets the variables for the mysteryship death animation.    *
  *************************************************************/
  MysteryExplosion(int x, int y, int score) {
    this.x = x;
    this.y = y;
    this.score = score;
    exists = true;
    explosionStartTime = millis();
  }

  /*** display *************************************************
  * Displays the score that the user received by killing the   *
  * mystery ship.                                              *
  *************************************************************/
  void display() {
    fill(255);
    textFont(font5);
    text(score, x, y);
  }
  
  /*** update **************************************************
  * Calls display until timePassed reaches 600ms, and then     *
  * the text disappears.                                       *
  *************************************************************/
  void update() {
    int timePassed = millis() - explosionStartTime;
    if (exists) {
      if (timePassed <= 600) { 
        display();
      }
      else {
        exists = false;
      }
    }
  }
}
