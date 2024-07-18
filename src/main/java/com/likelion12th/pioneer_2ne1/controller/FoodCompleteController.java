package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.FoodCompleteDto;
import com.likelion12th.pioneer_2ne1.service.FoodCompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foodcomplete")
public class FoodCompleteController {
    @Autowired
    private FoodCompleteService foodCompleteService;

    @GetMapping
    public String getFoodCompleteForm(Model model) {
        model.addAttribute("foodComplete", new FoodCompleteDto());
        return "fooddiary/foodcompletePage";
    }

    @PostMapping
    public String submitFoodComplete(@ModelAttribute FoodCompleteDto foodCompleteDto) {
        foodCompleteService.saveFoodComplete(foodCompleteDto);
        return "redirect:/";
    }
}
