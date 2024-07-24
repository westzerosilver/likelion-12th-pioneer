package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.dto.FoodCompleteDto;
import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import com.likelion12th.pioneer_2ne1.repository.FoodCompleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FoodCompleteService {
    @Autowired
    private FoodCompleteRepository foodCompleteRepository;

    public void saveFoodComplete(FoodCompleteDto foodCompleteDto) {
        FoodComplete foodComplete = new FoodComplete();
        foodComplete.setAfterfeeling(FoodComplete.Afterfeeling.valueOf(foodCompleteDto.getAfterfeeling()));
        Set<FoodComplete.Symptom> symptoms = foodCompleteDto.getSymptoms().stream()
                .map(String::trim)
                .map(FoodComplete.Symptom::valueOf)
                .collect(Collectors.toSet());

        foodComplete.setSymptoms(symptoms);
        foodComplete.setMemo(foodCompleteDto.getMemo());
        foodCompleteRepository.save(foodComplete);
    }
}
