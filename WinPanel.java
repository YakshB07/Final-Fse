/*
 * WinPanel.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * The screen that shows when the player beats all the levels.
 * Displays their death count for this run and their all time best, then
 * gives them buttons to go back to the menu or view the leaderboard.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class WinPanel extends JPanel {

    private Devil parentFrame;    // reference to the main frame for switching screens
    private UserData currentUser; // the player who just finished the game

    private static final Color BG = new Color(10, 10, 20);      // dark background
    private static final Color GOLD = new Color(255, 200, 50);  // gold color for the win title
    private static final Color TEXT = new Color(240, 230, 220); // main white text
    private static final Color DIM = new Color(140, 130, 150);  // dimmer text for secondary info

    public WinPanel(Devil parentFrame, UserData currentUser) {
        this.parentFrame = parentFrame;
        this.currentUser = currentUser;

        setPreferredSize(new Dimension(1500, 750));
        setBackground(BG);
        setLayout(null);

        buildContent();
    }

    /*
     * Builds and adds all the labels and buttons to the win screen.
     * Shows the death count for this run, the player's all time best, and
     * two buttons to go back to the menu or view the leaderboard.
     */
    private void buildContent() {
        Font bigFont = new Font("Courier New", Font.BOLD, 72);
        Font subFont = new Font("Courier New", Font.BOLD, 24);
        Font btnFont = new Font("Courier New", Font.BOLD, 18);

        JLabel winLabel = new JLabel("YOU WIN!", SwingConstants.CENTER);
        winLabel.setFont(bigFont);
        winLabel.setForeground(GOLD);
        winLabel.setBounds(0, 180, 1500, 90);
        add(winLabel);

        // use singular "death" if count is exactly 1, plural otherwise
        String deathText;
        if (currentUser.getLastRunDeaths() == 1) {
            deathText = "You finished with 1 death this run.";
        } else {
            deathText = "You finished with " + currentUser.getLastRunDeaths() + " deaths this run.";
        }

        JLabel deathLabel = new JLabel(deathText, SwingConstants.CENTER);
        deathLabel.setFont(subFont);
        deathLabel.setForeground(TEXT);
        deathLabel.setBounds(0, 300, 1500, 40);
        add(deathLabel);

        // show the player's all time best run below the current run
        String deathBestText;
        if (currentUser.getLastRunDeaths() == 1) {
            deathBestText = "Your best run: 1 death";
        } else {
            deathBestText = "Your best run: " + currentUser.getLastRunDeaths() + " deaths";
        }

        JLabel bestLabel = new JLabel(deathBestText, SwingConstants.CENTER);
        bestLabel.setFont(subFont);
        bestLabel.setForeground(DIM);
        bestLabel.setBounds(0, 350, 1500, 40);
        add(bestLabel);

        JButton menuBtn = makeBtn("BACK TO MENU", new Color(220, 50, 50), new Color(255, 100, 50));
        menuBtn.setBounds(590, 440, 320, 60);
        menuBtn.addActionListener(e -> parentFrame.showMenu(currentUser));
        add(menuBtn);

        JButton lbBtn = makeBtn("VIEW LEADERBOARD", new Color(30, 25, 60), new Color(55, 45, 90));
        lbBtn.setBounds(590, 520, 320, 60);
        lbBtn.addActionListener(e -> parentFrame.showLeaderboard(currentUser));
        add(lbBtn);
    }

    /*
     * Creates and returns a styled button with a hover color change.
     */
    private JButton makeBtn(String text, Color bg, Color hover) {
        JButton b = new JButton(text);
        b.setFont(new Font("Courier New", Font.BOLD, 18));
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            public void mouseExited(MouseEvent e) { b.setBackground(bg); }
        });
        return b;
    }

    /*
     * Draws the dark gradient background behind all the labels and buttons.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, new Color(10, 10, 20), 0, 750, new Color(5, 5, 35)));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}