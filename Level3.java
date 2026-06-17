import java.awt.*;
import javax.swing.ImageIcon;

class Level3 implements Devil.Level {
    private boolean finished = false;
    private boolean died = false;
    private Hole[] holes = {new Hole(600, 400, 0, 100, 20)};
    private Spike spike1 = new Spike(1015, 530, 120, 30);
    private Image backImage = new ImageIcon("map/map3.png").getImage();
    private final int doorX = 420;
    private boolean spikeMoved = false;

    public void reset() {
        finished = false;
        died = false;
    }

    public void update(Guy guy) {
        reset();
        guy.updateHole(guy.returnDx(), guy.returnJump(), holes);
        if (spike1.died(guy)) {
            died = true;     
            guy.respawn();
        }
        if (guy.getX() > 992) {
            spike1.letsMove();
            spike1.moveY(-60, -10);
            spikeMoved = true;
        }
        if(guy.getX() < 800 && spikeMoved){
            spike1.letsMove();
            spike1.moveX(-1000, -20);
        }
        if (guy.getX() < 750) {
            holes[0].sink();
        }
        if(guy.getY() > 725 - guy.getSize() || spike1.died(guy)){
            died = true;  
            spikeMoved = false;
            guy.respawn();
            spike1.move(1015, 530);
            for(Hole hole : holes){
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