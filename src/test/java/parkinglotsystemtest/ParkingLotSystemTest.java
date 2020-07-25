package parkinglotsystemtest;

import parkinglotsystem.exception.ParkingLotException;
import parkinglotsystem.observer.AirportSecurity;
import parkinglotsystem.observer.ParkingLotOwner;
import parkinglotsystem.service.DriverType;
import parkinglotsystem.service.ParkingLotAllotment;
import parkinglotsystem.service.ParkingLotSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        parkingLotAllotment.parkVehicle("AP 1234", DriverType.NORMAL );
        parkingLotAllotment.parkVehicle("AP 1456", DriverType.NORMAL );
        parkingLotAllotment.parkVehicle("AP 1237", DriverType.NORMAL );
        parkingLotAllotment.parkVehicle("AP 1238", DriverType.NORMAL );
        parkingLotAllotment.parkVehicle("AP 1231", DriverType.NORMAL );
        parkingLotAllotment.parkVehicle("AP 1230", DriverType.NORMAL );
        parkingLotAllotment.parkVehicle("AP 1233", DriverType.NORMAL );
        String carLocation = parkingLotAllotment.getCarLocation("AP 1233");
        String expectedLocation = "Lot Number: 0  Slot Number: 2";
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenSameCar_WhenParkedInDifferentLotAndSlot_ShouldThrowException() {
        try {
            ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
            parkingLotAllotment.parkVehicle("AP 1234", DriverType.NORMAL );
            parkingLotAllotment.parkVehicle("AP 1234", DriverType.NORMAL );
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.ALREADY_PARKED, e.type);
        }
    }

    @Test
    public void givenMultipleParkingLots_WhenAllSlotsAreFilled_ShouldThrowException() {
        try {
            ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(1, 1);
            parkingLotAllotment.parkVehicle("AP 1234", DriverType.NORMAL );
            parkingLotAllotment.parkVehicle("AP 1235", DriverType.NORMAL );
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.PARKING_LOT_FULL, e.type);
        }
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation_ForHandicappedDriver() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
        parkingLotAllotment.parkVehicle("AP 1234", DriverType.NORMAL );
        parkingLotAllotment.parkVehicle("AP 1456", DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle("AP 1237", DriverType.NORMAL );
        parkingLotAllotment.parkVehicle("AP 1238", DriverType.NORMAL );
        parkingLotAllotment.parkVehicle("AP 1231", DriverType.NORMAL );
        parkingLotAllotment.parkVehicle("AP 1230", DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle("AP 1233", DriverType.NORMAL );
        String carLocation = parkingLotAllotment.getCarLocation("AP 1233");
        String expectedLocation = "Lot Number: 2  Slot Number: 1";
        Assert.assertEquals(expectedLocation, carLocation);
    }
}