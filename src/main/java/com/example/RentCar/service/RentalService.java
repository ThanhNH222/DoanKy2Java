package com.example.RentCar.service;

import com.example.RentCar.entity.Rental;
import com.example.RentCar.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    // 📌 Lấy tất cả hợp đồng thuê xe
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    // 📌 Lấy hợp đồng thuê xe theo ID
    public Rental getRentalById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy hợp đồng với ID = " + id));
    }

    // 📌 Thêm hợp đồng mới (có kiểm tra trùng lịch)
    public Rental createRental(Rental rental) {
        List<Rental> overlapping = rentalRepository.findOverlappingRentals(
                rental.getCar().getId(),
                rental.getPickupDateTime(),
                rental.getDropoffDateTime()
        );

        if (!overlapping.isEmpty()) {
            throw new RuntimeException("🚫 Xe đã có người thuê trong khoảng thời gian này!");
        }

        rental.setStatus("DA_THUE"); // mặc định khi tạo mới
        rental.setCreatedAt(LocalDateTime.now());

        return rentalRepository.save(rental);
    }

    // 📌 Cập nhật hợp đồng
    public Rental saveRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    // 📌 Xóa hợp đồng
    public void deleteRental(Long id) {
        rentalRepository.deleteById(id);
    }

    // 📌 Cập nhật trạng thái thuê xe
    public void updateStatus(Long rentalId, String status) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy hợp đồng"));

        rental.setStatus(status);
        rentalRepository.save(rental);
    }
}
