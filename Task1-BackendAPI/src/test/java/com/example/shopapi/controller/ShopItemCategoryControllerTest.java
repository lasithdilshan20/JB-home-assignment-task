package com.example.shopapi.controller;

import com.example.shopapi.model.ShopItemCategory;
import com.example.shopapi.service.ShopItemCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShopItemCategoryController.class)
public class ShopItemCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopItemCategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private ShopItemCategory category1;
    private ShopItemCategory category2;
    private List<ShopItemCategory> categories;

    @BeforeEach
    void setUp() {
        category1 = new ShopItemCategory(1L, "Electronics", "Electronic devices and accessories");
        category2 = new ShopItemCategory(2L, "Books", "Books of various genres");
        categories = Arrays.asList(category1, category2);
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Electronics")))
                .andExpect(jsonPath("$[0].description", is("Electronic devices and accessories")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Books")))
                .andExpect(jsonPath("$[1].description", is("Books of various genres")));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById_WithValidId_ShouldReturnCategory() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(category1);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Electronics")))
                .andExpect(jsonPath("$.description", is("Electronic devices and accessories")));

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void createCategory_WithValidData_ShouldReturnCreatedCategory() throws Exception {
        ShopItemCategory newCategory = new ShopItemCategory(null, "Clothing", "Apparel and fashion items");
        ShopItemCategory savedCategory = new ShopItemCategory(3L, "Clothing", "Apparel and fashion items");

        when(categoryService.createCategory(any(ShopItemCategory.class))).thenReturn(savedCategory);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("Clothing")))
                .andExpect(jsonPath("$.description", is("Apparel and fashion items")));

        verify(categoryService, times(1)).createCategory(any(ShopItemCategory.class));
    }

    @Test
    void updateCategory_WithValidData_ShouldReturnUpdatedCategory() throws Exception {
        ShopItemCategory updatedCategory = new ShopItemCategory(1L, "Updated Electronics", "Updated description");

        when(categoryService.updateCategory(eq(1L), any(ShopItemCategory.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated Electronics")))
                .andExpect(jsonPath("$.description", is("Updated description")));

        verify(categoryService, times(1)).updateCategory(eq(1L), any(ShopItemCategory.class));
    }

    @Test
    void deleteCategory_WithValidId_ShouldReturnNoContent() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    void createCategory_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        ShopItemCategory invalidCategory = new ShopItemCategory(null, "", "Description without title");

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).createCategory(any(ShopItemCategory.class));
    }
}