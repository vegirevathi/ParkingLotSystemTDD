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
            throw new ParkingLotException("Parking Lot is full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
        this.vehicle.add(vehicle);
    }

    public boolean isVehicleParked(Object vehicle) {
        return this.vehicle.contains(vehicle);
    }

    public boolean unPark(Object vehicle) throws ParkingLotException {
        if (!this.vehicle.contains(vehicle))
            throw new ParkingLotException("Vehicle is not parked", ParkingLotException.ExceptionType.NO_SUCH_VEHICLE_PARKED);
        this.vehicle.remove(vehicle);
        return true;
    }
}
