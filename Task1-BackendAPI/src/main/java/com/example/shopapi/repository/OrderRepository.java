package com.example.shopapi.repository;

import com.example.shopapi.model.Customer;
import com.example.shopapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}