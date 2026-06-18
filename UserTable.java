/*
 * UserTable.java
 * Authors: Yaksh Butani, Arshvir Ghotra
 *
 * A custom hashtable that maps usernames to UserData objects.
 * We built this from scratch instead of using Java's built in HashMap
 * because the project requires us to implement our own data structure.
 *
 * How it works: there are 16 buckets. When you store a username, the hash function
 * turns it into a number from 0 to 15 and puts the data in that bucket.
 * If two usernames land in the same bucket (a collision), they get chained together
 * in a linked list inside that bucket.
 *
 * It also saves and loads all accounts to a text file so data survives between sessions.
 */

import java.io.*;
import java.util.ArrayList;

public class UserTable {

    /* Node is one entry in the linked list inside a bucket.
     * Each node holds one username, one UserData, and a pointer to the next node in the chain.
     */
    private class Node {
        String key;     // username stored in lowercase so login is not case sensitive
        UserData value; // the player data for this account
        Node next;      // next node in the chain if there was a collision

        Node(String key, UserData value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private static final int DEFAULT_CAPACITY = 16; // number of buckets in the table
    private static final String SAVE_FILE = "users.txt"; // file name where all accounts get saved
    private Node[] buckets; // array of buckets, each one is the head of a linked list
    private int size;       // total number of accounts currently stored

    public UserTable() {
        buckets = new Node[DEFAULT_CAPACITY];
        size = 0;
        loadFromFile(); // load any accounts that were saved from a previous session
    }

    /* hash() converts a username string into a bucket index between 0 and 15.
     * It goes through each character in the string, multiplies the running total by 31,
     * and adds the character's ASCII value. The % keeps it within bounds.
     * We use 31 because it is prime and produces a good spread across the buckets.
     * Math.abs makes sure the result is never negative since % can return negative in Java.
     */
    private int hash(String key) {
        int h = 0;
        for (int i = 0; i < key.length(); i++) {
            h = (h * 31 + key.charAt(i)) % buckets.length;
        }
        return Math.abs(h);
    }

    /* put() adds a new account or updates an existing one.
     * It first walks the chain in the bucket to see if the username already exists.
     * If found, it just replaces the value. If not, it adds a new node at the front.
     * It always calls saveToFile() after any change so the file stays up to date.
     */
    public void put(String username, UserData data) {
        String key = username.toLowerCase(); // always store in lowercase
        int idx = hash(key);
        Node cur = buckets[idx];

        // walk the chain to see if this username already exists
        while (cur != null) {
            if (cur.key.equals(key)) {
                cur.value = data; // found it, just update the value
                saveToFile();
                return;
            }
            cur = cur.next;
        }

        // username not found, so add a new node at the front of the chain
        Node newNode = new Node(key, data);
        newNode.next = buckets[idx];
        buckets[idx] = newNode;
        size++;
        saveToFile();
    }

    /* get() looks up a username and returns their UserData.
     * Returns null if the username does not exist in the table.
     */
    public UserData get(String username) {
        String key = username.toLowerCase();
        int idx = hash(key);
        Node cur = buckets[idx];

        // walk the chain in this bucket looking for the username
        while (cur != null) {
            if (cur.key.equals(key)) {
                return cur.value;
            }
            cur = cur.next;
        }
        return null; // not found
    }

    /* contains() is a quick check for whether a username exists, used during registration. */
    public boolean contains(String username) {
        return get(username) != null;
    }

    public int size() {
        return size;
    }

    /* allUsers() collects every UserData from every bucket into one flat list.
     * It walks each bucket and follows the chain to get all entries.
     * Used by getLeaderboard() and saveToFile().
     */
    public ArrayList<UserData> allUsers() {
        ArrayList<UserData> list = new ArrayList<>();
        for (Node bucket : buckets) {
            Node cur = bucket;
            while (cur != null) {
                list.add(cur.value);
                cur = cur.next;
            }
        }
        return list;
    }

    /* getLeaderboard() returns only the players who have actually beaten the game,
     * sorted from fewest deaths to most using bubble sort.
     * Players with bestDeaths == -1 have never finished so they are excluded.
     */
    public ArrayList<UserData> getLeaderboard() {
        ArrayList<UserData> finished = new ArrayList<>();

        // only include players who have finished the game
        for (UserData u : allUsers()) {
            if (u.getBestDeaths() >= 0) {
                finished.add(u);
            }
        }

        // bubble sort by bestDeaths from lowest to highest
        for (int i = 0; i < finished.size() - 1; i++) {
            for (int j = 0; j < finished.size() - 1 - i; j++) {
                if (finished.get(j).getBestDeaths() > finished.get(j + 1).getBestDeaths()) {
                    UserData temp = finished.get(j);
                    finished.set(j, finished.get(j + 1));
                    finished.set(j + 1, temp);
                }
            }
        }

        return finished;
    }

    /* saveToFile() writes every account to users.txt, 7 lines per user in this order:
     * username, password, bestDeaths, currentDeaths, savedLevel, savedX, savedY.
     * This gets called automatically every time put() is used.
     */
    private void saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE));
            for (UserData u : allUsers()) {
                writer.println(u.getUsername());
                writer.println(u.getPassword());
                writer.println(u.getBestDeaths());
                writer.println(u.getCurrentDeaths());
                writer.println(u.getSavedLevel());
                writer.println(u.getSavedX());
                writer.println(u.getSavedY());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not save users: " + e);
        }
    }

    /* loadFromFile() reads users.txt and rebuilds the hashtable from the saved data.
     * Called once in the constructor when the game starts up.
     * If the file does not exist yet it just skips loading, which is fine for first launch.
     * We insert directly into the buckets instead of calling put() to avoid re-saving the file.
     */
    private void loadFromFile() {
        File f = new File(SAVE_FILE);
        if (!f.exists()) {
            return; // no save file yet, nothing to load
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                // read 7 lines at a time, one per field
                String username = line;
                String password = reader.readLine();
                int bestDeaths = Integer.parseInt(reader.readLine());
                int currentDeaths = Integer.parseInt(reader.readLine());
                int savedLevel = Integer.parseInt(reader.readLine());
                double savedX = Double.parseDouble(reader.readLine());
                double savedY = Double.parseDouble(reader.readLine());

                // create the object and restore the saved values
                UserData u = new UserData(username, password);
                u.loadSavedData(bestDeaths, currentDeaths, savedLevel, savedX, savedY);

                // insert directly into the bucket without calling put() so we dont re-save
                String key = username.toLowerCase();
                int idx = hash(key);
                Node newNode = new Node(key, u);
                newNode.next = buckets[idx];
                buckets[idx] = newNode;
                size++;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Could not load users: " + e);
        }
    }
}