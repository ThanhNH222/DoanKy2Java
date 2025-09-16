package com.example.RentCar.repository;

import com.example.RentCar.entity.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarImageRepository extends JpaRepository<CarImage, Long> {
    List<CarImage> findByCarId(Long carId);
}
