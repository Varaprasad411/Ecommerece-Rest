package com.jsp.Ecommerece.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MerchantDto {
	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	@NotEmpty(message="name is required")
	private String name;
	@NotEmpty(message="Email is required")
	private String email;
	@NotEmpty(message="Password is required")
	private String password;
	@NotNull(message="Mobile no is required")
	private Long mobile;
	@NotEmpty(message="Address is required")
	private String address;
	@NotEmpty(message="gstNo is required")
	private String gstNo;
	@JsonProperty(access = Access.READ_ONLY)
	private String status;
	
}
