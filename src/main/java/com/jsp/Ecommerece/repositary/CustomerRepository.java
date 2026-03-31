package com.jsp.Ecommerece.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.Ecommerece.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
