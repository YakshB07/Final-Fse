import java.util.ArrayList;
import java.io.*;

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

    public int size(){
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
}