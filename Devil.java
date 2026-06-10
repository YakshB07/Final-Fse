/*
 * Yaksh Butani
 * Level Devil main file
 */
import java.awt.*;
import javax.swing.*;

public class Devil extends JFrame {

    private UserTable users = new UserTable();
    private JPanel currentPanel;

    public Devil() {
        super("Level Devil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MenuePanel menue = new MenuePanel(this, users, new UserData("default", "password"));
        add(menue);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void swapPanel(JPanel panel) {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = panel;
        add(panel);
        pack();
        revalidate();
        repaint();
        panel.requestFocusInWindow();
    }

    public void showMenu(UserData user) {
        swapPanel(new MenuePanel(this, users, user));
    }

    public void startGame(UserData user) {
        // swapPanel(new GamePanel(this, users, user));
    }

    public static void main(String[] args) {
        new Devil();
    }
}

interface Level {
    void reset();
    void update(Guy guy);
    void draw(Graphics g, int panelWidth, int panelHeight);
    boolean isFinished();
}
