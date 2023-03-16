package com.example.tradingplatform.reposiroty;

import com.example.tradingplatform.entity.Category;
import com.example.tradingplatform.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    Subcategory findByName(String name);
    List<Subcategory> findByCategory(Category category);
}
