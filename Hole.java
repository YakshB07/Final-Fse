import java.awt.*;
import javax.swing.*;

public class Hole {
    private int x;
    private int y;
    private int l;
    private int w;
    private int speed;
    private Rectangle hitbox;

    private static final Color holer = new Color(243, 187, 102);

    public Hole(int xer, int yer, int length, int width, int s){
        x = xer;
        y = yer;
        l = length;
        w = width;
        speed = s;
        hitbox = new Rectangle(x, y, l, w);
    }

    public int getX(){
        return x;
    }

    public void sink(){
        if(hitbox.getY() + hitbox.getHeight() <= 1000){
            l += speed;
            hitbox = new Rectangle(x, y, w, l);
        }
    }

    public void reset(){
        l = 0;
        hitbox = new Rectangle(x, y, w, l);
    }

    public boolean fell(Guy guy){
        return guy.getX() > x && guy.getX() + guy.getSize() < x + w;
    }

    public void draw(Graphics g){
        g.setColor(holer);
        g.fillRect(x, y, w, l);
    }
}
