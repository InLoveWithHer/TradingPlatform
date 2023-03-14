package com.example.tradingplatform.reposiroty;

import com.example.tradingplatform.entity.Auction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
