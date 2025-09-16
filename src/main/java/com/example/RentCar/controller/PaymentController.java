package com.example.RentCar.controller;

import com.example.RentCar.entity.*;
import com.example.RentCar.repository.*;
import com.example.RentCar.service.EmailService;
import com.example.RentCar.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Controller
public class PaymentController {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private RentalRateRepository rentalRateRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PaypalService paypalService;

    private static final double SHIPPING_FEE = 22222;

    /** Hiển thị trang thanh toán */
    @PostMapping("/payment")
    public String paymentPage(@RequestParam Long carId,
                              @RequestParam String pickupDate,
                              @RequestParam String pickupTime,
                              @RequestParam String dropoffDate,
                              @RequestParam String dropoffTime,
                              @RequestParam String rentalType,
                              @RequestParam(required = false) String customerName,
                              @RequestParam(required = false) String customerPhone,
                              @RequestParam(required = false) String customerEmail,
                              Model model) {

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        LocalDateTime pickupDateTime = LocalDateTime.parse(pickupDate + "T" + pickupTime);
        LocalDateTime dropoffDateTime = LocalDateTime.parse(dropoffDate + "T" + dropoffTime);

        RentalRate.RateType rateTypeEnum = RentalRate.RateType.valueOf(rentalType.toLowerCase());
        RentalRate rate = rentalRateRepository.findByCarIdAndRateType(carId, rateTypeEnum);
        if (rate == null) throw new RuntimeException("Không tìm thấy hình thức thuê này");

        long rentalUnits = calculateRentalUnits(pickupDateTime, dropoffDateTime, rateTypeEnum);
        BigDecimal rentalPrice = rate.getPrice().multiply(BigDecimal.valueOf(rentalUnits));
        BigDecimal depositAmount = car.getDepositAmount();
        BigDecimal shippingFee = BigDecimal.valueOf(SHIPPING_FEE);
        BigDecimal totalPayment = depositAmount.add(shippingFee);

        model.addAttribute("car", car);
        model.addAttribute("customerName", customerName);
        model.addAttribute("customerPhone", customerPhone);
        model.addAttribute("customerEmail", customerEmail);
        model.addAttribute("rentalType", rentalType);
        model.addAttribute("pickupDateTime", pickupDateTime);
        model.addAttribute("dropoffDateTime", dropoffDateTime);
        model.addAttribute("rentalPrice", rentalPrice);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("depositAmount", depositAmount);
        model.addAttribute("totalPayment", totalPayment);

        return "car/payment";
    }

    /** Xử lý thanh toán PayPal */
    @GetMapping("/pay")
    public String pay(@RequestParam BigDecimal amount,
                      @RequestParam String currency,
                      @RequestParam Long carId,
                      @RequestParam String rentalType,
                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime pickupDateTime,
                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dropoffDateTime,
                      @RequestParam String customerName,
                      @RequestParam String customerPhone,
                      @RequestParam String customerEmail) throws Exception {

        // Tạo rental và lưu vào DB
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Xe không tồn tại"));

        RentalRate.RateType rateTypeEnum = RentalRate.RateType.valueOf(rentalType.toLowerCase());
        RentalRate rate = rentalRateRepository.findByCarIdAndRateType(carId, rateTypeEnum);
        long rentalUnits = calculateRentalUnits(pickupDateTime, dropoffDateTime, rateTypeEnum);
        BigDecimal rentalPrice = rate.getPrice().multiply(BigDecimal.valueOf(rentalUnits));

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setRentalType(rentalType);
        rental.setPickupDateTime(pickupDateTime);
        rental.setDropoffDateTime(dropoffDateTime);
        rental.setCustomerName(customerName);
        rental.setCustomerPhone(customerPhone);
        rental.setCustomerEmail(customerEmail);
        rental.setDepositAmount(car.getDepositAmount());
        rental.setShippingFee(BigDecimal.valueOf(SHIPPING_FEE));
        rental.setRentalPrice(rentalPrice);
        rental.setTotalPayment(car.getDepositAmount().add(BigDecimal.valueOf(SHIPPING_FEE)));

        rentalRepository.save(rental);

        // Tạo PayPal order
        String approvalLink = paypalService.createOrder(
                String.format(Locale.US, "%.2f", amount.doubleValue()),
                currency,
                "http://localhost:8080/pay/success?rentalId=" + rental.getId(),
                "http://localhost:8080/pay/cancel"
        );

        // Gửi mail xác nhận
        try {
            emailService.sendRentalConfirmation(rental);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:" + approvalLink;
    }

    private long calculateRentalUnits(LocalDateTime pickup, LocalDateTime dropoff, RentalRate.RateType rateType) {
        Duration duration = Duration.between(pickup, dropoff);
        switch (rateType) {
            case hour:
                return duration.toHours();
            case day:
                return duration.toDays();
            case month:
                return ChronoUnit.MONTHS.between(pickup.toLocalDate(), dropoff.toLocalDate());
            default:
                throw new IllegalArgumentException("Không hỗ trợ loại hình thuê: " + rateType);
        }
    }
}
