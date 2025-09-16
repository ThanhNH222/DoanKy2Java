package com.example.RentCar.entity;

import jakarta.persistence.*;

@Entity
public class CarRentalRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rateType; // "Giờ", "Ngày", "Năm"
    private double price;
    private boolean hasDriver;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
}

