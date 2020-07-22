package parkinglotsystem.observer;

import java.util.Map;

public class ParkingLotOwner implements ParkingLotObserver {
    private boolean isFullCapacity;

    @Override
    public void capacityIsFull() {
        isFullCapacity = true;
    }

    public boolean isCapacityFull() {
        return this.isFullCapacity;
    }

    public static Integer getSlotToPark(Map<Integer, String> parkingMap) {
        for (Integer slotNumber = 1; slotNumber <= parkingMap.size(); slotNumber++)
            if (parkingMap.get(slotNumber) == null )
                return slotNumber;
            return 0;
    }
}
