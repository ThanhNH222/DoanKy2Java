//package com.example.RentCar.entity;
//
//import jakarta.persistence.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "rental_rates")
//
//public class RentalRate {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//      Long id;
//
//    @Enumerated(EnumType.STRING)
//    RateType rateType;
//
//    Boolean hasDriver;
//    BigDecimal price;
//
//    @ManyToOne
//    @JoinColumn(name = "car_id")
//    Car car;
//
//    public long countOverlappingRentals(Long carId, LocalDateTime pickup, LocalDateTime dropoff) {
//        return id;
//    }
//
//    public enum RateType{
//        hour, day, month
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public RateType getRateType() {
//        return rateType;
//    }
//
//    public void setRateType(RateType rateType) {
//        this.rateType = rateType;
//    }
//
//    public Boolean getHasDriver() {
//        return hasDriver;
//    }
//
//    public void setHasDriver(Boolean hasDriver) {
//        this.hasDriver = hasDriver;
//    }
//
//    public BigDecimal getPrice() {
//        return price;
//    }
//
//    public void setPrice(BigDecimal price) {
//        this.price = price;
//    }
//
//    public Car getCar() {
//        return car;
//    }
//
//    public void setCar(Car car) {
//        this.car = car;
//    }
//}

package com.example.RentCar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rental_rates")
public class RentalRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RateType rateType;  // hour, day, month

    @Column(nullable = false)
    private Boolean hasDriver;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;   // giá thuê theo loại

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    public enum RateType {
        hour, day, month
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RateType getRateType() {
        return rateType;
    }

    public void setRateType(RateType rateType) {
        this.rateType = rateType;
    }

    public Boolean getHasDriver() {
        return hasDriver;
    }

    public void setHasDriver(Boolean hasDriver) {
        this.hasDriver = hasDriver;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}

