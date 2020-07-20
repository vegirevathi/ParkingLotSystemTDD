import ParkingLotSystemTDD.Exception.ParkingLotException;
import ParkingLotSystemTDD.Service.ParkingLotInfo;
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
    public void givenAVehicleOwner_ShouldReturnFullSignal_WhenVehicleLotIsFull() throws ParkingLotException {
        Object vehicle1 = new Object();
        Object vehicle2 = new Object();
        parkingLotSystem.park(vehicle1);
        parkingLotSystem.park(vehicle2);
        ParkingLotInfo display = new ParkingLotInfo(ParkingLotInfo.Person.PARKING_LOT_OWNER);
        boolean parkingFull = display.isParkingFull(parkingLotSystem);
        Assert.assertTrue(parkingFull);
    }

    @Test
    public void givenAVehicleOwner_ShouldNotReturnFullSignal_WhenVehicleLotIsNotFull() throws ParkingLotException {
        Object vehicle = new Object();
        parkingLotSystem.park(vehicle);
        ParkingLotInfo display = new ParkingLotInfo(ParkingLotInfo.Person.PARKING_LOT_OWNER);
        boolean parkingFull = display.isParkingFull(parkingLotSystem);
        Assert.assertFalse(parkingFull);
    }

    @Test
    public void givenAAirportSecurity_ShouldReturnFullSignal_WhenVehicleLotIsFull() throws ParkingLotException {
        Object vehicle1 = new Object();
        Object vehicle2 = new Object();
        parkingLotSystem.park(vehicle1);
        parkingLotSystem.park(vehicle2);
        ParkingLotInfo display = new ParkingLotInfo(ParkingLotInfo.Person.AIRPORT_SECURITY);
        boolean parkingFull = display.isParkingFull(parkingLotSystem);
        Assert.assertTrue(parkingFull);
    }

    @Test
    public void givenAAirportSecurity_ShouldNotReturnFullSignal_WhenVehicleLotIsNotFull() throws ParkingLotException {
        Object vehicle = new Object();
        parkingLotSystem.park(vehicle);
        ParkingLotInfo display = new ParkingLotInfo(ParkingLotInfo.Person.AIRPORT_SECURITY);
        boolean parkingFull = display.isParkingFull(parkingLotSystem);
        Assert.assertFalse(parkingFull);
    }

    @Test
    public void givenAVehicleOwner_ShouldTakeInFullSignal_WhenVehicleLotHasSpace() throws ParkingLotException {
        Object vehicle = new Object();
        parkingLotSystem.park(vehicle);
        ParkingLotInfo display = new ParkingLotInfo(ParkingLotInfo.Person.PARKING_LOT_OWNER);
        boolean parkingHasSpace = display.isParkingFull(parkingLotSystem);
        Assert.assertFalse(parkingHasSpace);
    }

    @Test
    public void givenAVehicleOwner_ShouldNotTakeInFullSignal_WhenVehicleLotHasNoSpace() throws ParkingLotException {
        Object vehicle1 = new Object();
        Object vehicle2 = new Object();
        parkingLotSystem.park(vehicle1);
        parkingLotSystem.park(vehicle2);
        ParkingLotInfo display = new ParkingLotInfo(ParkingLotInfo.Person.PARKING_LOT_OWNER);
        boolean parkingHasSpace = display.isParkingFull(parkingLotSystem);
        Assert.assertTrue(parkingHasSpace);
    }
}
