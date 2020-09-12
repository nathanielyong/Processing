import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class spaceinvaders extends PApplet {

/*********************************************************
 *  Name: Nathaniel Yong, Mark Antony-Newman              *
 *  Course: ICS 3U Pd. 4                                  *
 *  Final Project: Space Invaders                         *
 *  Purpose: 1 player Space Invaders game where the user  *
 *           must control a spaceship and destroy the     *
 *           oncoming waves of aliens.                    *
 *  Due Date: June 4, 2020                                *
 *********************************************************/
PImage background, ship, laser, enemy_laser, alien1_1, alien1_2, alien2_1, alien2_2,
alien3_1, alien3_2, explosion_blue, explosion_green, explosion_purple, livesImage,
mysteryImage, mainScreenAlien1, mainScreenAlien2, mainScreenAlien3, mainScreenAlien4;

PFont font1, font2, font3, font4, font5;

boolean keyLeft, keyRight;
int playerDeadFC;
int nextRoundFC;
int gameOverFC;
int respawnFC;
int playerLives;
int round = 1;

SpaceInvaders game = new SpaceInvaders();

/*** setup ******************************************************
 * Loads all the fonts, images, and sets the size and frameRate.*
 ***************************************************************/
public void setup() {
  
  frameRate(60);
  
  String imgPath = "images/";
  String fPath = "fonts/";
  background = loadImage(imgPath + "background.jpg");

  ship = loadImage(imgPath + "ship.png");
  ship.resize(70, 70);

  livesImage = loadImage(imgPath + "ship.png");

  laser = loadImage(imgPath + "laser.png");
  laser.resize(5, 15);

  enemy_laser = loadImage(imgPath + "enemylaser.png");
  enemy_laser.resize(5, 15);

  alien1_1 = loadImage(imgPath + "enemy1_1.png");
  alien1_1.resize(50, 45);

  alien1_2 = loadImage(imgPath + "enemy1_2.png");
  alien1_2.resize(50, 45);

  alien2_1 = loadImage(imgPath + "enemy2_1.png");
  alien2_1.resize(50, 45);

  alien2_2 = loadImage(imgPath + "enemy2_2.png");
  alien2_2.resize(50, 45);

  alien3_1 = loadImage(imgPath + "enemy3_1.png");
  alien3_1.resize(50, 45);

  alien3_2 = loadImage(imgPath + "enemy3_2.png");
  alien3_2.resize(50, 45);

  mysteryImage = loadImage(imgPath + "mystery.png");
  mysteryImage.resize(85, 45);

  mainScreenAlien1 = loadImage(imgPath + "enemy1_1.png");
  mainScreenAlien2 = loadImage(imgPath + "enemy2_1.png");
  mainScreenAlien3 = loadImage(imgPath + "enemy3_1.png");
  mainScreenAlien4 = loadImage(imgPath + "mystery.png");

  explosion_blue = loadImage(imgPath + "explosionblue.png");
  explosion_green = loadImage(imgPath + "explosiongreen.png");
  explosion_purple = loadImage(imgPath + "explosionpurple.png");

  font1 = loadFont(fPath + "Impact-120.vlw");
  font2 = loadFont(fPath + "Impact-96.vlw");
  font3 = loadFont(fPath + "Impact-72.vlw");
  font4 = loadFont(fPath + "Impact-48.vlw");
  font5 = loadFont(fPath + "Impact-24.vlw");
}

/*** draw *******************************************************
 * Main loop where all the methods and attributes of objects are*
 * called, so that the game can run.                            *
 ***************************************************************/
public void draw() {
  background(background);
  if (game.mainScreen) 
  {
    game.create_main_menu();
  } else if (game.instructions) {
    game.write_instructions();
  } else if (game.nextRound) {
    game.write_next_round();
  } else if (game.gameOver) {
    game.write_game_over();
  } else if (game.startGame) {

    if (aliens.aliensAlive() == 0) {
      game.startGame = false;
      game.nextRound = true;
      round += 1;
      nextRoundFC = frameCount;
    }
    if (playerLives == 0) {
      game.gameOver = true;
      game.startGame = false;
      gameOverFC = frameCount;
    }

    lives.display();
    score.display();

    game.checkCollisions();

    if (!player.dead) {
      player.display();
      player.update();
    } else {
      if (frameCount - playerDeadFC == player.respawnTime) {
        player.spawn();
      }
    }

    if (shipBulletExists) {
      shipBullet.display(); 
      shipBullet.update();
    } 

    aliens.displayAliens();
    if (frameCount % aliens.moveTime == 0) {
      aliens.updateAliens();
    }
    if (frameCount % aliens.shootTime == 0) {
      aliens.alienShoot();
    }

    blockers.displayBlockers();

    for (int i = 0; i < aliens.alienBulletArray.size(); i++) {
      aliens.alienBulletArray.get(i).display();
      aliens.alienBulletArray.get(i).update();

      // remove bullets from array if they don't exist, to avoid wasting too much memory
      if (!aliens.alienBulletArray.get(i).exists) {
        aliens.alienBulletArray.remove(i);
      }
    }

    for (int i = 0; i < alienExplosionArray.size(); i++) {
      alienExplosionArray.get(i).update(alienExplosionArray.get(i).alienRow);  
      if (!alienExplosionArray.get(i).exists) {
        alienExplosionArray.remove(i);
      }
    }

    for (int i = 0; i < mysteryExplosionArray.size(); i++) {
      mysteryExplosionArray.get(i).update();
      if (!mysteryExplosionArray.get(i).exists) {
        mysteryExplosionArray.remove(i);
      }
    }

    passedTime = millis() - start;
    if (passedTime > totalTime) {
      mystery.display();
      mystery.update();
    }
  }
}

/*** keyPressed() **************************************
 * Stores the key that was pressed in a variable,      *
 * allowing objects to perform certain actions when    *
 * the user presses a particular key.                  *
 ******************************************************/
public void keyPressed() {
  if (key == CODED) {
    if (keyCode == LEFT) {
      keyLeft = true;
    }
    if (keyCode == RIGHT) {
      keyRight = true;
    }
  }
}

/*** keyReleased() *************************************
 * Stores the key that was released in a variable,     *
 * and has the program do a number of certain things   *
 * when the particular keys associated are released.   *
 ******************************************************/
public void keyReleased() {
  if (key == CODED) {
    if (keyCode == LEFT) {
      keyLeft = false;
    }
    if (keyCode == RIGHT) {
      keyRight = false;
    }
  }

  if (key == 'i' && game.mainScreen) {
    game.mainScreen = false;
    game.instructions = true;
  } else if (key == 'i' && game.instructions) {
    game.instructions = false;
    game.mainScreen = true;
  } 

  if (key == ' ' && game.mainScreen) {
    game.mainScreen = false;
    game.startGame = true;
    game.resetValues();
  } else if (key == ' ' && game.instructions) {
    game.instructions = false;
    game.startGame = true;
    game.resetValues();
  } else if (key == ' ' && game.startGame && !shipBulletExists && !player.dead) {
    shipBullet = new ShipBullet(player.x + 33, player.y, 20, -1);
    shipBulletExists = true;
  } else if (key == ' ' && game.nextRound && frameCount - nextRoundFC > 10) {
    game.nextRound = false;
    game.startGame = true;  
    shipBulletExists = false;
    aliens.makeAliens();
    blockers.makeBlockers();
    player.spawn();
    aliens.alienBulletArray.clear();
    aliens.bottomAliens.clear();
  } else if (key == ' ' && game.gameOver && frameCount - gameOverFC > 10) {
    game.gameOver = false;
    game.mainScreen = true;
    round = 1;
  }
}

class SpaceInvaders {
  boolean mainScreen;
  boolean startGame;
  boolean gameOver;
  boolean instructions;
  boolean nextRound;
  
  

  /*** SpaceInvaders constructor ************************
   * Sets all the variables for SpaceInvaders object.   *
   *****************************************************/
  SpaceInvaders() {
    mainScreen = true;
    startGame = false;    
    instructions = false;
    nextRound = false;
    gameOver = false;
  }
  
  /*** resetValues *******************************
   * Resets all the game values if the player    *
   * wants to play a new game.                   *
   ***********************************************/
  public void resetValues() {
    shipBulletExists = false;
    aliens.makeAliens();
    blockers.makeBlockers();
    player.spawn();
    aliens.alienBulletArray.clear();
    aliens.bottomAliens.clear();
    playerLives = 3;
    score.score = 0;
    round = 1;
    start = millis();
  }

  /*** create_main_menu **********************************
   * Draws the main menu screen.                         *
   ******************************************************/
  public void create_main_menu() {
    fill(255);
    textFont(font1);
    textAlign(CENTER);
    text("SPACE INVADERS", 600, 250);
    textFont(font4);
    text("Press space to play", 600, 350);
    textFont(font5);
    text("By Nathaniel Yong and Mark Antony-Newman", 600, 750);

    image(mainScreenAlien1, 250, 500);
    mainScreenAlien1.resize(100, 100);
    image(mainScreenAlien2, 450, 500);
    mainScreenAlien2.resize(110, 100);
    image(mainScreenAlien3, 650, 500);
    mainScreenAlien3.resize(100, 100);
    image(mainScreenAlien4, 850, 510);
    mainScreenAlien4.resize(150, 85);
  }

  /*** write_instructions ********************************
   * Writes the instructions on the instructions screen. *
   ******************************************************/
  public void write_instructions()
  {
    fill(255);
    textFont(font4);
    textAlign(CENTER);
    text("Move your spaceship horizontally using the \n right and left arrow keys.", 600, 150);
    text("Press the spacebar to shoot lasers at the alien ships.", 600, 300);
    text("Avoid the alien lasers and destroy all of the", 600, 400);
    text("aliens to win and go to the next round.", 600, 450);
    text("Purple aliens are worth 30 points, blue aliens are worth 20,", 600, 500);
    text("green aliens are worth 10, and mystery ships are worth ???", 600, 550); 
    text("Press i again to go back to home screen", 600, 720);
    text("Press space to play", 600, 770);
  }

  /*** write_next_round **********************************
   * Tells the user what round they are about to enter.  *
   ******************************************************/
  public void write_next_round()
  {
    fill(255);
    textFont(font1);
    textAlign(CENTER);
    text("Round " + round, 600, 400);
    textFont(font4);
    text("Press space to continue", 600, 500);
  }

  /*** write_game_over ***********************************
   * Tells the user what round they got to and the final *
   * score.                                              *
   ******************************************************/
  public void write_game_over()
  {
    fill(255);
    textFont(font1);
    textAlign(CENTER);
    text("Game Over", 600, 300);
    textFont(font3);
    text("You made it to Round: " + round, 600, 400);
    text("Final Score: " + score.score, 600, 500);
    textFont(font4);
    text("Press space to continue", 600, 600);
  }

  /*** checkCollisions *********************************************
   * This method detects the following collisions between objects: *
   * ShipBullet and Alien; ShipBullet and AlienBullet;             *
   * ShipBullet and Blocker; ShipBullet and Mystery ship;          *
   * AlienBullet and Ship; AlienBullet and Blocker;                *
   * Alien and Ship; Alien and Blocker;                            *               
   ****************************************************************/
  public void checkCollisions() {   
    if (shipBulletExists == true) {
      for (int col = 0; col < aliens.cols; col++) {
        for (int row = 0; row < aliens.rows; row++) {
          // ship bullet and alien collision
          if (shipBullet.x < aliens.alienArray[col][row].x + 50 &&
            shipBullet.x + 5 > aliens.alienArray[col][row].x &&
            shipBullet.y < aliens.alienArray[col][row].y + 45 &&
            shipBullet.y + 15 > aliens.alienArray[col][row].y &&
            !aliens.alienArray[col][row].dead) {
            shipBulletExists = false;
            aliens.alienArray[col][row].dead = true;
            if (row == 0) {
              score.score += 30;
            } else if (row == 1 || row == 2) {
              score.score += 20;
            } else if (row == 3 || row == 4) {
              score.score += 10;
            }

            alienExplosionArray.add(new AlienExplosion(aliens.alienArray[col][row].x, aliens.alienArray[col][row].y, row));
          }
        }
      }

      // ship bullet and alien bullet collision 
      for (int i = 0; i < aliens.alienBulletArray.size(); i++) {
        if (Math.abs(shipBullet.x - aliens.alienBulletArray.get(i).x) < 5 &&
          Math.abs(shipBullet.y - aliens.alienBulletArray.get(i).y) < 15) {
          shipBulletExists = false;
          aliens.alienBulletArray.get(i).exists = false;
        }
      }

      // ship bullet and blocker collision
      for (int blockerGroup = 0; blockerGroup < blockers.numberOfBlockers; blockerGroup++) {
        for (int col = 0; col < blockers.cols; col++) {
          for (int row = 0; row < blockers.rows; row++) {
            if (blockers.blockerArray[blockerGroup][col][row].x < shipBullet.x + 5 &&
              blockers.blockerArray[blockerGroup][col][row].x + 12 > shipBullet.x &&
              blockers.blockerArray[blockerGroup][col][row].y < shipBullet.y + 15 &&
              blockers.blockerArray[blockerGroup][col][row].y + 12 > shipBullet.y &&
              blockers.blockerArray[blockerGroup][col][row].exists) {
              blockers.blockerArray[blockerGroup][col][row].exists = false;
              shipBulletExists = false;
            }
          }
        }
      }

      // ship bullet and mystery ship collision
      if (shipBullet.x < mystery.x + 85 &&
        shipBullet.x + 5 > mystery.x &&
        shipBullet.y < mystery.y + 45 &&
        shipBullet.y + 15 > mystery.y && !mystery.dead) {
        mystery.dead = true;
        shipBulletExists = false;
        int scoreBonus = (int)random(2, 7)*50;
        score.score += scoreBonus;

        mysteryExplosionArray.add(new MysteryExplosion(mystery.x + 42, mystery.y + 22, scoreBonus));
      }
    }


    for (int i = 0; i < aliens.alienBulletArray.size(); i++) {
      // alien bullet and ship collision,; player loses life if this occurs
      if (aliens.alienBulletArray.get(i).x < player.x + 70 &&
        aliens.alienBulletArray.get(i).x + 5 > player.x &&
        aliens.alienBulletArray.get(i).y < player.y + 70 &&
        aliens.alienBulletArray.get(i).y + 15 > player.y &&
        !player.dead && frameCount - respawnFC > 60) {
        player.dead = true;
        aliens.alienBulletArray.get(i).exists = false;
        playerLives -= 1;
        playerDeadFC = frameCount;
      }

      // alien bullet and blocker collision 
      for (int blockerGroup = 0; blockerGroup < blockers.numberOfBlockers; blockerGroup++) {
        for (int col = 0; col < blockers.cols; col++) {
          for (int row = 0; row < blockers.rows; row++) {
            if (aliens.alienBulletArray.get(i).x < blockers.blockerArray[blockerGroup][col][row].x + 12 &&
              aliens.alienBulletArray.get(i).x + 5 > blockers.blockerArray[blockerGroup][col][row].x &&
              aliens.alienBulletArray.get(i).y < blockers.blockerArray[blockerGroup][col][row].y + 12 &&
              aliens.alienBulletArray.get(i).y + 15 > blockers.blockerArray[blockerGroup][col][row].y &&
              blockers.blockerArray[blockerGroup][col][row].exists) {
              blockers.blockerArray[blockerGroup][col][row].exists = false;
              aliens.alienBulletArray.get(i).exists = false;
            }
          }
        }
      }
    }

    ArrayList<Alien> arr = aliens.bottomAliens();
    for (int i = 0; i < arr.size(); i++) {      
      // ship and alien collision; game ends if this occurs        
      if (arr.get(i).x < player.x + 70 &&
        arr.get(i).x + 50 > player.x &&
        arr.get(i).y < player.y + 70 &&
        arr.get(i).y + 45 > player.y &&
        !player.dead && !arr.get(i).dead) {
        startGame = false;
        gameOver = true;
      }

      // alien and blocker collision
      for (int blockerGroup = 0; blockerGroup < blockers.numberOfBlockers; blockerGroup++) {
        for (int col = 0; col < blockers.cols; col++) {
          for (int row = 0; row < blockers.rows; row++) {
            if (arr.get(i).x < blockers.blockerArray[blockerGroup][col][row].x + 12 &&
              arr.get(i).x + 50 > blockers.blockerArray[blockerGroup][col][row].x &&
              arr.get(i).y < blockers.blockerArray[blockerGroup][col][row].y + 12 &&
              arr.get(i).y + 45 > blockers.blockerArray[blockerGroup][col][row].y &&
              blockers.blockerArray[blockerGroup][col][row].exists && !arr.get(i).dead) {
              blockers.blockerArray[blockerGroup][col][row].exists = false;
            }
          }
        }
      }
    }
  }
}
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
  public void display1_1() {
    image(alien1_1, x, y);
  }

  /*** display1_2 ****************************************
   * Displays state 2 of the purple alien to the screen  *
   ******************************************************/
  public void display1_2() {
    image(alien1_2, x, y);
  }

  /*** display2_1 *****************************************
   * Displays state 1 of the blue alien to the screen     *
   *******************************************************/
  public void display2_1() {
    image(alien2_1, x, y);
  }

  /*** display2_2 ****************************************
   * Displays state 2 of the blue alien to the screen    *
   ******************************************************/
  public void display2_2() {
    image(alien2_2, x, y);
  }

  /*** display3_1 ****************************************
   * Displays state 1 of the green alien to the screen   *
   ******************************************************/
  public void display3_1() {
    image(alien3_1, x, y);
  }

  /*** display3_2 ****************************************
   * Displays state 1 of the green alien to the screen   *
   ******************************************************/
  public void display3_2() {
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
  public void makeAliens() {
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
  public void displayAliens() {
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
  public void updateAliens() {
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
  public void updateSpeed() {
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
  public void updateShootSpeed() {
    if (round <= 16) 
      shootTime = 42 - (round - 1) * 2;  
    else if (round > 16)
      shootTime = 10;
  }

  /*** columnDead *********************************************
   * Takes the parameter columnNum and returns true if all    *
   * the aliens in that column are dead.                      *
   ***********************************************************/
  public boolean columnDead(int columnNum) {
    Alien[] column = alienArray[columnNum];
    return column[0].dead && column[1].dead && column[2].dead && column[3].dead && column[4].dead;
  }

  /*** aliensAlive ********************************************
   * Returns the number of aliens currently still alive.      *
   ***********************************************************/
  public int aliensAlive() {
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
  public void alienShoot() {
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
  public ArrayList bottomAliens() {
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
BlockerGroup blockers = new BlockerGroup(4, 12, 5);

class Blocker {
  int x;
  int y;
  int colour = 0xff41CE0A;
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
  public void display() {
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
  public void makeBlockers() {
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
  public void displayBlockers() {
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
boolean shipBulletExists;
ShipBullet shipBullet;
AlienBullet alienBullet;

class Bullet {
  int x;
  int y;
  int speed;
  int dir;

  /*** Bullet constructor ***************************************
   * Sets the x, y, speed and direction values for the bullet   *
   *************************************************************/
  Bullet(int x, int y, int speed, int dir) {
    this.speed = speed;
    this.x = x;
    this.y = y;
    this.dir = dir;
  }
}

class ShipBullet extends Bullet {
  /*** ShipBullet constructor ***********************************
   * Creates a subclass of the Bullet superclass.               *
   *************************************************************/
  ShipBullet(int x, int y, int speed, int dir) {
    super(x, y, speed, dir);
  }

  /*** display ***************
   * Displays the shipBullet *
   ***************************/
  public void display() {
    image(laser, x, y);
  }

  /*** update *********************************************
   * Moves the shipBullet vertically upwards and stops    *
   * displaying it when it moves off-screen.              *
   *******************************************************/
  public void update() {
    y += dir * speed;
    if (y < 0)
      shipBulletExists = false;
  }
}

class AlienBullet extends Bullet {
  boolean exists;

  /*** AlienBullet constructor **********************************
   * Creates a subclass of the Bullet superclass.               *
   *************************************************************/
  AlienBullet(int x, int y, int speed, int dir) {
    super(x, y, speed, dir);
    exists = true;
  }

  /*** display ****************
   * Displays the alienBullet *
   ***************************/
  public void display() {
    image(enemy_laser, x, y);
  }

  /*** update ********************************************
   * Moves the alienBullet vertically downwards and stops *
   * displaying it when it moves off-screen.              *
   *******************************************************/
  public void update() {
    y += dir * speed;
    if (y > height) {
      exists = false;
    }
  }
}
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
  public void display1(int row) {
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
  public void display2(int row) {
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
  public void update(int row) {
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
  public void display() {
    fill(255);
    textFont(font5);
    text(score, x, y);
  }
  
  /*** update **************************************************
  * Calls display until timePassed reaches 600ms, and then     *
  * the text disappears.                                       *
  *************************************************************/
  public void update() {
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
Lives lives = new Lives();
Score score = new Score();

class Lives {
  int x = 955;
  int y = 10;
  
  /*** display ******************************************
  * Keeps track of how many lives the player has, and   *
  * displays them on the game screen.                   *
  ******************************************************/
  public void display() {
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
  public void display() {
    fill(255);
    textFont(font4);
    text("SCORE: ", 80, 50);
    text(score, 195, 50);
  }
}
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
  public void display() {
    image(mysteryImage, x, y);
  }

  /*** update ********************************************
   * Moves the mystery ship across the screen, and       *
   * resets the timer for it to spawn again if it goes   *
   * out of the screen or gets destroyed by the player.  *
   ******************************************************/
  public void update() {
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
  public void display() {
    image(ship, x, y);
  }

  /*** update ********************************************
   * Makes the ship move left or right depending on      *
   * which arrow key the player presses.                 *
   ******************************************************/
  public void update() {
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
  public void spawn() {
    x = 565;
    y = 725;
    dead = false;
    respawnFC = frameCount;
  }
}
  public void settings() {  size(1200, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "spaceinvaders" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
