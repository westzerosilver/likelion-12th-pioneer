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
@NoArgsConstructor
public class FoodComplete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Afterfeeling afterfeeling;

    @ElementCollection(targetClass = Symptom.class)
    @CollectionTable(name = "food_complete_symptom", joinColumns = @JoinColumn(name = "food_complete_id"))
    @Enumerated(EnumType.STRING)
    private Set<Symptom> symptoms;

    private String memo;

    @OneToOne
    @JoinColumn(name = "food_diary_id")
    private FoodDiary foodDiary;

    public enum Symptom {
        VOMIT, MEDICINE, BINGE, REDUCE, SPIT, DIETMEDICINE, EXERCISE, OTHER, NOTHING
    }

    public enum Afterfeeling {
        COMFORTABLE(6),
        HAPPY(7),
        EASY(5),
        GUILT(1),
        IRRITATE(3),
        ANXIOUS(2),
        LONELY(4);

        private final int score;

        Afterfeeling(int score) {
            this.score = score;
        }

        public int getScore() {
            return score;
        }
    }

}
