package com.example.tradingplatform.service;

import com.example.tradingplatform.entity.Category;
import com.example.tradingplatform.reposiroty.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        category.setSubcategories(new ArrayList<>());
        return categoryRepository.save(category);
    }

    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }
}
