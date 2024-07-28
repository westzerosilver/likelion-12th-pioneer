package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.dto.JoinDTO;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final MemberRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(MemberRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getEmail();
        String password = joinDTO.getPassword();
        String name = joinDTO.getName();

        Boolean isExist = userRepository.existsByEmail(username);

        if (isExist) {

            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
//        else if (password == null || username == null || name == null) {
//
//        }

        Member data = new Member();

        data.setEmail(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole("ROLE_ADMIN");
        data.setName(name);

        userRepository.save(data);
    }
}