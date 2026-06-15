import java.awt.*;
import javax.swing.ImageIcon;

class Level3 implements Devil.Level{
    private boolean finished = false;

    @Override
    public void reset() {
        finished = false;
    }

    @Override
    public void update(Guy guy) {
        finished = false;
    }

    @Override
    public void draw(Graphics g, int panelWidth, int panelHeight) {
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}