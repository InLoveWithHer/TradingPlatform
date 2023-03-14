package com.example.tradingplatform.reposiroty;

import com.example.tradingplatform.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementsRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByUserId(Long userId);
    List<Advertisement> findTop9ByOrderByCreatedAtDesc();

    List<Advertisement> findByTitleContainingIgnoreCase(String searchTerm);
}
