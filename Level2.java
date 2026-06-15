import java.awt.*;
import javax.swing.ImageIcon;

class Level2 implements Devil.Level {
    private boolean finished = false;
    private Hole [] holes = {new Hole(600, 400, 0, 100, 20), new Hole(800, 400, 0, 100, 20)};
    private Image backImage1 = new ImageIcon("map/map2.png").getImage();
    private final int doorX = 1120;

    @Override
    public void reset() {
        finished = false;
    }

    @Override
    public void update(Guy guy) {
        finished = false;
        guy.updateHole(guy.returnDx(), guy.returnJump(), holes); 
        if(guy.getX() > 525){
            holes[0].sink();
        }
        if(guy.getX() > 700){
            holes[1].sink();
        }
        if(guy.getX() > doorX){
            finished = true;
        }
    }

    @Override
    public void draw(Graphics g, int panelWidth, int panelHeight) {
        g.drawImage(backImage1, 0, 0, panelWidth, panelHeight, null);
        for(Hole hole : holes){
            hole.draw(g);
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}