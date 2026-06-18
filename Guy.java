/*
 * Guy.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * This is the player character. It handles movement, jumping, gravity,
 * collision detection using the mask image, and drawing the sprite.
 * There are two update methods. update() is for normal levels and
 * updateHole() is for levels that have holes the player can fall into.
 */

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Guy {

    public static final int SIZE = 35; // width and height of the player in pixels

    private static final double GRAVITY = 0.7;     // how fast the player accelerates downward
    private static final double JUMP_POWER = -13.5; // initial upward velocity when jumping, negative means up
    private static final double WALK_SPEED = 4.0;  // how many pixels the player moves per frame

    private double x, y;       // current position on screen in pixels
    private double velY;       // vertical velocity, negative is moving up, positive is moving down
    private boolean onGround;  // true when the player is standing on solid ground
    private boolean alive;     // whether the player is currently alive
    private boolean facingLeft; // used to decide which way to draw the sprite
    private int lives;
    private int holenum;       // tracks which hole the player is currently interacting with
    private boolean died;

    private Rectangle hitbox;  // used for collision checks with spikes

    private int walkTick;  // frame counter for cycling through the walking animation
    private int frame;     // which of the 3 animation frames to draw right now
    private Image[] pics;  // the 3 sprite images loaded from disk

    private BufferedImage mask; // the collision mask image, blue pixels count as walls

    private int spawnX = 300; // x position to respawn at, changes when entering a new level
    private int spawnY = 416; // y position to respawn at, changes when entering a new level

    private GamePanel gamer; // reference to GamePanel so we can ask it what keys are pressed

    /* Constructor loads all 3 sprite frames, resets the player, and reads the level 1 mask. */
    public Guy(GamePanel game) {
        gamer = game;
        pics = new Image[3];
        holenum = 0;
        for (int i = 0; i < 3; i++) {
            pics[i] = new ImageIcon("man/man" + i + ".png").getImage();
        }
        fullReset();

        try {
            mask = ImageIO.read(new File("map/map1mask.png"));
        } catch (IOException e) {
            System.out.println(e);
        }

        hitbox = new Rectangle((int)x, (int)y, SIZE, SIZE);
    }

    /* setMask() loads a new collision mask image for a different level.
     * Called by GamePanel whenever the player transitions to the next level.
     */
    public void setMask(String file) {
        try {
            mask = ImageIO.read(new File(file));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Rectangle returnRect() {
        return hitbox;
    }

    /* setspawn() updates where the player will appear when they respawn.
     * This must be called before respawn() whenever switching levels so the
     * player ends up at the right starting position for the new level.
     */
    public void setspawn(int xer, int yer) {
        spawnX = xer;
        spawnY = yer;
    }

    /* respawn() moves the player back to the spawn point and resets all their movement state. */
    public void respawn() {
        x = spawnX;
        y = spawnY;
        velY = 0;
        onGround = true;
        alive = true;
        facingLeft = false;
        walkTick = 0;
        frame = 1;
    }

    /* fullReset() resets lives and spawn point back to the very beginning.
     * Used when starting a completely new game.
     */
    public void fullReset() {
        lives = 3;
        spawnX = 300;
        spawnY = 416;
        respawn();
    }

    /* update() handles movement for normal levels where the mask handles all ground collision.
     * The left and right movement checks both the top and bottom corners on the leading edge
     * so the player does not clip diagonally into walls.
     * Gravity is added to velY every frame when in the air so falling accelerates naturally.
     * The ceiling check stops the player from sticking to the ceiling when jumping into it.
     * The nudge loop at the end moves the player pixel by pixel until they are flush with the floor.
     */
    public void update(int dx, boolean jump) {
        if (!alive) {
            return;
        }

        // check both corners on the moving side before stepping
        if (dx > 0) {
            if (clear((int)(x + SIZE + WALK_SPEED), (int)y) && clear((int)(x + SIZE + WALK_SPEED), (int)(y + SIZE - 1))) {
                x += WALK_SPEED;
                facingLeft = false;
            }
        } else if (dx < 0) {
            if (clear((int)(x - WALK_SPEED), (int)y) && clear((int)(x - WALK_SPEED), (int)(y + SIZE - 1))) {
                x -= WALK_SPEED;
                facingLeft = true;
            }
        }

        // if there is ground directly below, stop falling. otherwise keep adding gravity
        if (!clear((int)x, (int)(y + SIZE))) {
            velY = 0;
            onGround = true;
        } else {
            velY += GRAVITY;
            onGround = false;
        }

        if (jump && onGround) {
            velY = JUMP_POWER;
            onGround = false;
        }

        // if the player jumps into a ceiling, kill the upward velocity
        if (jump && (!clear((int)x, (int)(y)) || !clear((int)(x + SIZE), (int)(y)))) {
            velY = GRAVITY;
            onGround = false;
        }

        // move by velY if the destination is clear, otherwise nudge to the floor one pixel at a time
        if (clear((int)x, (int)(y + SIZE + velY))) {
            y += velY;
        } else {
            while (clear((int)x, (int)(y + SIZE))) {
                y++;
            }
        }

        updateAnimation(dx);
        hitbox = new Rectangle((int)x, (int)y, SIZE, SIZE);
    }

    /* updateHole() works the same as update() but skips ground collision when the player is over a hole.
     * First it checks all the holes to see if the player is positioned over any of them.
     * If inHole is true, gravity still applies but the floor check is skipped so they fall through.
     * This is necessary because the mask image has solid ground everywhere, it does not know about holes.
     * The holes are drawn dynamically in Java so we have to handle their collision separately here.
     */
    public void updateHole(int dx, boolean jump, Hole[] holes) {
        if (!alive) {
            return;
        }

        // check if the player is currently over any hole
        boolean inHole = false;
        for (Hole hole : holes) {
            if (hole.fell(this)) {
                inHole = true;
                break;
            }
        }

        // same left and right movement checks as update()
        if (dx > 0) {
            if (clear((int)(x + SIZE + WALK_SPEED), (int)y) && clear((int)(x + SIZE + WALK_SPEED), (int)(y + SIZE - 1))) {
                x += WALK_SPEED;
                facingLeft = false;
            }
        } else if (dx < 0) {
            if (clear((int)(x - WALK_SPEED), (int)y) && clear((int)(x - WALK_SPEED), (int)(y + SIZE - 1))) {
                x -= WALK_SPEED;
                facingLeft = true;
            }
        }

        // if over a hole or in open air, apply gravity. if on solid ground, stop
        if (inHole || (clear((int)x, (int)(y + SIZE)) && clear((int)(x + SIZE), (int)(y + SIZE)))) {
            velY += GRAVITY;
            onGround = false;
        } else if (!clear((int)x, (int)(y + SIZE)) && !clear((int)(x + SIZE), (int)(y + SIZE))) {
            velY = 0;
            onGround = true;
        }

        // only allow jumping when on the ground and not already falling into a hole
        if (jump && onGround && !inHole) {
            velY = JUMP_POWER;
            onGround = false;
        }

        // ceiling check same as update()
        if (jump && (!clear((int)x, (int)(y)) || !clear((int)(x + SIZE), (int)(y)))) {
            velY = GRAVITY;
            onGround = false;
        }

        // if in a hole let gravity pull freely even if the mask says there is ground below
        if (clear((int)x, (int)(y + SIZE + velY)) || inHole) {
            y += velY;
        } else {
            while (clear((int)x, (int)(y + SIZE))) {
                y++;
            }
        }

        updateAnimation(dx);
        hitbox = new Rectangle((int)x, (int)y, SIZE, SIZE);
    }

    /* updateAnimation() picks which sprite frame to show based on what the player is doing.
     * walkTick cycles from 0 to 27 and alternates between frames 1 and 2 to make a walking animation.
     * Frame 2 is the jumping pose and frame 1 is the falling or standing pose.
     */
    private void updateAnimation(int dx) {
        if (!onGround) {
            if (velY < 0) {
                frame = 2; // jumping up
            } else {
                frame = 1; // falling down
            }
        } else if (dx != 0) {
            // cycle through frames while walking
            walkTick = (walkTick + 1) % 28;
            if (walkTick < 7) {
                frame = 1;
            } else if (walkTick < 14) {
                frame = 2;
            } else if (walkTick < 21) {
                frame = 1;
            } else {
                frame = 2;
            }
        } else {
            // standing still
            frame = 1;
            walkTick = 0;
        }
    }

    /* returnDx() and returnJump() let the level classes get the current key input.
     * The levels call guy.returnDx() instead of dealing with key handling themselves.
     */
    public int returnDx() {
        return gamer.returnDx();
    }

    public boolean returnJump() {
        return gamer.returnJump();
    }

    public void updateHolenum() {
        holenum++;
    }

    /* clear() checks if a screen coordinate is walkable by sampling the mask image.
     * The mask is a separate image the same shape as the level where blue pixels are walls.
     * We scale the screen coordinate to mask coordinates since the mask might be a different size.
     * If the pixel is outside the image bounds we treat it as a wall so the player stays on screen.
     * 0xFF0000FF is blue in Java's ARGB format where the bytes are Alpha, Red, Green, Blue.
     */
    private boolean clear(int x, int y) {
        int WALL = 0xFF0000FF; // blue in ARGB means wall

        // scale screen coords to mask coords
        int maskX = x * mask.getWidth() / 1500;
        int maskY = y * mask.getHeight() / 750;

        // treat out of bounds as a wall
        if (maskX < 0 || maskX >= mask.getWidth() || maskY < 0 || maskY >= mask.getHeight()) {
            return false;
        }

        int c = mask.getRGB(maskX, maskY);
        return c != WALL; // anything that is not blue is passable
    }

    public void setX(double v) {
        x = v;
    }

    public void setY(double v) {
        y = v;
    }

    public void setVelY(double v) {
        velY = v;
    }

    public void setOnGround(boolean b) {
        onGround = b;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public double getVelY() {
        return velY;
    }

    public double getSize() {
        return SIZE;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getLives() {
        return lives;
    }

    /* draw() renders the player at their current position.
     * When facing left, AffineTransform is used to flip the image horizontally.
     * We save the old transform before flipping and restore it after so nothing else gets mirrored.
     */
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int drawX = (int) x;
        int drawY = (int) y;

        if (facingLeft) {
            AffineTransform old = g2.getTransform(); // save current transform
            g2.translate(drawX + SIZE, drawY);       // move origin to right edge of player
            g2.scale(-1, 1);                          // flip horizontally
            g2.drawImage(pics[frame], 0, 0, SIZE, SIZE, null);
            g2.setTransform(old);                    // restore transform so other things draw normally
        } else {
            g2.drawImage(pics[frame], drawX, drawY, SIZE, SIZE, null);
        }
    }
}