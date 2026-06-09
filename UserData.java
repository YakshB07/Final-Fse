public class UserData {
    private String username;
    private String password;
    private int bestDeaths;
    private int currentDeaths;
    private int savedLevel;
    private int savedX;
    private int savedY;

    public UserData(String username, String password) {
        this.username = username;
        this.password = password;
        this.bestDeaths = -1;
        this.currentDeaths = 0;
        this.savedLevel = 1;
        this.savedX = 300;
        this.savedY = 416;
    }

    public void LoadData(int bestDeaths, int savedLevel, int savedX, int savedY) {
        this.bestDeaths = bestDeaths;
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
    public int getSavedX() {
        return savedX;
    }
    public int getSavedY() {
        return savedY;
    }
    public void addDeath() {
        currentDeaths++;
    }

    
}
