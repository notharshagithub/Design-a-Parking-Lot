package parking;
public class Slot {
    private int id;
    private SlotType type;
    private boolean isOccupied;
    private Vehicle vehicle;

    public Slot(int id, SlotType type) {
        this.id = id;
        this.type = type;
    }

    public boolean canFit(Vehicle vehicle) {
        return (vehicle.getType() == VehicleType.BIKE && type == SlotType.SMALL) ||
               (vehicle.getType() == VehicleType.CAR && type == SlotType.MEDIUM) ||
               (vehicle.getType() == VehicleType.TRUCK && type == SlotType.LARGE);
    }

    public boolean isOccupied() { return isOccupied; }

    public void park(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.isOccupied = true;
    }

    public void free() {
        this.vehicle = null;
        this.isOccupied = false;
    }

    public int getId() { return id; }
}
