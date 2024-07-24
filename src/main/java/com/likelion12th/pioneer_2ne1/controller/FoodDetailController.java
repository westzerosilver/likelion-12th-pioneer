package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.FoodDetailDto;
import com.likelion12th.pioneer_2ne1.service.FoodDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fooddiaries/detail")
@CrossOrigin(origins = "http://localhost:3000") // React 앱 주소
public class FoodDetailController {
    @Autowired
    private FoodDetailService foodDetailService;

    @GetMapping("/{id}")
    public ResponseEntity<FoodDetailDto> getFoodDiaryDetailById(@PathVariable Long id) {
        FoodDetailDto foodDetailDto = foodDetailService.getFoodDiaryDetailById(id);
        if (foodDetailDto != null) {
            return ResponseEntity.ok(foodDetailDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodDetailDto> updateFoodDiary(@PathVariable Long id, @RequestBody FoodDetailDto foodDetailDto) {
        FoodDetailDto updatedFoodDiary = foodDetailService.updateFoodDiary(id, foodDetailDto);
        return ResponseEntity.ok(updatedFoodDiary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodDiary(@PathVariable Long id) {
        foodDetailService.deleteFoodDiary(id);
        return ResponseEntity.noContent().build();
    }
}
