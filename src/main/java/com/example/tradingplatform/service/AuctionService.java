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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public List<Bid> getBids(Long advertisementId) throws ChangeSetPersister.NotFoundException {
        Advertisement advertisement = advertisementsRepository.findById(advertisementId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        Auction auction = advertisement.getAuction();

        return auction.getBids();
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

}
