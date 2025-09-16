package com.example.RentCar.repository;

import com.example.RentCar.entity.RentalImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalImageRepository extends JpaRepository<RentalImage, Long> {
    // Lấy danh sách ảnh theo rentalId
    List<RentalImage> findByRentalId(Long rentalId);

    // Nếu bạn muốn lấy ảnh theo rentalId và loại ảnh
    List<RentalImage> findByRentalIdAndImageType(Long rentalId, com.example.RentCar.entity.ImageType imageType);
}
