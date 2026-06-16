import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class LeaderboardPanel extends JPanel {

    private Devil parentFrame;
    private UserTable users;
    private UserData currentUser;

    private ArrayList<float[]> particles = new ArrayList<>();
    private Random rng = new Random();
    private Timer animTimer;

    private static final Color BG_TOP = new Color(10, 10, 20);
    private static final Color BG_BOT = new Color(5, 5, 35);
    private static final Color ACCENT = new Color(220, 50, 50);
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
        setLayout(null);

        Font logFont = new Font("Courier New", Font.BOLD, 14);
        JButton backBtn = makeBtn("BACK", logFont, new Color(20, 18, 35), new Color(35, 30, 55));
        backBtn.setBounds(30, 30, 120, 38);
        backBtn.addActionListener(e -> {
            animTimer.stop();
            parentFrame.showMenu(currentUser);
        });
        add(backBtn);

        initParticles();
        animTimer = new Timer(16, e -> {
            updateParticles();
            repaint();
        });
        animTimer.start();
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

    private void initParticles() {
        for (int i = 0; i < 50; i++) {
            particles.add(newParticle());
        }
    }

    private float[] newParticle() {
        float[] p = new float[6];
        p[0] = rng.nextFloat() * 1500;
        p[1] = rng.nextFloat() * 750;
        p[2] = (rng.nextFloat() - 0.5f) * 0.45f;
        p[3] = -rng.nextFloat() * 0.3f - 0.1f;
        p[4] = rng.nextFloat() * 3 + 1;
        p[5] = rng.nextFloat() * 0.4f + 0.1f;
        return p;
    }

    private void updateParticles() {
        for (int i = 0; i < particles.size(); i++) {
            float[] p = particles.get(i);
            p[0] += p[2];
            p[1] += p[3];
            if (p[1] < -5 || p[0] < -5 || p[0] > 1505) {
                particles.set(i, newParticle());
                particles.get(i)[1] = 755;
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, 750, BG_BOT));
        g2.fillRect(0, 0, getWidth(), getHeight());

        for (float[] p : particles) {
            g2.setColor(new Color(220, 80, 80, (int)(p[5] * 150)));
            g2.fillOval((int)p[0], (int)p[1], (int)p[4], (int)p[4]);
        }

        g2.setFont(new Font("Courier New", Font.BOLD, 46));
        String title = "HALL OF FAME";
        FontMetrics fm = g2.getFontMetrics();
        int tx = (getWidth() - fm.stringWidth(title)) / 2;

        g2.setColor(new Color(220, 50, 50, 35));
        for (int d = 6; d >= 1; d--) {
            g2.drawString(title, tx, 90 + d);
        }
        g2.setColor(ACCENT2);
        g2.drawString(title, tx, 90);

        g2.setFont(new Font("Courier New", Font.PLAIN, 15));
        g2.setColor(TEXT_DIM);
        String sub = "Players ranked by fewest deaths on a completed run";
        g2.drawString(sub, (getWidth() - g2.getFontMetrics().stringWidth(sub)) / 2, 115);

        ArrayList<UserData> lb = users.getLeaderboard();

        int cardW = 700;
        int cardH = Math.max(lb.size() * 58 + 70, 200);
        int cardX = (getWidth() - cardW) / 2;
        int cardY = 140;

        g2.setColor(CARD_BG);
        g2.fillRoundRect(cardX, cardY, cardW, cardH, 12, 12);
        g2.setColor(new Color(90, 60, 120, 120));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(cardX, cardY, cardW, cardH, 12, 12);

        g2.setFont(new Font("Courier New", Font.BOLD, 13));
        g2.setColor(TEXT_DIM);
        g2.drawString("RANK", cardX + 30, cardY + 38);
        g2.drawString("PLAYER", cardX + 110, cardY + 38);
        g2.drawString("BEST DEATHS", cardX + 420, cardY + 38);

        g2.setColor(new Color(90, 60, 120, 180));
        g2.drawLine(cardX + 20, cardY + 48, cardX + cardW - 20, cardY + 48);

        if (lb.isEmpty()) {
            g2.setFont(new Font("Courier New", Font.PLAIN, 16));
            g2.setColor(TEXT_DIM);
            String msg = "No one has finished the game yet. Be the first!";
            g2.drawString(msg, cardX + 30, cardY + 100);
        }

        for (int i = 0; i < lb.size(); i++) {
            UserData u = lb.get(i);
            int rowY = cardY + 55 + i * 58;

            if (i % 2 == 1) {
                g2.setColor(ROW_ALT);
                g2.fillRoundRect(cardX + 8, rowY - 18, cardW - 16, 48, 6, 6);
            }

            boolean isMe = u.getUsername().equalsIgnoreCase(currentUser.getUsername());
            if (isMe) {
                g2.setColor(new Color(220, 50, 50, 40));
                g2.fillRoundRect(cardX + 8, rowY - 18, cardW - 16, 48, 6, 6);
            }

            g2.setFont(new Font("Courier New", Font.BOLD, 22));
            if (i == 0) {
                g2.setColor(GOLD);
                g2.drawString("#1", cardX + 30, rowY + 18);
            } else if (i == 1) {
                g2.setColor(SILVER);
                g2.drawString("#2", cardX + 30, rowY + 18);
            } else if (i == 2) {
                g2.setColor(BRONZE);
                g2.drawString("#3", cardX + 30, rowY + 18);
            } else {
                g2.setColor(TEXT_DIM);
                g2.drawString("#" + (i + 1), cardX + 30, rowY + 18);
            }

            g2.setFont(new Font("Courier New", Font.BOLD, 18));
            if (isMe) {
                g2.setColor(ACCENT2);
                g2.drawString(u.getUsername() + "  <- you", cardX + 110, rowY + 18);
            } else {
                g2.setColor(TEXT_MAIN);
                g2.drawString(u.getUsername(), cardX + 110, rowY + 18);
            }

            g2.setFont(new Font("Courier New", Font.BOLD, 18));
            if (i == 0) {
                g2.setColor(GOLD);
            } else {
                g2.setColor(TEXT_MAIN);
            }
            String deathStr;
            if (u.getBestDeaths() == 1) {
                deathStr = u.getBestDeaths() + " death";
            } else {
                deathStr = u.getBestDeaths() + " deaths";
            }
            g2.drawString(deathStr, cardX + 420, rowY + 18);
        }

        if (currentUser.getBestDeaths() == -1) {
            g2.setFont(new Font("Courier New", Font.PLAIN, 13));
            g2.setColor(TEXT_DIM);
            String note = "You haven't finished the game yet, finish to get on the board!";
            g2.drawString(note, (getWidth() - g2.getFontMetrics().stringWidth(note)) / 2, cardY + cardH + 30);
        }
    }
}