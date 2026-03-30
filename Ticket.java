package parking;
import java.time.*;
public class Ticket {
    private static int counter = 0;
    private int id;
    private Vehicle vehicle;
    private Slot slot;
    private LocalDateTime entryTime;

    public Ticket(Vehicle vehicle, Slot slot) {
        this.id = ++counter;
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryTime = LocalDateTime.now();
    }

    public Slot getSlot() { return slot; }
    public LocalDateTime getEntryTime() { return entryTime; }
}
