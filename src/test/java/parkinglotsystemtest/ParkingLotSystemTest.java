package parkinglotsystemtest;

import parkinglotsystem.exception.ParkingLotException;
import parkinglotsystem.observer.AirportSecurity;
import parkinglotsystem.observer.ParkingLotOwner;
import parkinglotsystem.service.ParkingLotSystem;
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
            parkingLotSystem.park("AP 1234");
            boolean park = parkingLotSystem.isVehicleParked("AP 1234");
            Assert.assertTrue(park);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAVehicle_WhenParkingLotFull_ShouldThrowException() {
        try {
            parkingLotSystem.park("AP 1237");
            parkingLotSystem.park("AP 1234");
            parkingLotSystem.park("AP 1238");
        } catch (ParkingLotException e) {
            Assert.assertEquals("Parking Lot is full", e.getMessage());
        }
    }

    @Test
    public void givenAVehicle_WhenAlreadyParked_ShouldThrowException() {
        try {
            parkingLotSystem.park("AP 1234");
            parkingLotSystem.park("AP 1234");
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle is already parked", e.getMessage());
        }
    }

    @Test
    public void givenAVehicle_WhenNotParked_ShouldReturnFalse() {
        try {
            parkingLotSystem.park("AP 1234");
            boolean park = parkingLotSystem.isVehicleParked(null);
            Assert.assertFalse(park);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAVehicle_WhenUnParked_ShouldReturnTrue() {
        parkingLotSystem.park("AP 1234");
        parkingLotSystem.unPark(0,"AP 1234");
        boolean unPark = parkingLotSystem.isVehicleUnParked("AP 1234");
        Assert.assertTrue(unPark);
    }

    @Test
    public void givenAVehicle_WhenNotParked_AndTryingForUnPark_ShouldThrowException() {
        try {
            parkingLotSystem.unPark( 0,"AP 1234");
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle is not parked", e.getMessage());
        }
    }

    @Test
    public void givenWhenParkingLotIsFull_ShouldInformTheParkingLotOwner() {
        ParkingLotOwner parkingLotOwner = new ParkingLotOwner();
        parkingLotSystem.registerParkingLotObserver(parkingLotOwner);
        try {
            parkingLotSystem.park("AP 1234");
            parkingLotSystem.park("AP 1245");
            parkingLotSystem.park("AP 7896");
        } catch (ParkingLotException e) {
            boolean parkingFull = parkingLotOwner.isCapacityFull();
            Assert.assertTrue(parkingFull);
        }
    }

    @Test
    public void givenWhenParkingLotIsFull_ShouldInformTheAirportSecurity() {
        AirportSecurity airportSecurity = new AirportSecurity();
        parkingLotSystem.registerParkingLotObserver(airportSecurity);
        try {
            parkingLotSystem.park("AP 1234");
            parkingLotSystem.park("AP 7896");
            parkingLotSystem.park("AP 7412");
        } catch (ParkingLotException e) {
            boolean parkingFull = airportSecurity.isCapacityFull();
            Assert.assertTrue(parkingFull);
        }
    }

    @Test
    public void givenWhenParkingLotSpaceIsAvailableAfterFull_ShouldReturnTrue() {
        ParkingLotOwner parkingLotOwner = new ParkingLotOwner();
        parkingLotSystem.registerParkingLotObserver(parkingLotOwner);
        try {
            parkingLotSystem.park("AP 1234");
            parkingLotSystem.park("AP 1425");
        } catch (ParkingLotException e) {
            parkingLotSystem.unPark(0, "AP 1234");
            boolean parkingFull = parkingLotOwner.isCapacityFull();
            Assert.assertFalse(parkingFull);
        }
    }

    @Test
    public void givenVehicleWhenFindingWithCarNumber_ShouldReturnTrue() {
        parkingLotSystem.park("AP 1234");
        parkingLotSystem.park("AP 1235");
        int slotNumber = parkingLotSystem.findCarNumber("AP 1235");
        Assert.assertEquals(1, slotNumber);
    }

    @Test
    public void givenCarNumber_WhenNotFoundInParkingSlot_ShouldThrowException() {
        try {
            parkingLotSystem.park( "AP 1234");
            parkingLotSystem.findCarNumber("AP 1235");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.CAR_NUMBER_MISMATCH, e.type);
        }
    }
}