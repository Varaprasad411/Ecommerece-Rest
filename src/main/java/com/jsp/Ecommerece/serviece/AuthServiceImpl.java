package com.jsp.Ecommerece.serviece;

import java.security.SecureRandom;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jsp.Ecommerece.dao.UserDao;
import com.jsp.Ecommerece.dto.CustomerDto;
import com.jsp.Ecommerece.dto.MerchantDto;
import com.jsp.Ecommerece.dto.OtpDto;
import com.jsp.Ecommerece.entity.Customer;
import com.jsp.Ecommerece.entity.Merchant;
import com.jsp.Ecommerece.entity.User;
import com.jsp.Ecommerece.enums.UserRole;
import com.jsp.Ecommerece.mapper.UserMapper;
import com.jsp.Ecommerece.security.JwtService;
import com.jsp.Ecommerece.util.EmailService;
import com.jsp.Ecommerece.util.RedisService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl  implements AuthServiece {

	
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final RedisService redisService;
	private final UserMapper userMapper;
	
	
	
	@Override
	public Map<String, Object> login(String email, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		UserDetails userDetails=userDetailsService.loadUserByUsername(email);
		String token=jwtService.generateToken(userDetails);
		return Map.of("message","Login Success","token",token);
	}
	@Override
	public Map<String, Object> viewUser(String email) {
		User user=userDao.findByEmail(email);
		return Map.of("message", "Data Found", "user", userMapper.toUserDto(user));
	}
	@Override
	public Map<String, Object> updatepassword(String email, String oldpassword, String newpassword) {
		User user=userDao.findByEmail(email);
		if(passwordEncoder.matches(oldpassword, user.getPassword())) {
			user.setPassword(passwordEncoder.encode(newpassword));
			userDao.save(user);
			return Map.of("message", "Password Updated Success", "user", userMapper.toUserDto(user));
		}
		throw new IllegalArgumentException("Old Password Not Mathing");
	}
	
	private Integer generateotp() {
		return new SecureRandom().nextInt(100000,1000000);
	}
	
	@Override
	public Map<String, Object> registerMerchant(MerchantDto merchantDto) {
		if(userDao.checkEmailAndMobileDuplicate(merchantDto.getEmail(), merchantDto.getMobile())) 
			throw new IllegalArgumentException("Already Account Exists with Email or Mobile");
		MerchantDto tempData=redisService.getTempMerchantData(merchantDto.getEmail());
		if (tempData != null)
			throw new IllegalArgumentException("Already Otp Sent First Verify It or After 30 minutes Try Again");
		Integer otp=generateotp();
		emailService.sendOtpEmail(otp, merchantDto.getName(), merchantDto.getEmail());
		redisService.saveOtp(otp, merchantDto.getEmail());
		redisService.saveTempMerchantData(merchantDto, merchantDto.getEmail());
		return Map.of("message","Otp Sent Succes Verify within 5 minutes");

		
	}
	
	@Override
	public Map<String, Object> verifyMerchantOtp(OtpDto dto) {
		Integer storedOtp=redisService.getOtp(dto.getEmail());
		MerchantDto merchantDto = redisService.getTempMerchantData(dto.getEmail());
		if(merchantDto == null)
			throw new IllegalArgumentException("No Account Exists Recreate Accounnnnt");
		if (storedOtp == null)
			throw new IllegalArgumentException("Otp Expired, Try Resending");
		if(storedOtp.equals(dto.getOtp())) {
			if(userDao.checkEmailAndMobileDuplicate(merchantDto.getEmail(), merchantDto.getMobile()))
				throw new IllegalArgumentException("Already Account Exists with Email or Mobile");
			User user=userMapper.toUserEntity(merchantDto);
			userDao.save(user);
			Merchant merchant=userMapper.toMerchantEntity(merchantDto, user);
			userDao.save(merchant);
			return Map.of("message", "Account Created Success", "user", userMapper.toMerchantDto(merchant));
		} else {
			throw new IllegalArgumentException("Otp Missmatch Try Again");
		}

	}
	@Override
	public Map<String, Object> resendMerchantOtp(String email) {
		MerchantDto merchantDto=redisService.getTempMerchantData(email);
		if(merchantDto == null)
			throw new IllegalArgumentException("No Account Exists recreate account");
		if(userDao.checkEmailAndMobileDuplicate(merchantDto.getEmail(), merchantDto.getMobile()))
		throw new IllegalArgumentException("Already Account Exists with Email or Mobile");
		int otp=generateotp();
		emailService.sendOtpEmail(otp, merchantDto.getName(), merchantDto.getEmail());
		redisService.saveOtp(otp, merchantDto.getEmail());
		return Map.of("message","Otp Resent Success");
		
	}



	@Override
	public Map<String, Object> registerCustomer(CustomerDto customerDto) {
		if (userDao.checkEmailAndMobileDuplicate(customerDto.getEmail(), customerDto.getMobile()))
			throw new IllegalArgumentException("Already Account Exists with Email or Mobile");
		CustomerDto tempData = redisService.getTempCustomerData(customerDto.getEmail());
		if (tempData != null)
			throw new IllegalArgumentException("Already Otp Sent First Verify It or After 30 minutes Try Again");
		Integer otp = generateotp();
		emailService.sendOtpEmail(otp, customerDto.getName(), customerDto.getEmail());
		redisService.saveOtp(otp, customerDto.getEmail());
		redisService.saveTempCustomerData(customerDto, customerDto.getEmail());
		return Map.of("message", "Otp Sent Succes Verify within 5 minutes");
	}
	
	
	@Override
	public Map<String, Object> verifyCustomerOtp(OtpDto dto) {
		Integer storedOtp = redisService.getOtp(dto.getEmail());
		CustomerDto customerDto = redisService.getTempCustomerData(dto.getEmail());
		if (customerDto == null)
			throw new IllegalArgumentException("No Account Exists, recreate account");
		if (storedOtp == null)
			throw new IllegalArgumentException("Otp Expired, Try Resending");
		if (storedOtp.equals(dto.getOtp())) {
			if (userDao.checkEmailAndMobileDuplicate(customerDto.getEmail(), customerDto.getMobile()))
				throw new IllegalArgumentException("Already Account Exists with Email or Mobile");
			User user = new User(null, customerDto.getName(), customerDto.getEmail(), customerDto.getMobile(),
					passwordEncoder.encode(customerDto.getPassword()), UserRole.CUSTOMER, true);
			userDao.save(user);
			Customer customer=new Customer();
			userDao.save(customer);
			return Map.of("message", "Account Created Success", "user", customer);
		} else {
			throw new IllegalArgumentException("Otp Missmatch Try Again");
		}

	}
	@Override
	public Map<String, Object> resendCustomerOtp(String email) {
				CustomerDto customerDto = redisService.getTempCustomerData(email);
			if (customerDto == null)
				throw new IllegalArgumentException("No Account Exists recreate account");
			if (userDao.checkEmailAndMobileDuplicate(customerDto.getEmail(), customerDto.getMobile()))
				throw new IllegalArgumentException("Already Account Exists with Email or Mobile");
			
			int otp = generateotp();
			emailService.sendOtpEmail(otp, customerDto.getName(), customerDto.getEmail());
			redisService.saveOtp(otp, customerDto.getEmail());
			return Map.of("message", "OTP Resent Success");
		}
	}
	
	



