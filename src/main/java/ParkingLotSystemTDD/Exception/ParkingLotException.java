package ParkingLotSystemTDD.Exception;

public class ParkingLotException extends Throwable {
    public enum ExceptionType {
        PARKING_LOT_FULL, NO_SUCH_VEHICLE_PARKED;
    }
    public ExceptionType type;

    public ParkingLotException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
