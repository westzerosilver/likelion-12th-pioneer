package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.FoodDiaryDto;
import com.likelion12th.pioneer_2ne1.service.FoodDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/{date}")
    public ResponseEntity<List<FoodDiaryDto>> getFoodDiariesByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<FoodDiaryDto> foodDiaries = foodDiaryService.getFoodDiariesByDateForCurrentUser(date);
        return ResponseEntity.ok(foodDiaries);
    }


//    @PostMapping("/{date}")
//    public ResponseEntity<FoodDiaryDto> createFoodDiary(@RequestBody FoodDiaryDto foodDiaryDto) {
//        FoodDiaryDto createdFoodDiary = foodDiaryService.saveFoodDiary(foodDiaryDto);
//        return ResponseEntity.ok(createdFoodDiary);
//    }

    @PostMapping("/{date}")
    public ResponseEntity<FoodDiaryDto> createFoodDiary(@RequestBody FoodDiaryDto foodDiaryDto,
                                                        @AuthenticationPrincipal UserDetails userDetails,
                                                        @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        foodDiaryDto.setDate(date); // 날짜 설정
        FoodDiaryDto createdFoodDiary = foodDiaryService.saveFoodDiary(foodDiaryDto, userDetails.getUsername());
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
