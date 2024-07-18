package com.likelion12th.pioneer_2ne1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FoodComplete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Feeling feeling;

    @ElementCollection(targetClass = Symptom.class)
    @CollectionTable(name = "food_complete_symptom", joinColumns = @JoinColumn(name = "food_complete_id"))
    @Enumerated(EnumType.STRING)
    private Set<Symptom> symptoms;

    private String memo;

    public enum Feeling {
        COMFORTABLE, HAPPY, EASY, GUILT, IRRITATE, ANXIOUS, LONELY
    }

    public enum Symptom {
        VOMIT, MEDICINE, BINGE, REDUCE, SPIT, DIETMEDICINE, EXERCISE, OTHER, NOTHING
    }
}