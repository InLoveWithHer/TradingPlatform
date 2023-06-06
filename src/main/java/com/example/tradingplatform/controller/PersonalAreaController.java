package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.Advertisement;
import com.example.tradingplatform.entity.User;
import com.example.tradingplatform.service.OrderService;
import com.example.tradingplatform.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PersonalAreaController {
    private final UserService userService;
    private final OrderService orderService;

    public PersonalAreaController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
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

        if (user.getName() == null) {
            return "redirect:/namePhone";
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

    @GetMapping("/personal-area/personalData/editing")
    @PreAuthorize("isAuthenticated()")
    public String personalDataEditing(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.getByEmail(userEmail);

        model.addAttribute("user", user);

        return "personalDataEditing";
    }

    @PostMapping("/personal-area/personalData/editing")
    public String editUser(@RequestParam(value = "name", required = false, defaultValue = "") String name,
                           @RequestParam(value = "surname", required = false, defaultValue = "") String surname,
                           @RequestParam(value = "patronymic", required = false, defaultValue = "") String patronymic,
                           @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
                           @RequestParam(value = "email", required = false, defaultValue = "") String email,
                           @RequestParam(value = "password", required = false, defaultValue = "") String password) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.getByEmail(userEmail);

        if (!name.isEmpty()) {
            user.setName(name);
        }
        if (!surname.isEmpty()) {
            user.setSurname(surname);
        }
        if (!patronymic.isEmpty()) {
            user.setPatronymic(patronymic);
        }
        if (!phone.isEmpty()) {
            user.setPhone(phone);
        }
        if (!email.isEmpty()) {
            user.setEmail(email);
        }
        if (!password.isEmpty()) {
            userService.passwordEncode(user, password);
        }

        userService.updateUser(user);
        return "redirect:/personal-area";
    }

    @GetMapping("/personal-area/inactiveAdvertisements")
    @PreAuthorize("isAuthenticated()")
    public String showInactiveAdvertisements(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.getByEmail(userEmail);

        if (user == null) {
            return "redirect:/login";
        }

        Page<Advertisement> inactiveAdvertisements = userService.getInactiveAdvertisements(page, pageSize, user);
        model.addAttribute("user", user);
        model.addAttribute("inactiveAdvertisements", inactiveAdvertisements.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", inactiveAdvertisements.getTotalPages());

        return "inactiveAdvertisements";
    }

    @GetMapping("/personal-area/activeAdvertisements")
    @PreAuthorize("isAuthenticated()")
    public String showActiveAdvertisements(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.getByEmail(userEmail);

        if (user == null) {
            return "redirect:/login";
        }

        Page<Advertisement> activeAdvertisements = userService.getActiveAdvertisements(page, pageSize, user);
        model.addAttribute("user", user);
        model.addAttribute("activeAdvertisements", activeAdvertisements.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", activeAdvertisements.getTotalPages());

        return "activeAdvertisements";
    }

    @GetMapping("/personal-area/allOrders")
    @PreAuthorize("isAuthenticated()")
    public String showAllOrders(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.getByEmail(userEmail);

        if (user == null) {
            return "redirect:/login";
        }

        Page<Advertisement> advertisements = userService.getAdvertisementsForUser(user, page, pageSize);

        List<Advertisement> allAdvertisements = new ArrayList<>();
        advertisements.iterator().forEachRemaining(allAdvertisements::add);

        for (Advertisement advertisement : allAdvertisements) {
            int orderCount = orderService.getOrderCountByAdvertisement(advertisement.getId());
            advertisement.setOrderCount(orderCount);
        }

        model.addAttribute("user", user);
        model.addAttribute("advertisements", allAdvertisements);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", advertisements.getTotalPages());

        return "allOrders";
    }

}
