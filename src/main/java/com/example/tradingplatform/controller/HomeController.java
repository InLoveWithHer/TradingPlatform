package com.example.tradingplatform.controller;

import com.example.tradingplatform.entity.Advertisement;
import com.example.tradingplatform.service.AdvertisementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final AdvertisementService advertisementService;

    public HomeController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping("/")
    public String home(Model model){
        List<Advertisement> latestAdvertisements = advertisementService.getLatestAdvertisements();
        model.addAttribute("advertisements", latestAdvertisements);
        return "home";
    }
}