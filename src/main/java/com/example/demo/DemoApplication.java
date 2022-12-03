package com.example.demo;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@RestController
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("DemoApplication started");
    }

    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().permitAll())
                .securityContext((context) -> context
                        .requireExplicitSave(false))
                .formLogin().disable();
        return http.build();
    }

    @GetMapping("/who")
    public String getName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        var context = SecurityContextHolder.createEmptyContext();
        var authentication = new UsernamePasswordAuthenticationToken(UUID.randomUUID(), "password");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/login-not-working")
    public String login2(HttpServletRequest request, HttpServletResponse response) {
        var context = SecurityContextHolder.createEmptyContext();
        var authentication = new UsernamePasswordAuthenticationToken(UUID.randomUUID(), "password");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        // securityContextRepository.saveContext(context, request, response);
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
