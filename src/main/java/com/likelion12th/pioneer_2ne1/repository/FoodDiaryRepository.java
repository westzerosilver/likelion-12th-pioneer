package com.likelion12th.pioneer_2ne1.repository;

import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import com.likelion12th.pioneer_2ne1.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FoodDiaryRepository extends JpaRepository<FoodDiary, Long> {
    List<FoodDiary> findAllByMember(Member member);
    Optional<FoodDiary> findByIdAndMember(Long id, Member member);
    List<FoodDiary> findByMemberIdAndDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);

    List<FoodDiary> findByDateAndMember(LocalDate date, Member user);
    void deleteByIdAndMember(Long id, Member member);
    Optional<FoodDiary> findByIdAndDate(Long id, LocalDate date);

    @Query("SELECT fd FROM FoodDiary fd JOIN fd.foodComplete fc WHERE fc.member.id = :memberId AND :symptom MEMBER OF fc.symptoms")
    List<FoodDiary> findBingeEatingTypes(@Param("memberId") Long memberId, @Param("symptom") FoodComplete.Symptom symptom);
    @Query("SELECT fd FROM FoodDiary fd WHERE fd.id IN :ids")
    List<FoodDiary> findByIds(@Param("ids") List<Long> ids);
    // FoodDiary의 총 개수
    Long countByMemberId(Long memberId);

    List<FoodDiary> findByMemberId(Long memberId);


}

