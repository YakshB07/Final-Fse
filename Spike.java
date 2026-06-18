/*
 * Spike.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * Represents a spike obstacle. Spikes can be moved horizontally or vertically
 * by a set distance at a set speed. The moveX and moveY methods only move
 * a fixed amount per call so they need to be called every frame to animate.
 */

import java.awt.*;
import javax.swing.*;

public class Spike {

    private int x, y;   // current position on screen
    private int l, w;   // length and width of the spike hitbox
    private Image drawedImage = new ImageIcon("Spike.png").getImage();
    private Rectangle hitbox;
    private int currentdist; // how far the spike has moved so far in the current movement
    private boolean moveable = true; // flag that prevents moveX/moveY from running twice per frame

    public Spike(int x, int y, int l, int w) {
        this.x = x;
        this.y = y;
        this.l = l;
        this.w = w;
        hitbox = new Rectangle(x, y, l, w);
    }

    /* move() teleports the spike to a new position and resets currentdist.
     * Used to reset the spike after the player dies.
     */
    public void move(int xer, int yer) {
        x = xer;
        y = yer;
        currentdist = 0;
        hitbox = new Rectangle(x, y, l, w);
    }

    /* moveX() slides the spike horizontally toward a target distance at a given speed.
     * dist is the total distance to travel (negative = left, positive = right).
     * speed must match the sign of dist or it won't move.
     * letsMove() must be called before this each frame otherwise moveable blocks it.
     * The second if handles the case where the remaining distance is less than one full step.
     */
    public void moveX(int dist, int speed) {
        if (moveable) {
            if ((dist < 0 && speed < 0) || (dist > 0 && speed > 0)) {
                if (currentdist != dist) {
                    if (Math.abs((double)dist) - Math.abs((double)currentdist) > speed) {
                        x += speed;
                        currentdist += speed;
                    }
                    if (Math.abs((double)dist) - Math.abs((double)currentdist) < speed && Math.abs((double)dist) - Math.abs((double)currentdist) != 0) {
                        x += dist - currentdist;
                        currentdist = 0;
                    }
                }
            }
        }
        hitbox = new Rectangle(x, y, l, w);
        moveable = false; // block until letsMove() is called again next frame
    }

    /* moveY() does the same thing as moveX but moves vertically instead. */
    public void moveY(int dist, int speed) {
        if (moveable) {
            if ((dist < 0 && speed < 0) || (dist > 0 && speed > 0)) {
                if (currentdist != dist) {
                    if (Math.abs((double)dist) - Math.abs((double)currentdist) > speed) {
                        y += speed;
                        currentdist += speed;
                    }
                    if (Math.abs((double)dist) - Math.abs((double)currentdist) < speed && Math.abs((double)dist) - Math.abs((double)currentdist) != 0) {
                        y += dist - currentdist;
                        currentdist = 0;
                    }
                }
            }
        }
        hitbox = new Rectangle(x, y, l, w);
        moveable = false;
    }

    /* letsMove() resets the moveable flag so the spike can move again next frame. */
    public void letsMove() {
        moveable = true;
    }

    /* died() returns true if the spike is touching the player. */
    public boolean died(Guy guy) {
        return guy.returnRect().intersects(hitbox);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(drawedImage, x, y, l, w, null);
    }
}