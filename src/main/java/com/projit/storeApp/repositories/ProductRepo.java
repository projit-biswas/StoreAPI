package com.projit.storeApp.repositories;

import com.projit.storeApp.dtos.ProductDto;
import com.projit.storeApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
	List<Product> findByCategoryId(Byte categoryId);
}
