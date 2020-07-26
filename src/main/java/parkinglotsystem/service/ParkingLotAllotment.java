package parkinglotsystem.service;

import parkinglotsystem.enums.DriverType;
import parkinglotsystem.exception.ParkingLotException;
import parkinglotsystem.model.Car;
import parkinglotsystem.utility.ParkingSlotDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static parkinglotsystem.enums.CarDetails.*;

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

    public void parkVehicle(Car car, DriverType driverType) {
        int bound = numberOfLots;
        IntStream.range(0, bound).filter(parkingLot -> parkingLotList.get(parkingLot).isVehicleParked(car.getCarNumber())).forEach(parkingLot -> {
            throw new ParkingLotException("Vehicle already exists", ParkingLotException.e.ALREADY_PARKED);
        });
        ParkingLotSystem parkingLotSystem = getParkingLotNumber(car, driverType);
        parkingLotSystem.park(car.getCarNumber());
    }

    private ParkingLotSystem getParkingLotNumber(Car car, DriverType driverType) {
        int totalNumOfCars;
        totalNumOfCars = IntStream.range(0, numberOfLots).map(i -> this.parkingLotList.get(i).getVehicleCount()).sum();
        if (totalNumOfCars == (numberOfLots * numberOfSlots))
            throw new ParkingLotException("All lots are full", ParkingLotException.e.PARKING_LOT_FULL);
        List<ParkingLotSystem> lot = new ArrayList<>(this.parkingLotList);
        switch (driverType) {
            case NORMAL:
                int specificLotForLargeVehicles = 1;
                if (car.getCarSize() == LARGE) {
                    allottedLotNumber = lot.get(specificLotForLargeVehicles);
                }
                lot.sort(Comparator.comparing(ParkingLotSystem::getVehicleCount));
                allottedLotNumber =  lot.get(0);
                break;
            case HANDICAPPED:
                int specificLotForHandicapped = 0;
                int nearestParkingLot = 0;
                if (car.getCarSize() == LARGE) {
                    allottedLotNumber = lot.get(specificLotForHandicapped);
                }
                allottedLotNumber =  lot.get(nearestParkingLot);
                break;
        }
        return allottedLotNumber;
    }

    public String getCarLocation(Car car) {
        ParkingLotSystem parkingLotSystem = this.parkingLotList.stream().filter(lot -> lot.isVehicleParked(car.getCarNumber())).findFirst().get();
        return String.format("Lot Number: %d  Slot Number: %d", parkingLotList.indexOf(parkingLotSystem),
                parkingLotSystem.findCarNumber(car.getCarNumber()));
    }

    public List<String> findVehicleWithColourAndCompany(String colour, String company) {
        List<String> list = new ArrayList<>();
        this.parkingLotList.forEach(lot -> {
            List<ParkingSlotDetails> carsParkingDetails = new ArrayList<>(lot.findVehicleWithColourAndCompany(colour, company));
            carsParkingDetails.stream().map(details -> (details.getCar().getCarNumber())).forEach(list::add); });
        return list;
    }
}
