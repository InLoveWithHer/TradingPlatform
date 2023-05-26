package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.Category;
import com.example.tradingplatform.pojo.CategoryRequest;
import com.example.tradingplatform.entity.Subcategory;
import com.example.tradingplatform.pojo.SubcategoryRequest;
import com.example.tradingplatform.service.CategoryService;
import com.example.tradingplatform.service.SubcategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminPageController {
    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;

    public AdminPageController(CategoryService categoryService, SubcategoryService subcategoryService) {

        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
    }

    @PostMapping("/createCategory")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.createCategory(categoryRequest.getName());
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PostMapping("/createSubcategory")
    public ResponseEntity<Subcategory> createSubcategory(@RequestBody SubcategoryRequest subcategoryRequest) {
        Subcategory subcategory = subcategoryService.createSubcategory(subcategoryRequest.getName(), subcategoryRequest.getCategoryId());
        return new ResponseEntity<>(subcategory, HttpStatus.CREATED);
    }

    @GetMapping("/allCategories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }


}
