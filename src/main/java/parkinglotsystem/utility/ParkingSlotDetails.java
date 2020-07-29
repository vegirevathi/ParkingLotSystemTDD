package parkinglotsystem.utility;

import parkinglotsystem.model.Car;

import java.time.LocalDateTime;
import java.util.Objects;

public class ParkingSlotDetails {
    private String attendantName;
    private int lotNumber;
    private int slotNumber;
    private LocalDateTime time;
    private Car car;

    public ParkingSlotDetails(int lotNumber, int slotNumber, String attendantName, Car car) {
        this.lotNumber = lotNumber;
        this.slotNumber = slotNumber;
        this.attendantName = attendantName;
        this.car = car;
    }

    public ParkingSlotDetails() {
    }

    public LocalDateTime getParkedTime() {
        return time;
    }

    public String getAttendantName() {
        return this.attendantName;
    }

    public int getSlotNumber() {
        return this.slotNumber;
    }

    public Car getCarDetails() {
        return this.car;
    }

    public int getLotNumber() {
        return lotNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlotDetails that = (ParkingSlotDetails) o;
        return lotNumber == that.lotNumber &&
                slotNumber == that.slotNumber &&
                Objects.equals(attendantName, that.attendantName) &&
                Objects.equals(time, that.time) &&
                Objects.equals(car, that.car);
    }
}

