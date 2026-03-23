import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Level {
    private int levelNumber;
    private List<ParkingSlot> slots;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.slots       = new ArrayList<>();
    }

    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }

    public List<ParkingSlot> getAvailableSlots(SlotType slotType) {
        return slots.stream()
                .filter(s -> s.getSlotType() == slotType && !s.isOccupied())
                .collect(Collectors.toList());
    }

    public List<ParkingSlot> getSlots() { return slots; }
    public int getLevelNumber()         { return levelNumber; }

    @Override
    public String toString() {
        return "Level(" + levelNumber + ", slots=" + slots.size() + ")";
    }
}
