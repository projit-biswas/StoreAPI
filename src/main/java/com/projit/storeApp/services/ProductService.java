package com.projit.storeApp.services;

import com.projit.storeApp.dtos.ProductDto;
import com.projit.storeApp.mapper.ProductMapper;
import com.projit.storeApp.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	public List<ProductDto> getAllProducts(Byte categoryId) {
		if (categoryId == null) {
			return productRepository.findAll()
					.stream().map(productMapper::toDto).toList();
		}
		return productRepository.findByCategoryId(categoryId)
				.stream().map(productMapper::toDto).toList();
	}

	public ProductDto getProductById(Long id) {
		var product = productRepository.findById(id).orElse(null);
		if (product != null){
			return new ProductDto(
					product.getId(),product.getName(),product.getDescription(),product.getPrice(),product.getCategory().getId());
		}
		return null;
	}
}
