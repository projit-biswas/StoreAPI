package com.projit.storeApp.repositories;

import com.projit.storeApp.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}