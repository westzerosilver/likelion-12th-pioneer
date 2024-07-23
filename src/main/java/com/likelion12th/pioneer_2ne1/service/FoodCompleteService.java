package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.dto.FoodCompleteDto;
import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.repository.FoodCompleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodCompleteService {
    @Autowired
    private FoodCompleteRepository foodCompleteRepository;

    public void saveFoodComplete(FoodCompleteDto foodCompleteDto) {
        FoodComplete foodComplete = new FoodComplete();
        foodComplete.setFeeling(foodCompleteDto.getFeeling());
        foodComplete.setSymptoms(foodCompleteDto.getSymptoms());
        foodComplete.setMemo(foodCompleteDto.getMemo());
        foodCompleteRepository.save(foodComplete);
    }
}
