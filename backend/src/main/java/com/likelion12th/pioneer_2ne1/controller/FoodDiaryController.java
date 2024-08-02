package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.FoodDiaryDto;
import com.likelion12th.pioneer_2ne1.service.FoodDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping("/{date}")
    public ResponseEntity<FoodDiaryDto> createFoodDiary(@RequestPart(name = "foodDiaryDto") FoodDiaryDto foodDiaryDto,
                                                        @RequestPart(value = "photoFile", required = false) MultipartFile photoFile,
                                                        @AuthenticationPrincipal UserDetails userDetails,
                                                        @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws IOException {
        foodDiaryDto.setDate(date); // 날짜 설정
        FoodDiaryDto createdFoodDiary = foodDiaryService.saveFoodDiary(foodDiaryDto, userDetails.getUsername(), photoFile);
        return ResponseEntity.ok(createdFoodDiary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodDiary(@PathVariable Long id) {
        foodDiaryService.deleteFoodDiary(id);
        return ResponseEntity.noContent().build();
    }
}
