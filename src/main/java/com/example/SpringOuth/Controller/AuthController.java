package com.example.SpringOuth.Controller;

import com.example.SpringOuth.Repository.UserRepository;
import com.example.SpringOuth.config.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
public class AuthController {
    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;

    public AuthController(UserRepository userRepository, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/")
    public void handleOAuthSuccess(Authentication authentication, HttpServletResponse response) throws IOException {
        // Get user details from the authentication object
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String role = "USER"; // Default role

        // Check if the user already exists in the database
        Optional<com.example.SpringOuth.Model.User> existingUser = userRepository.findByEmail(email);
        com.example.SpringOuth.Model.User user;

        if (existingUser.isEmpty()) {
            // Create a new user if the user does not exist
            user = new com.example.SpringOuth.Model.User();
            user.setEmail(email);
            user.setRole(role);
            userRepository.save(user);
        } else {
            user = existingUser.get(); // Get existing user
        }

        // Generate JWT token for the user
        String token = jwtUtils.generateToken(user);

        // Send the JWT token in the response
        response.setHeader("Authorization", "Bearer " + token);
        response.getWriter().write("JWT Token: " + token);
    }
}
