import java.util.HashMap;
import java.util.Map;

public class ParkingSlot {
    private String   slotId;
    private SlotType slotType;
    private boolean  isOccupied;
    // distance from each gate: gateId -> distance value (lower = closer)
    private Map<String, Integer> distanceFromGates;

    public ParkingSlot(String slotId, SlotType slotType, Map<String, Integer> distanceFromGates) {
        this.slotId            = slotId;
        this.slotType          = slotType;
        this.distanceFromGates = distanceFromGates;
        this.isOccupied        = false;
    }

    public void markOccupied() { this.isOccupied = true; }
    public void markFree()     { this.isOccupied = false; }

    public int getDistanceFrom(String gateId) {
        return distanceFromGates.getOrDefault(gateId, Integer.MAX_VALUE);
    }

    public String   getSlotId()     { return slotId; }
    public SlotType getSlotType()   { return slotType; }
    public boolean  isOccupied()    { return isOccupied; }

    @Override
    public String toString() {
        return "Slot(" + slotId + ", " + slotType + ", " + (isOccupied ? "Occupied" : "Free") + ")";
    }
}
