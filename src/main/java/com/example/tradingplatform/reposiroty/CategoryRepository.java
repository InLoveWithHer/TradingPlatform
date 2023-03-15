package com.example.tradingplatform.reposiroty;

import com.example.tradingplatform.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
