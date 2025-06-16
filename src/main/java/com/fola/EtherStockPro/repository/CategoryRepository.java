package com.fola.EtherStockPro.repository;

import com.fola.EtherStockPro.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
