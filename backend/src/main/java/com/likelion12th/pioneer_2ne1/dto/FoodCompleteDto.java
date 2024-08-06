package com.likelion12th.pioneer_2ne1.dto;

import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Locale;
import java.util.Set;

@Getter
@Setter
public class FoodCompleteDto {
    private Long id;
    private Long foodDiaryId;
    private LocalTime startEatingTime;
    private LocalTime endEatingTime;
    private String afterfeeling;
    private Set<String> symptoms;
    private String memo;
}
