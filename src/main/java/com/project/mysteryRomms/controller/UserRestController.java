package com.project.mysteryRomms.controller;

import com.project.mysteryRomms.dto.request.UserSessionStatsRequest;
import com.project.mysteryRomms.dto.response.UserStatsSummaryResponse;
import com.project.mysteryRomms.dto.response.Meta;
import com.project.mysteryRomms.exception.GlobalResponseHandler;
import com.project.mysteryRomms.model.entity.Role;
import com.project.mysteryRomms.model.entity.User;
import com.project.mysteryRomms.model.enums.RoleEnum;
import com.project.mysteryRomms.repository.RoleRepository;
import com.project.mysteryRomms.repository.RepositoryUser;
import com.project.mysteryRomms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private RepositoryUser repositoryUser;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<User> ordersPage = repositoryUser.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(ordersPage.getTotalPages());
        meta.setTotalElements(ordersPage.getTotalElements());
        meta.setPageNumber(ordersPage.getNumber() + 1);
        meta.setPageSize(ordersPage.getSize());

        return new GlobalResponseHandler().handleResponse("Users retrieved successfully",
                ordersPage.getContent(), HttpStatus.OK, meta);
    }
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> findUserById(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundUser = repositoryUser.findById(userId);
        if (foundUser.isPresent()) {
            return new GlobalResponseHandler().handleResponse("User found",
                    foundUser.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody User user, HttpServletRequest request) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(optionalRole.get());
        repositoryUser.save(user);
        return new GlobalResponseHandler().handleResponse("User creado exitosamente",
                user, HttpStatus.OK, request);
    }

    private boolean isValidPhotoUrl(String url) {
        String regex = "^(http://|https://)[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(/\\S*)?$";
        return url.matches(regex);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundUser = repositoryUser.findById(userId);
        //Role userRole = foundUser.get().getRole();
        if(foundUser.isPresent()) {

            if (!user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            foundUser.get().setName(user.getName());
            foundUser.get().setLastname(user.getLastname());
            foundUser.get().setEmail(user.getEmail());
            //foundUser.get().setRole(user.getRole());
            foundUser.get().setEnabled(user.isEnabled());
            //foundUser.get().setRole(userRole);

            foundUser.get().setRole(user.getRole());
            foundUser.get().setPhoneNumber(user.getPhoneNumber());
            foundUser.get().setAddress(user.getAddress());
            if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                if (isValidPhotoUrl(user.getPhotoUrl())) {
                    foundUser.get().setPhotoUrl(user.getPhotoUrl());
                } else {
                    return new GlobalResponseHandler().handleResponse("Invalid photo URL",
                            HttpStatus.BAD_REQUEST, request);
                }
            }
            User updatedUser = repositoryUser.save(foundUser.get());
            return new GlobalResponseHandler().handleResponse("User updated successfully",
                    updatedUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundOrder = repositoryUser.findById(userId);
        if(foundOrder.isPresent()) {
            repositoryUser.deleteById(userId);
            return new GlobalResponseHandler().handleResponse("User deleted successfully",
                    foundOrder.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Order id " + userId + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/{userId}/stats")
    public ResponseEntity<UserStatsSummaryResponse> getUserStats(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserStatsSummary(userId));
    }

    @PostMapping("/{userId}/session-stats")
    public ResponseEntity<UserStatsSummaryResponse> recordSessionStats(
            @PathVariable Long userId,
            @RequestBody UserSessionStatsRequest request) {
        return ResponseEntity.ok(userService.recordSessionStats(userId, request));
    }

    @PatchMapping("/{userId}/disable")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> disableUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> userOptional = repositoryUser.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEnabled(false); // Disable the user
            repositoryUser.save(user); // Save changes
            return new GlobalResponseHandler().handleResponse("User disabled successfully",
                    user, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

}
