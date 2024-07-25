package com.likelion12th.pioneer_2ne1.repository;

import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import com.likelion12th.pioneer_2ne1.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodDiaryRepository extends JpaRepository<FoodDiary, Long> {
    List<FoodDiary> findAllByMember(Member member);
    Optional<FoodDiary> findByIdAndMember(Long id, Member member);
    void deleteByIdAndMember(Long id, Member member);
}