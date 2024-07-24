package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.dto.FoodDiaryDto;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import com.likelion12th.pioneer_2ne1.repository.FoodDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FoodDiaryService {
    @Autowired
    private FoodDiaryRepository foodDiaryRepository;

    public List<FoodDiaryDto> getAllFoodDiaries() {
        return foodDiaryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public FoodDiaryDto getFoodDiaryById(Long id) {
        Optional<FoodDiary> foodDiary = foodDiaryRepository.findById(id);
        return foodDiary.map(this::convertToDto).orElse(null);
    }

    public FoodDiaryDto saveFoodDiary(FoodDiaryDto foodDiaryDto) {
        FoodDiary foodDiary = convertToEntity(foodDiaryDto);
        FoodDiary savedFoodDiary = foodDiaryRepository.save(foodDiary);
        return convertToDto(savedFoodDiary);
    }

    public void deleteFoodDiary(Long id) {
        foodDiaryRepository.deleteById(id);
    }

    private FoodDiaryDto convertToDto(FoodDiary foodDiary) {
        FoodDiaryDto foodDiaryDto = new FoodDiaryDto();
        foodDiaryDto.setId(foodDiary.getId());
        foodDiaryDto.setDate(foodDiary.getDate());
        foodDiaryDto.setTime(foodDiary.getTime());
        foodDiaryDto.setEatingType(foodDiary.getEatingType().name());
        foodDiaryDto.setMenuName(foodDiary.getMenuName());
        foodDiaryDto.setPhotoUrl(foodDiary.getPhotoUrl());
        foodDiaryDto.setEatingWith(foodDiary.getEatingWith().name());
        foodDiaryDto.setEatingWhere(foodDiary.getEatingWhere().name());
        foodDiaryDto.setFeeling(foodDiary.getFeeling().name());
        return foodDiaryDto;
    }

    private FoodDiary convertToEntity(FoodDiaryDto foodDiaryDto) {
        FoodDiary foodDiary = new FoodDiary();
        foodDiary.setDate(foodDiaryDto.getDate());
        foodDiary.setTime(foodDiaryDto.getTime());
        foodDiary.setEatingType(FoodDiary.EatingType.valueOf(foodDiaryDto.getEatingType()));
        foodDiary.setMenuName(foodDiaryDto.getMenuName());
        foodDiary.setPhotoUrl(foodDiaryDto.getPhotoUrl());
        foodDiary.setEatingWith(FoodDiary.EatingWith.valueOf(foodDiaryDto.getEatingWith()));
        foodDiary.setEatingWhere(FoodDiary.EatingWhere.valueOf(foodDiaryDto.getEatingWhere()));
        foodDiary.setFeeling(FoodDiary.Feeling.valueOf(foodDiaryDto.getFeeling()));
        return foodDiary;
    }
}
