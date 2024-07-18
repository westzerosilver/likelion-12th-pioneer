package com.likelion12th.pioneer_2ne1.dto;

import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FoodCompleteDto {
    private FoodComplete.Feeling feeling;
    private Set<FoodComplete.Symptom> symptoms;
    private String memo;
}