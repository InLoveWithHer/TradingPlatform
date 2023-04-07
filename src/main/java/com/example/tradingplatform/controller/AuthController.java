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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController( UserService userService) {
        this.userService = userService;
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

    @PostMapping("/checkEmail")
    @ResponseBody
    public Map<String, Boolean> checkEmailExists(@RequestParam String email) {
        boolean isEmailExists = userService.isEmailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isEmailExists", isEmailExists);
        return response;
    }


    private static class LoginForm {
        public String email;
        public String password;
    }

}
