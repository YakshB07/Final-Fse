/*
 * Yaksh Butani
 * Level Devil main file
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
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        new Devil();
    }
}

interface Level {
    void reset();
    void update(Guy guy);
    void draw(Graphics g, int panelWidth, int panelHeight);
    boolean isFinished();
}

class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Guy guy = new Guy();
    private boolean[] keys = new boolean[2000];
    private Timer timer;
    private int currentLevel = 0;
    private Level[] levels = {new Level1(), new Level2()};

    public GamePanel() {
        setPreferredSize(new Dimension(1500, 750));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        for (Level lvl : levels) {
            lvl.reset();
        }
        timer = new Timer(15, this);
        timer.start();
    }

    public void updatePlay() {
        int dx = 0;
        if (keys[KeyEvent.VK_LEFT]){
            dx = -1;
        }
        if (keys[KeyEvent.VK_RIGHT]){
            dx = 1;
        }
        boolean jump = keys[KeyEvent.VK_UP];

        guy.update(dx, jump);

        if (guy.getX() < 100) {
            guy.setX(100);
        }
        if (guy.getX() > 1369 - Guy.SIZE) {
            guy.setX(1369 - Guy.SIZE);
        }

        levels[currentLevel].update(guy);
        
        if (levels[currentLevel].isFinished() && currentLevel < levels.length - 1) {
            currentLevel++;
            guy.respawn();
            levels[currentLevel].reset();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        levels[currentLevel].draw(g, getWidth(), getHeight());
        guy.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updatePlay();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}