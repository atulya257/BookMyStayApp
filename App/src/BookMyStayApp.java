import java.util.*;

// Reservation class (represents booking intent)
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

// Booking Queue Manager
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View all requests (read-only)
    public void displayQueue() {
        System.out.println("\nBooking Requests in Queue (FIFO Order):\n");

        if (queue.isEmpty()) {
            System.out.println("No booking requests.");
            return;
        }

        for (Reservation r : queue) {
            System.out.println("Guest: " + r.getGuestName());
            System.out.println("Requested Room: " + r.getRoomType());
            System.out.println("---------------------------");
        }
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulating guest requests
        bookingQueue.addRequest(new Reservation("Arun", "Single"));
        bookingQueue.addRequest(new Reservation("Priya", "Suite"));
        bookingQueue.addRequest(new Reservation("Rahul", "Double"));

        // Display queue (FIFO order maintained)
        bookingQueue.displayQueue();
    }
}