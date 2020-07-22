package ParkingLotSystemTDD.Service;

import ParkingLotSystemTDD.Exception.ParkingLotException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLotSystem {
    private final List<ParkingLotObserver> observers;
    private int parkingLotCapacity;
    private Map<Integer, String> parkingMap;

    public ParkingLotSystem() {
        this.observers = new ArrayList<>();
        this.parkingMap = new HashMap<>();
    }

    public void registerParkingLotObserver(ParkingLotObserver observer) {
        this.observers.add(observer);
    }
    public void setParkingLotCapacity(int parkingLotCapacity) {
        this.parkingLotCapacity = parkingLotCapacity;
    }

    public void park(int slotNumber, String carNumber) throws ParkingLotException {
        if (this.parkingMap.containsValue(carNumber))
            throw new ParkingLotException("Vehicle is already parked", ParkingLotException.e.ALREADY_PARKED);
        if (this.parkingMap.size() == parkingLotCapacity)
            throw new ParkingLotException("Parking Lot is full", ParkingLotException.e.PARKING_LOT_FULL);
            for (ParkingLotObserver observer: observers) {
                observer.capacityIsFull();
            }
        this.parkingMap.put(slotNumber, carNumber);
    }

    public boolean isVehicleParked(String carNumber) {
        return this.parkingMap.containsValue(carNumber);
    }

    public void unPark(int slotNumber, String carNumber) throws ParkingLotException {
        if (!this.parkingMap.containsValue(carNumber))
            throw new ParkingLotException("Vehicle is not parked", ParkingLotException.e.NO_SUCH_VEHICLE_PARKED);
        this.parkingMap.remove(slotNumber, carNumber);
    }

    public boolean isVehicleUnParked(String carNumber) {
        return !this.parkingMap.containsValue(carNumber);
    }
}
