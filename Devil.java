
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

    private boolean[] keys = new boolean[2000];
    private boolean[] prevKeys = new boolean[2000]; 
    private char keyTyped;


    public GamePanel() {
        setPreferredSize(new Dimension(700, 830));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);

    }

    public void actionPerformed(ActionEvent e) {
    
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