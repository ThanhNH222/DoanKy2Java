package com.example.RentCar.repository;

import com.example.RentCar.entity.Payment;
import com.example.RentCar.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Tìm tất cả thanh toán của 1 xe
    List<Payment> findByRental_Car_Id(Long carId);

    // Tìm tất cả thanh toán của khách theo email
    List<Payment> findByRental_CustomerEmail(String email);

    // Tìm tất cả thanh toán theo hình thức
    List<Payment> findByRental_PaymentMethod(String paymentMethod);

    Payment findByRentalId(Long rentalId);

    Payment findByRental(Rental rental);
}
