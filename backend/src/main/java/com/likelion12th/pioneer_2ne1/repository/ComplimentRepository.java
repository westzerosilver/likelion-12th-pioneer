package com.likelion12th.pioneer_2ne1.repository;

import com.likelion12th.pioneer_2ne1.entity.Compliment;
import com.likelion12th.pioneer_2ne1.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ComplimentRepository extends JpaRepository<Compliment, Long> {
    Compliment findByCreatedDateAndMember(LocalDate date, Member member);
}
