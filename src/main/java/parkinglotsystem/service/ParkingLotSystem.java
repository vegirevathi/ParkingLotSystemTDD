package parkinglotsystem.service;

import parkinglotsystem.enums.CarDetails;
import parkinglotsystem.enums.DriverType;
import parkinglotsystem.exception.ParkingLotException;
import parkinglotsystem.model.Car;
import parkinglotsystem.observer.ParkingLotObserver;
import parkinglotsystem.utility.ParkingSlotDetails;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLotSystem {
    private final List<ParkingLotObserver> observers;
    private final int numOfSlots;
    private int parkingLotCapacity;
    private final Map<Integer, ParkingSlotDetails> parkingMap;
    private int vehicleCount;
    private int lotNumber;
    private final String attendantName;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    public ParkingLotSystem(int numberOfSlots, String attendantName) {
        this.attendantName = attendantName;
        this.observers = new ArrayList<>();
        this.parkingMap = new HashMap<>();
        this.numOfSlots = numberOfSlots;
        IntStream.rangeClosed(1, (numberOfSlots)).forEach(slotNumber -> parkingMap.put(slotNumber, new ParkingSlotDetails()));
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

    public void park(Car car) {
        if (parkingMap.values()
                .stream()
                .anyMatch(slot -> slot.getCarDetails() == car))
            throw new ParkingLotException("Vehicle is already parked", ParkingLotException.e.ALREADY_PARKED);
        if (vehicleCount == numOfSlots)
            throw new ParkingLotException("Parking Lot is full", ParkingLotException.e.PARKING_LOT_FULL);
        for (ParkingLotObserver observer : observers) {
            observer.capacityIsFull();
        }
        int slotNumber = this.getSlotToPark(car.getCarSize());
        this.parkingMap.put(slotNumber, new ParkingSlotDetails(lotNumber, slotNumber, attendantName, car));
        this.vehicleCount++;
    }

    public boolean isVehicleParked(Car car) {
        return parkingMap.values()
                .stream()
                .anyMatch(slot -> slot.getCarDetails() == (car));
    }

    public void unPark(Car car) {
        int slotNumber = this.findCarNumber(car);
        if (parkingMap.values()
                .stream()
                .noneMatch(slot -> slot.getCarDetails() == (car)))
            throw new ParkingLotException("Vehicle is not parked", ParkingLotException.e.NO_SUCH_VEHICLE_PARKED);
        this.parkingMap.remove(slotNumber, new ParkingSlotDetails(lotNumber, slotNumber, attendantName, car));
        this.vehicleCount--;
    }

    public boolean isVehicleUnParked(Car car) {
        return parkingMap.values()
                .stream()
                .noneMatch(slot -> slot.getCarDetails() == car);
    }

    public int getSlotToPark(CarDetails carDetails) {
        if (CarDetails.LARGE.equals(carDetails))
            return this.parkingMap.keySet()
                    .stream()
                    .filter(slot -> parkingMap.get(slot).getCarDetails() == null)
                    .filter(slot -> (slot + 1) <= numOfSlots && parkingMap.get(slot + 1).getCarDetails() == null)
                    .findFirst().orElse(0);
        return parkingMap.keySet()
                .stream()
                .filter(slot -> parkingMap.get(slot).getCarDetails() == null)
                .findFirst().get();
    }

    private ParkingSlotDetails getSlotDetails(Car car) {
        return this.parkingMap.values()
                .stream()
                .filter(slot -> car.equals(slot.getCarDetails()))
                .findFirst()
                .orElseThrow(() ->
                        new ParkingLotException("Vehicle is not parked", ParkingLotException.e.NO_SUCH_VEHICLE_PARKED));
    }

    public int findCarNumber(Car car) {
        return this.getSlotDetails(car).getSlotNumber();
    }

    public String getVehicleParkedTime(Car car) {
        return LocalDateTime.now().format(format);
    }

    public List<ParkingSlotDetails> getCarDetailsBasedOnColour(String colour) {
        List<ParkingSlotDetails> list = this.parkingMap.values()
                .stream()
                .filter(parkingSlot -> parkingSlot.getCarDetails() != null)
                .filter(parkingSlot -> parkingSlot.getCarDetails().getCarColour().equals(colour))
                .collect(Collectors.toList());
        return list;
    }

    public List<ParkingSlotDetails> getCarDetailsBasedOnCompany(String company) {
        List<ParkingSlotDetails> list = this.parkingMap.values()
                .stream()
                .filter(parkingSlot -> parkingSlot.getCarDetails() != null)
                .filter(parkingSlot -> parkingSlot.getCarDetails().getCarCompany().equals(company))
                .collect(Collectors.toList());
        return list;
    }

    public List<ParkingSlotDetails> getCarDetailsBasedOnTime(int minutes) {
        List<ParkingSlotDetails> list = this.parkingMap.values()
                .stream()
                .filter(parkingSlot -> parkingSlot.getCarDetails() != null)
                .filter(parkingSlot -> parkingSlot.getParkedTime() != null)
                .filter(parkingSlot -> Duration.between(parkingSlot.getParkedTime(),
                        LocalDateTime.now()).toMinutes() <= minutes)
                .collect(Collectors.toList());
        return list;
    }

    public List<ParkingSlotDetails> getCarDetailsBasedOnLotNumber(int lotNumber) {
        List<ParkingSlotDetails> list = this.parkingMap.values()
                .stream()
                .filter(parkingSlot -> parkingSlot.getCarDetails() != null)
                .filter(parkingSlot -> parkingSlot.getCarDetails().getCarSize().equals(CarDetails.SMALL))
                .filter(parkingSlot -> parkingSlot.getCarDetails().getDriverType().equals(DriverType.HANDICAPPED))
                .collect(Collectors.toList());
        return list;
    }

    public List<ParkingSlotDetails> getAllParkedVehicles() {
        List<ParkingSlotDetails> list = this.parkingMap.values()
                .stream()
                .filter(parkingSlot -> parkingSlot.getCarDetails() != null)
                .collect(Collectors.toList());
        return list;
    }

    public List<ParkingSlotDetails> getCarDetailsBasedOnColourCompany(String carColour, String carCompany) {
        List<ParkingSlotDetails> list = this.parkingMap.values()
                .stream()
                .filter(parkingSlot -> parkingSlot.getCarDetails() != null)
                .filter(parkingSlot -> parkingSlot.getCarDetails().getCarColour().equals(carColour)
                                    || parkingSlot.getCarDetails().getCarCompany().equals(carCompany))
                .collect(Collectors.toList());
        return list;
    }
}
