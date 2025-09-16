package com.example.RentCar.repository;

import com.example.RentCar.entity.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarTypeRepository extends JpaRepository<CarType, Long> {
    Optional<CarType> findByName(String name);
}
