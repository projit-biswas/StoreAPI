package com.projit.storeApp.repositories;

import com.projit.storeApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCategoryId(Byte categoryId);
}