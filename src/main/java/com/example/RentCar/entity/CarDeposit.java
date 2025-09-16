package com.example.RentCar.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;



    @Entity
    @Table(name = "car_deposits")
    public class CarDeposit {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private BigDecimal depositAmount;

        @OneToOne
        @JoinColumn(name = "car_id")
        private Car car;

        // Getters & Setters

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public BigDecimal getDepositAmount() {
            return depositAmount;
        }

        public void setDepositAmount(BigDecimal depositAmount) {
            this.depositAmount = depositAmount;
        }

        public Car getCar() {
            return car;
        }

        public void setCar(Car car) {
            this.car = car;
        }
    }

