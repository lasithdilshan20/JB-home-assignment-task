package com.example.shopapi.repository;

import com.example.shopapi.model.ShopItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopItemCategoryRepository extends JpaRepository<ShopItemCategory, Long> {
    Optional<ShopItemCategory> findByTitle(String title);
    boolean existsByTitle(String title);
}