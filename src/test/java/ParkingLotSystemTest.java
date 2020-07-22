import ParkingLotSystemTDD.Exception.ParkingLotException;
import ParkingLotSystemTDD.Service.AirportSecurity;
import ParkingLotSystemTDD.Service.ParkingAttendant;
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
            parkingLotSystem.park(1, "AP 1234");
            boolean park = parkingLotSystem.isVehicleParked("AP 1234");
            Assert.assertTrue(park);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAVehicle_WhenParkingLotFull_ShouldThrowException() {
        try {
            parkingLotSystem.park(1, "AP 1237");
            parkingLotSystem.park(2, "AP 1234");
            parkingLotSystem.park(3, "AP 1238");
        } catch (ParkingLotException e) {
            Assert.assertEquals("Parking Lot is full", e.getMessage());
        }
    }

    @Test
    public void givenAVehicle_WhenAlreadyParked_ShouldThrowException() {
        try {
            parkingLotSystem.park(1, "AP 1234");
            parkingLotSystem.park(2, "AP 1234");
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle is already parked", e.getMessage());
        }
    }

    @Test
    public void givenAVehicle_WhenNotParked_ShouldReturnFalse() {
        try {
            parkingLotSystem.park(1, "AP 1234");
            boolean park = parkingLotSystem.isVehicleParked(null);
            Assert.assertFalse(park);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAVehicle_WhenUnParked_ShouldReturnTrue() throws ParkingLotException {
        parkingLotSystem.park(1, "AP 1234");
        parkingLotSystem.unPark(1, "AP 1234");
        boolean unPark = parkingLotSystem.isVehicleUnParked("AP 1234");
        Assert.assertTrue(unPark);
    }

    @Test
    public void givenAVehicle_WhenNotParked_AndTryingForUnPark_ShouldThrowException() {
        try {
            parkingLotSystem.unPark(1, "AP 1234");
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle is not parked", e.getMessage());
        }
    }

    @Test
    public void givenWhenParkingLotIsFull_ShouldInformTheParkingLotOwner() {
        ParkingLotOwner parkingLotOwner = new ParkingLotOwner();
        parkingLotSystem.registerParkingLotObserver(parkingLotOwner);
        try {
            parkingLotSystem.park(1, "AP 1234");
            parkingLotSystem.park(2, "AP 1245");
            parkingLotSystem.park(3, "AP 7896");
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
            parkingLotSystem.park(1, "AP 1234");
            parkingLotSystem.park(2, "AP 7896");
            parkingLotSystem.park(3, "AP 7412");
        } catch (ParkingLotException e) {
            boolean parkingFull = airportSecurity.isCapacityFull();
            Assert.assertTrue(parkingFull);
        }
    }

    @Test
    public void givenWhenParkingLotSpaceIsAvailableAfterFull_ShouldReturnTrue() throws ParkingLotException {
        ParkingLotOwner parkingLotOwner = new ParkingLotOwner();
        parkingLotSystem.registerParkingLotObserver(parkingLotOwner);
        try {
            parkingLotSystem.park(1, "AP 1234");
            parkingLotSystem.park(2, "AP 1425");
        } catch (ParkingLotException e) {
            parkingLotSystem.unPark(1, "AP 1234");
            boolean parkingFull = parkingLotOwner.isCapacityFull();
            Assert.assertFalse(parkingFull);
        }
    }

    @Test
    public void givenVehicleWhenParkedAtSlot_ShouldReturnTrue() {
        Object vehicle = new Object();
        ParkingAttendant parkingAttendant = new ParkingAttendant();
        try {
            parkingLotSystem.park(1, "AP 1234");
            int slotNumber = parkingAttendant.allottedSlot();
            Assert.assertEquals(1, slotNumber);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }
}