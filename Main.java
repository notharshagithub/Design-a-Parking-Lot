import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Main {

    /** Builds a 2-level lot with 2 entry gates for demo */
    static ParkingLot buildSampleLot() {
        Map<SlotType, Double> rates = new HashMap<>();
        rates.put(SlotType.SMALL,  20.0);
        rates.put(SlotType.MEDIUM, 40.0);
        rates.put(SlotType.LARGE,  80.0);

        ParkingLot lot = new ParkingLot(rates);
        lot.addGate(new Gate("G1"));
        lot.addGate(new Gate("G2"));

        // ── Level 1 ──────────────────────────────────────────────
        Level level1 = new Level(1);

        // Slots closer to G1
        level1.addSlot(new ParkingSlot("L1-S1", SlotType.SMALL,  map("G1",1,"G2",9)));
        level1.addSlot(new ParkingSlot("L1-S2", SlotType.SMALL,  map("G1",2,"G2",8)));
        level1.addSlot(new ParkingSlot("L1-M1", SlotType.MEDIUM, map("G1",3,"G2",7)));
        level1.addSlot(new ParkingSlot("L1-M2", SlotType.MEDIUM, map("G1",4,"G2",6)));
        // Slots closer to G2
        level1.addSlot(new ParkingSlot("L1-M3", SlotType.MEDIUM, map("G1",7,"G2",3)));
        level1.addSlot(new ParkingSlot("L1-L1", SlotType.LARGE,  map("G1",8,"G2",2)));
        lot.addLevel(level1);

        // ── Level 2 ──────────────────────────────────────────────
        Level level2 = new Level(2);
        level2.addSlot(new ParkingSlot("L2-S1", SlotType.SMALL,  map("G1",5,"G2",5)));
        level2.addSlot(new ParkingSlot("L2-M1", SlotType.MEDIUM, map("G1",5,"G2",5)));
        level2.addSlot(new ParkingSlot("L2-L1", SlotType.LARGE,  map("G1",5,"G2",5)));
        level2.addSlot(new ParkingSlot("L2-L2", SlotType.LARGE,  map("G1",6,"G2",6)));
        lot.addLevel(level2);

        return lot;
    }

    /** Tiny helper to build a Map<String,Integer> inline */
    static Map<String, Integer> map(String k1, int v1, String k2, int v2) {
        Map<String, Integer> m = new HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        return m;
    }

    public static void main(String[] args) {
        ParkingLot lot = buildSampleLot();

        System.out.println("=== Initial Status ===");
        lot.printStatus();

        // ── Scenario 1: Bike enters from G1 ──────────────────────
        Vehicle bike1   = new Vehicle("MH12-AB1234", VehicleType.TWO_WHEELER, "Ravi");
        LocalDateTime t1 = LocalDateTime.of(2024, 6, 1, 9, 0);
        ParkingTicket ticket1 = lot.park(bike1, t1, "G1");
        System.out.println(ticket1);

        // ── Scenario 2: Car enters from G2 ───────────────────────
        Vehicle car   = new Vehicle("MH01-CD5678", VehicleType.CAR, "Sneha");
        LocalDateTime t2 = LocalDateTime.of(2024, 6, 1, 9, 30);
        ParkingTicket ticket2 = lot.park(car, t2, "G2");
        System.out.println(ticket2);

        // ── Scenario 3: Bus enters from G1 ───────────────────────
        Vehicle bus   = new Vehicle("MH04-EF9999", VehicleType.BUS, "BEST");
        LocalDateTime t3 = LocalDateTime.of(2024, 6, 1, 10, 0);
        ParkingTicket ticket3 = lot.park(bus, t3, "G1");
        System.out.println(ticket3);

        // ── Scenario 4: All small slots fill up — bike falls back to MEDIUM ──
        Vehicle bike2 = new Vehicle("MH12-XY0001", VehicleType.TWO_WHEELER, "Arjun");
        Vehicle bikeTemp = new Vehicle("MH12-TMP001", VehicleType.TWO_WHEELER);
        lot.park(bikeTemp, t3, "G1");   // fills L2-S1 (last small slot)
        ParkingTicket ticket4 = lot.park(bike2, t3, "G1");
        System.out.println(ticket4);    // should land in a MEDIUM slot

        System.out.println("\n=== Status After Parking ===");
        lot.printStatus();

        // ── Exit scenarios ────────────────────────────────────────
        System.out.println("\n=== Exit Scenarios ===");

        // Bike1: 9:00 -> 11:30 = 2.5 hrs -> ceil = 3 hrs
        Bill bill1 = lot.exit(ticket1, LocalDateTime.of(2024, 6, 1, 11, 30));
        System.out.println(bill1);

        // Car: 9:30 -> 13:30 = 4 hrs
        Bill bill2 = lot.exit(ticket2, LocalDateTime.of(2024, 6, 1, 13, 30));
        System.out.println(bill2);

        // Bus: 10:00 -> 10:20 = 20 min -> minimum 1 hr
        Bill bill3 = lot.exit(ticket3, LocalDateTime.of(2024, 6, 1, 10, 20));
        System.out.println(bill3);

        // Bike2 parked in MEDIUM slot — billed at MEDIUM rate
        Bill bill4 = lot.exit(ticket4, LocalDateTime.of(2024, 6, 1, 12, 15));
        System.out.println(bill4);

        System.out.println("\n=== Final Status ===");
        lot.printStatus();
    }
}
