package parkinglotsystem.service;

import parkinglotsystem.enums.DriverType;
import parkinglotsystem.exception.ParkingLotException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotAllotment {

    private final int numberOfLots;
    private final int numberOfSlots;
    public ArrayList<ParkingLotSystem> parkingLotList;
    private ParkingLotSystem allottedLotNumber;

    public ParkingLotAllotment(int numberOfLots, int numberOfSlots) {
        this.numberOfLots = numberOfLots;
        this.numberOfSlots = numberOfSlots;
        this.parkingLotList = new ArrayList<>();
        IntStream.range(0, numberOfLots)
                .forEach(lotNumber -> parkingLotList.add(new ParkingLotSystem(numberOfSlots)));
    }

    public void parkVehicle(String carNumber, DriverType driverType) {
        IntStream.range(0, numberOfLots)
                .filter(parkingLot -> parkingLotList.get(parkingLot)
                        .isVehicleParked(carNumber)).forEach(parkingLot -> {
            throw new ParkingLotException("Vehicle already exists", ParkingLotException.e.ALREADY_PARKED);
        });
        ParkingLotSystem parkingLotSystem = getParkingLotNumber(driverType);
        parkingLotSystem.park(carNumber);
    }

    private ParkingLotSystem getParkingLotNumber(DriverType driverType) {
        int totalNumOfCars;
        totalNumOfCars = IntStream.range(0, numberOfLots)
                .map(i -> this.parkingLotList.get(i)
                        .getVehicleCount()).sum();
        if (totalNumOfCars == (numberOfLots * numberOfSlots))
            throw new ParkingLotException("All lots are full", ParkingLotException.e.PARKING_LOT_FULL);
        List<ParkingLotSystem> lot = new ArrayList<>(this.parkingLotList);
        switch (driverType) {
            case NORMAL:
                lot.sort(Comparator.comparing(ParkingLotSystem::getVehicleCount));
                allottedLotNumber = lot.get(0);
                break;
            case HANDICAPPED:
                allottedLotNumber = lot.get(0);
                break;
        }
        return allottedLotNumber;
    }

    public String getCarLocation(String carNumber) {
        ParkingLotSystem parkingLotSystem = this.parkingLotList.stream()
                .filter(lot -> lot.isVehicleParked(carNumber))
                .findFirst().get();
        return String.format("Lot Number: %d  Slot Number: %d", parkingLotList.indexOf(parkingLotSystem),
                parkingLotSystem.findCarNumber(carNumber));
    }
}
