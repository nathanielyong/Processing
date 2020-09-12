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
  void display() {
    image(laser, x, y);
  }

  /*** update *********************************************
   * Moves the shipBullet vertically upwards and stops    *
   * displaying it when it moves off-screen.              *
   *******************************************************/
  void update() {
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
  void display() {
    image(enemy_laser, x, y);
  }

  /*** update ********************************************
   * Moves the alienBullet vertically downwards and stops *
   * displaying it when it moves off-screen.              *
   *******************************************************/
  void update() {
    y += dir * speed;
    if (y > height) {
      exists = false;
    }
  }
}
