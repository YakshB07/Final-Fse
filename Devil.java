/*
 * Yaksh Butani
 * Level Devil main file
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*; 

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
    private BufferedImage mask;
    private Timer timer;

    private Image backImage;
    private Image spikes;

    public GamePanel() {

        backImage = new ImageIcon("map/map1.png").getImage();
        spikes = new ImageIcon("spike.png").getImage();
        setPreferredSize(new Dimension(1500, 750));
        // setBackground(Color.WHITE);
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
        if (guy.getX() < 100){
            guy.setX(100);
        }
        if (guy.getX() > 1369 - Guy.SIZE){
            guy.setX(1369 - Guy.SIZE);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw ground
        // g.setColor(new Color(60, 60, 60));
        // g.fillRect(0, 562, 700, 10);
        g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(spikes, 700,421, 80,30,null);
        guy.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updatePlay();
        repaint();
    }

    @Override 
    public void keyPressed(KeyEvent e){ 
        keys[e.getKeyCode()] = true;  
    }
    @Override 
    public void keyReleased(KeyEvent e){ 
        keys[e.getKeyCode()] = false; 
    }
    @Override 
    public void keyTyped(KeyEvent e){ 

    }
}