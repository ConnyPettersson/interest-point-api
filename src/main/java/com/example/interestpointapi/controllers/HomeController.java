package com.example.interestpointapi.controllers;

import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.services.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Intererst Point API!";
    }
}
