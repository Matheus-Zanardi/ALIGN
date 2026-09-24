package com.zanamat.align.controller;

import com.zanamat.align.dto.LoginRequestDTO;
import com.zanamat.align.dto.LoginResponseDTO;
import com.zanamat.align.dto.RegisterRequestDTO;
import com.zanamat.align.dto.UserResponseDTO;
import com.zanamat.align.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserResponseDTO register(
            @Valid @RequestBody RegisterRequestDTO request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        return authService.login(request);
    }
}