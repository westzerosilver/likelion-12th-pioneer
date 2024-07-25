package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.dto.JoinDTO;
import com.likelion12th.pioneer_2ne1.dto.MemberFormDto;
import com.likelion12th.pioneer_2ne1.dto.Mypage;
import com.likelion12th.pioneer_2ne1.dto.PasswordDto;
import com.likelion12th.pioneer_2ne1.entity.DateUtils;
import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.repository.FoodCompleteRepository;
import com.likelion12th.pioneer_2ne1.repository.FoodDiaryRepository;
import com.likelion12th.pioneer_2ne1.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;


@Service
@Transactional
//@RequiredArgsConstructor
public class MemberService  {
    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FoodCompleteRepository foodCompleteRepository;
    private final FoodDiaryRepository foodDiaryRepository;


    public MemberService(MemberRepository memberRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         FoodCompleteRepository foodCompleteRepository,
                         FoodDiaryRepository foodDiaryRepository) {

        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.foodCompleteRepository = foodCompleteRepository;
        this.foodDiaryRepository = foodDiaryRepository;
    }





    // create member
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member member1 = memberRepository.findByEmail(member.getEmail());
        if (member1 != null) {
            throw new IllegalStateException("이미 가입된 회원입니다");
        }
    }



    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id: " + id));
    }

    public void updateMember(Long id, JoinDTO memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id: " + id));
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());

        // 비밀번호가 입력된 경우에만 비밀번호 변경
        if (memberFormDto.getPassword() != null && !memberFormDto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(memberFormDto.getPassword());
            member.setPassword(encodedPassword);
        }

        memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        Member updateMember = memberRepository.findByEmail(member.getEmail());

        if (updateMember == null) {
            throw new IllegalArgumentException("정보를 찾을 수 없습니다.");
        }

        updateMember.setName(member.getName());
        updateMember.setEmail(member.getEmail());
//        updateMember.setPassword(passwordEncoder.encode(member.getPassword()));
        updateMember.setPassword(member.getPassword());

        return memberRepository.save(updateMember);
    }

    public void updatePassword(Member member, PasswordDto passwordDto, PasswordEncoder passwordEncoder) {
        Member updateMember = memberRepository.findByEmail(member.getEmail());

        if (updateMember == null) {
            throw new IllegalArgumentException("정보를 찾을 수 없습니다.");
        }

        if (!passwordDto.getPassword().equals(passwordDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        updateMember.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        memberRepository.save(updateMember);

    }


    public void deleteMember(String email) {
        Member deleteMember = memberRepository.findByEmail(email);


        if (deleteMember == null) {
            throw new IllegalArgumentException("정보를 찾을 수 없습니다.");
        }

        memberRepository.delete(deleteMember);
    }

//    public Mypage mypage(String email) {
//        Member member = findByEmail(email);
//
//        if (member == null) {
//            throw new IllegalArgumentException("정보를 찾을 수 없습니다.");
//        }
//
//        Mypage mypage = new Mypage();
//        foodCompleteRepository
//
//
//        mypage.setName(member.getName());
//
//    }


    public Mypage getMypage(String username) {
        Member member = memberRepository.findByEmail(username);

        if (member == null) {
            throw new IllegalArgumentException("정보를 찾을 수 없습니다.");
        }

        Long memberId = member.getId();

        LocalDate startOfWeek = DateUtils.getStartOfWeek();
        LocalDate endOfWeek = DateUtils.getEndOfWeek();

        // 일주일간의 Feeling 항목별 갯수 계산
        System.out.println("calculateWeeklyFeelingCounts");
        Map<FoodDiary.Feeling, Long> weeklyFeelingCounts = calculateWeeklyFeelingCounts(memberId, startOfWeek, endOfWeek);

        Map<FoodDiary.EatingType, Long> weeklyEatingTypeCounts = calculateweeklyEatingTypeCounts(memberId, startOfWeek, endOfWeek);

        // 일주일간의 증상 항목별 갯수 계산
        System.out.println("calculateWeeklySymptomCounts");
        Map<FoodComplete.Symptom, Long> weeklySymptomCounts = calculateWeeklySymptomCounts(memberId, startOfWeek, endOfWeek );

        // 요일별 증상의 전체 갯수 계산
        System.out.println("calculateDailySymptomCounts");
        Map<String, Long> dailySymptomCounts = calculateDailySymptomCounts(memberId, startOfWeek, endOfWeek );

        Mypage mypage = new Mypage();
        mypage.setName(member.getName());
        mypage.setComplementDate(member.getComplementDate());
        mypage.setStartOfWeek(startOfWeek);
        mypage.setEndOfWeek(endOfWeek);
        mypage.setComplimentCnt(member.getComplimentCnt());
        mypage.setWeeklyEatingTypeCounts(weeklyEatingTypeCounts);
        mypage.setWeeklyFeelingCounts(weeklyFeelingCounts);
        mypage.setWeeklySymptomCounts(weeklySymptomCounts);
        mypage.setDailySymptomCounts(dailySymptomCounts);

        return mypage;
    }

    private Map<FoodDiary.Feeling, Long> calculateWeeklyFeelingCounts(Long memberId, LocalDate startOfWeek, LocalDate endOfWeek ) {

        Map<FoodDiary.Feeling, Long> feelingCounts = new EnumMap<>(FoodDiary.Feeling.class);

        foodDiaryRepository.findByMemberIdAndDateBetween(memberId, startOfWeek, endOfWeek).forEach(diary -> {
            FoodDiary.Feeling feeling = diary.getFeeling();
            feelingCounts.put(feeling, feelingCounts.getOrDefault(feeling, 0L) + 1);
        });

        return feelingCounts;
    }

    private Map<FoodDiary.EatingType, Long> calculateweeklyEatingTypeCounts(Long memberId, LocalDate startOfWeek, LocalDate endOfWeek ) {


        Map<FoodDiary.EatingType, Long> eatingTypeCounts = new HashMap<>();

        foodDiaryRepository.findByMemberIdAndDateBetween(memberId, startOfWeek, endOfWeek).forEach(diary -> {
            FoodDiary.EatingType eatingType = diary.getEatingType();
            eatingTypeCounts.put(eatingType, eatingTypeCounts.getOrDefault(eatingType, 0L) + 1);
        });

        return eatingTypeCounts;
    }

    private Map<FoodComplete.Symptom, Long> calculateWeeklySymptomCounts(Long memberId, LocalDate startOfWeek, LocalDate endOfWeek ) {

        Map<FoodComplete.Symptom, Long> symptomCounts = new EnumMap<>(FoodComplete.Symptom.class);

        foodDiaryRepository.findByMemberIdAndDateBetween(memberId, startOfWeek, endOfWeek).forEach(diary -> {
            FoodComplete foodComplete = diary.getFoodComplete();
            if (foodComplete != null) {
                foodComplete.getSymptoms().forEach(symptom -> {
                    symptomCounts.put(symptom, symptomCounts.getOrDefault(symptom, 0L) + 1);
                });
            }
        });

        return symptomCounts;
    }

    private Map<String, Long> calculateDailySymptomCounts(Long memberId, LocalDate startOfWeek, LocalDate endOfWeek ) {


        Map<String, Long> dailyCounts = new HashMap<>();

        foodDiaryRepository.findByMemberIdAndDateBetween(memberId, startOfWeek, endOfWeek).forEach(diary -> {
            FoodComplete foodComplete = diary.getFoodComplete();
            if (foodComplete != null) {
                LocalDate date = LocalDate.from(diary.getTime());
                String dayOfWeek = date.getDayOfWeek().name();
                dailyCounts.put(dayOfWeek, dailyCounts.getOrDefault(dayOfWeek, 0L) + foodComplete.getSymptoms().size());
            }
        });

        return dailyCounts;
    }


}
