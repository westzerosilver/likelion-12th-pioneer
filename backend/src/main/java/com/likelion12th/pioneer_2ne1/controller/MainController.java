package com.likelion12th.pioneer_2ne1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {
    @GetMapping(value = "/")
    public String main() {
        return "main";
    }
    @GetMapping("/api/hello")
    public String test() {
        return "Hello, world!";
    }
}
