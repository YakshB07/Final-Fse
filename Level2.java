/*
 * Level2.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * The second level. Two holes open up in the floor as the player walks forward.
 * The player has to time their movement to avoid falling in.
 */

import java.awt.*;
import javax.swing.*;

class Level2 implements Devil.Level {

    private boolean finished = false;
    private boolean died = false;
    private Hole[] holes = {new Hole(600, 400, 0, 100, 20), new Hole(800, 400, 0, 100, 20)};
    private Image backImage = new ImageIcon("map/map2.png").getImage();
    private final int doorX = 1120;

    public void reset() {
        finished = false;
        died = false;
        holes = new Hole[]{new Hole(600, 400, 0, 100, 20), new Hole(800, 400, 0, 100, 20)};
    }

    public void update(Guy guy) {
        died = false;
        double prevX = guy.getX();
        double prevY = guy.getY();

        guy.updateHole(guy.returnDx(), guy.returnJump(), holes);

        // if the player moved more than 100 pixels in one frame they teleported, which means they respawned
        if (Math.abs(guy.getX() - prevX) > 100 || Math.abs(guy.getY() - prevY) > 100) {
            died = true;
        }

        // holes start growing once the player gets close to them
        if (guy.getX() > 525) { holes[0].sink(); }
        if (guy.getX() > 700) { holes[1].sink(); }

        if (guy.getX() > doorX) {
            finished = true;
        }

        // if the player falls off the bottom of the screen, respawn and reset the holes
        if (guy.getY() > 750 - guy.getSize()) {
            died = true;
            guy.respawn();
            for (Hole hole : holes) {
                hole.reset();
            }
        }
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        g.drawImage(backImage, 0, 0, panelWidth, panelHeight, null);
        for (Hole hole : holes) {
            hole.draw(g);
        }
    }

    public boolean isFinished() { 
        return finished; 
    }
    public boolean playerDied() { 
        return died; 
    }
}