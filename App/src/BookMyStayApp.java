import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

// Inventory Service
class RoomInventory {
    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public boolean isValidRoomType(String type) {
        return availability.containsKey(type);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) throws InvalidBookingException {
        int current = availability.getOrDefault(type, 0);

        if (current <= 0) {
            throw new InvalidBookingException("No available rooms for type: " + type);
        }

        availability.put(type, current - 1);
    }
}

// Validator
class BookingValidator {

    public static void validate(Reservation r, RoomInventory inventory) throws InvalidBookingException {

        if (r.getGuestName() == null || r.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available for type: " + r.getRoomType());
        }
    }
}

// Booking Service
class BookingService {

    private RoomInventory inventory;
    private int roomIdCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation r) {

        try {
            // Fail-fast validation
            BookingValidator.validate(r, inventory);

            // Allocation logic
            String roomId = generateRoomId(r.getRoomType());

            // Update inventory safely
            inventory.reduceRoom(r.getRoomType());

            System.out.println("\nBooking CONFIRMED for " + r.getGuestName());
            System.out.println("Room Type: " + r.getRoomType());
            System.out.println("Room ID: " + roomId);

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("\nBooking FAILED for " + r.getGuestName());
            System.out.println("Reason: " + e.getMessage());
        }
    }

    private String generateRoomId(String type) {
        return type.substring(0, 1).toUpperCase() + roomIdCounter++;
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single", 1);
        inventory.addRoom("Suite", 1);

        BookingService service = new BookingService(inventory);

        // Valid booking
        service.processBooking(new Reservation("Arun", "Single"));

        // Invalid room type
        service.processBooking(new Reservation("Priya", "Double"));

        // No availability
        service.processBooking(new Reservation("Rahul", "Single"));

        // Empty name
        service.processBooking(new Reservation("", "Suite"));
    }
}