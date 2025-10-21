package iuh.fit.se.nguyenphihung.config;

import iuh.fit.se.nguyenphihung.entities.Customer;
import iuh.fit.se.nguyenphihung.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SercurityConfig {
    private final CustomerService customerService;
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return name -> {
            Customer customer = customerService.findByUsername(name);
            return new User(
                    customer.getName(),
                    passwordEncoder().encode(customer.getPassword()),
                    customer.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.toString()))
                            .collect(Collectors.toList())
            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**", "/test").permitAll()
                        .requestMatchers("/", "/products", "/products/detail/**").permitAll()
                        .requestMatchers("/categories", "/categories/detail/**").hasAnyRole("ADMIN")
                        .requestMatchers("/customers", "/customers/detail/**").hasAnyRole("ADMIN")
                        .requestMatchers("/orders", "/orders/detail/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/products/new", "/products/edit/", "/products/save/").hasRole("ADMIN")
                        .requestMatchers("/categories/new", "/categories/edit/", "/categories/save/").hasRole("ADMIN")
                        .requestMatchers("/customers/new", "/customers/edit/", "/customers/save/").hasRole("ADMIN")
                        .requestMatchers("/orders/new", "/orders/edit/", "/orders/save/").hasRole("ADMIN")
                        .requestMatchers("/cart/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
}
