# FileSpark — Parking Lot System

A Java-based parking lot management system that handles multi-level parking, gate-based slot assignment, and billing.

---

## Class Diagram

```mermaid
classDiagram

    class ParkingLot {
        -List~Level~ levels
        -List~Gate~ gates
        -Map~String, ParkingTicket~ activeTickets
        -Map~SlotType, Double~ hourlyRates
        +ParkingLot(hourlyRates)
        +addLevel(Level)
        +addGate(Gate)
        +park(Vehicle, LocalDateTime, SlotType, String) ParkingTicket
        +park(Vehicle, LocalDateTime, String) ParkingTicket
        +exit(ParkingTicket, LocalDateTime) Bill
        +status() Map~SlotType, int[]~
        +printStatus()
    }

    class Level {
        -int levelNumber
        -List~ParkingSlot~ slots
        +addSlot(ParkingSlot)
        +getAvailableSlots(SlotType) List~ParkingSlot~
        +getSlots() List~ParkingSlot~
        +getLevelNumber() int
    }

    class Gate {
        -String gateId
        +getGateId() String
    }

    class ParkingSlot {
        -String slotId
        -SlotType slotType
        -boolean isOccupied
        -Map~String, Integer~ distanceFromGates
        +markOccupied()
        +markFree()
        +getDistanceFrom(String) int
        +getSlotId() String
        +getSlotType() SlotType
        +isOccupied() boolean
    }

    class ParkingTicket {
        -String ticketId
        -Vehicle vehicle
        -ParkingSlot slot
        -LocalDateTime entryTime
        -String entryGateId
        +getTicketId() String
        +getVehicle() Vehicle
        +getSlot() ParkingSlot
        +getEntryTime() LocalDateTime
        +getEntryGateId() String
    }

    class Bill {
        -ParkingTicket ticket
        -LocalDateTime exitTime
        -long hoursParked
        -double hourlyRate
        -double totalAmount
        +getTotalAmount() double
    }

    class Vehicle {
        -String vehicleNumber
        -VehicleType vehicleType
        -String ownerName
        +getVehicleNumber() String
        +getVehicleType() VehicleType
        +getOwnerName() String
    }

    class SlotType {
        <<enumeration>>
        SMALL
        MEDIUM
        LARGE
    }

    class VehicleType {
        <<enumeration>>
        TWO_WHEELER
        CAR
        BUS
    }

    ParkingLot "1" --> "*" Level : contains
    ParkingLot "1" --> "*" Gate : has
    ParkingLot "1" --> "*" ParkingTicket : tracks (active)
    ParkingLot ..> Bill : creates
    Level "1" --> "*" ParkingSlot : holds
    ParkingSlot --> SlotType : typed as
    ParkingTicket --> Vehicle : issued to
    ParkingTicket --> ParkingSlot : assigned
    Bill --> ParkingTicket : based on
    Vehicle --> VehicleType : typed as
```

---

## Architecture Overview

### Core Flow

1. **Entry** — A `Vehicle` calls `ParkingLot.park()` from a `Gate`. The lot walks a slot-type preference list (e.g. TWO_WHEELER → SMALL → MEDIUM → LARGE), collects all available slots of each type, and picks the one nearest to the entry gate by distance score.
2. **Ticket** — A `ParkingTicket` is issued and stored in `activeTickets`.
3. **Exit** — `ParkingLot.exit()` looks up the ticket, computes a `Bill` using the **slot type rate** (not vehicle type), frees the slot, and removes the ticket from active tracking.

### Slot Preference Rules

| Vehicle Type  | Preferred Slot Order           |
|---------------|-------------------------------|
| TWO_WHEELER   | SMALL → MEDIUM → LARGE        |
| CAR           | MEDIUM → LARGE                |
| BUS           | LARGE only                    |

### Billing Rules

- Rate is determined by **slot type**, not vehicle type.
- Minimum charge: **1 hour**.
- Partial hours are **rounded up** (ceiling).
