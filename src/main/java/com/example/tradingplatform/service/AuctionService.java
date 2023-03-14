package com.example.tradingplatform.service;

import com.example.tradingplatform.entity.Advertisement;
import com.example.tradingplatform.entity.Auction;
import com.example.tradingplatform.reposiroty.AuctionDuration;
import com.example.tradingplatform.reposiroty.AuctionRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
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

        return auctionRepository.save(auction);
    }





}
