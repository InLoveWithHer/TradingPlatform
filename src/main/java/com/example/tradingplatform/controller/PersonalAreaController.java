package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.Advertisement;
import com.example.tradingplatform.entity.User;
import com.example.tradingplatform.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PersonalAreaController {
    private final UserService userService;

    public PersonalAreaController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/personal-area")
    @PreAuthorize("isAuthenticated()")
    public String personalArea(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getByEmail(email);
        if (user == null) {
            return "redirect:/login";
        }

        String name = user.getName();
        if (name == null) {
            return "redirect:/namePhone";
        } else {
            model.addAttribute("showModal", false);
        }

        Page<Advertisement> advertisements = userService.getAdvertisementsForUser(user, page, pageSize);
        model.addAttribute("user", user);
        model.addAttribute("advertisements", advertisements.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", advertisements.getTotalPages());
        return "personal-area";
    }

    @GetMapping("/namePhone")
    @PreAuthorize("isAuthenticated()")
    public String userNameAndPhone() {
        return "namePhoneForm";
    }

    @PostMapping("/namePhone")
    public ModelAndView saveUserNameAndPhone(@RequestParam String name, @RequestParam String phone) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getByEmail(email);
        if (user == null) {
            return new ModelAndView("error");
        }
        user.setName(name);
        user.setPhone(phone);
        userService.saveUser(user);
        return new ModelAndView("redirect:/personal-area");
    }




}
