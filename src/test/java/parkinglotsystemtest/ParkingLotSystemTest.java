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
    Car firstVehicle;
    Car secondVehicle;
    Car thirdVehicle;
    Car fourthVehicle;
    Car fifthVehicle;
    private String[] attendantName;

    @Before
    public void setUp() {
        parkingLotSystem = new ParkingLotSystem(2, "Attendant");
        attendantName = new String[]{"Ramesh", "Suresh", "Rajesh"};
        this.parkingLotSystem.setParkingLotCapacity(2);
    }

    @Test
    public void givenAVehicle_WhenParked_ShouldReturnTrue() {
        try {
            firstVehicle = new Car("AP 7896", SMALL, "RED");
            parkingLotSystem.park(firstVehicle);
            boolean park = parkingLotSystem.isVehicleParked(firstVehicle);
            Assert.assertTrue(park);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAVehicle_WhenParkingLotFull_ShouldThrowException() {
        try {
            firstVehicle = new Car("AP 7896", SMALL, "RED");
            secondVehicle = new Car("AP 7814", SMALL, "RED");
            thirdVehicle = new Car("AP 1112", SMALL, "WHITE");
            parkingLotSystem.park(firstVehicle);
            parkingLotSystem.park(secondVehicle);
            parkingLotSystem.park(thirdVehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Parking Lot is full", e.getMessage());
        }
    }

    @Test
    public void givenAVehicle_WhenAlreadyParked_ShouldThrowException() {
        try {
            firstVehicle = new Car("AP 1234", LARGE, "RED");
            parkingLotSystem.park(firstVehicle);
            parkingLotSystem.park(firstVehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle is already parked", e.getMessage());
        }
    }

    @Test
    public void givenAVehicle_WhenNotParked_ShouldReturnFalse() {
        try {
            firstVehicle = new Car("AP 1234", LARGE, "RED");
            boolean park = parkingLotSystem.isVehicleParked(firstVehicle);
            Assert.assertFalse(park);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAVehicle_WhenUnParked_ShouldReturnTrue() {
        firstVehicle = new Car("AP 1234", LARGE, "RED");
        parkingLotSystem.park(firstVehicle);
        parkingLotSystem.unPark(firstVehicle);
        boolean unPark = parkingLotSystem.isVehicleUnParked(firstVehicle);
        Assert.assertTrue(unPark);
    }

    @Test
    public void givenAVehicle_WhenNotParked_AndTryingForUnPark_ShouldThrowException() {
        try {
            firstVehicle = new Car("AP 1234", LARGE, "RED");
            parkingLotSystem.unPark(firstVehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle is not parked", e.getMessage());
        }
    }

    @Test
    public void givenParkingLot_WhenItIsFull_ShouldInformTheParkingLotOwner() {
        ParkingLotOwner parkingLotOwner = new ParkingLotOwner();
        parkingLotSystem.registerParkingLotObserver(parkingLotOwner);
        try {
            firstVehicle = new Car("AP 7896", SMALL, "RED");
            secondVehicle = new Car("AP 7814", SMALL, "RED");
            thirdVehicle = new Car("AP 1112", SMALL, "WHITE");
            parkingLotSystem.park(firstVehicle);
            parkingLotSystem.park(secondVehicle);
            parkingLotSystem.park(thirdVehicle);
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
            firstVehicle = new Car("AP 7896", SMALL, "RED");
            secondVehicle = new Car("AP 7814", SMALL, "RED");
            thirdVehicle = new Car("AP 1112", SMALL, "WHITE");
            parkingLotSystem.park(firstVehicle);
            parkingLotSystem.park(secondVehicle);
            parkingLotSystem.park(thirdVehicle);
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
            firstVehicle = new Car("AP 7896", SMALL, "RED");
            secondVehicle = new Car("AP 7814", SMALL, "RED");
            parkingLotSystem.park(firstVehicle);
            parkingLotSystem.park(secondVehicle);
        } catch (ParkingLotException e) {
            parkingLotSystem.unPark(firstVehicle);
            boolean parkingFull = parkingLotOwner.isCapacityFull();
            Assert.assertFalse(parkingFull);
        }
    }

    @Test
    public void givenVehicle_WhenFoundWithCarNumber_ShouldReturnTrue() {
        firstVehicle = new Car("AP 7896", SMALL, "RED");
        secondVehicle = new Car("AP 7814", SMALL, "RED");
        parkingLotSystem.park(firstVehicle);
        int slotNumber = parkingLotSystem.findCarNumber(firstVehicle);
        Assert.assertEquals(1, slotNumber);
    }

    @Test
    public void givenCarNumber_WhenNotFoundInParkingSlot_ShouldThrowException() {
        try {
            firstVehicle = new Car("AP 7896", SMALL, "RED");
            secondVehicle = new Car("AP 7814", SMALL, "RED");
            parkingLotSystem.park(firstVehicle);
            parkingLotSystem.findCarNumber(secondVehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.NO_SUCH_VEHICLE_PARKED, e.type);
        }
    }

    @Test
    public void givenCarWhenParkedInParkingLot_ShouldReturnParkingTime() {
        firstVehicle = new Car("AP 7896", SMALL, "RED");
        parkingLotSystem.park(firstVehicle);
        String parkedTime = parkingLotSystem.getVehicleParkedTime(firstVehicle);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        Assert.assertEquals(LocalDateTime.now().format(format), parkedTime);
    }

    @Test
    public void givenCar_WhenNotParkedInParkingLot_ShouldThrowException() {
        try {
            firstVehicle = new Car("AP 7896", SMALL, "RED");
            String parkedTime = parkingLotSystem.getVehicleParkedTime(firstVehicle);
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
            Assert.assertEquals(LocalDateTime.now().format(format), parkedTime);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.NO_SUCH_VEHICLE_PARKED, e.type);
        }
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5, attendantName);
        firstVehicle = new Car("AP 7896", SMALL, "RED");
        secondVehicle = new Car("AP 7814", SMALL, "RED");
        thirdVehicle = new Car("AP 1112", SMALL, "WHITE");
        fourthVehicle = new Car("AP 1001", LARGE, "RED");
        fifthVehicle = new Car("AP 1234", LARGE, "RED");
        parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(secondVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fifthVehicle, DriverType.NORMAL);
        String carLocation = parkingLotAllotment.getCarLocation(fifthVehicle);
        String expectedLocation = "Lot Number: 2  Slot Number: 2";
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenSameCar_WhenParkedInDifferentLotAndSlot_ShouldThrowException() {
        try {
            ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5, attendantName);
            firstVehicle = new Car("AP 7896", SMALL, "RED");
            secondVehicle = new Car("AP 7814", SMALL, "RED");
            thirdVehicle = new Car("AP 1112", SMALL, "WHITE");
            fourthVehicle = new Car("AP 1001", LARGE, "RED");
            fifthVehicle = new Car("AP 1234", LARGE, "RED");
            parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(secondVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.ALREADY_PARKED, e.type);
        }
    }

    @Test
    public void givenMultipleParkingLots_WhenAllSlotsAreFilled_ShouldThrowException() {
        try {
            ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(2, 2, attendantName);
            firstVehicle = new Car("AP 7896", SMALL, "RED");
            secondVehicle = new Car("AP 7814", SMALL, "RED");
            thirdVehicle = new Car("AP 1112", SMALL, "WHITE");
            fourthVehicle = new Car("AP 1001", LARGE, "RED");
            fifthVehicle = new Car("AP 1234", LARGE, "RED");
            parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(secondVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(fifthVehicle, DriverType.NORMAL);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.PARKING_LOT_FULL, e.type);
        }
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation_ForHandicappedDriver() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5, attendantName);
        firstVehicle = new Car("AP 7896", SMALL, "RED");
        secondVehicle = new Car("AP 7814", SMALL, "RED");
        thirdVehicle = new Car("AP 1112", SMALL, "WHITE");
        fourthVehicle = new Car("AP 1001", LARGE, "RED");
        fifthVehicle = new Car("AP 1234", LARGE, "RED");
        parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(secondVehicle, DriverType.HANDICAPPED);
        parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.HANDICAPPED);
        parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fifthVehicle, DriverType.HANDICAPPED);
        String carLocation = parkingLotAllotment.getCarLocation(fifthVehicle);
        String expectedLocation = "Lot Number: 1  Slot Number: 4";
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation_ForLargeVehicles() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5, attendantName);
        firstVehicle = new Car("AP 7896", SMALL, "RED");
        secondVehicle = new Car("AP 7814", SMALL, "RED");
        thirdVehicle = new Car("AP 1112", SMALL, "WHITE");
        fourthVehicle = new Car("AP 1001", LARGE, "RED");
        fifthVehicle = new Car("AP 1234", LARGE, "RED");
        parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(secondVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fifthVehicle, DriverType.NORMAL);
        String carLocation1 = parkingLotAllotment.getCarLocation(fourthVehicle);
        String carLocation2 = parkingLotAllotment.getCarLocation(fifthVehicle);
        String expectedLocation1 = "Lot Number: 1  Slot Number: 2";
        String expectedLocation2 = "Lot Number: 2  Slot Number: 2";
        Assert.assertEquals(expectedLocation1, carLocation1);
        Assert.assertEquals(expectedLocation2, carLocation2);
    }

    @Test
    public void givenCar_WhenParkedInProvidedLotAndSlot_ShouldReturnCarLocation_ForLargeVehicles_Handicapped() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5, attendantName);
        firstVehicle = new Car("AP 7896", SMALL, "RED");
        secondVehicle = new Car("AP 7814", LARGE, "RED");
        thirdVehicle = new Car("AP 1112", SMALL, "WHITE");
        fourthVehicle = new Car("AP 1001", LARGE, "RED");
        fifthVehicle = new Car("AP 1234", LARGE, "RED");
        parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(secondVehicle, DriverType.HANDICAPPED);
        parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fifthVehicle, DriverType.HANDICAPPED);
        String carLocation1 = parkingLotAllotment.getCarLocation(fifthVehicle);
        String carLocation2 = parkingLotAllotment.getCarLocation(secondVehicle);
        String expectedLocation1 = "Lot Number: 1  Slot Number: 3";
        String expectedLocation2 = "Lot Number: 1  Slot Number: 2";
        Assert.assertEquals(expectedLocation1, carLocation1);
        Assert.assertEquals(expectedLocation2, carLocation2);
    }

    @Test
    public void givenCars_WhenParkedInSlotsAndLots_ShouldReturnListOfVehicles_WhenSearchedForColour() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5, attendantName);
        firstVehicle = new Car("AP 7896", SMALL, "WHITE");
        secondVehicle = new Car("AP 7814", SMALL, "RED");
        thirdVehicle = new Car("AP 1112", SMALL, "RED");
        fourthVehicle = new Car("AP 1001", LARGE, "RED");
        fifthVehicle = new Car("AP 1234", LARGE, "RED");
        parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(secondVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fifthVehicle, DriverType.NORMAL);
        List<String> carLocation = parkingLotAllotment.getCarLocationBasedOnColour("WHITE");
        List<String> expectedLocation = Arrays.asList(parkingLotAllotment.getCarLocation(firstVehicle));
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenCars_WhenParkedInSlotsAndLots_ShouldThrowException_WhenSearchedColourNotFound() {
        try {
            ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5, attendantName);
            firstVehicle = new Car("AP 7896", SMALL, "RED");
            secondVehicle = new Car("AP 7814", SMALL, "RED");
            thirdVehicle = new Car("AP 1112", SMALL, "RED");
            fourthVehicle = new Car("AP 1001", LARGE, "RED");
            fifthVehicle = new Car("AP 1234", LARGE, "RED");
            parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(secondVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(fifthVehicle, DriverType.NORMAL);
            parkingLotAllotment.getCarLocationBasedOnColour("WHITE");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.NO_SUCH_VEHICLE_PARKED, e.type);
        }
    }

    @Test
    public void givenCars_WhenParkedInSlotsAndLots_ShouldReturnListOfVehicles_WhenSearchedForColourAndCompany() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5, attendantName);
        firstVehicle = new Car("AP 7896", SMALL, "BLACK", "TOYOTA");
        secondVehicle = new Car("AP 7814", SMALL, "RED", "TOYOTA");
        thirdVehicle = new Car("AP 1112", SMALL, "RED", "TOYOTA");
        fourthVehicle = new Car("AP 1001", LARGE, "BLUE", "TOYOTA");
        fifthVehicle = new Car("AP 1234", LARGE, "RED", "TOYOTA");
        parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(secondVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fifthVehicle, DriverType.NORMAL);
        List<String> carLocation = parkingLotAllotment.getCarLocationBasedOnColourAndCompany("BLUE", "Toyota");
        List<String> expectedLocation = Arrays.asList("( Lot Number: 1, Slot Number: 2," +
                " Car Number: AP 1001, Attendant Name: Ramesh )");
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenCars_WhenParkedInSlotsAndLots_ShouldThrowException_WhenSearchedColourAndCompanyNotFound() {
        try {
            ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5, attendantName);
            firstVehicle = new Car("AP 7896", SMALL, "BLACK", "TOYOTA");
            secondVehicle = new Car("AP 7814", SMALL, "RED", "TOYOTA");
            thirdVehicle = new Car("AP 1112", SMALL, "RED", "TOYOTA");
            fourthVehicle = new Car("AP 1001", LARGE, "BLUE", "TOYOTA");
            fifthVehicle = new Car("AP 1234", LARGE, "RED", "TOYOTA");
            parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(secondVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(fifthVehicle, DriverType.NORMAL);
            parkingLotAllotment.getCarLocationBasedOnColourAndCompany("BLUE", "TOYOTA");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.NO_SUCH_VEHICLE_PARKED, e.type);
        }
    }

    @Test
    public void givenCars_WhenParkedInSlotsAndLots_ShouldReturnListOfVehicles_WhenSearchedForCompany() {
        ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5, attendantName);
        firstVehicle = new Car("AP 7896", SMALL, "BLACK", "TOYOTA");
        secondVehicle = new Car("AP 7814", SMALL, "RED", "TOYOTA");
        thirdVehicle = new Car("AP 1112", SMALL, "RED", "TOYOTA");
        fourthVehicle = new Car("AP 1001", LARGE, "BLUE", "BMW");
        fifthVehicle = new Car("AP 1234", LARGE, "RED", "TOYOTA");
        parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(secondVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
        parkingLotAllotment.parkVehicle(fifthVehicle, DriverType.NORMAL);
        List<String> carLocation = parkingLotAllotment.getCarLocationBasedOnCompany("BMW");
        List<String> expectedLocation = Arrays.asList(parkingLotAllotment.getCarLocation(fourthVehicle));
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenCars_WhenParkedInSlotsAndLots_ShouldThrowException_WhenSearchedCompanyNotFound() {
        try {
            ParkingLotAllotment parkingLotAllotment = new ParkingLotAllotment(3, 5, attendantName);
            firstVehicle = new Car("AP 7896", SMALL, "BLACK", "TOYOTA");
            secondVehicle = new Car("AP 7814", SMALL, "RED", "TOYOTA");
            thirdVehicle = new Car("AP 1112", SMALL, "RED", "TOYOTA");
            fourthVehicle = new Car("AP 1001", LARGE, "BLUE", "TOYOTA");
            fifthVehicle = new Car("AP 1234", LARGE, "RED", "TOYOTA");
            parkingLotAllotment.parkVehicle(firstVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(secondVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(thirdVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(fourthVehicle, DriverType.NORMAL);
            parkingLotAllotment.parkVehicle(fifthVehicle, DriverType.NORMAL);
            parkingLotAllotment.getCarLocationBasedOnCompany("BMW");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.e.NO_SUCH_VEHICLE_PARKED, e.type);
        }
    }
}