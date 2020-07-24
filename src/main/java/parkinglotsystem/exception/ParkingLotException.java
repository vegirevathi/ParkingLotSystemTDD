package parkinglotsystem.exception;

public class ParkingLotException extends RuntimeException {
    public enum e {
        PARKING_LOT_FULL, NO_SUCH_VEHICLE_PARKED, ALREADY_PARKED;
    }

    public e type;

    public ParkingLotException(String message, e type) {
        super(message);
        this.type = type;
    }
}
