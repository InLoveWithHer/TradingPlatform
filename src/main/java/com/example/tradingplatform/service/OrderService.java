package com.example.tradingplatform.service;

import com.example.tradingplatform.entity.*;
import com.example.tradingplatform.reposiroty.AuctionRepository;
import com.example.tradingplatform.reposiroty.BidRepository;
import com.example.tradingplatform.reposiroty.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    public OrderService(OrderRepository orderRepository, AuctionRepository auctionRepository, BidRepository bidRepository) {
        this.orderRepository = orderRepository;
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    public int getOrderCountByAdvertisement(Long advertisementId) {

        return orderRepository.countOrdersByAdvertisementId(advertisementId);
    }
    public Page<Order> getOrdersByAdvertisement(Advertisement advertisement, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderRepository.findByAdvertisement(advertisement, pageable);
    }

    public void createOrderByAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

        Advertisement advertisement = auction.getAdvertisement();

        Bid maxBid = bidRepository.findMaxBidByAuctionId(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Max bid not found"));

        User user = maxBid.getUser();

        Order order = new Order();
        order.setName(user.getName());
        order.setPhone(user.getPhone());
        order.setAdvertisement(advertisement);

        orderRepository.save(order);
    }

    public boolean checkOrderExists(Long advertisementId) {
        return orderRepository.existsByAdvertisementId(advertisementId);
    }

}
