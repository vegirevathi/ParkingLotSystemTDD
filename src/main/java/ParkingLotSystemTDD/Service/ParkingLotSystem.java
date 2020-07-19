package ParkingLotSystemTDD.Service;

import ParkingLotSystemTDD.ParkingLotException;

public class ParkingLotSystem {
    private Object vehicle;

    public void park(Object vehicle) throws ParkingLotException {
        if (this.vehicle != null)
            throw new ParkingLotException("Parking Lot is full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
        this.vehicle = vehicle;
        if (this.vehicle == null)
            throw new ParkingLotException("Parking Lot is empty", ParkingLotException.ExceptionType.PARKING_LOT_EMPTY);
    }

    public boolean isVehicleParked(Object vehicle) {
        return this.vehicle.equals(vehicle);
    }

    public boolean unPark(Object vehicle) throws ParkingLotException {
        //if (this.vehicle == null) return false;
        if (this.vehicle != null && this.vehicle.equals(vehicle)) {
            this.vehicle = null;
            return true;
        }
        throw new ParkingLotException("Vehicle is not parked", ParkingLotException.ExceptionType.NO_SUCH_VEHICLE_PARKED);
    }
}
