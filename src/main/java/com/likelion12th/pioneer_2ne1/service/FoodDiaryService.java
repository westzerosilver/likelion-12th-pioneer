package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.config.SecurityUtil;
import com.likelion12th.pioneer_2ne1.dto.FoodDiaryDto;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.repository.FoodDiaryRepository;
import com.likelion12th.pioneer_2ne1.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FoodDiaryService {
    @Autowired
    private FoodDiaryRepository foodDiaryRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<FoodDiaryDto> getAllFoodDiariesForCurrentUser() {
        String membername = SecurityUtil.getCurrentUsername();
        Member currentMember = memberRepository.findByEmail(membername);
        return foodDiaryRepository.findAllByMember(currentMember).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FoodDiaryDto> getFoodDiariesByDateForCurrentUser(LocalDate date) {
        // 현재 로그인된 사용자와 날짜에 따라 식사 일기 목록을 조회
        return foodDiaryRepository.findByDateAndMember(date, getCurrentUser()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public FoodDiaryDto saveFoodDiary(FoodDiaryDto foodDiaryDto) {
        FoodDiary foodDiary = convertToEntity(foodDiaryDto);
        FoodDiary savedFoodDiary = foodDiaryRepository.save(foodDiary);
        return convertToDto(savedFoodDiary);
    }

    public FoodDiaryDto saveFoodDiary(FoodDiaryDto foodDiaryDto, String email) {

        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다.");
        }


        FoodDiary foodDiary = convertToEntity(foodDiaryDto);
        foodDiary.setMember(member);
        FoodDiary savedFoodDiary = foodDiaryRepository.save(foodDiary);
        return convertToDto(savedFoodDiary);
    }


    public void deleteFoodDiary(Long id) {
        foodDiaryRepository.deleteById(id);
    }

    private Member getCurrentUser() {
        // SecurityContext에서 현재 인증 정보 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 있고 사용자 정보가 UserDetails 타입일 때 처리
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // 현재 로그인한 사용자 이름 얻음
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

            // 사용자 이메일로 Member 찾음
            Member member = memberRepository.findByEmail(username);

            // Member가 null인 경우 예외
            if (member != null) {
                return member;
            } else {
                throw new RuntimeException("User not found with email: " + username);
            }
        }

        // 인증 정보가 없거나 사용자 정보가 UserDetails 타입이 아닌 경우 예외
        throw new RuntimeException("User not authenticated");
    }


    private FoodDiaryDto convertToDto(FoodDiary foodDiary) {
        FoodDiaryDto foodDiaryDto = new FoodDiaryDto();
        foodDiaryDto.setId(foodDiary.getId());
        foodDiaryDto.setDate(foodDiary.getDate());
        foodDiaryDto.setTime(foodDiary.getTime());
        foodDiaryDto.setEatingType(foodDiary.getEatingType().name());
        foodDiaryDto.setMenuName(foodDiary.getMenuName());
        foodDiaryDto.setPhotoUrl(foodDiary.getPhotoUrl());
        foodDiaryDto.setEatingWith(foodDiary.getEatingWith().name());
        foodDiaryDto.setEatingWhere(foodDiary.getEatingWhere().name());
        foodDiaryDto.setFeeling(foodDiary.getFeeling().name());
        return foodDiaryDto;
    }

    private FoodDiary convertToEntity(FoodDiaryDto foodDiaryDto) {
        FoodDiary foodDiary = new FoodDiary();
        foodDiary.setDate(foodDiaryDto.getDate());
        foodDiary.setTime(foodDiaryDto.getTime());
        foodDiary.setEatingType(FoodDiary.EatingType.valueOf(foodDiaryDto.getEatingType()));
        foodDiary.setMenuName(foodDiaryDto.getMenuName());
        foodDiary.setPhotoUrl(foodDiaryDto.getPhotoUrl());
        foodDiary.setEatingWith(FoodDiary.EatingWith.valueOf(foodDiaryDto.getEatingWith()));
        foodDiary.setEatingWhere(FoodDiary.EatingWhere.valueOf(foodDiaryDto.getEatingWhere()));
        foodDiary.setFeeling(FoodDiary.Feeling.valueOf(foodDiaryDto.getFeeling()));
        return foodDiary;
    }
}
