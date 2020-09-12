Ship player = new Ship();
class Ship {
  int speed = 6;
  int x;
  int y;
  boolean dead;
  int respawnTime = 60;

  /*** Ship constructor *********************************
   * Sets all the variables for Ship object.            *
   *****************************************************/
  Ship() { 
    x = 565;
    y = 725;
    dead = false;
  }

  /*** display *******************************************
   * Draws the player's spaceship.                       *
   ******************************************************/
  void display() {
    image(ship, x, y);
  }

  /*** update ********************************************
   * Makes the ship move left or right depending on      *
   * which arrow key the player presses.                 *
   ******************************************************/
  void update() {
    if (!dead) {
      if (keyLeft && x > 0) 
        x -= speed;
      if (keyRight && x < width - 70)
        x += speed;
    }
  }

  /*** spawn *********************************************
   * Respawns the player's spaceship.                    *
   ******************************************************/
  void spawn() {
    x = 565;
    y = 725;
    dead = false;
    respawnFC = frameCount;
  }
}
