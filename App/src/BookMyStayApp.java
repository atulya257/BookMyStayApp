import java.util.*;

// Room class (Domain Model)
class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }
}

// Inventory class (State Holder)
class RoomInventory {
    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        availabilityMap.put(type, count);
    }

    // Read-only access
    public int getAvailability(String type) {
        return availabilityMap.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return availabilityMap.keySet();
    }
}

// Search Service (Read-only logic)
class SearchService {

    public static void searchAvailableRooms(RoomInventory inventory, Map<String, Room> roomDetails) {
        System.out.println("Available Rooms:\n");

        for (String type : inventory.getAllRoomTypes()) {
            int count = inventory.getAvailability(type);

            // Validation: only show available rooms
            if (count > 0) {
                Room room = roomDetails.get(type);

                System.out.println("Room Type: " + room.getType());
                System.out.println("Price: ₹" + room.getPrice());
                System.out.println("Amenities: " + room.getAmenities());
                System.out.println("Available Count: " + count);
                System.out.println("-----------------------------");
            }
        }
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        // Room details (Domain objects)
        Map<String, Room> roomDetails = new HashMap<>();
        roomDetails.put("Single", new Room("Single", 2000, "WiFi, AC"));
        roomDetails.put("Double", new Room("Double", 3500, "WiFi, AC, TV"));
        roomDetails.put("Suite", new Room("Suite", 5000, "WiFi, AC, TV, Mini Bar"));

        // Guest initiates search
        SearchService.searchAvailableRooms(inventory, roomDetails);
    }
}