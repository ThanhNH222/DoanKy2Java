package com.example.RentCar.controller;

import com.example.RentCar.entity.Car;
import com.example.RentCar.entity.Rental;
import com.example.RentCar.repository.RentalRepository;
import com.example.RentCar.service.CarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CarController {

    private final CarService carService;
    private final RentalRepository rentalRepository;

    public CarController(CarService carService, RentalRepository rentalRepository) {
        this.carService = carService;
        this.rentalRepository = rentalRepository;
    }



    @GetMapping("/car/{id}")
    public String carDetail(@PathVariable Long id, Model model) {
        Car car = carService.getCarById(id);
        if (car == null) {
            return "404"; // nếu không tìm thấy xe thì trả về trang 404
        }
        model.addAttribute("car", car);
        return "car/car-detail"; // file car-detail.html sẽ hiển thị thông tin xe
    }


    // Tìm kiếm xe
    @GetMapping("/search")
    public String searchCars(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Car> cars = carService.searchCars(keyword);
        model.addAttribute("cars", cars);
        model.addAttribute("keyword", keyword);
        return "/car/search";
    }

    // Check trùng lịch đặt (dùng pickupDate, pickupTime như paymentController)
    @GetMapping("/car/check-availability")
    public ResponseEntity<String> checkAvailability(
            @RequestParam("carId") Long carId,
            @RequestParam("pickupDate") String pickupDate,
            @RequestParam("pickupTime") String pickupTime,
            @RequestParam("dropoffDate") String dropoffDate,
            @RequestParam("dropoffTime") String dropoffTime) {

        try {
            // Parse thời gian
            LocalDate pickupLocalDate = LocalDate.parse(pickupDate);
            LocalTime pickupLocalTime = LocalTime.parse(pickupTime, DateTimeFormatter.ofPattern("HH:mm"));

            LocalDate dropoffLocalDate = LocalDate.parse(dropoffDate);
            LocalTime dropoffLocalTime = LocalTime.parse(dropoffTime, DateTimeFormatter.ofPattern("HH:mm"));

            LocalDateTime pickupDateTime = LocalDateTime.of(pickupLocalDate, pickupLocalTime);
            LocalDateTime dropoffDateTime = LocalDateTime.of(dropoffLocalDate, dropoffLocalTime);

            // Kiểm tra trùng lịch
            List<Rental> overlaps = rentalRepository.findOverlappingRentals(carId, pickupDateTime, dropoffDateTime);
            if (!overlaps.isEmpty()) {
                return ResponseEntity.ok("unavailable");
            }
            return ResponseEntity.ok("available");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date/time format");
        }
    }
}
