package ParkingLotSystemTDD.Service;

public class ParkingLotSystemDisplay {
    public enum Person{ PARKINGLOTOWNER, AIRPORTSECURITY }

    public boolean isParkingFull(Person person, int parkingLotSize, int parkingCapacity) {
        return parkingLotSize == parkingCapacity;
    }

    public boolean isParkingHasSpace(Person person, int parkingLotSize, int parkingCapacity) {
        return parkingLotSize < parkingCapacity;
    }
}
