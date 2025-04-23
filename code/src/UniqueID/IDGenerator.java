package UniqueID;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class IDGenerator implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String SAVE_FILE = "id_generator.ser";
    private static IDGenerator instance;

    private Map<String, Integer> counters = new HashMap<>();

    private IDGenerator() {}

    public static IDGenerator getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public synchronized int getNextId(String type) {
        int current = counters.getOrDefault(type, 0);
        counters.put(type, current + 1);
        save();
        return current;
    }

    private static IDGenerator load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (IDGenerator) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new IDGenerator(); // Start fresh if no file
        }
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Failed to save ID state: " + e.getMessage());
        }
    }
}
