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

    // Moves the object and resets it when it goes off screen.
    // Has no parameters and returns nothing.
    public void move(){
        if(speed < 0 && x < space * -1 - thickness){
            x = 700 + space + thickness;
        }
        if(speed > 0 && x > 700 + space + thickness){
            x = 0 - space - thickness;
        }
        x += speed;
        hitbox = new Rectangle(x, y, thickness, 50);
    }

    // Draws all itself on the screen. 
    // Parameters:
    // Graphics g - The tool used to draw on the screen.
    // returns nothing.
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
        // Draws if its a log.
        else if(objecttype == LOG){
            int logs = thickness / 50;
            for(int i = 0; i < logs; i++){
                if(i == 0){
                    drawedImage = new ImageIcon("logsprite/logsprite0.png").getImage();
                    g2.drawImage(drawedImage, x, y, 50, 50, null);
                }
                else if(i == logs - 1){
                    drawedImage = new ImageIcon("logsprite/logsprite2.png").getImage();
                    g2.drawImage(drawedImage, x + (i * 50), y, 50, 50, null);
                }
                else{
                    drawedImage = new ImageIcon("logsprite/logsprite1.png").getImage();
                    g2.drawImage(drawedImage, x + (i * 50), y, 50, 50, null);
                }
            }
        }
        // Draws if its a turtle.
        else if(objecttype == TURTLE){
            FrameCounter += 1;

            if(FrameCounter >= 0 && FrameCounter <= 6){
                drawedImage = new ImageIcon("turtlesprite/turtlesprite0.png").getImage();
            }
            else if(FrameCounter > 6 && FrameCounter <= 12){
                drawedImage = new ImageIcon("turtlesprite/turtlesprite1.png").getImage();
            }
            else if(FrameCounter > 12 && FrameCounter <= 18){
                drawedImage = new ImageIcon("turtlesprite/turtlesprite2.png").getImage();
            }
            else{
                FrameCounter = 0;
            }
            int turtles = thickness / 50;
            for(int i = 0; i < turtles; i++){
                g2.drawImage(drawedImage, x + (i * 50), y, 50, 50, null);
            }
        }
        // Draws if its a killer turtle.
        else if(objecttype == KILLERTURTLE){
            FrameCounter += 1;

            if(FrameCounter >= 0 && FrameCounter <= 6){
                drawedImage = new ImageIcon("turtlesprite/turtlesprite0.png").getImage();
                drown = false;
            }
            else if(FrameCounter > 6 && FrameCounter <= 12){
                drawedImage = new ImageIcon("turtlesprite/turtlesprite1.png").getImage();
                drown = false;
            }
            else if(FrameCounter > 12 && FrameCounter <= 18){
                drawedImage = new ImageIcon("turtlesprite/turtlesprite2.png").getImage();
                drown = false;
            }
            else if(FrameCounter > 18 && FrameCounter <= 24){
                drawedImage = new ImageIcon("turtlesprite/turtlesprite3.png").getImage();
                drown = false;
            }
            else if(FrameCounter > 24 && FrameCounter <= 30){
                drawedImage = new ImageIcon("turtlesprite/turtlesprite4.png").getImage();
                drown = false;
            }
            else if(FrameCounter > 30 && FrameCounter <= 36){
                drawedImage = null;
                drown = true;
            }
            else{
                FrameCounter = 0;
            }
            int turtles = thickness / 50;
            for(int i = 0; i < turtles; i++){
                g2.drawImage(drawedImage, x + (i * 50), y, 50, 50, null);
            }
        }    
    }
}
