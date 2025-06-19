package com.example.shopapi.repository;

import com.example.shopapi.model.ShopItem;
import com.example.shopapi.model.ShopItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    Optional<ShopItem> findByTitle(String title);
    boolean existsByTitle(String title);
    List<ShopItem> findByCategoriesContaining(ShopItemCategory category);
    List<ShopItem> findByPriceLessThanEqual(Float price);
}