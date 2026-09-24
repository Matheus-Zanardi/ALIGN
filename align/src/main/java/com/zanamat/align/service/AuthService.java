package com.zanamat.align.service;

import com.zanamat.align.dto.LoginRequestDTO;
import com.zanamat.align.dto.LoginResponseDTO;
import com.zanamat.align.dto.RegisterRequestDTO;
import com.zanamat.align.dto.UserResponseDTO;
import com.zanamat.align.model.User;
import com.zanamat.align.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponseDTO register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        String encryptedPassword =
                passwordEncoder.encode(request.getPassword());

        user.setPassword(encryptedPassword);

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    public LoginResponseDTO login(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalStateException("Invalid email or password")
                );

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new IllegalStateException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponseDTO(
                token,
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}