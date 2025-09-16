package com.example.RentCar.controller;

import com.example.RentCar.entity.Car;
import com.example.RentCar.entity.Rental;
import com.example.RentCar.repository.CarRepository;
import com.example.RentCar.repository.RentalRepository;
import com.example.RentCar.service.EmailService;
import com.example.RentCar.service.PaypalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
public class PaypalController {

    private final PaypalService paypalService;
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final EmailService emailService;

    public PaypalController(PaypalService paypalService,
                            RentalRepository rentalRepository,
                            CarRepository carRepository,
                            EmailService emailService) {
        this.paypalService = paypalService;
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.emailService = emailService;
    }

    /** Tạo Rental PENDING và redirect sang PayPal */
    @PostMapping("/pay")
    public String pay(@RequestParam Long carId,
                      @RequestParam String rentalType,
                      @RequestParam String pickupDateTime,
                      @RequestParam String dropoffDateTime,
                      @RequestParam String customerName,
                      @RequestParam String customerPhone,
                      @RequestParam String customerEmail) throws Exception {

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car ID không tồn tại"));

        // Tạo rental PENDING
        Rental rental = new Rental();
        rental.setCar(car);
        rental.setRentalType(rentalType);
        rental.setPickupDateTime(LocalDateTime.parse(pickupDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        rental.setDropoffDateTime(LocalDateTime.parse(dropoffDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        rental.setCustomerName(customerName);
        rental.setCustomerPhone(customerPhone);
        rental.setCustomerEmail(customerEmail);
        rental.setPaymentMethod("PayPal");
        rental.setStatus("PENDING"); // trạng thái chưa thanh toán

        rentalRepository.save(rental);

        // Tạo PayPal order, truyền rentalId làm custom invoiceId
        String approvalLink = paypalService.createOrder(
                String.format(Locale.US, "%.2f", car.getDepositAmount().doubleValue()),
                "USD",
                "http://localhost:8080/pay/success?rentalId=" + rental.getId(),
                "http://localhost:8080/pay/cancel"
        );

        return "redirect:" + approvalLink;
    }
        /** PayPal redirect khi thanh toán thành công */
    @GetMapping("/pay/success")
    public String success(@RequestParam("token") String orderId,
                          @RequestParam("rentalId") Long rentalId,
                          Model model) throws Exception {

        // Capture payment từ PayPal
        var response = paypalService.captureOrder(orderId);

        // Lấy rental theo rentalId
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental ID không tồn tại"));

        rental.setStatus("PAID"); // cập nhật trạng thái
        rental.setPaypalOrderId(orderId); // lưu orderId PayPal
        rentalRepository.save(rental);

        // Gửi email xác nhận
        try {
            emailService.sendRentalConfirmation(rental);
        } catch (Exception e) {
            System.err.println("Lỗi gửi email: " + e.getMessage());
        }

        model.addAttribute("rental", rental);
        model.addAttribute("transactionId", response.result().id());

        return "car/payment-success";
    }

    /** PayPal redirect khi khách hủy */
    @GetMapping("/pay/cancel")
    public String cancel() {
        return "car/payment-cancel";
    }
}
