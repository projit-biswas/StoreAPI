package com.projit.storeApp.products;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
			throw new ProductNotFoundException("Product not found");
		}
		productMapper.update(productDto, product);
		product.setCategory(category);
		productRepository.save(product);
		productDto.setId(product.getId());
		return productMapper.toDto(product);
	}

	public ProductDto getProductById(Long id) {
		var product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
		return new ProductDto(
				product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory().getId());
	}

	public void deleteProduct(Long id) {
		var product = productRepository.findById(id).orElse(null);
		if (product == null) {
			throw new ProductNotFoundException("Product not found");
		}
		productRepository.delete(product);
	}


}
