import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginPanel extends JPanel implements ActionListener {

    private Devil parentFrame;
    private UserTable users;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private boolean showLogin = true;

    private Image bgImage;

    private static final Color ACCENT = new Color(220, 50, 50);
    private static final Color ACCENT2 = new Color(255, 100, 50);
    private static final Color TEXT_MAIN = new Color(240, 230, 220);
    private static final Color TEXT_DIM = new Color(140, 130, 150);
    private static final Color FIELD_BG = new Color(30, 25, 50);
    private static final Color FIELD_BORDER = new Color(80, 60, 100);
    private static final Color TAB_IDLE = new Color(40, 35, 60);
    private static final Color CARD_BG = new Color(18, 18, 40, 230);

    public LoginPanel(Devil parentFrame, UserTable users) {
        this.parentFrame = parentFrame;
        this.users = users;

        bgImage = new ImageIcon("BgImages/login_bg.png").getImage();

        setPreferredSize(new Dimension(1500, 750));
        setLayout(null);

        buildCard();
    }

    private void buildCard() {
        int cw = 400;
        int ch = 460;
        int cx = (1500 - cw) / 2;
        int cy = 225;

        Font labelFont = new Font("Courier New", Font.BOLD, 13);
        Font fieldFont = new Font("Courier New", Font.PLAIN, 15);
        Font btnFont = new Font("Courier New", Font.BOLD, 16);
        Font tabFont = new Font("Courier New", Font.BOLD, 14);
        Font statusFont = new Font("Courier New", Font.BOLD, 12);

        JButton loginTab = makeTabBtn("LOGIN", tabFont);
        JButton registerTab = makeTabBtn("REGISTER", tabFont);

        loginTab.setBounds(cx, cy + 70, cw / 2, 38);
        registerTab.setBounds(cx + cw / 2, cy + 70, cw / 2, 38);

        loginTab.addActionListener(e -> {
            showLogin = true;
            clearStatus();
            repaint();
        });
        registerTab.addActionListener(e -> {
            showLogin = false;
            clearStatus();
            repaint();
        });

        add(loginTab);
        add(registerTab);

        JLabel userLabel = makeLabel("USERNAME", labelFont);
        userLabel.setBounds(cx + 30, cy + 130, 340, 22);
        add(userLabel);

        usernameField = new JTextField();
        styleField(usernameField, fieldFont);
        usernameField.setBounds(cx + 30, cy + 154, 340, 42);
        add(usernameField);

        JLabel passLabel = makeLabel("PASSWORD", labelFont);
        passLabel.setBounds(cx + 30, cy + 216, 340, 22);
        add(passLabel);

        passwordField = new JPasswordField();
        styleField(passwordField, fieldFont);
        passwordField.setBounds(cx + 30, cy + 240, 340, 42);
        add(passwordField);

        JButton actionBtn = new JButton("ENTER");
        actionBtn.setBounds(cx + 30, cy + 315, 340, 50);
        actionBtn.setFont(btnFont);
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setBackground(ACCENT);
        actionBtn.setBorder(BorderFactory.createEmptyBorder());
        actionBtn.setFocusPainted(false);
        actionBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        actionBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                actionBtn.setBackground(ACCENT2);
            }
            public void mouseExited(MouseEvent e) {
                actionBtn.setBackground(ACCENT);
            }
        });
        actionBtn.addActionListener(e -> handleAction());
        add(actionBtn);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(statusFont);
        statusLabel.setForeground(ACCENT);
        statusLabel.setBounds(cx + 20, cy + 378, 360, 24);
        add(statusLabel);

        KeyAdapter enterKey = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleAction();
                }
            }
        };
        usernameField.addKeyListener(enterKey);
        passwordField.addKeyListener(enterKey);
    }

    private JButton makeTabBtn(String text, Font f) {
        JButton b = new JButton(text);
        b.setFont(f);
        b.setForeground(TEXT_MAIN);
        b.setBackground(TAB_IDLE);
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JLabel makeLabel(String text, Font f) {
        JLabel l = new JLabel(text);
        l.setFont(f);
        l.setForeground(TEXT_DIM);
        return l;
    }

    private void styleField(JTextField field, Font f) {
        field.setFont(f);
        field.setForeground(TEXT_MAIN);
        field.setBackground(FIELD_BG);
        field.setCaretColor(TEXT_MAIN);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(FIELD_BORDER, 1),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
    }

    private void clearStatus() {
        statusLabel.setText("");
    }

    private void handleAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setForeground(ACCENT);
            statusLabel.setText("Please fill in both fields.");
            return;
        }

        if (showLogin) {
            UserData u = users.get(username);
            if (u == null) {
                statusLabel.setForeground(ACCENT);
                statusLabel.setText("Username not found.");
            } else if (!u.getPassword().equals(password)) {
                statusLabel.setForeground(ACCENT);
                statusLabel.setText("Wrong password.");
            } else {
                statusLabel.setForeground(new Color(80, 220, 100));
                statusLabel.setText("Welcome back, " + u.getUsername() + "!");
                Timer delay = new Timer(700, ev -> parentFrame.showMenu(u));
                delay.setRepeats(false);
                delay.start();
            }
        } else {
            if (username.length() < 3) {
                statusLabel.setForeground(ACCENT);
                statusLabel.setText("Username must be 3+ characters.");
                return;
            }
            if (password.length() < 4) {
                statusLabel.setForeground(ACCENT);
                statusLabel.setText("Password must be 4+ characters.");
                return;
            }
            if (users.contains(username)) {
                statusLabel.setForeground(ACCENT);
                statusLabel.setText("That username is taken.");
                return;
            }
            UserData newUser = new UserData(username, password);
            users.put(username, newUser);
            statusLabel.setForeground(new Color(80, 220, 100));
            statusLabel.setText("Account created! Logging in...");
            Timer delay = new Timer(700, ev -> parentFrame.showMenu(newUser));
            delay.setRepeats(false);
            delay.start();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);

        int cw = 400;
        int ch = 460;
        int cx = (getWidth() - cw) / 2;
        int cy = 225;

        g2.setColor(CARD_BG);
        g2.fillRoundRect(cx - 10, cy - 10, cw + 20, ch + 20, 12, 12);

        g2.setColor(new Color(90, 60, 120, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(cx - 10, cy - 10, cw + 20, ch + 20, 12, 12);

        int tabW = cw / 2;
        int tabY = cy + 70;
        g2.setColor(ACCENT);
        if (showLogin) {
            g2.fillRect(cx, tabY + 35, tabW, 3);
        } else {
            g2.fillRect(cx + tabW, tabY + 35, tabW, 3);
        }

        g2.setFont(new Font("Courier New", Font.PLAIN, 11));
        g2.setColor(new Color(140, 130, 150));
        String footer;
        if (showLogin) {
            footer = "New here? Click REGISTER above.";
        } else {
            footer = "Already have an account? Click LOGIN.";
        }
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(footer, (getWidth() - fm.stringWidth(footer)) / 2, cy + ch + 30);
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
    }
}