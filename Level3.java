import java.awt.*;
import javax.swing.ImageIcon;

class Level3 implements Devil.Level{
    private boolean finished = false;
    private boolean died = false;
    private Image backImage = new ImageIcon("map/map3.png").getImage();

    public void reset() {
        finished = false;
        died = false;
    }

    public void update(Guy guy) {
        finished = false;
        died = false;
        guy.update(guy.returnDx(), guy.returnJump());
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        g.drawImage(backImage, 0, 0, panelWidth, panelHeight, null);
    }

    public boolean playerDied() {
        return false;
    }

    public boolean isFinished() {
        return finished;
    }
}