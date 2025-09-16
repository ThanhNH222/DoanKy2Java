package com.example.RentCar.controller;

import com.example.RentCar.entity.Rental;
import com.example.RentCar.repository.RentalRepository;
import com.example.RentCar.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/test-email")
public class TestEmailController {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private EmailService emailService;

    // Test gửi email theo rentalId
    @GetMapping("/{rentalId}")
    public String sendTestEmail(@PathVariable Long rentalId) {
        Optional<Rental> rentalOpt = rentalRepository.findById(rentalId);

        if (rentalOpt.isEmpty()) {
            return "❌ Không tìm thấy rental với ID = " + rentalId;
        }

        Rental rental = rentalOpt.get();
        try {
            emailService.sendRentalConfirmation(rental);
            return "✅ Email test đã gửi tới: " + rental.getCustomerEmail();
        } catch (MessagingException e) {
            return "❌ Lỗi gửi email: " + e.getMessage();
        }
    }
}
