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
    public void givenParkingLot_WhenItIsFull_ShouldInformTheParkingLotOwner() {
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
    public void givenParkingLot_WhenItIsFull_ShouldInformTheAirportSecurity() {
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
    public void givenParkingLot_WhenSpaceIsAvailableAfterFull_ShouldReturnTrue() {
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
    public void givenCarNumber_WhenFoundInParkingSlot_ShouldReturnTrue() {
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
    public void givenCar_WhenParkedInParkingLot_ShouldReturnParkingTime() {
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
        parkingLotAllotment.parkVehicle(new Car("AP 1234", LARGE), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1456", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1123", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 7896", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1246", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 7824", LARGE), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 7524", LARGE), DriverType.NORMAL );
        String carLocation = parkingLotAllotment.getCarLocation(new Car("AP 7524", LARGE));
        String expectedLocation = "Lot Number: 0  Slot Number: 2";
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenSameCar_WhenParkedInDifferentLotAndSlot_ShouldThrowException() {
        try {
            ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
            parkingLotAllotment.parkVehicle(new Car("AP 1234", SMALL), DriverType.NORMAL );
            parkingLotAllotment.parkVehicle(new Car("AP 1234", SMALL), DriverType.NORMAL );
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.ALREADY_PARKED, e.type);
        }
    }

    @Test
    public void givenMultipleParkingLots_WhenAllSlotsAreFilled_ShouldThrowException() {
        try {
            ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(1, 1);
            parkingLotAllotment.parkVehicle(new Car("AP 1234", SMALL), DriverType.NORMAL );
            parkingLotAllotment.parkVehicle(new Car("AP 1237", SMALL), DriverType.NORMAL );
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.PARKING_LOT_FULL, e.type);
        }
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation_ForHandicappedDriver() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
        parkingLotAllotment.parkVehicle(new Car("AP 1235", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1789", SMALL), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1237", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1247", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1268", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1234", SMALL), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1001", SMALL), DriverType.HANDICAPPED );
        String carLocation = parkingLotAllotment.getCarLocation(new Car("AP 1001", SMALL));
        String expectedLocation = "Lot Number: 0  Slot Number: 3";
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation_ForLargeVehicles() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
        parkingLotAllotment.parkVehicle(new Car("AP 1235", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1789", SMALL), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1237", LARGE), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1247", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1268", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1234", SMALL), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1001", SMALL), DriverType.HANDICAPPED );
        String carLocation1 = parkingLotAllotment.getCarLocation(new Car("AP 1237", LARGE));
        String carLocation2 = parkingLotAllotment.getCarLocation(new Car("AP 1247", SMALL));
        String expectedLocation1 = "Lot Number: 1  Slot Number: 0";
        String expectedLocation2 = "Lot Number: 2  Slot Number: 0";
        Assert.assertEquals(expectedLocation1, carLocation1);
        Assert.assertEquals(expectedLocation2, carLocation2);
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation_ForLargeVehicles_Handicapped() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5);
        parkingLotAllotment.parkVehicle(new Car("AP 1235", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1789", SMALL), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1237", LARGE), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1247", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1268", SMALL), DriverType.NORMAL );
        parkingLotAllotment.parkVehicle(new Car("AP 1234", LARGE), DriverType.HANDICAPPED );
        parkingLotAllotment.parkVehicle(new Car("AP 1001", SMALL), DriverType.HANDICAPPED );
        String carLocation1 = parkingLotAllotment.getCarLocation(new Car("AP 1234", LARGE));
        String carLocation2 = parkingLotAllotment.getCarLocation(new Car("AP 1001", SMALL));
        String expectedLocation1 = "Lot Number: 0  Slot Number: 2";
        String expectedLocation2 = "Lot Number: 0  Slot Number: 3";
        Assert.assertEquals(expectedLocation1, carLocation1);
        Assert.assertEquals(expectedLocation2, carLocation2);
    }
}