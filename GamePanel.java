import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Devil parentFrame;
    private UserTable users;
    private UserData currentUser;
    private Guy guy = new Guy(this);
    private boolean[] keys = new boolean[2000];
    private Timer timer;
    private int currentLevel = 0;
    private Devil.Level[] levels = {new Level1(), new Level2(), new Level3()};
    private Point[] spawns = {new Point(300, 416), new Point(300, 465), new Point(0, 0)};
    private boolean paused = false;
    private int dx = 0;
    private boolean jump;


   public GamePanel(Devil parentFrame, UserTable users, UserData currentUser) {
        this.parentFrame = parentFrame;
        this.users = users;
        this.currentUser = currentUser;
        setPreferredSize(new Dimension(1500, 750));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        for (Devil.Level lvl : levels) {
            lvl.reset();
        }
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

    private void restartLevel() {
        paused = false;
        levels[currentLevel].reset();
        guy.respawn();
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


    private void quitToMenu() {
        timer.stop();
        currentUser.saveProgress(currentLevel, guy.getX(), guy.getY());
        users.put(currentUser.getUsername(), currentUser);
        parentFrame.showMenu(currentUser);
    }

    

    public void updatePlay() {
        // System.out.println("Current Level: " + (currentLevel));
        dx = 0;
        if (keys[KeyEvent.VK_LEFT]){
            dx = -1;
        }
        if (keys[KeyEvent.VK_RIGHT]){
            dx = 1;
        }
        
        jump = keys[KeyEvent.VK_UP];

        // guy.update(dx, jump);

        levels[currentLevel].update(guy);
        
        if (levels[currentLevel].isFinished() && currentLevel < levels.length - 1) {
            currentLevel++;
            guy.setspawn((int)spawns[currentLevel].getX(), (int)spawns[currentLevel].getY());
            guy.respawn();
            levels[currentLevel].reset();
            guy.setMask("map/map" + (currentLevel + 1) + "mask.png");
        }
    }

   
    private void showWinScreen() {
        removeAll();
        setLayout(null);

        Font bigFont = new Font("Courier New", Font.BOLD, 48);
        Font subFont = new Font("Courier New", Font.BOLD, 20);
        Font btnFont = new Font("Courier New", Font.BOLD, 16);

        JLabel winLabel = new JLabel("YOU WIN!", SwingConstants.CENTER);
        winLabel.setFont(bigFont);
        winLabel.setForeground(new Color(255, 200, 50));
        winLabel.setBounds(0, 220, 1500, 70);
        add(winLabel);

        String deathText;
        if (currentUser.getCurrentDeaths() == 1) {
            deathText = "You finished with 1 death this run.";
        } else {
            deathText = "You finished with " + currentUser.getCurrentDeaths() + " deaths this run.";
        }

        JLabel deathLabel = new JLabel(deathText, SwingConstants.CENTER);
        deathLabel.setFont(subFont);
        deathLabel.setForeground(new Color(240, 230, 220));
        deathLabel.setBounds(0, 310, 1500, 40);
        add(deathLabel);

        String bestText;
        if (currentUser.getBestDeaths() == 1) {
            bestText = "Your best run: " + currentUser.getBestDeaths() + " death";
        } else {
            bestText = "Your best run: " + currentUser.getBestDeaths() + " deaths";
        }

        JLabel bestLabel = new JLabel(bestText, SwingConstants.CENTER);
        bestLabel.setFont(subFont);
        bestLabel.setForeground(new Color(140, 130, 150));
        bestLabel.setBounds(0, 360, 1500, 40);
        add(bestLabel);

        JButton menuBtn = new JButton("BACK TO MENU");
        menuBtn.setFont(btnFont);
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setBackground(new Color(220, 50, 50));
        menuBtn.setBorder(BorderFactory.createEmptyBorder());
        menuBtn.setFocusPainted(false);
        menuBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menuBtn.setBounds(590, 430, 320, 55);
        menuBtn.addActionListener(e -> parentFrame.showMenu(currentUser));
        menuBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                menuBtn.setBackground(new Color(255, 100, 50));
            }
            public void mouseExited(MouseEvent e) {
                menuBtn.setBackground(new Color(220, 50, 50));
            }
        });
        add(menuBtn);

        JButton lbBtn = new JButton("VIEW LEADERBOARD");
        lbBtn.setFont(btnFont);
        lbBtn.setForeground(Color.WHITE);
        lbBtn.setBackground(new Color(30, 25, 60));
        lbBtn.setBorder(BorderFactory.createLineBorder(new Color(90, 60, 120, 150), 1));
        lbBtn.setFocusPainted(false);
        lbBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbBtn.setBounds(590, 500, 320, 55);
        lbBtn.addActionListener(e -> parentFrame.showLeaderboard(currentUser));
        lbBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                lbBtn.setBackground(new Color(55, 45, 90));
            }
            public void mouseExited(MouseEvent e) {
                lbBtn.setBackground(new Color(30, 25, 60));
            }
        });
        add(lbBtn);

        revalidate();
        repaint();
    }
    
    public int returnDx(){
        return dx;
    }

    public boolean returnJump(){
        return jump;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        levels[currentLevel].draw(g, getWidth(), getHeight());

        guy.draw(g);

        Graphics2D g2 = (Graphics2D) g;

        String deathStr;
        if (currentUser.getCurrentDeaths() == 1) {
            deathStr = "Deaths: 1";
        } else {
            deathStr = "Deaths: " + currentUser.getCurrentDeaths();
        }

        g2.setFont(new Font("Courier New", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(deathStr)) / 2;

        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(deathStr, textX + 2, 32);

        g2.setColor(new Color(255, 255, 255));
        g2.drawString(deathStr, textX, 30);

        g2.setFont(new Font("Courier New", Font.PLAIN, 13));
        g2.setColor(new Color(200, 200, 200));
        String levelStr = "Level " + currentLevel;
        int lvlX = (getWidth() - g2.getFontMetrics().stringWidth(levelStr)) / 2;
        g2.drawString(levelStr, lvlX, 52);

        if (paused) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setFont(new Font("Courier New", Font.BOLD, 48));
            g2.setColor(new Color(240, 230, 220));
            String pauseStr = "PAUSED";
            FontMetrics pfm = g2.getFontMetrics();
            g2.drawString(pauseStr, (getWidth() - pfm.stringWidth(pauseStr)) / 2, getHeight() / 2);

            g2.setFont(new Font("Courier New", Font.PLAIN, 18));
            g2.setColor(new Color(140, 130, 150));
            String hint = "Click PAUSE again to resume";
            FontMetrics hfm = g2.getFontMetrics();
            g2.drawString(hint, (getWidth() - hfm.stringWidth(hint)) / 2, getHeight() / 2 + 45);
        }
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