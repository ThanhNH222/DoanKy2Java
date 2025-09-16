package com.example.RentCar.repository;

import com.example.RentCar.entity.Store;  // import đúng entity Store
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
