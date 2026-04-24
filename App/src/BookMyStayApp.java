import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }
}

// Inventory class (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public Map<String, Integer> getAvailabilityMap() {
        return availability;
    }

    public void setAvailabilityMap(Map<String, Integer> map) {
        this.availability = map;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "booking_data.ser";

    // Save data
    public static void save(RoomInventory inventory, List<Reservation> history) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(inventory);
            oos.writeObject(history);

            System.out.println("\nData successfully saved to file.");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load data
    public static Object[] load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("\nNo previous data found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            RoomInventory inventory = (RoomInventory) ois.readObject();
            List<Reservation> history = (List<Reservation>) ois.readObject();

            System.out.println("\nData successfully loaded from file.");

            return new Object[]{inventory, history};

        } catch (Exception e) {
            System.out.println("Error loading data. Starting fresh.");
            return null;
        }
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        RoomInventory inventory;
        List<Reservation> history;

        // Try loading previous data
        Object[] data = PersistenceService.load();

        if (data != null) {
            inventory = (RoomInventory) data[0];
            history = (List<Reservation>) data[1];
        } else {
            // Initialize fresh data
            inventory = new RoomInventory();
            inventory.addRoom("Single", 2);
            inventory.addRoom("Suite", 1);

            history = new ArrayList<>();
        }

        // Simulate booking
        Reservation r1 = new Reservation("Arun", "Single", "S1");
        history.add(r1);

        System.out.println("\nCurrent Booking History:");
        for (Reservation r : history) {
            System.out.println(r.getGuestName() + " | " +
                    r.getRoomType() + " | " + r.getRoomId());
        }

        // Save before shutdown
        PersistenceService.save(inventory, history);

        System.out.println("\nSystem shutting down...");
    }
}