package com.project.eatwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/add-meal")
    public String addMeal() {
        return "add-meal";
    }

    @GetMapping("/edit-profile")
    public String editProfile() {
        return "edit-profile";
    }

    @GetMapping("/restaurant-map")
    public String restaurantMap() {
        return "restaurant-map";
    }

    @GetMapping("/meal-records")
    public String mealRecords() {
        return "meal-records";
    }

    @GetMapping("/report")
    public String report() {
        return "report";
    }
}
