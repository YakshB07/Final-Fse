import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*; 
import java.io.*; 

public class Guy {

    public static final int SIZE = 35;

    public final int STARTX = 300;
    public final int STARTY = 416;

    private static final double GRAVITY    = 0.7;
    private static final double JUMP_POWER = -13.5;
    private static final double WALK_SPEED = 4.0;
    private static final int GROUND_Y   = 416;

    private double x, y;
    private double velY;
    private boolean onGround;
    private boolean alive;
    private boolean facingLeft;
    private int lives;

    private int walkTick;
    private int frame; 
    private Image[] pics;

    private BufferedImage map1mask;

    public Guy() {
        pics = new Image[3];
        for (int i = 0; i < 3; i++) {
            pics[i] = new ImageIcon("man/man" + i + ".png").getImage();
        }
        fullReset();

        try {
    		map1mask = ImageIO.read(new File("map/map1mask.png"));
		} 
		catch (IOException e) {
			System.out.println(e);
		}
    }

    public void respawn() {
        x = STARTX;
        y = STARTY;
        velY = 0;
        onGround = true;
        alive = true;
        facingLeft = false;
        walkTick = 0;
        frame = 1;
    }

    public void fullReset() {
        lives = 3;
        respawn();
    }

    public void update(int dx, boolean jump) {
        if (!alive) {
            return;
        }

        if (dx != 0 && clear((int)(x + dx * WALK_SPEED), (int)y)){
            x += dx * WALK_SPEED;
            facingLeft = (dx < 0);
        }

        if (jump && onGround) {
            velY = JUMP_POWER;
            onGround = false;
        }

        velY += GRAVITY;
        y += velY;

        // stop at the ground
        if (!clear((int)x, (int)(y + SIZE))) {
            y = GROUND_Y;
            velY = 0;
            onGround = true;
        }

        if (!onGround) {
            if (velY < 0) {
                frame = 2;
            } else {
                frame = 1;
            }
        } else if (dx != 0) {
            walkTick = (walkTick + 1) % 28;
            if (walkTick < 7) {
                frame = 1;
            } else if (walkTick < 14) {
                frame = 2;
            } else if (walkTick < 21) {
                frame = 1;
            } else {
                frame = 2;
            }
        } else {
            // standing still
            frame = 1;
            walkTick = 0;
        }
        //System.out.println(jump && onGround);
    }

    private boolean clear(int x, int y){
        int WALL = 0xFF0000FF;
        
		/* colour in Java is ARGB. You can use a Color object, or just a single int. As an int
		 * it is best to use hexadecimal. Each component is 8 bits, that is 0-256 in decimal,
		 * or 0-FF in hex. My walls are blue in my mask. 
		 **/
        int maskX = x * map1mask.getWidth() / 1500;
        int maskY = y * map1mask.getHeight() / 750;
        
        if(maskX < 0 || maskX >= map1mask.getWidth() || maskY < 0 || maskY >= map1mask.getHeight()){
            return false;
        }
        int c = map1mask.getRGB(maskX, maskY);
        return c != WALL;
    }

    public void setX(double v) {
        x = v;
    }

    public void setY(double v) {
        y = v;
    }

    public void setVelY(double v) {
        velY = v;
    }

    public void setOnGround(boolean b) {
        onGround = b;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelY() {
        return velY;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getLives() {
        return lives;
    }



    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int drawX = (int) x;
        int drawY = (int) y;

        if (facingLeft) {
            // flip the image horizontally when going left
            AffineTransform old = g2.getTransform();
            g2.translate(drawX + SIZE, drawY);
            g2.scale(-1, 1);
            g2.drawImage(pics[frame], 0, 0, SIZE, SIZE, null);
            g2.setTransform(old);
        } else {
            g2.drawImage(pics[frame], drawX, drawY, SIZE, SIZE, null);
        }
    }
}