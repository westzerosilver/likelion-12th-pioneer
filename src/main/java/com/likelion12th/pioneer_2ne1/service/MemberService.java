package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.dto.JoinDTO;
import com.likelion12th.pioneer_2ne1.dto.MemberFormDto;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
//@RequiredArgsConstructor
public class MemberService  {
    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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


    public void deleteMember(String email) {
        Member deleteMember = memberRepository.findByEmail(email);


        if (deleteMember == null) {
            throw new IllegalArgumentException("정보를 찾을 수 없습니다.");
        }

        memberRepository.delete(deleteMember);
    }




}
