package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.Category;
import com.example.tradingplatform.entity.Subcategory;
import com.example.tradingplatform.pojo.CategoryRequest;
import com.example.tradingplatform.pojo.SubcategoryRequest;
import com.example.tradingplatform.service.AdvertisementService;
import com.example.tradingplatform.service.CategoryService;
import com.example.tradingplatform.service.SubcategoryService;
import com.example.tradingplatform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminPageController {
    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;
    private final UserService userService;
    private final AdvertisementService advertisementService;

    public AdminPageController(CategoryService categoryService, SubcategoryService subcategoryService, UserService userService, AdvertisementService advertisementService) {

        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
        this.userService = userService;
        this.advertisementService = advertisementService;
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

    @GetMapping("/admin/allCategories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/admin/updateUserRole")
    public ResponseEntity<String> updateUserRoleToAdmin(@RequestParam String userEmail) {
        userService.updateUserRoleToAdmin(userEmail);
        return ResponseEntity.ok("User role updated successfully");
    }

    @DeleteMapping("/admin/deleteAdvertisement")
    public ResponseEntity<String> deleteAdvertisementById(@RequestParam Long advertisementId) {
        advertisementService.deleteAdvertisement(advertisementId);
        return ResponseEntity.ok("Advertisement deleted successfully");
    }


}
