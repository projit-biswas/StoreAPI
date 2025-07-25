package com.projit.storeApp.controllers;

import com.projit.storeApp.dtos.ProductDto;
import com.projit.storeApp.mapper.ProductMapper;
import com.projit.storeApp.repositories.CategoryRepository;
import com.projit.storeApp.repositories.ProductRepository;
import com.projit.storeApp.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
@Tag(name = "Products")
public class ProductController {
	private final ProductService productService;
	private final ProductMapper productMapper;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	@GetMapping
	public List<ProductDto> getAllProducts(@RequestParam(required = false, name = "categoryId") Byte categoryId){
		return productService.getAllProducts(categoryId);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
		var product = productService.getProductById(id);
		if (product == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(product);
	}

	@PostMapping
	public ResponseEntity<ProductDto> createProducts(@RequestBody ProductDto productDto, UriComponentsBuilder uriBuilder){
		var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
		if (category == null) {
			ResponseEntity.badRequest().build();
		}
		var product = productMapper.toEntity(productDto);
		product.setCategory(category);
		productRepository.save(product);
		productDto.setId(product.getId());
		var uri = uriBuilder.path("/api/v1/products/{id}").buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(uri).body(productDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
		var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
		if (category == null) {
			ResponseEntity.badRequest().build();
		}
		var product = productRepository.findById(id).orElse(null);
		if (product == null) {
			return ResponseEntity.notFound().build();
		}
		productMapper.update(productDto, product);
		product.setCategory(category);
		productRepository.save(product);
		productDto.setId(product.getId());
		return ResponseEntity.ok(productMapper.toDto(product));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id){
		var product = productRepository.findById(id).orElse(null);
		if (product == null) {
			return ResponseEntity.notFound().build();
		}
		productRepository.delete(product);
		return ResponseEntity.noContent().build();
	}
}
