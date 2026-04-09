import java.util.HashMap;

class RoomInventory {
    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    void updateAvailability(String type, int count) {
        inventory.put(type, count);
    }

    void displayInventory() {
        for (String type : inventory.keySet()) {
            System.out.println(type + " Available: " + inventory.get(type));
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();

        inventory.displayInventory();
        System.out.println();

        inventory.updateAvailability("Single Room", 4);
        inventory.updateAvailability("Double Room", 2);

        inventory.displayInventory();
    }
}