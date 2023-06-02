package com.example.tradingplatform.service;

import com.example.tradingplatform.entity.Advertisement;
import com.example.tradingplatform.entity.Auction;
import com.example.tradingplatform.entity.Bid;
import com.example.tradingplatform.entity.User;
import com.example.tradingplatform.reposiroty.AdvertisementsRepository;
import com.example.tradingplatform.reposiroty.AuctionDuration;
import com.example.tradingplatform.reposiroty.AuctionRepository;
import com.example.tradingplatform.reposiroty.BidRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final AdvertisementsRepository advertisementsRepository;

    public AuctionService(AuctionRepository auctionRepository, BidRepository bidRepository, AdvertisementsRepository advertisementsRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.advertisementsRepository = advertisementsRepository;
    }

    public Auction createAuction(AuctionDuration auctionDuration, Double startingBid, Advertisement advertisement) {
        Auction auction = new Auction();
        auction.setEndDate(new Date()); // Устанавливаем текущую дату
        auction.setStartingBid(startingBid);
        auction.setAdvertisement(advertisement);

        // Вычисляем дату окончания аукциона на основе выбранной продолжительности
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(auction.getEndDate());
        calendar.add(Calendar.DATE, auctionDuration.getDays());
        Date endDate = calendar.getTime();
        auction.setEndDate(endDate);

        advertisement.setAuction(auction); // Устанавливаем аукцион в объекте Advertisement
        return auctionRepository.save(auction);
    }

    public Bid createBid(Long advertisementId, Double price, User user) {
        Advertisement advertisement = advertisementsRepository.findById(advertisementId).orElseThrow(() -> new EntityNotFoundException("Advertisement not found"));
        Auction auction = (Auction) auctionRepository.findByAdvertisement(advertisement)
                .orElseThrow(() -> new EntityNotFoundException("Active auction not found for this advertisement"));

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setBid(price);
        bid.setAuction(auction);

        return bidRepository.save(bid);
    }

    public List<Bid> getBids(Long advertisementId) {
        Advertisement advertisement = advertisementsRepository.findById(advertisementId).orElse(null);

        if (advertisement != null && advertisement.getAuction() != null) {
            Auction auction = advertisement.getAuction();
            return auction.getBids();
        } else {
            return new ArrayList<>();
        }
    }

    public String getUserNameByBidId(Long bidId) throws ChangeSetPersister.NotFoundException {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        User user = bid.getUser();

        return user.getName();
    }

    public Auction getAuctionByAdvertisementId(Long advertisementId) {
        return auctionRepository.findByAdvertisementId(advertisementId);
    }

    public Double getMaxBidForUserInAuction(Long userId, Long auctionId) {
        return bidRepository.findMaxBidByUserIdAndAuctionId(userId, auctionId);
    }

//    public Double getMaxBidForAdvertisement(Long advertisementId) {
//        Optional<Advertisement> optionalAdvertisement = advertisementsRepository.findById(advertisementId);
//
//        if (optionalAdvertisement.isEmpty()) {
//            return null; // или любое значение по умолчанию для случая, когда объявление не найдено
//        }
//
//        Advertisement advertisement = optionalAdvertisement.get();
//        Auction auction = advertisement.getAuction();
//
//        if (auction == null) {
//            return null; // или любое значение по умолчанию для случая, когда у объявления нет аукциона
//        }
//
//        List<Bid> bids = bidRepository.findByAuction(auction);
//
//        if (bids.isEmpty()) {
//            return auction.getStartingBid();
//        } else {
//            Double maxBid = bids.stream()
//                    .map(Bid::getBid)
//                    .max(Double::compare)
//                    .orElse(auction.getStartingBid());
//            return maxBid;
//        }
//    }



}
