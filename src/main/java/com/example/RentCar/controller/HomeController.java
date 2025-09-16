package com.example.RentCar.controller;

import com.example.RentCar.entity.Car;
import com.example.RentCar.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {



    private final CarService carService;

    public HomeController(CarService carService) {
        this.carService = carService;
    }


    // Trang chủ
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("content", "home");
        model.addAttribute("title", "Trang chủ");
        return "layout/main";
    }



    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("content", "about");
        model.addAttribute("title", "Về chúng tôi");
        return "about";
    }

    @GetMapping("/service")
    public String service(Model model) {
        return "service";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        return "blog";
    }

    @GetMapping("/feature")
    public String feature(Model model) {
        return "feature";
    }

    @GetMapping("/cars")
    public String cars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "cars";
    }


    @GetMapping("/team")
    public String team(Model model) {
        return "team";
    }

    @GetMapping("/testimonial")
    public String testimonial(Model model) {
        return "testimonial";
    }

    @GetMapping("/page")
    public String page(Model model) {
        return "page";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        return "contact";
    }
}
