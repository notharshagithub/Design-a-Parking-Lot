package parking;
public interface SlotAllocationStrategy {
    Slot findSlot(ParkingLot lot, Vehicle vehicle);
}
