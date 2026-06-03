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

class GamePanel extends JPanel implements KeyListener, ActionListener {

    private Guy guy = new Guy();
    private boolean[] keys = new boolean[2000];
    private Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(700, 600));
        setBackground(Color.WHITE);
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        timer = new Timer(15, this);
        timer.start();
    }

    public void updatePlay() {
        // left/right input
        int dx = 0;
        if (keys[KeyEvent.VK_LEFT])  dx = -1;
        if (keys[KeyEvent.VK_RIGHT]) dx =  1;

        // jump input
        boolean jump = keys[KeyEvent.VK_UP];

        guy.update(dx, jump);

        // keep guy inside the window left/right
        if (guy.getX() < 0)              guy.setX(0);
        if (guy.getX() > 700 - Guy.SIZE) guy.setX(700 - Guy.SIZE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw ground
        g.setColor(new Color(60, 60, 60));
        g.fillRect(0, 562, 700, 10);

        guy.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updatePlay();
        repaint();
    }

    @Override public void keyPressed(KeyEvent e)  { keys[e.getKeyCode()] = true;  }
    @Override public void keyReleased(KeyEvent e) { keys[e.getKeyCode()] = false; }
    @Override public void keyTyped(KeyEvent e)    { }
}