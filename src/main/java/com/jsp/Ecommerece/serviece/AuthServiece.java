package com.jsp.Ecommerece.serviece;

import java.util.Map;

import com.jsp.Ecommerece.dto.CustomerDto;
import com.jsp.Ecommerece.dto.MerchantDto;
import com.jsp.Ecommerece.dto.OtpDto;

public interface AuthServiece {
	Map<String, Object> login(String email,String password);
	
	Map<String, Object> viewUser(String email);
	
	Map<String,Object >updatepassword(String email,String oldpassword,String newpassword);
	
	Map<String, Object> registerMerchant( MerchantDto merchantDto);

	Map<String, Object> verifyMerchantOtp(OtpDto dto);
	
	Map<String, Object> resendMerchantOtp(String email);
	
	Map<String, Object> registerCustomer( CustomerDto customerDto);

	Map<String, Object> verifyCustomerOtp(OtpDto dto);
	
	Map<String, Object> resendCustomerOtp(String email);

	
	
	
}
