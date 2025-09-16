package com.example.RentCar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tên cơ sở, ví dụ: "Cơ sở chính"
    @Column(name = "name", nullable = false)
    private String name;

    // Địa chỉ cụ thể, ví dụ: "123 Nguyễn Văn A, Quận 1, TP.HCM"
    @Column(name = "address", nullable = false)
    private String address;

    // Có thể thêm các trường khác nếu cần, ví dụ số điện thoại, email,...

    // Constructors
    public Store() {
    }

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

