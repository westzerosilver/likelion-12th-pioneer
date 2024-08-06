package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.dto.FoodCompleteDto;
import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import com.likelion12th.pioneer_2ne1.repository.FoodCompleteRepository;
import com.likelion12th.pioneer_2ne1.repository.FoodDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FoodCompleteService {
    @Autowired
    private FoodCompleteRepository foodCompleteRepository;

    @Autowired
    private FoodDiaryRepository foodDiaryRepository;


    public void saveFoodComplete(FoodCompleteDto foodCompleteDto) {
        FoodComplete foodComplete = new FoodComplete();
        foodComplete.setAfterfeeling(FoodComplete.Afterfeeling.valueOf(foodCompleteDto.getAfterfeeling()));
        foodComplete.setStartEatingTime(foodCompleteDto.getStartEatingTime());
        foodComplete.setEndEatingTime(foodCompleteDto.getEndEatingTime());
        Set<FoodComplete.Symptom> symptoms = foodCompleteDto.getSymptoms().stream()
                .map(String::trim)
                .map(FoodComplete.Symptom::valueOf)
                .collect(Collectors.toSet());
        foodComplete.setSymptoms(symptoms);
        foodComplete.setMemo(foodCompleteDto.getMemo());

        Optional<FoodDiary> optionalFoodDiary = foodDiaryRepository.findById(foodCompleteDto.getFoodDiaryId());
        if (optionalFoodDiary.isPresent()) {
            FoodDiary foodDiary = optionalFoodDiary.get();
            foodComplete.setFoodDiary(foodDiary);
            foodCompleteRepository.save(foodComplete);

            foodDiary.setFoodComplete(foodComplete);
            foodDiaryRepository.save(foodDiary);
        } else {
            throw new RuntimeException("FoodDiary not found with id: " + foodCompleteDto.getFoodDiaryId());
        }
    }
}
