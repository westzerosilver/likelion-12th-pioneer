package com.likelion12th.pioneer_2ne1.dto;

import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FoodCompleteDto {
    private Long id;
    private Long foodDiaryId;
    private String afterfeeling;
    private Set<String> symptoms;
    private String memo;
}