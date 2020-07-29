package parkinglotsystem.utility;

import parkinglotsystem.model.Car;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ParkingSlotDetails {
    private String attendantName;
    private int lotNumber;
    private int slotNumber;
    private String time;
    private Car car;

    public ParkingSlotDetails(int lotNumber, int slotNumber, String attendantName, Car car) {
        this.lotNumber = lotNumber;
        this.slotNumber = slotNumber;
        this.attendantName = attendantName;
        this.car = car;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        this.time = LocalDateTime.now().format(format);
    }

    public ParkingSlotDetails() {
    }

    public String getParkedTime() {
        return this.time;
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

