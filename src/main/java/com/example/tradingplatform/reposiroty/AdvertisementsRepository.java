package com.example.tradingplatform.reposiroty;

import com.example.tradingplatform.entity.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementsRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByUserId(Long userId);
    List<Advertisement> findTop9ByOrderByCreatedAtDesc();
    Page<Advertisement> findByTitleContainingIgnoreCase(String searchTerm, Pageable pageable);
}
