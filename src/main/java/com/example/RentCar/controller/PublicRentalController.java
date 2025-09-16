package com.example.RentCar.controller;

import com.example.RentCar.entity.Rental;
import com.example.RentCar.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublicRentalController {

    @Autowired
    private RentalRepository rentalRepository;

    @GetMapping("/rental/confirm")
    public String confirmRental(@RequestParam String token, Model model) {
        Rental rental = rentalRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token không hợp lệ"));

        rental.setStatus("COMPLETED");
        rental.setConfirmationToken(null);
        rentalRepository.save(rental);

        model.addAttribute("message", "Bạn đã xác nhận nhận xe thành công!");
        return "public/confirm-success";
    }
}
