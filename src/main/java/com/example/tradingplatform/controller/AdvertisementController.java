package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.Advertisement;
import com.example.tradingplatform.entity.Category;
import com.example.tradingplatform.entity.Subcategory;
import com.example.tradingplatform.exception.ResNotFoundException;
import com.example.tradingplatform.reposiroty.AuctionDuration;
import com.example.tradingplatform.reposiroty.CategoryRepository;
import com.example.tradingplatform.reposiroty.SubcategoryRepository;
import com.example.tradingplatform.service.AdvertisementService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    public AdvertisementController(AdvertisementService advertisementService, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository) {
        this.advertisementService = advertisementService;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @GetMapping("/advertisements")
    public String getAdvertisementsPage(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 6; // количество записей на странице
        Page<Advertisement> advertisementsPage = advertisementService.findLatestAdvertisements(page, pageSize);
        List<Advertisement> advertisements = advertisementsPage.getContent();
        int totalPages = advertisementsPage.getTotalPages();
        model.addAttribute("advertisements", advertisements);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "advertisements";
    }

    @GetMapping("/advertisement/{id}")
    public String getAdvertisementById(@PathVariable Long id, Model model) {
        Advertisement advertisement = advertisementService.getAdvertisementById(id);
        model.addAttribute("advertisement", advertisement);
        return "advertisement";
    }

    @GetMapping("/search")
    public String search(@RequestParam("search") String searchTerm, Model model) {
        List<Advertisement> advertisements = advertisementService.findByTitleContainingIgnoreCase(searchTerm);
        model.addAttribute("advertisements", advertisements);
        model.addAttribute("pageTitle", searchTerm);
        return "search";
    }

    @PostMapping("/createAdvertisements")
    public ResponseEntity<Advertisement> createAdvertisement(@RequestParam("title") String title,
                                                             @RequestParam(value = "price", required = false) Double price,
                                                             @RequestParam("category") String categoryName,
                                                             @RequestParam("subcategory") String subcategoryName,
                                                             @RequestParam(value = "status", required = false) String status,
                                                             @RequestParam("description") String description,
                                                             @RequestParam(value = "type", required = false) String type,
                                                             @RequestParam("file") MultipartFile file,
                                                             @RequestParam("userId") Long userId,
                                                             @RequestParam("isAuction") boolean isAuction,
                                                             @RequestParam(value = "auctionDuration", required = false) AuctionDuration auctionDuration,
                                                             @RequestParam(value = "auctionStartingBid", required = false) Double auctionStartingBid) throws IOException {

        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Subcategory subcategory = subcategoryRepository.findByName(subcategoryName);
        if (subcategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Advertisement advertisement = advertisementService.createAdvertisement(title, price, category.getId(), subcategory.getId(), status, description, type,
                file, userId, isAuction, auctionDuration, auctionStartingBid);

        return new ResponseEntity<>(advertisement, HttpStatus.CREATED);
    }



    @PutMapping("/advertisements/{id}")
    public Advertisement updateAdvertisement(@PathVariable Long id, @RequestBody Advertisement advertisementDetails) {
        return advertisementService.updateAdvertisement(id, advertisementDetails);
    }

    @DeleteMapping("/advertisements/{id}")
    public ResponseEntity<?> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/advertisements/{userId}")
    public List<Advertisement> getAllAdvertisementsByUserId(@PathVariable Long userId) {
        return advertisementService.getAllAdvertisementsByUserId(userId);
    }

}
