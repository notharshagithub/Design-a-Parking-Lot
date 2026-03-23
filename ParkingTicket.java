import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ParkingTicket {
    private String         ticketId;
    private Vehicle        vehicle;
    private ParkingSlot    slot;
    private LocalDateTime  entryTime;
    private String         entryGateId;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ParkingTicket(Vehicle vehicle, ParkingSlot slot,
                         LocalDateTime entryTime, String entryGateId) {
        this.ticketId    = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.vehicle     = vehicle;
        this.slot        = slot;
        this.entryTime   = entryTime;
        this.entryGateId = entryGateId;
    }

    public String        getTicketId()    { return ticketId; }
    public Vehicle       getVehicle()     { return vehicle; }
    public ParkingSlot   getSlot()        { return slot; }
    public LocalDateTime getEntryTime()   { return entryTime; }
    public String        getEntryGateId() { return entryGateId; }

    @Override
    public String toString() {
        return "\n── Parking Ticket ──────────────────\n" +
               "  Ticket ID   : " + ticketId + "\n" +
               "  Vehicle     : " + vehicle.getVehicleNumber() +
                                   " (" + vehicle.getVehicleType() + ")\n" +
               "  Slot        : " + slot.getSlotId() +
                                   " (" + slot.getSlotType() + ")\n" +
               "  Entry Time  : " + entryTime.format(FMT) + "\n" +
               "  Entry Gate  : " + entryGateId + "\n" +
               "────────────────────────────────────";
    }
}
