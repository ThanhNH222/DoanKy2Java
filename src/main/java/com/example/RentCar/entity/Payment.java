package com.example.RentCar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Gắn với hợp đồng thuê
    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    // Số tiền hoàn trả khách (nếu cọc + ship > rentalPrice)
    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount = BigDecimal.ZERO;

    // Số tiền khách phải bù thêm (nếu cọc + ship < rentalPrice)
    @Column(name = "extra_charges", precision = 10, scale = 2)
    private BigDecimal extraCharges = BigDecimal.ZERO;

    // Tiền phát sinh do sự cố (admin thêm tay)
    @Column(name = "incident_charges", precision = 10, scale = 2)
    private BigDecimal incidentCharges = BigDecimal.ZERO;

    // Ngày thanh toán ghi nhận
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ===== Getter & Setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Rental getRental() { return rental; }
    public void setRental(Rental rental) { this.rental = rental; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public BigDecimal getExtraCharges() { return extraCharges; }
    public void setExtraCharges(BigDecimal extraCharges) { this.extraCharges = extraCharges; }

    public BigDecimal getIncidentCharges() { return incidentCharges; }
    public void setIncidentCharges(BigDecimal incidentCharges) { this.incidentCharges = incidentCharges; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void setStatus(String paid) {

    }
}
