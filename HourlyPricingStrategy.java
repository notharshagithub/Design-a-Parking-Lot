package parking;
import java.time.*;
public class HourlyPricingStrategy implements PricingStrategy {
    public double calculate(Ticket ticket) {
        long hours = Duration.between(ticket.getEntryTime(), LocalDateTime.now()).toHours() + 1;
        return hours * 10;
    }
}
