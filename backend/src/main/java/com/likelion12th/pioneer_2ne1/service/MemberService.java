package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.dto.*;
import com.likelion12th.pioneer_2ne1.entity.DateUtils;
import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.repository.FoodCompleteRepository;
import com.likelion12th.pioneer_2ne1.repository.FoodDiaryRepository;
import com.likelion12th.pioneer_2ne1.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


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


    public MainDto getMain(String email) {
        Member member = findMember(email);

        MainDto mainDto = new MainDto();
        mainDto.setName(member.getName());
        mainDto.setDate(member.getComplementDate());

        return mainDto;
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

    private Member findMember(String email) {
        Member member= memberRepository.findByEmail(email);

        if (member == null) {
            throw new IllegalArgumentException("정보를 찾을 수 없습니다.");
        }

        return member;
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

    @Value("${uploadPath}")
    private String uploadPath;
    public void updateProfile(String email, ProfileDto profileDto, MultipartFile profileImg) throws IOException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new IllegalArgumentException("정보를 찾을 수 없습니다.");
        }

        if (!member.getName().equals( profileDto.getName())) {
            member.setName(profileDto.getName());
        }

        if(profileImg != null) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid.toString() + "_" + profileImg.getOriginalFilename();
            File itemImgFile = new File(uploadPath, fileName);
            profileImg.transferTo(itemImgFile);
            member.setProfileImg(fileName);
            member.setProfileImgPath(uploadPath+ "/" + fileName);

        }

        memberRepository.save(member);

    }


    public void deleteMember(String email) {
        Member deleteMember = memberRepository.findByEmail(email);


        if (deleteMember == null) {
            throw new IllegalArgumentException("정보를 찾을 수 없습니다.");
        }

        memberRepository.delete(deleteMember);
    }

    public void checkPassword(String email, CheckPasswordDto checkPasswordDto, PasswordEncoder passwordEncoder) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("정보를 찾을 수 없습니다.");
        }

        if (!passwordEncoder.matches(checkPasswordDto.getOldPassword(), member.getPassword())) {
            throw new  IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

    }


    public Mypage getMypage(String username) {
        Member member = memberRepository.findByEmail(username);

        if (member == null) {
            throw new IllegalArgumentException("정보를 찾을 수 없습니다.");
        }

        Long memberId = member.getId();

        LocalDate startOfWeek = DateUtils.getStartOfWeek();
        LocalDate endOfWeek = DateUtils.getEndOfWeek();

        long daysSinceJoined = member.daysSinceJoined() + 1;

        // 일주일간의 Feeling 항목별 갯수 계산
        Map<FoodDiary.Feeling, Long> weeklyFeelingCounts = calculateWeeklyFeelingCounts(memberId, startOfWeek, endOfWeek);

        Map<FoodDiary.EatingType, Long> weeklyEatingTypeCounts = calculateweeklyEatingTypeCounts(memberId, startOfWeek, endOfWeek);

        // 일주일간의 증상 항목별 갯수 계산
        Map<FoodComplete.Symptom, Long> weeklySymptomCounts = calculateWeeklySymptomCounts(memberId, startOfWeek, endOfWeek );

        // 요일별 증상의 전체 갯수 계산
        Map<String, Long> dailySymptomCounts = calculateDailySymptomCounts(memberId, startOfWeek, endOfWeek );


        // 폭식 증상 시 통계
        List<FoodDiary> diaries = getDiariesWithBingeSymptom(memberId);
        Map<FoodDiary.EatingType, Long> bingeEatingTypeCounts = getBingeEatingTypeCounts(diaries);
        LocalTime averageBingeEatingTime = getAverageBingeEatingTime();
        Map<FoodDiary.Feeling, Long> top3BingeFeelings = getTop3BingeFeelings(diaries);
        Map<FoodComplete.Afterfeeling, Long> top3BingeAfterFeelings = getTop3BingeAfterFeelings(diaries);


        Mypage mypage = new Mypage();
        mypage.setProfileImgPath(member.getProfileImgPath());
        mypage.setName(member.getName());
        mypage.setComplementDate(member.getComplementDate());
        mypage.setStartOfWeek(startOfWeek);
        mypage.setEndOfWeek(endOfWeek);
        mypage.setComplimentCnt(member.getComplimentCnt());
        mypage.setWeeklyEatingTypeCounts(weeklyEatingTypeCounts);
        mypage.setWeeklyFeelingCounts(weeklyFeelingCounts);
        mypage.setWeeklySymptomCounts(weeklySymptomCounts);
        mypage.setDailySymptomCounts(dailySymptomCounts);
        mypage.setBingeEatingTypeCounts(bingeEatingTypeCounts);
        mypage.setAverageBingeEatingTime(averageBingeEatingTime);
        mypage.setTop3BingeFeelings(top3BingeFeelings);
        mypage.setTop3BingeAfterFeelings(top3BingeAfterFeelings);
        mypage.setDaysSinceJoined(daysSinceJoined);
        mypage.setTotalFoodDiaryCount(getTotalFoodDiaryCount(memberId));

        FoodDiary highestScoreDiary = getHighestScoreAfterfeelingDiary(memberId); // 가장 높은 점수를 가진 식사 조회


        // 가장 높은 Afterfeeling 점수를 가진 식사 정보 설정
        if (highestScoreDiary != null) {
            mypage.setHighestScoreAfterfeelingEatingType(highestScoreDiary.getEatingType());
            mypage.setHighestScoreAfterfeelingEatingWith(highestScoreDiary.getEatingWith());
            mypage.setHighestScoreAfterfeelingEatingWhere(highestScoreDiary.getEatingWhere());
            mypage.setHighestScoreAfterfeelingMenuName(highestScoreDiary.getMenuName());
            mypage.setHighestScoreAfterfeeling(highestScoreDiary.getFoodComplete().getAfterfeeling());
        }



        return mypage;
    }

    public Long getTotalFoodDiaryCount(Long memberId) {
        System.out.println("getTotalFoodDiaryCount");
        return foodDiaryRepository.countByMemberId(memberId);
    }

    //ok
    private Map<FoodDiary.Feeling, Long> calculateWeeklyFeelingCounts(Long memberId, LocalDate startOfWeek, LocalDate endOfWeek ) {

        Map<FoodDiary.Feeling, Long> feelingCounts = new EnumMap<>(FoodDiary.Feeling.class);

        foodDiaryRepository.findByMemberIdAndDateBetween(memberId, startOfWeek, endOfWeek).forEach(diary -> {
            FoodDiary.Feeling feeling = diary.getFeeling();
            feelingCounts.put(feeling, feelingCounts.getOrDefault(feeling, 0L) + 1);
        });

        return feelingCounts;
    }
//ok
    private Map<FoodDiary.EatingType, Long> calculateweeklyEatingTypeCounts(Long memberId, LocalDate startOfWeek, LocalDate endOfWeek ) {

        Map<FoodDiary.EatingType, Long> eatingTypeCounts = new HashMap<>();

        foodDiaryRepository.findByMemberIdAndDateBetween(memberId, startOfWeek, endOfWeek).forEach(diary -> {
            FoodDiary.EatingType eatingType = diary.getEatingType();
            eatingTypeCounts.put(eatingType, eatingTypeCounts.getOrDefault(eatingType, 0L) + 1);
        });

        return eatingTypeCounts;
    }
//ok
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
//ok
    private Map<String, Long> calculateDailySymptomCounts(Long memberId, LocalDate startOfWeek, LocalDate endOfWeek ) {

        Map<String, Long> dailyCounts = new HashMap<>();

        foodDiaryRepository.findByMemberIdAndDateBetween(memberId, startOfWeek, endOfWeek).forEach(diary -> {
            FoodComplete foodComplete = diary.getFoodComplete();
            if (foodComplete != null) {
                LocalDate date = LocalDate.from(diary.getDate());
                String dayOfWeek = date.getDayOfWeek().name();
                dailyCounts.put(dayOfWeek, dailyCounts.getOrDefault(dayOfWeek, 0L) + foodComplete.getSymptoms().size());
            }
        });

        return dailyCounts;
    }

    public List<FoodDiary> getDiariesWithBingeSymptom(Long memberId) {
        // BINGE 증상을 가진 FoodComplete 조회
        List<FoodComplete> foodCompletes = foodCompleteRepository.findFoodCompleteBySymptoms(FoodComplete.Symptom.BINGE);

        // FoodDiary 리스트를 조회하기 위한 FoodComplete ID 리스트 생성
        List<Long> foodDiaryIds = foodCompletes.stream()
                .map(FoodComplete::getFoodDiary) // FoodDiary 가져오기
                .map(FoodDiary::getId) // FoodDiary ID 추출
                .collect(Collectors.toList());

        // FoodDiary 리스트 조회
        List<FoodDiary> diaries = foodDiaryRepository.findAllById(foodDiaryIds);

        return diaries;
    }


    public Map<FoodDiary.EatingType, Long> getBingeEatingTypeCounts(List<FoodDiary> diaries) {
        System.out.println("getBingeEatingTypeCounts---------------" + diaries);
        return diaries.stream()
                .collect(Collectors.groupingBy(FoodDiary::getEatingType, Collectors.counting()));

    }
    public LocalTime getAverageBingeEatingTime() {
        // 식사 시간 저장할 리스트
        List<LocalTime> eatingTimes = new ArrayList<>();

        List<FoodComplete> foodCompletes = foodCompleteRepository.findFoodCompleteBySymptoms(FoodComplete.Symptom.BINGE);


        for (FoodComplete foodComplete : foodCompletes) {
            LocalTime eatingTime = foodComplete.getStartEatingTime();
            if (eatingTime != null) {
                eatingTimes.add(eatingTime);
            }
        }
        System.out.println("eatingTimes: " + eatingTimes);

        // 수집한 시간이 없는 경우 null 반환
        if (eatingTimes.isEmpty()) {
            return null;
        }

        // 평균 시간 계산
        long totalSeconds = eatingTimes.stream()
                .mapToLong(LocalTime::toSecondOfDay) // 시간을 초로 변환
                .sum();

        long averageSeconds = totalSeconds / eatingTimes.size(); // 평균 초 계산
        return LocalTime.ofSecondOfDay(averageSeconds); // 평균 시간을 LocalTime으로 반환
    }

    public Map<FoodDiary.Feeling, Long> getTop3BingeFeelings(List<FoodDiary> diaries ){
        return diaries.stream()
                .collect(Collectors.groupingBy(FoodDiary::getFeeling, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<FoodDiary.Feeling, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<FoodComplete.Afterfeeling, Long> getTop3BingeAfterFeelings(List<FoodDiary> diaries) {
        System.out.println("getTop3BingeAfterFeelings---------------" + diaries);
        return diaries.stream()
                .filter(diary -> diary.getFoodComplete() != null)
                .collect(Collectors.groupingBy(diary -> diary.getFoodComplete().getAfterfeeling(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<FoodComplete.Afterfeeling, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // 가장 높은 Afterfeeling 점수를 가진 식사 조회
    public FoodDiary getHighestScoreAfterfeelingDiary(Long memberId) {
        System.out.println("getHighestScoreAfterfeelingDiary");
        List<FoodDiary> diaries = foodDiaryRepository.findByMemberId(memberId);

        System.out.println("getHighestScoreAfterfeelingDiary");
        return diaries.stream()
                .filter(diary -> diary.getFoodComplete() != null) // FoodComplete가 있는 경우만 필터링
                .max(Comparator.comparingInt(diary -> diary.getFoodComplete().getAfterfeeling().getScore())) // Afterfeeling 점수로 최대값 찾기
                .orElse(null); // 없을 경우 null 반환
    }


}
