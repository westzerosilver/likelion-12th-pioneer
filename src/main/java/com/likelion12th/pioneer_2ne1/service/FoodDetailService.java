package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.dto.FoodDetailDto;
import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import com.likelion12th.pioneer_2ne1.repository.FoodCompleteRepository;
import com.likelion12th.pioneer_2ne1.repository.FoodDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FoodDetailService {
    @Autowired
    private FoodDiaryRepository foodDiaryRepository;

    @Autowired
    private FoodCompleteRepository foodCompleteRepository;

    public FoodDetailDto getFoodDiaryDetailById(LocalDate date, Long id) {
        Optional<FoodDiary> foodDiary = foodDiaryRepository.findByIdAndDate(id, date);
        if (foodDiary.isPresent()) {
            Optional<FoodComplete> foodComplete = foodCompleteRepository.findById(id);
            if (foodComplete.isPresent()) {
                return convertToDetailDto(foodDiary.get(), foodComplete.get());
            }
        }
        return null;
    }

    public FoodDetailDto updateFoodDiary(LocalDate date, Long id, FoodDetailDto foodDetailDto) {
        Optional<FoodDiary> optionalFoodDiary = foodDiaryRepository.findByIdAndDate(id, date);
        Optional<FoodComplete> optionalFoodComplete = foodCompleteRepository.findById(id);

        if (optionalFoodDiary.isPresent() && optionalFoodComplete.isPresent()) {
            FoodDiary foodDiary = optionalFoodDiary.get();
            FoodComplete foodComplete = optionalFoodComplete.get();

            foodDiary.setDate(date);
            foodDiary.setStartEatingTime(parseTime(foodDetailDto.getStartEatingTime()));
            foodDiary.setEndEatingTime(parseTime(foodDetailDto.getEndEatingTime()));
            foodDiary.setMenuName(foodDetailDto.getMenuName());
            foodDiary.setPhotoUrl(foodDetailDto.getPhotoUrl());
            foodDiary.setEatingWith(FoodDiary.EatingWith.valueOf(foodDetailDto.getEatingWith()));
            foodDiary.setEatingWhere(FoodDiary.EatingWhere.valueOf(foodDetailDto.getEatingWhere()));
            foodDiary.setFeeling(FoodDiary.Feeling.valueOf(foodDetailDto.getFeeling()));

            foodComplete.setAfterfeeling(FoodComplete.Afterfeeling.valueOf(foodDetailDto.getAfterFeeling()));
            foodComplete.setSymptoms(parseSymptoms(foodDetailDto.getSymptoms()));
            foodComplete.setMemo(foodDetailDto.getMemo());

            FoodDiary updatedFoodDiary = foodDiaryRepository.save(foodDiary);
            foodCompleteRepository.save(foodComplete);

            return convertToDetailDto(updatedFoodDiary, foodComplete);
        }
        return null;
    }

    public boolean deleteFoodDiary(LocalDate date, Long id) {
        Optional<FoodDiary> optionalFoodDiary = foodDiaryRepository.findByIdAndDate(id, date);
        Optional<FoodComplete> optionalFoodComplete = foodCompleteRepository.findById(id);

        if (optionalFoodDiary.isPresent() && optionalFoodComplete.isPresent()) {
            foodDiaryRepository.deleteById(id);
            foodCompleteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private FoodDetailDto convertToDetailDto(FoodDiary foodDiary, FoodComplete foodComplete) {
        FoodDetailDto foodDetailDto = new FoodDetailDto();
        foodDetailDto.setId(foodDiary.getId());
        foodDetailDto.setDate(foodDiary.getDate());
        foodDetailDto.setMenuName(foodDiary.getMenuName());
        foodDetailDto.setPhotoUrl(foodDiary.getPhotoUrl());
        foodDetailDto.setEatingWith(foodDiary.getEatingWith().name());
        foodDetailDto.setEatingWhere(foodDiary.getEatingWhere().name());
        foodDetailDto.setFeeling(foodDiary.getFeeling().name());

        if (foodDiary.getStartEatingTime() != null) {
            foodDetailDto.setStartEatingTime(foodDiary.getStartEatingTime().toString());
        }
        if (foodDiary.getEndEatingTime() != null) {
            foodDetailDto.setEndEatingTime(foodDiary.getEndEatingTime().toString());
        }
        if (foodComplete.getAfterfeeling() != null) {
            foodDetailDto.setAfterFeeling(foodComplete.getAfterfeeling().name());
        }
        if (foodComplete.getSymptoms() != null) {
            foodDetailDto.setSymptoms(foodComplete.getSymptoms().stream()
                    .map(FoodComplete.Symptom::name)
                    .collect(Collectors.toSet()));
        }
        foodDetailDto.setMemo(foodComplete.getMemo());

        return foodDetailDto;
    }

    private LocalTime parseTime(String time) {
        return time != null ? LocalTime.parse(time) : null;
    }

    private Set<FoodComplete.Symptom> parseSymptoms(Set<String> symptoms) {
        if (symptoms != null && !symptoms.isEmpty()) {
            return symptoms.stream()
                    .map(String::trim)
                    .map(FoodComplete.Symptom::valueOf)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
