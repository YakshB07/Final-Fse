import java.awt.*;
import javax.swing.*;

class Level1 implements Devil.Level {
    private int spikeX = 700;
    private boolean died = false;
    private boolean spikeMoved = false;
    private final int doorX = 1245;
    private boolean finished = false;
    private Spike spike1 = new Spike(700, 421, 80, 30);
    private Image backImage = new ImageIcon("map/map1.png").getImage();

    @Override
    public void reset() {
        spikeX = 700;
        spikeMoved = false;
        finished = false;
        died = false;
        spike1.move(spikeX, 421);
    }

    @Override
    public void update(Guy guy) {
        finished = false;
        died = false;      

        guy.update(guy.returnDx(), guy.returnJump());
        if (guy.getX() > spikeX - 180 && guy.getX() < 800) {
            spikeMoved = true;
            spike1.letsMove();
            spike1.moveX(-150, -10);
        }
        if(guy.getX() > 900 && spikeMoved){
            spike1.letsMove();
            spike1.moveX(800, 20);
        }
        if (spike1.died(guy)) {
            died = true;     
            guy.respawn();
            spikeX = 700;
            spikeMoved = false;
            spike1.move(spikeX, 421);
        }
        if (guy.getX() > doorX) {
            finished = true;
        }
    }

    @Override
    public void draw(Graphics g, int panelWidth, int panelHeight) {
        g.drawImage(backImage, 0, 0, panelWidth, panelHeight, null);
        spike1.draw(g);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean playerDied() {
        return died;
    }
}