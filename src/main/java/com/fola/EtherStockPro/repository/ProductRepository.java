package com.fola.EtherStockPro.repository;

import com.fola.EtherStockPro.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
