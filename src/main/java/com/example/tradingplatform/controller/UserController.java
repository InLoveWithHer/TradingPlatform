package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.User;
import com.example.tradingplatform.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // Метод для проверки статуса аутентификации пользователя
    @GetMapping("/api/user/authenticated")
    public ResponseEntity<Boolean> isAuthenticated(Authentication authentication) {
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        return ResponseEntity.ok(isAuthenticated);
    }

    @GetMapping("/api/user/currentUserId")
    public Long getCurrentUserId(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserIdByEmail(email);
    }


}
