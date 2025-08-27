package com.example.todolist.controller;

import com.example.todolist.service.JwtService;
import com.example.todolist.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.example.todolist.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final UserService userService;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    // POST /api/auth/login - Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username và password không được để trống"));
        }
        
        // Kiểm tra đăng nhập (tạm thời đơn giản)
        boolean isValid = userService.validateUser(username, password);
        
        if (isValid) {
            return ResponseEntity.ok(Map.of(
                "message", "Đăng nhập thành công",
                "username", username,
                "status", "success"
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Tên đăng nhập hoặc mật khẩu không đúng"));
        }
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody User authRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new RuntimeException("Invalid access");
        }
    }
    // GET /api/auth/check - Kiểm tra trạng thái đăng nhập
    @GetMapping("/check")
    public ResponseEntity<Map<String, String>> checkAuth() {
        return ResponseEntity.ok(Map.of(
            "status", "authenticated",
            "message", "API đang hoạt động"
        ));
    }
}
