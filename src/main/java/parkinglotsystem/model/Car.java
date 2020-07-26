package parkinglotsystem.model;

import parkinglotsystem.enums.CarDetails;

import java.util.Objects;

public class Car {
    private final String carNumber;
    private final CarDetails carSize;
    private String colour;
    private String company;

    public Car(String carNumber, CarDetails carDetails) {
        this.carNumber = carNumber;
        this.carSize = carDetails;
    }

    public Car(String carNumber, CarDetails carSize, String colour, String company) {
        this.carNumber = carNumber;
        this.carSize = carSize;
        this.colour = colour;
        this.company = company;
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

    public String getColour() {
        return colour;
    }

    public String getCompany() {
        return company;
    }
}
