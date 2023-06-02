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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdvertisementService {

    private final AdvertisementsRepository advertisementRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final AuctionService auctionService;
    private final UsersRepository usersRepository;
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;

    @Autowired
    public AdvertisementService(AdvertisementsRepository advertisementRepository, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, AuctionService auctionService, UsersRepository usersRepository, BidRepository bidRepository, AuctionRepository auctionRepository1) {
        this.advertisementRepository = advertisementRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.auctionService = auctionService;
        this.usersRepository = usersRepository;
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository1;
    }

    public Page<Advertisement> findLatestAdvertisements(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        return advertisementRepository.findAll(pageable);
    }

    public List<Advertisement> getLatestAdvertisements() {
        return advertisementRepository.findTop9ByOrderByCreatedAtDesc();
    }

    public Page<Advertisement> findAdvertisementsByTitleContainingIgnoreCase(String searchTerm, Pageable pageable) {
        return advertisementRepository.findByTitleContainingIgnoreCase(searchTerm, pageable);
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

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResNotFoundException("Category not found with id " + categoryId));

        Subcategory subcategory = subcategoryRepository.findById(subcategoryId)
                .orElseThrow(() -> new ResNotFoundException("Subcategory not found with id " + subcategoryId));

        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResNotFoundException("User not found with id " + userId));

        Advertisement advertisement = new Advertisement(title, price, category, subcategory, status, description, type, fileName, user);
        advertisementRepository.save(advertisement);

        if (isAuction) {
            Auction auction = auctionService.createAuction(auctionDuration, auctionStartingBid, advertisement);
            advertisement.setAuction(auction);
            advertisementRepository.save(advertisement); // Сохраняем обновленное объявление вместе с аукционом
        }

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

    public List<Advertisement> getAllAdvertisementsByUserId(Long userId) {
        return advertisementRepository.findByUserId(userId);
    }

    public void saveAdvertisement(Advertisement advertisement) {
        advertisementRepository.save(advertisement);
    }

    public void changePrice(Long advertisementId, Double newPrice) {
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(advertisementId);
        if (advertisementOptional.isPresent()) {
            Advertisement advertisement = advertisementOptional.get();
            if (advertisement.getAuction() != null) {
                throw new IllegalStateException("Cannot change price for an advertisement with an auction");
            }
            advertisement.setPrice(newPrice);
            advertisementRepository.save(advertisement);
        } else {
            throw new IllegalArgumentException("Advertisement not found with ID: " + advertisementId);
        }
    }

    public void deleteAdvertisement(Long advertisementId) {
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(advertisementId);
        if (advertisementOptional.isPresent()) {
            Advertisement advertisement = advertisementOptional.get();

            // Удаление связанного аукциона
            Auction auction = advertisement.getAuction();
            if (auction != null) {
                // Удаление связанных ставок
                List<Bid> bids = auction.getBids();
                if (bids != null) {
                    bidRepository.deleteAll(bids);
                }

                auctionRepository.delete(auction);
            }

            advertisementRepository.delete(advertisement);
        } else {
            throw new IllegalArgumentException("Advertisement not found with ID: " + advertisementId);
        }
    }


}