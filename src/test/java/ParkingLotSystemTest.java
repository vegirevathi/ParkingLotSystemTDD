import ParkingLotSystemTDD.Exception.ParkingLotException;
import ParkingLotSystemTDD.Service.AirportSecurity;
import ParkingLotSystemTDD.Service.ParkingLotOwner;
import ParkingLotSystemTDD.Service.ParkingLotSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParkingLotSystemTest {
    ParkingLotSystem parkingLotSystem = null;

    @Before
    public void setUp() {
        parkingLotSystem = new ParkingLotSystem();
        this.parkingLotSystem.setParkingLotCapacity(2);
    }

    @Test
    public void givenAVehicle_WhenParked_ShouldReturnTrue() {
        try {
            Object vehicle = new Object();
            parkingLotSystem.park(vehicle);
            boolean park = parkingLotSystem.isVehicleParked(vehicle);
            Assert.assertTrue(park);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAVehicle_WhenParkingLotFull_ShouldThrowException() {
        try {
            Object vehicle1 = new Object();
            parkingLotSystem.park(vehicle1);
            Object vehicle2 = new Object();
            parkingLotSystem.park(vehicle2);
            Object vehicle3 = new Object();
            parkingLotSystem.park(vehicle3);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Parking Lot is full", e.getMessage());
        }
    }

    @Test
    public void givenAVehicle_WhenAlreadyParked_ShouldThrowException() {
        try {
            Object vehicle = new Object();
            parkingLotSystem.park(vehicle);
            parkingLotSystem.park(vehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle is already parked", e.getMessage());
        }
    }

    @Test
    public void givenAVehicle_WhenNotParked_ShouldReturnFalse() {
        try {
            Object vehicle = new Object();
            parkingLotSystem.park(vehicle);
            boolean park = parkingLotSystem.isVehicleParked(null);
            Assert.assertFalse(park);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAVehicle_WhenUnParked_ShouldReturnTrue() throws ParkingLotException {
        Object vehicle = new Object();
        parkingLotSystem.park(vehicle);
        parkingLotSystem.unPark(vehicle);
        boolean unPark = parkingLotSystem.isVehicleUnParked(vehicle);
        Assert.assertTrue(unPark);
    }

    @Test
    public void givenAVehicle_WhenNotParked_AndTryingForUnPark_ShouldThrowException() {
        try {
            Object vehicle = new Object();
            parkingLotSystem.unPark(vehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle is not parked", e.getMessage());
        }
    }

    @Test
    public void givenWhenParkingLotIsFull_ShouldInformTheParkingLotOwner() {
        ParkingLotOwner parkingLotOwner = new ParkingLotOwner();
        parkingLotSystem.registerParkingLotObserver(parkingLotOwner);
        Object vehicle1 = new Object();
        Object vehicle2 = new Object();
        try {
            parkingLotSystem.park(vehicle1);
            parkingLotSystem.park(vehicle2);
            parkingLotSystem.park(new Object());
        } catch (ParkingLotException e) {
            boolean parkingFull = parkingLotOwner.isCapacityFull();
            Assert.assertTrue(parkingFull);
        }
    }

    @Test
    public void givenWhenParkingLotIsFull_ShouldInformTheAirportSecurity() {
        AirportSecurity airportSecurity = new AirportSecurity();
        parkingLotSystem.registerParkingLotObserver(airportSecurity);
        Object vehicle1 = new Object();
        Object vehicle2 = new Object();
        try {
            parkingLotSystem.park(vehicle1);
            parkingLotSystem.park(vehicle2);
            parkingLotSystem.park(new Object());
        } catch (ParkingLotException e) {
            boolean parkingFull = airportSecurity.isCapacityFull();
            Assert.assertTrue(parkingFull);
        }
    }

    @Test
    public void givenWhenParkingLotSpaceIsAvailableAfterFull_ShouldReturnTrue() throws ParkingLotException {
        ParkingLotOwner parkingLotOwner = new ParkingLotOwner();
        parkingLotSystem.registerParkingLotObserver(parkingLotOwner);
        Object vehicle1 = new Object();
        Object vehicle2 = new Object();
        try {
            parkingLotSystem.park(vehicle1);
            parkingLotSystem.park(vehicle2);
        } catch (ParkingLotException e) {
            parkingLotSystem.unPark(vehicle1);
            boolean parkingFull = parkingLotOwner.isCapacityFull();
            Assert.assertFalse(parkingFull);
        }
    }
}