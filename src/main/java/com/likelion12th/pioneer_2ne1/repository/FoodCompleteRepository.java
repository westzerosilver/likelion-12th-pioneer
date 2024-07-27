package com.likelion12th.pioneer_2ne1.repository;

import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodCompleteRepository extends JpaRepository<FoodComplete, Long> {
}

