package com.projit.storeApp.products;

import com.projit.storeApp.controllers.ErrorDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final CategoryRepository categoryRepository;

	public List<ProductDto> getAllProducts(Byte categoryId) {
		if (categoryId == null) {
			return productRepository.findAll()
					.stream().map(productMapper::toDto).toList();
		}
		return productRepository.findByCategoryId(categoryId)
				.stream().map(productMapper::toDto).toList();
	}

	public ProductDto createProduct(ProductDto productDto) {
		var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
		if (category == null) {
			throw new CategoryNotFoundException("category not found");
		}
		var product = productMapper.toEntity(productDto);
		product.setCategory(category);
		productRepository.save(product);
		productDto.setId(product.getId());
		return productMapper.toDto(product);
	}

	public ProductDto updateProduct(Long id, ProductDto productDto) {
		var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
		if (category == null) {
			throw new CategoryNotFoundException("Category not found");
		}
		var product = productRepository.findById(id).orElse(null);
		if (product == null) {
			throw new ProductNotFoundException();
		}
		productMapper.update(productDto, product);
		product.setCategory(category);
		productRepository.save(product);
		productDto.setId(product.getId());
		return productMapper.toDto(product);
	}

	public ProductDto getProductById(Long id) {
		var product = productRepository.findById(id).orElse(null);
		if (product == null) {
			throw new ProductNotFoundException();
		}
		return new ProductDto(
				product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory().getId());
	}

	public void deleteProduct(Long id) {
		var product = productRepository.findById(id).orElse(null);
		if (product == null) {
			throw new ProductNotFoundException();
		}
		productRepository.delete(product);
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ErrorDto> handleCategoryNotFound() {
		return ResponseEntity.badRequest().body(new ErrorDto("Category not found"));
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorDto> handleProductNotFound() {
		return ResponseEntity.notFound().build();
	}
}
