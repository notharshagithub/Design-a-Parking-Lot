package parking;
import java.util.*;
public class ParkingLot {
    private List<Floor> floors;
    private SlotAllocationStrategy strategy;
    private PricingStrategy pricing;

    public ParkingLot(List<Floor> floors, SlotAllocationStrategy s, PricingStrategy p) {
        this.floors = floors;
        this.strategy = s;
        this.pricing = p;
    }

    public Ticket park(Vehicle v) {
        Slot slot = strategy.findSlot(this, v);
        if (slot == null) return null;
        slot.park(v);
        return new Ticket(v, slot);
    }

    public void exit(Ticket t) {
        t.getSlot().free();
        System.out.println("Bill: " + pricing.calculate(t));
    }

    public List<Floor> getFloors() { return floors; }
}
