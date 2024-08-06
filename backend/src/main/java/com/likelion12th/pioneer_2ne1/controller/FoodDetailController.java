package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.FoodDetailDto;
import com.likelion12th.pioneer_2ne1.service.FoodDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/fooddiaries/detail")
@CrossOrigin(origins = "http://localhost:3000") // React 앱 주소
public class FoodDetailController {
    @Autowired
    private FoodDetailService foodDetailService;

    @GetMapping("/{date}/{id}")
    public ResponseEntity<FoodDetailDto> getFoodDiaryDetailById(@PathVariable("id") Long id,
                                                                @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        FoodDetailDto foodDetailDto = foodDetailService.getFoodDiaryDetailById(date, id);
        if (foodDetailDto != null) {
            return ResponseEntity.ok(foodDetailDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{date}/{id}")
    public ResponseEntity<FoodDetailDto> updateFoodDiary(@PathVariable("id") Long id,
                                                         @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                         @RequestPart(name = "foodDetailDto") FoodDetailDto foodDetailDto,
                                                         @RequestPart(value = "photoFile", required = false) MultipartFile photoFile) throws IOException {
        FoodDetailDto updatedFoodDiary = foodDetailService.updateFoodDiary(date, id, foodDetailDto, photoFile);
        return ResponseEntity.ok(updatedFoodDiary);
    }

    @DeleteMapping("/{date}/{id}")
    public ResponseEntity<Void> deleteFoodDiary(@PathVariable("id") Long id,
                                                @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        foodDetailService.deleteFoodDiary(date, id);
        return ResponseEntity.noContent().build();
    }
}
