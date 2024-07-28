package com.likelion12th.pioneer_2ne1.repository;

import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import com.likelion12th.pioneer_2ne1.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodCompleteRepository extends JpaRepository<FoodComplete, Long> {
    @Query("SELECT fd.id FROM FoodComplete fc JOIN fc.foodDiary fd JOIN fc.symptoms s WHERE fc.member.id = :memberId AND s = :symptom")
    List<Long> findFoodDiaryIdsBySymptom(@Param("memberId") Long memberId, @Param("symptom") FoodComplete.Symptom symptom);


    List<FoodComplete> findFoodCompleteBySymptoms(FoodComplete.Symptom symptom);

}

