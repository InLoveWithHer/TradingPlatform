package com.example.tradingplatform.service;

import com.example.tradingplatform.entity.Category;
import com.example.tradingplatform.entity.Subcategory;
import com.example.tradingplatform.exception.ResNotFoundException;
import com.example.tradingplatform.reposiroty.CategoryRepository;
import com.example.tradingplatform.reposiroty.SubcategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubcategoryService(SubcategoryRepository subcategoryRepository, CategoryRepository categoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public Subcategory createSubcategory(String name, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResNotFoundException("Category not found with id " + categoryId));
        Subcategory subcategory = new Subcategory();
        subcategory.setName(name);
        subcategory.setCategory(category);
        return subcategoryRepository.save(subcategory);
    }

    public List<Subcategory> getSubcategoriesByCategory(Category category) {
        return subcategoryRepository.findByCategory(category);
    }

    public Subcategory getSubcategoryByName(String subcategoryName) {
        return subcategoryRepository.findByName(subcategoryName);
    }

}
