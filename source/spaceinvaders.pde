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
void setup() {
  size(1200, 800);
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
void draw() {
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
void keyPressed() {
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
void keyReleased() {
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
  void resetValues() {
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
  void create_main_menu() {
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
  void write_instructions()
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
  void write_next_round()
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
  void write_game_over()
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
  void checkCollisions() {   
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
