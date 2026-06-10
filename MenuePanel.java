import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuePanel extends JPanel {

    private Devil parentFrame;
    private UserTable users;
    private UserData currentUser;

    private Image bgImage;

    private static final Color ACCENT = new Color(220, 50, 50);
    private static final Color ACCENT2 = new Color(255, 100, 50);
    private static final Color TEXTMAIN = new Color(240, 230, 220);
    private static final Color TEXTDIM = new Color(140, 130, 150);
    private static final Color CARDBG = new Color(18, 18, 40, 220);

    public MenuePanel(Devil parentFrame, UserTable users, UserData user) {
        this.parentFrame = parentFrame;
        this.users = users;
        this.currentUser = user;

        bgImage = new ImageIcon("BgImages/menu_bg.png").getImage();

        setPreferredSize(new Dimension(1500, 750));
        setLayout(null);

        buildButtons();
    }

    private void buildButtons() {
        Font btnFont = new Font("Courier New", Font.BOLD, 20);
        Font logFont = new Font("Courier New", Font.BOLD, 14);

        int bw = 320;
        int bh = 65;
        int cx = (1500 - bw) / 2;

        boolean hasSave = currentUser.getSavedLevel() > 1 || currentUser.getSavedX() != 300;

        String playLabel;
        if (hasSave) {
            playLabel = "CONTINUE";
        } else {
            playLabel = "PLAY";
        }

        JButton playBtn = makeBtn(playLabel, btnFont, ACCENT, new Color(200, 30, 30));
        playBtn.setBounds(cx, 435, bw, bh);
        playBtn.addActionListener(e -> {
            parentFrame.startGame(currentUser);
        });
        add(playBtn);

        if (hasSave) {
            JButton newBtn = makeBtn("NEW GAME", logFont, new Color(50, 40, 80), new Color(70, 55, 110));
            newBtn.setBounds(cx, 515, bw, 45);
            newBtn.addActionListener(e -> {
                currentUser.resetRun();
                parentFrame.startGame(currentUser);
            });
            add(newBtn);
        }
        
        JButton lbBtn = makeBtn("LEADERBOARD", btnFont, new Color(30, 25, 60), new Color(55, 45, 90));
        lbBtn.setBounds(cx, hasSave ? 580 : 515, bw, bh);
        lbBtn.addActionListener(e -> {
            // parentFrame.showLeaderboard(currentUser);
        });
        add(lbBtn);

        JButton logoutBtn = makeBtn("LOGOUT", logFont, new Color(20, 18, 35), new Color(35, 30, 55));
        logoutBtn.setBounds(30, 30, 130, 38);
        logoutBtn.addActionListener(e -> {
            // parentFrame.showLogin();
        });
        add(logoutBtn);
    }

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

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);

        g2.setFont(new Font("Courier New", Font.PLAIN, 20));
        g2.setColor(TEXTMAIN);
        String welcome = "Welcome back,  " + currentUser.getUsername().toUpperCase() + "!";
        FontMetrics wfm = g2.getFontMetrics();
        g2.drawString(welcome, (getWidth() - wfm.stringWidth(welcome)) / 2, 310);

        int cardW = 400;
        int cardH = 90;
        int cardX = (getWidth() - cardW) / 2;
        int cardY = 328;

        g2.setColor(CARDBG);
        g2.fillRoundRect(cardX, cardY, cardW, cardH, 10, 10);
        g2.setColor(new Color(90, 60, 120, 120));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(cardX, cardY, cardW, cardH, 10, 10);

        g2.setFont(new Font("Courier New", Font.BOLD, 13));
        g2.setColor(TEXTDIM);
        g2.drawString("BEST RUN", cardX + 30, cardY + 28);
        g2.drawString("CURRENT DEATHS", cardX + 220, cardY + 28);

        g2.setFont(new Font("Courier New", Font.BOLD, 22));
        g2.setColor(TEXTMAIN);
        g2.drawString(currentUser.bestScoreString(), cardX + 30, cardY + 65);

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