package com.example.shopapi.service;

import com.example.shopapi.model.ShopItemCategory;
import com.example.shopapi.repository.ShopItemCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ShopItemCategoryService {

    private final ShopItemCategoryRepository categoryRepository;

    @Autowired
    public ShopItemCategoryService(ShopItemCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<ShopItemCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public ShopItemCategory getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    public ShopItemCategory createCategory(ShopItemCategory category) {
        if (categoryRepository.existsByTitle(category.getTitle())) {
            throw new IllegalArgumentException("Category with title already exists: " + category.getTitle());
        }
        return categoryRepository.save(category);
    }

    public ShopItemCategory updateCategory(Long id, ShopItemCategory categoryDetails) {
        ShopItemCategory category = getCategoryById(id);
        
        // Check if title is being changed and if it's already in use
        if (!category.getTitle().equals(categoryDetails.getTitle()) && 
            categoryRepository.existsByTitle(categoryDetails.getTitle())) {
            throw new IllegalArgumentException("Category with title already exists: " + categoryDetails.getTitle());
        }
        
        category.setTitle(categoryDetails.getTitle());
        category.setDescription(categoryDetails.getDescription());
        
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}