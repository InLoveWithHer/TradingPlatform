package com.example.tradingplatform.reposiroty;

import com.example.tradingplatform.entity.Advertisement;
import com.example.tradingplatform.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT COUNT(o) FROM Order o WHERE o.advertisement.id = :advertisementId")
    int countOrdersByAdvertisementId(Long advertisementId);

    Page<Order> findByAdvertisement(Advertisement advertisement, Pageable pageable);

    boolean existsByAdvertisementId(Long advertisementId);
}
