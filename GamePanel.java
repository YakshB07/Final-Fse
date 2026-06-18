/*
 * GamePanel.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * This is the main game screen. It runs the game loop using a Swing Timer
 * at roughly 15ms per tick. It handles keyboard input for moving and jumping,
 * keeps track of which level the player is on, counts deaths, saves progress
 * when the player quits or moves to the next level, and shows a pause menu
 * when the player hits the pause button. When the last level is finished it
 * shows the win screen directly inside this panel.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GamePanel extends JPanel implements KeyListener, ActionListener {

    private Devil parentFrame;    // reference to the main frame for switching screens
    private UserTable users;      // the full user table used to save progress
    private UserData currentUser; // the player who is currently logged in and playing

    private Guy guy = new Guy(this); // the player character
    private boolean[] keys = new boolean[2000]; // tracks which keys are currently held down
    private Timer timer;           // the main game loop timer
    private int currentLevel = 0;  // index of the level the player is currently on
    private Devil.Level[] levels = {new Level1(), new Level2(), new Level3(), new Level4()}; // all levels in order
    private Point[] spawns = {new Point(300, 416), new Point(300, 465), new Point(900, 250), new Point(725, 60)}; // spawn point for each level
    private boolean paused = false; // true when the game is paused
    private int dx = 0;            // horizontal direction, -1 left, 0 still, 1 right
    private boolean jump = false;  // true when the player is holding the jump key

    public GamePanel(Devil parentFrame, UserTable users, UserData currentUser) {
        this.parentFrame = parentFrame;
        this.users = users;
        this.currentUser = currentUser;

        setPreferredSize(new Dimension(1500, 750));
        setFocusable(true);
        setLayout(null);
        requestFocus();
        addKeyListener(this);

        // load the player's saved level, subtract 1 since save data is 1 indexed
        currentLevel = currentUser.getSavedLevel() - 1;
        if (currentLevel < 0) {
            currentLevel = 0; // clamp to first level if save is invalid
        }
        if (currentLevel >= levels.length) {
            currentLevel = levels.length - 1; // clamp to last level if save is out of range
        }

        // place the player at the correct spawn for their level
        Point spawn = spawns[currentLevel];
        guy.setspawn((int)spawn.getX(), (int)spawn.getY());
        guy.respawn();
        guy.setMask("map/map" + (currentLevel + 1) + "mask.png");

        // reset all levels so moving obstacles start fresh
        for (Devil.Level lvl : levels) {
            lvl.reset();
        }

        buildHUD();

        timer = new Timer(15, this); // fires every 15ms for the game loop
        timer.start();
    }

    /*
     * Adds the invisible pause and restart buttons on top of the HUD image.
     * These are transparent so they sit over the HUD graphic without showing a button background.
     */
    private void buildHUD() {
        JButton pauseBtn = new JButton();
        pauseBtn.setOpaque(false);
        pauseBtn.setContentAreaFilled(false);
        pauseBtn.setBorderPainted(false);
        pauseBtn.setFocusPainted(false);
        pauseBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pauseBtn.setBounds(43, 6, 52, 77); // positioned over the pause icon in the HUD image
        pauseBtn.addActionListener(e -> showPauseMenu());
        add(pauseBtn);

        JButton restartBtn = new JButton();
        restartBtn.setOpaque(false);
        restartBtn.setContentAreaFilled(false);
        restartBtn.setBorderPainted(false);
        restartBtn.setFocusPainted(false);
        restartBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        restartBtn.setBounds(124, 29, 66, 57); // positioned over the restart icon in the HUD image
        restartBtn.addActionListener(e -> restartLevel());
        add(restartBtn);
    }

    /*
     * Resets the current level and puts the player back at the spawn point.
     * Called when the player clicks the restart button in the HUD.
     */
    private void restartLevel() {
        paused = false;
        levels[currentLevel].reset();
        Point spawn = spawns[currentLevel];
        guy.setspawn((int)spawn.getX(), (int)spawn.getY());
        guy.respawn();
        if (!timer.isRunning()) {
            timer.start();
        }
        requestFocus(); // regain focus so keyboard input works again
    }

    /*
     * Stops the game loop and shows the pause overlay with resume and quit buttons.
     * The overlay is a transparent dark panel added on top of everything.
     */
    private void showPauseMenu() {
        paused = true;
        timer.stop();

        // dark semi transparent overlay covering the whole screen
        JPanel overlay = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        overlay.setOpaque(false);
        overlay.setLayout(null);
        overlay.setBounds(0, 0, 1500, 750);

        // the actual pause menu card drawn in the center of the screen
        JPanel menuPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(18, 18, 40, 240));
                ((Graphics2D)g).fillRoundRect(590, 250, 320, 250, 15, 15);
            }
        };
        menuPanel.setOpaque(false);
        menuPanel.setLayout(null);
        menuPanel.setBounds(0, 0, 1500, 750);

        JLabel titleLabel = new JLabel("PAUSED", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 200, 50));
        titleLabel.setBounds(590, 270, 320, 50);
        menuPanel.add(titleLabel);

        // resume button removes the overlay and restarts the timer
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
            // clear all key states so nothing carries over from before the pause
            for (int i = 0; i < keys.length; i++) {
                keys[i] = false;
            }
            requestFocus();
            timer.start();
            repaint();
        });
        menuPanel.add(resumeBtn);

        // quit button saves progress and returns to the main menu
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

        // add overlay and menu on top of everything else
        add(overlay);
        add(menuPanel);
        setComponentZOrder(overlay, 0);
        setComponentZOrder(menuPanel, 0);
        revalidate();
        repaint();
    }

    /*
     * Saves the player's current level and position then goes back to the main menu.
     * Called when the player clicks quit from the pause menu.
     */
    private void quitToMenu() {
        timer.stop();
        currentUser.saveProgress(currentLevel + 1, guy.getX(), guy.getY()); // save level as 1 indexed
        users.put(currentUser.getUsername(), currentUser);
        parentFrame.showMenu(currentUser);
    }

    /*
     * Returns the current horizontal movement direction.
     * Used by the Guy class to update player movement.
     */
    public int returnDx() {
        return dx;
    }

    /*
     * Returns whether the jump key is currently being held.
     * Used by the Guy class to decide if the player should jump.
     */
    public boolean returnJump() {
        return jump;
    }

    /*
     * The main game update method called every timer tick.
     * Reads keyboard input, updates the current level, handles death counting,
     * level transitions, and triggers the win screen when the last level is done.
     */
    public void updatePlay() {
        if (paused) {
            return; // skip all updates while the game is paused
        }

        // figure out horizontal direction from left and right arrow keys
        dx = 0;
        if (keys[KeyEvent.VK_LEFT]) {
            dx = -1;
        }
        if (keys[KeyEvent.VK_RIGHT]) {
            dx = 1;
        }
        jump = keys[KeyEvent.VK_UP];

        levels[currentLevel].update(guy);

        // if the level says the player died, add a death to their count and save immediately
        if (levels[currentLevel].playerDied()) {
            currentUser.addDeath();
            users.put(currentUser.getUsername(), currentUser);
        }

        // if the level is done and there is another level after this one, move to it
        if (levels[currentLevel].isFinished() && currentLevel < levels.length - 1) {
            currentLevel++;
            Point spawn = spawns[currentLevel];
            guy.setspawn((int)spawn.getX(), (int)spawn.getY());
            guy.respawn();
            levels[currentLevel].reset();
            guy.setMask("map/map" + (currentLevel + 1) + "mask.png");
            currentUser.saveProgress(currentLevel + 1, guy.getX(), guy.getY()); // save the new level
            users.put(currentUser.getUsername(), currentUser);
        }

        // if the last level is finished, stop the timer and show the win screen
        if (levels[currentLevel].isFinished() && currentLevel == levels.length - 1) {
            timer.stop();
            currentUser.finishedGame(); // sets lastRunDeaths and updates bestDeaths before reset
            users.put(currentUser.getUsername(), currentUser); // saves correct bestDeaths to file
            parentFrame.showWin(currentUser);
        }
    }

    /*
     * Clears the panel and builds a win screen directly inside it.
     * Shows the player's death count for this run and their all time best,
     * with buttons to go back to the menu or view the leaderboard.
     */
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

        // use singular "death" if count is exactly 1, plural otherwise
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

        // show the player's best run death count below the current run count
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

    /*
     * Draws the current level background and player, then draws the death counter
     * and level number centered at the top of the screen.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        levels[currentLevel].draw(g, getWidth(), getHeight());
        guy.draw(g);

        Graphics2D g2 = (Graphics2D) g;

        // use singular "Deaths: 1" if count is 1, plural otherwise
        String deathStr;
        if (currentUser.getCurrentDeaths() == 1) {
            deathStr = "Deaths: 1";
        } else {
            deathStr = "Deaths: " + currentUser.getCurrentDeaths();
        }

        // center the death counter and draw a dark shadow behind it so it's readable on any background
        g2.setFont(new Font("Courier New", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(deathStr)) / 2;
        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(deathStr, textX + 2, 32);
        g2.setColor(Color.WHITE);
        g2.drawString(deathStr, textX, 30);

        // draw the level number just below the death counter
        g2.setFont(new Font("Courier New", Font.PLAIN, 13));
        g2.setColor(new Color(200, 200, 200));
        String levelStr = "Level " + (currentLevel + 1);
        int lvlX = (getWidth() - g2.getFontMetrics().stringWidth(levelStr)) / 2;
        g2.drawString(levelStr, lvlX, 52);
    }

    // called every timer tick, runs the game update then redraws the screen
    public void actionPerformed(ActionEvent e) {
        updatePlay();
        repaint();
    }

    // sets the key state to true when a key is pressed
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    // sets the key state to false when a key is released
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    // required by KeyListener but we don't use it
    public void keyTyped(KeyEvent e) {
    }
}