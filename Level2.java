import java.awt.*;

import javax.swing.ImageIcon;

class Level2 implements Level {
    private boolean finished = false;
    private Hole hole1 = new Hole(600, 500, 0, 100, 10);
    private Image backImage1 = new ImageIcon("map/map2nohole.png").getImage();
    private Image backImage2 = new ImageIcon("map/map2.png").getImage();

    @Override
    public void reset() {
        finished = false;
    }

    @Override
    public void update(Guy guy) {
        finished = false;
        if(guy.getX() > 400){
            hole1.sink();
        }
    }

    @Override
    public void draw(Graphics g, int panelWidth, int panelHeight) {
        g.drawImage(backImage1, 0, 0, panelWidth, panelHeight, null);
        hole1.draw(g);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}