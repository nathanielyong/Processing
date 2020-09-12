BlockerGroup blockers = new BlockerGroup(4, 12, 5);

class Blocker {
  int x;
  int y;
  color colour = #41CE0A;
  boolean exists = true;

  /*** Blocker constructor ************************************
   * Sets the x and y coordinates for the blocker object.     *
   ***********************************************************/
  Blocker (int x, int y) {
    this.x = x;
    this.y = y;
  }

  /*** display *************************************************
   * Draws a green 12 by 12 rectangle using the x and y values.*
   ************************************************************/
  void display() {
    fill(colour);
    stroke(colour);
    rect(x, y, 12, 12);
  }
}

class BlockerGroup {
  Blocker[][][] blockerArray;
  int numberOfBlockers;
  int cols;
  int rows;

  /*** BlockerGroup constructor *******************************
   * Sets the number of columns, rows and blocker groups.     *
   ***********************************************************/
  BlockerGroup(int numberOfBlockers, int cols, int rows) {
    this.numberOfBlockers = numberOfBlockers;
    this.cols = cols;
    this.rows = rows;
  }

  /*** makeBlockers ********************************************
  * Creates a 3d array of blocker objects and sets the x and y *
  * coordinates for each blocker. There are 4 different        *
  * groups of blockers, each with columns and rows of 12 by 5. *
  *************************************************************/
  void makeBlockers() {
    blockerArray = new Blocker[numberOfBlockers][cols][rows];
    for (int blockerGroup = 0; blockerGroup < numberOfBlockers; blockerGroup++) {
      for (int col = 0; col < cols; col++) {
        for (int row = 0; row < rows; row++) {
          blockerArray[blockerGroup][col][row] = new Blocker((75 + blockerGroup * 295) + ((col + 1) * 12), 600 + ((row + 1) * 12));
        }
      }
    }
  }

  /*** displayBlockers ********************************************
  * Displays the blockers using the x and y values created by the *
  * makeBlockers() method.                                        *
  ****************************************************************/
  void displayBlockers() {
    for (int blockerGroup = 0; blockerGroup < numberOfBlockers; blockerGroup++) {
      for (int col = 0; col < cols; col++) {
        for (int row = 0; row < rows; row++) {
          if (blockerArray[blockerGroup][col][row].exists) {
            blockerArray[blockerGroup][col][row].display();
          }
        }
      }
    }
  }
}
