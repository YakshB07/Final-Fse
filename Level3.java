import java.awt.*;
import javax.swing.ImageIcon;

class Level3 implements Devil.Level{
    private boolean finished = false;
    private Image backImage = new ImageIcon("map/map3.png").getImage();

    @Override
    public void reset() {
        finished = false;
    }

    @Override
    public void update(Guy guy) {
        finished = false;
        guy.update(guy.returnDx(), guy.returnJump());
    }

    @Override
    public void draw(Graphics g, int panelWidth, int panelHeight) {
        g.drawImage(backImage, 0, 0, panelWidth, panelHeight, null);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}