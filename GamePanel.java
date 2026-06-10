import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Guy guy = new Guy();
    private boolean[] keys = new boolean[2000];
    private Timer timer;
    private int currentLevel = 0;
    private Level[] levels = {new Level1(), new Level2()};
    private Point[] spawns = {new Point(300, 416), new Point(300, 465)};

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
        // System.out.println("Current Level: " + (currentLevel));
        int dx = 0;
        if (keys[KeyEvent.VK_LEFT]){
            dx = -1;
        }
        if (keys[KeyEvent.VK_RIGHT]){
            dx = 1;
        }
        boolean jump = keys[KeyEvent.VK_UP];

        guy.update(dx, jump);

        levels[currentLevel].update(guy);
        
        if (levels[currentLevel].isFinished() && currentLevel < levels.length - 1) {
            currentLevel++;
            guy.setspawn((int)spawns[currentLevel].getX(), (int)spawns[currentLevel].getY());
            guy.respawn();
            levels[currentLevel].reset();
            guy.setMask("map/map" + (currentLevel + 1) + "mask.png");
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