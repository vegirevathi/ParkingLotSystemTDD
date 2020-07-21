package ParkingLotSystemTDD.Service;

public class ParkingLotInfo {
    private final Person person;

    public enum Person {PARKING_LOT_OWNER, AIRPORT_SECURITY}

    public ParkingLotInfo(Person person) {
        this.person = person;
    }

    public boolean isParkingFull(ParkingLotSystem parkingLotSystem) {
        return parkingLotSystem.vehicle.size() == parkingLotSystem.parkingLotCapacity;
    }
}
