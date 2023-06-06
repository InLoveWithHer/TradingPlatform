package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.Advertisement;
import com.example.tradingplatform.entity.Order;
import com.example.tradingplatform.entity.User;
import com.example.tradingplatform.service.AdvertisementService;
import com.example.tradingplatform.service.OrderService;
import com.example.tradingplatform.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final AdvertisementService advertisementService;
    private final UserService userService;

    public OrderController(OrderService orderService, AdvertisementService advertisementService, UserService userService) {
        this.orderService = orderService;
        this.advertisementService = advertisementService;
        this.userService = userService;
    }

    @GetMapping("/advertisement/{advertisementId}/create-order")
    public String createOrder(Model model, @PathVariable Long advertisementId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getByEmail(email);

        model.addAttribute("user", user);
        model.addAttribute("advertisementId", advertisementId);
        return "create-order";
    }

    @PostMapping("/advertisement/{advertisementId}/create-order")
    public String createOrder(@RequestParam("name") String name,
                              @RequestParam("surname") String surname,
                              @RequestParam("patronymic") String patronymic,
                              @RequestParam("phone") String phone,
                              @PathVariable Long advertisementId) {
        Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);
        if (advertisement == null) {
            return "redirect:/";
        }

        Order order = new Order();
        order.setName(name);
        order.setSurname(surname);
        order.setPatronymic(patronymic);
        order.setPhone(phone);
        order.setAdvertisement(advertisement);

        orderService.createOrder(order);

        return "redirect:/";
    }

    @GetMapping("/personal-area/{advertisementId}/orders")
    @PreAuthorize("isAuthenticated()")
    public String showOrdersForAdvertisement(@PathVariable Long advertisementId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int pageSize,
                                             Model model) {
        Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);
        Page<Order> orders = orderService.getOrdersByAdvertisement(advertisement, page, pageSize);

        model.addAttribute("advertisement", advertisement);
        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());

        return "ordersForAdvertisement";
    }

    @PostMapping("auction/{auctionId}/createOrder/maxBid")
    public ResponseEntity<?> createOrder(@PathVariable Long auctionId) {
        try {
            orderService.createOrderByAuction(auctionId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create order.");
        }
    }

    @GetMapping("/advertisement/{advertisementId}/checkOrder")
    public ResponseEntity<Map<String, Boolean>> checkOrderExists(@PathVariable Long advertisementId) {
        boolean orderExists = orderService.checkOrderExists(advertisementId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("orderExists", orderExists);

        return ResponseEntity.ok(response);
    }

}
