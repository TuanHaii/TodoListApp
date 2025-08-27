package com.example.todolist.filter;

import com.example.todolist.service.JwtService;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@AllArgsConstructor
@Getter
@Setter
@Component
// Đảm bảo mỗi request chỉ được lọc một lần
public class JwtAuthfilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);// bỏ "Bearer "
            username = jwtService.extractUsername(token);// lấy username từ token
        }
        //xác thực người dùng nếu chưa được xác thực
        //Trong SecurityContextHolder đã có Authentication chưa? (nếu chưa thì mới xác thực).
        //Load user từ DB bằng userDetailsService.loadUserByUsername(username).
        //Gọi jwtService.validateToken(token, userDetails) để check:
        //Token có hợp lệ không (chữ ký đúng, chưa hết hạn).
        //Username trong token có trùng với DB không.
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserByUsername(username);
            if(jwtService.validateToken(token, userDetails)) {
                //Tạo UsernamePasswordAuthenticationToken (object này đại diện cho user đã đăng nhập).
                //Gán vào SecurityContextHolder.
                //Từ giờ, trong suốt lifecycle của request → Spring Security biết user nào đang gọi API, với quyền gì (authorities/roles).
               UsernamePasswordAuthenticationToken authToken =
                       new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // cho request tiếp tục đi qua các filter khác
        filterChain.doFilter(request, response);
    }
}
