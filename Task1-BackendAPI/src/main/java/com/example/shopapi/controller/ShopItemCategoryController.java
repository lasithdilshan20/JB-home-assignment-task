package com.example.shopapi.controller;

import com.example.shopapi.model.ShopItemCategory;
import com.example.shopapi.service.ShopItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ShopItemCategoryController {

    private final ShopItemCategoryService categoryService;

    @Autowired
    public ShopItemCategoryController(ShopItemCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<ShopItemCategory>> getAllCategories() {
        List<ShopItemCategory> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopItemCategory> getCategoryById(@PathVariable Long id) {
        ShopItemCategory category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<ShopItemCategory> createCategory(@Valid @RequestBody ShopItemCategory category) {
        ShopItemCategory createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShopItemCategory> updateCategory(@PathVariable Long id, @Valid @RequestBody ShopItemCategory category) {
        ShopItemCategory updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}