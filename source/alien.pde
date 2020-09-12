AlienGroup aliens = new AlienGroup(10, 5);

class Alien {
  int x;
  int y;
  boolean dead = false;

  /*** Alien constructor *********************************
   * Sets the x and y coordinates of the alien object    *
   ******************************************************/
  Alien(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /*** display1_1 ****************************************
   * Displays state 1 of the purple alien to the screen  *
   ******************************************************/
  void display1_1() {
    image(alien1_1, x, y);
  }

  /*** display1_2 ****************************************
   * Displays state 2 of the purple alien to the screen  *
   ******************************************************/
  void display1_2() {
    image(alien1_2, x, y);
  }

  /*** display2_1 *****************************************
   * Displays state 1 of the blue alien to the screen     *
   *******************************************************/
  void display2_1() {
    image(alien2_1, x, y);
  }

  /*** display2_2 ****************************************
   * Displays state 2 of the blue alien to the screen    *
   ******************************************************/
  void display2_2() {
    image(alien2_2, x, y);
  }

  /*** display3_1 ****************************************
   * Displays state 1 of the green alien to the screen   *
   ******************************************************/
  void display3_1() {
    image(alien3_1, x, y);
  }

  /*** display3_2 ****************************************
   * Displays state 1 of the green alien to the screen   *
   ******************************************************/
  void display3_2() {
    image(alien3_2, x, y);
  }
}

class AlienGroup {
  Alien[][] alienArray;
  ArrayList<Alien> bottomAliens = new ArrayList<Alien>();
  ArrayList<AlienBullet> alienBulletArray = new ArrayList<AlienBullet>();

  int cols;
  int rows;
  int leftAlien;
  int rightAlien;
  int moveSizeX = 20;
  int moveSizeY = 50;
  int moveTime;
  int shootTime = 42;
  int dir;
  boolean moveDown;
  boolean displayState = true;

  /*** AlienGroup constructor *********************************
   * Sets the number of columns and rows for the alienGroup   *
   ***********************************************************/
  AlienGroup(int cols, int rows) {
    this.cols = cols;
    this.rows = rows;
  }

  /*** makeAliens *********************************************
   * Creates a 2d array of alien objects and sets the x and y *
   * coordinates for each alien. Depending on what round it   *
   * is, the aliens' x values will be closer to the bottom of *
   * the screen to make it harder for the player. This        *
   * function also resets any initial values such as the      *
   * move speed, dir, etc.                                    *
   ***********************************************************/
  void makeAliens() {
    alienArray = new Alien[cols][rows];
    for (int col = 0; col < cols; col++) {
      for (int row = 0; row < rows; row++) {
        if (round <= 8)
          alienArray[col][row] = new Alien(((col + 1) * 50) + ((col + 1) * 8), 25 * round - 1 + ((row + 1) * 50) + ((row + 1) * 8));
        else if (round > 8)
          alienArray[col][row] = new Alien(((col + 1) * 50) + ((col + 1) * 8), 25 * 7 + ((row + 1) * 50) + ((row + 1) * 8));
      }
    }
    dir = 1;
    moveTime = 40;
    displayState = true;
    updateShootSpeed();
  }

  /*** displayAliens ******************************************
   * Uses the 2d array created by the makeAliens() method and *
   * displays all the alien objects inside of the array.      *
   * Depending on the row, the type of alien displayed will   *
   * be different. Also, depending on the value of the        *
   * displayState boolean, the aliens will either be displayed*
   * in state 1 or state 2.                                   *
   ***********************************************************/
  void displayAliens() {
    for (int col = 0; col < cols; col++) {
      for (int row = 0; row < rows; row++) {
        if (!alienArray[col][row].dead) {
          if (row == 0) {
            if (displayState == true) 
              alienArray[col][row].display1_1();
            else
              alienArray[col][row].display1_2();
          } else if (row == 1 || row == 2) {
            if (displayState == true) 
              alienArray[col][row].display2_1();
            else
              alienArray[col][row].display2_2();
          } else if (row == 3 || row == 4) {
            if (displayState == true) 
              alienArray[col][row].display3_1();
            else
              alienArray[col][row].display3_2();
          }
        }
      }
    }
    updateSpeed();
  }

  /*** updateAliens *******************************************
   * Uses the 2d array created by the makeAliens() method to  *
   * update the x and y values of the aliens and the display  *
   * state. This method shifts the aliens' x values by the    *
   * amount of variable moveSizeX, and shifts the aliens down *
   * by the amount of moveSizeY depending on whether the      *
   * variable leftAlien or rightAlien is touching the edge.   *
   * The display state is also reversed every time this method*
   * is called.                                               *
   ***********************************************************/
  void updateAliens() {
    displayState = !displayState;
    moveDown = false;
    Alien[] leftColumn;
    Alien[] rightColumn;

    // check for left-most alien, to determine whether alien group hits left edge of screen
    for (int col = 0; col < cols; col++) {
      if (!columnDead(col)) {
        leftColumn = alienArray[col];
        for (int row = 0; row < rows; row++) {
          if (!leftColumn[row].dead) {
            leftAlien = leftColumn[row].x;  
            break;
          }
        }
        break;
      }
    }    
    // check for right-most alien, to determine whether alien group hits right edge of screen
    for (int col = cols - 1; col >= 0; col--) {
      if (!columnDead(col)) {
        rightColumn = alienArray[col];
        for (int row = 0; row < rows; row++) {
          if (!rightColumn[row].dead) {
            rightAlien = rightColumn[row].x;
            break;
          }
        }
        break;
      }
    }

    if (leftAlien <= 10 || rightAlien >= width - 60) {
      dir *= -1;
      moveDown = true;
    }

    for (int col = 0; col < cols; col++) {
      for (int row = 0; row < rows; row++) {
        if (!alienArray[col][row].dead) {
          alienArray[col][row].x += dir * moveSizeX;
          if (moveDown) {
            alienArray[col][row].y += moveSizeY;
          }
        }
      }
    }
  }

  /*** updateSpeed ********************************************
   * Changes the value of the variable moveTime based on how  *
   * many aliens are currently on the screen. The aliens move *
   * faster the fewer aliens there are.                       *
   ***********************************************************/
  void updateSpeed() {
    if (aliensAlive() == 1)
      moveTime = 15;
    else if (aliensAlive() <= 5)
      moveTime = 20;
    else if (aliensAlive() <= 10) 
      moveTime = 25;
    else if (aliensAlive() <= 20)
      moveTime = 30;
    else if (aliensAlive() <= 30)
      moveTime = 35;
    else
      moveTime = 40;
  }

  /*** updateShootSpeed ***************************************
   * Changes the value of the variable shootTime based on what*
   * round it is. The aliens shoot faster the higher the      *
   * round it is, with a limit on round 16.                   *
   ***********************************************************/
  void updateShootSpeed() {
    if (round <= 16) 
      shootTime = 42 - (round - 1) * 2;  
    else if (round > 16)
      shootTime = 10;
  }

  /*** columnDead *********************************************
   * Takes the parameter columnNum and returns true if all    *
   * the aliens in that column are dead.                      *
   ***********************************************************/
  boolean columnDead(int columnNum) {
    Alien[] column = alienArray[columnNum];
    return column[0].dead && column[1].dead && column[2].dead && column[3].dead && column[4].dead;
  }

  /*** aliensAlive ********************************************
   * Returns the number of aliens currently still alive.      *
   ***********************************************************/
  int aliensAlive() {
    int aliensAlive = 0;
    for (int col = 0; col < cols; col++) {
      for (int row = 0; row < rows; row++) {
        if (!alienArray[col][row].dead)
          aliensAlive += 1;
      }
    }
    return aliensAlive;
  }

  /*** alienShoot **********************************************
   * Chooses a random alien object from the bottomAliens array *
   * and creates an AlienBullet object using those coordinates.*
   ************************************************************/
  void alienShoot() {
    bottomAliens = bottomAliens();
    int num = (int)random(0, bottomAliens.size());
    Alien randAlien = bottomAliens.get(num);
    alienBulletArray.add(new AlienBullet(randAlien.x + 25, randAlien.y + 45, 5, 1));
    bottomAliens.clear();
  }

  /*** bottomAliens *******************************************
   * Returns an array of all the aliens at the bottom of their* 
   * column, for each column.                                 *
   ***********************************************************/
  ArrayList bottomAliens() {
    for (int col = 0; col < cols; col++) {
      if (!columnDead(col)) {
        Alien[] column = alienArray[col];
        if (!column[4].dead)
          bottomAliens.add(column[4]);
        else if (!column[3].dead)
          bottomAliens.add(column[3]);
        else if (!column[2].dead)
          bottomAliens.add(column[2]);
        else if (!column[1].dead)
          bottomAliens.add(column[1]);
        else if (!column[0].dead)
          bottomAliens.add(column[0]);
      }
    }
    return bottomAliens;
  }
}
