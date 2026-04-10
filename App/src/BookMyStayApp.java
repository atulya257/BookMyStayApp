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

// Thread-safe Inventory
class RoomInventory {
    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    // Critical Section (synchronized)
    public synchronized boolean allocateRoom(String type) {
        int current = availability.getOrDefault(type, 0);

        if (current > 0) {
            availability.put(type, current - 1);
            return true;
        }
        return false;
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;
    private static int roomIdCounter = 1;

    public BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            // Get request safely
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) break;

            // Allocate room safely
            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                String roomId;

                // Critical section for ID generation
                synchronized (BookingProcessor.class) {
                    roomId = r.getRoomType().substring(0, 1).toUpperCase() + roomIdCounter++;
                }

                System.out.println(getName() + " CONFIRMED booking for " +
                        r.getGuestName() + " | Room ID: " + roomId);

            } else {
                System.out.println(getName() + " FAILED booking for " +
                        r.getGuestName() + " (No availability)");
            }
        }
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single", 2);

        BookingQueue queue = new BookingQueue();

        // Simulate multiple booking requests
        queue.addRequest(new Reservation("Arun", "Single"));
        queue.addRequest(new Reservation("Priya", "Single"));
        queue.addRequest(new Reservation("Rahul", "Single"));
        queue.addRequest(new Reservation("Sneha", "Single"));

        // Multiple threads (users)
        Thread t1 = new BookingProcessor("Thread-1", queue, inventory);
        Thread t2 = new BookingProcessor("Thread-2", queue, inventory);

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nFinal Availability: " + inventory.getAvailability("Single"));
    }
}