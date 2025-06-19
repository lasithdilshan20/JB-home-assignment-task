package com.example.shopapi.config;

import com.example.shopapi.model.Customer;
import com.example.shopapi.model.Order;
import com.example.shopapi.model.ShopItem;
import com.example.shopapi.model.ShopItemCategory;
import com.example.shopapi.repository.CustomerRepository;
import com.example.shopapi.repository.OrderRepository;
import com.example.shopapi.repository.ShopItemCategoryRepository;
import com.example.shopapi.repository.ShopItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final ShopItemCategoryRepository categoryRepository;
    private final ShopItemRepository shopItemRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public DataInitializer(
            CustomerRepository customerRepository,
            ShopItemCategoryRepository categoryRepository,
            ShopItemRepository shopItemRepository,
            OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.categoryRepository = categoryRepository;
        this.shopItemRepository = shopItemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {
        Customer john = new Customer(null, "John", "Doe", "john.doe@example.com");
        Customer jane = new Customer(null, "Jane", "Smith", "jane.smith@example.com");

        customerRepository.saveAll(Arrays.asList(john, jane));

        ShopItemCategory electronics = new ShopItemCategory(null, "Electronics", "Electronic devices and accessories");
        ShopItemCategory books = new ShopItemCategory(null, "Books", "Books of various genres");
        ShopItemCategory clothing = new ShopItemCategory(null, "Clothing", "Apparel and fashion items");

        categoryRepository.saveAll(Arrays.asList(electronics, books, clothing));

        ShopItem laptop = new ShopItem(null, "Laptop", "High-performance laptop", 1200.0f, Arrays.asList(electronics));
        ShopItem smartphone = new ShopItem(null, "Smartphone", "Latest smartphone model", 800.0f, Arrays.asList(electronics));
        ShopItem novel = new ShopItem(null, "Novel", "Bestselling fiction novel", 15.0f, Arrays.asList(books));
        ShopItem tshirt = new ShopItem(null, "T-Shirt", "Cotton t-shirt", 25.0f, Arrays.asList(clothing));
        ShopItem jeans = new ShopItem(null, "Jeans", "Denim jeans", 45.0f, Arrays.asList(clothing));

        shopItemRepository.saveAll(Arrays.asList(laptop, smartphone, novel, tshirt, jeans));

        Order johnOrder = new Order();
        johnOrder.setCustomer(john);

        Order janeOrder = new Order();
        janeOrder.setCustomer(jane);

        orderRepository.saveAll(Arrays.asList(johnOrder, janeOrder));

        johnOrder.addItem(createOrderItem(laptop, 1, johnOrder));
        johnOrder.addItem(createOrderItem(novel, 2, johnOrder));

        janeOrder.addItem(createOrderItem(smartphone, 1, janeOrder));
        janeOrder.addItem(createOrderItem(tshirt, 3, janeOrder));
        janeOrder.addItem(createOrderItem(jeans, 1, janeOrder));

        orderRepository.saveAll(Arrays.asList(johnOrder, janeOrder));

        System.out.println("Data initialization completed!");
    }

    private OrderItem createOrderItem(ShopItem shopItem, int quantity, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setShopItem(shopItem);
        orderItem.setQuantity(quantity);
        orderItem.setOrder(order);
        return orderItem;
    }
}
