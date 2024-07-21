package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.dto.ComplimentReqDto;
import com.likelion12th.pioneer_2ne1.entity.Compliment;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.repository.ComplimentRepository;
import com.likelion12th.pioneer_2ne1.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class ComplimentService {

    private final ComplimentRepository complimentRepository;
    private final MemberRepository memberRepository;

    public void getTodayCompliment(Member member) {
        Compliment compliment = complimentRepository.findByCreatedDateAndMember(LocalDate.now(), member);
        System.out.println("complimentRepository.findByCreatedDateAndMember(LocalDate.now(), member): " + compliment);
        if (compliment != null) {
            throw new IllegalStateException("이미 칭찬일기를 작성함");
        }
    }

    public void createNewCompliment(ComplimentReqDto complimentReqDto, String email) {
        System.out.println("ComplimentService email: " + email);
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new IllegalArgumentException("회원이 존재하지 않음");
        }

        getTodayCompliment(member);


        Compliment compliment = Compliment.createCompliment(member, complimentReqDto);

        complimentRepository.save(compliment);
    }
}
