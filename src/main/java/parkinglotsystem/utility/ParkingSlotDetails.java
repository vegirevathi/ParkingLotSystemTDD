package parkinglotsystem.utility;

import parkinglotsystem.model.Car;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ParkingSlotDetails {
    private String carNumber;
    private final int slotNumber;
    private String time;
    private Car car;

    public ParkingSlotDetails(int slotNumber, String carNumber) {
        this.car = car;
        this.carNumber = carNumber;
        this.slotNumber = slotNumber;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        this.time = LocalDateTime.now().format(format);
    }


    public String getParkedTime() {
        return this.time;
    }

    public int getSlotNumber() {
        return this.slotNumber;
    }

    public String getCarNumber() {
        return this.carNumber;
    }

    public Car getCarDetails() {
        return this.car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlotDetails that = (ParkingSlotDetails) o;
        return slotNumber == that.slotNumber &&
                Objects.equals(carNumber, that.carNumber) &&
                Objects.equals(time, that.time) &&
                Objects.equals(car, that.car);
    }
}

