package com.example.RentCar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "deposit_amount")
    private BigDecimal depositAmount;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    @Column(name = "has_driver")
    private Boolean hasDriver;

    private String status;
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    // Quan hệ với CarType
    @ManyToOne
    @JoinColumn(name = "car_type_id")
    private CarType carType;

    // Quan hệ với CarBrand
    @ManyToOne
    @JoinColumn(name= "brand_id")
    private CarBrand brand;

    // Giá thuê (RentalRate)
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentalRate> rentalRates = new ArrayList<>();

    // Tiền đặt cọc
    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private CarDeposit carDeposit;

    // Danh sách ảnh
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarImage> images = new ArrayList<>();

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public Integer getSeatNumber() { return seatNumber; }
    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }

    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }

    public Boolean getHasDriver() { return hasDriver; }
    public void setHasDriver(Boolean hasDriver) { this.hasDriver = hasDriver; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public CarType getCarType() { return carType; }
    public void setCarType(CarType carType) { this.carType = carType; }

    public CarBrand getBrand() { return brand; }
    public void setBrand(CarBrand brand) { this.brand = brand; }

    public List<RentalRate> getRentalRates() { return rentalRates; }
    public void setRentalRates(List<RentalRate> rentalRates) { this.rentalRates = rentalRates; }

    public CarDeposit getCarDeposit() { return carDeposit; }
    public void setCarDeposit(CarDeposit carDeposit) { this.carDeposit = carDeposit; }

    public List<CarImage> getImages() { return images; }
    public void setImages(List<CarImage> images) { this.images = images; }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

}


