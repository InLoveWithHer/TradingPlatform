package com.example.tradingplatform.reposiroty;


import com.example.tradingplatform.entity.Auction;
import com.example.tradingplatform.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query("SELECT MAX(b.bid) FROM Bid b WHERE b.user.id = :userId AND b.auction.id = :auctionId")
    Double findMaxBidByUserIdAndAuctionId(Long userId, Long auctionId);

    void deleteByAuction(Auction auction);
}
