package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public UserDetailsServiceImpl getUserDetailsService() {
        //step one
        return new UserDetailsServiceImpl();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        //step two
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        //step three
        return configuration.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        //step four
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //step five
        http.authorizeHttpRequests()
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll().and()
                .formLogin().loginPage("/login")
                .defaultSuccessUrl("/user/dashboard")
                .loginProcessingUrl("/signin")
                .and().csrf().disable();
        http.authenticationProvider(daoAuthenticationProvider());
        return http.build();
    }




}
