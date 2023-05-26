package com.example.tradingplatform.reposiroty;

import com.example.tradingplatform.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
