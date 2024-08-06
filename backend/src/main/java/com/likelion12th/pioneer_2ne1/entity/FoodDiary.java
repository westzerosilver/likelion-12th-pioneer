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
@NoArgsConstructor
public class FoodDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private LocalDate date;
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private EatingType eatingType;

    private String menuName;

    private String photoUrl;
    private String photoUrlPath;

    @Enumerated(EnumType.STRING)
    private EatingWith eatingWith;

    private String eatingWithOther;

    @Enumerated(EnumType.STRING)
    private EatingWhere eatingWhere;

    private String eatingWhereOther;

    @Enumerated(EnumType.STRING)
    private Feeling feeling;


    @OneToOne(mappedBy = "foodDiary", cascade = CascadeType.ALL, orphanRemoval = true)
    private FoodComplete foodComplete;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public enum EatingType {
        BREAKFAST, LUNCH, DINNER, LATENIGHT, SNACK
    }

    public enum EatingWith {
        ALONE, FRIEND, FAMILY, PARTNER, COLLEAGUE, OTHER
    }

    public enum EatingWhere {
        HOME, RESTAURANT, SCHOOL, WORK, OTHER
    }

    public enum Feeling {
        COMFORTABLE, HAPPY, EASY, GUILT, IRRITATE, ANXIOUS, LONELY
    }

}
