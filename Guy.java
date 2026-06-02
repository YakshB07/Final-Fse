import java.awt.*;
import javax.swing.*;
public class Guy {

    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    public final int STARTX = 300;
    public final int STARTY = 100;


    private double x, y;
    private int dir;
    private int frame;        // which animation frame to draw (0 = still, 1 = mid-hop)
    private int hopTick;      // counts ticks during a hop (each hop takes 10 ticks)
    private int deathTick;    // separate counter for the death animation so it doesnt mix with hopTick
    private boolean jumping;
    private boolean alive;
    private int lives;

    private Image[] pics; 


    public Guy() {
        
        pics = new Image[4];
        for (int i = 1; i < 4; i++) {
            pics[i] = new ImageIcon("man/man" + i + ".png").getImage();
        }
        for (int i = 0; i < 4; i++) {
            pics[i] = new ImageIcon("man/man" + i + ".png").getImage();
            System.out.println(pics[i].getWidth(null));
        }

        fullReset();
    }

    // resets position and timer for a new life, but keeps the lives count
    public void respawn() {
        x = STARTX;
        y = STARTY;
        dir = RIGHT;
        jumping = true;   // starts jumping so the guy pops into the scene
        alive = true;
        frame = 0;
        hopTick = 0;
        deathTick = 0;
}

    // full reset including lives - called when starting a new game
    public void fullReset() {
        lives = 3;
        respawn();
    }

    // starts a hop in the given direction, only if not already jumping
    public void hop(int direction) {
        if (!jumping && alive) {
            dir = direction;
            jumping = true;
            hopTick = 0;
            frame = 1; // show the mid-jump frame while jumping
        }
    }

    // called every tick - moves the frog during a hop and plays the death animation when dead
    public void update() {
        if (alive && jumping) {
            // move 5px per tick in the hop direction
            // each hop takes 10 ticks so the frog moves 50px total per hop
            if (dir == UP) y -= 5;
            if (dir == DOWN) y += 5;
            if (dir == LEFT) x -= 5;
            if (dir == RIGHT) x += 5;

            hopTick++;
            if (hopTick == 8) frame = 0;    // switch to landing frame near the end of the hop
            if (hopTick >= 10) jumping = false; // hop is complete
        }

        // death animation - advance a frame every 10 ticks
        if (!alive) {
            deathTick++;
            if (deathTick % 10 == 0) {
                frame++;
            }
            // once all death frames are done, lose a life and respawn
            // if (frame >= deathPics.length) {
            //     lives--;
            //     respawn();
            //     if (lives > 0) {
            //         respawnSound.play();
            //     }
            // }
        }
    }

    // triggers the death animation and plays the right sound
    public void die() {
        if (!alive) return; // already dead, dont trigger again
        alive = false;
        jumping = false;
        frame = 0;
        deathTick = 0;
    }

    // moves the frog sideways with a log or turtle
    // we dont drift if the frog is jumping vertically, otherwise it would slide sideways mid-jump
    public void drift(double amount) {
        if (!jumping || (dir != UP && dir != DOWN)) {
            x += amount;
        }
    }

    public boolean isAlive() { 
        return alive; 
    }
    public boolean isHopping() { 
        return jumping; 
    }
    public int getLives() { 
        return lives; 
    }
    public void setLives(int n) { 
        lives = n; 
    }
    public double getY() { 
        return y; 
    }
    public Image getStillImage() { 
        return pics[0]; 
    }

    // the river section is above y = 420 on screen
    public boolean inWater() {
        return y < 420 && alive;
    }

    public boolean isOnScreen() {
        return x >= 0 && x <= 670;
    }

    // prevents the frog from jumping backward past the starting row
    public boolean canGoDown() {
        return y < STARTY - 50;
    }

    // // returns true the first time the frog moves into a new higher row
    // // used to give the player +10 points per forward lane
    // public boolean reachedNewLane() {
    //     if (y < highestY - 49 && y > 200) {
    //         highestY = y; // update so we dont count the same row twice
    //         return true;
    //     }
    //     return false;
    // }

    // // checks if the frog's hitbox overlaps any of the 5 lily pad x positions at y=130
    // public int getPadIndex() {
    //     Rectangle hb = getHitbox();
    //     for (int i = 0; i < PAD_X.length; i++) {
    //         if (hb.contains(PAD_X[i], 130)) {
    //             return i;
    //         }
    //     }
    //     return -1;
    // }

    // // the hitbox is a 30x30 box at the frog's current position
    // public Rectangle getHitbox() {
    //     return new Rectangle((int)x, (int)y, 30, 30);
    // }

    public void draw(Graphics g) {
        g.drawImage(pics[frame], (int)x, (int)y, 100, 100, null);
    }

//     public void draw(Graphics g) {
//         // System.out.println("draw called");

//         g.setColor(Color.RED);
//         g.fillRect((int)x, (int)y, 30, 30);
//     }
}
