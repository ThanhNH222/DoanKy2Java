package com.example.RentCar.service;

import com.example.RentCar.entity.Car;
import com.example.RentCar.repository.CarRepository;
import com.example.RentCar.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    public CarService(CarRepository carRepository, RentalRepository rentalRepository) {
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
    }

    // 📌 Lấy tất cả xe
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // 📌 Lấy chi tiết xe theo ID
    public Car getCarById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    // 📌 Thêm mới xe
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    // 📌 Cập nhật xe
    public Car updateCar(Long id, Car carDetails) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với ID: " + id));

        car.setName(carDetails.getName());
        car.setLicensePlate(carDetails.getLicensePlate());
        car.setSeatNumber(carDetails.getSeatNumber());
        car.setPricePerDay(carDetails.getPricePerDay()); // ✅ sửa cho khớp
        car.setHasDriver(carDetails.getHasDriver());
        car.setStatus(carDetails.getStatus());
        car.setDescription(carDetails.getDescription());
        car.setImageUrl(carDetails.getImageUrl());
        car.setCarType(carDetails.getCarType());
        car.setBrand(carDetails.getBrand());

        return carRepository.save(car);
    }

    // 📌 Xóa xe
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    // 📌 Tìm kiếm xe
    public List<Car> searchCars(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return carRepository.findAll();
        }
        return carRepository.searchCars(keyword);
    }

    // 📌 Kiểm tra xem xe có bị trùng lịch đặt không
    public boolean isCarAvailable(Long carId, LocalDateTime pickup, LocalDateTime dropoff) {
        return rentalRepository.findOverlappingRentals(carId, pickup, dropoff).isEmpty();
    }
}
