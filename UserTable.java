import java.io.*;
import java.util.ArrayList;

public class UserTable {

    private class Node {
        String key;
        UserData value;
        Node next;

        Node(String key, UserData value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final String SAVE_FILE = "users.txt";
    private Node[] buckets;
    private int size;

    public UserTable() {
        buckets = new Node[DEFAULT_CAPACITY];
        size = 0;
        loadFromFile();
    }

    private int hash(String key) {
        int h = 0;
        for (int i = 0; i < key.length(); i++) {
            h = (h * 31 + key.charAt(i)) % buckets.length;
        }
        return Math.abs(h);
    }

    public void put(String username, UserData data) {
        String key = username.toLowerCase();
        int idx = hash(key);
        Node cur = buckets[idx];

        while (cur != null) {
            if (cur.key.equals(key)) {
                cur.value = data;
                saveToFile();
                return;
            }
            cur = cur.next;
        }

        Node newNode = new Node(key, data);
        newNode.next = buckets[idx];
        buckets[idx] = newNode;
        size++;
        saveToFile();
    }

    public UserData get(String username) {
        String key = username.toLowerCase();
        int idx = hash(key);
        Node cur = buckets[idx];

        while (cur != null) {
            if (cur.key.equals(key)) {
                return cur.value;
            }
            cur = cur.next;
        }
        return null;
    }

    public boolean contains(String username) {
        return get(username) != null;
    }

    public int size() {
        return size;
    }

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

    public ArrayList<UserData> getLeaderboard() {
        ArrayList<UserData> finished = new ArrayList<>();

        for (UserData u : allUsers()) {
            if (u.getBestDeaths() >= 0) {
                finished.add(u);
            }
        }

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

    private void loadFromFile() {
        File f = new File(SAVE_FILE);
        if (!f.exists()) {
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String username = line;
                String password = reader.readLine();
                int bestDeaths = Integer.parseInt(reader.readLine());
                int currentDeaths = Integer.parseInt(reader.readLine());
                int savedLevel = Integer.parseInt(reader.readLine());
                double savedX = Double.parseDouble(reader.readLine());
                double savedY = Double.parseDouble(reader.readLine());

                UserData u = new UserData(username, password);
                u.loadSavedData(bestDeaths, currentDeaths, savedLevel, savedX, savedY);

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