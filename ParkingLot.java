import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ParkingLot {

    private List<Level>           levels;
    private List<Gate>            gates;
    private Map<String, ParkingTicket> activeTickets;   // ticketId -> ticket
    private Map<SlotType, Double> hourlyRates;

    // Which slot types a vehicle can use (in preference order: best fit first)
    private static final Map<VehicleType, List<SlotType>> SLOT_PREFERENCE;
    static {
        SLOT_PREFERENCE = new HashMap<>();
        SLOT_PREFERENCE.put(VehicleType.TWO_WHEELER,
                Arrays.asList(SlotType.SMALL, SlotType.MEDIUM, SlotType.LARGE));
        SLOT_PREFERENCE.put(VehicleType.CAR,
                Arrays.asList(SlotType.MEDIUM, SlotType.LARGE));
        SLOT_PREFERENCE.put(VehicleType.BUS,
                Arrays.asList(SlotType.LARGE));
    }

    // ── Constructor ──────────────────────────────────────────────────────────

    public ParkingLot(Map<SlotType, Double> hourlyRates) {
        this.levels        = new ArrayList<>();
        this.gates         = new ArrayList<>();
        this.activeTickets = new HashMap<>();
        this.hourlyRates   = hourlyRates;
    }

    /** Convenience constructor with default rates */
    public ParkingLot() {
        this(defaultRates());
    }

    private static Map<SlotType, Double> defaultRates() {
        Map<SlotType, Double> r = new HashMap<>();
        r.put(SlotType.SMALL,  20.0);
        r.put(SlotType.MEDIUM, 40.0);
        r.put(SlotType.LARGE,  80.0);
        return r;
    }

    // ── Setup helpers ────────────────────────────────────────────────────────

    public void addLevel(Level level) { levels.add(level); }
    public void addGate(Gate gate)    { gates.add(gate); }

    // ── Core API ─────────────────────────────────────────────────────────────

    /**
     * Parks a vehicle and returns a ParkingTicket.
     *
     * Algorithm:
     * 1. Build a preference list of compatible slot types for the vehicle.
     * 2. If caller requested a specific slot type (and it's compatible), try that first.
     * 3. Walk the list until a slot is available.
     * 4. Among available slots, pick the one with the smallest distance to entryGateId.
     */
    public ParkingTicket park(Vehicle vehicle, LocalDateTime entryTime,
                              SlotType requestedSlotType, String entryGateId) {

        List<SlotType> preference = new ArrayList<>(SLOT_PREFERENCE.get(vehicle.getVehicleType()));

        // Move requested type to front if compatible
        if (requestedSlotType != null && preference.contains(requestedSlotType)) {
            preference.remove(requestedSlotType);
            preference.add(0, requestedSlotType);
        }

        ParkingSlot bestSlot = null;
        for (SlotType slotType : preference) {
            List<ParkingSlot> candidates = getAllAvailableSlots(slotType);
            if (!candidates.isEmpty()) {
                final String gId = entryGateId;
                // Sort by distance from entry gate; pick nearest
                candidates.sort(Comparator.comparingInt(s -> s.getDistanceFrom(gId)));
                bestSlot = candidates.get(0);
                break;
            }
        }

        if (bestSlot == null) {
            throw new RuntimeException("No compatible parking slot available.");
        }

        bestSlot.markOccupied();
        ParkingTicket ticket = new ParkingTicket(vehicle, bestSlot, entryTime, entryGateId);
        activeTickets.put(ticket.getTicketId(), ticket);
        return ticket;
    }

    /** Overload — no specific slot type requested */
    public ParkingTicket park(Vehicle vehicle, LocalDateTime entryTime, String entryGateId) {
        return park(vehicle, entryTime, null, entryGateId);
    }

    /**
     * Processes vehicle exit.
     * Bill is calculated based on SLOT TYPE rate, not vehicle type.
     */
    public Bill exit(ParkingTicket ticket, LocalDateTime exitTime) {
        if (!activeTickets.containsKey(ticket.getTicketId())) {
            throw new RuntimeException("Ticket " + ticket.getTicketId() +
                                       " not found or already processed.");
        }

        double rate = hourlyRates.get(ticket.getSlot().getSlotType());
        Bill bill   = new Bill(ticket, exitTime, rate);

        ticket.getSlot().markFree();
        activeTickets.remove(ticket.getTicketId());
        return bill;
    }

    /**
     * Returns current availability of parking slots by SlotType.
     * Map: SlotType -> int[]{total, available, occupied}
     */
    public Map<SlotType, int[]> status() {
        Map<SlotType, int[]> result = new LinkedHashMap<>();
        for (SlotType st : SlotType.values()) {
            result.put(st, new int[]{0, 0, 0});   // [total, available, occupied]
        }

        for (Level level : levels) {
            for (ParkingSlot slot : level.getSlots()) {
                int[] counts = result.get(slot.getSlotType());
                counts[0]++;                         // total
                if (slot.isOccupied()) counts[2]++;  // occupied
                else                  counts[1]++;   // available
            }
        }
        return result;
    }

    public void printStatus() {
        Map<SlotType, int[]> s = status();
        System.out.println("\n── Parking Lot Status ──────────────");
        for (Map.Entry<SlotType, int[]> e : s.entrySet()) {
            int[] c = e.getValue();
            System.out.printf("  %-8s | Total: %d  Available: %d  Occupied: %d%n",
                    e.getKey(), c[0], c[1], c[2]);
        }
        System.out.println("────────────────────────────────────");
    }

    // ── Private helper ───────────────────────────────────────────────────────

    private List<ParkingSlot> getAllAvailableSlots(SlotType slotType) {
        return levels.stream()
                .flatMap(l -> l.getAvailableSlots(slotType).stream())
                .collect(Collectors.toList());
    }
}
