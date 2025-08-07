package com.projit.storeApp.products;

import com.projit.storeApp.common.ErrorDto;
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

	@GetMapping
	public List<ProductDto> getAllProducts(@RequestParam(required = false, name = "categoryId") Byte categoryId){
		return productService.getAllProducts(categoryId);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@PostMapping
	public ResponseEntity<ProductDto> createProducts(@RequestBody ProductDto productDto, UriComponentsBuilder uriBuilder){
		var product = productService.createProduct(productDto);
		var uri = uriBuilder.path("/api/v1/products/{id}").buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(uri).body(productDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
		return ResponseEntity.ok(productService.updateProduct(id, productDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id){
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
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
