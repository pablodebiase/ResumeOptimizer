package org.resumeoptimizer.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http //.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .requestMatchers("/", "/home", "/register", "/login", "/guest").permitAll()
                .requestMatchers("/upload", "/process/**").hasAnyAuthority("ROLE_USER", "ROLE_GUEST")
                .requestMatchers("/return", "/stop").hasAnyAuthority("ROLE_USER", "ROLE_GUEST") // Allow /stop
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login").permitAll().defaultSuccessUrl("/upload", true).failureUrl("/login?error=true")
            )
            .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
