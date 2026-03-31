package com.jsp.Ecommerece.Controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.jsp.Ecommerece.dto.CustomerDto;
import com.jsp.Ecommerece.dto.LoginDto;
import com.jsp.Ecommerece.dto.MerchantDto;
import com.jsp.Ecommerece.dto.OtpDto;
import com.jsp.Ecommerece.dto.PasswordDto;
import com.jsp.Ecommerece.serviece.AuthServiece;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthServiece authserviece;
	
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> login(@Valid @RequestBody  LoginDto loginDto){
		return authserviece.login(loginDto.getEmail(),loginDto.getPassword());
	}
	
	@GetMapping("/me")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAnyRole('ADMIN','USER','MERCHANT')")
	public Map<String, Object> viewLoggedInUser(Principal principal){
		return authserviece.viewUser(principal.getName());
	}
	
	@PatchMapping("/password")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAnyRole('ADMIN','USER','MERCHANT')")
	public Map<String,Object> updatePassword(Principal principal,@Valid @RequestBody PasswordDto passwordDto){
		return authserviece.updatepassword(principal.getName(),passwordDto.getOldPassword(),passwordDto.getNewPassword());
	}
	@PostMapping("/merchant/register")
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, Object> registerMerchantAccount(@Valid @RequestBody MerchantDto MerchantDto){
		return authserviece.registerMerchant(MerchantDto);
	}
	
	@PatchMapping("/merchant/otp")
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object>verifyOtp(@Valid @RequestBody OtpDto dto){
		return authserviece.verifyMerchantOtp(dto);
	}
	@PatchMapping("/merchant/resend/{email}")
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> resendOtp(@PathVariable String email){
		return authserviece.resendMerchantOtp(email);
	}
	
	@PostMapping("/customer/register")
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, Object> registerCustomerAccount(@Valid @RequestBody CustomerDto customerDto){
		return authserviece.registerCustomer(customerDto);
	}
	
	@PatchMapping("/customer/otp")
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object>  verifyCustomerOtp(@Valid @RequestBody OtpDto dto){
		return authserviece.verifyCustomerOtp(dto);
		}
	
	@PatchMapping("/customer/resend/{email}")
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object >resendCustomerOtp(@PathVariable String email){
		return authserviece.resendCustomerOtp(email);
	}
	

}
