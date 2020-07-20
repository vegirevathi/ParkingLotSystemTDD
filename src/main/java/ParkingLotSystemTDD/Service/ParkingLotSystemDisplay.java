package ParkingLotSystemTDD.Service;

public class ParkingLotSystemDisplay {
    public boolean parkingFull = false;

    public boolean isParkingFull(int parkingLotSize, int parkingCapacity) {
        if (parkingLotSize == parkingCapacity)
            return true;
        return parkingFull;
    }
}
