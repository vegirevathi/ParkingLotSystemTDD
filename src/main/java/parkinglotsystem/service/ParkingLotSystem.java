package parkinglotsystem.service;

import parkinglotsystem.exception.ParkingLotException;
import parkinglotsystem.observer.ParkingLotObserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLotSystem {
    private final List<ParkingLotObserver> observers;
    private int parkingLotCapacity;
    private Map<Integer, String> parkingMap;
    private ParkingAttendant parkingAttendant;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    public ParkingLotSystem() {
        this.observers = new ArrayList<>();
        this.parkingMap = new HashMap<>();
        parkingAttendant = new ParkingAttendant();
    }

    public void registerParkingLotObserver(ParkingLotObserver observer) {
        this.observers.add(observer);
    }

    public void setParkingLotCapacity(int parkingLotCapacity) {
        this.parkingLotCapacity = parkingLotCapacity;
    }

    public void park(String carNumber) {
        if (parkingMap.containsValue(carNumber))
            throw new ParkingLotException("Vehicle is already parked", ParkingLotException.e.ALREADY_PARKED);
        if (parkingMap.size() == parkingLotCapacity)
            throw new ParkingLotException("Parking Lot is full", ParkingLotException.e.PARKING_LOT_FULL);
        for (ParkingLotObserver observer : observers) {
            observer.capacityIsFull();
        }
        this.parkingMap = parkingAttendant.parkCar(parkingMap, carNumber);
    }

    public boolean isVehicleParked(String carNumber) {
        return this.parkingMap.containsValue(carNumber);
    }

    public void unPark(int slotNumber, String carNumber) {
        if (!this.parkingMap.containsValue(carNumber))
            throw new ParkingLotException("Vehicle is not parked", ParkingLotException.e.NO_SUCH_VEHICLE_PARKED);
        this.parkingMap.remove(slotNumber, carNumber);
    }

    public boolean isVehicleUnParked(String carNumber) {
        return !this.parkingMap.containsValue(carNumber);
    }

    public int findCarNumber(String carNumber) throws ParkingLotException {
        if (!parkingMap.containsValue(carNumber))
            throw new ParkingLotException("Vehicle number not found", ParkingLotException.e.CAR_NUMBER_MISMATCH);
        return parkingMap.keySet()
                .stream()
                .filter(key -> carNumber.equals(parkingMap.get(key)))
                .findFirst().get();
    }

    public String getVehicleParkedTime(String carNumber) {
        if (!this.parkingMap.containsValue(carNumber))
            throw new ParkingLotException("Vehicle is not parked", ParkingLotException.e.NO_SUCH_VEHICLE_PARKED);
        return LocalDateTime.now().format(format);

    }
}
