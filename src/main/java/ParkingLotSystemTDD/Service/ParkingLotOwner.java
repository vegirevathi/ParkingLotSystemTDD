package ParkingLotSystemTDD.Service;

public class ParkingLotOwner implements  ParkingLotObserver{
    private boolean isFullCapacity;

    @Override
    public void capacityIsFull() {
        isFullCapacity = true;
    }

    @Override
    public void capacityIsAvailable() {
        isFullCapacity = false;
    }

    public boolean isCapacityFull() {
        return this.isFullCapacity;
    }
}
