package com.likelion12th.pioneer_2ne1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FoodDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime time;
    private LocalTime startEatingTime;
    private LocalTime endEatingTime;

    @Enumerated(EnumType.STRING)
    private EatingType eatingType;

    private String menuName;

    private String photoUrl;

    @Enumerated(EnumType.STRING)
    private EatingWith eatingWith;

    @Enumerated(EnumType.STRING)
    private EatingWhere eatingWhere;

    @Enumerated(EnumType.STRING)
    private Feeling feeling;

    @OneToOne(mappedBy = "foodDiary", cascade = CascadeType.ALL)
    private FoodComplete foodComplete;

    public enum EatingType {
        BREAKFAST, LUNCH, DINNER, LATENIGHT, SNACK
    }

    public enum EatingWith {
        ALONE, FRIEND, PARTNER, COLLEAGUE, OTHER
    }

    public enum EatingWhere {
        HOME, RESTAURANT, SCHOOL, WORK, OTHER
    }

    public enum Feeling {
        COMFORTABLE, HAPPY, EASY, GUILT, IRRITATE, ANXIOUS, LONELY
    }

}

