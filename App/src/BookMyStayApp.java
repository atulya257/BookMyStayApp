import java.util.*;

// Reservation class
class Reservation {
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

// Booking History (stores confirmed bookings)
class BookingHistory {

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Read-only access
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Reporting Service
class BookingReportService {

    // Display all bookings
    public static void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---\n");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            System.out.println("Guest: " + r.getGuestName());
            System.out.println("Room Type: " + r.getRoomType());
            System.out.println("Room ID: " + r.getRoomId());
            System.out.println("---------------------------");
        }
    }

    // Generate summary report
    public static void generateSummary(List<Reservation> reservations) {

        Map<String, Integer> countByType = new HashMap<>();

        for (Reservation r : reservations) {
            countByType.put(
                    r.getRoomType(),
                    countByType.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n--- Booking Summary Report ---\n");

        for (String type : countByType.keySet()) {
            System.out.println("Room Type: " + type + " | Bookings: " + countByType.get(type));
        }

        System.out.println("Total Bookings: " + reservations.size());
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        // Simulating confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("Arun", "Single", "S1"));
        history.addReservation(new Reservation("Priya", "Suite", "S2"));
        history.addReservation(new Reservation("Rahul", "Single", "S3"));

        // Admin views booking history
        BookingReportService.displayAllBookings(history.getAllReservations());

        // Admin generates report
        BookingReportService.generateSummary(history.getAllReservations());
    }
}