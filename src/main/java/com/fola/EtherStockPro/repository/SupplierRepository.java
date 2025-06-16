package com.fola.EtherStockPro.repository;

import com.fola.EtherStockPro.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
