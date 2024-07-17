package com.likelion12th.pioneer_2ne1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

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

    private String menuName;

    private String photoUrl;

    @Enumerated(EnumType.STRING)
    private EatingWith eatingWith;

    @Enumerated(EnumType.STRING)
    private EatingWhere eatingWhere;

    @Enumerated(EnumType.STRING)
    private Feeling feeling;


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

