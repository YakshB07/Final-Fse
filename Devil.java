/*
 * Yaksh Butani
 * This is the main frogger file 
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Devil extends JFrame {

    public Devil() {
        super("Level Devil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel game = new GamePanel();
        add(game);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new Devil();
    }
}

// Main class to control the game. Handles the intro, levels, gameover screen,
// keyboard input, and all drawing. Uses other classes for specific game objects.
class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Guy Guy = new Guy();

    private boolean[] keys = new boolean[2000];
    private boolean[] prevKeys = new boolean[2000]; 
    private char keyTyped;
    private Timer timer; 


    public GamePanel() {
        setPreferredSize(new Dimension(700, 830));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);

        timer = new Timer(15, this);
        timer.start();

    }

    public void updatePlay(){
         
        if (keyDown(KeyEvent.VK_UP)) {
            Guy.hop(Guy.UP);
        } else if (keyDown(KeyEvent.VK_DOWN) && Guy.canGoDown()) {
            Guy.hop(Guy.DOWN);
        } else if (keyDown(KeyEvent.VK_RIGHT)) {
            Guy.hop(Guy.RIGHT);
        } else if (keyDown(KeyEvent.VK_LEFT)) {
            Guy.hop(Guy.LEFT);
        }
        
        Guy.update();

    }
    // public void draw(Graphics g) {
    //     g.setColor(Color.RED);
    //     g.fillRect((int)x, (int)y, 30, 30);
    // }

    public void drawGame(Graphics g) {
        Guy.draw(g);
    }

     public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }


    public void actionPerformed(ActionEvent e) {
        updatePlay();
        repaint();
    }


    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public void keyTyped(KeyEvent e) {
        keyTyped = e.getKeyChar();
    }

    // returns true only on the first tick a key is pressed
    // comparing to prevKeys means holding a key doesnt keep firing
    public boolean keyDown(int code) {
        return keys[code] && !prevKeys[code];
    }
}