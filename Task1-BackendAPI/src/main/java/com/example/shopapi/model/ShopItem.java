package com.example.shopapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Float price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "shop_item_categories",
            joinColumns = @JoinColumn(name = "shop_item_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<ShopItemCategory> categories = new ArrayList<>();
}