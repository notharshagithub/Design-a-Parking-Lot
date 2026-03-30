package parking;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<Slot> slots = Arrays.asList(
            new Slot(1, SlotType.SMALL),
            new Slot(2, SlotType.MEDIUM),
            new Slot(3, SlotType.LARGE)
        );

        Floor f = new Floor(1, slots);

        ParkingLot lot = new ParkingLot(
            Arrays.asList(f),
            new NearestSlotStrategy(),
            new HourlyPricingStrategy()
        );

        Vehicle car = new Vehicle("MH12", VehicleType.CAR);
        Ticket t = lot.park(car);
        lot.exit(t);
    }
}
