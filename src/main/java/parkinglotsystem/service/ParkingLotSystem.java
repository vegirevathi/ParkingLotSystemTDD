package parkinglotsystem.service;

import parkinglotsystem.exception.ParkingLotException;
import parkinglotsystem.observer.ParkingLotObserver;
import parkinglotsystem.utility.ParkingSlotDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ParkingLotSystem {
    private final List<ParkingLotObserver> observers;
    private final int numOfSlots;
    private int parkingLotCapacity;
    private final Map<Integer, ParkingSlotDetails> parkingMap;
    private int vehicleCount;

    public ParkingLotSystem(int numberOfSlots) {
        this.observers = new ArrayList<>();
        this.parkingMap = new HashMap<>();
        this.numOfSlots = numberOfSlots;
    }

    public void registerParkingLotObserver(ParkingLotObserver observer) {
        this.observers.add(observer);
    }

    public void setParkingLotCapacity(int parkingLotCapacity) {
        this.parkingLotCapacity = parkingLotCapacity;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void park(String carNumber) {
        if (parkingMap.values()
                .stream()
                .anyMatch(slot -> slot.getCarNumber().equals(carNumber)))
            throw new ParkingLotException("Vehicle is already parked", ParkingLotException.e.ALREADY_PARKED);
        if (vehicleCount == numOfSlots)
            throw new ParkingLotException("Parking Lot is full", ParkingLotException.e.PARKING_LOT_FULL);
        for (ParkingLotObserver observer : observers) {
            observer.capacityIsFull();
        }
        int slotNumber = this.getSlotToPark(this.parkingMap);
        this.parkingMap.put(slotNumber, new ParkingSlotDetails(slotNumber, carNumber));
        this.vehicleCount++;
    }

    public boolean isVehicleParked(String carNumber) {
        return parkingMap.values()
                .stream()
                .anyMatch(slot -> slot.getCarNumber()==(carNumber));
    }

    public void unPark(int slotNumber, String carNumber) {
        if (parkingMap.values()
                      .stream()
                      .noneMatch(slot -> slot.getCarNumber().equals(carNumber)))
            throw new ParkingLotException("Vehicle is not parked", ParkingLotException.e.NO_SUCH_VEHICLE_PARKED);
        this.parkingMap.remove(slotNumber, new ParkingSlotDetails(slotNumber, carNumber));
        this.vehicleCount--;
    }

    public boolean isVehicleUnParked(String carNumber) {
        return parkingMap.values()
                .stream()
                .noneMatch(slot -> slot.getCarNumber().equals(carNumber));
    }

    public Integer getSlotToPark(Map<Integer, ParkingSlotDetails> parkingMap) {
        return Stream.iterate(1, slotNumber -> slotNumber <= this.parkingMap.size(),
                slotNumber -> (slotNumber + 1))
                .filter(slotNumber -> this.parkingMap.get(slotNumber) == null)
                .findFirst()
                .orElse(0);
    }

    private ParkingSlotDetails getSlotDetails(String carNumber) {
        return this.parkingMap.values()
                .stream()
                .filter(slot -> carNumber.equals(slot.getCarNumber()))
                .findFirst()
                .orElseThrow(() -> new ParkingLotException("Vehicle is not parked", ParkingLotException.e.NO_SUCH_VEHICLE_PARKED));
    }

    public int findCarNumber(String carNumber) throws ParkingLotException {
       return this.getSlotDetails(carNumber).getSlotNumber();
    }

    public String getVehicleParkedTime(String carNumber) {
        return this.getSlotDetails(carNumber).getParkedTime();
    }
}
