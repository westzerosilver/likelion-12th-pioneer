package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.FoodCompleteDto;
import com.likelion12th.pioneer_2ne1.service.FoodCompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/foodcomplete")
@CrossOrigin(origins = "http://localhost:3000") // React 앱 주소
public class FoodCompleteController {

    @Autowired
    private FoodCompleteService foodCompleteService;

    @PostMapping
    public ResponseEntity<Void> submitFoodComplete(@RequestBody FoodCompleteDto foodCompleteDto) {
        foodCompleteService.saveFoodComplete(foodCompleteDto);
        return ResponseEntity.ok().build();
    }
}
