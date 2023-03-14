package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.User;
import com.example.tradingplatform.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/personal-area")
    @PreAuthorize("isAuthenticated()")
    public String personalArea(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getByEmail(email);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "personal-area";
    }



    @GetMapping("/login")
    public String loginPage() {
        return "sign-in";
    }

    @PostMapping("/login")
    public String login(BindingResult result) {
        if (result.hasErrors()) {
            return "sign-in";
        }
        return "redirect:/personal-area";
    }



    @GetMapping("/register")
    public String registrationPage() {
        return "sign-up";
    }

    @PostMapping("/register")
    public String registration(User user) {
        userService.create(user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/login";
    }

    private static class LoginForm {
        public String email;
        public String password;
    }

}
