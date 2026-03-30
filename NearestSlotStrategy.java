package parking;
public class NearestSlotStrategy implements SlotAllocationStrategy {
    public Slot findSlot(ParkingLot lot, Vehicle vehicle) {
        for (Floor f : lot.getFloors()) {
            for (Slot s : f.getSlots()) {
                if (!s.isOccupied() && s.canFit(vehicle)) return s;
            }
        }
        return null;
    }
}
