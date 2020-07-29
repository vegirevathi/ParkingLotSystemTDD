package parkinglotsystem.model;

import parkinglotsystem.enums.CarDetails;
import parkinglotsystem.enums.DriverType;

import java.util.Objects;

public class Car {
    private final String carNumber;
    private final CarDetails carSize;
    private final String carColour;
    private DriverType driverType;
    private String carCompany;

    public Car(String carNumber, CarDetails carDetails, String carColour) {
        this.carNumber = carNumber;
        this.carSize = carDetails;
        this.carColour = carColour;
    }

    public Car(String carNumber, CarDetails carDetails, String carColour, String carCompany) {
        this.carNumber = carNumber;
        this.carSize = carDetails;
        this.carColour = carColour;
        this.carCompany = carCompany;
    }

    public Car(String carNumber, CarDetails carDetails, String carColour, String carCompany, DriverType driverType) {
        this.carNumber = carNumber;
        this.carSize = carDetails;
        this.carColour = carColour;
        this.carCompany = carCompany;
        this.driverType = driverType;
    }

    public CarDetails getCarSize() {
        return carSize;
    }

    public String getCarColour() {
        return carColour;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getCarCompany() {
        return carCompany;
    }

    public DriverType getDriverType() {
        return driverType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(carNumber, car.carNumber) &&
                carSize == car.carSize &&
                Objects.equals(carColour, car.carColour) &&
                driverType == car.driverType &&
                Objects.equals(carCompany, car.carCompany);
    }
}
