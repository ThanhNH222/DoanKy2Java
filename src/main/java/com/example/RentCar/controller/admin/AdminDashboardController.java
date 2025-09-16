package com.example.RentCar.controller.admin;

import com.example.RentCar.repository.CarRepository;
import com.example.RentCar.repository.RentalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    public AdminDashboardController(CarRepository carRepository, RentalRepository rentalRepository) {
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
    }

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        long carsCount = carRepository.count();
        long customersCount = rentalRepository.count();

        // TODO: sau này thêm logic cho đang thuê, doanh thu
        long rentingCars = 0;
        long revenue = 0;

        model.addAttribute("carsCount", carsCount);
        model.addAttribute("customersCount", customersCount);
        model.addAttribute("rentingCars", rentingCars);
        model.addAttribute("revenue", revenue);

        return "admin/dashboard";
    }
}
