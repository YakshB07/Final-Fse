import java.awt.*;
import javax.swing.ImageIcon;

public class Spike {
    private int x;
    private int y;
    private int l;
    private int w;
    private Image drawedImage = new ImageIcon("Spike.png").getImage();
    private Rectangle hitbox;
    private int currentdist;
    private boolean moveable = true;

    public Spike(int x, int y, int l, int w) {
        this.x = x;
        this.y = y;
        this.l = l;
        this.w = w;
        hitbox = new Rectangle(x, y, l, w);
    }

    public void move(int xer, int yer) {
        x = xer;
        y = yer;
        currentdist = 0;
        hitbox = new Rectangle(x, y, l, w);
    }

    public void moveX(int dist, int speed){
        if(moveable){
            if((dist < 0 && speed < 0) || (dist > 0 && speed > 0)){
                if(currentdist != dist){
                    if(Math.abs((double)dist) - Math.abs((double)currentdist) > speed){
                        x += speed;
                        currentdist += speed;
                    }
                    if(Math.abs((double)dist) - Math.abs((double)currentdist) < speed && Math.abs((double)dist) - Math.abs((double)currentdist) != 0){
                        x += dist - currentdist;
                        currentdist = 0;
                    }
                }
            }
        }
        hitbox = new Rectangle(x, y, l, w);
        moveable = false;
    }

    public void moveY(int dist, int speed){
        if(moveable){
            if((dist < 0 && speed < 0) || (dist > 0 && speed > 0)){
                if(currentdist != dist){
                    if(Math.abs((double)dist) - Math.abs((double)currentdist) > speed){
                        y += speed;
                        currentdist += speed;
                    }
                    if(Math.abs((double)dist) - Math.abs((double)currentdist) < speed && Math.abs((double)dist) - Math.abs((double)currentdist) != 0){
                        y += dist - currentdist;
                        currentdist = 0;
                    }
                }
            }
        }
        hitbox = new Rectangle(x, y, l, w);
        moveable = false;
    }

    public void letsMove(){
        moveable = true;
    }

    public boolean died(Guy guy) {
        return guy.returnRect().intersects(hitbox);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(drawedImage, x, y, l, w, null);
    }
}