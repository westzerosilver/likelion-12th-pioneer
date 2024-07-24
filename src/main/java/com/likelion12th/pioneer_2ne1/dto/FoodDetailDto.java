package com.likelion12th.pioneer_2ne1.dto;

import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter @Setter
public class FoodDetailDto {
    private Long id;
    private LocalDate date;
    private String menuName;
    private String photoUrl;
    private String eatingWith;
    private String eatingWhere;
    private String feeling;
    private String startEatingTime;
    private String endEatingTime;
    private String afterFeeling;
    private Set<String> symptoms;
    private String memo;
}
