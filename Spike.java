import java.awt.*;
import java.util.Random;
import javax.swing.ImageIcon;

public class Spike {
    private int x;  // Stores the current x coordinate of the object.
    private int y;  // Stores the current y coordinate of the object.
    private int l;
    private int w;
    private int speed;  // Stores the speed of the object.
    private Random random = new Random(); // Used to randomly generate cars.
    private Image drawedImage = null;   // Used to store images to draw onto the screen.

    private Rectangle hitbox;   // Stores the hitbox of the moving object.

    private int FrameCounter;   // Stores the frame # and used to animate.

    private boolean drown = false;  // Stores if the turtle has drowned.

    public static final int LEFT = -1, RIGHT = 1, CAR = 2, LOG = 3, TURTLE = 4, KILLERTURTLE = 5;   // Stores the magic numbers for the object type and direction of the moving object.

    // The constructor for the moving object.
    // Parameters:
    // int xer - Gets the starting x coordinate of the moving object.
    // int yer - Gets the starting y coordinate of the moving object.
    // int l - Gets the thickness of the moving object.
    // int sped - Gets the speed of the moving object.
    // int spce - Gets the spacing between the moving objects.
    // int objct - Gets the object type of the moving object.
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
