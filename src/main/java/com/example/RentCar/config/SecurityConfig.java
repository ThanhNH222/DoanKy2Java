package com.example.RentCar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Cho phép login, register và static resources không cần đăng nhập
                        .requestMatchers("/","/car/**","/payment/**","/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        // Cho phép cả ADMIN và USER vào /admin/**
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "USER")
                        // Các request khác phải đăng nhập
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/admin", true)   // login xong về trang chủ
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")   // logout xong về lại login
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Tắt CSRF (chỉ nên dùng khi test)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
