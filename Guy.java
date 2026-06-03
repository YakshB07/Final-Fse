import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class Guy {

    public static final int SIZE = 32;

    public final int STARTX = 300;
    public final int STARTY = 469;

    private static final double GRAVITY    = 0.7;
    private static final double JUMP_POWER = -14.0;
    private static final double WALK_SPEED = 4.0;
    private static final int    GROUND_Y   = 469;

    private double x, y;
    private double velY;
    private boolean onGround;
    private boolean alive;
    private boolean facingLeft;
    private int lives;

    private int walkTick;
    private int frame; 
    private Image[] pics;

    public Guy() {
        pics = new Image[3];
        for (int i = 0; i < 3; i++) {
            pics[i] = new ImageIcon("man/man" + i + ".png").getImage();
        }
        fullReset();
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

        if (dx != 0) {
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
        if (y >= GROUND_Y) {
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