/*
 * UserData.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * Stores all the information for one player account.
 * Every time a player registers, one of these objects gets created and saved into the hashtable.
 * It keeps track of their password, how many times they died, their best run, and where they left off.
 */

public class UserData {

    private String username;
    private String password;
    private int bestDeaths;    // lowest death count from a finished run, starts at -1 which means never finished
    private int currentDeaths; // how many times the player has died in their current run
    private int savedLevel;    // which level they were on when they quit
    private double savedX;     // x position they were at when they quit
    private double savedY;     // y position they were at when they quit
    private int lastRunDeaths; // death count from the most recently completed run, used by the win screen

    /* Constructor sets up a brand new account with default values.
     * bestDeaths starts at -1 to signal they have never beaten the game.
     * savedLevel starts at 1 and position starts at the beginning of level 1.
     */
    public UserData(String username, String password) {
        this.username = username;
        this.password = password;
        this.bestDeaths = -1;
        this.currentDeaths = 0;
        this.savedLevel = 1;
        this.savedX = 300;
        this.savedY = 416;
    }

    /* loadSavedData() is only used when reading accounts back from the save file.
     * The constructor always resets everything to defaults, so after creating a blank object
     * from the file we call this to put the real values back in.
     */
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

    /* addDeath() adds one to the death counter for the current run.
     * Called by GamePanel every time a level reports that the player died.
     */
    public void addDeath() {
        currentDeaths++;
    }

    /* finishedGame() is called when the player beats the last level.
     * It checks if this run was better than their previous best and updates it if so.
     * Then it resets their progress so their next run starts fresh from level 1.
     */
    public void finishedGame() {
        lastRunDeaths = currentDeaths; // save it before resetting

        if (bestDeaths == -1 || currentDeaths < bestDeaths) {
            bestDeaths = currentDeaths;
        }

        currentDeaths = 0;
        savedLevel = 1;
        savedX = 300;
        savedY = 416;
    }

    public int getLastRunDeaths() {
        return lastRunDeaths;
    }

    /* saveProgress() stores where the player currently is so they can continue later.
     * Called when the player hits quit in the pause menu.
     */
    public void saveProgress(int level, double x, double y) {
        this.savedLevel = level;
        this.savedX = x;
        this.savedY = y;
    }

    /* resetRun() wipes the current run completely so the player starts over from level 1.
     * Called when the player clicks New Game on the menu.
     */
    public void resetRun() {
        currentDeaths = 0;
        savedLevel = 1;
        savedX = 300;
        savedY = 416;
    }

    /* bestScoreString() returns a nicely formatted string of their best run for the menu screen. */
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