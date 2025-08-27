package com.example.todolist.config;

import com.example.todolist.filter.JwtAuthfilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthfilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    //Main Security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tắt CSRF cho API testing
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless cho API
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll() // Cho phép tất cả API endpoints không cần authentication
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Cho phép static resources
                .requestMatchers("/health", "/actuator/**").permitAll() // Cho phép health check
                .requestMatchers("/", "/login", "/public/**").permitAll() // Cho phép trang chủ và login
                .anyRequest().permitAll() // Tạm thời cho phép tất cả để test
            )
            .httpBasic(Customizer.withDefaults()) // Sử dụng HTTP Basic thay vì form login
            .formLogin(form -> form.disable()); // Tắt form login để tránh redirect

        return http.build();
    }
    //Hashing password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // thuc hien logic xác thực người dùng
    /*
    - khi người dùng nhập username và password, Spring Security sẽ đóng gói vào 1 object UsernamePasswordAuthenticationToken
    - Gọi UserdetailsService để load user theo username từ DB
    - So sánh password nhập vào với password trong DB (đã mã hóa)
    - Nếu khớp, trả về Authentication object với thông tin user đã xác thực
    - Nếu không khớp, ném exception xác thực thất bại

    * */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Cung cấp cách load user từ DB
        provider.setPasswordEncoder(passwordEncoder()); // Cung cấp cách mã hóa và so sánh
        return provider;
    }
//    lấy AuthenticationManager từ Spring Security (thông qua AuthenticationConfiguration).
//    Đăng ký nó làm bean, để bạn có thể inject vào controller / service và gọi authenticate(...) khi cần xác thực user.
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

}
