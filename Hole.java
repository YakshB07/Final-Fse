/*
 * Hole.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * Represents a hole in the floor that grows over time and kills the player if they fall in.
 * The hole starts with zero length and expands downward each frame that sink() is called.
 */

import java.awt.*;

public class Hole {

    private int x, y;   // top left corner of the hole
    private int l;      // current length (height) of the hole, grows over time
    private int w;      // width of the hole, stays fixed
    private int speed;  // how many pixels the hole grows per frame
    private Rectangle hitbox;
    private static final Color holer = new Color(243, 187, 102); // color that matches the map floor

    public Hole(int xer, int yer, int length, int width, int s) {
        x = xer;
        y = yer;
        l = length;
        w = width;
        speed = s;
        hitbox = new Rectangle(x, y, l, w);
    }

    public int getX() { return x; }

    /* sink() grows the hole downward by the speed amount each call.
     * Stops growing once it reaches 1000 pixels deep.
     */
    public void sink() {
        if (hitbox.getY() + hitbox.getHeight() <= 1000) {
            l += speed;
            hitbox = new Rectangle(x, y, w, l);
        }
    }

    /* reset() shrinks the hole back to nothing, used when the player respawns. */
    public void reset() {
        l = 0;
        hitbox = new Rectangle(x, y, w, l);
    }

    /* fell() checks if the player is horizontally positioned over this hole.
     * This is used by updateHole() in Guy to decide whether to ignore ground collision.
     */
    public boolean fell(Guy guy) {
        return guy.getX() > x && guy.getX() + guy.getSize() < x + w;
    }

    public void draw(Graphics g) {
        g.setColor(holer);
        g.fillRect(x, y, w, l);
    }
}