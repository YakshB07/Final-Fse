import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Guy {

    public static final int SIZE = 35;

    private static final double GRAVITY = 0.7;
    private static final double JUMP_POWER = -13.5;
    private static final double WALK_SPEED = 4.0;

    private double x, y;
    private double velY;
    private boolean onGround;
    private boolean alive;
    private boolean facingLeft;
    private int lives;

    private Rectangle hitbox;

    private int walkTick;
    private int frame;
    private Image[] pics;

    private BufferedImage mask;

    private int spawnX = 300;
    private int spawnY = 416;

    public Guy() {
        pics = new Image[3];
        for (int i = 0; i < 3; i++) {
            pics[i] = new ImageIcon("man/man" + i + ".png").getImage();
        }
        fullReset();

        try {
            mask = ImageIO.read(new File("map/map1mask.png"));
        } catch (IOException e) {
            System.out.println(e);
        }

        hitbox = new Rectangle((int)x, (int)y, SIZE, SIZE);
    }

    public void setMask(String file) {
        try {
            mask = ImageIO.read(new File(file));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Rectangle returnRect() {
        return hitbox;
    }

    public void setspawn(int xer, int yer) {
        spawnX = xer;
        spawnY = yer;
    }

    public void respawn() {
        x = spawnX;
        y = spawnY;
        velY = 0;
        onGround = true;
        alive = true;
        facingLeft = false;
        walkTick = 0;
        frame = 1;
    }

    public void fullReset() {
        lives = 3;
        spawnX = 300;
        spawnY = 416;
        respawn();
    }

    public void update(int dx, boolean jump) {
        if (!alive) {
            return;
        }

        if (dx != 0 && clear((int)(x + dx * WALK_SPEED), (int)y)) {
            x += dx * WALK_SPEED;
            facingLeft = (dx < 0);
        }

        if (jump && onGround) {
            velY = JUMP_POWER;
            onGround = false;
        }

        velY += GRAVITY;
        y += velY;

        if (!clear((int)x, (int)(y + SIZE))) {
            y = spawnY;
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
            frame = 1;
            walkTick = 0;
        }

        hitbox = new Rectangle((int)x, (int)y, SIZE, SIZE);
    }

    private boolean clear(int x, int y) {
        int WALL = 0xFF0000FF;

        int maskX = x * mask.getWidth() / 1500;
        int maskY = y * mask.getHeight() / 750;

        if (maskX < 0 || maskX >= mask.getWidth() || maskY < 0 || maskY >= mask.getHeight()) {
            return false;
        }
        int c = mask.getRGB(maskX, maskY);
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