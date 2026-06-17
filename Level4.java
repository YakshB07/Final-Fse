import java.awt.*;
import javax.swing.ImageIcon;

class Level4 implements Devil.Level {
    private boolean finished = false;
    private boolean died = false;
    private Spike spike1 = new Spike(655, 520, 80, 30);
    private Hole [] holes = {new Hole(900, 550, 0, 60, 20)};
    private Image backImage = new ImageIcon("map/map4.png").getImage();
    private final int doorX = 1000;
    private int counter = 0;

    public void reset() {
        finished = false;
        died = false;
    }

    public void update(Guy guy) {
        reset();
        System.out.println("X: " + guy.getX() + " Y: " + guy.getY());
        System.out.println(counter);
        guy.updateHole(guy.returnDx(), guy.returnJump(), holes);
        if(guy.getY() > 100 && counter < 150){
            spike1.letsMove();
            spike1.moveX(120, 12);
            counter++;
        }
        if(counter >= 150 && counter < 200){
            spike1.letsMove();
            spike1.moveX(-200, -20);
            counter++;
        }
        if(counter >= 200 && counter < 300){
            spike1.letsMove();
            spike1.moveX(400, 20);
            counter++;
        }
        if(counter >= 300){
            spike1.letsMove();
            spike1.moveX(-600, -30);
            counter++;
        }
        if(guy.getX() > 840){
            holes[0].sink();
        }
        if(guy.getY() > 725 - guy.getSize() || spike1.died(guy)){
            died = true;  
            counter = 0;
            guy.respawn();
            spike1.move(655, 520);
            for(Hole hole : holes){
                hole.reset();
            }
        }
        if (guy.getX() > doorX) {
            finished = true;
        }
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        g.drawImage(backImage, 0, 0, panelWidth, panelHeight, null);
        spike1.draw(g);
        for(Hole hole : holes){
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