package com.example.shopapi.controller;

import com.example.shopapi.model.Customer;
import com.example.shopapi.model.Order;
import com.example.shopapi.model.OrderItem;
import com.example.shopapi.model.ShopItem;
import com.example.shopapi.model.ShopItemCategory;
import com.example.shopapi.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer customer;
    private ShopItem laptop;
    private ShopItem smartphone;
    private Order order1;
    private Order order2;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "John", "Doe", "john.doe@example.com");

        ShopItemCategory electronicsCategory = new ShopItemCategory(1L, "Electronics", "Electronic devices");
        laptop = new ShopItem(1L, "Laptop", "High-performance laptop", 1200.0f, Collections.singletonList(electronicsCategory));
        smartphone = new ShopItem(2L, "Smartphone", "Latest smartphone model", 800.0f, Collections.singletonList(electronicsCategory));

        OrderItem laptopItem = new OrderItem();
        laptopItem.setId(1L);
        laptopItem.setShopItem(laptop);
        laptopItem.setQuantity(1);

        OrderItem smartphoneItem = new OrderItem();
        smartphoneItem.setId(2L);
        smartphoneItem.setShopItem(smartphone);
        smartphoneItem.setQuantity(2);

        order1 = new Order();
        order1.setId(1L);
        order1.setCustomer(customer);
        order1.setCreatedAt(LocalDateTime.now());
        order1.setItems(new ArrayList<>());
        order1.addItem(laptopItem);

        order2 = new Order();
        order2.setId(2L);
        order2.setCustomer(customer);
        order2.setCreatedAt(LocalDateTime.now());
        order2.setItems(new ArrayList<>());
        order2.addItem(smartphoneItem);

        orders = Arrays.asList(order1, order2);
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].customer.id", is(1)))
                .andExpect(jsonPath("$[0].customer.name", is("John")))
                .andExpect(jsonPath("$[0].items", hasSize(1)))
                .andExpect(jsonPath("$[0].items[0].shopItem.title", is("Laptop")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].items", hasSize(1)))
                .andExpect(jsonPath("$[1].items[0].shopItem.title", is("Smartphone")));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void getOrderById_WithValidId_ShouldReturnOrder() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(order1);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customer.id", is(1)))
                .andExpect(jsonPath("$.customer.name", is("John")))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].shopItem.title", is("Laptop")));

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void getOrdersByCustomer_ShouldReturnCustomerOrders() throws Exception {
        when(orderService.getOrdersByCustomer(1L)).thenReturn(orders);

        mockMvc.perform(get("/api/orders/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(orderService, times(1)).getOrdersByCustomer(1L);
    }

    @Test
    void createOrder_WithValidData_ShouldReturnCreatedOrder() throws Exception {
        Order newOrder = new Order();
        newOrder.setCustomer(customer);

        OrderItem newItem = new OrderItem();
        newItem.setShopItem(laptop);
        newItem.setQuantity(1);

        newOrder.setItems(new ArrayList<>());
        newOrder.addItem(newItem);

        Order savedOrder = new Order();
        savedOrder.setId(3L);
        savedOrder.setCustomer(customer);
        savedOrder.setCreatedAt(LocalDateTime.now());
        savedOrder.setItems(new ArrayList<>());

        OrderItem savedItem = new OrderItem();
        savedItem.setId(3L);
        savedItem.setShopItem(laptop);
        savedItem.setQuantity(1);
        savedOrder.addItem(savedItem);

        when(orderService.createOrder(any(Order.class))).thenReturn(savedOrder);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.customer.id", is(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].shopItem.title", is("Laptop")));

        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    void updateOrder_WithValidData_ShouldReturnUpdatedOrder() throws Exception {
        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setCustomer(customer);

        OrderItem updatedItem = new OrderItem();
        updatedItem.setShopItem(smartphone);
        updatedItem.setQuantity(3);

        updatedOrder.setItems(new ArrayList<>());
        updatedOrder.addItem(updatedItem);

        when(orderService.updateOrder(eq(1L), any(Order.class))).thenReturn(updatedOrder);

        mockMvc.perform(put("/api/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].shopItem.title", is("Smartphone")))
                .andExpect(jsonPath("$.items[0].quantity", is(3)));

        verify(orderService, times(1)).updateOrder(eq(1L), any(Order.class));
    }

    @Test
    void deleteOrder_WithValidId_ShouldReturnNoContent() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(1L);
    }

    @Test
    void addItemToOrder_ShouldReturnUpdatedOrder() throws Exception {
        OrderItem newItem = new OrderItem();
        newItem.setShopItem(smartphone);
        newItem.setQuantity(1);

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setCustomer(customer);
        updatedOrder.setItems(new ArrayList<>());

        OrderItem laptopItem = new OrderItem();
        laptopItem.setId(1L);
        laptopItem.setShopItem(laptop);
        laptopItem.setQuantity(1);
        updatedOrder.addItem(laptopItem);

        OrderItem smartphoneItem = new OrderItem();
        smartphoneItem.setId(3L);
        smartphoneItem.setShopItem(smartphone);
        smartphoneItem.setQuantity(1);
        updatedOrder.addItem(smartphoneItem);

        when(orderService.addItemToOrder(eq(1L), any(OrderItem.class))).thenReturn(updatedOrder);

        mockMvc.perform(post("/api/orders/1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].shopItem.title", is("Laptop")))
                .andExpect(jsonPath("$.items[1].shopItem.title", is("Smartphone")));

        verify(orderService, times(1)).addItemToOrder(eq(1L), any(OrderItem.class));
    }

    @Test
    void removeItemFromOrder_ShouldReturnUpdatedOrder() throws Exception {
        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setCustomer(customer);
        updatedOrder.setItems(new ArrayList<>());

        when(orderService.removeItemFromOrder(1L, 1L)).thenReturn(updatedOrder);

        mockMvc.perform(delete("/api/orders/1/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.items", hasSize(0)));

        verify(orderService, times(1)).removeItemFromOrder(1L, 1L);
    }
}
