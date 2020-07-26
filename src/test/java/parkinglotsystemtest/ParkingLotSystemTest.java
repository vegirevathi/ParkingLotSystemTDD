package parkinglotsystemtest;

import parkinglotsystem.exception.ParkingLotException;
import parkinglotsystem.model.Car;
import parkinglotsystem.observer.AirportSecurity;
import parkinglotsystem.observer.ParkingLotOwner;
import parkinglotsystem.enums.DriverType;
import parkinglotsystem.service.ParkingLotAllotment;
import parkinglotsystem.service.ParkingLotSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static parkinglotsystem.enums.CarDetails.LARGE;
import static parkinglotsystem.enums.CarDetails.SMALL;

public class ParkingLotSystemTest {
    ParkingLotSystem parkingLotSystem = null;

    @Before
    public void setUp() {
        parkingLotSystem = new ParkingLotSystem(2);
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
        parkingLotSystem.unPark(0, "AP 1234");
        boolean unPark = parkingLotSystem.isVehicleUnParked("AP 1234");
        Assert.assertTrue(unPark);
    }

    @Test
    public void givenAVehicle_WhenNotParked_AndTryingForUnPark_ShouldThrowException() {
        try {
            parkingLotSystem.unPark(0, "AP 1234");
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
            parkingLotSystem.park("AP 1234");
            parkingLotSystem.findCarNumber("AP 1235");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.NO_SUCH_VEHICLE_PARKED, e.type);
        }
    }

    @Test
    public void givenCarWhenParkedInParkingLot_ShouldReturnParkingTime() {
        parkingLotSystem.park("AP 1234");
        String parkedTime = parkingLotSystem.getVehicleParkedTime("AP 1234");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        Assert.assertEquals(LocalDateTime.now().format(format), parkedTime);
    }

    @Test
    public void givenCar_WhenNotParkedInParkingLot_ShouldThrowException() {
        try {
            String parkedTime = parkingLotSystem.getVehicleParkedTime("AP 1234");
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
            Assert.assertEquals(LocalDateTime.now().format(format), parkedTime);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.NO_SUCH_VEHICLE_PARKED, e.type);
        }
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
        parkingLotAllotment.parkVehicle(new Car("AP 1234", LARGE, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1456", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1123", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 7896", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1246", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 7824", LARGE, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 7524", LARGE, "RED"), DriverType.NORMAL );
        String carLocation = parkingLotAllotment.getCarLocation(new Car("AP 7524", LARGE, "RED"));
        String expectedLocation = "Lot Number: 0  Slot Number: 2";
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenSameCar_WhenParkedInDifferentLotAndSlot_ShouldThrowException() {
        try {
            ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
            parkingLotAllotment.parkVehicle(new Car("AP 1234", SMALL, "BLACK"), DriverType.NORMAL );
            parkingLotAllotment.parkVehicle(new Car("AP 1234", SMALL, "BLACK"), DriverType.NORMAL );
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.ALREADY_PARKED, e.type);
        }
    }

    @Test
    public void givenMultipleParkingLots_WhenAllSlotsAreFilled_ShouldThrowException() {
        try {
            ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(1, 1);
            parkingLotAllotment.parkVehicle(new Car("AP 1234", SMALL, "BLACK"), DriverType.NORMAL );
            parkingLotAllotment.parkVehicle(new Car("AP 1237", SMALL, "RED"), DriverType.NORMAL );
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.PARKING_LOT_FULL, e.type);
        }
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation_ForHandicappedDriver() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
        parkingLotAllotment.parkVehicle(new Car("AP 1235", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1789", SMALL, "RED"), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1237", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1247", SMALL, "BLACK"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1268", SMALL, "YELLOW"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1234", SMALL, "BLUE"), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1001", SMALL, "RED"), DriverType.HANDICAPPED );
        String carLocation = parkingLotAllotment.getCarLocation(new Car("AP 1001", SMALL, "RED"));
        String expectedLocation = "Lot Number: 0  Slot Number: 3";
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation_ForLargeVehicles() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
        parkingLotAllotment.parkVehicle(new Car("AP 1235", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1789", SMALL, "RED"), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1237", LARGE, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1247", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1268", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1234", SMALL, "RED"), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1001", SMALL, "RED"), DriverType.HANDICAPPED );
        String carLocation1 = parkingLotAllotment.getCarLocation(new Car("AP 1237", LARGE, "RED"));
        String carLocation2 = parkingLotAllotment.getCarLocation(new Car("AP 1247", SMALL, "RED"));
        String expectedLocation1 = "Lot Number: 1  Slot Number: 0";
        String expectedLocation2 = "Lot Number: 2  Slot Number: 0";
        Assert.assertEquals(expectedLocation1, carLocation1);
        Assert.assertEquals(expectedLocation2, carLocation2);
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation_ForLargeVehicles_Handicapped() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
        parkingLotAllotment.parkVehicle(new Car("AP 1235", SMALL, "BLACK"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1789", SMALL, "BLACK"), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1237", LARGE, "BLACK"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1247", SMALL, "BLACK"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1268", SMALL, "BLACK"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1234", LARGE, "BLACK"), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1001", SMALL, "BLACK"), DriverType.HANDICAPPED );
        String carLocation1 = parkingLotAllotment.getCarLocation(new Car("AP 1234", LARGE, "BLACK"));
        String carLocation2 = parkingLotAllotment.getCarLocation(new Car("AP 1001", SMALL, "BLACK"));
        String expectedLocation1 = "Lot Number: 0  Slot Number: 2";
        String expectedLocation2 = "Lot Number: 0  Slot Number: 3";
        Assert.assertEquals(expectedLocation1, carLocation1);
        Assert.assertEquals(expectedLocation2, carLocation2);
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation_ForWhiteCars() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
        parkingLotAllotment.parkVehicle(new Car("AP 1234", LARGE, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1456", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1123", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 7896", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1246", SMALL, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 7824", LARGE, "RED"), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 7524", LARGE, "WHITE"), DriverType.NORMAL );
        List<String> carLocation = parkingLotAllotment.getCarLocationBasedOnColour("WHITE");
        List<String> expectedLocation = Arrays.asList(parkingLotAllotment.getCarLocation(new Car("AP 7524", LARGE, "WHITE")));
        Assert.assertEquals(expectedLocation, carLocation);
    }
}