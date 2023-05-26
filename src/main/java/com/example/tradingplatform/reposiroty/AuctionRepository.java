package com.example.tradingplatform.reposiroty;

import com.example.tradingplatform.entity.Advertisement;
import com.example.tradingplatform.entity.Auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Object> findByAdvertisement(Advertisement advertisement);

    Auction findByAdvertisementId(Long advertisementId);

}
