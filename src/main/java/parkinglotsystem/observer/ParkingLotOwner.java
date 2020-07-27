package parkinglotsystem.observer;

import java.util.Map;
import java.util.stream.Stream;

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
        return Stream.iterate(1, slotNumber -> slotNumber <= parkingMap.size(), slotNumber -> (slotNumber + 1))
                .filter(slotNumber -> parkingMap.get(slotNumber) == null)
                .findFirst()
                .orElse(0);
    }
}
