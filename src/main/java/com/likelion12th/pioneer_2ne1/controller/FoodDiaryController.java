package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.FoodDiaryDto;
import com.likelion12th.pioneer_2ne1.service.FoodDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fooddiaries")
@CrossOrigin(origins = "http://localhost:3000") // React 앱 주소
public class FoodDiaryController {

    @Autowired
    private FoodDiaryService foodDiaryService;

    @GetMapping
    public ResponseEntity<List<FoodDiaryDto>> getAllFoodDiaries() {
        List<FoodDiaryDto> foodDiaries = foodDiaryService.getAllFoodDiariesForCurrentUser();
        return ResponseEntity.ok(foodDiaries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDiaryDto> getFoodDiaryById(@PathVariable Long id) {
        FoodDiaryDto foodDiaryDto = foodDiaryService.getFoodDiaryByIdForCurrentUser(id);
        if (foodDiaryDto != null) {
            return ResponseEntity.ok(foodDiaryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{date}")
    public ResponseEntity<FoodDiaryDto> createFoodDiary(@RequestBody FoodDiaryDto foodDiaryDto) {
        FoodDiaryDto createdFoodDiary = foodDiaryService.saveFoodDiary(foodDiaryDto);
        return ResponseEntity.ok(createdFoodDiary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodDiaryDto> updateFoodDiary(@PathVariable Long id, @RequestBody FoodDiaryDto foodDiaryDto) {
        foodDiaryDto.setId(id);
        FoodDiaryDto updatedFoodDiary = foodDiaryService.saveFoodDiary(foodDiaryDto);
        return ResponseEntity.ok(updatedFoodDiary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodDiary(@PathVariable Long id) {
        foodDiaryService.deleteFoodDiary(id);
        return ResponseEntity.noContent().build();
    }
}
