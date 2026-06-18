/*
 * MenuPanel.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * This panel is the main menu screen the player sees after logging in.
 * It shows their username, best run, and current death count on a small
 * stats card. It also shows buttons to play or continue their saved game,
 * start a new game if they have a save, view the leaderboard, or log out.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuPanel extends JPanel {

    private Devil parentFrame;    // reference to the main frame for switching screens
    private UserTable users;      // the full user table, kept in case we need to update data
    private UserData currentUser; // the player who is currently logged in

    private Image bgImage; // the background image drawn behind everything

    // colors used throughout the panel
    private static final Color ACCENT = new Color(220, 50, 50);      // red for the main play button
    private static final Color ACCENT2 = new Color(255, 100, 50);    // lighter red for hover and death count text
    private static final Color TEXTMAIN = new Color(240, 230, 220);  // main bright text color
    private static final Color TEXTDIM = new Color(140, 130, 150);   // dimmer color for the stat labels
    private static final Color CARDBG = new Color(18, 18, 40, 220);  // semi transparent stats card background

    public MenuPanel(Devil parentFrame, UserTable users, UserData user) {
        this.parentFrame = parentFrame;
        this.users = users;
        this.currentUser = user;

        bgImage = new ImageIcon("BgImages/menu_bg.png").getImage();

        setPreferredSize(new Dimension(1500, 750));
        setLayout(null);

        buildButtons();
    }

    /*
     * Builds and adds all the buttons to the menu.
     * If the player has a save, the play button says CONTINUE and a NEW GAME
     * button also appears below it. Otherwise it just says PLAY.
     */
    private void buildButtons() {
        Font btnFont = new Font("Courier New", Font.BOLD, 20);
        Font logFont = new Font("Courier New", Font.BOLD, 14);

        int bw = 320; // button width
        int bh = 65;  // button height
        int cx = (1500 - bw) / 2; // centered x position

        // check if the player has a save to continue from
        boolean hasSave = currentUser.getSavedLevel() > 1 || currentUser.getSavedX() != 300;

        String playLabel;
        if (hasSave) {
            playLabel = "CONTINUE";
        } else {
            playLabel = "PLAY";
        }

        JButton playBtn = makeBtn(playLabel, btnFont, ACCENT, new Color(200, 30, 30));
        playBtn.setBounds(cx, 435, bw, bh);
        playBtn.addActionListener(e -> parentFrame.startGame(currentUser));
        add(playBtn);

        // only show new game button if there is an existing save to overwrite
        if (hasSave) {
            JButton newBtn = makeBtn("NEW GAME", logFont, new Color(50, 40, 80), new Color(70, 55, 110));
            newBtn.setBounds(cx, 515, bw, 45);
            newBtn.addActionListener(e -> {
                currentUser.resetRun(); // clear the save data and start fresh
                parentFrame.startGame(currentUser);
            });
            add(newBtn);
        }

        JButton lbBtn = makeBtn("LEADERBOARD", btnFont, new Color(30, 25, 60), new Color(55, 45, 90));
        lbBtn.setBounds(cx, 580, bw, bh);
        lbBtn.addActionListener(e -> parentFrame.showLeaderboard(currentUser));
        add(lbBtn);

        // logout button is in the top left corner
        JButton logoutBtn = makeBtn("LOGOUT", logFont, new Color(20, 18, 35), new Color(35, 30, 55));
        logoutBtn.setBounds(30, 30, 130, 38);
        logoutBtn.addActionListener(e -> parentFrame.showLogin());
        add(logoutBtn);
    }

    /*
     * Creates and returns a styled button with a hover color change.
     */
    private JButton makeBtn(String text, Font f, Color bg, Color hover) {
        JButton b = new JButton(text);
        b.setFont(f);
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setBorder(BorderFactory.createLineBorder(new Color(90, 60, 120, 150), 1));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(hover);
            }
            public void mouseExited(MouseEvent e) {
                b.setBackground(bg);
            }
        });
        return b;
    }

    /*
     * Draws the background image, the welcome message, and the stats card
     * showing the player's best run and current death count.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);

        // draw the welcome message centered above the stats card
        g2.setFont(new Font("Courier New", Font.PLAIN, 20));
        g2.setColor(TEXTMAIN);
        String welcome = "Welcome back,  " + currentUser.getUsername().toUpperCase() + "!";
        FontMetrics wfm = g2.getFontMetrics();
        g2.drawString(welcome, (getWidth() - wfm.stringWidth(welcome)) / 2, 310);

        // stats card dimensions and position
        int cardW = 400;
        int cardH = 90;
        int cardX = (getWidth() - cardW) / 2;
        int cardY = 328;

        // draw the card background and border
        g2.setColor(CARDBG);
        g2.fillRoundRect(cardX, cardY, cardW, cardH, 10, 10);
        g2.setColor(new Color(90, 60, 120, 120));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(cardX, cardY, cardW, cardH, 10, 10);

        // draw the stat labels on the top row of the card
        g2.setFont(new Font("Courier New", Font.BOLD, 13));
        g2.setColor(TEXTDIM);
        g2.drawString("BEST RUN", cardX + 30, cardY + 28);
        g2.drawString("CURRENT DEATHS", cardX + 220, cardY + 28);

        // draw the best run value below its label
        g2.setFont(new Font("Courier New", Font.BOLD, 22));
        g2.setColor(TEXTMAIN);
        g2.drawString(currentUser.bestScoreString(), cardX + 30, cardY + 65);

        // draw the current death count below its label, singular if exactly 1
        g2.setColor(ACCENT2);
        String deathStr;
        if (currentUser.getCurrentDeaths() == 1) {
            deathStr = currentUser.getCurrentDeaths() + " death";
        } else {
            deathStr = currentUser.getCurrentDeaths() + " deaths";
        }
        g2.drawString(deathStr, cardX + 220, cardY + 65);
    }
}