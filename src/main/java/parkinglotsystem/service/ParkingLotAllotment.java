package parkinglotsystem.service;

import parkinglotsystem.exception.ParkingLotException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotAllotment {

    private final int numberOfLots;
    private final int numberOfSlots;
    public ArrayList<ParkingLotSystem> parkingLotList;

    public ParkingLotAllotment(int numberOfLots, int numberOfSlots) {
        this.numberOfLots = numberOfLots;
        this.numberOfSlots = numberOfSlots;
        this.parkingLotList = new ArrayList<>();
        IntStream.range(0, numberOfLots).forEach(lotNumber -> parkingLotList.add(new ParkingLotSystem(numberOfSlots)));
    }

    public void parkVehicle(String carNumber) {
        IntStream.range(0, numberOfLots).filter(parkingLot -> parkingLotList.get(parkingLot).isVehicleParked(carNumber))
                .forEach(i -> {
                    throw new ParkingLotException("Vehicle already exists", ParkingLotException.e.ALREADY_PARKED);
                });
        ParkingLotSystem lot = getParkingLotNumber();
        lot.park(carNumber);
    }

    private ParkingLotSystem getParkingLotNumber() {
        int totalNumOfCars = IntStream.range(0, numberOfLots)
                .map(i -> this.parkingLotList.get(i).getVehicleCount()).sum();
        if (totalNumOfCars == (numberOfLots * numberOfSlots))
            throw new ParkingLotException("All lots are full", ParkingLotException.e.PARKING_LOT_FULL);
        List<ParkingLotSystem> selectLot = new ArrayList<>(this.parkingLotList);
        selectLot.sort(Comparator.comparing(ParkingLotSystem::getVehicleCount));
        return selectLot.get(0);
    }

    public String getCarLocation(String carNumber) {
        ParkingLotSystem parkingLotSystem = this.parkingLotList.stream().filter(lot -> lot.isVehicleParked(carNumber)).findFirst().get();
        return String.format("Lot Number: %d  Slot Number: %d", parkingLotList.indexOf(parkingLotSystem) + 1,
                parkingLotSystem.findCarNumber(carNumber));
    }
}
