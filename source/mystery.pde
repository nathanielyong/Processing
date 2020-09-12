Mystery mystery = new Mystery();
int start;
int totalTime = (int)random(20000, 30000);
int passedTime;

class Mystery {
  int x;
  int y;
  boolean dead;

  /*** Mystery *******************************************
   * Sets the variables for the mystery ship, such as    *
   * the spawning coordinates.                           *
   ******************************************************/
  Mystery() {
    x = -100;
    y = 65;
    dead = false;
  }

  /*** display *******************************************
   * Draws the mystery ship.                             *
   ******************************************************/
  void display() {
    image(mysteryImage, x, y);
  }

  /*** update ********************************************
   * Moves the mystery ship across the screen, and       *
   * resets the timer for it to spawn again if it goes   *
   * out of the screen or gets destroyed by the player.  *
   ******************************************************/
  void update() {
    x += 3;
    if (x >= 1250 || dead) {
      start = millis();
      totalTime = (int)random(20000, 30000);
      x = -100;
      y = 65;
      dead = false;
    }
  }
}
