package com.example.RentCar.repository;

import com.example.RentCar.entity.Car;
import com.example.RentCar.entity.RentalRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    // Search bằng JPQL
    @Query("SELECT c FROM Car c " +
            "LEFT JOIN c.brand b " +
            "LEFT JOIN c.carType t " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Car> searchCars(@Param("keyword") String keyword);
}
