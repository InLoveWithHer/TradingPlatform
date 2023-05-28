package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.Auction;
import com.example.tradingplatform.entity.Bid;
import com.example.tradingplatform.service.AuctionService;
import com.example.tradingplatform.service.UserService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.tradingplatform.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

@Controller
public class AuctionController {
    private final AuctionService auctionService;
    private final UserService userService;

    public AuctionController(AuctionService auctionService, UserService userService) {
        this.auctionService = auctionService;
        this.userService = userService;
    }

    @PostMapping("advertisement/{advertisementId}/auction/createBids")
    public ResponseEntity<Bid> createBid(Authentication authentication, @PathVariable Long advertisementId, @RequestParam Double price) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getByEmail(authentication.getName());
            Bid createdBid = auctionService.createBid(advertisementId, price, user);
            return ResponseEntity.ok(createdBid);
        } else {
            // Обработка случая, когда пользователь не авторизован
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("advertisement/{advertisementId}/auction/bids")
    public ResponseEntity<List<Bid>> getBids(@PathVariable Long advertisementId) throws ChangeSetPersister.NotFoundException {
        List<Bid> bids = auctionService.getBids(advertisementId);
        if (bids != null) {
            return ResponseEntity.ok(bids);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/bid/{bidId}/user/name")
    public ResponseEntity<String> getUserNameByBidId(@PathVariable Long bidId) {
        try {
            String userName = auctionService.getUserNameByBidId(bidId);
            return ResponseEntity.ok(userName);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/advertisement/{advertisementId}/auction")
    public ResponseEntity<?> getAuction(@PathVariable Long advertisementId) {
        Auction auction = auctionService.getAuctionByAdvertisementId(advertisementId);
        if (auction != null) {
            return ResponseEntity.ok(auction);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}/auction/{auctionId}/maxBid")
    public ResponseEntity<Double> getMaxBidForUserInAuction(@PathVariable Long userId, @PathVariable Long auctionId) {
        Double maxUserBid = auctionService.getMaxBidForUserInAuction(userId, auctionId);
        return ResponseEntity.ok(maxUserBid);
    }

}
