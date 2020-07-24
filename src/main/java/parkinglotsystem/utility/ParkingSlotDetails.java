package parkinglotsystem.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ParkingSlotDetails {
    private final String carNumber;
    private final int slotNumber;
    private final String time;

    public ParkingSlotDetails(int slotNumber, String carNumber) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlotDetails that = (ParkingSlotDetails) o;
        return slotNumber == that.slotNumber &&
                Objects.equals(carNumber, that.carNumber) &&
                Objects.equals(time, that.time);
    }
}

