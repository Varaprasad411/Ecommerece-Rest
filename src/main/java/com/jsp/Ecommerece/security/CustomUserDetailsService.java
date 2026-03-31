//package com.jsp.Ecommerece.security;
//
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import com.jsp.Ecommerece.entity.User;
//import com.jsp.Ecommerece.repositary.UserRepositary;
//
//import lombok.RequiredArgsConstructor;
//
//
//@Component
//@RequiredArgsConstructor
//public class CustomUserDetailsService  implements UserDetailsService{
//
//	
//	 private final UserRepositary userrepositary;
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		User user = userrepositary.findByEmail(email).orElseThrow(() -> new BadCredentialsException("Invalid Email"));
//		return new CustomUserDetail(user);
//	}
//
//}
package com.jsp.Ecommerece.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.jsp.Ecommerece.entity.User;
import com.jsp.Ecommerece.repositary.UserRepositary;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositary userrepositary;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userrepositary.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );
        return new CustomUserDetail(user);
    }
}
