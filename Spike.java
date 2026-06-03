import java.awt.*;
import java.util.Random;
import javax.swing.ImageIcon;

public class Spike {
    private int x;
    private int y;  
    private int l;
    private int w;
    private int speed;
    private Random random = new Random(); // Used to randomly generate cars.
    private Image drawedImage = null;   // Used to store images to draw onto the screen.
    private Rectangle hitbox; 
    private boolean death = false;

    public Spike(int x, int y, int l, int w, int speed){
        this.x = x;
        this.y = y;
        this.l = l;
        this.w = w;
        this.speed = speed;
        hitbox = new Rectangle(x, y, l, w);
    }

    public void moveX(int dist){
        int currentdist = 0;
        if(speed < 0){
            if(currentdist + speed * -1 >= dist){
                x -= dist - currentdist;
            }
            else{
                x -= speed;
            }
        }
        if(speed > 0){
            if(currentdist + speed >= dist){
                x += dist - currentdist;
            }
            else{
                x += speed;
            }
        }
        hitbox = new Rectangle(x, y, l, w);
    }

    public void moveY(int dist){
        int currentdist = 0;
        if(speed < 0){
            if(currentdist + speed * -1 >= dist){
                y -= dist - currentdist;
            }
            else{
                y -= speed;
            }
        }
        if(speed > 0){
            if(currentdist + speed >= dist){
                y += dist - currentdist;
            }
            else{
                y += speed;
            }
        }
        hitbox = new Rectangle(x, y, l, w);
    }

    public void died(Guy guy){
        if()
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        // Draws if its a vehicle.
        if(objecttype == CAR){
            int car = random.nextInt(4);
            if(thickness >= 75 && thickness <= 100){
                drawedImage = new ImageIcon("vehicles/vehicles4.png").getImage();
            }
            else if(drawedImage == null){
                drawedImage = new ImageIcon("vehicles/vehicles" + car + ".png").getImage();
            }
            
            if(speed < 0){
                g2.drawImage(drawedImage, x, y, thickness, 50, null);
            }
            else{
                g2.drawImage(drawedImage, x + thickness, y, -thickness, 50, null);
            }
        }    
    }
}
