/*
 * LoginPanel.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * This panel handles the login and registration screen. It has two tabs,
 * LOGIN and REGISTER, that the player can switch between. On the login side
 * it checks the username and password against the user table. On the register
 * side it validates the input and creates a new account. Either way it waits
 * a short moment then sends the player to the main menu. There is a background
 * image behind a semi transparent card that holds all the input fields.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginPanel extends JPanel implements ActionListener {

    private Devil parentFrame; // reference to the main frame for switching screens
    private UserTable users;   // the table of all user accounts used for login and registration checks

    private JTextField usernameField;     // text field where the player types their username
    private JPasswordField passwordField; // password field where the player types their password
    private JLabel statusLabel;           // shows error or success messages below the button
    private boolean showLogin = true;     // true if the login tab is active, false if register tab is active

    private Image bgImage; // the background image drawn behind the card

    // colors used throughout the panel
    private static final Color ACCENT = new Color(220, 50, 50);         // red used for errors and the action button
    private static final Color ACCENT2 = new Color(255, 100, 50);       // lighter red for button hover
    private static final Color TEXT_MAIN = new Color(240, 230, 220);    // main bright text color
    private static final Color TEXT_DIM = new Color(140, 130, 150);     // dimmer color for labels above the fields
    private static final Color FIELD_BG = new Color(30, 25, 50);        // dark background for input fields
    private static final Color FIELD_BORDER = new Color(80, 60, 100);   // border color for input fields
    private static final Color TAB_IDLE = new Color(40, 35, 60);        // background color for the tab buttons
    private static final Color CARD_BG = new Color(18, 18, 40, 230);    // semi transparent card background

    public LoginPanel(Devil parentFrame, UserTable users) {
        this.parentFrame = parentFrame;
        this.users = users;

        bgImage = new ImageIcon("BgImages/login_bg.png").getImage();

        setPreferredSize(new Dimension(1500, 750));
        setLayout(null);

        buildCard();
    }

    /*
     * Builds all the UI components on the login card including the tabs,
     * username and password fields, the action button, the status label,
     * and the enter key shortcut.
     */
    private void buildCard() {
        // card dimensions and position
        int cw = 400;
        int ch = 460;
        int cx = (1500 - cw) / 2;
        int cy = 225;

        Font labelFont = new Font("Courier New", Font.BOLD, 13);
        Font fieldFont = new Font("Courier New", Font.PLAIN, 15);
        Font btnFont = new Font("Courier New", Font.BOLD, 16);
        Font tabFont = new Font("Courier New", Font.BOLD, 14);
        Font statusFont = new Font("Courier New", Font.BOLD, 12);

        // create the two tab buttons for switching between login and register
        JButton loginTab = makeTabBtn("LOGIN", tabFont);
        JButton registerTab = makeTabBtn("REGISTER", tabFont);

        loginTab.setBounds(cx, cy + 70, cw / 2, 38);
        registerTab.setBounds(cx + cw / 2, cy + 70, cw / 2, 38);

        // switching tabs just sets the flag and repaints so the underline moves
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

        // the main action button, clicking it runs handleAction
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

        // status label shows errors in red or success in green below the button
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(statusFont);
        statusLabel.setForeground(ACCENT);
        statusLabel.setBounds(cx + 20, cy + 378, 360, 24);
        add(statusLabel);

        // lets the player press enter instead of clicking the button
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

    /*
     * Creates and returns a styled tab button used for the LOGIN and REGISTER tabs.
     */
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

    /*
     * Creates and returns a styled label used above the input fields.
     */
    private JLabel makeLabel(String text, Font f) {
        JLabel l = new JLabel(text);
        l.setFont(f);
        l.setForeground(TEXT_DIM);
        return l;
    }

    /*
     * Applies consistent styling to a text field or password field.
     * Sets the font, colors, and border to match the rest of the card.
     */
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

    /*
     * Clears any message currently shown in the status label.
     * Called when the player switches between tabs.
     */
    private void clearStatus() {
        statusLabel.setText("");
    }

    /*
     * Handles the ENTER button click and enter key press.
     * If the login tab is active it checks the username and password
     * against the user table and logs in if they match.
     * If the register tab is active it validates the input and creates
     * a new account if the username is not already taken.
     * On success it waits 700ms then goes to the menu screen.
     */
    private void handleAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // make sure neither field is empty before doing anything
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setForeground(ACCENT);
            statusLabel.setText("Please fill in both fields.");
            return;
        }

        if (showLogin) {
            // login flow: look up the user and check the password
            UserData u = users.get(username);
            if (u == null) {
                statusLabel.setForeground(ACCENT);
                statusLabel.setText("Username not found.");
            } else if (!u.getPassword().equals(password)) {
                statusLabel.setForeground(ACCENT);
                statusLabel.setText("Wrong password.");
            } else {
                // correct login, show success then go to the menu after a short delay
                statusLabel.setForeground(new Color(80, 220, 100));
                statusLabel.setText("Welcome back, " + u.getUsername() + "!");
                Timer delay = new Timer(700, ev -> parentFrame.showMenu(u));
                delay.setRepeats(false);
                delay.start();
            }
        } else {
            // register flow: validate username length, password length, and uniqueness
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
            // all checks passed, create the account and go to the menu after a short delay
            UserData newUser = new UserData(username, password);
            users.put(username, newUser);
            statusLabel.setForeground(new Color(80, 220, 100));
            statusLabel.setText("Account created! Logging in...");
            Timer delay = new Timer(700, ev -> parentFrame.showMenu(newUser));
            delay.setRepeats(false);
            delay.start();
        }
    }

    /*
     * Draws the background image, the semi transparent card, the tab underline
     * indicator for whichever tab is active, and the footer hint text at the bottom.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);

        int cw = 400;
        int ch = 460;
        int cx = (getWidth() - cw) / 2;
        int cy = 225;

        // draw the card background and border
        g2.setColor(CARD_BG);
        g2.fillRoundRect(cx - 10, cy - 10, cw + 20, ch + 20, 12, 12);
        g2.setColor(new Color(90, 60, 120, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(cx - 10, cy - 10, cw + 20, ch + 20, 12, 12);

        // draw the red underline under whichever tab is currently active
        int tabW = cw / 2;
        int tabY = cy + 70;
        g2.setColor(ACCENT);
        if (showLogin) {
            g2.fillRect(cx, tabY + 35, tabW, 3);
        } else {
            g2.fillRect(cx + tabW, tabY + 35, tabW, 3);
        }

        // draw a hint at the bottom telling the player how to switch tabs
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

    // required by ActionListener but we handle everything through lambdas so this stays empty
    public void actionPerformed(java.awt.event.ActionEvent e) {
    }
}