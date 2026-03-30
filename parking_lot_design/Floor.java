package parking;
import java.util.*;
public class Floor {
    private int floorNumber;
    private List<Slot> slots;

    public Floor(int floorNumber, List<Slot> slots) {
        this.floorNumber = floorNumber;
        this.slots = slots;
    }

    public List<Slot> getSlots() { return slots; }
}
