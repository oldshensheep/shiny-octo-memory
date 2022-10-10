package com.example.demo.sec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RestController
public class Yes {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers("/**").permitAll()
                )
                .formLogin().disable();
        return http.build();
    }

    @GetMapping("/who")
    public String getName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/login")
    public String login() {
        var context = SecurityContextHolder.createEmptyContext();
        var authentication =
                new TestingAuthenticationToken("miku", "password", "ROLE_USER");
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
