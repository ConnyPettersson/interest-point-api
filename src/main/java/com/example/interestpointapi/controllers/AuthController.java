package com.example.interestpointapi.controllers;

import com.example.interestpointapi.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

  if (username == null || username.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Username is required");
    }

    if (password == null || password.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Password is required");
    }

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password)
    );

    var authorities = authentication.getAuthorities().stream()
        .map(grantedAuthority -> grantedAuthority.getAuthority())
        .toList();

    String token = jwtUtil.generateToken(authentication.getName(), authorities);
    return ResponseEntity.ok(token);
}
}
