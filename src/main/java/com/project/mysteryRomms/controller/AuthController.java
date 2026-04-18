package com.project.mysteryRomms.controller;

import com.project.mysteryRomms.model.enums.UserRepository;
import com.project.mysteryRomms.repository.RoleRepository;
import com.project.mysteryRomms.security.AuthenticationService;
import com.project.mysteryRomms.security.JwtService;
import com.project.mysteryRomms.dto.request.ResetPasswordRequest;
import com.project.mysteryRomms.dto.response.LoginResponse;
import com.project.mysteryRomms.model.entity.Role;
import com.project.mysteryRomms.model.entity.User;
import com.project.mysteryRomms.model.enums.RoleEnum;
import com.project.mysteryRomms.exception.GlobalResponseHandler;
import com.project.mysteryRomms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.project.mysteryRomms.service.EmailServiceJava;

import java.util.Optional;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailServiceJava emailService;

    private AuthenticationService authenticationService = null;
    private final JwtService jwtService;

    public AuthController(JwtService jwtService, AuthenticationService authentication) {
        this.jwtService = jwtService;
        this.authenticationService = authentication;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody User user, HttpServletRequest request) {
        Optional<User> foundedUser = userRepository.findByEmail(user.getEmail());

        if (foundedUser.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("No se ha encontrado el usuario",
                    HttpStatus.UNAUTHORIZED, request);
        }

        User authenticatedUser = foundedUser.get();

        // Check if the user is disabled
        if (!authenticatedUser.isEnabled()) {
            return new GlobalResponseHandler().handleResponse("Usuario deshabilitado",
                    HttpStatus.FORBIDDEN, request);
        }

        // Proceed with authentication if the user is not disabled
        authenticatedUser = authenticationService.authenticate(user);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setAuthUser(authenticatedUser);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role not found");
        }
        user.setRole(optionalRole.get());
        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody User user) {
        String token = userService.createPasswordResetToken(user);
        if (token == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        String resetLink = "Enter in this link to reset your password: " + "http://localhost:4200/reset-password"
                + " Your token is " + token;
        emailService.sendEmail(user.getEmail(), "Password Reset Request",
                "To reset your password, click the link below:\n" + resetLink);
        return ResponseEntity.ok("Password reset link sent to your email");
    }

    @PutMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable String token, @RequestBody ResetPasswordRequest request) {
        boolean result = userService.resetPassword(token, request.getNewPassword());
        if (!result) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
        return ResponseEntity.ok("Password reset successfully");
    }
}
