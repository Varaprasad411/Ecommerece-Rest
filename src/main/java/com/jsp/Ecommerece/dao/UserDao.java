package com.jsp.Ecommerece.dao;

import java.util.List;
import java.util.NoSuchElementException;

import org.aspectj.lang.reflect.NoSuchAdviceException;
import org.springframework.stereotype.Repository;

import com.jsp.Ecommerece.entity.Customer;
import com.jsp.Ecommerece.entity.Merchant;
import com.jsp.Ecommerece.entity.User;
import com.jsp.Ecommerece.repositary.CustomerRepository;
import com.jsp.Ecommerece.repositary.MerchantRepository;
import com.jsp.Ecommerece.repositary.UserRepositary;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDao {
	private final UserRepositary userrepositary;
	private final MerchantRepository merchantRepository;
	private final CustomerRepository customerRepository;
	
	public boolean checkEmailAndMobileDuplicate(String email,Long mobile) {
		return userrepositary.existsByEmailOrMobile(email,mobile);
	}
	
	public User findByEmail(String email) {
		return userrepositary.findByEmail(email).orElseThrow();
	}
	
	public void save(User user) {
		userrepositary.save(user);
	}
	
	
	public void save(Merchant merchant){
		merchantRepository.save(merchant);
	}
	
	public void save(Customer customer){
	customerRepository.save(customer);
	}
	public List<Merchant>getAllMerchants(){
		List<Merchant> merchants =merchantRepository.findAll();
		if(merchants.isEmpty())
			throw new NoSuchElementException("No Customer Records Found");
		return merchants;
	}
	public List<Customer>getAllCustomers(){
		List<Customer> customers =customerRepository.findAll();
		if(customers.isEmpty())
			throw new NoSuchElementException("No Customer Records Found");
		return customers;
	}
	
}
