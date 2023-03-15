package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.Category;
import com.example.tradingplatform.pojo.CategoryRequest;
import com.example.tradingplatform.entity.Subcategory;
import com.example.tradingplatform.exception.ResNotFoundException;
import com.example.tradingplatform.pojo.SubcategoryRequest;
import com.example.tradingplatform.reposiroty.CategoryRepository;
import com.example.tradingplatform.service.CategoryService;
import com.example.tradingplatform.service.SubcategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminPageController {
    public final CategoryService categoryService;
    public final SubcategoryService subcategoryService;
    public final CategoryRepository categoryRepository;

    public AdminPageController(CategoryService categoryService, SubcategoryService subcategoryService, CategoryRepository categoryRepository) {

        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
        this.categoryRepository = categoryRepository;
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

}
