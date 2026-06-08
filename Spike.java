import java.awt.*;
import javax.swing.ImageIcon;

public class Spike {
    private int x;
    private int y;
    private int l;
    private int w;
    private int speed;
    private Image drawedImage = new ImageIcon("Spike.png").getImage();
    private Rectangle hitbox;

    public Spike(int x, int y, int l, int w, int speed) {
        this.x = x;
        this.y = y;
        this.l = l;
        this.w = w;
        this.speed = speed;
        hitbox = new Rectangle(x, y, l, w);
    }

    public void move(int xer, int yer) {
        x = xer;
        y = yer;
        hitbox = new Rectangle(x, y, l, w);
    }

    public boolean died(Guy guy) {
        return guy.returnRect().intersects(hitbox);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.red);
        g2.drawRect(x, y, l, w);
    }
}