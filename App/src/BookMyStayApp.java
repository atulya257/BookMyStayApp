import java.util.*;

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service (State Holder)
class RoomInventory {
    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

// Booking Service (Core Logic)
class BookingService {

    private Queue<Reservation> requestQueue;
    private RoomInventory inventory;

    // Track allocated room IDs
    private Set<String> allocatedRoomIds;

    // Map room type → allocated room IDs
    private Map<String, Set<String>> roomAllocations;

    private int roomIdCounter = 1;

    public BookingService(Queue<Reservation> requestQueue, RoomInventory inventory) {
        this.requestQueue = requestQueue;
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.roomAllocations = new HashMap<>();
    }

    // Process all booking requests
    public void processBookings() {

        while (!requestQueue.isEmpty()) {

            Reservation r = requestQueue.poll(); // FIFO
            String type = r.getRoomType();

            System.out.println("\nProcessing request for " + r.getGuestName());

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(type);

                // Ensure uniqueness using Set
                while (allocatedRoomIds.contains(roomId)) {
                    roomId = generateRoomId(type);
                }

                allocatedRoomIds.add(roomId);

                // Map room type → allocated IDs
                roomAllocations.putIfAbsent(type, new HashSet<>());
                roomAllocations.get(type).add(roomId);

                // Update inventory (IMPORTANT)
                inventory.reduceRoom(type);

                System.out.println("Booking CONFIRMED for " + r.getGuestName());
                System.out.println("Room Type: " + type);
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED for " + r.getGuestName() + " (No rooms available)");
            }
        }
    }

    // Generate room ID
    private String generateRoomId(String type) {
        return type.substring(0, 1).toUpperCase() + roomIdCounter++;
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Inventory setup
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);
        inventory.addRoom("Suite", 1);

        // Booking queue (FIFO)
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Arun", "Single"));
        queue.offer(new Reservation("Priya", "Suite"));
        queue.offer(new Reservation("Rahul", "Single"));
        queue.offer(new Reservation("Sneha", "Single")); // should fail

        // Booking service
        BookingService service = new BookingService(queue, inventory);

        // Process bookings
        service.processBookings();
    }
}