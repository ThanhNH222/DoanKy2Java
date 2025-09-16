package com.example.RentCar.repository;

import com.example.RentCar.entity.RentalRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRateRepository extends JpaRepository<RentalRate, Long> {

    RentalRate findByCarIdAndRateType(Long carId, RentalRate.RateType rateType);
}
