package com.jsp.Ecommerece.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ProductDto {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	@NotEmpty(message = "name is Required")
	private String name;
	@NotEmpty(message = "brand is Required")
	private String brand;
	@NotEmpty(message = "category is Required")
	private String category;
	@NotEmpty(message = "description is Required")
	private String description;
	@NotNull(message = "price is Required")
	private double price;
	@NotNull(message = "stock is Required")
	private Integer stock;
	@NotEmpty(message = "size is Required")
	private String size;
	
	
}
