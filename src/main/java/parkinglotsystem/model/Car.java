package parkinglotsystem.model;

import parkinglotsystem.enums.CarDetails;

import java.util.Objects;

public class Car {
    private final String carNumber;
    private final CarDetails carSize;

    public Car(String carNumber, CarDetails carDetails) {
        this.carNumber = carNumber;
        this.carSize = carDetails;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public CarDetails getCarSize() {
        return carSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(carNumber, car.carNumber) &&
                carSize == car.carSize;
    }
}
