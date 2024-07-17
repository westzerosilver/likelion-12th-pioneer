package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.FoodDiaryDto;
import com.likelion12th.pioneer_2ne1.service.FoodDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fooddiaries")
public class FoodDiaryController {
    @Autowired
    private FoodDiaryService foodDiaryService;

    @GetMapping
    public String getAllFoodDiaries(Model model) {
        model.addAttribute("foodDiary", new FoodDiaryDto());
        model.addAttribute("foodDiaries", foodDiaryService.getAllFoodDiaries());
        return "fooddiary/fooddiaryPage";
    }

    @GetMapping("/{id}")
    public String getFoodDiaryById(@PathVariable Long id, Model model) {
        model.addAttribute("foodDiary", foodDiaryService.getFoodDiaryById(id));
        return "fooddiary/fooddiaryPage";
    }

    @PostMapping
    public String createFoodDiary(@ModelAttribute FoodDiaryDto foodDiaryDto) {
        foodDiaryService.saveFoodDiary(foodDiaryDto);
        return "redirect:/fooddiaries";
    }

    @PutMapping("/{id}")
    public String updateFoodDiary(@PathVariable Long id, @ModelAttribute FoodDiaryDto foodDiaryDto) {
        foodDiaryDto.setId(id);
        foodDiaryService.saveFoodDiary(foodDiaryDto);
        return "redirect:/fooddiaries";
    }

    @DeleteMapping("/{id}")
    public String deleteFoodDiary(@PathVariable Long id) {
        foodDiaryService.deleteFoodDiary(id);
        return "redirect:/fooddiaries";
    }
}
