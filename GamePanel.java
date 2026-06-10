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
    private Devil parentFrame;
    private UserTable users;
    private UserData currentUser;
    private boolean paused = false;

    public GamePanel(Devil parentFrame, UserTable users, UserData currentUser) {
        this.parentFrame = parentFrame;
        this.users = users;
        this.currentUser = currentUser;

        setPreferredSize(new Dimension(1500, 750));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        for (Level lvl : levels) {
            lvl.reset();
        }
        timer = new Timer(15, this);
        timer.start();



        currentLevel = currentUser.getSavedLevel();
        guy.setX(currentUser.getSavedX());
        guy.setY(currentUser.getSavedY());
        guy.setOnGround(true);

        buildHUD();

        timer = new Timer(15, this);
        timer.start();
    }

    private void buildHUD() {
        // Invisible pause button
        JButton pauseBtn = new JButton();
        pauseBtn.setOpaque(false);
        pauseBtn.setContentAreaFilled(false);
        pauseBtn.setBorderPainted(false);
        pauseBtn.setFocusPainted(false);
        pauseBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pauseBtn.setBounds(43, 6, 52, 77);
        pauseBtn.addActionListener(e -> showPauseMenu());
        add(pauseBtn);

        // Invisible restart button
        JButton restartBtn = new JButton();
        restartBtn.setOpaque(false);
        restartBtn.setContentAreaFilled(false);
        restartBtn.setBorderPainted(false);
        restartBtn.setFocusPainted(false);
        restartBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        restartBtn.setBounds(124, 29, 66, 57);
        restartBtn.addActionListener(e -> restartLevel());
        add(restartBtn);
    }

    private void showPauseMenu() {
        paused = true;
        timer.stop();
        
        JPanel overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        overlay.setOpaque(false);
        overlay.setLayout(null);
        overlay.setBounds(0, 0, 1500, 750);
        
        // Pause menu background
        JPanel menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(18, 18, 40, 240));
                g.fillRoundRect(590, 250, 320, 250, 15, 15);
            }
        };
        menuPanel.setOpaque(false);
        menuPanel.setLayout(null);
        menuPanel.setBounds(0, 0, 1500, 750);
        
        // Pause title
        JLabel titleLabel = new JLabel("PAUSED", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 200, 50));
        titleLabel.setBounds(590, 270, 320, 50);
        menuPanel.add(titleLabel);
        
        // Resume button
        JButton resumeBtn = new JButton("RESUME");
        resumeBtn.setFont(new Font("Courier New", Font.BOLD, 18));
        resumeBtn.setForeground(Color.WHITE);
        resumeBtn.setBackground(new Color(220, 50, 50));
        resumeBtn.setBorder(BorderFactory.createEmptyBorder());
        resumeBtn.setFocusPainted(false);
        resumeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resumeBtn.setBounds(610, 340, 280, 50);
        resumeBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                resumeBtn.setBackground(new Color(255, 100, 50));
            }
            public void mouseExited(MouseEvent e) {
                resumeBtn.setBackground(new Color(220, 50, 50));
            }
        });
        resumeBtn.addActionListener(e -> {
            remove(overlay);
            remove(menuPanel);
            paused = false;
            for (int i = 0; i < keys.length; i++) {
                keys[i] = false;
            }
            requestFocus();
            timer.start();
            repaint();
        });
        menuPanel.add(resumeBtn);
        
        // Quit button
        JButton quitBtn = new JButton("QUIT");
        quitBtn.setFont(new Font("Courier New", Font.BOLD, 18));
        quitBtn.setForeground(Color.WHITE);
        quitBtn.setBackground(new Color(60, 20, 20));
        quitBtn.setBorder(BorderFactory.createLineBorder(new Color(120, 60, 60, 150), 1));
        quitBtn.setFocusPainted(false);
        quitBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        quitBtn.setBounds(610, 410, 280, 50);
        quitBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                quitBtn.setBackground(new Color(100, 30, 30));
            }
            public void mouseExited(MouseEvent e) {
                quitBtn.setBackground(new Color(60, 20, 20));
            }
        });
        quitBtn.addActionListener(e -> {
            remove(overlay);
            remove(menuPanel);
            quitToMenu();
        });
        menuPanel.add(quitBtn);
        
        add(overlay);
        add(menuPanel);
        setComponentZOrder(overlay, 0);
        setComponentZOrder(menuPanel, 0);
        revalidate();
        repaint();
    }

    private void restartLevel() {
        paused = false;
        levels[currentLevel].reset();
        guy.respawn();
    }

    private void quitToMenu() {
        timer.stop();
        currentUser.saveProgress(currentLevel, guy.getX(), guy.getY());
        users.put(currentUser.getUsername(), currentUser);
        parentFrame.showMenu(currentUser);
    }

    public void updatePlay() {
        System.out.println("Current Level: " + (currentLevel));
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