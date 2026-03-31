package com.jsp.Ecommerece.repositary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.Ecommerece.entity.User;

public interface UserRepositary extends JpaRepository<User, Integer> {

	
	boolean existsByEmail(String adminEmail);
	
	boolean existsByEmailOrMobile(String mail,Long mobile);

	Optional<User> findByEmail(String email);

	
}
