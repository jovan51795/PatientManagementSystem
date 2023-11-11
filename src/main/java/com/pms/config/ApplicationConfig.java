package com.pms.config;

import com.pms.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepo;
    @Value("${aes.secretKey}")
    private String base64SecretKey;

    @Bean
    public UserDetailsService userDetailsService() {
//		return new UserDetailsService() {
//
//			@Override
//			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//				return null;
//			}
//		};

        return username -> userRepo.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey secretKey() throws Exception {
        if (base64SecretKey == null || base64SecretKey.isEmpty()) {
            throw new IllegalArgumentException("AES_SECRET_KEY is not set");
        }
        byte[] decodedKey = Base64.getDecoder().decode(base64SecretKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }


}