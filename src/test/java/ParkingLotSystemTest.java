import ParkingLotSystemTDD.Service.ParkingLotSystem;
import org.junit.Assert;
import org.junit.Test;

public class ParkingLotSystemTest {
    @Test
    public void givenAVehicle_WhenParked_ShouldReturnTrue() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        boolean park = parkingLotSystem.park(new Object());
        Assert.assertTrue(park);
    }
}
