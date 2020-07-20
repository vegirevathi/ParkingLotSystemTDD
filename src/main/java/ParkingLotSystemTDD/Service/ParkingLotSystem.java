package ParkingLotSystemTDD.Service;

import ParkingLotSystemTDD.Exception.ParkingLotException;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotSystem {
    public int parkingLotCapacity;
    public List vehicle;

    public ParkingLotSystem() {
        this.vehicle = new ArrayList();
    }

    public void setParkingLotCapacity(int parkingLotCapacity) {
        this.parkingLotCapacity = parkingLotCapacity;
    }

    public void park(Object vehicle) throws ParkingLotException {
        if (this.vehicle.size() == parkingLotCapacity)
            throw new ParkingLotException("Parking Lot is full", ParkingLotException.e.PARKING_LOT_FULL);
        if (this.vehicle.contains(vehicle))
            throw new ParkingLotException("Vehicle is already parked", ParkingLotException.e.ALREADY_PARKED);
        this.vehicle.add(vehicle);
    }

    public boolean isVehicleParked(Object vehicle) {
        return this.vehicle.contains(vehicle);
    }

    public void unPark(Object vehicle) throws ParkingLotException {
        if (!this.vehicle.contains(vehicle))
            throw new ParkingLotException("Vehicle is not parked", ParkingLotException.e.NO_SUCH_VEHICLE_PARKED);
        this.vehicle.remove(vehicle);
    }

    public boolean isVehicleUnParked(Object vehicle) {
        return !this.vehicle.contains(vehicle);
    }
}
