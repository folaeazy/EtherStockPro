package com.fola.EtherStockPro.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "sku cannot be blank")
    @Column(unique = true)
    private String sku;

    private BigInteger price;

    private BigInteger stockQuantity;

    private String description;

    private  String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private LocalDateTime expiryDate;

    private LocalDateTime updatedAt;

    private final LocalDateTime createdAt = LocalDateTime.now();



}
