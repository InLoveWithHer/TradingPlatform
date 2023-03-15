package com.example.tradingplatform.service;

import com.example.tradingplatform.entity.*;
import com.example.tradingplatform.exception.ResNotFoundException;
import com.example.tradingplatform.reposiroty.*;
import com.example.tradingplatform.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdvertisementService {

    private final AdvertisementsRepository advertisementRepository;
    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final UserService userService;
    private final AuctionService auctionService;

    @Autowired
    public AdvertisementService(AdvertisementsRepository advertisementRepository, AuctionRepository auctionRepository, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, UserService userService, AuctionService auctionService) {
        this.advertisementRepository = advertisementRepository;
        this.auctionRepository = auctionRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.userService = userService;
        this.auctionService = auctionService;
    }

    public Page<Advertisement> findLatestAdvertisements(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        return advertisementRepository.findAll(pageable);
    }


    public List<Advertisement> getLatestAdvertisements() {
        return advertisementRepository.findTop9ByOrderByCreatedAtDesc();
    }

    public List<Advertisement> findByTitleContainingIgnoreCase(String searchTerm) {
        return advertisementRepository.findByTitleContainingIgnoreCase(searchTerm);
    }

    public Advertisement getAdvertisementById(Long id) {
        Optional<Advertisement> advertisement = advertisementRepository.findById(id);
        if (advertisement.isPresent()) {
            return advertisement.get();
        } else {
            throw new ResNotFoundException("Advertisement not found with id " + id);
        }
    }


    public Advertisement createAdvertisement(String title, Double price, Long categoryId, Long subcategoryId, String status, String description, String type,
                                             MultipartFile file, Long userId, boolean isAuction, AuctionDuration auctionDuration, Double auctionStartingBid) throws IOException {

        User user = userService.getUserById(userId);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResNotFoundException("Category not found with id " + categoryId));

        Subcategory subcategory = subcategoryRepository.findById(subcategoryId)
                .orElseThrow(() -> new ResNotFoundException("Subcategory not found with id " + subcategoryId));

        Advertisement advertisement;

        if (isAuction) {
            Auction auction = auctionService.createAuction(auctionDuration, auctionStartingBid, null); // передаем null, так как advertisement еще не создан
            advertisement = new Advertisement(title, price, category, subcategory, status, description, type, fileName, user, auction);
            auction.setAdvertisement(advertisement); // устанавливаем связь с созданным объявлением
            auctionRepository.save(auction); // сохраняем аукцион с обновленной связью
        } else {
            advertisement = new Advertisement(title, price, category, subcategory, status, description, type, fileName, user);
        }

        advertisementRepository.save(advertisement);
        advertisement.setPhotoUrl(fileName);

        String uploadDir = "D:\\diplom\\TradingPlatform\\src\\main\\resources\\static\\images";
        FileUploadUtil.saveFile(uploadDir, file);

        return advertisement;
    }




    public Advertisement updateAdvertisement(Long id, Advertisement advertisementDetails) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new ResNotFoundException("Advertisement not found with id " + id));

        advertisement.setTitle(advertisementDetails.getTitle());
        advertisement.setPrice(advertisementDetails.getPrice());
        advertisement.setCategory(advertisementDetails.getCategory());
        advertisement.setAuction(advertisementDetails.getAuction());
        advertisement.setStatus(advertisementDetails.getStatus());

        return advertisementRepository.save(advertisement);
    }

    public void deleteAdvertisement(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new ResNotFoundException("Advertisement not found with id " + id));
        advertisementRepository.delete(advertisement);
    }

    public List<Advertisement> getAllAdvertisementsByUserId(Long userId) {
        return advertisementRepository.findByUserId(userId);
    }

}