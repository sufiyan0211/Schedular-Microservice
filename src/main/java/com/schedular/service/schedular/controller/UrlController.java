package com.schedular.service.schedular.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    @GetMapping("/")
    public String status() {
        return "Scheduler Service is up";
    }
}
