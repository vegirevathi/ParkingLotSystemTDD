package parkinglotsystem.utility;

import parkinglotsystem.model.Car;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ParkingSlotDetails {
    private String attendantName;
    private String carNumber;
    private final int slotNumber;
    private String time;
    private Car car;

    public ParkingSlotDetails(int slotNumber, Car car, String attendantName) {
        this.car = car;
        this.slotNumber = slotNumber;
        this.attendantName = attendantName;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        this.time = LocalDateTime.now().format(format);
    }

    public ParkingSlotDetails(int slotNumber, Car car) {
        this.car = car;
        this.slotNumber = slotNumber;
    }

    public ParkingSlotDetails(int slotNumber, String attendantName, String carNumber) {
        this.attendantName = attendantName;
        this.carNumber = carNumber;
        this.slotNumber = slotNumber;
    }

    public ParkingSlotDetails(int slotNumber, String carNumber) {
        this.carNumber = carNumber;
        this.slotNumber = slotNumber;
    }

    public String getParkedTime() {
        return this.time;
    }

    public int getSlotNumber() {
        return this.slotNumber;
    }

    public Car getCar() {
        return car;
    }

    public String getAttendantName() {
        return attendantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlotDetails that = (ParkingSlotDetails) o;
        return slotNumber == that.slotNumber &&
                Objects.equals(attendantName, that.attendantName) &&
                Objects.equals(carNumber, that.carNumber) &&
                Objects.equals(time, that.time) &&
                Objects.equals(car, that.car);
    }
}

