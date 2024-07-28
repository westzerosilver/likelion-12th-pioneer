package com.likelion12th.pioneer_2ne1.dto;

import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Getter
@Setter
public class Mypage {
    private String name;
    private String profileImgPath;

    // 가입 후 경과된 일수
    private long daysSinceJoined;

    private int complimentCnt;
    private Long totalFoodDiaryCount; // FoodDiary의 총 개수

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

    // Symptom이 BINGE인 경우의 EatingType 통계
    private Map<FoodDiary.EatingType, Long> bingeEatingTypeCounts;

    // Symptom이 BINGE인 경우의 평균 시간대
    private LocalTime averageBingeEatingTime;

    // Symptom이 BINGE인 경우의 상위 3개의 Feeling
    private Map<FoodDiary.Feeling, Long> top3BingeFeelings;

    // Symptom이 BINGE인 경우의 상위 3개의 AfterFeeling
    private Map<FoodComplete.Afterfeeling, Long> top3BingeAfterFeelings;

    // 가장 높은 Afterfeeling 점수를 가진 식사의 정보
    private FoodDiary.EatingType highestScoreAfterfeelingEatingType;
    private FoodDiary.EatingWith highestScoreAfterfeelingEatingWith;
    private FoodDiary.EatingWhere highestScoreAfterfeelingEatingWhere;
    private String highestScoreAfterfeelingMenuName;
    private FoodComplete.Afterfeeling highestScoreAfterfeeling;
}
