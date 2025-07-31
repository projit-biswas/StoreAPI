package com.projit.storeApp.products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
	Long id;
	String name;
	String description;
	BigDecimal price;
	Byte categoryId;
}
