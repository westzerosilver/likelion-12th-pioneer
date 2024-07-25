package com.likelion12th.pioneer_2ne1.dto;

import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class Mypage {
    private String name;

    private int complimentCnt;

    private LocalDate complementDate;


    private LocalDate startOfWeek;
    private LocalDate endOfWeek;


    // 일주일간 Feeling 항목별 갯수
    private Map<FoodDiary.Feeling, Long> weeklyFeelingCounts;

    // 일주일간 EatingType 항목별 갯수
    private Map<FoodDiary.EatingType, Long> weeklyEatingTypeCounts;

    // 일주일간 증상 항목별 갯수
    private Map<FoodComplete.Symptom, Long> weeklySymptomCounts;

    // 요일별 증상의 전체 갯수
    private Map<String, Long> dailySymptomCounts;
}
