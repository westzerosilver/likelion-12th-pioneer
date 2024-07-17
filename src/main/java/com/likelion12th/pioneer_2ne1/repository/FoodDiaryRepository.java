package com.likelion12th.pioneer_2ne1.repository;

import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodDiaryRepository extends JpaRepository<FoodDiary, Long> {
}