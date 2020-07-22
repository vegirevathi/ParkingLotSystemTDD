package parkinglotsystem.service;

import parkinglotsystem.observer.ParkingLotOwner;

import java.util.Map;

public class ParkingAttendant {
    private Map<Integer, String> parkingMap;

    public Map<Integer, String> parkCar(Map<Integer, String> parkingMap, String carNumber) {
        int slotNumber = ParkingLotOwner.getSlotToPark(parkingMap);
        this.parkingMap = parkingMap;
        this.parkingMap.put(slotNumber, carNumber);
        return this.parkingMap;
    }
}
