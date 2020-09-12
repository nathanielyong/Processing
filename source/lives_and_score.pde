Lives lives = new Lives();
Score score = new Score();

class Lives {
  int x = 955;
  int y = 10;
  
  /*** display ******************************************
  * Keeps track of how many lives the player has, and   *
  * displays them on the game screen.                   *
  ******************************************************/
  void display() {
    fill(255);
    textFont(font4);
    text("LIVES: ", 950, 50);
    lives = new Lives();

    for (int i = playerLives; i >= 1; i--) {      
      x+=60;
      image(livesImage, x, y);
    }
  }
}

class Score {
  int score = 0;  
  
  /*** display ******************************************
  * Writes the current score on the top left of the     *
  * game screen.                                        *
  ******************************************************/
  void display() {
    fill(255);
    textFont(font4);
    text("SCORE: ", 80, 50);
    text(score, 195, 50);
  }
}
