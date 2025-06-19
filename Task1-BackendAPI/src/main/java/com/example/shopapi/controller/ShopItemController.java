package com.example.shopapi.controller;

import com.example.shopapi.model.ShopItem;
import com.example.shopapi.service.ShopItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ShopItemController {

    private final ShopItemService shopItemService;

    @Autowired
    public ShopItemController(ShopItemService shopItemService) {
        this.shopItemService = shopItemService;
    }

    @GetMapping
    public ResponseEntity<List<ShopItem>> getAllItems() {
        List<ShopItem> items = shopItemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopItem> getItemById(@PathVariable Long id) {
        ShopItem item = shopItemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ShopItem>> getItemsByCategory(@PathVariable Long categoryId) {
        List<ShopItem> items = shopItemService.getItemsByCategory(categoryId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/price")
    public ResponseEntity<List<ShopItem>> getItemsByMaxPrice(@RequestParam Float maxPrice) {
        List<ShopItem> items = shopItemService.getItemsByMaxPrice(maxPrice);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<ShopItem> createItem(@Valid @RequestBody ShopItem item) {
        ShopItem createdItem = shopItemService.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShopItem> updateItem(@PathVariable Long id, @Valid @RequestBody ShopItem item) {
        ShopItem updatedItem = shopItemService.updateItem(id, item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        shopItemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{itemId}/categories/{categoryId}")
    public ResponseEntity<ShopItem> addCategoryToItem(@PathVariable Long itemId, @PathVariable Long categoryId) {
        ShopItem updatedItem = shopItemService.addCategoryToItem(itemId, categoryId);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{itemId}/categories/{categoryId}")
    public ResponseEntity<ShopItem> removeCategoryFromItem(@PathVariable Long itemId, @PathVariable Long categoryId) {
        ShopItem updatedItem = shopItemService.removeCategoryFromItem(itemId, categoryId);
        return ResponseEntity.ok(updatedItem);
    }
}