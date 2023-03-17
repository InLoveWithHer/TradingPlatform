package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.Advertisement;
import com.example.tradingplatform.entity.Category;
import com.example.tradingplatform.entity.Subcategory;
import com.example.tradingplatform.entity.User;
import com.example.tradingplatform.reposiroty.AuctionDuration;
import com.example.tradingplatform.reposiroty.CategoryRepository;
import com.example.tradingplatform.reposiroty.SubcategoryRepository;
import com.example.tradingplatform.service.AdvertisementService;
import com.example.tradingplatform.service.CategoryService;
import com.example.tradingplatform.service.SubcategoryService;
import com.example.tradingplatform.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;

    public AdvertisementController(AdvertisementService advertisementService, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, UserService userService, CategoryService categoryService, SubcategoryService subcategoryService) {
        this.advertisementService = advertisementService;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
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
    public String showAdvertisement(@PathVariable("id") Long id, Model model) {
        Advertisement advertisement = advertisementService.getAdvertisementById(id);
        model.addAttribute("advertisement", advertisement);
        return "advertisement";
    }



    @GetMapping("/search")
    public String search(@RequestParam("search") String searchTerm,
                         @RequestParam(defaultValue = "0") int page,
                         Model model,
                         UriComponentsBuilder uriBuilder) {
        int pageSize = 6; // количество записей на странице
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Advertisement> advertisementsPage = advertisementService.findAdvertisementsByTitleContainingIgnoreCase(searchTerm, pageable);
        List<Advertisement> advertisements = advertisementsPage.getContent();
        int totalPages = advertisementsPage.getTotalPages();
        model.addAttribute("advertisements", advertisements);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageTitle", searchTerm);

        // Добавление параметров поиска в URL для сохранения параметров при переходе на другую страницу
        UriComponents uriComponents = uriBuilder.path("/search")
                .queryParam("search", searchTerm)
                .queryParam("page", page + 1)
                .build();
        model.addAttribute("nextPageUrl", uriComponents.toUriString());
        return "search";
    }

    @GetMapping("/createAdvertisements")
    public String createAdvertisementForm(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "create-advertisement";
    }

    @GetMapping("/subcategory/{category}")
    @ResponseBody
    public List<String> getSubcategoriesByCategory(@PathVariable String category) {
        Category categoryObj = categoryService.getCategoryByName(category);
        if (categoryObj == null) {
            return Collections.emptyList();
        }
        List<Subcategory> subcategories = subcategoryService.getSubcategoriesByCategory(categoryObj);
        return subcategories.stream()
                .map(Subcategory::getName)
                .collect(Collectors.toList());
    }

    @PostMapping("/createAdvertisements")
    public String createAdvertisement(Authentication authentication, @RequestParam("title") String title,
                                      @RequestParam(value = "price", required = false) Double price,
                                      @RequestParam("category") String categoryName,
                                      @RequestParam("subcategory") String subcategoryName,
                                      @RequestParam(value = "status", required = false) String status,
                                      @RequestParam("description") String description,
                                      @RequestParam(value = "type", required = false) String type,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam(value = "isAuction", defaultValue = "false") boolean isAuction,
                                      @RequestParam(value = "auctionDuration", required = false) AuctionDuration auctionDuration,
                                      @RequestParam(value = "auctionStartingBid", required = false) Double auctionStartingBid,
                                      RedirectAttributes redirectAttributes) throws IOException {

        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            return "redirect:/";
        }

        Subcategory subcategory = subcategoryRepository.findByName(subcategoryName);
        if (subcategory == null) {
            return "redirect:/";
        }

        User user = userService.getByEmail(authentication.getName());

        Advertisement advertisement = advertisementService.createAdvertisement(title, price, category.getId(), subcategory.getId(), status, description, type,
                file, user.getId(), isAuction, auctionDuration, auctionStartingBid);

        redirectAttributes.addAttribute("id", advertisement.getId());

        return "redirect:/advertisement/{id}";
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
