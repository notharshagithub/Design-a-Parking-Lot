import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class Bill {
    private ParkingTicket ticket;
    private LocalDateTime exitTime;
    private long          hoursParked;
    private double        hourlyRate;
    private double        totalAmount;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Bill(ParkingTicket ticket, LocalDateTime exitTime, double hourlyRate) {
        this.ticket     = ticket;
        this.exitTime   = exitTime;
        this.hourlyRate = hourlyRate;

        long seconds    = Duration.between(ticket.getEntryTime(), exitTime).getSeconds();
        // Minimum 1 hour; round up partial hours
        this.hoursParked   = Math.max(1, (long) Math.ceil(seconds / 3600.0));
        this.totalAmount   = hoursParked * hourlyRate;
    }

    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return "\n── Bill ────────────────────────────\n" +
               "  Ticket ID   : " + ticket.getTicketId() + "\n" +
               "  Vehicle     : " + ticket.getVehicle().getVehicleNumber() + "\n" +
               "  Slot Type   : " + ticket.getSlot().getSlotType() + "\n" +
               "  Duration    : " + hoursParked + " hour(s)\n" +
               "  Rate        : Rs." + String.format("%.2f", hourlyRate) + "/hr\n" +
               "  Total       : Rs." + String.format("%.2f", totalAmount) + "\n" +
               "────────────────────────────────────";
    }
}
