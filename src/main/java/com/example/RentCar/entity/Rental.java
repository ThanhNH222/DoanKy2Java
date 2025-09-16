package com.example.RentCar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ====== Quan hệ ======
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    // ====== Thông tin thuê xe / thanh toán ======
    private String rentalType;

    @Column(name = "pickup_location", length = 255)
    private String pickupLocation;

    @Column(name = "return_location", length = 255)
    private String returnLocation;

    @Column(name = "pickup_date_time")
    private LocalDateTime pickupDateTime;

    @Column(name = "dropoff_date_time")
    private LocalDateTime dropoffDateTime;

    @Column(name = "rental_price", precision = 10, scale = 2)
    private BigDecimal rentalPrice;

    @Column(name = "deposit_amount", precision = 10, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "shipping_fee", precision = 10, scale = 2)
    private BigDecimal shippingFee;

    @Column(name = "total_payment", precision = 10, scale = 2)
    private BigDecimal totalPayment;

    @Column(name = "insurance_fee", precision = 10, scale = 2)
    private BigDecimal insuranceFee;

    // ====== Quan hệ với Payment ======
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Payment> payments = new java.util.ArrayList<>();


    // ====== Thông tin khách hàng (nếu không dùng Customer entity) ======
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String paymentMethod;

    // ====== Trạng thái ======
    private String status;

    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    // ====== Ảnh & ghi chú ======
    @Column(name = "handover_image_url", length = 500)
    private String handoverImageUrl;

    @Column(name = "return_image_url", length = 500)
    private String returnImageUrl;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    // ====== Thời gian ======
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ====== Lifecycle ======
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Column(name = "admin_notes")
    private String adminNotes;

    // ====== Getter & Setter ======
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getRentalType() {
        return rentalType;
    }

    public void setRentalType(String rentalType) {
        this.rentalType = rentalType;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
    }

    public LocalDateTime getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(LocalDateTime pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public LocalDateTime getDropoffDateTime() {
        return dropoffDateTime;
    }

    public void setDropoffDateTime(LocalDateTime dropoffDateTime) {
        this.dropoffDateTime = dropoffDateTime;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(BigDecimal insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getHandoverImageUrl() {
        return handoverImageUrl;
    }

    public void setHandoverImageUrl(String handoverImageUrl) {
        this.handoverImageUrl = handoverImageUrl;
    }

    public String getReturnImageUrl() {
        return returnImageUrl;
    }

    public void setReturnImageUrl(String returnImageUrl) {
        this.returnImageUrl = returnImageUrl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public java.util.List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(java.util.List<Payment> payments) {
        this.payments = payments;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
    // ====== Xác nhận khách hàng ======
    @Column(name = "confirmation_token", length = 100)
    private String confirmationToken;

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public void setPaypalOrderId(String orderId) {
    }
}

