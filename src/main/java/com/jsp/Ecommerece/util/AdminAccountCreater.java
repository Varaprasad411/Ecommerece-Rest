package com.jsp.Ecommerece.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jsp.Ecommerece.entity.User;
import com.jsp.Ecommerece.enums.UserRole;
import com.jsp.Ecommerece.repositary.UserRepositary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminAccountCreater  implements CommandLineRunner{
	private final UserRepositary userRepositary;
	private final PasswordEncoder passwordEncoder;
	
	
	@Value("${admin.email}")
	private String adminEmail;
	@Value("${admin.password}")
	private String adminPassword;
	@Value("${admin.mobile}")
	private Long adminMobile;
	@Value("${admin.username}")
	private String adminUserName;
	
	
	@Override
	public void run(String... args) throws Exception
	{
		log.info("Admin Account Creation Started");
		if (userRepositary.existsByEmail(adminEmail))
		{
			log.info("Admin Account Already Exists");
		}
		else
		{
//			User user = new User();
//			user.setActive(true);
//			user.setEmail(adminEmail);
//			user.setMobile(adminMobile);
//			user.setUsername(adminUserName);
//			user.setPassword(passwordEncoder.encode(adminPassword));
//			user.setRole(UserRole.ADMIN);
//			userRepositary.save(user);
			 
					User user = new User(null, adminUserName, adminEmail, adminMobile, passwordEncoder.encode(adminPassword),
						UserRole.ADMIN, true);
				userRepositary.save(user);
					log.info("Admin Account Creaion Success - " + adminUserName);
				}
			log.info("Admin Account Creaion Success - " + adminUserName);
		
	}

}


