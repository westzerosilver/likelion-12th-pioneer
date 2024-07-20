package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.JoinDTO;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/mypage/{id}")
    public ResponseEntity<JoinDTO> myPage(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        Member member = memberService.findById(id);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (!userDetails.getUsername().equals(member.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        JoinDTO memberFormDto = new JoinDTO();
        memberFormDto.setEmail(member.getEmail());
        memberFormDto.setName(member.getName());

        return ResponseEntity.ok(memberFormDto);
    }


    @GetMapping("/update/{id}")
    public ResponseEntity<JoinDTO> updateMember(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        Member member = memberService.findById(id);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (!userDetails.getUsername().equals(member.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        JoinDTO memberFormDto = new JoinDTO();
        memberFormDto.setEmail(member.getEmail());
        memberFormDto.setName(member.getName());

        return ResponseEntity.ok(memberFormDto);
    }



//    @PutMapping("/update/{id}")
//    public ResponseEntity<JoinDTO> updateMember(
//            @PathVariable Long id,
//            @RequestBody JoinDTO joinDTO,
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        // 회원 조회
//        Member member = memberService.findById(id);
//        if (member == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        if (!userDetails.getUsername().equals(member.getEmail())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
//
//        // 회원 정보 업데이트
//        member.setName(joinDTO.getName());
//        if (joinDTO.getPassword() != null && !joinDTO.getPassword().isEmpty()) {
//            member.setPassword(passwordEncoder.encode(joinDTO.getPassword()));
//        }
//
//        memberService.updateMember(member);
//
//        JoinDTO memberFormDto = new JoinDTO();
//        memberFormDto.setEmail(member.getEmail());
//        memberFormDto.setName(member.getName());
//
//        return ResponseEntity.ok(memberFormDto);
//    }

//    @GetMapping("/delete")
//    public String deleteMember() {
//        return "member/delete";
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMember(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {

            // 회원 조회
            Member member = memberService.findById(id);
            if (member == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (!userDetails.getUsername().equals(member.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            memberService.deleteMember(userDetails.getUsername());


            return ResponseEntity.ok("delete ok");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("delete error");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<JoinDTO> updateMember(
            @PathVariable Long id,
            @RequestBody JoinDTO updateRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 회원 조회
        Member member = memberService.findById(id);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 사용자 인증 확인 (선택 사항)
        if (!userDetails.getUsername().equals(member.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // 회원 정보 업데이트
        member.setName(updateRequest.getName());
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            // 비밀번호 암호화 후 저장
            String encodedPassword = passwordEncoder.encode(updateRequest.getPassword());
            member.setPassword(encodedPassword);
        }

        memberService.updateMember(member); // 업데이트된 회원 정보를 저장

        // 업데이트된 정보 반환
        JoinDTO memberFormDto = new JoinDTO();
        memberFormDto.setEmail(member.getEmail());
        memberFormDto.setName(member.getName());

        return ResponseEntity.ok(memberFormDto);
    }



}
