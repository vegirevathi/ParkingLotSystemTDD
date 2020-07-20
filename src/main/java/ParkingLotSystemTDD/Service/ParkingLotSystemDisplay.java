package ParkingLotSystemTDD.Service;

public class ParkingLotSystemDisplay {
    public enum Person{ PARKINGLOTOWNER, AIRPORTSECURITY}
    public boolean parkingFull = false;

    public boolean isParkingFull(Person person, int parkingLotSize, int parkingCapacity) {
        if (parkingLotSize == parkingCapacity)
            return true;
        return parkingFull;
    }
}
