/*
 * LeaderboardPanel.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * This panel shows the Hall of Fame leaderboard screen. It pulls all users who
 * have finished the game and ranks them by fewest deaths on a completed run.
 * It also highlights the current logged in user in the list so they can find
 * themselves easily. The player can hit the back button to return to the menu.
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class LeaderboardPanel extends JPanel {

    private Devil parentFrame;// reference to the main frame for switching screens
    private UserTable users; // the table of all user accounts
    private UserData currentUser; // the user who is currently logged in

    // colors used throughout the panel
    private static final Color BG = new Color(10, 10, 20);           
    private static final Color ACCENT2 = new Color(255, 180, 50);    
    private static final Color TEXT_MAIN = new Color(240, 230, 220); 
    private static final Color TEXT_DIM = new Color(140, 130, 150);  
    private static final Color CARD_BG = new Color(18, 18, 40, 210); 
    private static final Color ROW_ALT = new Color(30, 25, 55, 180);
    private static final Color GOLD = new Color(255, 200, 50);       
    private static final Color SILVER = new Color(190, 190, 210);    
    private static final Color BRONZE = new Color(200, 120, 60);     

    public LeaderboardPanel(Devil parentFrame, UserTable users, UserData currentUser) {
        this.parentFrame = parentFrame;
        this.users = users;
        this.currentUser = currentUser;

        setPreferredSize(new Dimension(1500, 750));
        setBackground(BG);
        setLayout(null);

        Font logFont = new Font("Courier New", Font.BOLD, 14);
        JButton backBtn = makeBtn("BACK", logFont, new Color(20, 18, 35), new Color(35, 30, 55));
        backBtn.setBounds(30, 30, 120, 38);
        backBtn.addActionListener(e -> parentFrame.showMenu(currentUser));
        add(backBtn);
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
            public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            public void mouseExited(MouseEvent e) { b.setBackground(bg); }
        });
        return b;
    }

    /*
     * Draws the leaderboard panel including the title, column headers,
     * and all leaderboard rows. Also highlights the current user's row
     * and shows a note if they haven't finished the game yet.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw the title centered at the top
        g2.setFont(new Font("Courier New", Font.BOLD, 46));
        String title = "HALL OF FAME";
        FontMetrics fm = g2.getFontMetrics();
        int tx = (getWidth() - fm.stringWidth(title)) / 2;
        g2.setColor(ACCENT2);
        g2.drawString(title, tx, 90);

        // draw subtitle under the title
        g2.setFont(new Font("Courier New", Font.PLAIN, 15));
        g2.setColor(TEXT_DIM);
        String sub = "Players ranked by fewest deaths on a completed run";
        g2.drawString(sub, (getWidth() - g2.getFontMetrics().stringWidth(sub)) / 2, 115);

        ArrayList<UserData> lb = users.getLeaderboard(); // get sorted list of finishers

        // size the card based on how many players are on the board
        int cardW = 700;
        int cardH = Math.max(lb.size() * 58 + 70, 200);
        int cardX = (getWidth() - cardW) / 2;
        int cardY = 140;

        // draw the card background and border
        g2.setColor(CARD_BG);
        g2.fillRoundRect(cardX, cardY, cardW, cardH, 12, 12);
        g2.setColor(new Color(90, 60, 120, 120));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(cardX, cardY, cardW, cardH, 12, 12);

        // draw the column headers
        g2.setFont(new Font("Courier New", Font.BOLD, 13));
        g2.setColor(TEXT_DIM);
        g2.drawString("RANK", cardX + 30, cardY + 38);
        g2.drawString("PLAYER", cardX + 110, cardY + 38);
        g2.drawString("BEST DEATHS", cardX + 420, cardY + 38);

        // draw a line separating the headers from the rows
        g2.setColor(new Color(90, 60, 120, 180));
        g2.drawLine(cardX + 20, cardY + 48, cardX + cardW - 20, cardY + 48);

        // show a placeholder message if nobody has finished yet
        if (lb.isEmpty()) {
            g2.setFont(new Font("Courier New", Font.PLAIN, 16));
            g2.setColor(TEXT_DIM);
            g2.drawString("No one has finished the game yet. Be the first!", cardX + 30, cardY + 100);
        }

        // draw each player row
        for (int i = 0; i < lb.size(); i++) {
            UserData u = lb.get(i);
            int rowY = cardY + 75 + i * 58; // y position for this row

            // alternate row background for readability
            if (i % 2 == 1) {
                g2.setColor(ROW_ALT);
                g2.fillRoundRect(cardX + 8, rowY - 18, cardW - 16, 48, 6, 6);
            }

            // highlight the current user's row in red
            boolean isMe = u.getUsername().equalsIgnoreCase(currentUser.getUsername());
            if (isMe) {
                g2.setColor(new Color(220, 50, 50, 40));
                g2.fillRoundRect(cardX + 8, rowY - 18, cardW - 16, 48, 6, 6);
            }

            // rank number, gold silver bronze for top 3
            g2.setFont(new Font("Courier New", Font.BOLD, 22));
            if (i == 0) g2.setColor(GOLD);
            else if (i == 1) g2.setColor(SILVER);
            else if (i == 2) g2.setColor(BRONZE);
            else g2.setColor(TEXT_DIM);
            g2.drawString("#" + (i + 1), cardX + 30, rowY + 18);

            // username, highlighted in gold if it is the current user
            g2.setFont(new Font("Courier New", Font.BOLD, 18));
            g2.setColor(isMe ? ACCENT2 : TEXT_MAIN);
            g2.drawString(u.getUsername(), cardX + 110, rowY + 18);

            // best death count, gold color for 1st place
            g2.setFont(new Font("Courier New", Font.BOLD, 18));
            g2.setColor(i == 0 ? GOLD : TEXT_MAIN);
            int best = u.getBestDeaths();
            g2.drawString(best + (best == 1 ? " death" : " deaths"), cardX + 420, rowY + 18);
        }

        // if the current user has never finished, show a note below the card
        if (currentUser.getBestDeaths() == -1) {
            g2.setFont(new Font("Courier New", Font.PLAIN, 13));
            g2.setColor(TEXT_DIM);
            String note = "You haven't finished the game yet, finish to get on the board!";
            g2.drawString(note, (getWidth() - g2.getFontMetrics().stringWidth(note)) / 2, cardY + cardH + 30);
        }
    }
}