package com.example.tradingplatform.reposiroty;

import com.example.tradingplatform.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    Subcategory findByName(String name);
}
