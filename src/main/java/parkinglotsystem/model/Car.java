package parkinglotsystem.model;

import parkinglotsystem.enums.CarDetails;

import java.util.Objects;

public class Car {
    private final String carNumber;
    private final CarDetails carSize;
    private final String carColour;

    public Car(String carNumber, CarDetails carDetails, String carColour) {
        this.carNumber = carNumber;
        this.carSize = carDetails;
        this.carColour = carColour;
    }

    public CarDetails getCarSize() {
        return carSize;
    }

    public String getCarColour() {
        return carColour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(carNumber, car.carNumber) &&
                carSize == car.carSize &&
                Objects.equals(carColour, car.carColour);
    }
}
