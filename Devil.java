/*
 * Devil.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * This is the main file that runs the game. It creates the window
 * and handles switching between the login screen, main menu, leaderboard,
 * win screen, and the game. It also holds the Level interface that every level must follow.
 */

import java.awt.*;
import javax.swing.*;

public class Devil extends JFrame {

    private UserTable users = new UserTable(); // all player accounts stored in a hashtable
    private JPanel currentPanel;               // the screen currently showing

    public Devil() {
        super("Level Devil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        showLogin();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /* showLogin() switches to the login page, called on startup or after logout. */
    public void showLogin() {
        swapPanel(new LoginPanel(this, users));
    }

    /* showMenu(UserData user) switches to the main menu for the logged in player. */
    public void showMenu(UserData user) {
        swapPanel(new MenuPanel(this, users, user));
    }

    /* showLeaderboard(UserData user) switches to the leaderboard page. */
    public void showLeaderboard(UserData user) {
        swapPanel(new LeaderboardPanel(this, users, user));
    }

    /* showWin(UserData user) switches to the win screen after the player beats the game. */
    public void showWin(UserData user) {
        swapPanel(new WinPanel(this, user));
    }

    /* startGame(UserData user) switches to the game panel for the current player. */
    public void startGame(UserData user) {
        swapPanel(new GamePanel(this, users, user));
    }

    /* swapPanel(JPanel panel) removes the old screen and puts the new one on the window.
     * panel is the new screen to show.
     */
    private void swapPanel(JPanel panel) {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = panel;
        add(panel);
        pack();
        revalidate();
        repaint();
        panel.requestFocusInWindow(); // makes sure keyboard input goes to the new screen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Devil());
    }

    /*
     * Level interface that every level must implement.
     * Adding a new level is easy, just implement these 5 methods
     * and GamePanel will handle the rest automatically.
     */
    interface Level {
        public void reset(); // resets the level to its start state
        public void update(Guy guy); // runs every tick, handles level logic
        public void draw(Graphics g, int panelWidth, int panelHeight); // draws everything in the level
        public boolean isFinished(); // returns true when player reaches the end
        public boolean playerDied(); // returns true on the frame the player dies
    }
}