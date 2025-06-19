package com.example.shopapi.service;

import com.example.shopapi.model.ShopItem;
import com.example.shopapi.model.ShopItemCategory;
import com.example.shopapi.repository.ShopItemCategoryRepository;
import com.example.shopapi.repository.ShopItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopItemService {

    private final ShopItemRepository shopItemRepository;
    private final ShopItemCategoryRepository categoryRepository;

    @Autowired
    public ShopItemService(ShopItemRepository shopItemRepository, ShopItemCategoryRepository categoryRepository) {
        this.shopItemRepository = shopItemRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ShopItem> getAllItems() {
        return shopItemRepository.findAll();
    }

    public ShopItem getItemById(Long id) {
        return shopItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shop item not found with id: " + id));
    }

    public List<ShopItem> getItemsByCategory(Long categoryId) {
        ShopItemCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
        return shopItemRepository.findByCategoriesContaining(category);
    }

    public List<ShopItem> getItemsByMaxPrice(Float maxPrice) {
        return shopItemRepository.findByPriceLessThanEqual(maxPrice);
    }

    public ShopItem createItem(ShopItem item) {
        if (shopItemRepository.existsByTitle(item.getTitle())) {
            throw new IllegalArgumentException("Shop item with title already exists: " + item.getTitle());
        }

        if (item.getCategories() != null && !item.getCategories().isEmpty()) {
            List<ShopItemCategory> validCategories = item.getCategories().stream()
                    .map(category -> categoryRepository.findById(category.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + category.getId())))
                    .collect(Collectors.toList());
            item.setCategories(validCategories);
        }

        return shopItemRepository.save(item);
    }

    public ShopItem updateItem(Long id, ShopItem itemDetails) {
        ShopItem item = getItemById(id);

        if (!item.getTitle().equals(itemDetails.getTitle()) && 
            shopItemRepository.existsByTitle(itemDetails.getTitle())) {
            throw new IllegalArgumentException("Shop item with title already exists: " + itemDetails.getTitle());
        }

        item.setTitle(itemDetails.getTitle());
        item.setDescription(itemDetails.getDescription());
        item.setPrice(itemDetails.getPrice());

        if (itemDetails.getCategories() != null && !itemDetails.getCategories().isEmpty()) {
            List<ShopItemCategory> validCategories = itemDetails.getCategories().stream()
                    .map(category -> categoryRepository.findById(category.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + category.getId())))
                    .collect(Collectors.toList());
            item.setCategories(validCategories);
        }

        return shopItemRepository.save(item);
    }

    public void deleteItem(Long id) {
        if (!shopItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Shop item not found with id: " + id);
        }
        shopItemRepository.deleteById(id);
    }

    public ShopItem addCategoryToItem(Long itemId, Long categoryId) {
        ShopItem item = getItemById(itemId);
        ShopItemCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

        if (item.getCategories().stream().noneMatch(c -> c.getId().equals(categoryId))) {
            item.getCategories().add(category);
            return shopItemRepository.save(item);
        }

        return item;
    }

    public ShopItem removeCategoryFromItem(Long itemId, Long categoryId) {
        ShopItem item = getItemById(itemId);

        if (item.getCategories().removeIf(category -> category.getId().equals(categoryId))) {
            return shopItemRepository.save(item);
        }

        throw new EntityNotFoundException("Category with id " + categoryId + " not found in item with id " + itemId);
    }
}
