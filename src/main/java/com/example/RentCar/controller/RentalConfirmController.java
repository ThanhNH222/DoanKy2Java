package com.example.RentCar.controller;

import com.example.RentCar.entity.Rental;
import com.example.RentCar.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rentals")
public class RentalConfirmController {

    @Autowired
    private RentalRepository rentalRepository;

    /** ---------------- Khách xác nhận đã nhận xe ---------------- */
    @GetMapping("/confirm")
    @ResponseBody
    public String confirmRental(@RequestParam("token") String token) {
        Rental rental = rentalRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token không hợp lệ hoặc đã được xác nhận"));

        rental.setStatus("DELIVERED");
        rental.setConfirmationToken(null); // xoá token để tránh xác nhận lại
        rentalRepository.save(rental);

        return "✅ Bạn đã xác nhận nhận xe thành công!";
    }
}
