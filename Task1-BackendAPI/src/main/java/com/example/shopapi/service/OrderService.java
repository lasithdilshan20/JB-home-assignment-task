package com.example.shopapi.service;

import com.example.shopapi.model.Customer;
import com.example.shopapi.model.Order;
import com.example.shopapi.model.OrderItem;
import com.example.shopapi.model.ShopItem;
import com.example.shopapi.repository.CustomerRepository;
import com.example.shopapi.repository.OrderRepository;
import com.example.shopapi.repository.ShopItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ShopItemRepository shopItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ShopItemRepository shopItemRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.shopItemRepository = shopItemRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    public List<Order> getOrdersByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId));
        return orderRepository.findByCustomer(customer);
    }

    public List<Order> getOrdersByDateRange(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByCreatedAtBetween(start, end);
    }

    @Transactional
    public Order createOrder(Order order) {
        Customer customer = customerRepository.findById(order.getCustomer().getId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + order.getCustomer().getId()));
        order.setCustomer(customer);

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        List<OrderItem> validatedItems = order.getItems().stream()
                .map(item -> {
                    ShopItem shopItem = shopItemRepository.findById(item.getShopItem().getId())
                            .orElseThrow(() -> new EntityNotFoundException("Shop item not found with id: " + item.getShopItem().getId()));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setShopItem(shopItem);
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.getItems().clear();
        validatedItems.forEach(order::addItem);

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(Long id, Order orderDetails) {
        Order order = getOrderById(id);

        if (orderDetails.getCustomer() != null && orderDetails.getCustomer().getId() != null) {
            Customer customer = customerRepository.findById(orderDetails.getCustomer().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + orderDetails.getCustomer().getId()));
            order.setCustomer(customer);
        }

        if (orderDetails.getItems() != null && !orderDetails.getItems().isEmpty()) {
            order.getItems().clear();
            orderDetails.getItems().forEach(item -> {
                ShopItem shopItem = shopItemRepository.findById(item.getShopItem().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Shop item not found with id: " + item.getShopItem().getId()));

                OrderItem orderItem = new OrderItem();
                orderItem.setShopItem(shopItem);
                orderItem.setQuantity(item.getQuantity());
                orderItem.setOrder(order);
                order.addItem(orderItem);
            });
        }

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Transactional
    public Order addItemToOrder(Long orderId, OrderItem item) {
        Order order = getOrderById(orderId);

        ShopItem shopItem = shopItemRepository.findById(item.getShopItem().getId())
                .orElseThrow(() -> new EntityNotFoundException("Shop item not found with id: " + item.getShopItem().getId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setShopItem(shopItem);
        orderItem.setQuantity(item.getQuantity());
        orderItem.setOrder(order);

        order.addItem(orderItem);

        return orderRepository.save(order);
    }

    @Transactional
    public Order removeItemFromOrder(Long orderId, Long itemId) {
        Order order = getOrderById(orderId);

        OrderItem itemToRemove = order.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Order item not found with id: " + itemId));

        order.removeItem(itemToRemove);

        return orderRepository.save(order);
    }
}
