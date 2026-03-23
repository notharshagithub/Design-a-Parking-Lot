public class Vehicle {
    private String vehicleNumber;
    private VehicleType vehicleType;
    private String ownerName;

    public Vehicle(String vehicleNumber, VehicleType vehicleType, String ownerName) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType   = vehicleType;
        this.ownerName     = ownerName;
    }

    public Vehicle(String vehicleNumber, VehicleType vehicleType) {
        this(vehicleNumber, vehicleType, "");
    }

    public String getVehicleNumber() { return vehicleNumber; }
    public VehicleType getVehicleType() { return vehicleType; }
    public String getOwnerName()     { return ownerName; }

    @Override
    public String toString() {
        return "Vehicle(" + vehicleNumber + ", " + vehicleType + ")";
    }
}
