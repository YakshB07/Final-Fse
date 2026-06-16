import java.awt.*;
import javax.swing.ImageIcon;

class Level3 implements Devil.Level {
    private boolean finished = false;
    private boolean died = false;
    private Hole[] holes = {new Hole(600, 400, 0, 100, 20), new Hole(800, 400, 0, 100, 20)};
    private Image backImage = new ImageIcon("map/map3.png").getImage();
    private final int doorX = 1120;

    public void reset() {
        finished = false;
        died = false;
    }

    public void update(Guy guy) {

    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        g.drawImage(backImage, 0, 0, panelWidth, panelHeight, null);
 
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean playerDied() {
        return died;
    }
}