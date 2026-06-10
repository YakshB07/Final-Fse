public class UserData {

    private String username;
    private String password;
    private int bestDeaths;
    private int currentDeaths;
    private int savedLevel;
    private double savedX;
    private double savedY;

    public UserData(String username, String password) {
        this.username = username;
        this.password = password;
        this.bestDeaths = -1;
        this.currentDeaths = 0;
        this.savedLevel = 1;
        this.savedX = 300;
        this.savedY = 416;
    }

    public void loadSavedData(int bestDeaths, int currentDeaths, int savedLevel, double savedX, double savedY) {
        this.bestDeaths = bestDeaths;
        this.currentDeaths = currentDeaths;
        this.savedLevel = savedLevel;
        this.savedX = savedX;
        this.savedY = savedY;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getBestDeaths() {
        return bestDeaths;
    }

    public int getCurrentDeaths() {
        return currentDeaths;
    }

    public int getSavedLevel() {
        return savedLevel;
    }

    public double getSavedX() {
        return savedX;
    }

    public double getSavedY() {
        return savedY;
    }

    public void addDeath() {
        currentDeaths++;
    }

    public void finishedGame() {
        if (bestDeaths == -1 || currentDeaths < bestDeaths) {
            bestDeaths = currentDeaths;
        }
        currentDeaths = 0;
        savedLevel = 1;
        savedX = 300;
        savedY = 416;
    }

    public void saveProgress(int level, double x, double y) {
        this.savedLevel = level;
        this.savedX = x;
        this.savedY = y;
    }

    public void resetRun() {
        currentDeaths = 0;
        savedLevel = 1;
        savedX = 300;
        savedY = 416;
    }

    public String bestScoreString() {
        if (bestDeaths == -1) {
            return "Not finished";
        }
        if (bestDeaths == 1) {
            return bestDeaths + " death";
        }
        return bestDeaths + " deaths";
    }

    public String toString() {
        return username + " | best=" + bestDeaths + " | currentDeaths=" + currentDeaths;
    }
}