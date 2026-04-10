import java.util.*;

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isActive;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
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

    public boolean isActive() {
        return isActive;
    }

    public void cancel() {
        isActive = false;
    }
}

// Inventory Service
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

    public void increaseRoom(String type) {
        availability.put(type, availability.getOrDefault(type, 0) + 1);
    }
}

// Cancellation Service
class CancellationService {

    private Map<String, Reservation> reservationMap; // roomId → Reservation
    private Stack<String> rollbackStack; // track released room IDs
    private RoomInventory inventory;

    public CancellationService(RoomInventory inventory) {
        this.inventory = inventory;
        this.reservationMap = new HashMap<>();
        this.rollbackStack = new Stack<>();
    }

    // Add confirmed reservation (simulate from previous use case)
    public void addReservation(Reservation r) {
        reservationMap.put(r.getRoomId(), r);
    }

    // Cancel booking
    public void cancelBooking(String roomId) {

        System.out.println("\nAttempting cancellation for Room ID: " + roomId);

        // Validation
        if (!reservationMap.containsKey(roomId)) {
            System.out.println("Cancellation FAILED: Reservation does not exist.");
            return;
        }

        Reservation r = reservationMap.get(roomId);

        if (!r.isActive()) {
            System.out.println("Cancellation FAILED: Booking already cancelled.");
            return;
        }

        // Rollback logic
        rollbackStack.push(roomId); // Track rollback

        // Restore inventory
        inventory.increaseRoom(r.getRoomType());

        // Mark reservation inactive
        r.cancel();

        System.out.println("Cancellation SUCCESSFUL for " + r.getGuestName());
        System.out.println("Room Type Restored: " + r.getRoomType());
    }

    // View rollback history
    public void showRollbackHistory() {
        System.out.println("\nRollback Stack (Recent First): " + rollbackStack);
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Setup inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single", 0); // already booked
        inventory.addRoom("Suite", 0);

        // Cancellation service
        CancellationService service = new CancellationService(inventory);

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("Arun", "Single", "S1");
        Reservation r2 = new Reservation("Priya", "Suite", "S2");

        service.addReservation(r1);
        service.addReservation(r2);

        // Perform cancellations
        service.cancelBooking("S1"); // success
        service.cancelBooking("S1"); // already cancelled
        service.cancelBooking("S3"); // invalid

        // Show rollback stack
        service.showRollbackHistory();

        // Check updated inventory
        System.out.println("\nUpdated Inventory:");
        System.out.println("Single: " + inventory.getAvailability("Single"));
        System.out.println("Suite: " + inventory.getAvailability("Suite"));
    }
}