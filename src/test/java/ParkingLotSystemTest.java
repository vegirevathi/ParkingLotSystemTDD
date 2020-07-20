import ParkingLotSystemTDD.Exception.ParkingLotException;
import ParkingLotSystemTDD.Service.ParkingLotSystemDisplay;
import ParkingLotSystemTDD.Service.ParkingLotSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParkingLotSystemTest {
    ParkingLotSystem parkingLotSystem = null;
    Object vehicle;

    @Before
    public void setUp() throws Exception {
        parkingLotSystem = new ParkingLotSystem();
        vehicle = new Object();
        this.parkingLotSystem.setParkingLotCapacity(2);
    }

    @Test
    public void givenAVehicle_WhenParked_ShouldReturnTrue() {
        try {
            parkingLotSystem.park(vehicle);
            boolean park = parkingLotSystem.isVehicleParked(vehicle);
            Assert.assertTrue(park);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAVehicle_WhenAlreadyParked_ShouldThrowException() {
        try {
            parkingLotSystem.park(vehicle);
            parkingLotSystem.park(vehicle);
            parkingLotSystem.park(new Object());
        } catch (ParkingLotException e) {
            Assert.assertEquals("Parking Lot is full", e.getMessage());
        }
    }

    @Test
    public void givenAVehicle_WhenNotParked_ShouldReturnFalse() {
        try {
            parkingLotSystem.park(vehicle);
            boolean park = parkingLotSystem.isVehicleParked(null);
            Assert.assertFalse(park);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAVehicle_WhenUnParked_ShouldReturnTrue() throws ParkingLotException {
        parkingLotSystem.park(vehicle);
        boolean unPark = parkingLotSystem.unPark(vehicle);
        Assert.assertTrue(unPark);
    }

    @Test
    public void givenAVehicle_WhenNotParked_AndTryingForUnPark_ShouldThrowException() {
        try {
            parkingLotSystem.unPark(vehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle is not parked", e.getMessage());
        }
    }

    @Test
    public void givenAVehicleOwner_ShouldReturnFullSignal_WhenVehicleLotIsFull() throws ParkingLotException {
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle);
        ParkingLotSystemDisplay parkingLotSystemDisplay = new ParkingLotSystemDisplay();
        boolean parkingFull = parkingLotSystemDisplay.isParkingFull(ParkingLotSystemDisplay.Person.PARKINGLOTOWNER, parkingLotSystem.vehicle.size(), parkingLotSystem.parkingLotCapacity);
        Assert.assertTrue(parkingFull);
    }

    @Test
    public void givenAVehicleOwner_ShouldNotReturnFullSignal_WhenVehicleLotIsNotFull() throws ParkingLotException {
        parkingLotSystem.park(vehicle);
        ParkingLotSystemDisplay parkingLotSystemDisplay = new ParkingLotSystemDisplay();
        boolean parkingFull = parkingLotSystemDisplay.isParkingFull(ParkingLotSystemDisplay.Person.PARKINGLOTOWNER, parkingLotSystem.vehicle.size(), parkingLotSystem.parkingLotCapacity);
        Assert.assertFalse(parkingFull);
    }

    @Test
    public void givenAAirportSecurity_ShouldReturnFullSignal_WhenVehicleLotIsFull() throws ParkingLotException {
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle);
        ParkingLotSystemDisplay parkingLotSystemDisplay = new ParkingLotSystemDisplay();
        boolean parkingFull = parkingLotSystemDisplay.isParkingFull(ParkingLotSystemDisplay.Person.AIRPORTSECURITY, parkingLotSystem.vehicle.size(), parkingLotSystem.parkingLotCapacity);
        Assert.assertTrue(parkingFull);
    }

    @Test
    public void givenAAirportSecurity_ShouldNotReturnFullSignal_WhenVehicleLotIsNotFull() throws ParkingLotException {
        parkingLotSystem.park(vehicle);
        ParkingLotSystemDisplay parkingLotSystemDisplay = new ParkingLotSystemDisplay();
        boolean parkingFull = parkingLotSystemDisplay.isParkingFull(ParkingLotSystemDisplay.Person.AIRPORTSECURITY, parkingLotSystem.vehicle.size(), parkingLotSystem.parkingLotCapacity);
        Assert.assertFalse(parkingFull);
    }

    @Test
    public void givenAVehicleOwner_ShouldTakeInFullSignal_WhenVehicleLotHasSpace() throws ParkingLotException {
        parkingLotSystem.park(vehicle);
        ParkingLotSystemDisplay parkingLotSystemDisplay = new ParkingLotSystemDisplay();
        boolean parkingHasSpace = parkingLotSystemDisplay.isParkingHasSpace(ParkingLotSystemDisplay.Person.PARKINGLOTOWNER, parkingLotSystem.vehicle.size(), parkingLotSystem.parkingLotCapacity);
        Assert.assertTrue(parkingHasSpace);
    }

    @Test
    public void givenAVehicleOwner_ShouldNotTakeInFullSignal_WhenVehicleLotHasNoSpace() throws ParkingLotException {
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle);
        ParkingLotSystemDisplay parkingLotSystemDisplay = new ParkingLotSystemDisplay();
        boolean parkingHasSpace = parkingLotSystemDisplay.isParkingHasSpace(ParkingLotSystemDisplay.Person.PARKINGLOTOWNER, parkingLotSystem.vehicle.size(), parkingLotSystem.parkingLotCapacity);
        Assert.assertFalse(parkingHasSpace);
    }
}
