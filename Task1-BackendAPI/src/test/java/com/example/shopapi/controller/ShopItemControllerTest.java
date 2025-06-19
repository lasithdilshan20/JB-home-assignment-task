package com.example.shopapi.controller;

import com.example.shopapi.model.ShopItem;
import com.example.shopapi.model.ShopItemCategory;
import com.example.shopapi.service.ShopItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShopItemController.class)
public class ShopItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopItemService shopItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private ShopItemCategory electronicsCategory;
    private ShopItem item1;
    private ShopItem item2;
    private List<ShopItem> items;

    @BeforeEach
    void setUp() {
        electronicsCategory = new ShopItemCategory(1L, "Electronics", "Electronic devices and accessories");
        item1 = new ShopItem(1L, "Laptop", "High-performance laptop", 1200.0f, Collections.singletonList(electronicsCategory));
        item2 = new ShopItem(2L, "Smartphone", "Latest smartphone model", 800.0f, Collections.singletonList(electronicsCategory));
        items = Arrays.asList(item1, item2);
    }

    @Test
    void getAllItems_ShouldReturnAllItems() throws Exception {
        when(shopItemService.getAllItems()).thenReturn(items);

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Laptop")))
                .andExpect(jsonPath("$[0].description", is("High-performance laptop")))
                .andExpect(jsonPath("$[0].price", is(1200.0)))
                .andExpect(jsonPath("$[0].categories", hasSize(1)))
                .andExpect(jsonPath("$[0].categories[0].title", is("Electronics")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Smartphone")))
                .andExpect(jsonPath("$[1].price", is(800.0)));

        verify(shopItemService, times(1)).getAllItems();
    }

    @Test
    void getItemById_WithValidId_ShouldReturnItem() throws Exception {
        when(shopItemService.getItemById(1L)).thenReturn(item1);

        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Laptop")))
                .andExpect(jsonPath("$.description", is("High-performance laptop")))
                .andExpect(jsonPath("$.price", is(1200.0)))
                .andExpect(jsonPath("$.categories", hasSize(1)))
                .andExpect(jsonPath("$.categories[0].title", is("Electronics")));

        verify(shopItemService, times(1)).getItemById(1L);
    }

    @Test
    void getItemsByCategory_ShouldReturnItemsInCategory() throws Exception {
        when(shopItemService.getItemsByCategory(1L)).thenReturn(items);

        mockMvc.perform(get("/api/items/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Laptop")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Smartphone")));

        verify(shopItemService, times(1)).getItemsByCategory(1L);
    }

    @Test
    void getItemsByMaxPrice_ShouldReturnItemsWithPriceLessThanOrEqual() throws Exception {
        when(shopItemService.getItemsByMaxPrice(1000.0f)).thenReturn(Collections.singletonList(item2));

        mockMvc.perform(get("/api/items/price").param("maxPrice", "1000.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].title", is("Smartphone")))
                .andExpect(jsonPath("$[0].price", is(800.0)));

        verify(shopItemService, times(1)).getItemsByMaxPrice(1000.0f);
    }

    @Test
    void createItem_WithValidData_ShouldReturnCreatedItem() throws Exception {
        ShopItem newItem = new ShopItem(null, "Tablet", "New tablet model", 500.0f, Collections.singletonList(electronicsCategory));
        ShopItem savedItem = new ShopItem(3L, "Tablet", "New tablet model", 500.0f, Collections.singletonList(electronicsCategory));

        when(shopItemService.createItem(any(ShopItem.class))).thenReturn(savedItem);

        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("Tablet")))
                .andExpect(jsonPath("$.description", is("New tablet model")))
                .andExpect(jsonPath("$.price", is(500.0)))
                .andExpect(jsonPath("$.categories", hasSize(1)))
                .andExpect(jsonPath("$.categories[0].title", is("Electronics")));

        verify(shopItemService, times(1)).createItem(any(ShopItem.class));
    }

    @Test
    void updateItem_WithValidData_ShouldReturnUpdatedItem() throws Exception {
        ShopItem updatedItem = new ShopItem(1L, "Updated Laptop", "Updated description", 1300.0f, Collections.singletonList(electronicsCategory));

        when(shopItemService.updateItem(eq(1L), any(ShopItem.class))).thenReturn(updatedItem);

        mockMvc.perform(put("/api/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated Laptop")))
                .andExpect(jsonPath("$.description", is("Updated description")))
                .andExpect(jsonPath("$.price", is(1300.0)));

        verify(shopItemService, times(1)).updateItem(eq(1L), any(ShopItem.class));
    }

    @Test
    void deleteItem_WithValidId_ShouldReturnNoContent() throws Exception {
        doNothing().when(shopItemService).deleteItem(1L);

        mockMvc.perform(delete("/api/items/1"))
                .andExpect(status().isNoContent());

        verify(shopItemService, times(1)).deleteItem(1L);
    }

    @Test
    void addCategoryToItem_ShouldReturnUpdatedItem() throws Exception {
        ShopItemCategory booksCategory = new ShopItemCategory(2L, "Books", "Books of various genres");
        ShopItem updatedItem = new ShopItem(1L, "Laptop", "High-performance laptop", 1200.0f, 
                Arrays.asList(electronicsCategory, booksCategory));

        when(shopItemService.addCategoryToItem(1L, 2L)).thenReturn(updatedItem);

        mockMvc.perform(post("/api/items/1/categories/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.categories", hasSize(2)))
                .andExpect(jsonPath("$.categories[0].title", is("Electronics")))
                .andExpect(jsonPath("$.categories[1].title", is("Books")));

        verify(shopItemService, times(1)).addCategoryToItem(1L, 2L);
    }

    @Test
    void removeCategoryFromItem_ShouldReturnUpdatedItem() throws Exception {
        ShopItem updatedItem = new ShopItem(1L, "Laptop", "High-performance laptop", 1200.0f, Collections.emptyList());

        when(shopItemService.removeCategoryFromItem(1L, 1L)).thenReturn(updatedItem);

        mockMvc.perform(delete("/api/items/1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.categories", hasSize(0)));

        verify(shopItemService, times(1)).removeCategoryFromItem(1L, 1L);
    }

    @Test
    void createItem_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        ShopItem invalidItem = new ShopItem(null, "", "No title", -100.0f, Collections.emptyList());

        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidItem)))
                .andExpect(status().isBadRequest());

        verify(shopItemService, never()).createItem(any(ShopItem.class));
    }
}