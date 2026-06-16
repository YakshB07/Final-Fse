import java.awt.*;
import javax.swing.*;

public class Devil extends JFrame {

    private UserTable users = new UserTable();
    private JPanel currentPanel;

    public Devil() {
        super("Level Devil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        showLogin();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showLogin() {
        swapPanel(new LoginPanel(this, users));
    }

    public void showMenu(UserData user) {
        swapPanel(new MenuPanel(this, users, user));
    }

    public void showLeaderboard(UserData user) {
        swapPanel(new LeaderboardPanel(this, users, user));
    }

    public void startGame(UserData user) {
        swapPanel(new GamePanel(this, users, user));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Devil());
    }

    interface Level {
        void reset();
        void update(Guy guy);
        void draw(Graphics g, int panelWidth, int panelHeight);
        boolean isFinished();
        boolean playerDied();
    }
}