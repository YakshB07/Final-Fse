/*
 * Level3.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * The third level. The player moves to the left this time.
 * A spike moves upward and then sweeps across the screen as the player progresses.
 * There is also a hole that opens up on the way back.
 */

import java.awt.*;
import javax.swing.*;

class Level3 implements Devil.Level {

    private boolean finished = false;
    private boolean died = false;
    private Hole[] holes = {new Hole(600, 400, 0, 100, 20)};
    private Spike spike1 = new Spike(1015, 530, 120, 30);
    private Image backImage = new ImageIcon("map/map3.png").getImage();
    private final int doorX = 420; // player needs to reach the left side this time
    private boolean spikeMoved = false;

    public void reset() {
        finished = false;
        died = false;
    }

    public void update(Guy guy) {
        reset();
        guy.updateHole(guy.returnDx(), guy.returnJump(), holes);

        // spike moves up when the player walks into the right side of the map
        if (guy.getX() > 992) {
            spike1.letsMove();
            spike1.moveY(-60, -10);
            spikeMoved = true;
        }

        // once the player starts heading back left, spike sweeps across the screen
        if (guy.getX() < 800 && spikeMoved) {
            spike1.letsMove();
            spike1.moveX(-1000, -20);
        }

        // hole opens up as the player heads back to the left
        if (guy.getX() < 750) {
            holes[0].sink();
        }

        // death by falling or touching the spike
        if (guy.getY() > 725 - guy.getSize() || spike1.died(guy)) {
            died = true;
            spikeMoved = false;
            guy.respawn();
            spike1.move(1015, 530);
            for (Hole hole : holes) {
                hole.reset();
            }
        }

        if (guy.getX() < doorX) {
            finished = true;
        }
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        g.drawImage(backImage, 0, 0, panelWidth, panelHeight, null);
        spike1.draw(g);
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