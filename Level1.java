import java.awt.*;
import javax.swing.*;

class Level1 implements Level {
    private int spikeX = 700;
    private boolean spikeMoved = false;
    private final int doorX = 1245;
    private boolean finished = false;
    private Spike spike1 = new Spike(700, 421, 80, 30, 0);
    private Image backImage = new ImageIcon("map/map1.png").getImage();

    @Override
    public void reset() {
        spikeX = 700;
        spikeMoved = false;
        finished = false;
        spike1.move(spikeX, 421);
    }

    @Override
    public void update(Guy guy) {
        finished = false;
        if (guy.getX() > spikeX - 180 && !spikeMoved) {
            spikeMoved = true;
            spikeX -= 75;
            spike1.move(spikeX, 421);
        }
        if (spike1.died(guy)) {
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
}