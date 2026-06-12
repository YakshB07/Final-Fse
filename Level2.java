import java.awt.*;
import javax.swing.ImageIcon;

class Level2 implements Devil.Level {
    private boolean finished = false;
    private Hole [] holes = {new Hole(600, 400, 0, 100, 10), new Hole(800, 400, 0, 100, 10)};
    private Image backImage1 = new ImageIcon("map/map2.png").getImage();

    @Override
    public void reset() {
        finished = false;
    }

    @Override
    public void update(Guy guy) {
        finished = false;
        guy.updateHole(guy.returnDx(), guy.returnJump(), holes);
        System.out.println("goon");
        if(guy.getX() > 400){
            holes[0].sink();
        }
        if(guy.getX() > 700){
            holes[1].sink();
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