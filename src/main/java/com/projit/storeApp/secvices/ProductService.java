package com.projit.storeApp.secvices;

import com.projit.storeApp.dtos.ProductDto;
import com.projit.storeApp.mapper.ProductMapper;
import com.projit.storeApp.repositories.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
	private final ProductRepo productRepo;
	private final ProductMapper productMapper;

	public List<ProductDto> getAllProducts(Byte categoryId) {
		if (categoryId == null) {
			return productRepo.findAll()
					.stream().map(productMapper::toDto).toList();
		}
		return productRepo.findByCategoryId(categoryId)
				.stream().map(productMapper::toDto).toList();
	}

	public ProductDto getProductById(Long id) {
		var product = productRepo.findById(id).orElse(null);
		if (product != null){
			return new ProductDto(
					product.getId(),product.getName(),product.getDescription(),product.getPrice(),product.getCategory().getId());
		}
		return null;
	}
}
